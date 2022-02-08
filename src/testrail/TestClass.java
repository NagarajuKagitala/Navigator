package testrail;

import java.io.File;
import java.io.IOException;


import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;


public class TestClass implements ITestListener {
	String filePath = "D:\\SCREENSHOTS";
	WebDriver driver;
	@Override
	public void onFinish(ITestContext arg0) {
		// TODO Auto-generated method stub
		System.out.println("Method Finished: " + arg0.getName());
		
	}

	@Override
	public void onStart(ITestContext arg0) {
		// TODO Auto-generated method stub
		
		System.out.println("Method Started: " + arg0.getName());
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailure(ITestResult arg0) {
		// TODO Auto-generated method stub
		
		//System.out.println(Reporter.getCurrentTestResult());
		int testCaseID=0;
		  try {
			  
			  if (arg0.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(TestRail.class))
				{
				TestRail testCase = arg0.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestRail.class);
				// Get the TestCase ID for TestRail
				testCaseID = testCase.testCaseId();
				//System.out.println("Test Case ID = " + testCaseID);
				 ITestContext context = arg0.getTestContext();
				//System.out.println("Context = " + arg0.getTestContext().getAttribute("Status"));
				 //WebDriver driver ;
			    	//takeScreenShot(arg0.getMethod().getMethodName());
				
				 	/*File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(scrFile,new File("F:\\Nagaraju\\Workgroup server\\Workgroupserver_hh.png"));*/
			
				//api.updateRun(testCaseID,status,comment,arg0.getName());
				
				 	int status=5;
					String comment=(String) arg0.getTestContext().getAttribute("Comment");
					//System.out.println("Status: "+ status + ' ' + "Comment: " + comment);
					TestRailAPI api=new TestRailAPI();
					api.getRun(testCaseID,status,comment,arg0.getMethod().getMethodName());
				}
				else
				{
					System.out.println("Testcase id not defined");
				}
		   
			
			} catch (Exception e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println("Method Failed: " + arg0.getName());
	}
	
    public void takeScreenShot(String methodName) {
    	
    	TakesScreenshot screenshot=(TakesScreenshot)driver;
   	 File scrFile = screenshot.getScreenshotAs(OutputType.FILE);
        //The below method will save the screen shot in d drive with test method name 
           try {
				FileHandler.copy(scrFile, new File(filePath+methodName+".png"));
				System.out.println("***Placed screen shot in "+filePath+" ***");
			} catch (IOException e) {
				e.printStackTrace();
			}
   }

	@Override
	public void onTestSkipped(ITestResult arg0) {
		
		// TODO Auto-generated method stub
				
				System.out.println("Method Skipped: " + arg0.getName());
		
	}

	@Override
	public void onTestStart(ITestResult arg0) {
		
		
		System.out.println("Test Started for method: " + arg0.getName());
		
		
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
		// TODO Auto-generated method stub
		int testCaseID=0;
		int defectId=0;
		  try {
			  
			  if (arg0.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(TestRail.class))
				{
				TestRail testCase = arg0.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestRail.class);
				
				// Get the TestCase ID for TestRail
				testCaseID = testCase.testCaseId();
				defectId=testCase.defectId();
				//System.out.println("Test Case ID = " + testCaseID);
				
				//System.out.println("Defect ID = " + defectId);
				
				//System.out.println("Context = " + arg0.getTestContext().getAttribute("Status"));
				
				int status=(int) arg0.getTestContext().getAttribute("Status");
				String comment=(String) arg0.getTestContext().getAttribute("Comment");
				//System.out.println("Status: "+ status + ' ' + "Comment: " + comment);
				TestRailAPI api=new TestRailAPI();
				api.getRun(testCaseID,status,comment,arg0.getMethod().getMethodName());
				
				//api.updateRun(testCaseID,status,comment,arg0.getName());
				
				}
				else
				{
					System.out.println("No Test Case ID");
				}
		   
			
			} catch (Exception e ) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		System.out.println("Method Success: " + arg0.getName());
		
	}
}

