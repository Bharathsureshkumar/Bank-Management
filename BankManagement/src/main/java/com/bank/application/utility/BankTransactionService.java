package com.bank.application.utility;

import com.bank.application.persistance.Account;
import com.bank.application.persistance.Transaction;
import com.bank.application.repository.AccountRepo;
import com.bank.application.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class BankTransactionService {

    @Autowired
    AccountRepo accountRepo;
    @Autowired
    TransactionRepo transactionRepo;

    public List<Transaction> makeTransaction(Map<String, String> trncDetails) {

        boolean transactionFlag = true;

        String payee_ac_number = trncDetails.get("payee-account-number");
        String payer_ac_number = trncDetails.get("payer-account-number");
        Double amount = Double.parseDouble(trncDetails.get("amount"));
        //fetching account details from DB.
        Account payee_account = accountRepo.findByAccountNumber(payee_ac_number).get(0);
        Account payer_account = accountRepo.findByAccountNumber(payer_ac_number).get(0);

        Double payee_pre_balance = Double.parseDouble(payee_account.getAccountBalance());
        Double payer_pre_balance = Double.parseDouble(payer_account.getAccountBalance());

        Double payee_new_balance = payee_pre_balance - amount;
        Double payer_new_balance = payer_pre_balance + amount;

        payee_account.setAccountBalance(payee_new_balance + "");
        payer_account.setAccountBalance(payer_new_balance + "");

        //making Transaction Objects.
        Transaction payeeTransaction = registerTransfer(trncDetails,payee_account,payer_account, payee_pre_balance, payee_new_balance, "Debit");
        Transaction payerTransaction = registerTransfer(trncDetails,payer_account,payee_account, payer_pre_balance, payer_new_balance, "Credit");

//        Storing transactions to return result.

        List<Transaction> transactions = new ArrayList<>();

        //Adding the transactions to the previous list
        transactions.add(payeeTransaction);
        payee_account.getTransactions().add(payeeTransaction);
        transactions.add(payerTransaction);
        payer_account.getTransactions().add(payerTransaction);

        payeeTransaction.setAccount(payee_account);
        payerTransaction.setAccount(payer_account);
        transactionRepo.save(payeeTransaction);
        transactionRepo.save(payerTransaction);
        accountRepo.save(payee_account);
        accountRepo.save(payer_account);


        return transactions;
    }

    public Transaction registerTransfer(Map<String, String> trncDetails,
                                        Account fromAccount,Account toAccount,
                                        Double pre_balance, Double new_balance, String type){

        Transaction transaction = new Transaction();

        transaction.setType(type);
        transaction.setDetails(trncDetails.get("details"));
        transaction.setAmount(Double.parseDouble(trncDetails.get("amount")+""));
        transaction.setPreviousBalance(pre_balance);
        transaction.setCurrentBalance(new_balance);
        if(type.equalsIgnoreCase("Debit")){
            transaction.setSendTo(toAccount.getAccountNumber());
            transaction.setReceivedFrom("-");
        }
        else if(type.equalsIgnoreCase("Credit")){
            transaction.setReceivedFrom(fromAccount.getAccountNumber());
            transaction.setSendTo("-");

        }
        transaction.setStatus("success");
        return transaction;
    }

}
