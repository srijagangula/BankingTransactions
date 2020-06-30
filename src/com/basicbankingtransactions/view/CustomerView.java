
package com.basicbankingtransactions.view;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.basicbankingtransactions.exceptions.InsufficientBalanceException;
import com.basicbankingtransactions.exceptions.MinmumBalanceException;
import com.basicbankingtransactions.models.Account;
import com.basicbankingtransactions.service.BankingService;

public class CustomerView {

	private static BankingService bankingService = new BankingService();

	private static Scanner sc = new Scanner(System.in);
	private static Account account = null;

	public static void main(String[] args) {
		boolean isLoggedIn = false;

		do {
			System.out.println("ENTER THE ACCOUNT NUMBER");
			int accountNumber = sc.nextInt();
			System.out.println("ENTER THE FOUR DIGIT PIN");
			String password = sc.next();

			if (bankingService.login(accountNumber, password) != null) {
				account = bankingService.login(accountNumber, password);
				showWelcomeScreen();
				isLoggedIn = true;
			} else {
				System.out.println("INVALID DETAILS ENTERED!!");
			}
		} while (!isLoggedIn);

	}

	public static void showWelcomeScreen() {
		System.out.println("WELECOME " + account.getAccountHolderName().toUpperCase());
		boolean invalidChoice = false;
		do {
			System.out.println("PLEASE SELECT YOUR OPTION : ");
			System.out.println(
					"1.ACCOUNT INFO\n2.CHECK BALANCE\n3.WITHDRAW\n4.DEPOSIT\n5.TRANFER\n6.UPDATE PASSWORD\n7.LOGOUT");
			int option = sc.nextInt();
			switch (option) {
			case 1:
				showAccountDetails();
				break;
			case 2:
				showBalance();
				break;
			case 3:
				withdraw();
				break;
			case 4:
				deposit();
				break;
			case 5:
				transfer();
				break;
			case 6:
				updatePassword();
				break;
			case 7:
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

	private static void updatePassword() {
		System.out.println("UPDATE PIN");
		while (true) {
			System.out.println("ENTER THE FOUR DIGTI PIN");
			String password1 = sc.next();
			System.out.println("RE-ENETER THE PIN");
			String password2 = sc.next();
			Pattern pinPattern = Pattern.compile("\\d{4}");
			Matcher matcher = pinPattern.matcher(password1);

			if (matcher.matches()) {
				if (password1.equals(password2)) {
					if (!(password1.equals(account.getPassword()))) {
						if (bankingService.updatePassword(account, password1)) {
							System.out.println("PIN UPDATED SUCCESSFULLY");
							logout();
						} else {
							System.out.println("ERROR IN UPDATING PIN");
						}
					} else {
						System.out.println("PIN SHOULD NOT MATCH WITH LAST USED PIN.. PLEASE TRY AGAIN");
					}
				} else {
					System.out.println("PIN DOES NOT MATCH.. PLEASE TRY AGAIN");
				}
			} else {
				System.out.println("PIN SHOULD BE A FOUR DIGIT NUMBER.. PLEASE TRY AGAIN");
			}
		}
	}

	private static void logout() {
		account = null;
		bankingService = null;
		sc.close();
		System.out.println("LOGOUT SUCCESS, THANK YOU");
		System.exit(0);
	}

	private static void transfer() {

		Account payeeAccount = null;
		do {
			System.out.println("ENTER THE PAYEE ACCOUNT NUMBER");
			int payeeAccoutNumber = sc.nextInt();

			payeeAccount = bankingService.getAccount(payeeAccoutNumber);

			if (payeeAccount == null) {
				System.out.println("ACCOUNT DOESN'T EXISTS!!!");
			} else if (payeeAccount.getAccountNumber() == account.getAccountNumber()) {
				System.out.println("YOU CAN'T TRANSFER TO YOUR OWN ACCOUNT!!!");
				payeeAccount = null;
			}

		} while (payeeAccount == null);
		System.out.println("ACCOUNT NUMBER FOUND FOR PAYEE : " + payeeAccount.getAccountHolderName());
		System.out.println("ENTER THE AMOUNT TO BE TRANSFERRED");
		double amount = sc.nextDouble();

		try {
			bankingService.transfer(account, payeeAccount, amount);
			System.out.println("TRANSCATION SUCCESSFUL");
		} catch (InsufficientBalanceException e) {
			System.out.println("INSUFFICIENT FUNDS!!!");
		} catch (MinmumBalanceException e) {
			System.out.println("MINIMUM BALANCE MUST BE MAINTAINED!!!!");
		}

		showReturnToMainMenu();

	}

	private static void deposit() {
		System.out.println("ENTER THE AMOUNT TO BE DEPOSITED");
		double amount = sc.nextDouble();
		do {
			if (amount <= 0) {
				System.out.println("INVALID AMOUNT!!");
				deposit();
			} else {
				break;
			}
		} while (true);
		account.setBalance(bankingService.deposit(account, amount));
		System.out.println("AMOUNT DEPOSIT SUCCESS");
		System.out.println("UPDATED BALANCE : Rs." + account.getBalance());
		showReturnToMainMenu();
	}

	private static void withdraw() {
		System.out.println("ENTER THE AMOUNT TO BE WITHDRAWN");
		double amount = sc.nextDouble();
		do {
			if (amount <= 0) {
				System.out.println("INVALID AMOUNT!!");
				withdraw();
			} else {
				break;
			}
		} while (true);
		try {
			account.setBalance(bankingService.withdraw(account, amount));
			System.out.println("AMOUNT WITHDRAW SUCCESS");
			System.out.println("REMAINING BALANCE : Rs." + account.getBalance());
		} catch (InsufficientBalanceException e) {
			System.out.println("INSUFFICIENT FUNDS!!!!");
		} catch (MinmumBalanceException e) {
			System.out.println("MINIMUM BALANCE MUST BE MAINTAINED!!!!");
		}
		showReturnToMainMenu();
	}

	private static void showBalance() {
		System.out.println("\n\nACCOUNT BALANCE\n");
		System.out.println("YOUR ACCOUNT BALANCE IS : Rs." + account.getBalance());
		showReturnToMainMenu();
	}

	public static void showAccountDetails() {
		System.out.println("\n\nACCOUNT DETAILS\n");
		System.out.format("%20s : %d\n", "ACCOUNT NUMBER", account.getAccountNumber());
		System.out.format("%20s : %s\n", "CUSTOMER NAME", account.getAccountHolderName());
		System.out.format("%20s : %s\n", "EMAIL", account.getEmail());
		System.out.format("%20s : %s\n", "MOBILE", account.getMobileNumber());
		System.out.println();
		showReturnToMainMenu();
	}

	public static void showReturnToMainMenu() {
		boolean invalidChoice = true;
		do {
			System.out.println("PRESS 1 TO GO BACK TO MAIN MENU");
			int option = sc.nextInt();
			if (option == 1) {
				invalidChoice = false;
			}
		} while (invalidChoice);
		showWelcomeScreen();
	}
}
