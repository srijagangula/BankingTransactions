package com.basicbankingtransactions.exceptions;

@SuppressWarnings("serial")
public class MinmumBalanceException extends Exception {
	
	public MinmumBalanceException(String errorMessage) {
		super(errorMessage);
	}
}
