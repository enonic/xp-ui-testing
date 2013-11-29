package com.enonic.autotests.logger;

public interface ILogger {
	public static interface Severity {
		final static int EXCEPTION = 0;

		final static int ERROR = 1;

		final static int WARNING = 2;

		final static int INFO = 3;

		final static int PERFOMANCE = 4;

		final static int DEBUG = 5;

		final static int ALL = Integer.MAX_VALUE;
	}

	public interface IEntry {
		int getSeverity();

		String getMessage();

		Throwable getException();
	}

	void setSeverity(int severity);

	void log(IEntry entry);
}
