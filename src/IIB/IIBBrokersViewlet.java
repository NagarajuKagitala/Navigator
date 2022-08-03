package IIB;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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

import ApodGUI.ObjectsVerificationForAllViewlets;
import Common.ClearSelectionofCheckbox;
import Common.Dashboard;
import Common.LogoutForAll;
import Common.Viewlets;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class IIBBrokersViewlet 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName;
	static String Node_Hostname;
	static String IIB_WGSNAME;
	static String IIBNode;
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
		Node_Hostname =Settings.getNode_Hostname();
		IIB_WGSNAME =Settings.getIIB_WGSNAME();
		IIBNode =Settings.getIIBNode();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
		
	}
	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname) throws Exception
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
		Thread.sleep(HighSleep);
		
		//Delete if dashboard exists with same name
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboard(driver, Dashboardname);
		
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		
		
		/*driver.findElement(By.id("createInitialViewlets")).click();
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByIndex(Integer.parseInt(WGS_INDEX));*/
		
		/*//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);
	}
	
	@Test(priority=1)
	@TestRail(testCaseId=1051)
    @Parameters({"ViewletName", "ViewletValue"})
	public static void AddIIBBrokerViewlet(String ViewletName, int ViewletValue, ITestContext context) throws InterruptedException
	{
		Viewlets obj=new Viewlets();
		obj.IIBViewlet(driver, ViewletValue, ViewletName, IIB_WGSNAME, IIBNode);
		Thread.sleep(4000);
		
		if(driver.getPageSource().contains(ViewletName))
		{
			System.out.println("IIB Broker Viewlet is created");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "IIB Broker viewlet is created successfully");
		}
		else
		{
			System.out.println("IIB Broker viewlet is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create IIB Broker viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
			
    }
	
	@TestRail(testCaseId=1052)
	@Test(priority=2)
	public void BrokerAdminLogs(ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(6000);
		
		//Select Admin logs option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Admin Logs")).click();
		Thread.sleep(3000);
		
		String Title=driver.findElement(By.xpath("//app-modal-title/div/div/span")).getText();
		System.out.println("Title is :" +Title);
		
		if(Title.equalsIgnoreCase("IIB Broker Admin Logs"))
		{
			System.out.println("Admin logs page is opened");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Admin logs is working fine");
		}
		else
		{
			System.out.println("Admin logs page is not opened");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Admin logs is not working");
			driver.findElement(By.cssSelector(".btn-danger")).click();
			driver.findElement(By.id("Admin logs failed")).click();
		}
		
		//close button
		driver.findElement(By.cssSelector(".btn-danger")).click();
	}
	
	@TestRail(testCaseId=1053)
	@Test(priority=3)
	public void SearchWithBIPNumber(ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(6000);
		
		//Select Admin logs option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Admin Logs")).click();
		Thread.sleep(3000);
		
		WebElement logs=driver.findElement(By.className("adminLogs-table")).findElement(By.tagName("tbody"));
		List<WebElement> count=logs.findElements(By.tagName("tr"));
		int logscount=count.size();
		System.out.println("logs size is: " +logscount);
		
		if(logscount >= 2)
		{
			//Get the second bip number
			String Bip=driver.findElement(By.xpath("//tr[2]/td")).getText();
			
			//Search with bip number
			driver.findElement(By.xpath("//th/input")).clear();
			driver.findElement(By.xpath("//th/input")).sendKeys(Bip);
			driver.findElement(By.xpath("//th/input")).sendKeys(Keys.ENTER);
			Thread.sleep(3000);
			
			//Get the result values
			String Result=driver.findElement(By.xpath("//td")).getText();
			System.out.println("Result value is: " +Result);
			
			if(Result.equalsIgnoreCase(Bip))
			{
				System.out.println("BIP search is working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "BIP search is working fine");
			}
			else
			{
				System.out.println("BIP search is not working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "BIP search is not working");
				driver.findElement(By.cssSelector(".btn-danger")).click();
				driver.findElement(By.id("BIP search failed")).click();
			}
		}
		else
		{
			System.out.println("logs are not present");
		}
		//close button
		driver.findElement(By.cssSelector(".btn-danger")).click();
	}
	
	@TestRail(testCaseId=1054)
	@Test(priority=4)
	public void SearchWithTimeStamp(ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(6000);
		
		//Select Admin logs option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Admin Logs")).click();
		Thread.sleep(3000);
		
		WebElement logs=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
		List<WebElement> count=logs.findElements(By.tagName("tr"));
		int logscount=count.size();
		System.out.println("logs size is: " +logscount);
		
		if(logscount >= 2)
		{
			//Get the time stamp number
			String Timestamp=driver.findElement(By.xpath("//tr[2]/td[2]")).getText();
			
			//Search with bip number
			driver.findElement(By.xpath("//th[2]/input")).clear();
			driver.findElement(By.xpath("//th[2]/input")).sendKeys(Timestamp);
			driver.findElement(By.xpath("//th[2]/input")).sendKeys(Keys.ENTER);
			Thread.sleep(3000);
			
			//Get the result values
			String Result=driver.findElement(By.xpath("//td[2]")).getText();
			System.out.println("Result value is: " +Result);
			
			if(Result.equalsIgnoreCase(Timestamp))
			{
				System.out.println("Timestamp search is working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "Timestamp search is working fine");
			}
			else
			{
				System.out.println("Timestamp search is not working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Timestamp search is not working");
				driver.findElement(By.cssSelector(".btn-danger")).click();
				driver.findElement(By.id("Timestamp search failed")).click();
			}
		}
		else
		{
			System.out.println("logs are not present");
		}
		//close button
		driver.findElement(By.cssSelector(".btn-danger")).click();
	}
	
	@TestRail(testCaseId=1055)
	@Test(priority=5)
	public void SearchWithSource(ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(6000);
		
		//Select Admin logs option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Admin Logs")).click();
		Thread.sleep(3000);
		
		WebElement logs=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
		List<WebElement> count=logs.findElements(By.tagName("tr"));
		int logscount=count.size();
		System.out.println("logs size is: " +logscount);
		
		if(logscount >= 2)
		{
			//Get the source
			String Source=driver.findElement(By.xpath("//tr[2]/td[5]")).getText();
			
			//Search with bip number
			driver.findElement(By.xpath("//th[5]/input")).clear();
			driver.findElement(By.xpath("//th[5]/input")).sendKeys(Source);
			driver.findElement(By.xpath("//th[5]/input")).sendKeys(Keys.ENTER);
			Thread.sleep(3000);
			
			//Get the result values
			String Result=driver.findElement(By.xpath("//td[5]")).getText();
			System.out.println("Result value is: " +Result);
			
			if(Result.equalsIgnoreCase(Source))
			{
				System.out.println("Source search is working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "Source search is working fine");
			}
			else
			{
				System.out.println("Source search is not working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Source search is not working");
				driver.findElement(By.cssSelector(".btn-danger")).click();
				driver.findElement(By.id("Source search failed")).click();
			}
		}
		else
		{
			System.out.println("logs are not present");
		}
		//close button
		driver.findElement(By.cssSelector(".btn-danger")).click();
	}
	
	@TestRail(testCaseId=1056)
	@Test(priority=6)
	public void SearchWithMessage(ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(6000);
		
		//Select Admin logs option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Admin Logs")).click();
		Thread.sleep(3000);
		
		WebElement logs=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
		List<WebElement> count=logs.findElements(By.tagName("tr"));
		int logscount=count.size();
		System.out.println("logs size is: " +logscount);
		
		if(logscount >= 2)
		{
			//Get the Message
			String Message=driver.findElement(By.xpath("//tr[2]/td[8]")).getText();
			
			//Search with bip number
			driver.findElement(By.xpath("//th[8]/input")).clear();
			driver.findElement(By.xpath("//th[8]/input")).sendKeys(Message);
			driver.findElement(By.xpath("//th[8]/input")).sendKeys(Keys.ENTER);
			Thread.sleep(3000);
			
			//Get the result values
			String Result=driver.findElement(By.xpath("//td[8]")).getText();
			System.out.println("Result value is: " +Result);
			
			if(Result.equalsIgnoreCase(Message))
			{
				System.out.println("Message search is working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "Message search is working fine");
			}
			else
			{
				System.out.println("Message search is not working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Message search is not working");
				driver.findElement(By.cssSelector(".btn-danger")).click();
				driver.findElement(By.id("Message search failed")).click();
			}
		}
		else
		{
			System.out.println("logs are not present");
		}
		//close button
		driver.findElement(By.cssSelector(".btn-danger")).click();
	}
	
	
	@Parameters({"AttributeName", "AttributeValue"})
	@Test(priority=7)
	public void CustomAttributes(String AttributeName, String AttributeValue, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(6000);
		
		//Select Admin logs option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Custom Attributes")).click();
		Thread.sleep(3000);
		
		//Enter the custom Attribute name and value
		driver.findElement(By.xpath("//div[3]/div/input")).sendKeys(AttributeName);
		driver.findElement(By.xpath("//div[2]/input")).sendKeys(AttributeValue);
		
		//Click on add button
		driver.findElement(By.xpath("//button[contains(.,'Add')]")).click();
		Thread.sleep(6000);
		
		//click on OK button
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(6000);
		
		try
		{
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
		}
		catch(Exception e)
		{
			System.out.println("No need to click on canecl button");
		}
		//Clearing selection of object
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(6000);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(4000);
		
		//search with Attribute value
		driver.findElement(By.id("attrSearch")).sendKeys(AttributeValue);
		driver.findElement(By.id("attrSearch")).sendKeys(Keys.ENTER);
		Thread.sleep(LowSleep);
		
		//Get the result count into integer
		String lim=driver.findElement(By.xpath("//app-console-search/span[2]")).getText();
		System.out.println("output value is: " +lim);
		String[] arrOfStr = lim.split("/ ");
		String part1 = arrOfStr[1];
		int count=Integer.parseInt(part1);
		System.out.println("Result limit count is: " +count);
		
		//Click on close button
		driver.findElement(By.cssSelector(".close-button")).click();
		
		if(!(count==0))
		{
			System.out.println("Custom attribute is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Custom attribute is working fine");
		}
		else
		{
			System.out.println("Custom attribute is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Custom attribute is not working");
			driver.findElement(By.cssSelector(".btn-danger")).click();
			driver.findElement(By.id("Custom attribute failed")).click();
		}
	}
	
	@Parameters({"Dashboardname", "schemaName"})
	@TestRail(testCaseId=1058)
	@Test(priority=17)
	public static void ShowObjectAttributesForIIBBroker(String Dashboardname, String schemaName, ITestContext context) throws InterruptedException
	{
		try {
			IIBObjectAttributes obj=new IIBObjectAttributes();
		obj.IIBBrokerShowObjectAttributes(Dashboardname, driver, schemaName);
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "Show object attributes for IIB Broker viewlet is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to Show object attributes for IIB Broker viewlet, check details: "+ e.getMessage());
			driver.findElement(By.id("Attributes failed")).click();	
		}
	} 
	
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
    @TestRail(testCaseId=1057)
	@Test(priority=10)
	public static void AddToFavoriteViewlet(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{    	
    	//Store the service name into string
		String IntegrationNodeName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Create favorite viewlet
		driver.findElement(By.id("add-viewlet")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.id("viewlet-type-ok")).click();
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		
		//Select WGS dropdown
		driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div/div[2]/div/ng-select/div/span")).click();
		Thread.sleep(3000);
		
		WebElement drop1=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div1=drop1.findElements(By.tagName("div"));
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement di1 : div1)
		{
			//System.out.println("text is :" +di.getText());
			if(di1.getText().equalsIgnoreCase(IIB_WGSNAME))
			{
				di1.click();
				break;
			}	
		}
		Thread.sleep(2000);
		
		//Submit
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(2000);
		
		//Clearing selection of object
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(6000);

		
		//Select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(3000);
		
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div")).click();
		Thread.sleep(3000); 
		
		WebElement drop=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div=drop.findElements(By.tagName("div"));
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement di : div)
		{
			//System.out.println("text is :" +di.getText());
			if(di.getText().equalsIgnoreCase(FavoriteViewletName))
			{
				di.click();
				break;
			}	
		}
		
		/*
		 * //Select the favorite viewlet name Select fav=new
		 * Select(driver.findElement(By.cssSelector(".input > .ng-select-container")));
		 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(2000);
		 */
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(2000);
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification of service added to favorite viewlet
		if(Favdata.contains(IntegrationNodeName))
		{
			System.out.println("Broker name is added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Broker names are  added successfully to favorite viewlet");
		}
		else
		{
			System.out.println("Broker name is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add Broker name to favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);

	}
	
	
	
	@Parameters({"Dashboardname"})
	@Test(priority=18)
	public void Logout(String Dashboardname) throws InterruptedException 
	{
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, Dashboardname);
	}
	
	@AfterMethod
	public void tearDown(ITestResult result) {

		final String dir = System.getProperty("user.dir");
		String screenshotPath;
		//System.out.println("dir: " + dir);
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
				FileHandler.copy(scrFile,
						new File(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png"));
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
