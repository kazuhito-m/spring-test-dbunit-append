package com.github.kazuhito_m.fw.custom.dbunit;

import org.dbunit.operation.CompositeOperation;
import org.dbunit.operation.DatabaseOperation;

import com.github.kazuhito_m.fw.custom.dbunit.mysql.ConstraintSwitchOperation;

public class CleanInsertWithoutConstraintOperation extends CompositeOperation {

	public static final DatabaseOperation CLEAN_INSERT_WITHOUT_CONSTRAINT = new CleanInsertWithoutConstraintOperation();

	public CleanInsertWithoutConstraintOperation() {
		super(new DatabaseOperation[] { new ConstraintSwitchOperation(false),
				CLEAN_INSERT, new ConstraintSwitchOperation(true) });
	}

}
