package com.bankingtransactions.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bankingtransactions.db.DBUtil;
import com.bankingtransactions.exceptions.InsufficientBalanceException;
import com.bankingtransactions.models.Account;

public class AccountDao {
	private Connection con;
	private PreparedStatement ps;
	private String sql;
	private ResultSet rs;
	
	public Account insert(Account account) {
		Account insertedAccount = null;
		
		sql = "INSERT INTO "
				+ "accounts (balance, password, status, account_holder_name, mobile_number, email)"
				+ " VALUES (?, ?, ?, ?, ?, ?)";
		
		if(account.getPassword() == null) {
			account.setPassword("1234");
		}
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setDouble(1, account.getBalance());
			ps.setString(2, account.getPassword());
			ps.setString(3, account.getStatus());
			ps.setString(4, account.getAccountHolderName());
			ps.setString(5, account.getMobileNumber());
			ps.setString(6, account.getEmail());
			
			int rowInserted = ps.executeUpdate();
			
			if (rowInserted > 0) {
				account.setAccountNumber(getInsertedAccountNumber(account));
				insertedAccount = account;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return insertedAccount;
	}
	
	public double deposit(Account account, double amount) {
		double updatedBalance = account.getBalance();
		
		sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setDouble(1, account.getBalance() + amount);
			ps.setInt(2, account.getAccountNumber());
			
			int rowUpdated = ps.executeUpdate();
			
			if (rowUpdated > 0) {
				updatedBalance = account.getBalance() + amount;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return updatedBalance;
	}
	
	public double withdraw(Account account, double amount) throws InsufficientBalanceException {
		double updatedBalance = account.getBalance();
		
		if (amount > account.getBalance()) {
			throw new InsufficientBalanceException("INSUFFICIENT FUNDS");
		}
		
		sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setDouble(1, account.getBalance() - amount);
			ps.setInt(2, account.getAccountNumber());
			
			int rowUpdated = ps.executeUpdate();
			
			if (rowUpdated > 0) {
				updatedBalance = account.getBalance() - amount;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return updatedBalance;
	}
	
	public Account getAccount(int accountNumber) {
		Account account = null;
		
		sql = "SELECT * FROM accounts WHERE account_number = ?";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setInt(1, accountNumber);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				double balance = rs.getDouble(2);
				String password = rs.getString(3);
				String status = rs.getString(4);
				String accountHolderName = rs.getString(5);
				String mobileNumber = rs.getString(6);
				String email = rs.getString(7);
				
				account = new Account(accountNumber, balance, password, status, accountHolderName, mobileNumber, email);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return account;
	}
	
	public boolean transfer(Account fromAccount, Account toAccount, double amount) throws InsufficientBalanceException {
		boolean transactionSuccess = false;
		
		withdraw(fromAccount, amount);
		deposit(toAccount, amount);
		transactionSuccess = true;
		
		return transactionSuccess;
	}
	
	private int getInsertedAccountNumber(Account account) {
		int insertedAccountNumber = 0;
		
		sql = "SELECT account_number FROM accounts "
				+ "WHERE "
				+ "balance = ? AND "
				+ "password = ? AND "
				+ "status = ? AND "
				+ "account_holder_name = ? AND "
				+ "mobile_number = ? AND "
				+ "email = ?";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setDouble(1, account.getBalance());
			ps.setString(2, account.getPassword());
			ps.setString(3, account.getStatus());
			ps.setString(4, account.getAccountHolderName());
			ps.setString(5, account.getMobileNumber());
			ps.setString(6, account.getEmail());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				insertedAccountNumber = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return insertedAccountNumber;
	}

	public boolean activateAccount(Account account) {
		boolean isActivated = false;
		
		sql = "UPDATE accounts SET status = ? WHERE account_number = ?";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setString(1, Account.ACCOUNT_ACTIVE);
			ps.setInt(2, account.getAccountNumber());
			
			int rowUpdated = ps.executeUpdate();
			
			if (rowUpdated > 0) {
				isActivated = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return isActivated;
	}
	
	public boolean deactivateAccount(Account account) {
		boolean isDeactivated = false;
		
		sql = "UPDATE accounts SET status = ? WHERE account_number = ?";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setString(1, Account.ACCOUNT_INACTIVE);
			ps.setInt(2, account.getAccountNumber());
			
			int rowUpdated = ps.executeUpdate();
			
			if (rowUpdated > 0) {
				isDeactivated = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return isDeactivated;
	}
	
	public ArrayList<Account> getAllAccounts() {
		ArrayList<Account> allAccounts = new ArrayList<Account>();
		
		sql = "SELECT * FROM accounts";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				int accountNumber = rs.getInt(1);
				double balance = rs.getDouble(2);
				String password = rs.getString(3);
				String status = rs.getString(4);
				String accountHolderName = rs.getString(5);
				String mobileNumber = rs.getString(6);
				String email = rs.getString(7);
				Account account = new Account(accountNumber, balance, password, status, accountHolderName, mobileNumber, email);
				
				allAccounts.add(account);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return allAccounts;
	}
	
	public boolean updatePassword(Account account, String password) {
		boolean isUpdated = false;
		
		sql = "UPDATE accounts SET password = ? WHERE account_number = ?";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setString(1, password);
			ps.setInt(2, account.getAccountNumber());
			
			int rowUpdated = ps.executeUpdate();
			
			if (rowUpdated > 0) {
				isUpdated = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return isUpdated;
	}
}
