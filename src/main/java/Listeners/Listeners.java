package Listeners;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import resources.Base;
import utilities.ExtentReporter;

public class Listeners extends Base implements ITestListener{
	WebDriver driver = null;
	ExtentReports extentReport = ExtentReporter.getExtentReport();
	ThreadLocal<ExtentTest> extentTestThread = new ThreadLocal<ExtentTest>();
	ExtentTest extentTest;

	@Override
	public void onTestStart(ITestResult result) {
		String testname = result.getName();
		extentTest = extentReport.createTest(testname+"executed started");
		extentTestThread.set(extentTest);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		extentTestThread.get().log(Status.PASS,"Test Passed");
		//extentTest.log(Status.PASS, " Test Passed");
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String testName=result.getName();
		//extentTest.fail(result.getThrowable());
		extentTestThread.get().fail(result.getThrowable());
		try {
		driver = (WebDriver)result.getTestClass().getRealClass().getDeclaredField("driver").get(result.getInstance());
		}catch(Exception e) {
			e.printStackTrace();
		}
		try {
			String screenshotFilePath = takeScreenshot(testName,driver);
			extentTestThread.get().addScreenCaptureFromPath(screenshotFilePath, testName);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		
	}

	@Override
	public void onStart(ITestContext context) {
		
	}

	@Override
	public void onFinish(ITestContext context) {
		extentReport.flush();
		

	}
}
