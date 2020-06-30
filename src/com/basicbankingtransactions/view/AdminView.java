package com.basicbankingtransactions.view;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.basicbankingtransactions.models.Account;
import com.basicbankingtransactions.service.BankingService;

public class AdminView {
	
	private static BankingService bankingService = new BankingService();
	
	private static ArrayList<Account> allAccounts = new ArrayList<Account>();
	
	private static final String ADMIN_USERNAME = "admin";
	private static final String ADMIN_PASSWORD = "admin";
	
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		boolean isLoggedIn = false;
		
		do {
			System.out.println("ENTER THE USERNAME");
			String username = sc.next();
			System.out.println("ENTER THE PASSWORD");
			String password = sc.next();
			
			if(username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
				isLoggedIn = true;
				showAdminMenu();
			}
			else {
				System.out.println("INVALID CREDENTIALS!!!");
			}
		} while(!isLoggedIn);
		showAdminMenu();
	}

	private static void showAdminMenu() {
		System.out.println("WELECOME TO ADMIN PANEL");		
		boolean invalidChoice = false;
		do {
			System.out.println("PLEASE SELECT YOUR OPTION : ");
			System.out.println("1.CREATE ACCOUNT\n2.ACTIVATE ACCOUNT\n3.DEACTIVATE ACCOUNT\n4.LOGOUT");
			int option = sc.nextInt();
			switch (option) {
			case 1:
				createAccount();
				break;
			case 2:
				activateAccount();
				break;
			case 3:
				deactivateAccount();
				break;
			case 4:
				logout();
				break;
			default:
				System.out.println("INVALID OPTION SELECTED!");
				System.out.println("PLEASE SELCET A VALID OPTION");
				invalidChoice = true;
				break;
			}
		} while (invalidChoice);
	}

	private static void createAccount() {
		System.out.println("ENETER THE NAME");
		String accountHolderName = sc.next();
		System.out.println("ENTER THE E-MAIL");
		String email = sc.next();
		
		String mobileNumber = null;
		
		do {
			System.out.println("ENTER THE MOBILE NUMBER");
			mobileNumber = sc.next();
			Pattern mobileNumberPattern = Pattern.compile("\\d{10}");
			Matcher matcher = mobileNumberPattern.matcher(mobileNumber);
			
			if(matcher.matches()) {
				break;
			}
			else {
				System.out.println("MOBILE NUMBER MUST BE 10 DIGIT INTEGER");
			}
		} while (true);
		
		int statusCode = 0;
		do {
			System.out.println("PLEASE SELECT THE STATUS OF ACCOUNT\n1.ACTIVE\n2.INACTIVE");
			statusCode = sc.nextInt();
		} while (!isValidStatus(statusCode));
		String status = getStatusString(statusCode);
		
		Account account = bankingService.insert(new Account(0.0, null, status, accountHolderName, mobileNumber, email));;
		if(account != null) {
			System.out.println("ACCOUNT CREATED SUCCESSFULLY WITH ACCOUNT NUMBER : " + account.getAccountNumber());
		}
		showReturnToMainMenu();
	}

	private static String getStatusString(int statusCode) {
		if(statusCode == 1) {
			return Account.ACCOUNT_ACTIVE;
		}else if(statusCode == 2) {
			return Account.ACCOUNT_INACTIVE;
		}
		return null;
	}

	private static boolean isValidStatus(int statusCode) {
		if(statusCode==1 || statusCode==2) {
			return true;
		}
		System.out.println("INVALID STATUS SELECTED!!!");
		return false;
	}
	
	private static void activateAccount() {

		Account account = null;

		do {
			showAllAccounts();
			System.out.println("PLEASE SELECT AN ACCOUNT NUMBER");
			int accountNumber = sc.nextInt();
			
			account = bankingService.getAccount(accountNumber);
			
			if(account == null) {
				System.out.println("PLEASE SELECT A VALID ACCOUNT NUMBER!!");
			}
			else if(account.getStatus().equals(Account.ACCOUNT_ACTIVE)) {
				System.out.println("THIS ACCOUNT IS ALREADY ACTIVE");
				System.out.println("PLEASE SELECT ANOTHER ACCOUNT");
				account = null;
			}
		} while (account == null);
		
		if(bankingService.activateAccount(account)) {
			System.out.println("ACCOUNT ACTIVATED SUCCESSFULLY");
			showAllAccounts();
		}
		showReturnToMainMenu();
	}

	private static void showAllAccounts() {
		allAccounts = bankingService.getAllAccounts();
		
		if(allAccounts.size() > 0) {
			System.out.format("| %20s | %20s | %15s | %17s | %40s |\n", "ACCOUNT NUMBER", "CUSTOMER NAME", "STATUS", "MOBILE NUMBER", "EMAIL");
		}
		
		for (Account account : allAccounts) {
			System.out.format("| %20d | %20s | %15s | %17s | %40s |\n", account.getAccountNumber(), account.getAccountHolderName(), 
					account.getStatus(), account.getMobileNumber(), account.getEmail());
		}		
	}

	private static void deactivateAccount() {
		Account account = null;

		do {
			showAllAccounts();
			System.out.println("PLEASE SELECT AN ACCOUNT NUMBER");
			int accountNumber = sc.nextInt();
			
			account = bankingService.getAccount(accountNumber);
			
			if(account == null) {
				System.out.println("PLEASE SELECT A VALID ACCOUNT NUMBER!!");
			}
			else if(account.getStatus().equals(Account.ACCOUNT_INACTIVE)) {
				System.out.println("THIS ACCOUNT IS ALREADY INACTIVE");
				System.out.println("PLEASE SELECT ANOTHER ACCOUNT");
				account = null;
			}
		} while (account == null);
		
		if(bankingService.deactivateAccount(account)) {
			System.out.println("ACCOUNT DEACTIVATED SUCCESSFULLY");
			showAllAccounts();
		}
		showReturnToMainMenu();
	}

	private static void logout() {
		bankingService = null;
		sc.close();
		System.out.println("LOGOUT SUCCESS, THANK YOU");
		System.exit(0);
	}
	
	public static void showReturnToMainMenu() {
		boolean invalidChoice = true;
		do {
			System.out.println("PRESS 1 TO GO BACK TO MAIN MENU");
			int option = sc.nextInt();
			if(option == 1) {
				invalidChoice = false;
			}
		} while (invalidChoice);
		showAdminMenu();
	}
}
