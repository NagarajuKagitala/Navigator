package KafkaGUI;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Common.AllEvents;
import Common.ClearSelectionofCheckbox;
import Common.Dashboard;
import Common.KafkaLogin;
import Common.LogoutForAll;
import Common.Viewlets;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;
import KafkaGUI.KafkaObjectAttributes;

@Listeners(TestClass.class)
public class KafkaNode 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName;
	static String KafkaNodeName;
	static String Low;
	static String Medium;
	static String High;
	static int LowSleep;
	static int MediumSleep;
	static int HighSleep;
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		WGSName =Settings.getWGSNAME();
		KafkaNodeName =Settings.getKafkaNodeName();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
	}
	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "ViewletValue", "ViewletName"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname, int ViewletValue, String ViewletName) throws Exception
	{
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
		System.setProperty(sDriver, sDriverpath);
		driver=new ChromeDriver();
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
		Thread.sleep(8000);
		
		//Delete if dashboard exists with same name
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboard(driver, Dashboardname);
				
		/*
		 * KafkaLogin login=new KafkaLogin(); 
		 * login.KafkaLoginPage(driver, sDriver,sDriverpath); 
		 * System.out.println("Login done");
		 */
		
		
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		/*driver.findElement(By.id("createInitialViewlets")).click();
		
		
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByIndex(wgs);*/
		
		/*//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(4000);
		
		// ---- Creating Channel Viewlet ----		
		Viewlets obj=new Viewlets();
		obj.KafkaViewletForNode(driver, ViewletValue, ViewletName, WGSName, KafkaNodeName);
	}
	
	@Parameters({"Dashboardname", "SchemaName"})
	@TestRail(testCaseId = 889)
	@Test(priority=6)
	public void KafkaNodeShowObjectAttributes(String Dashboardname, String SchemaName, ITestContext context) throws InterruptedException
	{
		try
		{
			KafkaObjectAttributes obj=new KafkaObjectAttributes();
			obj.NodeShowObjectAttributes(Dashboardname, driver, SchemaName);
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "object attibutes opened with the selected kafka node");
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed object attibutes page");
			driver.findElement(By.id("Attributes failed")).click();
		}
		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=1)
	@TestRail(testCaseId = 890)
	public void ShowTopology(String Dashboardname, ITestContext context) throws InterruptedException
	{		
		/*
		 * int ManagerName_Index=3; if(!WGSName.contains("MQM")) { ManagerName_Index=4;
		 * }
		 * 
		 * //Store the Manager name into string String
		 * ManagerName=driver.findElement(By.xpath("//datatable-body-cell["+
		 * ManagerName_Index +"]/div/span")).getText();
		 */
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Show topology option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Topology")).click();
		Thread.sleep(14000);
		
		try
		{
		if (!checkprogress()) {

			System.out.println("exit");
		}
		}
		catch (Exception e)
		{
			System.out.println("No progress bar");
		}
		
		//Click on set and show topology option
		driver.findElement(By.xpath("//button[contains(.,'Set and show topology')]")).click();
		Thread.sleep(18000);
		
		//Store the Topology page data into string
		String Topology=driver.findElement(By.cssSelector("svg")).getText();
		
		//Verification condition
		if(Topology.contains("Cluster"))
		{
			System.out.println("Topology page is opened with the selected kafka node");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Topology page is opened with the selected kafka node");
			driver.findElement(By.xpath("//app-modal-title/div/div[2]/i[2]")).click();
		} 
		else
		{
			System.out.println("Topology page is not opened with the selected kafka node");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to open Topology page");
			driver.findElement(By.xpath("//app-modal-title/div/div[2]/i[2]")).click();
			driver.findElement(By.xpath("Topology page failed")).click();
		}
		Thread.sleep(1000);	
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 891)
	@Test(priority=2)
	public static void Events(String Dashboardname, ITestContext context) throws InterruptedException
	{
		try
		{
			AllEvents obj=new AllEvents();
			obj.Events(Dashboardname, driver, context);
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Events Working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Events failed");
			driver.findElement(By.xpath("Topology page failed")).click();
		}
		
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 892)
	@Test(priority=3)
	public static void DiscoverNow(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		try {
		//Select Incremental option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Discover now"))).perform();
		driver.findElement(By.linkText("Incremental")).click();
		Thread.sleep(4000);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
				
		//Select Full option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverFull=new Actions(driver);
		MousehoverFull.moveToElement(driver.findElement(By.linkText("Discover now"))).perform();
		driver.findElement(By.linkText("Full")).click();
		Thread.sleep(4000);
		
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Discover now option working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an error while testing discover now option: "+ e.getMessage());
		}
		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority = 4)
	@TestRail(testCaseId = 893)
	public static void PropertiesOfNode(String Dashboardname, ITestContext context) throws InterruptedException 
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		// Select Manage
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Manage")).click();
		Thread.sleep(5000);
		try {
			//Clearing selection of object
			ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
			che1.Deselectcheckbox(Dashboardname, driver);
			
			// Properties option selection
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Properties...")).click();
			Thread.sleep(2000);

			// Store the editable function in to a string
			boolean FieldNamevalue = driver.findElement(By.cssSelector(".ng-touched")).isEnabled();
			System.out.println(FieldNamevalue);

			// Verification
			if (FieldNamevalue == false) {
				System.out.println("Node Name field is UnEditable");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Node Name field is UnEditable");
				driver.findElement(By.cssSelector(".btn-danger")).click();
				Thread.sleep(8000);
			} else {
				System.out.println("Node Name field is Editable");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Node Name field is Editable");
				driver.findElement(By.cssSelector(".btn-danger")).click();
				Thread.sleep(8000);
				driver.findElement(By.xpath("Node name edit function Failed")).click();
			}
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Getting exception in properties page, check details: " + e.getMessage());
			System.out.println("Getting exception in properties page");
			
			//Clearing selection of object
			ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
			che2.Deselectcheckbox(Dashboardname, driver);
			
			// Select Manage
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Manage")).click();
			Thread.sleep(5000);
			driver.findElement(By.cssSelector(".btn-danger")).click();
		}
		Thread.sleep(1000);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che3=new ClearSelectionofCheckbox();
		che3.Deselectcheckbox(Dashboardname, driver);

		// Select Manage
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Manage")).click();
		Thread.sleep(5000);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=10)
	public static void Logout(String Dashboardname) throws InterruptedException
	{
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, Dashboardname);
	}
	
	private static boolean checkprogress() throws InterruptedException {
		try {
			WebElement progressBar = driver.findElement(By.cssSelector(".progress-bar"));
			while (progressBar.isDisplayed()) {
				System.out.println("Progress bar loading....");
				Thread.sleep(1000);
			}
		} catch (StaleElementReferenceException e) {
			// TODO: handle exception
			return false;
		}
		return false;
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
