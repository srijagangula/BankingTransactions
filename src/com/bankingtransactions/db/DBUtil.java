package com.bankingtransactions.db;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	public static void createDB() {
		Connection con = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:banking_database.sqlite");
			System.out.println("Opened database successfully");

			stmt = con.createStatement();
			
			String accountTableCreateQuery = "CREATE TABLE IF NOT EXISTS accounts "
					+ "(account_number INTEGER PRIMARY KEY autoincrement,"
					+ " balance REAL(8,2) NOT NULL, "
					+ "password INTEGER NOT NULL, "
					+ "status CHAR(10) NOT NULL, "
					+ "account_holder_name CHAR(50) NOT NULL,"
					+ "mobile_number CHAR(10) NOT NULL,"
					+ "email CHAR(50) NOT NULL)";
			
			String bankTableCreateQuery = "CREATE TABLE IF NOT EXISTS banks (id INTEGER PRIMARY KEY autoincrement,"
					+ "name CHAR(50) NOT NULL)";
			
			String bankAccountTableCreateQuery = "CREATE TABLE IF NOT EXISTS bank_accounts (bank_id INTEGER NOT NULL, account_number INTEGER NOT NULL,"
					+ "FOREIGN KEY (bank_id) REFERENCES banks(id), FOREIGN KEY (account_number) REFERENCES accounts(account_number),"
					+ "UNIQUE (bank_id, account_number))";
			
			stmt.executeUpdate(accountTableCreateQuery);
			stmt.executeUpdate(bankTableCreateQuery);
			stmt.executeUpdate(bankAccountTableCreateQuery);
			stmt.close();
			con.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Tables created successfully");
	}

	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:banking_database.sqlite");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
}
