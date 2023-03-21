package NavigatorCore;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
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
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class Scheduling {
	static WebDriver driver;
	static String WGS_INDEX;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName;
	static String UploadFilepath;
	static String EMS_WGS_INDEX;
	static String EMS_WGSNAME;
	static String SelectTopicName;
	static String DeleteDurableName;

	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();

		WGS_INDEX = Settings.getWGS_INDEX();
		Screenshotpath = Settings.getScreenshotPath();
		DownloadPath = Settings.getDownloadPath();
		WGSName = Settings.getWGSNAME();
		UploadFilepath = Settings.getUploadFilepath();
		EMS_WGS_INDEX = Settings.getEMS_WGS_INDEX();
		EMS_WGSNAME = Settings.getEMS_WGSNAME();
		SelectTopicName = Settings.getSelectTopicName();
		DeleteDurableName = Settings.getDeleteDurableName();
	}

	@Parameters({ "sDriver", "sDriverpath", "Dashboardname" })
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname) throws Exception {
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname = Settings.getNav_Username();
		String password = Settings.getNav_Password();

		if (sDriver.equalsIgnoreCase("webdriver.chrome.driver")) {
			System.setProperty(sDriver, sDriverpath);
			ChromeOptions options = new ChromeOptions(); 
			options.addArguments("--remote-allow-origins=*");
			driver=new ChromeDriver(options);
		} else if (sDriver.equalsIgnoreCase("webdriver.ie.driver")) {
			System.setProperty(sDriver, sDriverpath);
			driver = new InternetExplorerDriver();
		} else if (sDriver.equalsIgnoreCase("webdriver.edge.driver")) {
			System.setProperty(sDriver, sDriverpath);
			driver = new EdgeDriver();
		} else {
			System.setProperty(sDriver, sDriverpath);
			driver = new FirefoxDriver();
		}

		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);

		// Login page
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(8000);
		
		//Delete if dashboard exists with same name
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboard(driver, Dashboardname);

		// Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);

		// Click on create button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);

	}

	@Parameters({ "ViewletName", "NameOfTheMonth", "YearValue" })
	@TestRail(testCaseId = 335)
	@Test(priority = 1)
	public void ManagerScheduling(String ViewletName, String NameOfTheMonth, String YearValue, ITestContext context)
			throws InterruptedException {

		// Click on Viewlet button
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();

		// Create Manager
		driver.findElement(By.cssSelector(".object-type:nth-child(2)")).click();

		this.CreateViewlet(ViewletName);

		// Click on manager check box and select the properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		
		//Click on General tab
		driver.findElement(By.linkText("General")).click();
		//Schedule description
		driver.findElement(By.id("qmngrDescription")).clear();
		driver.findElement(By.id("qmngrDescription")).sendKeys("Scheduling the Manager Description");

		this.SchedulingProcess(NameOfTheMonth, YearValue);
		
		// Click on OK
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(20000);
		

		this.SchedulerVerification(context);

		// System.out.println("Manager Scheduler is fine");

	}

	@Parameters({ "ViewletName", "NameOfTheMonth", "YearValue" })
	@TestRail(testCaseId = 336)
	@Test(priority = 2)
	public void QueuesScheduling(String ViewletName, String NameOfTheMonth, String YearValue, ITestContext context)
			throws InterruptedException {
		// delete Manager Viewlet
		this.DeleteViewlet();

		// Click on Viewlet button
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();

		// Create Queue
		driver.findElement(By.cssSelector(".object-type:nth-child(3)")).click();

		this.CreateViewlet(ViewletName);

		//put new message option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MMFMousehour = new Actions(driver);
		MMFMousehour.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[6]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[6]/ul/li")).click();
		Thread.sleep(1000);
		
		//Put a message data
		driver.findElement(By.xpath("//*[@id=\"9\"]")).click();
		driver.findElement(By.xpath("//*[@id=\"9\"]")).sendKeys("Schedule Message");
		/*driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		Thread.sleep(2000);*/

		this.SchedulingProcess(NameOfTheMonth, YearValue);
		
		// Click on OK
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(20000);

		this.SchedulerVerification(context);
	}

	@Parameters({ "ViewletName", "NameOfTheMonth", "YearValue" })
	@TestRail(testCaseId = 337)
	@Test(priority = 3)
	public void ChannelScheduling(String ViewletName, String NameOfTheMonth, String YearValue, ITestContext context)
			throws InterruptedException {
		// delete Queue Viewlet
		this.DeleteViewlet();

		// Click on Viewlet button
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();

		// Create Queue
		driver.findElement(By.cssSelector(".object-type:nth-child(4)")).click();

		this.CreateViewlet(ViewletName);

		// Click on Channel check box and select the properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		
		//Channel Description
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Channel description Scheduled");
		Thread.sleep(1000);

		this.SchedulingProcess(NameOfTheMonth, YearValue);
		
		// Click on OK
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(20000);
		this.SchedulerVerification(context);
		// System.out.println("Channel Scheduler is fine");
	}

	@Parameters({ "ViewletName", "NameOfTheMonth", "YearValue" })
	@TestRail(testCaseId = 338)
	@Test(priority = 4)
	public void ProcessScheduling(String ViewletName, String NameOfTheMonth, String YearValue, ITestContext context)
			throws InterruptedException {
		// delete Channel Viewlet
		this.DeleteViewlet();

		// Click on Viewlet button
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();

		// Create Queue
		driver.findElement(By.cssSelector(".object-type:nth-child(5)")).click();

		this.CreateViewlet(ViewletName);

		// Click on Process check box and select the properties option
		driver.findElement(By.xpath(
				"/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		
		//Process Description
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Process description Scheduled");
		Thread.sleep(1000);

		this.SchedulingProcess(NameOfTheMonth, YearValue);
		
		// Click on OK
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(20000);
		this.SchedulerVerification(context);

	}

	@Parameters({ "ViewletName", "NameOfTheMonth", "YearValue" })
	@TestRail(testCaseId = 339)
	@Test(priority = 5)
	public void TopicScheduling(String ViewletName, String NameOfTheMonth, String YearValue, ITestContext context)
			throws InterruptedException {
		// delete Process Viewlet
		this.DeleteViewlet();

		// Click on Viewlet button
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();

		// Create Queue
		driver.findElement(By.cssSelector(".object-type:nth-child(6)")).click();

		this.CreateViewlet(ViewletName);

		// Click on Topic check box and select the properties option
		driver.findElement(By.xpath(
				"/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"))
				.click();
		driver.findElement(By.linkText("Properties...")).click();
		
		//Topic Description
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Topic description Scheduled");
		Thread.sleep(1000);

		this.SchedulingProcess(NameOfTheMonth, YearValue);
		this.SchedulerVerification(context);
	}

	@Parameters({ "ViewletName", "NameOfTheMonth", "YearValue" })
	@TestRail(testCaseId = 340)
	@Test(priority = 6)
	public void ListenerScheduling(String ViewletName, String NameOfTheMonth, String YearValue, ITestContext context)
			throws InterruptedException {
		// delete Topic Viewlet
		this.DeleteViewlet();

		// Click on Viewlet button
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();

		// Create Queue
		driver.findElement(By.cssSelector(".object-type:nth-child(7)")).click();

		this.CreateViewlet(ViewletName);

		// Click on Listener check box and select the properties option
		driver.findElement(By.xpath(
				"/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"))
				.click();
		driver.findElement(By.linkText("Properties...")).click();
		
		//Listener Description
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Listener description Scheduled");
		Thread.sleep(1000);

		this.SchedulingProcess(NameOfTheMonth, YearValue);
		
		// Click on OK
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(20000);
		this.SchedulerVerification(context);
	}

	@Parameters({ "ViewletName", "NameOfTheMonth", "YearValue" })
	@TestRail(testCaseId = 341)
	@Test(priority = 7)
	public void SubScriptionScheduling(String ViewletName, String NameOfTheMonth, String YearValue,
			ITestContext context) throws InterruptedException {
		// delete Listener Viewlet
		this.DeleteViewlet();

		// Click on Viewlet button
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();

		// Create Queue
		driver.findElement(By.cssSelector(".object-type:nth-child(12)")).click();

		this.CreateViewlet(ViewletName);

		// Click on Subscription check box and select the properties option
		driver.findElement(By.xpath(
				"/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"))
				.click();
		driver.findElement(By.linkText("Properties...")).click();
		
		//Subscription User data
		driver.findElement(By.id("userData")).clear();
		driver.findElement(By.id("userData")).sendKeys("Subscription userData Scheduled");
		Thread.sleep(1000);

		this.SchedulingProcess(NameOfTheMonth, YearValue);
		this.SchedulerVerification(context);
	}

	@Parameters({ "ViewletName", "NameOfTheMonth", "YearValue" })
	@TestRail(testCaseId = 342)
	@Test(priority = 8)
	public void RouteScheduling(String ViewletName, String NameOfTheMonth, String YearValue, ITestContext context)
			throws InterruptedException {
		// delete Subscription Viewlet
		this.DeleteViewlet();

		// Click on Viewlet button
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		
		//Create Route viewlet
		WebElement ele=driver.findElement(By.className("left-block")).findElement(By.className("object-types"));
		
		List<WebElement> names=ele.findElements(By.tagName("div"));
		System.out.println(names.size());
		
		for(WebElement click : names)
		{
			if(click.getText().equalsIgnoreCase("Route"))
			{
				click.click();
				break;
			}
		}

		this.CreateViewlet(ViewletName);

		// Click on Route check box and select the properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		
		//Route Connection URL
		driver.findElement(By.id("connectionURL")).clear();
		driver.findElement(By.id("connectionURL")).sendKeys("tcp://127.0.0.1:7222");
		Thread.sleep(1000);

		this.SchedulingProcess(NameOfTheMonth, YearValue);
		
		// Click on OK
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(20000);
		this.SchedulerVerification(context);
	}

	@Parameters({ "ViewletName", "NameOfTheMonth", "YearValue" })
	@TestRail(testCaseId = 343)
	@Test(priority = 9)
	public void BridgeScheduling(String ViewletName, String NameOfTheMonth, String YearValue, ITestContext context)
			throws InterruptedException {
		// delete Route Viewlet
		this.DeleteViewlet();

		// Click on Viewlet button
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		
		//Create Bridge viewlet
		WebElement ele=driver.findElement(By.className("left-block")).findElement(By.className("object-types"));
		
		List<WebElement> names=ele.findElements(By.tagName("div"));
		System.out.println(names.size());
		
		for(WebElement click : names)
		{
			if(click.getText().equalsIgnoreCase("Bridge"))
			{
				click.click();
				break;
			}
		}

		this.CreateViewlet(ViewletName);

		// Click on Bridge check box and select the properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		
		//Bridge selector data
		driver.findElement(By.id("targetSelector")).clear();
		driver.findElement(By.id("targetSelector")).sendKeys("BridgeselectorScheduled");
		Thread.sleep(1000);

		this.SchedulingProcess(NameOfTheMonth, YearValue);
		
		// Click on OK
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(20000);
		this.SchedulerVerification(context);
	}

	@Parameters({ "ViewletName", "NameOfTheMonth", "YearValue", "ScheduleDurableName"})
	@TestRail(testCaseId = 344)
	@Test(priority = 10)
	public void DurableScheduling(String ViewletName, String NameOfTheMonth, String YearValue, String ScheduleDurableName, ITestContext context) throws InterruptedException {
		// delete Bridge Viewlet
		this.DeleteViewlet();

		// Click on Viewlet button
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		
		//Create Durable viewlet
		WebElement ele=driver.findElement(By.className("left-block")).findElement(By.className("object-types"));
		
		List<WebElement> names=ele.findElements(By.tagName("div"));
		System.out.println(names.size());
		
		for(WebElement click : names)
		{
			if(click.getText().equalsIgnoreCase("Durable"))
			{
				click.click();
				break;
			}
		}

		this.CreateViewlet(ViewletName);

		//create durable for schedule
		this.ScheduleDurableFromPlusIcon(ScheduleDurableName, context);

		this.SchedulingProcess(NameOfTheMonth, YearValue);
		
		//Search with that name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ScheduleDurableName);
		
		this.SchedulerVerification(context);
	}

	@Test(priority = 25)
	public void Logout() throws InterruptedException {
		// delete Durable Viewlet
		this.DeleteViewlet();

		try {
			driver.findElement(By.cssSelector(".active .g-tab-btn-close-block")).click();
			// driver.findElement(By.cssSelector(".fa-times")).click();
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(1000);
		} catch (Exception e) {
			System.out.println("Dashboards are not present");
		}
		Thread.sleep(1000);

		// Logout
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		driver.close();
	}

	public void CreateViewlet(String ViewletName) throws InterruptedException {
		// Viewlet Name
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(ViewletName);

		// Select WGS
		Select dd = new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(1000);
		dd.selectByVisibleText(EMS_WGSNAME);

		// driver.findElement(By.xpath("//button[@type='button']")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
	}

	public void SchedulingProcess(String NameOfTheMonth, String YearValue) throws InterruptedException {
		// Click on Schedule
		driver.findElement(By.cssSelector(".btn-green")).click();

		// Click on Date picker
		driver.findElement(By.xpath("//app-datetime-input/div/div/div/button")).click();

		// Select Month
		Select Month = new Select(driver.findElement(By.xpath("//ngb-datepicker-navigation-select/select")));
		Month.selectByVisibleText(NameOfTheMonth);

		// Select Year
		Select Year = new Select(driver.findElement(By.xpath("//select[2]")));
		Year.selectByVisibleText(YearValue);

		// Select Date
		driver.findElement(By.xpath("//div[5]/div[2]/div")).click();

		// Click on OK
		driver.findElement(By.xpath("//app-mod-scheduler/div/div[2]/div/div/div/button")).click();
		Thread.sleep(2000);

		try {
			driver.findElement(By.id("yes")).click();
		} catch (Exception e) {
			System.out.println("Error page is not displayed");
		}

	}

	public void SchedulerVerification(ITestContext context) throws InterruptedException {
		// Get the color of the scheduled object
		String Color = driver.findElement(By.xpath("//datatable-body-cell[3]/div/i")).getAttribute("Style");
		System.out.println(Color);

		/*
		 * //Store the object name before clicking String
		 * ObjectName=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).
		 * getText(); System.out.println(ObjectName); Thread.sleep(4000);
		 * 
		 * //Click Scheduler ICon
		 * driver.findElement(By.cssSelector(".fa-clock-o")).click();
		 * Thread.sleep(1000);
		 * 
		 * //Search the results with object name
		 * driver.findElement(By.id("tag")).clear();
		 * driver.findElement(By.id("tag")).sendKeys(ObjectName);
		 * driver.findElement(By.cssSelector(".btn-primary")).click();
		 * Thread.sleep(1000);
		 * 
		 * //Store the Object name String NodeData=driver.findElement(By.xpath(
		 * "//div/div/ngx-datatable/div/datatable-body")).getText();
		 * System.out.println("Node data is:" +NodeData);
		 * 
		 * for(int i=0; i<=ObjectName.length(); i++) {
		 * driver.findElement(By.id("tag")).sendKeys(Keys.BACK_SPACE); }
		 * Thread.sleep(1000);
		 */

		// Verification of node is added or not
		if (Color.contains("green")) {
			System.out.println("Scheduler is working");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Scheduler is working fine");
		} else {
			System.out.println("Scheduler not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Scheduler is not working properly");
			driver.findElement(By.xpath("Node creation failed")).click();
		}
		Thread.sleep(4000);
	}

	public void DeleteViewlet() throws InterruptedException {
		// Click on Dropdown menu
		driver.findElement(By.id("dropdownMenuButton")).click();
		Thread.sleep(1000);

		// select Edit viewlet option
		driver.findElement(By.linkText("Delete viewlet")).click();
		Thread.sleep(1000);

		// Confirmation ok button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(4000);
	}
	
	public void ScheduleDurableFromPlusIcon(String ScheduleDurableName, ITestContext context) throws InterruptedException
	{
		//Click on + icon
		driver.findElement(By.xpath("//img[@title='Add EMS Durable']")).click();
		
		//Select WGS
		Select WGS=new Select(driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/select")));
		WGS.selectByVisibleText(EMS_WGSNAME);
		
		//Select Node 
		driver.findElement(By.xpath("//div[2]/input")).click();
		driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div")).click();
		Thread.sleep(1000);
		
		//Select Manager
		driver.findElement(By.xpath("//div[2]/ng-select/div")).click();
		driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div")).click();
		Thread.sleep(1000);
		
		//Click on Select path
		driver.findElement(By.xpath("//div[3]/div/div/div/button")).click();
		Thread.sleep(1000);
		
		//Durable Name
		driver.findElement(By.id("durableName")).sendKeys(ScheduleDurableName);
		
		//Topic Name selection
		try 
		{
			driver.findElement(By.id("topicName")).click();
			List<WebElement> TopicList=driver.findElements(By.className("ng-option"));
			System.out.println(TopicList.size());	
			for (int i=0; i<TopicList.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				System.out.println("Radio button id:" + TopicList.get(i).getAttribute("id"));
				String s=TopicList.get(i).getText();
				if(s.equals(SelectTopicName))
				{
					String id=TopicList.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(2000);
	}

	@AfterMethod
	public void tearDown(ITestResult result) throws Exception {

		final String dir = System.getProperty("user.dir");
		String screenshotPath;
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
				// Update attachment to testrail server
				int testCaseID = 0;
				// int status=(int) result.getTestContext().getAttribute("Status");
				// String comment=(String) result.getTestContext().getAttribute("Comment");
				if (result.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(TestRail.class)) {
					TestRail testCase = result.getMethod().getConstructorOrMethod().getMethod()
							.getAnnotation(TestRail.class);
					// Get the TestCase ID for TestRail
					testCaseID = testCase.testCaseId();

					TestRailAPI api = new TestRailAPI();
					api.Getresults(testCaseID, result.getMethod().getMethodName());

				}
			} catch (Exception e) {
				// TODO: handle exception
				// e.printStackTrace();
			}

		}

	}

	public void capturescreen(WebDriver driver, String screenShotName, String status) {
		try {
			TakesScreenshot takesScreenshot = (TakesScreenshot) driver;

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
