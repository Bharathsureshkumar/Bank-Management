package com.bank.application.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceUtils {

    private static final Logger log = LoggerFactory.getLogger(ResourceUtils.class);

    public static String getResourceContent(Resource resource) throws IOException {

        InputStream input = resource.getInputStream();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
        String fileContent = "";
        String line;
        while ((line = buffer.readLine()) != null) {
            fileContent += line;
        }

        buffer.close();
        return fileContent;
    }
}
