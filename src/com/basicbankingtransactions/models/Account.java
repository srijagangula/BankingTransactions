package com.basicbankingtransactions.models;

public class Account {
	
	public static final double ACCOUNT_THRESHOLD_AMOUNT = 100;
	
	public static final String ACCOUNT_ACTIVE = "ACTIVE";
	public static final String ACCOUNT_INACTIVE = "INACTIVE";
	
	private int accountNumber;//primary key
	private double balance;
	private String password;
	private String status;
	private String accountHolderName;
	private String mobileNumber;
	private String email;
	
	public Account(double balance, String password, String status, String accountHolderName, String mobileNumber,
			String email) {
		super();
		this.balance = balance;
		this.password = password;
		this.status = status;
		this.accountHolderName = accountHolderName;
		this.mobileNumber = mobileNumber;
		this.email = email;
	}

	public Account(int accountNumber, double balance, String password, String status, String accountHolderName,
			String mobileNumber, String email) {
		super();
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.password = password;
		this.status = status;
		this.accountHolderName = accountHolderName;
		this.mobileNumber = mobileNumber;
		this.email = email;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", balance=" + balance + ", password=" + password
				+ ", status=" + status + ", accountHolderName=" + accountHolderName + ", mobileNumber=" + mobileNumber
				+ ", email=" + email + "]";
	}
	
}
