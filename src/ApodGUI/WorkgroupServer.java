package ApodGUI;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
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

import Common.ClearSelectionofCheckbox;
import Common.Dashboard;
import Common.LogoutForAll;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class WorkgroupServer {
	static WebDriver driver;
	static String IPAddress;
	static String HostName;
	static String PortNo;
	static String WGSPassword;
	String VerificationData;
	String Screenshotpath;
	static String NodeName;
	static String Node_PortNumber;
	static String Node_Hostname;
	static String Node_IPAddress;
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
		IPAddress = Settings.getIPAddress();
		HostName = Settings.getWGS_HostName();
		PortNo = Settings.getWGS_PortNo();
		WGSPassword = Settings.getWGS_Password();
		VerificationData = Settings.getVerificationData();
		Screenshotpath = Settings.getScreenshotPath();
		NodeName = Settings.getNodeName();
		Node_PortNumber = Settings.getNode_PortNumber();
		Node_Hostname =Settings.getNode_Hostname();
		Node_IPAddress =Settings.getNode_IPAddress();
		WGSName =Settings.getWGSNAME();
		KafkaNodeName=Settings.getKafkaNodeName();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
	}

	@Test
	@Parameters({"sDriver", "sDriverpath", "NewDashboardname"})
	public void Login(String sDriver, String sDriverpath, String NewDashboardname, ITestContext context) throws Exception {

		Settings.read();
		String URL = Settings.getSettingURL();

		// Select the required browser for running the script
		if (sDriver.equalsIgnoreCase("webdriver.chrome.driver")) {
			System.setProperty(sDriver, sDriverpath);
			ChromeOptions options = new ChromeOptions(); 
			options.addArguments("--remote-allow-origins=*");
			driver=new ChromeDriver(options);
		} else if (sDriver.equalsIgnoreCase("webdriver.edge.driver")) {
			System.setProperty(sDriver, sDriverpath);
			driver = new EdgeDriver();
		} else if (sDriver.equalsIgnoreCase("webdriver.ie.driver")) {
			System.setProperty(sDriver, sDriverpath);
			driver = new InternetExplorerDriver();
		} else {
			System.setProperty(sDriver, sDriverpath);
			FirefoxOptions options = new FirefoxOptions();
			options.setCapability("marionette", false);
			driver = new FirefoxDriver(options);
		}

		// Enter the URL into browser and Maximize the browser
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);

		// Login
		driver.findElement(By.id("username")).sendKeys(Settings.getNav_Username());
		driver.findElement(By.id("password")).sendKeys(Settings.getNav_Password());
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(8000);
		
		//delete if dashboard exists
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboardForWGSExpert(driver, NewDashboardname);
		
		//Click on Create button
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(NewDashboardname);
		
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(4000);
		
		WebElement cla=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis=cla.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis.size());
		
		for(WebElement li: lis)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi=li.findElement(By.className("g-tab-title"));
			//System.out.println("Names are: " +fi.getText());
			
			if(fi.getText().equalsIgnoreCase("WorkSpace"))
			{
				Actions a=new Actions(driver);
				a.click(fi).perform();
				Thread.sleep(5000);
				break;
			}
		}
	}
	
	@Parameters({"NewDashboardname"})
	@Test(priority=1)
	@TestRail(testCaseId = 961)
	public void ShowTopology(String NewDashboardname, ITestContext context) throws InterruptedException
	{	
		//Open dash board
		ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
		obj.MoveDashboard(NewDashboardname, driver);
		
		//Select Show topology option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Topology")).click();
		Thread.sleep(4000);
		
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
		Thread.sleep(18000);
		
		//Store the Topology page data into string
		String Topology=driver.findElement(By.cssSelector("svg")).getText();
		Thread.sleep(6000);
		
		//Verification condition
		if(Topology.contains(Node_Hostname))
		{
			System.out.println("Topology page is opened with the selected WGS expert");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Topology page is opened with the selected WGS Expert");
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
		} 
		else
		{
			System.out.println("Topology page is not opened with the selected WGS Expert");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to open Topology page");
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			driver.findElement(By.xpath("Topology page failed")).click();
		}
		Thread.sleep(1000);	
	}
	
	@Parameters({"NewDashboardname"})
	@TestRail(testCaseId = 759)
	@Test(priority=2)
	public void DefaultConnection(String NewDashboardname, ITestContext context) throws InterruptedException
	{	
		//Open dash board
		ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
		obj.MoveDashboard(NewDashboardname, driver);
				
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		/*driver.findElement(By.linkText("Default Connection")).click();
		Thread.sleep(4000);
		
		//click on Confirmation ok button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(2000);*/
		List<WebElement> lst=Getlist();
		
		for(WebElement li : lst)
		{
			System.out.println("in for loop");
			System.out.println("options are: " +li.getText());
			if(li.getText().equalsIgnoreCase("Default Connection"))
			{
				try {
				li.findElement(By.tagName("i"));
								
				driver.findElement(By.linkText("Default Connection")).click();
				Thread.sleep(4000);
				System.out.println("Default");
				//click on Confirmation ok button
				driver.findElement(By.id("accept-true")).click();
				Thread.sleep(2000);
				
				//Open dash board
				ClearSelectionofCheckbox obj1=new ClearSelectionofCheckbox();
				obj1.MoveDashboard(NewDashboardname, driver);
				
				driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
				
				boolean Def=false;
				List<WebElement> innerlst=Getlist();
				
				for(WebElement lis : innerlst)
				{
					if(lis.getText().contains("Edit workgroup server"))
					{
						System.out.println("Default connection is not seletced and working fine");
						context.setAttribute("Status", 1);
						context.setAttribute("Comment", "Default connection option is working");
						Def=true;
						//Open dash board
						ClearSelectionofCheckbox obj2=new ClearSelectionofCheckbox();
						obj2.MoveDashboard(NewDashboardname, driver);
						driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
						driver.findElement(By.linkText("Default Connection")).click();
						Thread.sleep(4000);
						System.out.println("Default");
						//click on Confirmation ok button
						driver.findElement(By.id("accept-true")).click();
						Thread.sleep(2000);
						break;
					}
				}
				
				if(!Def)
				{
					System.out.println("failed");
					context.setAttribute("Status", 5);
					context.setAttribute("Comment", "Default connection option is not working");
					driver.findElement(By.id("Deafult connection")).click();
				}
				break;
				
				}
				catch (Exception e) {
					// TODO: handle exception
					
					System.out.println("Exception");
					driver.findElement(By.linkText("Default Connection")).click();
					Thread.sleep(4000);
					
					//click on Confirmation ok button
					driver.findElement(By.id("accept-true")).click();
					Thread.sleep(2000);
					
					//Open dash board
					ClearSelectionofCheckbox obj3=new ClearSelectionofCheckbox();
					obj3.MoveDashboard(NewDashboardname, driver);
					
					driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
					boolean verifyconnection=false;
					List<WebElement> innerlst=Getlist();
					System.out.println("Exception list");
					for(WebElement lis : innerlst)
					{
						if(lis.getText().contains("Edit workgroup server"))
						{
							verifyconnection=true;
							System.out.println("Failed");
							context.setAttribute("Status", 5);
							context.setAttribute("Comment", "Default connection option is not working");
							driver.findElement(By.id("Deafult connection")).click();
							break;
						}
					}
					
					if(!verifyconnection)
					{
						System.out.println("Default connection is seletced and working fine");
						//driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
						driver.findElement(By.linkText("Default Connection")).click();
						Thread.sleep(4000);
						
						//click on Confirmation ok button
						driver.findElement(By.id("accept-true")).click();
						Thread.sleep(2000);
						
						context.setAttribute("Status", 1);
						context.setAttribute("Comment", "Default connection option is working");
						break;
					}
				}
			}
			
		}		
	}
	
	@Parameters({"NewDashboardname"})
	@TestRail(testCaseId = 760)
	@Test(priority=3)
	public void WGSProperties(String NewDashboardname, ITestContext context) throws InterruptedException
	{	
		//Open dash board
		ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
		obj.MoveDashboard(NewDashboardname, driver);
				
		//Click on check box and choose Properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties")).click();
		Thread.sleep(6000);
		
		//Store the editable function in to a string
		boolean FieldNamevalue=driver.findElement(By.id("name")).isEnabled();
		System.out.println(FieldNamevalue);
		
		//Verification of name field is editable or not
		if(FieldNamevalue == false)
		{
			System.out.println("WGS Name field is UnEditable");
			 driver.findElement(By.cssSelector(".btn-primary")).click();
			 context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "WGS Name field is UnEditable");
			 Thread.sleep(3000);
		}
		else
		{
			System.out.println("WGS Name field is Editable");
			context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "WGS Name field is Editable");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(3000);
			driver.findElement(By.xpath("WGS name edit function Failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId = 761)
	@Parameters({"NewDashboardname", "schemaName"})
	@Test(priority=29)
	public static void ShowObjectAttributes(String NewDashboardname, String schemaName, ITestContext context) throws InterruptedException
	{		
		try {
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.WGSAttributes(NewDashboardname, driver, schemaName, WGSName);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Show object attributes working fine");
		
		}catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got exception while showing object attributes, check details: "+  e.getMessage());
			driver.findElement(By.id("Difference failed")).click();
		}
	}


	@Test(priority = 31)
	public static void ConnectIcon(ITestContext context) throws InterruptedException {
		try {
						
			// Select the Edit WGS option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"))
					.click();

			// driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/div/div[2]")).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//li[2]/a")).click();

			// Change password
			driver.findElement(By.name("port")).clear();
			driver.findElement(By.name("port")).sendKeys("4010");
			driver.findElement(By.cssSelector(".button-blue")).click();
			Thread.sleep(3000);

			// Store the Connection status into string
			String ConnectionFail = driver.findElement(By.xpath(
					"/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span"))
					.getText();
			System.out.println(ConnectionFail);

			// Verification condition
			if (ConnectionFail.equalsIgnoreCase("Not Connected")) {
				System.out.println("Work Group Server connection failed is successfully done");
			} else {
				System.out.println("Work Group Server connection failed");
			}

			// Click on Connection Icon
			driver.findElement(By.cssSelector("img.settings-image")).click();
			Thread.sleep(3000);

			// Connection Verification
			String ConnectionPass = driver.findElement(By.xpath(
					"/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span"))
					.getText();
			if (ConnectionPass.equalsIgnoreCase("Connected")) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Workgrup server connection success");
				System.out.println("Work Group Server connected");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Workgrup server connection failed");
				System.out.println("Work Group Server not connected");
			}
			Thread.sleep(1000);

			// Click on Edit
			driver.findElement(By.xpath(
					"/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"))
					.click();
			// driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/div/div[2]")).click();
			driver.findElement(By.xpath("//li[2]/a")).click();

			// Change password
			driver.findElement(By.name("port")).clear();
			driver.findElement(By.name("port")).sendKeys("4010");
			Thread.sleep(3000);
			driver.findElement(By.cssSelector(".button-blue")).click();
			Thread.sleep(3000);
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Exception occures while connecting to workgroup server, check details: " + e.getMessage());
		}
	}

	@Parameters({"NewDashboardname"})
	@TestRail(testCaseId = 28)
	@Test(priority = 4)
	public static void AddNode(String NewDashboardname, ITestContext context) throws InterruptedException, IOException {
		try {
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
						
			// Click on checbox and Select the create node option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions MousehourNode = new Actions(driver);
			MousehourNode.moveToElement(driver.findElement(By.linkText("Create"))).perform();
			driver.findElement(By.linkText("Node...")).click();
			Thread.sleep(2000);

			// Create Node page          
			// driver.findElement(By.xpath("(//input[@type='text'])[4]")).click();
			driver.findElement(By.xpath("//app-mod-node-properties-identity/div/div[2]/div/input")).sendKeys(NodeName);
			driver.findElement(By.xpath("//div[3]/div/input")).sendKeys(Node_Hostname);
			driver.findElement(By.xpath("//div[5]/div/input")).sendKeys(Node_IPAddress);

			//port number
			driver.findElement(By.xpath("//div[7]/div/input")).clear();
			driver.findElement(By.xpath("//div[7]/div/input")).sendKeys(Node_PortNumber);
			Thread.sleep(2000);
			
			//Select Node type
			Select dd=new Select(driver.findElement(By.xpath("//select")));
			dd.selectByVisibleText("M6-WMQ Agent-managed MQ Node");
			Thread.sleep(3000);

			// Submit
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(8000);

			// Refresh the Node viewlet
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);

			// Store the viewlet data into string
			String NodeData = driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
			//System.out.println("Node viewlet data is: " +NodeData);
			
			// Verification of node is added or not
			if (NodeData.contains(NodeName)) {
				System.out.println("Node is created successfully");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Node creation success");
			} else {
				System.out.println("Node is not created");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Node creation failed");
				driver.findElement(By.xpath("Node creation failed")).click();
			}
		} catch (Exception e) {
			/*
			 * File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			 * FileUtils.copyFile(scrFile,new
			 * File("F:\\Nagaraju\\Workgroup server\\AddNode.png"));
			 */
			context.setAttribute("Status", 5);
			context.setAttribute("Comment","Exception occured when adding Node from Workspace page, check details: " + e.getMessage());
			System.out.println("Exception occured when adding Node from Workspace page");
			driver.findElement(By.cssSelector(".btn-danger")).click();
			driver.findElement(By.xpath("Node creation failed")).click();
		}
		Thread.sleep(1000);
	}

	@Parameters({"NewDashboardname", "ConnectionInstanceName", "RemoteQueueManagerName", "ConnectionName", "CommandQueueName", "ChannelName" })
	@TestRail(testCaseId = 29)
	@Test(priority = 5)
	public void AddRemoteQueueManager(String NewDashboardname, String ConnectionInstanceName, String RemoteQueueManagerName, String ConnectionName, String CommandQueueName, String ChannelName, ITestContext context) throws InterruptedException {
		try {
			
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
						
			// Select Remote queue manager option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions Mousehour = new Actions(driver);
			Mousehour.moveToElement(driver.findElement(By.linkText("Create"))).perform();
			driver.findElement(By.linkText("Remote Queue Managers...")).click();
			Thread.sleep(2000);

			// Click on Add button
			driver.findElement(By.xpath("//button[contains(.,'Add')]")).click();

			// Queue manager connection instance
			driver.findElement(By.name("name")).clear();
			driver.findElement(By.name("name")).sendKeys(ConnectionInstanceName);

			// Queue manager name
			driver.findElement(By.name("qmgrName")).clear();
			driver.findElement(By.name("qmgrName")).sendKeys(RemoteQueueManagerName);

			// Goto communication Tab
			driver.findElement(By.linkText("Communication")).click();

			// Connection name
			driver.findElement(By.name("connName")).clear();
			driver.findElement(By.name("connName")).sendKeys(ConnectionName);

			// command Queue Name
			Select queue = new Select(driver.findElement(By.name("references")));
			queue.selectByVisibleText(CommandQueueName);

			// Channel Name
			driver.findElement(By.name("channelName")).clear();
			driver.findElement(By.name("channelName")).sendKeys(ChannelName);

			// click on OK button        
			driver.findElement(By.xpath("//app-mod-remote-queue-manager-options/div/div[2]/div/div/div/button")).click();
			Thread.sleep(8000);

			// Store the Queue managers into string            
			String Queuemanagers = driver.findElement(By.xpath("//app-mod-remote-queue-manager-connections/div/div/div/div[2]")).getText();

			// Verification
			if (Queuemanagers.contains(RemoteQueueManagerName)) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Remote Queue Manager is added successfully");
				System.out.println("Remote Queue Manager is added successfully");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to add Remote Queue Manager");
				System.out.println("Remote Queue Manager is not added");
				driver.findElement(By.xpath("Queue manager add option is failed")).click();
			}
			Thread.sleep(1000);

			// Close the remote queue managers connection popup page
			driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured when Adding remote Queue manager, Check details: " + e.getMessage());
			System.out.println("Exception occured when Adding remote Queue manager");
			driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
			driver.findElement(By.id("Remote Queue manager failed")).click();
		}
		Thread.sleep(1000);

	}

	@TestRail(testCaseId = 30)
	@Parameters({"NewDashboardname", "Node_NewConnectionName", "RemoteQueueManagerName"})
	@Test(priority = 6, dependsOnMethods= {"AddRemoteQueueManager"})
	public void ModifyRemoteQueueManager(String NewDashboardname, String Node_NewConnectionName, String RemoteQueueManagerName, ITestContext context) throws InterruptedException {
		try {	
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
			
			// Select Remote queue manager option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions Mousehour = new Actions(driver);
			Mousehour.moveToElement(driver.findElement(By.linkText("Create"))).perform();
			driver.findElement(By.linkText("Remote Queue Managers...")).click();
			Thread.sleep(2000);
			
			this.ServerSelection(RemoteQueueManagerName);

			// Click on Modify button
			driver.findElement(By.xpath("//button[contains(.,'Modify')]")).click();

			// Goto communication Tab
			driver.findElement(By.linkText("Communication")).click();

			// Connection name
			driver.findElement(By.name("connName")).clear();
			driver.findElement(By.name("connName")).sendKeys(Node_NewConnectionName);
			Thread.sleep(2000);

			// click on OK button
			driver.findElement(By.xpath("//app-mod-remote-queue-manager-options/div/div[2]/div/div/div/button")).click();
			Thread.sleep(8000);
			
			this.ServerSelection(RemoteQueueManagerName);
			Thread.sleep(2000);

			// Store the connection ip into string after modifying the name
			String ChangedConnectionip = driver.findElement(By.xpath("//div[2]/div/table/tbody/tr[3]/td[2]")).getText();
			System.out.println("Changed connection is: " +ChangedConnectionip);

			if (ChangedConnectionip.equalsIgnoreCase(Node_NewConnectionName)) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Remote Queue modification are done successfully");
				System.out.println("Remote Queue modification is done successfully");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to edit Remote Queue Manager");
				System.out.println("Remote Queue modification is failed");
				driver.findElement(By.xpath("Modification failed")).click();
			}

			// Close the remote queue managers connection popup page
			driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Exception occured while modifying remote Queue manager, Check details: " + e.getMessage());
			System.out.println("Exception occured when modifying remote Queue manager");
			driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
		}
		Thread.sleep(1000);
	}

	@Parameters({"NewDashboardname", "DeleteManagerName" })
	@TestRail(testCaseId = 31)
	@Test(priority = 7, dependsOnMethods= {"AddRemoteQueueManager", "ModifyRemoteQueueManager"})
	public void DeleteRemoteQueueManager(String NewDashboardname, String DeleteManagerName, ITestContext context) throws InterruptedException {
		try {	
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
			
			// Select Remote queue manager option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions Mousehour = new Actions(driver);
			Mousehour.moveToElement(driver.findElement(By.linkText("Create"))).perform();
			driver.findElement(By.linkText("Remote Queue Managers...")).click();
			Thread.sleep(5000);
			
			WebElement bu=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr=bu.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr.size());
			for(WebElement active : tr)
			{
				System.out.println("Text is: " +active.getText());
				if(active.getText().contains(DeleteManagerName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active.click();
						break;
					}
				}
			}
			
			/*boolean RMQM=driver.findElement(By.xpath("//td[contains(.,'" + DeleteManagerName + "')]")).isEnabled();
			System.out.println("Status of deleting rmqm is :" +RMQM);
			if(RMQM)
			{
				System.out.println("Deleting remote queue manager is already selected");
			}
			else
			{
				// Select the required Queue manager
				driver.findElement(By.xpath("//td[contains(.,'" + DeleteManagerName + "')]")).click();
				
			}*/

			// Click on Delete
			driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
			driver.findElement(By.id("accept-true")).click();
			Thread.sleep(8000);

			// Store the Queue managers into string
			String Queuemanagers = driver.findElement(By.xpath("//app-mod-remote-queue-manager-connections/div/div/div/div")).getText();
			System.out.println(Queuemanagers);

			// Verification condition
			if (Queuemanagers.contains(DeleteManagerName)) {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to delete Remote Queue Manager");
				System.out.println("Remote Queue Manager is not deleted");
				driver.findElement(By.xpath("Remote Queue Manager delete Failed")).click();
			} else {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Remote Queue Manager deleted successflly");
				System.out.println("Remote Queue Manager is deleted");
			}
			Thread.sleep(2000);
			// Close the remote queue managers connection popup page
			driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured while Deleting remote Queue manager, Check details: " + e.getMessage());
			System.out.println("Exception occured when Deleting remote Queue manager");
			driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
			driver.findElement(By.id("Delete failed")).click();
		}
		Thread.sleep(1000);
	}

	@Parameters({"NewDashboardname", "AgentInstanceName", "ServerName", "ServerURL" })
	@TestRail(testCaseId = 32)
	@Test(priority = 8)
	public void AddRemoteEMSManager(String NewDashboardname, String AgentInstanceName, String ServerName, String ServerURL, ITestContext context)
			throws InterruptedException {
		try {	
			
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
			
			// Select Remote EMS manager option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(2000);

			Actions Mousehour = new Actions(driver);
			Mousehour.moveToElement(driver.findElement(By.linkText("Create"))).perform();
			driver.findElement(By.linkText("Remote EMS Managers...")).click();
			Thread.sleep(2000);

			// Click on Add button //app-dropdown[@id='dropdown-block']/div/ul/li[3]/a
			driver.findElement(By.xpath("//button[contains(.,'Add')]")).click();

			// EMS Agent Instance Name
			driver.findElement(By.id("agentInstanceName")).clear();
			driver.findElement(By.id("agentInstanceName")).sendKeys(AgentInstanceName);

			// EMS Server name
			driver.findElement(By.id("serverName")).clear();
			driver.findElement(By.id("serverName")).sendKeys(ServerName);

			// Server URL
			driver.findElement(By.id("serverURL")).clear();
			driver.findElement(By.id("serverURL")).sendKeys(ServerURL);

			// click on OK button
			driver.findElement(By.xpath("//app-mod-remote-ems-manager-options/div/div[2]/div/div/div/button")).click();
			Thread.sleep(8000);

			// Store the EMS servers data into string
			String RemoteEMSserver = driver.findElement(By.xpath("//app-mod-remote-ems-manager-connections/div/div/div/div[2]")).getText();

			// verification of Remote ems server
			if (RemoteEMSserver.contains(AgentInstanceName) && RemoteEMSserver.contains(ServerName)) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Remote EMS server is added successflly");
				System.out.println("Remote EMS server is added");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to add Remote EMS server");
				System.out.println("Remote EMS server is not added");
				driver.findElement(By.id("Add EMS failed")).click();
			}
			Thread.sleep(1000);
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured while adding Remote EMS server, Check details: " + e.getMessage());
		}
	}

	@Parameters({ "UpdatedServerURL", "ServerName"})
	@TestRail(testCaseId = 33)
	@Test(priority = 9, dependsOnMethods= {"AddRemoteEMSManager"})
	public void ModifyRemoteEMSServer(String UpdatedServerURL, String ServerName, ITestContext context) throws InterruptedException {
		try {
						
			WebElement bu=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr=bu.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr.size());
			for(WebElement active : tr)
			{
				System.out.println("Text is: " +active.getText());
				if(active.getText().contains(ServerName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active.click();
						break;
					}
				}
			}
			
			/*
			WebElement ss=driver.findElement(By.xpath("//td[contains(.,'" + ServerName + "')]"));
			
			WebElement parentElement = ss.findElement(By.xpath("./.."));
			String ll=ss.getAttribute("class");
			System.out.println("Value is: " +ll);
			boolean REMS=driver.findElement(By.xpath("//td[contains(.,'" + ServerName + "')]")).isSelected();
			System.out.println("Remote EMS server selection is: " +REMS);
			
			if(REMS==true)
			{
				System.out.println("EMS server is already selected");
			}
			else
			{
				//Click on Remote EMS server name
				driver.findElement(By.xpath("//td[contains(.,'" + ServerName + "')]")).click();				
			}
			
			Thread.sleep(2000); */
			
			// Click on Modify button
			driver.findElement(By.xpath("//button[contains(.,'Modify')]")).click();

			// Server URL
			driver.findElement(By.id("serverURL")).clear();
			driver.findElement(By.id("serverURL")).sendKeys(UpdatedServerURL);

			// click on OK button
			driver.findElement(By.xpath("//app-mod-remote-ems-manager-options/div/div[2]/div/div/div/button")).click();
			Thread.sleep(5000);
			
			WebElement but=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr1=but.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr1.size());
			for(WebElement active1 : tr1)
			{
				System.out.println("Text is: " +active1.getText());
				if(active1.getText().contains(ServerName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active1.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active1.click();
						break;
					}
				}
			}

			// Store the Server URL value into string
			String URL = driver.findElement(By.xpath("//div[2]/div/table/tbody/tr[3]/td[2]")).getText();
			System.out.println("Updated Server url is: " +URL);

			if (URL.equalsIgnoreCase(UpdatedServerURL)) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Remote EMS server is edited successflly");
				System.out.println("Remote EMS server is modified");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to edit Remote EMS server");
				System.out.println("Remote EMS server is not modified");
				driver.findElement(By.id("Modify failed")).click();
			}
			Thread.sleep(1000);
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured while editing Remote EMS server, Check details: " + e.getMessage());
			driver.findElement(By.id("Modify failed")).click();
		}
	}

	@Parameters({ "ServerName" })
	@TestRail(testCaseId = 34)
	@Test(priority = 10, dependsOnMethods= {"AddRemoteEMSManager", "ModifyRemoteEMSServer"})
	public void DeleteRemoteEMSServer(String ServerName, ITestContext context) throws InterruptedException {
		try {
			
			WebElement bu=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr=bu.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr.size());
			for(WebElement active : tr)
			{
				System.out.println("Text is: " +active.getText());
				if(active.getText().contains(ServerName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active.click();
						break;
					}
				}
			}
			
			/*//Click on Remote EMS server name
			driver.findElement(By.xpath("//td[contains(.,'" + ServerName + "')]")).click();	
			
			boolean REMS=driver.findElement(By.xpath("//td[contains(.,'" + ServerName + "')]")).isSelected();
			System.out.println("Remote EMS server selection is: " +REMS);
			Thread.sleep(3000);
			
			if(REMS==true)
			{
				System.out.println("EMS server is already selected");
			}
			else
			{
				//Click on Remote EMS server name
				driver.findElement(By.xpath("//td[contains(.,'" + ServerName + "')]")).click();	
			}*/

			Thread.sleep(2000);

			// Click on Delete button
			driver.findElement(By.xpath("//div[3]/button")).click();

			// Click on Confirmation yes button
			driver.findElement(By.id("accept-true")).click();
			Thread.sleep(6000);

			// Store the EMS servers data into string
			String RemoteEMSserver = driver.findElement(By.xpath("//app-mod-remote-ems-manager-connections/div/div/div/div[2]")).getText();

			// verification of Remote ems server
			if (RemoteEMSserver.contains(ServerName)) {
				System.out.println("Delete Remote EMS server is failed");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to delete Remote EMS server");
				driver.findElement(By.id("Delete failed")).click();
			} else {
				System.out.println("Remote EMS server is deleed successflly");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Remote EMS server is edited successflly");
			}
			Thread.sleep(1000);

			// Close the window
			driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
			Thread.sleep(2000);
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Exception occured while deleting Remote EMS server, Check details: " + e.getMessage());
		}

	}
	
	@Parameters({"NewDashboardname", "ClusterName", "BootstrapServer" })
	@TestRail(testCaseId = 940)
	@Test(priority = 11)
	public void AddRemoteKafkaManager(String NewDashboardname, String ClusterName, String BootstrapServer, ITestContext context)
			throws InterruptedException {
		try {	
			
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
			
			// Select Remote kafka manager option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(2000);

			Actions Mousehour = new Actions(driver);
			Mousehour.moveToElement(driver.findElement(By.linkText("Create"))).perform();
			driver.findElement(By.linkText("Remote Kafka Managers...")).click();
			Thread.sleep(2000);

			// Click on Add button //app-dropdown[@id='dropdown-block']/div/ul/li[3]/a
			driver.findElement(By.xpath("//button[contains(.,'Add')]")).click();

			// Kafka Agent Instance Name
			driver.findElement(By.xpath("//ng-select/div/span[2]")).click();
			try
			{
			
			WebElement drop=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
			List<WebElement> div=drop.findElements(By.tagName("div")); 
			//System.out.println("Number of divs are: " +div.size());
			
			for(WebElement di : div)
			{
				//System.out.println("text is :" +di.getText());
				if(di.getText().equalsIgnoreCase(KafkaNodeName))
				{
					di.click();
					break;
				}	
			}
			Thread.sleep(3000);
			}
			catch (Exception ee)
			{
				driver.findElement(By.cssSelector("#nodeName input")).sendKeys(KafkaNodeName);
				driver.findElement(By.cssSelector("#nodeName input")).sendKeys(Keys.ENTER);
			}
			Thread.sleep(3000);
			//driver.findElement(By.xpath("//div[3]/input")).clear();
			//driver.findElement(By.xpath("//div[3]/input")).sendKeys(KafkaNodeName);

			// Cluster name
			driver.findElement(By.xpath("//app-text-input/div/div/input")).clear();
			driver.findElement(By.xpath("//app-text-input/div/div/input")).sendKeys(ClusterName);

			// Boot strap Server
			driver.findElement(By.xpath("//div[3]/div/app-text-input/div/div/input")).clear();
			driver.findElement(By.xpath("//div[3]/div/app-text-input/div/div/input")).sendKeys(BootstrapServer);

			// click on OK button   
			driver.findElement(By.xpath("//app-mod-remote-kafka-manager-options/div/div[2]/div/div/div/button")).click();
			Thread.sleep(8000);

			// Store the EMS servers data into string
			String Remotekafkaserver = driver.findElement(By.xpath("//app-mod-remote-kafka-manager-connections/div/div/div/div[2]")).getText();

			// verification of Remote ems server
			if (Remotekafkaserver.contains(KafkaNodeName) && Remotekafkaserver.contains(ClusterName)) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Remote Kafak server is added successflly");
				System.out.println("Remote Kafka server is added");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to add Remote Kafka server");
				System.out.println("Remote Kafka server is not added");
				driver.findElement(By.id("Add Kafka failed")).click();
			}
			Thread.sleep(1000);
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
			
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured while adding Remote Kafka server, Check details: " + e.getMessage());
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
			driver.findElement(By.id("Add Kafka failed")).click();
		}
	}
	
	@TestRail(testCaseId = 941)
	@Parameters({"NewDashboardname", "ClusterName", "UpdtedBootstrapServer"})
	@Test(priority = 12, dependsOnMethods= {"AddRemoteKafkaManager"})
	public void ModifyRemoteKafkaManager(String NewDashboardname, String ClusterName, String UpdtedBootstrapServer, ITestContext context) throws InterruptedException {
		try {
			
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
			
			// Select Remote queue manager option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions Mousehour = new Actions(driver);
			Mousehour.moveToElement(driver.findElement(By.linkText("Create"))).perform();
			driver.findElement(By.linkText("Remote Kafka Managers...")).click();
			Thread.sleep(2000);
			
			WebElement bu=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr=bu.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr.size());
			for(WebElement active : tr)
			{
				System.out.println("Text is: " +active.getText());
				if(active.getText().contains(ClusterName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active.click();
						break;
					}
				}
			}

			// Click on Modify button
			driver.findElement(By.xpath("//button[contains(.,'Modify')]")).click();

			// Boot strap Server         
			driver.findElement(By.xpath("//div[3]/div/app-text-input/div/div/input")).clear();
			driver.findElement(By.xpath("//div[3]/div/app-text-input/div/div/input")).sendKeys(UpdtedBootstrapServer);


			// click on OK button
			driver.findElement(By.xpath("//app-mod-remote-kafka-manager-options/div/div[2]/div/div/div/button")).click();
			Thread.sleep(8000);
			
			WebElement bu1=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr1=bu1.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr1.size());
			for(WebElement active1 : tr1)
			{
				System.out.println("Text is: " +active1.getText());
				if(active1.getText().contains(ClusterName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active1.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active1.click();
						break;
					}
				}
			}
			Thread.sleep(2000);

			// Store the connection ip into string after modifying the name
			String ChangedBootstrap = driver.findElement(By.xpath("//div[2]/div/table/tbody/tr[3]/td[2]")).getText();
			System.out.println("Changed connection is: " +ChangedBootstrap);

			if (ChangedBootstrap.equalsIgnoreCase(UpdtedBootstrapServer)) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Remote kafka modification are done successfully");
				System.out.println("Remote kafka modification is done successfully");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to edit Remote kafka Manager");
				System.out.println("Remote kafka modification is failed");
				driver.findElement(By.xpath("Modification failed")).click();
			}

			// Close the remote queue managers connection popup page
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Exception occured while modifying remote kafka manager, Check details: " + e.getMessage());
			System.out.println("Exception occured when modifying remote kafka manager");
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId = 942)
	@Parameters({"NewDashboardname", "ClusterName", "CopyasClusterName", "CopyasBootstrapServer"})
	@Test(priority = 13, dependsOnMethods= {"AddRemoteKafkaManager"})
	public void CopyAsRemoteKafkaManager(String NewDashboardname, String ClusterName, String CopyasClusterName, String CopyasBootstrapServer, ITestContext context) throws InterruptedException {
		try {
			
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
			
			// Select Remote queue manager option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions Mousehour = new Actions(driver);
			Mousehour.moveToElement(driver.findElement(By.linkText("Create"))).perform();
			driver.findElement(By.linkText("Remote Kafka Managers...")).click();
			Thread.sleep(2000);
			
			WebElement bu=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr=bu.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr.size());
			for(WebElement active : tr)
			{
				System.out.println("Text is: " +active.getText());
				if(active.getText().contains(ClusterName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active.click();
						break;
					}
				}
			}

			// Click on Modify button
			driver.findElement(By.xpath("//button[contains(.,'Copy as')]")).click();
			
			// Cluster name
			driver.findElement(By.xpath("//app-text-input/div/div/input")).clear();
			driver.findElement(By.xpath("//app-text-input/div/div/input")).sendKeys(CopyasClusterName);

			// Boot strap Server
			driver.findElement(By.xpath("//div[3]/div/app-text-input/div/div/input")).clear();
			driver.findElement(By.xpath("//div[3]/div/app-text-input/div/div/input")).sendKeys(CopyasBootstrapServer);


			// click on OK button
			driver.findElement(By.xpath("//app-mod-remote-kafka-manager-options/div/div[2]/div/div/div/button")).click();
			Thread.sleep(8000);

			// Store the connection ip into string after modifying the name
			String Remotekafkaserver = driver.findElement(By.xpath("//app-mod-remote-kafka-manager-connections/div/div/div/div[2]")).getText();

			if (Remotekafkaserver.contains(CopyasClusterName)) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Remote kafka Copy as done successfully");
				System.out.println("Remote kafka Copy as done successfully");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to Copy as Remote kafka Manager");
				System.out.println("Remote kafka copy as is failed");
				driver.findElement(By.xpath("Copy as failed")).click();
			}

			// Close the remote queue managers connection popup page
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Exception occured while modifying remote kafka manager, Check details: " + e.getMessage());
			System.out.println("Exception occured when Copy as remote kafka manager");
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId = 943)
	@Parameters({"NewDashboardname", "ClusterName"})
	@Test(priority = 14, dependsOnMethods= {"AddRemoteKafkaManager"})
	public void DeleteRemoteKafkaManager(String NewDashboardname, String ClusterName, ITestContext context) throws InterruptedException {
		try {
			
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
			
			// Select Remote queue manager option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions Mousehour = new Actions(driver);
			Mousehour.moveToElement(driver.findElement(By.linkText("Create"))).perform();
			driver.findElement(By.linkText("Remote Kafka Managers...")).click();
			Thread.sleep(2000);
			
			WebElement bu=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr=bu.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr.size());
			for(WebElement active : tr)
			{
				System.out.println("Text is: " +active.getText());
				if(active.getText().contains(ClusterName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active.click();
						break;
					}
				}
			}

			// Click on Modify button
			driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
			
			//Confirmation of delete
			driver.findElement(By.id("accept-true")).click();
			Thread.sleep(6000);
			
			// Store the connection ip into string after modifying the name
			String Remotekafkaserver = driver.findElement(By.xpath("//app-mod-remote-kafka-manager-connections/div/div/div/div[2]")).getText();
			System.out.println("Remote kafka details: " +Remotekafkaserver);
			
			System.out.println("Deleted cluster names is: " +ClusterName);

			if (Remotekafkaserver.contains(ClusterName)) {
				System.out.println("Delete Remote kafka server is failed");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to delete Remote kafka server");
				driver.findElement(By.id("Delete failed")).click();
			} else {
				System.out.println("Remote kafka server is deleed successflly");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Remote kafka server is edited successflly");
			}
			Thread.sleep(1000);


			// Close the remote queue managers connection popup page
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Exception occured while delete remote kafka manager, Check details: " + e.getMessage());
			System.out.println("Exception occured when Delete remote kafka manager");
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
			driver.findElement(By.id("Delete failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"NewDashboardname", "NodeInstanceName", "QueueManagerName", "IIBConnectionURL", "ConnectionType"})
	@TestRail(testCaseId = 1025)
	@Test(priority = 15)
	public void AddRemoteIIBORACEManagerConnection(String NewDashboardname, String NodeInstanceName, String QueueManagerName, String IIBConnectionURL, String ConnectionType, ITestContext context) throws InterruptedException {
		try {
			
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
						
			// Select Remote iib/ace manager connections
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions Mousehour = new Actions(driver);
			Mousehour.moveToElement(driver.findElement(By.linkText("Create"))).perform();
			driver.findElement(By.linkText("Remote ACE/IIB Managers...")).click();
			Thread.sleep(2000);

			// Click on Add button
			driver.findElement(By.xpath("//button[contains(.,'Add')]")).click();

			// Node instance name
			driver.findElement(By.id("name")).clear();
			driver.findElement(By.id("name")).sendKeys(NodeInstanceName);

			// Queue manager name
			driver.findElement(By.id("qmgrName")).clear();
			driver.findElement(By.id("qmgrName")).sendKeys(QueueManagerName);

			// URL
			driver.findElement(By.id("url")).clear();
			driver.findElement(By.id("url")).sendKeys(IIBConnectionURL);
			Thread.sleep(3000);

			// connection type
			Select type=new Select(driver.findElement(By.id("type")));
			type.selectByVisibleText(ConnectionType);
			Thread.sleep(3000);
			

			// click on OK button        
			driver.findElement(By.xpath("//app-mod-remote-ace-manager-options/div/div[2]/div/div/div/button")).click();
			Thread.sleep(8000);

			// Store the Queue managers into string            
			String IIBACEManagerConnection = driver.findElement(By.xpath("//app-mod-remote-ace-manager-connections/div/div/div/div[2]")).getText();

			// Verification
			if (IIBACEManagerConnection.contains(NodeInstanceName)) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "IIB/ACE Manager Connection is added successfully");
				System.out.println("IIB/ACE Manager Connection is added successfully");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to add IIB/ACE Manager Connection");
				System.out.println("IIB/ACE Manager Connection is not added");
				driver.findElement(By.xpath("IIB/ACE Manager Connection add option is failed")).click();
			}
			Thread.sleep(1000);

			// Close the remote queue managers connection popup page
			driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured when Adding IIB/ACE Manager Connection, Check details: " + e.getMessage());
			System.out.println("Exception occured when Adding IIB/ACE Manager Connection");
			driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
			driver.findElement(By.id("IIB/ACE Manager Connection failed")).click();
		}
		Thread.sleep(1000);

	}

	@TestRail(testCaseId = 1026)
	@Parameters({"NewDashboardname", "NodeInstanceName", "Node_NewURL"})
	@Test(priority = 16, dependsOnMethods= {"AddRemoteIIBORACEManagerConnection"})
	public void ModifyIIBOrACEManagerConnection(String NewDashboardname, String NodeInstanceName, String Node_NewURL, ITestContext context) throws InterruptedException {
		try {	
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
			
			// Select Remote queue manager option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions Mousehour = new Actions(driver);
			Mousehour.moveToElement(driver.findElement(By.linkText("Create"))).perform();
			driver.findElement(By.linkText("Remote ACE/IIB Managers...")).click();
			Thread.sleep(2000);
			
			WebElement bu=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr=bu.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr.size());
			for(WebElement active : tr)
			{
				System.out.println("Text is: " +active.getText());
				if(active.getText().contains(NodeInstanceName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Node instance is already selected");
					}
					else
					{
						active.click();
						break;
					}
				}
			}

			// Click on Modify button
			driver.findElement(By.xpath("//button[contains(.,'Modify')]")).click();

			// Connection url
			driver.findElement(By.id("url")).clear();
			driver.findElement(By.id("url")).sendKeys(Node_NewURL);
			Thread.sleep(2000);

			// click on OK button         
			driver.findElement(By.xpath("//app-mod-remote-ace-manager-options/div/div[2]/div/div/div/button")).click();
			Thread.sleep(8000);
			
			WebElement bu1=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr1=bu1.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr1.size());
			for(WebElement active1 : tr1)
			{
				System.out.println("Text is: " +active1.getText());
				if(active1.getText().contains(NodeInstanceName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active1.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Node instance is already selected");
					}
					else
					{
						active1.click();
						break;
					}
				}
			}
			Thread.sleep(2000);

			// Store the connection ip into string after modifying the name
			String ChangedConnectionurl = driver.findElement(By.xpath("//tr[4]/td[2]")).getText();
			System.out.println("Changed url is: " +ChangedConnectionurl);

			if (ChangedConnectionurl.equalsIgnoreCase(Node_NewURL)) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "IIB/ACE Connection manager modification are done successfully");
				System.out.println("IIB/ACE Connection manager is done successfully");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to edit IIB/ACE Connection manager");
				System.out.println("IIB/ACE Connection manager modification is failed");
				driver.findElement(By.xpath("Modification failed")).click();
			}

			// Close the remote queue managers connection popup page
			driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Exception occured while modifying IIB/ACE Connection manager, Check details: " + e.getMessage());
			System.out.println("Exception occured when modifying IIB/ACE Connection manager");
			driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
		}
		Thread.sleep(1000);
	}

	@Parameters({"NewDashboardname", "NodeInstanceName" })
	@TestRail(testCaseId = 1027)
	@Test(priority = 17, dependsOnMethods= {"AddRemoteIIBORACEManagerConnection", "ModifyIIBOrACEManagerConnection"})
	public void DeleteIIBOrACEConnectionManager(String NewDashboardname, String NodeInstanceName, ITestContext context) throws InterruptedException {
		try {	
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
			
			// Select Remote queue manager option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions Mousehour = new Actions(driver);
			Mousehour.moveToElement(driver.findElement(By.linkText("Create"))).perform();
			driver.findElement(By.linkText("Remote ACE/IIB Managers...")).click();
			Thread.sleep(5000);
			
			WebElement bu=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr=bu.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr.size());
			for(WebElement active : tr)
			{
				System.out.println("Text is: " +active.getText());
				if(active.getText().contains(NodeInstanceName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Node instance is already selected");
					}
					else
					{
						active.click();
						break;
					}
				}
			}
			
			/*boolean RMQM=driver.findElement(By.xpath("//td[contains(.,'" + DeleteManagerName + "')]")).isEnabled();
			System.out.println("Status of deleting rmqm is :" +RMQM);
			if(RMQM)
			{
				System.out.println("Deleting remote queue manager is already selected");
			}
			else
			{
				// Select the required Queue manager
				driver.findElement(By.xpath("//td[contains(.,'" + DeleteManagerName + "')]")).click();
				
			}*/

			// Click on Delete
			driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
			driver.findElement(By.id("accept-true")).click();
			Thread.sleep(8000);

			// Store the Queue managers into string
			String Queuemanagers = driver.findElement(By.xpath("//app-mod-remote-ace-manager-connections/div/div/div/div[2]")).getText();
			System.out.println(Queuemanagers);

			// Verification condition
			if (Queuemanagers.contains(NodeInstanceName)) {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to delete IIB/ACE Connection manager");
				System.out.println("IIB/ACE Connection Manager is not deleted");
				driver.findElement(By.xpath("IIB/ACE Connection manager delete Failed")).click();
			} else {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "IIB/ACE Connection manager deleted successflly");
				System.out.println("IIB/ACE Connection manager is deleted");
			}
			Thread.sleep(2000);
			// Close the remote queue managers connection popup page
			driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured while Deleting IIB/ACE Connection manager, Check details: " + e.getMessage());
			System.out.println("Exception occured when Deleting IIB/ACE Connection manager");
			driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
			driver.findElement(By.id("Delete failed")).click();
		}
		Thread.sleep(1000);
	}
	
	
	@Parameters({"NewDashboardname", "SolaceNodeInstanceName", "SolaceBrokerName", "SolaceServerURL" })
	@TestRail(testCaseId = 1166)
	@Test(priority = 18)
	public void AddRemoteSolaceManager(String NewDashboardname, String SolaceNodeInstanceName, String SolaceBrokerName, String SolaceServerURL, ITestContext context)
			throws InterruptedException {
		try {	
			
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
			
			// Select Remote Solace manager option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(2000);

			Actions Mousehour = new Actions(driver);
			Mousehour.moveToElement(driver.findElement(By.linkText("Create"))).perform();
			driver.findElement(By.linkText("Remote Solace Managers...")).click();
			Thread.sleep(2000);

			// Click on Add button //app-dropdown[@id='dropdown-block']/div/ul/li[3]/a
			driver.findElement(By.xpath("//button[contains(.,'Add')]")).click();

			// EMS Agent Instance Name
			driver.findElement(By.id("name")).clear();
			driver.findElement(By.id("name")).sendKeys(SolaceNodeInstanceName);

			// EMS Server name
			driver.findElement(By.id("brokerName")).clear();
			driver.findElement(By.id("brokerName")).sendKeys(SolaceBrokerName);

			// Server URL
			driver.findElement(By.id("url")).clear();
			driver.findElement(By.id("url")).sendKeys(SolaceServerURL);

			// click on OK button
			driver.findElement(By.xpath("//app-mod-remote-solace-manager-options/div/div[2]/div/div/div/button")).click();
			Thread.sleep(8000);

			// Store the EMS servers data into string
			String RemoteSolaceserver = driver.findElement(By.xpath("//app-mod-remote-solace-manager-connections/div/div/div/div[2]")).getText();

			// verification of Remote ems server
			if (RemoteSolaceserver.contains(SolaceNodeInstanceName) && RemoteSolaceserver.contains(SolaceBrokerName)) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Remote Solace server is added successflly");
				System.out.println("Remote Solace server is added");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to add Remote Solace server");
				System.out.println("Remote Solace server is not added");
				driver.findElement(By.id("Add Solace failed")).click();
			}
			Thread.sleep(1000);
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured while adding Remote Solace server, Check details: " + e.getMessage());
		}
	}

	@Parameters({ "SolaceUpdatedServerURL", "SolaceServerName"})
	@TestRail(testCaseId = 1167)
	@Test(priority = 19, dependsOnMethods= {"AddRemoteSolaceManager"})
	public void ModifyRemoteSolaceServer(String SolaceUpdatedServerURL, String SolaceServerName, ITestContext context) throws InterruptedException {
		try {
						
			WebElement bu=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr=bu.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr.size());
			for(WebElement active : tr)
			{
				System.out.println("Text is: " +active.getText());
				if(active.getText().contains(SolaceServerName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active.click();
						break;
					}
				}
			}
			
						
			// Click on Modify button
			driver.findElement(By.xpath("//button[contains(.,'Modify')]")).click();

			// Server URL
			driver.findElement(By.id("url")).clear();
			driver.findElement(By.id("url")).sendKeys(SolaceUpdatedServerURL);

			// click on OK button
			driver.findElement(By.xpath("//app-mod-remote-solace-manager-options/div/div[2]/div/div/div/button")).click();
			Thread.sleep(5000);
			
			WebElement but=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr1=but.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr1.size());
			for(WebElement active1 : tr1)
			{
				System.out.println("Text is: " +active1.getText());
				if(active1.getText().contains(SolaceServerName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active1.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active1.click();
						break;
					}
				}
			}

			// Store the Server URL value into string
			String URL = driver.findElement(By.xpath("//div[2]/div/table/tbody/tr[3]/td[2]")).getText();
			System.out.println("Updated Server url is: " +URL);

			if (URL.equalsIgnoreCase(SolaceUpdatedServerURL)) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Remote Solace server is edited successflly");
				System.out.println("Remote Solace server is modified");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to edit Remote Solace server");
				System.out.println("Remote Solace server is not modified");
				driver.findElement(By.id("Modify failed")).click();
			}
			Thread.sleep(1000);
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured while editing Remote Solace server, Check details: " + e.getMessage());
			driver.findElement(By.id("Modify failed")).click();
		}
	}

	@Parameters({ "SolaceServerName" })
	@TestRail(testCaseId = 1168)
	@Test(priority = 20, dependsOnMethods= {"AddRemoteSolaceManager", "ModifyRemoteSolaceServer"})
	public void DeleteRemoteSolaceServer(String SolaceServerName, ITestContext context) throws InterruptedException {
		try {
			
			WebElement bu=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr=bu.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr.size());
			for(WebElement active : tr)
			{
				System.out.println("Text is: " +active.getText());
				if(active.getText().contains(SolaceServerName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active.click();
						break;
					}
				}
			}
			Thread.sleep(2000);

			// Click on Delete button
			driver.findElement(By.xpath("//div[3]/button")).click();

			// Click on Confirmation yes button
			driver.findElement(By.id("accept-true")).click();
			Thread.sleep(6000);

			// Store the EMS servers data into string
			String RemoteSolaceserver = driver.findElement(By.xpath("//app-mod-remote-solace-manager-connections/div/div/div/div[2]")).getText();

			// verification of Remote ems server
			if (RemoteSolaceserver.contains(SolaceServerName)) {
				System.out.println("Delete Remote Solace server is failed");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to delete Remote Solace server");
				driver.findElement(By.id("Delete failed")).click();
			} else {
				System.out.println("Remote Solace server is deleed successfully");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Remote Solace server is Deleted successfully");
			}
			Thread.sleep(1000);

			// Close the window
			driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
			Thread.sleep(2000);
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Exception occured while deleting Remote Solace server, Check details: " + e.getMessage());
		}

	}

	@Parameters({"NewDashboardname", "ConnectionName", "Server", "PortNumber"})
	@TestRail(testCaseId = 962)
	@Test(priority = 21)
	public static void AddWGSConnection(String NewDashboardname, String ConnectionName, String Server, String PortNumber, ITestContext context)
			throws InterruptedException {
		try {
			
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
			
			//Click on Manage WGS icon
			driver.findElement(By.xpath("//img[@title='Manage Workgroup Servers']")).click();
			Thread.sleep(3000);

			// Click on Add button
			driver.findElement(By.xpath("//button[contains(.,'Add')]")).click();
			
			//Give connection name
			driver.findElement(By.xpath("//form/div/div/div/div/div/div/input")).clear();
			driver.findElement(By.xpath("//form/div/div/div/div/div/div/input")).sendKeys(ConnectionName);
			
			// server name
			driver.findElement(By.xpath("//form/div/div/div/div[2]/div/div/input")).clear();
			driver.findElement(By.xpath("//form/div/div/div/div[2]/div/div/input")).sendKeys(Server);

			// Port
			driver.findElement(By.xpath("//input[@type='number']")).clear();
			driver.findElement(By.xpath("//input[@type='number']")).sendKeys(PortNumber);
			Thread.sleep(3000);

			// click on OK button   
			driver.findElement(By.xpath("//form/div/div[2]/div/div[2]/button")).click();
			Thread.sleep(8000);

			// Store the EMS servers data into string
			String WGSConnections = driver.findElement(By.xpath("//app-mod-wgs-connections-view/div/div/div/div[2]")).getText();

			// verification of Remote ems server
			if (WGSConnections.contains(ConnectionName) && WGSConnections.contains(Server)) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "WGS Connection is added successflly");
				System.out.println("WGS Connection is added");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to add WGS Connection");
				System.out.println("WGS Connection is not added");
				driver.findElement(By.id("Add WGS Connection failed")).click();
			}
			Thread.sleep(1000);
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
			
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured while adding WGS Connection, Check details: " + e.getMessage());
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
			driver.findElement(By.id("Add WGS Connection failed")).click();
		}
	}
	
	@TestRail(testCaseId = 963)
	@Parameters({"ConnectionName", "UpdatedConnectionName"})
	@Test(priority = 22, dependsOnMethods= {"AddWGSConnection"})
	public void ModifyWGSConnection(String ConnectionName, String UpdatedConnectionName, ITestContext context) throws InterruptedException {
		try {	
			
			Thread.sleep(6000);
			//Click on Manage WGS icon
			driver.findElement(By.xpath("//img[@title='Manage Workgroup Servers']")).click();
			Thread.sleep(3000);
			
			WebElement bu=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr=bu.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr.size());
			for(WebElement active : tr)
			{
				System.out.println("Text is: " +active.getText());
				if(active.getText().contains(ConnectionName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active.click();
						break;
					}
				}
			}

			// Click on Modify button
			driver.findElement(By.xpath("//button[contains(.,'Modify')]")).click();

			//Update connection name
			driver.findElement(By.xpath("//form/div/div/div/div/div/div/input")).clear();
			driver.findElement(By.xpath("//form/div/div/div/div/div/div/input")).sendKeys(UpdatedConnectionName);


			// click on OK button
			driver.findElement(By.xpath("//form/div/div[2]/div/div[2]/button")).click();
			Thread.sleep(8000);
			
			WebElement bu1=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr1=bu1.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr1.size());
			for(WebElement active1 : tr1)
			{
				System.out.println("Text is: " +active1.getText());
				if(active1.getText().contains(UpdatedConnectionName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active1.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active1.click();
						break;
					}
				}
			}
			Thread.sleep(2000);

			// Store the connection ip into string after modifying the name 
			String ChangedConnectionName = driver.findElement(By.xpath("//div[2]/div/table/tbody/tr/td[2]")).getText();
			System.out.println("Changed connection is: " +ChangedConnectionName);

			if (ChangedConnectionName.equalsIgnoreCase(UpdatedConnectionName)) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "WGS Connection modification are done successfully");
				System.out.println("WGS Connection modification is done successfully");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to edit WGS Connection");
				System.out.println("WGS Connection modification is failed");
				driver.findElement(By.xpath("Modification failed")).click();
			}

			// Close the remote queue managers connection popup page
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Exception occured while modifying WGS Connection, Check details: " + e.getMessage());
			System.out.println("Exception occured when modifying WGS Connection");
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId = 964)
	@Parameters({"UpdatedConnectionName"})
	@Test(priority = 23, dependsOnMethods= {"ModifyWGSConnection"})
	public void DeleteWGSConnection(String UpdatedConnectionName, ITestContext context) throws InterruptedException {
		try {			
			//Click on Manage WGS icon
			driver.findElement(By.xpath("//img[@title='Manage Workgroup Servers']")).click();
			Thread.sleep(3000);
			
			WebElement bu=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr=bu.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr.size());
			for(WebElement active : tr)
			{
				System.out.println("Text is: " +active.getText());
				if(active.getText().contains(UpdatedConnectionName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active.click();
						break;
					}
				}
			}

			// Click on delete button
			driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
			
			//Choose check box
			driver.findElement(By.id("requiredCheckbox")).click();
			Thread.sleep(3000);
			
			//Confirmation of delete
			driver.findElement(By.id("accept-true")).click();
			Thread.sleep(6000);
			
			// Store the connection ip into string after modifying the name
			String WGSConnections = driver.findElement(By.xpath("//app-mod-wgs-connections-view/div/div/div/div[2]")).getText();
			System.out.println("Remote kafka details: " +WGSConnections);
			
			System.out.println("Deleted cluster names is: " +UpdatedConnectionName);

			if (WGSConnections.contains(UpdatedConnectionName)) {
				System.out.println("Delete WGS Connection is failed");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to delete WGS Connection");
				driver.findElement(By.id("Delete failed")).click();
			} else {
				System.out.println("WGS Connection is deleed successflly");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "WGS Connection is edited successflly");
			}
			Thread.sleep(1000);


			// Close the remote queue managers connection popup page
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Exception occured while delete WGS Connection, Check details: " + e.getMessage());
			System.out.println("Exception occured when Delete WGS Connection");
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
			driver.findElement(By.id("Delete failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"NewDashboardname", "ConnectionName", "Server", "PortNumber"})
	@Test(priority = 24)
	@TestRail(testCaseId = 25)
	public void AddWorkgroupFromPlusIcon(String NewDashboardname, String ConnectionName, String Server, String PortNumber, ITestContext context) throws Exception {

		try {
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
			
			AddWGSConnection(NewDashboardname, ConnectionName, Server, PortNumber, context);
			
			// Click on + icon
			driver.findElement(By.cssSelector("img[title=\"Add Workgroup Server\"]")).click();
			
			//search with connection name
			driver.findElement(By.xpath("//div[2]/div/input")).sendKeys(ConnectionName);
			Thread.sleep(2000);
			
			//Select checkbox
			driver.findElement(By.id(""+ ConnectionName +"")).click();
			Thread.sleep(2000);
			
			//Click on save changes
			driver.findElement(By.xpath("//button[contains(.,'Save changes')]")).click();
			Thread.sleep(2000);
			
			//Enter password
			driver.findElement(By.name("wgsPassword")).sendKeys("admin");
			
			//Click on Ok button
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(8000);
		
			// Store the Viewlet data into string
			String ViewletData = driver.findElement(By.xpath("//datatable-body")).getText();
			// System.out.println(ViewletData);

			// Verification Condition
			if (ViewletData.contains(ConnectionName)) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Workgroup server is added successfylly");
				System.out.println("Workgroup server is added successfylly");
				Reporter.log("Workgroup server is added successfylly");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed adding workgrou server");
				System.out.println("Workgroup Server is not added");
				driver.findElement(By.xpath("Condition failed")).click();
			}
		} catch (Exception e) {
			driver.findElement(By.cssSelector(".g-button-red")).click();
			/*
			 * File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			 * FileUtils.copyFile(scrFile,new File(Screenshotpath+ "/"+
			 * "AddWorkgroupFromPlusIcon.png"));
			 */
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception Occured when adding the work group server : " + e.getMessage());
			System.out.println("Exception Occured when adding the work group server");
			driver.findElement(By.cssSelector(".g-button-red")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"NewDashboardname", "ConnectionName", "ChangedPortNumber" })
	@TestRail(testCaseId = 26)
	@Test(priority = 25, dependsOnMethods= {"AddWorkgroupFromPlusIcon"})
	public static void EditWorkgroup(String NewDashboardname, String ConnectionName, String ChangedPortNumber, ITestContext context) throws Exception {
		try {
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
			
			//Search with connection name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ConnectionName);
						
			// Select the Edit WGS option
			WebElement EditWGS = driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"));
			EditWGS.click();
			Thread.sleep(LowSleep);
			driver.findElement(By.linkText("Edit workgroup server")).click();
			Thread.sleep(2000);
			
			WebElement bu=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr=bu.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr.size());
			for(WebElement active : tr)
			{
				System.out.println("Text is: " +active.getText());
				if(active.getText().contains(ConnectionName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active.click();
						break;
					}
				}
			}

			// Click on Modify button
			driver.findElement(By.xpath("//button[contains(.,'Modify')]")).click();

			// Change port number
			driver.findElement(By.xpath("//div[3]/div/div/input")).clear();
			driver.findElement(By.xpath("//div[3]/div/div/input")).sendKeys(ChangedPortNumber);
			Thread.sleep(2000);
			
			//Click on ok button
			driver.findElement(By.xpath("//form/div/div[2]/div/div[2]/button")).click();
			Thread.sleep(4000);
			
			WebElement bu1=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
			List<WebElement> tr1=bu1.findElements(By.tagName("tr"));
			System.out.println("No of trs: " +tr1.size());
			for(WebElement active1 : tr1)
			{
				System.out.println("Text is: " +active1.getText());
				if(active1.getText().contains(ConnectionName))
				{
					//System.out.println("Class is: " +active.getAttribute("class"));
					if(active1.getAttribute("class").contains("table-row-selected"))
					{
						System.out.println("Server is already selected");
					}
					else
					{
						active1.click();
						break;
					}
				}
			}
			Thread.sleep(2000);
			
			//Get the port number and store into string
			String portno=driver.findElement(By.xpath("//tr[3]/td[2]")).getText();
			System.out.println("Update port number is: " +portno);
			
			//click on ok button
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
			Thread.sleep(4000);
		
			// Verification condition
			if (portno.equalsIgnoreCase(ChangedPortNumber)) {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Updating Workgroup server is working fine");
				System.out.println("Updating Workgroup server is working fine");
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to edit workgroup server");
				System.out.println("Updating Workgroup server is not working fine");
				driver.findElement(By.xpath("condition failed")).click();
			}
		} catch (Exception e) {

			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception Occured when Editing the work group server :" + e.getMessage());
			System.out.println("Exception Occured when Editing the work group server");
			driver.findElement(By.cssSelector(".g-button-red")).click();
		}
		Thread.sleep(1000);

	}
	
	@Parameters({"NewDashboardname", "ConnectionName"})
	@TestRail(testCaseId = 27)
	@Test(priority = 26, dependsOnMethods= {"EditWorkgroup"})
	public void DeleteWorkgroup(String NewDashboardname, String ConnectionName, ITestContext context) throws Exception {
		try {
			
			//Open dash board
			ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
			obj.MoveDashboard(NewDashboardname, driver);
			
			//Search with connection name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ConnectionName);
			
			WebElement DeleteWGS = driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"));
			if (DeleteWGS.isDisplayed()) {
				// Select the Delete WGS option
				System.out.println("Condition");
				DeleteWGS.click();
				driver.findElement(By.linkText("Delete workgroup server")).click();
				Thread.sleep(4000);
				driver.findElement(By.id("accept-true")).click();
				Thread.sleep(8000);

				// Store the Viewlet data into string
				String WGSServerData = driver.findElement(By.xpath("//datatable-body")).getText();
			    //System.out.println(WGSServerData);
				
				for(int j=0; j<=ConnectionName.length(); j++)
				{
					driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
				}
				Thread.sleep(1000);

				// Verification of delete work group server
				if (WGSServerData.contains(ConnectionName)) {
					System.out.println("WGS is not deleted");
					context.setAttribute("Status", 5);
					context.setAttribute("Comment", "Failed Deleting Workgroup server");
					driver.findElement(By.xpath("Deleting failed")).click();
				} else {
					context.setAttribute("Status", 1);
					context.setAttribute("Comment", "Deleting Workgroup server is success");
					System.out.println("WGS is deleted successfully");
				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment","Got exception while deleting workgroup server check details : " + e.getMessage());
			// TODO Auto-generated catch block
			System.out.println("Second WGS is not present");
		}
	}
	
	@TestRail(testCaseId = 35)
	@Test(priority = 27)
	public void SearchFilter(ITestContext context) throws Exception {
		try {			
			/*
			 * // Add New WGS10 this.AddWorkgroupFromPlusIcon(context); Thread.sleep(2000);
			 */
			//Get the WGS name 
			String WGSSearchInputData=driver.findElement(By.xpath("//datatable-body-cell[3]/div/span")).getText();

			// Enter the search data into search field
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(WGSSearchInputData);
			Thread.sleep(2000);

			// driver.findElement(By.id("faile test case")).click();

			// Store the Viewlet data into String
			String Viewletdata = driver.findElement(By.xpath("//datatable-body")).getText();
			// System.out.println(Viewletdata);

			// Split the Number of rows present in the viewlet
			String[] multiwords = Viewletdata.split("4010", 1);
			// System.out.println("The Rows are:" +Arrays.toString(multiwords));
			Thread.sleep(2000);

			// Check the each row contains the searched data or not
			for (String a : multiwords) {
				if (a.toUpperCase().contains(WGSSearchInputData.toUpperCase())) {
					context.setAttribute("Status", 1);
					context.setAttribute("Comment", "Search is working fine");
					System.out.println("Search is working fine");
				} else {
					context.setAttribute("Status", 5);
					context.setAttribute("Comment", "Search is not working");
					System.out.println("Search is not working fine");
					driver.findElement(By.xpath("Search is failed")).click();
				}

			}
			Thread.sleep(2000);
			// Clear the Search data+-
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			Thread.sleep(2000);
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured while doing search on workgroup server, Check details: " + e.getMessage());
			driver.findElement(By.id("Failing the condition")).click();
		}
	}
	
	@Parameters({"NewDashboardname"})
	@Test(priority = 30)
	public void Logout(String NewDashboardname) throws Exception 
	{
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboardForWGSExpert(driver, NewDashboardname);
		
		//Logout option
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		Thread.sleep(4000);
		driver.findElement(By.id("yesButton")).click();
		driver.close();

	}
	
	
	public List<WebElement> Getlist()
	{
		WebElement lis=driver.findElement(By.className("wrapper-dropdown")).findElement(By.tagName("ul"));
		List<WebElement> op=lis.findElements(By.tagName("li"));
		return op;
	}
	
	public void ServerSelection(String RemoteQueueManagerName)
	{
		WebElement bu=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
		List<WebElement> tr=bu.findElements(By.tagName("tr"));
		System.out.println("No of trs: " +tr.size());
		for(WebElement active : tr)
		{
			System.out.println("Text is: " +active.getText());
			if(active.getText().contains(RemoteQueueManagerName))
			{
				//System.out.println("Class is: " +active.getAttribute("class"));
				if(active.getAttribute("class").contains("table-row-selected"))
				{
					System.out.println("Server is already selected");
				}
				else
				{
					active.click();
					break;
				}
			}
		}
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
