package ApodGUI;

import java.io.File;
import java.io.IOException;
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

import Common.AllEvents;
import Common.ClearSelectionofCheckbox;
import Common.Dashboard;
import Common.LogoutForAll;
import Common.Viewlets;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class NodeViewlet {
	static WebDriver driver;
	static String IPAddress;
	static String HostName;
	static String PortNo;
	static String WGSPassword;
	static String Node_hostname;
	static String NodeNameFromIcon;
	static String HostNameFromIcon;
	static String IPAddressFromIcon;
	static String QueueManagerName;
	static String Node_Hostname;
	static String DefaultTransmissionQueue;
	static String WGS_INDEX;
	static String Screenshotpath;
	static String Manager1;
	static String Manager2;
	static String NodeWGS;
	static String WGSName;
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
		IPAddress = Settings.getIPAddress();
		HostName = Settings.getWGS_HostName();
		PortNo = Settings.getWGS_PortNo();
		WGSPassword = Settings.getWGS_Password();
		Node_hostname = Settings.getNode_Hostname();
		NodeNameFromIcon = Settings.getNode_NameFromIcon();
		HostNameFromIcon = Settings.getHostNameFromIcon();
		IPAddressFromIcon = Settings.getIPAddressFromIcon();
		QueueManagerName = Settings.getQueueManagerName();
		Node_Hostname =Settings.getNode_Hostname();
		DefaultTransmissionQueue =Settings.getDefaultTransmissionQueue();
		WGS_INDEX =Settings.getWGS_INDEX();
		Screenshotpath =Settings.getScreenshotPath();
		Manager1 =Settings.getManager1();
		Manager2 =Settings.getManager2();
		NodeWGS =Settings.getNodeWGS();
		WGSName =Settings.getWGSNAME();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
	}

	@Parameters({ "sDriver", "sDriverpath", "Dashboardname", "ViewletValue", "ViewletName"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname, int ViewletValue, String ViewletName) throws Exception {
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		if (sDriver.equalsIgnoreCase("webdriver.chrome.driver")) {
			System.setProperty(sDriver, sDriverpath);
			driver = new ChromeDriver();
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

		Reporter.log("Url to test");
		driver.get(URL);
		driver.manage().window().maximize();
		Reporter.log("Maximizing the window");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);

		// Login page
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(HighSleep);
				
		//Delete if dashboard exists with same name
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboard(driver, Dashboardname);
		Thread.sleep(LowSleep);
		
		//Click on Create button
		//driver.findElement(By.xpath("//app-side-dashboard-menu/div/div/div[2]/div[2]")).click();
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);	
		Thread.sleep(LowSleep);
		//Create button
		driver.findElement(By.xpath("//button[contains(.,'Create')]")).click();
		Thread.sleep(MediumSleep);
		
		// ---- Creating Node Viewlet ----
		Viewlets obj=new Viewlets();
		obj.IBMMQViewletForNode(driver, ViewletValue, ViewletName, WGSName, HostNameFromIcon);
		Thread.sleep(LowSleep);

	}

	@Parameters({"Dashboardname", "SchemaName" })
	@TestRail(testCaseId = 36)
	@Test(priority = 20)
	public static void ShowObjectAttributesforNode(String Dashboardname, String SchemaName, ITestContext context) throws InterruptedException {
		try {

			// ------------ Objects Verification ----------------
			ObjectsVerificationForAllViewlets obj = new ObjectsVerificationForAllViewlets();
			obj.NodeAttributes(Dashboardname, driver, SchemaName);
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show Object Attributes for Node is working fine");
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured while showing object properties, Check details: " + e.getMessage());
			driver.findElement(By.id("Attributes failed")).click();
		}

	}

	@Parameters({"Dashboardname"})
	@Test(priority = 3)
	@TestRail(testCaseId = 37)
	public void ShowTopology(String Dashboardname, ITestContext context) throws InterruptedException {
		
		try {
			
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname,driver);

			// Select Show Topology option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Show Topology")).click();
			Thread.sleep(MediumSleep);
			try
			{
			if (!checkprogress()) {

				System.out.println("exit");
			}
			}
			catch (Exception e)
			{
				
			}
			
			//Click on set and show topology option
			driver.findElement(By.xpath("//button[contains(.,'Set and show topology')]")).click();
			Thread.sleep(HighSleep);

			// Save the topology page data into string
			String Topology = driver.findElement(By.cssSelector("svg")).getText();
		    System.out.println(Topology);
		    Thread.sleep(LowSleep);
		    
		    System.out.println("Manager1 is: " +Manager1);
		    System.out.println("Manager2 is: " +Manager2);

			if (Topology.contains(Manager1) || Topology.contains(Manager2)) {
				System.out.println("Topology page is opened with the list of QM's");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Topology page is opened with the list of QM's");
				driver.findElement(By.xpath("//app-modal-title/div/div[2]/i[2]")).click();
			} else {                    
				System.out.println("Topology page is not opened with the list of QM's");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed opening Topology page with the list of QM's");
				driver.findElement(By.xpath("//app-modal-title/div/div[2]/i[2]")).click();
				driver.findElement(By.xpath("Topology openeing is failed with the list of QM's")).click();
			}

			Thread.sleep(2000);
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment","Exception occured while showing Topology, Check details: " + e.getMessage());
			driver.findElement(By.xpath("//app-modal-title/div/div[2]/i[2]")).click();
			driver.findElement(By.xpath("Topology openeing is failed with the list of QM's")).click();
		}
	}

	@Parameters({"Dashboardname"})
	@Test(priority = 4)
	@TestRail(testCaseId = 38)
	public static void NodeEvents(String Dashboardname, ITestContext context) throws InterruptedException {
		
		AllEvents obj=new AllEvents();
		obj.Events(Dashboardname, driver, context);
	}

	@Parameters({"Dashboardname"})
	@Test(priority = 5)
	@TestRail(testCaseId = 39)
	public static void ManageAndUnmanageNode(String Dashboardname, ITestContext context) throws InterruptedException {
		try {
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname,driver);
			
			// Select Manage option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Manage")).click();
			Thread.sleep(MediumSleep);

			/*
			 * //Refreshing the Viewlet
			 * driver.findElement(By.xpath("//div[2]/div/div/img")).click();
			 * Thread.sleep(2000);
			 * 
			 * //Store the viewlet data into string String
			 * Manage=driver.findElement(By.cssSelector("datatable-body.datatable-body")).
			 * getText(); //System.out.println(Manage);
			 * 
			 * //verification if(Manage.contains("No data to display")) {
			 * System.out.println("Manage Node is done Successfully"); } else {
			 * System.out.println("Manage Node is not Done");
			 * driver.findElement(By.xpath("Manage Node not")).click(); }
			 * Thread.sleep(2000);
			 */
			
			//Clearing selection of object
			ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
			che1.Deselectcheckbox(Dashboardname,driver);

			// Select UnManage option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Manage")).click();
			Thread.sleep(MediumSleep);

			/*
			 * //Refreshing the Viewlet
			 * driver.findElement(By.xpath("//div[2]/div/div/img")).click();
			 * Thread.sleep(2000);
			 * 
			 * //Store the vuewlet data into string String
			 * Unmanage=driver.findElement(By.cssSelector("datatable-body.datatable-body")).
			 * getText(); //System.out.println(Unmanage);
			 * 
			 * //Verification if(Unmanage.contains("M10")) {
			 * System.out.println("UnManage Node is done Successfully"); } else {
			 * System.out.println("UnManage Node is not Done");
			 * driver.findElement(By.xpath("UnManage Node not")).click(); }
			 */
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Manage And Unmanaging Node is working fine");
			Thread.sleep(2000);
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got exception while Manage And Unmanaging Node, check details: " + e.getMessage());
			driver.findElement(By.id("Manage and Unmanage failed")).click();
		}
	}

	@Parameters({"Dashboardname"})
	@Test(priority = 6)
	@TestRail(testCaseId = 40)
	public static void DiscoverNow(String Dashboardname, ITestContext context) throws InterruptedException {
		try {
			
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname,driver);
			
			// Select Incremental option from Discover now
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions MousehoverIncremental = new Actions(driver);
			MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Discover now"))).perform();
			Thread.sleep(LowSleep);
			driver.findElement(By.linkText("Incremental")).click();
			Thread.sleep(MediumSleep);

			//Clearing selection of object
			ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
			che1.Deselectcheckbox(Dashboardname,driver);
			
			// Select Full option from Discover now
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions MousehoverFull = new Actions(driver);
			MousehoverFull.moveToElement(driver.findElement(By.linkText("Discover now"))).perform();
			Thread.sleep(LowSleep);
			driver.findElement(By.linkText("Full")).click();
			Thread.sleep(MediumSleep);
			
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Discover now oprtion is working fine");
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got exception, check details: " + e.getMessage());
			driver.findElement(By.id("Discover failed")).click();
		}

	}

	@Parameters({"Dashboardname"})
	@Test(priority = 7)
	@TestRail(testCaseId = 42)
	public static void PropertiesOfNode(String Dashboardname, ITestContext context) throws InterruptedException 
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
				
		// Select Manage
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Manage")).click();
		Thread.sleep(MediumSleep);
		try {
			//Clearing selection of object
			ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
			che1.Deselectcheckbox(Dashboardname,driver);
			
			// Properties option selection
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Properties...")).click();
			Thread.sleep(HighSleep);

			// Store the editable function in to a string
			boolean FieldNamevalue = driver.findElement(By.xpath("//app-mod-node-properties-identity/div/div[2]/div/input")).isEnabled();
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
			che2.Deselectcheckbox(Dashboardname,driver);
			
			// Select Manage
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Manage")).click();
			Thread.sleep(MediumSleep);
			driver.findElement(By.cssSelector(".btn-danger")).click();
			Thread.sleep(LowSleep);
		}
		Thread.sleep(1000);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che3=new ClearSelectionofCheckbox();
		che3.Deselectcheckbox(Dashboardname,driver);

		// Select Manage
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Manage")).click();
		Thread.sleep(MediumSleep);

	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 45)
	@Test(priority = 8)
	public void CreateNodeUsingIcon(String Dashboardname, ITestContext context) throws Exception 
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Go to Edit Viewlet
		driver.findElement(By.id("dropdownMenuButton")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Edit viewlet")).click();
		Thread.sleep(LowSleep);
		
		WebElement els=driver.findElement(By.className("ng-select-taggable")).findElement(By.className("ng-select-container"));
		List<WebElement> tag=els.findElements(By.tagName("span"));
		System.out.println("Size is: " +tag.size());
		
		for(WebElement e : tag)
		{
			//System.out.println("Title is :" +e.getAttribute("title"));	
			//System.out.println("html :" +e.getAttribute("innerHTML"));
			if(e.getAttribute("title").contains("Clear all"))
			{
				e.click();
				break;
			}
		}
		
		//Click on Apply changes
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
		
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(NodeNameFromIcon);
		Thread.sleep(LowSleep);
		
		String NodeViewletdata1 = driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println("Node data is: " +NodeViewletdata1);
		
		if(NodeViewletdata1.contains(NodeNameFromIcon))
		{
			DeleteNode(Dashboardname, context);
		}

		// Click on + Icon for adding the Node from Node viewlet
		driver.findElement(By.xpath("//img[@title='Add Node']")).click();
		Thread.sleep(LowSleep);

		// Create Node page
		driver.findElement(By.xpath("//app-mod-node-properties-identity/div/div[2]/div/input")).sendKeys(NodeNameFromIcon);
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//div[3]/div/input")).sendKeys(HostNameFromIcon);
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//div[5]/div/input")).sendKeys(IPAddressFromIcon);
		Thread.sleep(LowSleep);
		
		//Select Node type
		Select dd=new Select(driver.findElement(By.xpath("//select")));
		dd.selectByVisibleText("M6-WMQ Agent-managed MQ Node");
		Thread.sleep(LowSleep);

		// Submit
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
				
		try
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e)
		{
			System.out.println("Error popup not displayed");
		}

		// Refresh the viewlet
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(MediumSleep);

		// Store the Viewlet data into string
		String NodeViewletdata = driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println("Node data is: " +NodeViewletdata);

		// Verification
		if (NodeViewletdata.contains(NodeNameFromIcon)) {
			System.out.println("Node is created from + Icon");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Node is created from + Icon");
		} else {
			System.out.println("Node is not created from + Icon");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed creating Node using + Icon");
			driver.findElement(By.xpath("Node Creation Failed")).click();

		}
		Thread.sleep(1000);
	}

	@Parameters({"Dashboardname", "FavoriteViewletName", "Favwgs" })
	@Test(priority = 9)
	public void AddToFavotiteViewlet(String Dashboardname, String FavoriteViewletName, int Favwgs, ITestContext context) throws InterruptedException {
		try {
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname,driver);
			
		Reporter.log("Reading WGS name");
		// Store the WGS name into string
		String NodeName = driver.findElement(By.xpath("//datatable-body-cell[3]/div/span")).getText();

		Reporter.log("Adding Dashboard");
		// Add Dashboard
		driver.findElement(By.cssSelector("div.block-with-border3")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		Reporter.log("Dashboardname: "+ Dashboardname);
		System.out.println("Dashboardname: "+ Dashboardname);
		// Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);

		// Create the favorite viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();

		// Viewlet Name
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
			if(di1.getText().equalsIgnoreCase(WGSName))
			{
				di1.click();
				break;
			}	
		}

		// Submit
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(2000);

		// Back to Workspace page
		driver.findElement(By.xpath("//li/div/div")).click();
		Thread.sleep(1000);

		// Select Add to Favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(1000);

		// Select the favorite viewlet name
		Select fav = new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(1000);

		// Go to Favorite viewlet dashboard page
		driver.findElement(By.xpath("//li[33]")).click();
		Thread.sleep(1000);

		// Store the favorite viewlet data into string
		String FavViewletData = driver.findElement(By.xpath("//datatable-body")).getText();

		if (FavViewletData.contains(NodeName)) {

			System.out.println("Node is added to favorite viewlet");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Node is added to favorite viewlet");
		} else {
			System.out.println("Node is not added to favorite viewlet");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Node Failed adding to favorite viewlet");
			driver.findElement(By.id("Add to favorite failed")).click();
		}
		Thread.sleep(2000);

		// Back to Workspace page
		driver.findElement(By.xpath("//li/div/div")).click();
		Thread.sleep(1000);
		}
		catch(Exception e)
		{
			Reporter.log("Test Case Failed" );
			Reporter.log("Error:  "+ e.getMessage());
			//To capture screenshot path and store the path of the screenshot in the string "screenshotPath"
                        //We do pass the path captured by this mehtod in to the extent reports using "logger.addScreenCapture" method. 			
			File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			try {
				FileHandler.copy(scrFile,new File("D:\\SCREENSHOTS\\Workgroup server\\AddToFavotiteViewlet.png"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Node Failed adding to favorite viewlet, check details: "+ e.getMessage());
			 String screenshotPath ="D:\\SCREENSHOTS\\Workgroup server\\AddToFavotiteViewlet.png";
			// String htmlText = new String("<img src=\"\\\"file://\"\" alt=\"\\\"\\\"/\" />");
			 String path ="<img src=\" "+ screenshotPath +"\" alt=\"\"\"/\" />";
			//To add it in the report 
			 Reporter.log(path);
			// Reporter.log(screenshotPath);
			driver.findElement(By.xpath("failed buton")).click();
			 
			// logger.log(LogStatus.FAIL, logger.addScreenCapture(screenshotPath));
		}
	}
	

	@Parameters({"Dashboardname"})
	@Test(priority = 10)
	@TestRail(testCaseId = 43)
	public static void CompareTwoNodes(String Dashboardname, ITestContext context) throws InterruptedException 
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
				
		//Get the First object Name                         
		String compare1 = driver.findElement(By.xpath("//datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span")).getText();
		System.out.println("First obj name is: " +compare1);
		
		//Get the second object name
		String compare2 = driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span")).getText();
		System.out.println("Second obj name is: " +compare2);
		
		// Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();

		// System.out.println("Cpmare to: " + compare1 + "::"+ compare2);
		String comparenameslist = compare1  +" Attribute Value"+ "::"+ compare2  +" Attribute Value";
		driver.findElement(By.linkText("Compare")).click();
		Thread.sleep(LowSleep);
		System.out.println("Before names are: " +comparenameslist);


		// Reading comparing
		String aftercompare1 = driver.findElement(By.xpath("//th[2]")).getText();
		String aftercompare2 = driver.findElement(By.xpath("//th[3]")).getText();
		String verifycomparenamelist = aftercompare1 + "::" + aftercompare2;
		System.out.println("After names are: " +verifycomparenamelist);

		if (verifycomparenamelist.compareTo(comparenameslist) == 0) {
			System.out.println("Compare page is opened with selected object names");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Compare option is working fine");
		} else {
			System.out.println("Compare page is not opened with selected objetcs");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to compare option");
			driver.findElement(By.cssSelector(".close-button")).click();
			driver.findElement(By.xpath("Comparision failed")).click();
		}
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(2000);
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 762)
	@Test(priority=11)
	public void CheckDifferencesForNodes(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		// Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Compare")).click();
		Thread.sleep(LowSleep);
		
		// Check differences only option while compare
		driver.findElement(By.cssSelector("div.differences > label.switch > span.slider.round")).click();
		Thread.sleep(LowSleep);
		try {

			List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr"));
			System.out.println("AttributesData count: " + AttributesData.size());

			boolean verifydiff = false;
			String difference1=driver.findElement(By.xpath("//tr[4]/td[2]")).getText();
			System.out.println("First value" +difference1);
			String difference2=driver.findElement(By.xpath("//tr[4]/td[3]")).getText();
			System.out.println("Second value" +difference2);

			if(!(difference1.isEmpty() && difference2.isEmpty()))
			{
			for (int i = 0; i < AttributesData.size(); i++) 
			{
				String cls = AttributesData.get(i).getAttribute("style");
				System.out.println("classname: "+ cls);
				if (!cls.contains("display: none")) 
					{
					System.out.println("index: " + i);
					String secondvalue;
					String firstvalue;
					if (i == 0)
					{
						firstvalue = driver.findElement(By.xpath("//td[2]")).getText();
						System.out.println("First value" + firstvalue);
						secondvalue = driver.findElement(By.xpath("//td[3]")).getText();
						System.out.println("Second value" + secondvalue);
						
						if (!firstvalue.equalsIgnoreCase(secondvalue)) 
						{
							verifydiff = true;
						}
					} 
					else
					{
						int j = i + 1;
						System.out.println("index changed: " + j);
						firstvalue = driver.findElement(By.xpath("//tr[" + j + "]/td[2]")).getText();
						System.out.println("First value" + firstvalue);
						secondvalue = driver.findElement(By.xpath("//tr[" + j + "]/td[3]")).getText();
						System.out.println("Second value" + secondvalue);
						if (!firstvalue.equalsIgnoreCase(secondvalue)) 
						{
							verifydiff = true;
						}
					}
				}

				}
			
			}
			else
			{
			verifydiff=true;
			}

			//System.out.println("");
			if (!verifydiff) {
				System.out.println("Popup showing the same values Differences");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Differences is not working");
				driver.findElement(By.xpath("Differences")).click();
			} else {
				System.out.println("Popup showing the Different values");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Showing the different values");

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Popup showing the same values Differences");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception while differentiate object values, check details: " + e.getMessage());
			driver.findElement(By.cssSelector(".close-button")).click();
			driver.findElement(By.xpath("Differences")).click();
		}
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);
	}

	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 41)
	@Test(priority = 12, dependsOnMethods= {"CreateNodeUsingIcon"})
	public static void DeleteNode(String Dashboardname, ITestContext context) throws Exception 
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
				
		// Search with node name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(NodeNameFromIcon);
		Thread.sleep(LowSleep);
		
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		String Options=driver.findElement(By.id("dropdown-block")).getText();
		System.out.println("Options are: " +Options);
		
		if(Options.contains("Delete"))
		{
			// Delete the Node
			//driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Delete")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.id("accept-true")).click();
			Thread.sleep(MediumSleep);
		}
		else
		{
			//Unmanage the Node
			driver.findElement(By.linkText("Manage")).click();
			Thread.sleep(LowSleep);
			
			//Clearing selection of object
			ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
			che1.Deselectcheckbox(Dashboardname,driver);
			
			// Search with node name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(NodeNameFromIcon);
			Thread.sleep(LowSleep);
			
			// Delete the Node
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Delete")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.id("accept-true")).click();
			Thread.sleep(MediumSleep);
			
		}	

		// Edit the search field data
		for (int j = 0; j <= NodeNameFromIcon.length(); j++) {

			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		Thread.sleep(LowSleep);

		// Store the viewlet data after deleting the node
		String AfterDeleting = driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println("After Deleting Node:" + AfterDeleting);

		// Deleted node verification
		if (AfterDeleting.contains(NodeNameFromIcon)) {
			System.out.println("Node is not deleted");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Deletion of node is not working");
			driver.findElement(By.xpath("Node not deleted")).click();
		} else {
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Node is deleted");
			System.out.println("Node is deleted successfully");
		}
		Thread.sleep(2000);
	}

	@Parameters({"Dashboardname", "FavoriteViewletName" })
	@Test(priority = 12)
	public void AddtoFavoriteForMultipleNodes(String Dashboardname, String FavoriteViewletName,ITestContext context) throws InterruptedException {
		try {
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname,driver);
			
		// Store two nodes data into string
		String NodeName2 = driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span")).getText();
		String NodeName3 = driver.findElement(By.xpath(
				"//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span"))
				.getText();

		// Select compare option for comparing the two nodes
		driver.findElement(By.xpath(
				"/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"))
				.click();
		driver.findElement(By.xpath(
				"/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"))
				.click();
		driver.findElement(By.xpath("//ul[2]/li")).click();
		Thread.sleep(2000);

		// Select the favorite viewlet name
		Select fav = new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(1000);

		// Go to Favorite viewlet dashboard page
		driver.findElement(By.xpath("//li[2]")).click();
		Thread.sleep(1000);

		// Store the favorite viewlet data into string
		String FavViewletData = driver.findElement(By.xpath("//datatable-body")).getText();

		if (FavViewletData.contains(NodeName2) && FavViewletData.contains(NodeName3)) {
			System.out.println("Multiple Nodes are added to favorite viewlet");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Multiple Nodes are added to favorite viewlet");
		} else {
			System.out.println("Multiple Nodes are not added to favorite viewlet");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Multiple Nodes are not added to favorite viewlet");
			driver.findElement(By.id("Add to favorite failed")).click();
		}
		Thread.sleep(2000);

		// Back to Workspace page
		driver.findElement(By.xpath("//li/div/div")).click();
		Thread.sleep(1000);
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Multiple Nodes are not added to favorite viewlet, check details: "+ e.getMessage());
			driver.findElement(By.xpath("custom failed condition")).click();
		}

	}

	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 44)
	@Test(priority = 19)
	public static void SearchFilter(String Dashboardname, ITestContext context) throws InterruptedException 
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Store the node name into string
		String NodeNameForSearch=driver.findElement(By.xpath("//datatable-body-cell[3]/div/span")).getText();
		
		// Enter the search input data into search box
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(NodeNameForSearch);
		Thread.sleep(LowSleep);

		// Store the Viewlet data into string
		String Viewletdata = driver.findElement(By.xpath("//datatable-body")).getText();
		// System.out.println(Viewletdata);

		// Verification
		if (Viewletdata.toUpperCase().contains(NodeNameForSearch.toUpperCase())) {
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Search is working fine");
			System.out.println("Search is working fine");
		} else {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Search is not working fine");
			System.out.println("Search is not working fine");
			driver.findElement(By.xpath("Search is failed")).click();
		}
		Thread.sleep(2000);

		// Clear the search data
		for (int k = 0; k <= NodeNameForSearch.length(); k++) {
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		Thread.sleep(2000);

		// Refresh the viewlet
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(MediumSleep);

	}

	@Parameters({"Dashboardname", "Description"})
	@TestRail(testCaseId = 46)
	@Test(priority = 14)
	public void CreateQueueManagerFromNodeViewletOptions(String Dashboardname, String Description, ITestContext context) throws InterruptedException {
		try {
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname,driver);
			
			
			//Go to Edit Viewlet
			driver.findElement(By.id("dropdownMenuButton")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.linkText("Edit viewlet")).click();
			Thread.sleep(LowSleep);
			
			//Select node value
			driver.findElement(By.cssSelector(".ng-select-taggable .ng-arrow-wrapper")).click();
			Thread.sleep(LowSleep);
			 try 
				{
		        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
					List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
					System.out.println(mdivs.size());	
					
					for (WebElement mdi : mdivs)
					{
						if(mdi.getText().equals(Node_Hostname))
						{
							mdi.click();
							break;
						}
					}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		        Thread.sleep(1000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
			
		// Select Create Queue manager option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Create Queue Manager")).click();
		Thread.sleep(LowSleep);

		// Queue Details
		driver.findElement(By.xpath("//app-qmgrcreatestep1/div/div[2]/div/input")).sendKeys(QueueManagerName);
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//div[4]/div/input")).sendKeys(DefaultTransmissionQueue);
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//textarea[@type='text']")).sendKeys(Description);
		Thread.sleep(LowSleep);

		// Next button
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		Thread.sleep(LowSleep);

		// driver.findElement(By.xpath("(//input[@type='text'])[9]")).sendKeys("New
		// Manager");
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		Thread.sleep(LowSleep);

		// Log Path
		// driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys("Desktop");
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		Thread.sleep(LowSleep);

		// Data Path
		// driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys("Test
		// data path");
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		Thread.sleep(LowSleep);

		// Final Submit
		driver.findElement(By.xpath("//button[contains(.,'Finish')]")).click();
		Thread.sleep(HighSleep);
		
		try
		{
		if (!checkprogress()) {

			System.out.println("exit");
		}
		}
		catch (Exception e)
		{
			
		}
		
		try
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}

		/*try {
			// Get Error Message
			String Errorpopup = driver.findElement(By.xpath("//app-mod-errors-display/div/div")).getText();
			System.out.println(Errorpopup);                  
			driver.findElement(By.id("yes")).click();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got some exception, check details: "+ e.getMessage());
			System.out.println("No message is displaying");
		}*/
		
		for(int i=0; i<=Node_Hostname.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Click on Viewlet button
		driver.findElement(By.xpath("//button[3]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click();
		Thread.sleep(LowSleep);
		
		//Select IBM MQ product
		driver.findElement(By.cssSelector(".field-workgroup-input > .ng-select-container")).click();
		Thread.sleep(LowSleep);  
		
		WebElement drop=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div=drop.findElements(By.tagName("div")); 
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement di : div)
		{
			//System.out.println("text is :" +di.getText());
			if(di.getText().equalsIgnoreCase("IBM MQ"))
			{
				di.click();
				break;
			}	
		}
		Thread.sleep(3000);
		
		//Create Manager
		driver.findElement(By.cssSelector(".object-type:nth-child(2)")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys("Manager Viewlet");
		Thread.sleep(LowSleep);
		
		//Select WGS type
		driver.findElement(By.xpath("//div/div/div[2]/div/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(LowSleep);   
		
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
				
		driver.findElement(By.id("save-viewlet")).click();
		Thread.sleep(HighSleep);
		
		//Search with queue manager name
		driver.findElement(By.xpath("(//input[@type='text'])[6]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys(QueueManagerName);
		Thread.sleep(LowSleep);
		
		// Store viewlet data into string
		String Favdata = driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println(Favdata);
		
		for(int j=0; j<=QueueManagerName.length(); j++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys(Keys.BACK_SPACE);
		}
		Thread.sleep(2000);
		
		if(Favdata.contains(QueueManagerName))
		{
			System.out.println("Queue manager is added successfully");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Queue manager is added successfully");
		}
		else
		{
			System.out.println("Queue manager is not added");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Queue manager is not added");
			driver.findElement(By.id("Add Queue manager failed")).click();
		}
		
		}catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got exception while adding Queue Manager, check details: "+ e.getMessage());
			driver.findElement(By.xpath("Got exception")).click();
		}
	}

	@Parameters({"Dashboardname"})
	@Test(priority = 22)
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
