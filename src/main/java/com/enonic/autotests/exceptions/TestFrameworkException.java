package com.enonic.autotests.exceptions;

/**
 * A runtime exception representing a failure related with Test Framework. <br>
 * For example, wrong locator was specified or bad javaScript method was
 * executed...
 * 
 * 01.04.2013
 */
public class TestFrameworkException extends RuntimeException {

	private static final long serialVersionUID = -646053856977947972L;

	public TestFrameworkException(String message) {
		super(message);

	}

	public TestFrameworkException() {

	}

}
