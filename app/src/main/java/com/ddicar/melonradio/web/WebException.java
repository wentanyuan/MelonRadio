package com.ddicar.melonradio.web;

public class WebException extends Exception {

	public String message;

	public WebException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
