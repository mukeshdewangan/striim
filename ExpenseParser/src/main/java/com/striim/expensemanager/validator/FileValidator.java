package com.striim.expensemanager.validator;

public interface FileValidator {
    boolean isValidFile(String filePath, String... schemaFile);

}
