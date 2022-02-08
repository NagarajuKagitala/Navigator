package NavigatorCore;

import java.io.File;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Common.ClearSelectionofCheckbox;
import Common.Dashboard;
import Common.LogoutForAll;
import Common.Viewlets;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class SearchCriteria 
{
	String Queue="";
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
		else if(sDriver.equalsIgnoreCase("webdriver.edge.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new EdgeDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.ie.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new InternetExplorerDriver();
		}
		else
		{
			System.setProperty(sDriver, sDriverpath);
			//driver=new FirefoxDriver();
			FirefoxOptions options = new FirefoxOptions();
			options.setCapability("marionette", false);
			driver = new FirefoxDriver(options);
		}
		
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(HighSleep);
		
		//Delete if dashboard exists with same name
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboard(driver, Dashboardname);
		
		//---------- Create New Dashboard ---
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		
		
		/*driver.findElement(By.id("createInitialViewlets")).click();
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByVisibleText(wgs);
		
		//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(MediumSleep);	
		
		
		//--------- Create Queue viewlet-----------
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		Thread.sleep(LowSleep);
	}
	
	@Parameters({"Dashboardname", "MessageData"})
	@Test(priority=1)
	public void PutAmessageIntoQueue(String Dashboardname, String MessageData) throws Exception
	{
		/*
		 * //Check Show Empty Queues check box
		 * driver.findElement(By.cssSelector(".fa-cog")).click();
		 * driver.findElement(By.id("empty-queues")).click(); Thread.sleep(4000);
		 * driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		 * Thread.sleep(3000);
		 */
				
		//Get the Queue name
		Queue=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		//System.out.println("Queue name is: " +Queue);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
				
		//Select the put new message option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(3000);
		Actions PutMessagesMousehour=new Actions(driver);
		PutMessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Put New Message")).click();
		Thread.sleep(3000);
		
		//Select the number of messages
		driver.findElement(By.name("generalNumberOfMsgs")).click();
		driver.findElement(By.name("generalNumberOfMsgs")).clear();
		driver.findElement(By.name("generalNumberOfMsgs")).sendKeys("1");
		
		//Put a message data
		//driver.findElement(By.id("encoding-text-9")).click();
		driver.findElement(By.xpath("//textarea")).sendKeys(MessageData);
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		try
		{
			driver.findElement(By.id("yes")).click();
			driver.findElement(By.cssSelector(".btn-danger")).click();
			Thread.sleep(1000);
		}
		catch (Exception e)
		{
			System.out.println("No Exception");
		}
			
		/*
		 * //Restoring the Default Settings
		 * driver.findElement(By.cssSelector(".fa-cog")).click();
		 * driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click
		 * (); Thread.sleep(4000);
		 * driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		 * Thread.sleep(3000);
		 */
	}
	
	
	@Parameters({"SearchCriteriaName", "SearchCriteriaData"})
	@TestRail(testCaseId = 349)
	@Test(priority=2)
	public void AddSearchCriteriaCondition(String SearchCriteriaName, String SearchCriteriaData, ITestContext context) throws Exception
	{
		//Edit Viewlet page
		driver.findElement(By.id("dropdownMenuButton")).click();
		Thread.sleep(8000);
		driver.findElement(By.linkText("Edit viewlet")).click();
		Thread.sleep(4000);
		
		//Search Criteria
		boolean FindMessagesCheckbox=driver.findElement(By.id("sc-find-messages-checkbox")).isSelected();
		if(FindMessagesCheckbox==false)
		{
			driver.findElement(By.id("sc-find-messages-checkbox")).click();
		}
		
		driver.findElement(By.cssSelector("div.right > div.g-text-and-input.line > button.btn-white-round")).click();
		Thread.sleep(1000);
		
		//Get the list of conditions
		String Conditions=driver.findElement(By.xpath("//div/div[3]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Conditions are :" +Conditions);
		
		if(Conditions.contains(SearchCriteriaName))
		{
			//click on condition name
			driver.findElement(By.xpath("//span[contains(.,'Search Queues')]")).click();
			Thread.sleep(LowSleep);
			
			//Click on delete button
			driver.findElement(By.xpath("//img[@alt='delete']")).click();
			Thread.sleep(MediumSleep);
			
		}
		
		//Click on Add icon
		driver.findElement(By.xpath("//img[@alt='new']")).click();
		Thread.sleep(LowSleep);
		
		//Double click on Element
		Actions actionclick=new Actions(driver);
		WebElement DC=driver.findElement(By.xpath("//span[contains(.,'Double Click here to Write Message Criteria Name')]"));
		actionclick.doubleClick(DC).build().perform();
		Thread.sleep(LowSleep);
		
		driver.findElement(By.xpath("//datatable-body-cell[3]/div/input")).click();
		driver.findElement(By.xpath("//datatable-body-cell[3]/div/input")).sendKeys(SearchCriteriaName);
		//driver.findElement(By.xpath("(//input[@type='text'])[13]")).click();           
		//driver.findElement(By.xpath("(//input[@type='text'])[13]")).sendKeys(SearchCriteriaName);
						
		//Click on Data enter icon
		driver.findElement(By.xpath("//img[@alt='Data']")).click();
		
		//Enter the search data
		driver.findElement(By.xpath("//div[2]/textarea")).sendKeys(SearchCriteriaData);
				
		//Click on Save button
		driver.findElement(By.xpath("//app-mod-data/div/div[2]/div/div/div/button")).click();
		Thread.sleep(LowSleep);
			
		//Click on save button in windo
		driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
		Thread.sleep(LowSleep);
		
		//Click on Apply changes button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
		
		/*//Search criteria name
		driver.findElement(By.cssSelector(".input-group > .form-control")).sendKeys(SearchCriteriaName);
		driver.findElement(By.cssSelector("button.btn.btn-outline-secondary")).click();
		Thread.sleep(2000);
		
		//Add Message data
		driver.findElement(By.xpath("//td/input")).click();
		driver.findElement(By.xpath("//img[@alt='Data']")).click();
		Thread.sleep(2000);
		
		//Enter data
		driver.findElement(By.xpath("//app-mod-message-data-criteria/div/div[2]/input")).sendKeys(SearchCriteriaData);
		
		//Click on OK buttons
		driver.findElement(By.xpath("//app-mod-message-data-criteria/div/div[3]/button")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[5]/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		driver.findElement(By.xpath("//input[@type='text']")).clear();
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Queue);*/
		
		//Store the table body
		String ViewletData=driver.findElement(By.xpath("//datatable-body")).getText();
		
		for(int j=0; j<=Queue.length(); j++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		if(ViewletData.contains(Queue))
		{
			System.out.println("Search criteria is working fine");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Search criteria working fine");
		}
		else
		{
			System.out.println("Search criteria is not working fine");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Search criteria not working fine");
    		driver.findElement(By.xpath("Search criteria failed")).click();
		}
		
	}
	
	@TestRail(testCaseId = 1046)
	@Parameters({"SearchCriteriaName", "SearchCriteriaData"})
	@Test(priority=3)
	public void ActiveFilterInBrowseMessagePage(String SearchCriteriaName, String SearchCriteriaData, ITestContext context) throws InterruptedException
	{
		//Click on clear all check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(2000);  
		
		//Search with Queue name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Queue);
		Thread.sleep(LowSleep);
		
		//Browse the Queue
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div[3]/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Browse messages")).click();
		Thread.sleep(HighSleep);
		
		//Choose the Active filter
		//driver.findElement(By.xpath("//div[2]/div/div[2]/div/ng-select/div")).clear();
		//driver.findElement(By.xpath("//div[2]/div/div[2]/div/ng-select/div")).sendKeys(SearchCriteriaName);
		//driver.findElement(By.xpath("//div[2]/div/div[2]/div/ng-select/div")).sendKeys(Keys.ENTER);
		//Thread.sleep(4000);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		Thread.sleep(LowSleep);
		
		//Edit option                   
		driver.findElement(By.cssSelector(".item-dropdown:nth-child(2)")).click();
		Thread.sleep(LowSleep);
				
		//Click on Data
		driver.findElement(By.cssSelector(".g-tab:nth-child(7)")).click();
		Thread.sleep(LowSleep);
				
		//get the message data and store into string
		String MessageData=driver.findElement(By.id("encoding-text-7")).getAttribute("value");
		System.out.println("message data is: " +MessageData);
		
		//Click on cancel button
		driver.findElement(By.cssSelector(".btn-danger")).click();
		Thread.sleep(MediumSleep);
		
		//Click on close 
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(HighSleep);
		
		if(MessageData.equalsIgnoreCase(SearchCriteriaData) || MessageData.contains(SearchCriteriaData))
		{
			System.out.println("Active filter is working fine");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Active filter working fine");
		}
		else
		{
			System.out.println("Active filter is not working fine");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Actiove filter not working fine");
    		driver.findElement(By.xpath("Active filter failed")).click();
		}
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=20)
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
