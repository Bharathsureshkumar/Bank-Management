package com.bank.application.validators;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class Header {
        public Field account;
        public Field amount;
        public Field details;

        public Field panCard;

        public Field getAccount() {
            return account;
        }

        public void setAccount(Field account) {
            this.account = account;
        }

        public Field getAmount() {
            return amount;
        }

        public void setAmount(Field amount) {
            this.amount = amount;
        }

        public Field getDetails() {
            return details;
        }

        public void setDetails(Field details) {
            this.details = details;
        }
}
