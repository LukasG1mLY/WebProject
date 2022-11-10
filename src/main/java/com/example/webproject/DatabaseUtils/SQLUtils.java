package com.example.webproject.DatabaseUtils;

import java.sql.*;

public class SQLUtils {

    private Connection con;
    private PreparedStatement addStatementVariables(String pStatement, Object... pSet) throws SQLException {
        PreparedStatement lStmt = this.con.prepareStatement(pStatement);
        this.addStatementVariables(lStmt, pSet);
        return lStmt;
    }
    private void addStatementVariables(PreparedStatement pStatement, Object... pSet) throws SQLException {
        if (pSet != null) {
            for (int i = 0; i < pSet.length; ++i) {
                if (Blob.class.equals(pSet[i].getClass())) {
                    pStatement.setBlob(i + 1, (Blob) pSet[i]);
                } else if (byte[].class.equals(pSet[i].getClass())) {
                    pStatement.setBytes(i + 1, (byte[]) pSet[i]);
                } else if (Byte.TYPE.equals(pSet[i].getClass())) {
                    pStatement.setByte(i + 1, (Byte) pSet[i]);
                } else if (String.class.equals(pSet[i].getClass())) {
                    pStatement.setString(i + 1, (String) pSet[i]);
                } else if (Integer.class.equals(pSet[i].getClass())) {
                    pStatement.setInt(i + 1, (Integer) pSet[i]);
                } else if (Long.class.equals(pSet[i].getClass())) {
                    pStatement.setLong(i + 1, (Long) pSet[i]);
                } else if (Boolean.class.equals(pSet[i].getClass())) {
                    pStatement.setBoolean(i + 1, (Boolean) pSet[i]);
                } else if (Double.class.equals(pSet[i].getClass())) {
                    pStatement.setDouble(i + 1, (Double) pSet[i]);
                } else if (Date.class.equals(pSet[i].getClass())) {
                    pStatement.setDate(i + 1, (Date) pSet[i]);
                }
            }

        }
    }
    public ResultSet onQuery(String pStatement) throws SQLException {
        return con.createStatement().executeQuery(pStatement);
    }

    public ResultSet onQuery(String pStatement, Object... pSet) throws SQLException {
        return addStatementVariables(pStatement, pSet).executeQuery();

    }
    public void onExecute(String pStatement) throws SQLException {
        con.createStatement().execute(pStatement);
        con.commit();
    }

    public void onExecute(String pStatement, Object... pSet) throws SQLException {
        addStatementVariables(pStatement, pSet).execute();
        con.commit();
    }

    public void setDatabase(String pDatabaseUrl, String pUser, String pPassword) throws SQLException {
        this.con = DriverManager.getConnection(pDatabaseUrl, pUser, pPassword);
        this.con.setAutoCommit(false);
    }

}
