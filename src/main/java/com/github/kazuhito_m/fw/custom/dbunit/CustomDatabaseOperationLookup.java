package com.github.kazuhito_m.fw.custom.dbunit;

import org.dbunit.operation.DatabaseOperation;

import com.github.springtestdbunit.operation.DatabaseOperationLookup;

public class CustomDatabaseOperationLookup implements DatabaseOperationLookup {

    @Override
    public DatabaseOperation get(com.github.springtestdbunit.annotation.DatabaseOperation operation) {
        switch (operation) {
        case UPDATE:
            return org.dbunit.operation.DatabaseOperation.UPDATE;
        case INSERT:
            return org.dbunit.operation.DatabaseOperation.INSERT;
        case REFRESH:
            return org.dbunit.operation.DatabaseOperation.REFRESH;
        case DELETE:
            return org.dbunit.operation.DatabaseOperation.DELETE;
        case DELETE_ALL:
            return org.dbunit.operation.DatabaseOperation.DELETE_ALL;
        case TRUNCATE_TABLE:
            return org.dbunit.operation.DatabaseOperation.TRUNCATE_TABLE;
        case CLEAN_INSERT:
            return CleanInsertWithoutConstraintOperation.CLEAN_INSERT_WITHOUT_CONSTRAINT;
        default:
            return null;
        }
    }
}
