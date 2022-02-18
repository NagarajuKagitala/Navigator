package KafkaGUI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import KafkaGUI.KafkaObjectAttributes;
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

@Listeners(TestClass.class)
public class KafkaClusterViewlet 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String DownloadPath;
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
		DownloadPath =Settings.getDownloadPath();
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
		try {
		
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
		
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
			
		 KafkaLogin login=new KafkaLogin(); 
		 boolean islogin=login.KafkaLoginPage(driver, sDriver, sDriverpath); 
		 
		 if(islogin)
		 {
			 System.out.println("login passed");
			 
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
				obj.KafkaViewlet(driver, ViewletValue, ViewletName, WGSName, KafkaNodeName);
		 }
		 else
		 {
			 System.out.println("Login done"); 
		 }
		}
		
		catch(Exception ee)
		{
			//System.out.println("Exceptio is: " +ee.getStackTrace());
			ee.printStackTrace();
		}
		
		
	}
	
	@Parameters({"Dashboardname", "SchemaName"})
	@TestRail(testCaseId = 894)
	@Test(priority=11)
	public void ShowObjectAttributes(String Dashboardname, String SchemaName, ITestContext context) throws InterruptedException
	{
		try
		{
			KafkaObjectAttributes obj=new KafkaObjectAttributes();
			obj.ClusterShowObjectAttributes(Dashboardname, driver, SchemaName);
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
	@TestRail(testCaseId = 1077)
	public void ClusterTopology(String Dashboardname, ITestContext context) throws InterruptedException
	{		
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
			System.out.println("Topology page is opened with the selected kafka cluster");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Topology page is opened with the selected kafka cluster");
			driver.findElement(By.xpath("//app-modal-title/div/div[2]/i[2]")).click();
		} 
		else
		{
			System.out.println("Topology page is not opened with the selected kafka cluster");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to open Topology page");
			driver.findElement(By.xpath("//app-modal-title/div/div[2]/i[2]")).click();
			driver.findElement(By.xpath("Topology page failed")).click();
		}
		Thread.sleep(1000);	
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=2)
	@TestRail(testCaseId=895)
	public void Properties(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		try
		{
		//Select properties option
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	driver.findElement(By.linkText("Properties...")).click();
    	Thread.sleep(8000);
    	
    	//storing the name field status into boolean
		/*
		 * boolean NameField=driver.findElement(By.id("name")).isEnabled(); WebElement
		 * ele=driver.findElement(By.id("name"));
		 */
    	
    	WebElement El=driver.findElement(By.tagName("app-text-input")).findElement(By.tagName("div")).findElement(By.tagName("div"));
    	System.out.println(El.getAttribute("innerHTML"));
    	
    	String Namefield=El.getAttribute("innerHTML");
    	
    
		/*
		 * ArrayList<String> parentAttributes = (ArrayList<String>)
		 * ((JavascriptExecutor)driver).executeScript("return arguments[0].attributes",
		 * ele);
		 * 
		 * 
		 * System.out.println("size" +parentAttributes.size()); for(int i=0;
		 * i<parentAttributes.size(); i++) {
		 * System.out.println(parentAttributes.get(i).toString()); }
		 */
		//System.out.println(ele);
		
		//Verification Condition
		if(Namefield.contains("disabled"))
		{
			System.out.println("The Cluster name field is Disabled");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Cluster properties are working fine");
			driver.findElement(By.cssSelector(".btn-danger")).click();
		}
		else
		{
			System.out.println("The Cluster name field is Enabled");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Cluster properties are not working properly");
			driver.findElement(By.cssSelector(".btn-danger")).click();
			driver.findElement(By.xpath("Name field is disabled")).click();
			
		}
		Thread.sleep(1000); 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 896)
	@Test(priority=3)
	public static void Events(String Dashboardname, ITestContext context) throws InterruptedException
	{
		AllEvents obj=new AllEvents();
		obj.Events(Dashboardname, driver, context);
	}
	
	@TestRail(testCaseId = 898)
	@Parameters({"Dashboardname", "ResourceName", "ResourceType", "ResourcePattern", "EntryPricipal", "Host", "Operation", "PermissionType"})
	@Test(priority=4)
	public void AddManageACL(String Dashboardname, String ResourceName, String ResourceType, String ResourcePattern, String EntryPricipal, String Host, String Operation, String PermissionType, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Click on Checkbox and choose Manage ACLS 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions CopyMousehover=new Actions(driver);
		CopyMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Manage ACLs")).click();
		Thread.sleep(1000);
		
		//Click on Add button
		driver.findElement(By.xpath("//button[contains(.,'Add')]")).click();
		Thread.sleep(2000);
		
		driver.findElement(By.id("resourceName")).sendKeys(ResourceName);
		
		Select Resourcetype=new Select(driver.findElement(By.id("resourceType")));
		Resourcetype.selectByVisibleText(ResourceType);
		Thread.sleep(1000);
		
		Select ResourcePatternType=new Select(driver.findElement(By.id("resourcePatternType")));
		ResourcePatternType.selectByVisibleText(ResourcePattern);
		Thread.sleep(1000);
		
		driver.findElement(By.id("entryPrincipal")).sendKeys(EntryPricipal);
		driver.findElement(By.id("entryHost")).sendKeys(Host);
		
		Select EntryOperation=new Select(driver.findElement(By.id("entryOperation")));
		EntryOperation.selectByVisibleText(Operation);
		Thread.sleep(1000);
		
		Select EntryPermissionType=new Select(driver.findElement(By.id("entryPermissionType")));
		EntryPermissionType.selectByVisibleText(PermissionType);
		Thread.sleep(1000);
		
		//Click on crete button
		driver.findElement(By.xpath("//button[contains(.,'Create')]")).click();
		Thread.sleep(6000);
		
		try 
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e) 
		{
			System.out.println("error popup is not displayed");
		} 
		
		//Search with name
		driver.findElement(By.xpath("//div[3]/div/input")).sendKeys(ResourceName);
		
		//Get text from the result
		String Result=driver.findElement(By.xpath("//div[4]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Result is :" +Result);
		
		for(int i=0; i<=ResourceName.length(); i++)
		{
			driver.findElement(By.xpath("//div[3]/div/input")).sendKeys(Keys.BACK_SPACE);
		}
		Thread.sleep(3000);
		
		if(Result.contains(ResourceName))
		{
			System.out.println("ACL is added to Cluster");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Cluster ACL is added");
		}
		else
		{
			System.out.println("Cluster ACL adding failed");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Cluster ACL adding failed");
    		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
    		driver.findElement(By.id("Add ACL failed")).click();
		}
		
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
	}
	
	@Parameters({"Dashboardname", "ResourceName"})
	@TestRail(testCaseId=899)
	@Test(priority=5)
	public void RemoveACL(String Dashboardname, String ResourceName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Click on Checkbox and choose Manage ACLS 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions CopyMousehover=new Actions(driver);
		CopyMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Manage ACLs")).click();
		Thread.sleep(1000);
		
		//Search with name
		driver.findElement(By.xpath("//div[3]/div/input")).sendKeys(ResourceName);
		
		//Click on remove button
		driver.findElement(By.xpath("//button[contains(.,'Remove')]")).click();
		
		//Click on confirmation yes button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(6000);
		
		//Get text from the result
		String Result=driver.findElement(By.xpath("//div[4]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Result is :" +Result);
		
		for(int i=0; i<=ResourceName.length(); i++)
		{
			driver.findElement(By.xpath("//div[3]/div/input")).sendKeys(Keys.BACK_SPACE);
		}
		Thread.sleep(3000);
		
		if(Result.contains(ResourceName))
		{
			System.out.println("Remove ACL failed");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Remove ACL failed");
    		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
    		driver.findElement(By.id("Remove ACL failed")).click();
		}
		else
		{
			System.out.println("ACL is removed");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "ACL removed");
    		
		}
		
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
		
	}
	
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=897)
	@Test(priority=6)
	public static void AddToFavoriteViewlet(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
    	//Store the Bridge Name into string
		String BridgeName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Create favorite viewlet
		driver.findElement(By.xpath("//button[3]")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		
		/*
		 * Select wgsdropdown=new Select(driver.findElement(By.name("wgs")));
		 * wgsdropdown.selectByVisibleText(WGSName);
		 */
		
		//Select WGS
		driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div/div[2]/div/ng-select/div/span")).click();
		Thread.sleep(2000);
		
		WebElement dropw=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> divw=dropw.findElements(By.tagName("div")); 
		//System.out.println("Number of divs are: " +divw.size());
		
		for(WebElement diw : divw)
		{
			//System.out.println("WGS text is :" +diw.getText());
			if(diw.getText().equalsIgnoreCase(WGSName))
			{
				diw.click();
				break;
			}	
		}
		Thread.sleep(2000);
		
		//Submit
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(MediumSleep);
		
		//Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(3000);
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(4000);

		//Select the favorite viewlet name
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
		
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(6000);
		
		/*
		 * Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(1000);
		 * driver.findElement(By.
		 * cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue"
		 * )).click(); Thread.sleep(1000);
		 */
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification of Bridge added to favorite viewlet
		if(Favdata.contains(BridgeName))
		{
			System.out.println("Cluster name is added to the Favorite viewlet");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Cluster name added successfully to favorite viewlet");
		}
		else
		{
			System.out.println("Cluster name is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to add Cluster name added to favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=27)
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
