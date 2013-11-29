package com.enonic.autotests.logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.utils.TestUtils;

/**
 * Wrapper for {@link Reporter} instance.
 * 
 * 02.04.2013
 */
public class Logger implements ILogger {

	private static volatile Logger instance;

	protected static java.util.logging.Logger perfLogger;

	protected static Logger defaultLogger = new Logger();

	protected int severity;

	private WebDriver driver;

	public static Logger getLogger() {
		if (instance == null) {
			synchronized (Logger.class) {
				if (instance == null) {
					instance = new Logger();
					instance.setSeverity(5);
				}

			}
		}
		return instance;
	}

	public class Entry implements IEntry {

		protected int severity;

		protected String message;

		protected Throwable exception;

		public Entry(int severity, String message) {
			this.severity = severity;
			this.message = message;

		}

		public Entry(int severity, String message, Throwable exception) {
			this.severity = severity;
			this.message = message;
			this.exception = exception;
		}

		public int getSeverity() {
			return severity;
		}

		public String getMessage() {
			return message;
		}

		public Throwable getException() {
			return exception;
		}

		public String toString() {
			SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
			Date time = Calendar.getInstance().getTime();
			StringBuffer buffer = new StringBuffer(dateFormat.format(time));

			buffer.append(severityToString(severity)).append(": ");

			if (message != null) {
				buffer.append(message);
			}

			if (exception != null) {
				if (message != null) {
					buffer.append(", ");
				}
				buffer.append(exception);
			}

			return buffer.toString();
		}
	}

	public class PerfFormatter extends java.util.logging.Formatter {
		///private String lineSeparator = (String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction(
		//		"line.separator"));

		String lineSeparator = java.security.AccessController.doPrivileged(
			    new java.security.PrivilegedAction<String>() {
			        public String run() {
			            return System.getProperty("line.separator");
			        }
			    }
			 );
		@Override
		public String format(java.util.logging.LogRecord record) {
			String message = formatMessage(record);
			SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
			Date time = Calendar.getInstance().getTime();
			StringBuffer sb = new StringBuffer(dateFormat.format(time));
			sb.append(record.getLevel().getLocalizedName());
			sb.append(": ");
			sb.append(message);
			sb.append(lineSeparator);
			return sb.toString();
		}
	}

	/*
	 * 
	 */
	public static String severityToString(int severity) {
		switch (severity) {
		case Severity.ERROR:
			return "ERROR";
		case Severity.WARNING:
			return "WARNING";
		case Severity.INFO:
			return "INFO";
		case Severity.PERFOMANCE:
			return "PERFOMANCE";
		case Severity.DEBUG:
			return "DEBUG";
		case Severity.ALL:
			return "ALL";
		}

		return null;
	}

	public static final String stackTrace(Throwable ex) {
		ByteArrayOutputStream ostr = new ByteArrayOutputStream();
		ex.printStackTrace(new PrintWriter(ostr, true));
		String text = ostr.toString();

		return text;
	}

	public static Logger getDefault() {
		return Logger.defaultLogger;
	}

	private Logger() {

		try {
			perfLogger = java.util.logging.Logger.getLogger("Perfomance");
			java.util.logging.FileHandler perfHandler = new java.util.logging.FileHandler("performance%u.output", true);
			perfHandler.setFormatter(new PerfFormatter());
			perfLogger.addHandler(perfHandler);

			perfLogger.setUseParentHandlers(false);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void log(IEntry entry) {
		Reporter.log(entry.toString());
		System.out.println(entry.toString());
	}

	public synchronized void  log(int severity, String message, TestSession session) {
		if (this.severity < severity)
			return;

		if (severity == Severity.ERROR && session != null) {
			log(new Entry(severity, message + " SNAPSHOT: " + TestUtils.getInstance().saveScreenshot(session)));
		} else
			log(new Entry(severity, message));
	}

	public void info(String message) {
		log(Severity.INFO, message, null);
	}

	private class PerformanceLevel extends Level {
		private static final long serialVersionUID = 1L;

		protected PerformanceLevel() {
			super("PERFORMANCE", 1488);
		}
	}

	private PerformanceLevel oLevel = new PerformanceLevel();

	public void perfomance(String message, long startTime) {
		double time = System.currentTimeMillis() - startTime;
		log(Severity.PERFOMANCE, time / 1000 + ", Sec takes to " + message, null);
		perfLogger.log(oLevel, time / 1000 + ", Sec takes to " + message);
	}

	public void error(String message, TestSession session) {
		log(Severity.ERROR, message, session);
	}

	public void warning(String message) {
		log(Severity.WARNING, message, null);
	}

	public void debug(String message) {
		log(Severity.INFO, message, null);
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public void exception(String message, TestSession session) {
		log(Severity.EXCEPTION, message, session);
	}

	public WebDriver getDriver() {
		return driver;
	}

	public Logger setDriver(WebDriver driver) {
		this.driver = driver;
		return this;
	}

}
