package com.github.kazuhito_m.fw.custom.dbunit.mysql;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.statement.IBatchStatement;
import org.dbunit.database.statement.IPreparedBatchStatement;
import org.dbunit.database.statement.IStatementFactory;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.AbstractOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MySQL専用「DBの制約(CONSTRAINT)をON/OFFするオペレーションクラス。</br>
 * 
 * コンストラクタにて制御し,true:on, false:off
 */
public class ConstraintSwitchOperation extends AbstractOperation {

    private static final Logger logger = LoggerFactory.getLogger(ConstraintSwitchOperation.class);

    private final boolean withConstraint;

    public ConstraintSwitchOperation(boolean withConstraint) {
        this.withConstraint = withConstraint;
    }

    @Override
    public void execute(IDatabaseConnection connection, IDataSet dataSet) throws DatabaseUnitException, SQLException {
        Statement statement = null;
        String sql = "SET FOREIGN_KEY_CHECKS = " + (withConstraint ? "1" : "0") + ";";
        try {
            // DBUnit絡みではなく、生のJDBCのコネクションを取得。
            Connection con = connection.getConnection();
            statement = con.createStatement();
            // その場で「制約解除」のSQLを実行する。
            logger.trace("execute statement '" + sql + "'");
            statement.execute(sql);
        } catch (SQLException e) {
            final String msg = "Exception processing " + sql;
            throw new DatabaseUnitException(msg, e);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

}
