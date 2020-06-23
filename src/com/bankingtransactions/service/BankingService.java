package com.bankingtransactions.service;

import java.util.ArrayList;

import com.bankingtransactions.dao.AccountDao;
import com.bankingtransactions.exceptions.InsufficientBalanceException;
import com.bankingtransactions.models.Account;

public class BankingService {
	AccountDao accountDao = new AccountDao();
	
	public Account insert(Account account) {
		return accountDao.insert(account);
	}
	
	public double deposit(Account account, double amount) {
		return accountDao.deposit(account, amount);
	}
	
	public double withdraw(Account account, double amount) throws InsufficientBalanceException {
		return accountDao.withdraw(account, amount);
	}
	
	public boolean transfer(Account fromAccount, Account toAccount, double amount) throws InsufficientBalanceException {
		return accountDao.transfer(fromAccount, toAccount, amount);
	}
	
	public Account getAccount(int accountNumber) {
		return accountDao.getAccount(accountNumber);
	}
	
	public boolean activateAccount(Account account) {
		return accountDao.activateAccount(account);
	}
	
	public boolean deactivateAccount(Account account) {
		return accountDao.deactivateAccount(account);
	}
	
	public ArrayList<Account> getAllAccounts() {
		return accountDao.getAllAccounts();
	}
	
	public boolean updatePassword(Account account, String password) {
		return accountDao.updatePassword(account, password);
	}
	
	public Account login(int accountNumber, String password) {
		Account account = null;
		
		if(accountDao.getAccount(accountNumber) != null) {
			if(password.equals(accountDao.getAccount(accountNumber).getPassword())) {
				account = accountDao.getAccount(accountNumber);
			}
		}
		
		return account;
	}
}
