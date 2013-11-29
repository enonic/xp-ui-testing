/**
 * 
 */
package com.enonic.autotests;

import org.testng.ITestContext;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

public class TestListener extends TestListenerAdapter {
	@Override
	public void onConfigurationFailure(ITestResult tr) {
		log("FAILED CONFIGURATION: " + tr.getName());
		log(tr.getThrowable().toString());
		log("================================================================================");
	}

	@Override
	public void onConfigurationSkip(ITestResult tr) {
		log("SKIPPED CONFIGURATION: " + tr.getName());
		log("================================================================================");
	}

	@Override
	public void onConfigurationSuccess(ITestResult tr) {
		//log("PASSED CONFIGURATION: " + tr.getName());
		//log("================================================================================");
	}

	@Override
	public void onStart(ITestContext tc) {
		log("================================================================================");
		log("STARTED TEST: " + tc.getName());
		log("================================================================================");
	}

	@Override
	public void onFinish(ITestContext tc) {
		log("TEST FINISHED: " + tc.getName());
		log("Tests run: " + tc.getPassedTests().size() + ", Failures: " + tc.getFailedTests().size() + ", Skips: " + tc.getSkippedTests().size());
		if (tc.getFailedConfigurations().size() > 0) {
			log("Configurations Failures: " + tc.getFailedConfigurations().size() + ", Skips: " + tc.getSkippedConfigurations().size());
		}
		log("================================================================================");
	}

	@Override
	public void onTestStart(ITestResult tr) {
		log("STARTED: " + tr.getName());
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		log("FAILED: " + tr.getName());
		log(tr.getThrowable().toString());
		log("================================================================================");
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		log("SKIPPED: " + tr.getName());
		log("================================================================================");
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		log("PASSED: " + tr.getName());
		log("================================================================================");
	}

	private void log(String string) {
		Reporter.log(string);
	}
}
