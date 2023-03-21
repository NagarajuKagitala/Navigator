package NavigatorCore;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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

import Common.Dashboard;
import Common.LogoutForAll;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class ManageDashboardOptions 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName_CreateDashboard;
	static String EMSWGS_CreateDashboard;
	static String Node_Hostname;
	static String Low;
	static String Medium;
	static String High;
	static int LowSleep;
	static int MediumSleep;
	static int HighSleep;
	static String DashboardNames;
	static String TagNames;
	static String UploadDashboard;
	static String UploadFilepath;

	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		DownloadPath =Settings.getDownloadPath();
		WGSName_CreateDashboard =Settings.getWGSName_CreateDashboard();
		EMSWGS_CreateDashboard =Settings.getEMSWGS_CreateDashboard();
		Node_Hostname =Settings.getNode_Hostname();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
		UploadDashboard=Settings.getUploadDashboard();
		UploadFilepath=Settings.getUploadFilepath();
		
	}
		
	@Parameters({"sDriver", "sDriverpath", "Dashboardname"})
	@Test
	public void Login(String sDriver, String sDriverpath, String Dashboardname) throws Exception
	{
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		String filepath=System.getProperty("user.dir") + "\\" + DownloadPath;
		
		//Select the required browser for running the script
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.prompt_for_download", "false");
			chromePrefs.put("download.default_directory", filepath);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			options.addArguments("--remote-allow-origins=*");
			driver=new ChromeDriver(options);
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
			FirefoxOptions options=new FirefoxOptions();
			options.setCapability("marionette", false);
			driver=new FirefoxDriver(options);
		}
		
		//Enter the URL into browser and Maximize the browser 
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		
		//Login
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(HighSleep);	
		
		Dashboard object=new Dashboard();
		object.DeleteExistDashboard(driver, Dashboardname);
	}
	
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 1149)
	@Test(priority=1)
	public void CreareDashboardUsingNewButton(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//click on dashboard button
		driver.findElement(By.id("add-dashboard")).click();
		Thread.sleep(LowSleep);
		
		//Click on new button
		driver.findElement(By.xpath("//button[contains(.,' New')]")).click();
		Thread.sleep(LowSleep);
		
		//give the dashboard name
		driver.findElement(By.id("dashboardName")).sendKeys(Dashboardname);
		Thread.sleep(LowSleep);
		
		//Click on create button
		driver.findElement(By.xpath("//button[contains(.,'Create')]")).click();
		Thread.sleep(MediumSleep);
		
		ListofDashboards();
		System.out.println("dashboards are : "+DashboardNames);
		
		if(DashboardNames.contains(Dashboardname))
		{
			System.out.println("Dashboard is created using new button");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Dashboard is created successfully");
			
		}
		else
		{
			System.out.println("Dashboard is not created using new button");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to create Dashboard");
			driver.findElement(By.id("Create dashboard failed")).click();
		}
	}
	
	
	@Parameters({"Dashboardname", "RenamedDashboardName"})
	@TestRail(testCaseId = 1150)
	@Test(priority=2)
	public void RenameDashboardUsingRenamebutton(String Dashboardname, String RenamedDashboardName, ITestContext context) throws InterruptedException
	{
		WebElement cla=driver.findElement(By.tagName("app-mod-manage-dashboard"));
		List<WebElement> lis=cla.findElements(By.tagName("div"));
		System.out.println("No of dashboards are: " +lis.size());
		
		
		for(WebElement li: lis)
		{
			//System.out.println("innet: " +li.getAttribute("innerHTML"));
			String clas=li.getAttribute("class");
			//System.out.println("class name : " +clas);
			
			if(clas.contains("maf-container"))
			{
				WebElement lef=li.findElement(By.className("left-side")).findElement(By.tagName("table"));
				List<WebElement> trs=lef.findElements(By.tagName("tr"));
				System.out.println("number of trs: " +trs.size());
				
				for(WebElement fi:trs)
				{
					if(fi.getText().equalsIgnoreCase(Dashboardname))
					{
						fi.click();
						Thread.sleep(LowSleep);
						break;
					}
				}
				break;
			}	
		}
		
		//Click on Rename button
		driver.findElement(By.xpath("//button[contains(.,' Rename')]")).click();
		Thread.sleep(LowSleep);
		
		//Give the new name for dashboard
		driver.findElement(By.name("tabName")).clear();
		driver.findElement(By.name("tabName")).sendKeys(RenamedDashboardName);
		Thread.sleep(LowSleep);
		
		//Click on Ok button
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(MediumSleep);
		
		ListofDashboards();
		System.out.println("dashboards are : "+DashboardNames);
		if(DashboardNames.contains(RenamedDashboardName))
		{
			System.out.println("Dashboard is Renamed using Rename button");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Dashboard is Renamed successfully");
			
		}
		else
		{
			System.out.println("Dashboard is not Renamed using Rename button");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to Rename Dashboard");
			driver.findElement(By.id("Rename dashboard failed")).click();
		}
	}
	
	@Parameters({"RenamedDashboardName"})
	@TestRail(testCaseId = 1151)
	@Test(priority=3)
	public void DeleteDashboardUsingDeleteButton(String RenamedDashboardName, ITestContext context) throws InterruptedException
	{
		//Delete the existing dashboard
		driver.findElement(By.xpath("//button[contains(.,' Delete')]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(MediumSleep);
				
		ListofDashboards();
		System.out.println("dashboards are : "+DashboardNames);
		if(DashboardNames.contains(RenamedDashboardName))
		{
			System.out.println("Dashboard is not deleted using deleted button");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to delete Dashboard");
			driver.findElement(By.id("delete dashboard failed")).click();
						
		}
		else
		{
			System.out.println("Dashboard is Deleted using Delete button");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Dashboard is deleted successfully");
		}
	}
	
	@Parameters({"TagName"})
	@TestRail(testCaseId = 1152)
	@Test(priority=4)
	public void CreateTagUsingAddButton(String TagName, ITestContext context) throws InterruptedException 
	{
		//click on dashboard button
		//driver.findElement(By.id("add-dashboard")).click();
		//Thread.sleep(4000);
				
		//Select dashboard
		driver.findElement(By.xpath("//td/input")).click();
		Thread.sleep(LowSleep);
		
		//Click on tag button
		driver.findElement(By.xpath("//button[contains(.,' Tags')]")).click();
		Thread.sleep(LowSleep);
		//Click on +add button
		driver.findElement(By.xpath("//app-mod-manage-dashboard-tags/div[3]/div/div/div/button")).click();
		Thread.sleep(LowSleep);
		//give the dashboard name   
		driver.findElement(By.xpath("//app-mod-manage-dashboard-tags-properties/div[2]/input")).sendKeys(TagName);
		Thread.sleep(LowSleep);
		
		//Click on Add button
		driver.findElement(By.xpath("//app-mod-manage-dashboard-tags-properties/div[2]/button")).click();
		Thread.sleep(LowSleep);
		//Click on save button
		driver.findElement(By.xpath("//div[4]/div/div/div/button")).click();
		Thread.sleep(MediumSleep);
		
		//Get the tags
		ListOfTags();
		
		if(TagNames.contains(TagName))
		{
			System.out.println("Tag is added successfully");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Tag is added successfully");
		}
		else
		{
			System.out.println("Tag is not added");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add tag");
			driver.findElement(By.id("Add tag failed")).click();
		}
		
	}
	
	@Parameters({"TagName", "NameTagName"})
	@TestRail(testCaseId = 1153)
	@Test(priority=5)
	public void UpdateTagNameUsingEditButton(String TagName, String NameTagName, ITestContext context) throws InterruptedException
	{
		WebElement ta=driver.findElement(By.className("parts-container")).findElement(By.className("left-part")).findElement(By.className("filter-table"));
		List<WebElement> trs=ta.findElements(By.tagName("tr"));
		System.out.println("size of trs: " +trs.size());
		for(WebElement tds:trs)
		{
			if(tds.getAttribute("class").contains("ng-star-inserted"))
			{
				System.out.println("tag name :" +tds.getText());
				
				if(tds.getText().equalsIgnoreCase(TagName))
				{
					tds.click();
					break;
				}
			}
		}
		
		
		//Click on edit button 
		driver.findElement(By.xpath("//app-mod-manage-dashboard-tags/div[3]/div/div/div[2]/button")).click();
		Thread.sleep(LowSleep);
		//enter TagName
		driver.findElement(By.xpath("//div[3]/div/div/input")).clear();
		driver.findElement(By.xpath("//div[3]/div/div/input")).sendKeys(NameTagName);
		Thread.sleep(LowSleep);		
		//Save Changes 
		driver.findElement(By.xpath("//div[4]/div/div/div/button")).click();		
		Thread.sleep(MediumSleep);
		
		//Get the tags
		ListOfTags();
		
		if(TagNames.contains(NameTagName))
		{
			System.out.println("Tag is Updated successfully");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Tag is Updated successfully");
		}
		else
		{
			System.out.println("Tag is not Updated");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to updated tag");
			driver.findElement(By.id("update tag failed")).click();
		}
				
	}
	
	@Parameters({"NameTagName"})
	@TestRail(testCaseId = 1154)
	@Test(priority=6)
	public void DeleteTagUsingDeleteButton(String NameTagName, ITestContext context) throws InterruptedException
	{
		//Click on Delete button
		driver.findElement(By.xpath("//app-mod-manage-dashboard-tags/div[3]/div/div/div[3]/button")).click();
		Thread.sleep(LowSleep);
		
		//Click on confirmation yes button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(MediumSleep);
		
		//Get the list of tags
		ListOfTags();
		
		if(TagNames.contains(NameTagName))
		{
			System.out.println("Tag is not deleted");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to delete tag");
			//Clikc on Cancel button
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(4000);
			driver.findElement(By.id("Delete tag failed")).click();
			
		}
		else
		{
			System.out.println("Tag is deleted");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Tag is Deleted successfully");
		}
		
		//Clikc on Cancel button
		driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
		Thread.sleep(MediumSleep);
	}
	

	//@Parameters({"ExportDashboardname"})
	@Test(priority=7)
	@TestRail(testCaseId = 1155)
	public static void ExportDashboard(ITestContext context) throws InterruptedException
	{
		try
		{
			//Click on Export button
			driver.findElement(By.xpath("//button[contains(.,' Export')]")).click();
			Thread.sleep(MediumSleep);
			
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages exported successfully");
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got exception while exporting messages, check details: "+ e.getMessage());
			driver.findElement(By.id("Export Dashboard failed")).click();
		}
		    
	}
	
	
	@Parameters({"ImportedDashboardname"})
	@Test(priority=8)
	public void ImportDashboard(String ImportedDashboardname, ITestContext context) throws InterruptedException, AWTException
	{
		driver.findElement(By.id("add-dashboard")).click();
		Thread.sleep(LowSleep);
		
		//click on Import button
		driver.findElement(By.xpath("//button[contains(.,' Import')]")).click();
		Thread.sleep(MediumSleep);
		
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", driver.findElement(By.id("fileSelect")));
		Thread.sleep(MediumSleep);
				
		//Loading the file into queue by using robot class
		String filepath1=System.getProperty("user.dir") + "\\" + UploadDashboard;
		StringSelection stringSelection = new StringSelection(filepath1);
		System.out.println("upload: " +UploadDashboard);
		System.out.println("file path : " +filepath1);
		//StringSelection stringSelection = new StringSelection(UploadFilepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		//robot.keyPress(KeyEvent.VK_CONTROL);
		//robot.keyPress(KeyEvent.VK_V);
		//robot.keyRelease(KeyEvent.VK_V);
		//robot.keyRelease(KeyEvent.VK_CONTROL);
		//Thread.sleep(2000);
	    //robot.keyPress(KeyEvent.VK_ENTER);
	    //robot.keyRelease(KeyEvent.VK_ENTER);
	    //Thread.sleep(10000);
	    Actions a=new Actions(driver);
	    a.sendKeys(Keys.CONTROL+ "V").sendKeys(Keys.ENTER).build().perform();
	    Thread.sleep(HighSleep);

	    
		/*
		 * try { if (!checkprogress()) {
		 * 
		 * System.out.println("exit"); } } catch (Exception e) {
		 * 
		 * } Thread.sleep(6000);
		 */
	    
	    //Uncheck Add new dashboard to current view checkbox
	    driver.findElement(By.id("id_add_to_view")).click();
		Thread.sleep(MediumSleep);
		
	    //Click on Import Button
	    driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
		Thread.sleep(HighSleep);
		
		//Click on dashboard
		driver.findElement(By.id("add-dashboard")).click();
		Thread.sleep(MediumSleep);
		
		//Get the list of dashboards
		ListofDashboards();
		
		System.out.println("dashboards are : "+DashboardNames);
		if(DashboardNames.contains(ImportedDashboardname))
		{
			System.out.println("Dashboard is imported successfully");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Dashboard is imported successfully");
			
		}
		else
		{
			System.out.println("Dashboard is not imported");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to import Dashboard");
			driver.findElement(By.id("import dashboard failed")).click();
		}
	}
	
	
	@Parameters({"ImportedDashboardname"})
	@Test(priority=9)
	public void AddToCurrentViewButton(String ImportedDashboardname, ITestContext context) throws InterruptedException
	{
		WebElement cla=driver.findElement(By.tagName("app-mod-manage-dashboard"));
		List<WebElement> lis=cla.findElements(By.tagName("div"));
		System.out.println("No of dashboards are: " +lis.size());
		
		
		for(WebElement li: lis)
		{
			//System.out.println("innet: " +li.getAttribute("innerHTML"));
			String clas=li.getAttribute("class");
			//System.out.println("class name : " +clas);
			
			if(clas.contains("maf-container"))
			{
				WebElement lef=li.findElement(By.className("left-side")).findElement(By.tagName("table"));
				List<WebElement> trs=lef.findElements(By.tagName("tr"));
				System.out.println("number of trs: " +trs.size());
				
				for(WebElement fi:trs)
				{
					if(fi.getText().equalsIgnoreCase(ImportedDashboardname))
					{
						fi.click();
						Thread.sleep(MediumSleep);
						break;
					}
				}
				break;
			}	
		}
		
		//Click on Add to current view button
		driver.findElement(By.xpath("div[7]/button")).click();
		Thread.sleep(MediumSleep);
		
		//Clikc on Cancel button
		driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
		Thread.sleep(LowSleep);
		
		WebElement cla2=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis2=cla2.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis2.size());
		
		StringBuffer buffer=new StringBuffer();
		for(WebElement li2: lis2)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi2=li2.findElement(By.className("g-tab-title"));
			//System.out.println("Names are: " +fi.getText());
			buffer.append(fi2.getText());
			buffer.append(",");
		}
		
		String DashboardNames=buffer.toString();
		System.out.println("List of dashboards are: " +DashboardNames);
		
		if(DashboardNames.contains(ImportedDashboardname))
		{
			System.out.println("Dashboard is Added to current view");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Dashboard is Added to current view successfully");
			
		}
		else
		{
			System.out.println("Dashboard is not added to current view");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to Add Dashboard to current view");
			driver.findElement(By.id("Add to current view failed")).click();
		}
		
		//Delete dashboard
		Dashboard object=new Dashboard();
		object.DeleteExistDashboard(driver, ImportedDashboardname);
	}
			
	
	@Parameters({"Dashboardname"})
	@Test(priority=20)
	public void Logout(String Dashboardname) throws InterruptedException 
	{
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, Dashboardname);
	}
	
	public void ListofDashboards()
	{
		WebElement cla=driver.findElement(By.tagName("app-mod-manage-dashboard"));
		List<WebElement> lis=cla.findElements(By.tagName("div"));
		System.out.println("No of dashboards are: " +lis.size());
		
		StringBuffer buffer=new StringBuffer();
		for(WebElement li: lis)
		{
			//System.out.println("innet: " +li.getAttribute("innerHTML"));
			String clas=li.getAttribute("class");
			//System.out.println("class name : " +clas);
			
			if(clas.contains("maf-container"))
			{
				WebElement lef=li.findElement(By.className("left-side")).findElement(By.tagName("table"));
				List<WebElement> trs=lef.findElements(By.tagName("tr"));
				System.out.println("number of trs: " +trs.size());
				
				for(WebElement fi:trs)
				{
					System.out.println("dashboard name is: " +fi.getText());
					buffer.append(fi.getText());
					buffer.append(",");
				}
				
			}
		}
		
		DashboardNames=buffer.toString();
		System.out.println("List of dashboards are: " +DashboardNames);
	}
	
	public void ListOfTags()
	{
		WebElement ta1=driver.findElement(By.className("parts-container")).findElement(By.className("left-part")).findElement(By.className("filter-table"));
		List<WebElement> trs1=ta1.findElements(By.tagName("tr"));
		System.out.println("size of trs: " +trs1.size());
		
		StringBuffer buffer=new StringBuffer();
		for(WebElement tds1:trs1)
		{
			if(tds1.getAttribute("class").contains("ng-star-inserted"))
			{
				System.out.println("tag name :" +tds1.getText());
				buffer.append(tds1.getText());
				buffer.append(",");
			
			}
		}
		
		TagNames=buffer.toString();
		System.out.println("List of tags are: " +TagNames);
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

