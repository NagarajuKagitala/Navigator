package NavigatorCore;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.common.eventbus.DeadEvent;

import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class UserPerspective 
{
static WebDriver driver;
	
	static String Screenshotpath;
	static String WGSName;
	static String Node_Hostname;
	static String Low;
	static String Medium;
	static String High;
	static int LowSleep;
	static int MediumSleep;
	static int HighSleep;
	static String ListOfEnvironments="";
	static String ListOfViews="";

	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		WGSName =Settings.getWGSNAME();
		Node_Hostname =Settings.getNode_Hostname();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
	}
	
	@Parameters({"sDriver", "sDriverpath"})
	@Test
	public static void Login(String sDriver, String sDriverpath) throws Exception
	{
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			ChromeOptions options = new ChromeOptions(); 
			options.addArguments("--remote-allow-origins=*");
			driver=new ChromeDriver(options);
		}
		else if(sDriver.equalsIgnoreCase("webdriver.ie.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new InternetExplorerDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.edge.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver= new EdgeDriver();
		}
		else
		{
			System.setProperty(sDriver, sDriverpath);
			driver= new FirefoxDriver();
		}
		
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		
		//Login page
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(HighSleep);
	}
	
	
	@Parameters({"OptionName", "UserViewName"})
	@TestRail(testCaseId=1158)
	@Test(priority=1)
	public void AddUserView(String OptionName, String UserViewName, ITestContext context) throws InterruptedException 
	{
		//Click on View menu
		driver.findElement(By.cssSelector(".rep-btn")).click();
		Thread.sleep(LowSleep);
		
		//click on Add user view
		SelectOption(OptionName);
		Thread.sleep(MediumSleep);
				
		//Give the view name
		driver.findElement(By.xpath("//form/div/div/input")).sendKeys(UserViewName);
		Thread.sleep(LowSleep);
		
		//Click on Ok button
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(MediumSleep);
				
		//get list of views
		ListOfViews();
		
		//verification
		if(ListOfViews.contains(UserViewName))
		{
			System.out.println("Add view is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Add view is working");
		}
		else
		{
			System.out.println("Add view is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Add view is not working");
			
			driver.findElement(By.id("Add view failed")).click();
		}
		
		
	}
	
	
	@Parameters({"UserViewName", "DefaultName", "sDriver", "sDriverpath"})
	@TestRail(testCaseId=1159)
	@Test(priority=2)
	public void DefaultView(String UserViewName, String DefaultName, String sDriver, String sDriverpath, ITestContext context) throws Exception
	{		
		WebElement ele=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("scroll-host"));
		List<WebElement> divs=ele.findElements(By.tagName("div"));
		System.out.println("No of divs are: " +divs.size());
		
		for(WebElement div:divs)
		{
			if(div.getAttribute("class").contains("select-block"))
			{
				String View=div.getAttribute("title");
				if(View.contains(UserViewName))
				{
					div.click();
					Thread.sleep(MediumSleep);
					break;
				}
				
			}
			
		}
		
		//Click on View menu
		driver.findElement(By.cssSelector(".rep-btn")).click();
		Thread.sleep(LowSleep);
		
		//click on Add user view
		SelectOption(DefaultName);
		Thread.sleep(MediumSleep);
		
		driver.findElement(By.xpath("//ng-select/div/span")).click();
		Thread.sleep(MediumSleep);
		
		WebElement ele1=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("scroll-host"));
		List<WebElement> divs1=ele1.findElements(By.tagName("div"));
		System.out.println("No of divs are: " +divs1.size());
		//System.out.println("total html: " +ele1.getAttribute("innerHTML"));
		
		StringBuffer buffer=new StringBuffer();
		for(WebElement eee: divs1)
		{
			//System.out.println("innert html: " +eee.getAttribute("innerHTML"));
			String classdata=eee.getAttribute("class");
			System.out.println("class data is: " +classdata);
			if(classdata.contains("ng-option ng-option-selected ng-option-marked ng-star-inserted"))
			{
				//System.out.println("text is: " +eee.getText());
				buffer.append(eee.getText());
			}
		}
		
		String Defaultviewname=buffer.toString();
		System.out.println("Default view name is: " +Defaultviewname);
		
		if(Defaultviewname.contains(UserViewName))
		{
			System.out.println("Set as default view is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Set as default view is working");
		}
		else
		{
			System.out.println("Set as default view is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Set as default view is not working");
			driver.findElement(By.id("Set as default view failed")).click();
		}
		
	}
	
	@Parameters({"Editoptionname", "UpdatedViewName"})
	@TestRail(testCaseId=1160)
	@Test(priority=3)
	public void UpdateUserView(String Editoptionname, String UpdatedViewName, ITestContext context) throws InterruptedException
	{
		
		//Click on View menu
		driver.findElement(By.cssSelector(".rep-btn")).click();
		Thread.sleep(LowSleep);
		
		//click on Add user view
		SelectOption(Editoptionname);
		Thread.sleep(MediumSleep);
		
		//Give the view name
		driver.findElement(By.xpath("//form/div/div/input")).clear();
		driver.findElement(By.xpath("//form/div/div/input")).sendKeys(UpdatedViewName);
		Thread.sleep(LowSleep);
		
		//Click on Ok button
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(MediumSleep);
				
		//Get list of views
		ListOfViews();
		
		if(ListOfViews.contains(UpdatedViewName))
		{
			System.out.println("User view update is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "User view update is working");
		}
		else
		{
			System.out.println("User view update is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "User view update is not working");
			driver.findElement(By.id("User view update failed")).click();
		}
		
	}
	
	@Parameters({"Deleteoptionname", "UpdatedViewName"})
	@TestRail(testCaseId=1161)
	@Test(priority=4)
	public void DeleteUserView(String Deleteoptionname, String UpdatedViewName, ITestContext context) throws InterruptedException
	{
		//Click on View menu
		driver.findElement(By.cssSelector(".rep-btn")).click();
		Thread.sleep(LowSleep);
		
		//click on Add user view
		SelectOption(Deleteoptionname);
		Thread.sleep(MediumSleep);
		
		//click on confirmation yes button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(MediumSleep);
		
		//get list of views
		ListOfViews();
		
		if(ListOfViews.contains(UpdatedViewName))
		{
			System.out.println("User view delete is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "User view delete is not working");
			driver.findElement(By.id("User view delete failed")).click();
		}
		else
		{
			System.out.println("User view delete is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "User view delete is working");
			
		}
	}
	
	@Parameters({"DefaultName"})
	@Test(priority=30)
	public void Logout(String DefaultName) throws InterruptedException 
	{
		//Click on View menu
		driver.findElement(By.cssSelector(".rep-btn")).click();
		Thread.sleep(LowSleep);
		
		//click on Add user view
		SelectOption(DefaultName);
		Thread.sleep(MediumSleep);
		
		//Logout option
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		Thread.sleep(4000);
		driver.findElement(By.id("yesButton")).click();
		driver.close();

	}
	
	
	public void SelectOption(String OptionName) throws InterruptedException
	{
		WebElement ele=driver.findElement(By.id("user-view-dropdown"));
		List<WebElement> divs=ele.findElements(By.tagName("div"));
		System.out.println("size of options: " +divs.size());
		
		for(WebElement div:divs)
		{
			System.out.println("Options are: " +div.getText());
			
			if(div.getText().contains(OptionName))
			{
				div.click();
				Thread.sleep(MediumSleep);
				break;
			}
		}
	}
	
	public void ListOfViews() throws InterruptedException
	{
		//Click on menu drop down
		driver.findElement(By.xpath("//ng-select/div/span")).click();
		Thread.sleep(MediumSleep);
		
		WebElement ele=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("scroll-host"));
		List<WebElement> divs=ele.findElements(By.tagName("div"));
		System.out.println("No of divs are: " +divs.size());
		
		StringBuffer buffer=new StringBuffer();
		for(WebElement div:divs)
		{
			if(div.getAttribute("class").contains("select-block"))
			{
				System.out.println("View names are: " +div.getAttribute("title"));
				buffer.append(div.getAttribute("title"));
				buffer.append(",");
			}
			
		}
		
		ListOfViews=buffer.toString(); 
		System.out.println("List of views are: " +ListOfViews);	
		
	}
	
	
	@AfterMethod
	public void tearDown(ITestResult result) {

		final String dir = System.getProperty("user.dir");
		String screenshotPath;
		
		System.out.println("result getStatus: " + result.getStatus());
		// System.out.println("dir: " + dir);
		if (!result.getMethod().getMethodName().contains("Logout")) {
			if (ITestResult.FAILURE == result.getStatus()) {
				this.capturescreen(driver, result.getMethod().getMethodName(), "FAILURE");
				Reporter.setCurrentTestResult(result);

				Reporter.log("<br/>Failed to execute method: " + result.getMethod().getMethodName() + "<br/>");
				// Attach screenshot to report log
				screenshotPath = dir + "/" + Screenshotpath + "/ScreenshotsFailure/"
						+ result.getMethod().getMethodName() + ".png";

			} else {
				this.capturescreen(driver, result.getMethod().getMethodName(), "SUCCESS");
				Reporter.setCurrentTestResult(result);

				// Attach screenshot to report log
				screenshotPath = dir + "/" + Screenshotpath + "/ScreenshotsSuccess/"
						+ result.getMethod().getMethodName() + ".png";

			}

			String path = "<img src=\" " + screenshotPath + "\" alt=\"\"\"/\" />";
			// To add it in the report
			Reporter.log("<br/>");
			Reporter.log(path);
			try {
				//Update attachment to testrail server
				int testCaseID=0;
				//int status=(int) result.getTestContext().getAttribute("Status");
				//String comment=(String) result.getTestContext().getAttribute("Comment");
				  if (result.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(TestRail.class))
					{
					TestRail testCase = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestRail.class);
					// Get the TestCase ID for TestRail
					testCaseID = testCase.testCaseId();
					
					
					
					TestRailAPI api=new TestRailAPI();
					api.Getresults(testCaseID, result.getMethod().getMethodName());
					
					}
				}catch (Exception e) {
					// TODO: handle exception
					//e.printStackTrace();
				}
		}

	}

	public void capturescreen(WebDriver driver, String screenShotName, String status) {
		try {

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			if (status.equals("FAILURE")) {
				FileHandler.copy(scrFile, new File(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png"));
				//FileUtils.copyFile(scrFile, new File(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png"));
				Reporter.log(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png");
			} else if (status.equals("SUCCESS")) {
				FileHandler.copy(scrFile,
						new File(Screenshotpath + "./ScreenshotsSuccess/" + screenShotName + ".png"));

			}

			System.out.println("Printing screen shot taken for className " + screenShotName);

		} catch (Exception e) {
			System.out.println("Exception while taking screenshot " + e.getMessage());
		}

	}

}
