package ApodGUI;

import java.io.File;
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
import Common.CompareObjects;
import Common.Dashboard;
import Common.DifferenceOfObjects;
import Common.Discoverfull;
import Common.LogoutForAll;

//import com.gargoylesoftware.htmlunit.javascript.host.Set;

import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class TopicViewlet 
{
    String FinaleCopyAsName="";
	static WebDriver driver;
	
	static String Screenshotpath;
	static String WGSName;
	static String DWGS;
	static String Dnode;
	static String DestinationManager;
	static String DestinationQueue;
	static String Manager1;
	static String Manager2;
	static String Manager1Queuename;
	static String Manager2Queuename;
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
		DWGS =Settings.getDWGS();
		Dnode =Settings.getDnode();
		DestinationManager =Settings.getDestinationManager();
		DestinationQueue =Settings.getDestinationQueue();
		Manager1 =Settings.getManager1();
		Manager2 =Settings.getManager2();
		Manager1Queuename =Settings.getManager1Queuename();
		Manager2Queuename =Settings.getManager2Queuename();
		Node_Hostname =Settings.getNode_Hostname();
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
				
		//Delete existing dashboard
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboard(driver, Dashboardname);
		
		//Click on Create button
		//driver.findElement(By.xpath("//app-side-dashboard-menu/div/div/div[2]/div[2]")).click();
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		Thread.sleep(LowSleep);
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(MediumSleep);
	}
	
	@Parameters({"Topicname"})
	@TestRail(testCaseId=132)
	@Test(priority=1)
	public static void AddTopicViewlet(String Topicname, ITestContext context) throws InterruptedException
	{
		try {
		//Click on Viewlet
		driver.findElement(By.xpath("//button[3]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		Thread.sleep(LowSleep);
		
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
				
		//Create Topic
		driver.findElement(By.cssSelector(".object-type:nth-child(6)")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(Topicname);
		Thread.sleep(LowSleep);
		
		//Work group server selection
		/*
		 * Select dd=new
		 * Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		 * Thread.sleep(2000); dd.selectByVisibleText(WGSName);
		 */
		
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
		
		//Select node value          
		driver.findElement(By.xpath("//ng-select/div/span[2]")).click();
		//driver.findElement(By.cssSelector(".ng-select-focused .ng-arrow-wrapper")).click();
		Thread.sleep(4000);
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
	
		driver.findElement(By.id("save-viewlet")).click();
		Thread.sleep(MediumSleep);
				
		if(driver.getPageSource().contains(Topicname))
		{
			System.out.println("Topic Viewlet is created");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Topic viewlet is created successfully");
		}
		else
		{
			System.out.println("Topic viewlet is not created");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to create topic viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
		Thread.sleep(1000);
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Got an exception while creating topic viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
	}
	
	@Parameters({"Dashboardname", "schemaName"})
	@TestRail(testCaseId=133)
	@Test(priority=29)
	public static void ShowObjectAttributesForTopic(String Dashboardname, String schemaName,ITestContext context) throws InterruptedException
	{
	
		try {
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.TopicObjectAttributesVerification(Dashboardname, driver, schemaName, WGSName);
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "Show object attribute for Topic viewlet is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Got an exception while show object attributes option is selected for topic viewlet, check details: "+ e.getMessage());
		}
	}
	
	@TestRail(testCaseId=1156)
	@Test(priority=28)
	public void ShowTopicStatus(ITestContext context) throws InterruptedException
	{
		//Select Show topic status option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Show Topic Status")).click();
		Thread.sleep(MediumSleep);
		
		//Get the status data into string
		String StatusData=driver.findElement(By.xpath("//th[3]")).getText();
		System.out.println("Status data is: " +StatusData);
		
		if(StatusData.contains("Topic String"))
    	{
    		System.out.println("Topic status is working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "topic status is working fine");
		
    	}
    	else
    	{
    		System.out.println("Topic status is not working");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "topic status failed");
			driver.findElement(By.cssSelector(".close-button")).click();
		
    		driver.findElement(By.xpath("Topic status failed")).click();
    	}
		
		//Close the popup page
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(MediumSleep);
		
	}
	
	
	@Parameters({"Dashboardname", "TopicNameFromOptions", "Description", "TopicUniquestring"})
	@TestRail(testCaseId=134)
	@Test(priority=3)
	public void CreateTopic(String Dashboardname, String TopicNameFromOptions, String Description, String TopicUniquestring, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Search with the manager name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(DestinationManager);
		Thread.sleep(LowSleep);
		
		//Select Create new Topic option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Create Topic")).click();
		Thread.sleep(LowSleep);
		
		//Give the name of the topic
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(TopicNameFromOptions);
		Thread.sleep(LowSleep);
		
		//Add Data into description field
		driver.findElement(By.id("description")).sendKeys(Description);
		Thread.sleep(LowSleep);
		
		//Add Topic string
		driver.findElement(By.id("topicString")).sendKeys(TopicUniquestring);
		Thread.sleep(LowSleep);
		
		//Click on submit button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
				
		try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(LowSleep);
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
		
		for(int j=0; j<=DestinationManager.length(); j++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Search with the added Topic name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TopicNameFromOptions);
    	Thread.sleep(LowSleep);
		
		//Store the Topic viewlet data into string
		String Viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
		
		//Edit the search field data
    	for(int j=0; j<=TopicNameFromOptions.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
		
    	//Verification condition
		if(Viewletdata.contains(TopicNameFromOptions))
		{
			System.out.println("Topic is created successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Topic created successfully");
		}
		else
		{
			System.out.println("Topic is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create topic");
			driver.findElement(By.xpath("Topic vielwr Failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "CopyObjectName", "TopicNameFromOptions", "TopicUniquestringForCopyAs"})
	@TestRail(testCaseId=135)
	@Test(priority=4, dependsOnMethods= {"CreateTopic"})
	public void CopyAsFromCommands(String Dashboardname, String CopyObjectName, String TopicNameFromOptions, String TopicUniquestringForCopyAs, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Search with the added Topic name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TopicNameFromOptions);
    	Thread.sleep(LowSleep);
		
		//Select Copy as From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Copy As...")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Give the object name
    	driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).sendKeys(CopyObjectName);
    	Thread.sleep(LowSleep);
    	
    	//Unique string  
    	driver.findElement(By.xpath("//div[2]/div[2]/input")).sendKeys(TopicUniquestringForCopyAs);
    	Thread.sleep(LowSleep);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(LowSleep);
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
    	    	
    	//Edit the search field data
    	for(int j=0; j<=TopicNameFromOptions.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	 //discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Combining the strings 
    	String CopyasTopicName=TopicNameFromOptions+CopyObjectName;
    	System.out.println(CopyasTopicName);
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyasTopicName);
    	Thread.sleep(LowSleep);
    	
    	/*//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(2000);
    	}*/
    	Thread.sleep(6000);
    	    	
    	//Store the viewlet data into string  
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println("Viewlet data after copy as: " +Subviewlet);
    	
    	for(int j=0; j<=CopyasTopicName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Verification condition
    	if(Subviewlet.contains(CopyasTopicName))
    	{
    		System.out.println("Topic is copied");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Copying topic option is working fine");
		
    	}
    	else
    	{
    		System.out.println("Topic is not copied");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to copy topic");
		
    		driver.findElement(By.xpath("Topic failed to copy")).click();
    	}
    	Thread.sleep(1000);	   	
	}
	
	@Parameters({"Dashboardname", "TopicNameFromOptions", "CopyObjectName"})
	@TestRail(testCaseId=136)
	@Test(priority=5, dependsOnMethods= {"CopyAsFromCommands"})
	public void DeleteFromCommands(String Dashboardname, String TopicNameFromOptions, String CopyObjectName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		String CopyasTopicName=TopicNameFromOptions+CopyObjectName;
		
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyasTopicName);
    	Thread.sleep(LowSleep);
		    	
		//Select Delete From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Delete")).click();
    	Thread.sleep(MediumSleep);
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	    	
    	//Edit the search field data
    	for(int j=0; j<=CopyasTopicName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(CopyasTopicName))
    	{
    		System.out.println("Topic is not deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete topic");
    		driver.findElement(By.xpath("Topic delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Topic is deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Deletion of topic option is working fine");
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "TopicNameFromOptions", "MessageData", "PropertyName", "PropertyValue", "AddSubscriptionName", "TopicUniquestring"})
	@TestRail(testCaseId=137)
	@Test(priority=6, dependsOnMethods= {"CreateTopic"})
	public void PublishFromCommands(String Dashboardname, String TopicNameFromOptions, String MessageData, String PropertyName, String PropertyValue, String AddSubscriptionName, String TopicUniquestring, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
				
		this.Addsubscription(AddSubscriptionName, TopicNameFromOptions, TopicUniquestring);
		
		//Show Empty queues
    	driver.findElement(By.xpath("//i[4]")).click();
    	Thread.sleep(LowSleep);
    	driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click();
    	Thread.sleep(LowSleep);
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(MediumSleep);
    	
    	//Search the queue
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(DestinationQueue);
    	Thread.sleep(LowSleep);
    	
    	//get the Current depth of the queue
    	String Queuedepth=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
    	int result = Integer.parseInt(Queuedepth);
		System.out.println("Initial depth of the queue: " +result);
		
		//Search with topic name
		driver.findElement(By.xpath("(//input[@type='text'])[9]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[9]")).sendKeys(TopicNameFromOptions);
		Thread.sleep(LowSleep);
    	
		//Select publish From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Publish")).click();
    	Thread.sleep(MediumSleep);
		
    	//Send the New name into field
    	driver.findElement(By.id("messageData")).sendKeys(MessageData);
    	Thread.sleep(LowSleep);
    	
    	//driver.findElement(By.id("propertyName")).sendKeys(PropertyName);
    	//driver.findElement(By.id("propertyValue")).sendKeys(PropertyValue);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    		Thread.sleep(LowSleep);
    		driver.findElement(By.cssSelector(".btn-danger")).click();
    		Thread.sleep(LowSleep);
    	}
    	catch (Exception e)
    	{
    		
    		System.out.println("Error not present while publishing topic");
       	}
    	
    	Thread.sleep(4000);
    	
    	for(int j=0; j<=TopicNameFromOptions.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[9]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Open the browse messages page and close it
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	driver.findElement(By.linkText("Browse messages")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Close the popup page
    	driver.findElement(By.xpath("//app-console-tabs/div/div/ul/li/div/div[2]/i")).click();
    	Thread.sleep(LowSleep);
    	System.out.println("Pop up is closed");
    	
    	//Refresh the queue viewlet
    	for(int i=0; i<=4; i++)
    	{
    		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    		Thread.sleep(4000);
    	}
    	
    	//System.out.println("get queue depth");
    	
    	//get the Current depth of the queue
    	String Queuedepth1=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
    	//String Queuedepth1=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth0 +"]/div/span")).getText();
    	int result1 = Integer.parseInt(Queuedepth1);
		System.out.println("Depth of the after publishing: " +result1);
    	
		//Restore the settings
    	driver.findElement(By.xpath("//i[4]")).click();
    	Thread.sleep(LowSleep);
    	driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click();
    	Thread.sleep(LowSleep);
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(MediumSleep);
    	
    	if(result!=result1)
    	{
    		System.out.println("Publish the messgae into queue is done");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Publishing the mnessages is working fine");
    	}
    	else
    	{
    		System.out.println("Publish the messgae into queue is failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to publish messages");
    		driver.findElement(By.xpath("Publish failed")).click();
    	}
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 999)
	@Test(priority=7)
	public void MQSCSnapshot(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Save the Topic name into string
		String Topicname=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Object name is: " +Topicname);
		
		//Select MQSCSnapshot option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("MQSC"))).perform();
    	driver.findElement(By.linkText("Snapshot...")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Get the snapshot data and store into string
    	String SnapshotData=driver.findElement(By.xpath("//textarea")).getText();
    	System.out.println("Snapshot data is: " +SnapshotData);
    	
    	//verification
    	if(SnapshotData.contains(Topicname))
    	{
    		System.out.println("MQSC sanpshot is opened");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "MQSC Snapshot is opened");
    	}
    	else
    	{
    		System.out.println("MQSC Sanpshot is not opened");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "MQSC Snapshot is not opened");
			driver.findElement(By.id("Topic snapshot failed")).click();
    	}
	}
	
	
	@TestRail(testCaseId = 1000)
	@Test(priority=8, dependsOnMethods= {"MQSCSnapshot"})
	public void SaveTopicSnapshot(ITestContext context)
	{
		try
		{
			//Click on Save button
			driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
			Thread.sleep(MediumSleep);
			
			//Click on cancel button
			driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
			
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "MQSC Snapshot is saved");
		}
		catch (Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "MQSC Snapshot is not saved");
			driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
			driver.findElement(By.id("MQSC snapshot save failed")).click();
		}
		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=9)
	@TestRail(testCaseId=138)
	public void TopicProperties(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Select Properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		//storing the name field status into boolean
		boolean NameField=driver.findElement(By.id("name")).isEnabled();
		System.out.println(NameField);
		
		//Verification Condition
		if(NameField == false)
		{
			System.out.println("The Topic name field is Disabled");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "The Topic name field is Disabled");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(4000);
		}
		else
		{
			System.out.println("The Topic name field is Enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "The Topic name field is Enabled on properties option");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(4000);
			driver.findElement(By.xpath("Name field is disabled")).click();
			
		}		
	}
	@Parameters({"Dashboardname"})
	@Test(priority=10)
	@TestRail(testCaseId=139)
	public static void TopicEvents(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Select channel Events option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Events...")).click();
		Thread.sleep(MediumSleep);
		
		//Events Popup page
		String Eventdetails=driver.findElement(By.cssSelector(".custom-column-H-L-border")).getText();
		System.out.println(Eventdetails);
				
		//Verification condition
		if(Eventdetails.equalsIgnoreCase("Event #"))
		{
			System.out.println("Events page is opened");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Events page is opened and working fine");
		}
		else
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to open events page");
			System.out.println("Events page is not opened");
			driver.findElement(By.xpath("Events failed")).click();
		}
				
		//Clicking on Events Count
		try 
		{
			if(driver.findElement(By.xpath("//span/div[2]")).isDisplayed())
			{
				String Eventcount=driver.findElement(By.xpath("//span/div[2]")).getText();
				System.out.println(Eventcount);
				driver.findElement(By.xpath("//span/div[2]")).click();
				Thread.sleep(LowSleep);
				
				//Click on daignostic tab
				driver.findElement(By.xpath("//button[contains(.,'Diagnostic')]")).click();
				Thread.sleep(MediumSleep);
				
				//get the vents count and store the into string
				String DignosticCount=driver.findElement(By.name("event#")).getAttribute("value");
				System.out.println("Daignostic events count:" +DignosticCount);
				
				if(Eventcount.equalsIgnoreCase(DignosticCount))
				{
					System.out.println("Events count is matched");
					context.setAttribute("Status", 1);
					context.setAttribute("Comment", "Event Count is Matched and working fine");
					//Close the Event details page
					//driver.findElement(By.xpath("//app-mod-event-details/div/div[2]/button")).click();
					WebElement close=driver.findElement(By.xpath("//app-mod-event-details/div/div[2]/button"));
					JavascriptExecutor js = (JavascriptExecutor)driver;
					js.executeScript("arguments[0].click();", close);
				}
				else
				{
					System.out.println("Events count is not matched");
					context.setAttribute("Status", 5);
					context.setAttribute("Comment", "Got exception while opening events page, check details: ");
					driver.findElement(By.xpath("//app-mod-event-details/div/div[2]/button")).click();
					driver.findElement(By.cssSelector(".close-button")).click();
					driver.findElement(By.id("Events count failed")).click();
				}
				
			}
		}
		catch (Exception e)
		{
			System.out.println("Events are not present");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Events not found");
		}
				
		//Close the events popup page
		driver.findElement(By.cssSelector(".close-button")).click();
		
	}
				
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=140)
	@Test(priority=11)
	public static void AddToFavoriteViewlet(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Store Topic name into string
		String TopicName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Create the favorite viewlet
		driver.findElement(By.xpath("//button[3]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.id("fav")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		Thread.sleep(LowSleep);
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		Thread.sleep(LowSleep);
		
		//Select WGS dropdown
		//driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div/div[2]/div/ng-select/div/span")).click();
		Thread.sleep(LowSleep);
		
		//Deselect WGS 
		driver.findElement(By.xpath("//span[2]/i")).click();
		Thread.sleep(LowSleep);
		
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
		Thread.sleep(2000);
		
		//Submit
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(MediumSleep);
		
		//Select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		WebElement fav=driver.findElement(By.linkText("Add to favorites..."));
		JavascriptExecutor addfav = (JavascriptExecutor)driver;
		addfav.executeScript("arguments[0].click();", fav);
		Thread.sleep(LowSleep);
		
		//Select favorite viewlet
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div/span")).click();
		Thread.sleep(LowSleep); 
		
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
		Thread.sleep(MediumSleep);
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		                                            
		//Verification of topic added to favorite viewlet 
		if(Favdata.contains(TopicName))
		{
			System.out.println("Topic name is added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Topic is added to Favorite viewlet");
		}
		else
		{
			System.out.println("Topic name is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add Topic name to Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	private void topiccreation(String TopicNameFromIcon, String DescriptionFromIcon, String TopicUniquestringFromICon, ITestContext context, String manager) throws InterruptedException
	{
		//Click on + icon present in the viewlet
		driver.findElement(By.xpath("//img[@title='Add Topic']")).click();
		Thread.sleep(LowSleep);
		
		//Select WGS
		/*
		 * Select WGS=new Select(driver.findElement(By.xpath("//select")));
		 * WGS.selectByVisibleText(WGSName); Thread.sleep(3000);
		 */
		
		/*driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/ng-select/div/span")).click();
		Thread.sleep(LowSleep);
		try 
		{
			WebElement ChannelauthNode=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
			List<WebElement> divs=ChannelauthNode.findElements(By.tagName("div"));
			System.out.println(divs.size());	
			for (WebElement di : divs)
			{					
				if(di.getText().equals(WGSName))
				{
					di.click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(2000);
		
		//Select Node 
		driver.findElement(By.xpath("//ng-select[2]/div/span")).click();
		Thread.sleep(LowSleep);
		try 
		{
			WebElement ChannelauthNode=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
			List<WebElement> divs=ChannelauthNode.findElements(By.tagName("div"));
			System.out.println(divs.size());	
			for (WebElement di : divs)
			{					
				if(di.getText().equals(Dnode))
				{
					di.click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(2000);*/
		
		//Select Manager
		driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div[2]/ng-select/div/span")).click(); 
		Thread.sleep(LowSleep);
        try 
		{
        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
			List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
			System.out.println(mdivs.size());	
			
			for (WebElement mdi : mdivs)
			{
				if(mdi.getText().equals(manager))
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
		
		//Click on Select path button
        driver.findElement(By.xpath("//button[contains(.,'Select path')]")).click();
        Thread.sleep(LowSleep);
		
		//Give the name of the topic
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(TopicNameFromIcon);
		Thread.sleep(LowSleep);
		
		//Add Data into description field
		driver.findElement(By.id("description")).sendKeys(DescriptionFromIcon);
		Thread.sleep(LowSleep);
		
		//Add Topic string
		driver.findElement(By.id("topicString")).sendKeys(TopicUniquestringFromICon);
		Thread.sleep(LowSleep);
		
		//Click on submit button
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(HighSleep);
				
		try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
			Thread.sleep(LowSleep);
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
		
		//Edit the search field data
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TopicNameFromIcon);
    	Thread.sleep(LowSleep);
    	
		
		//Store the Topic viewlet data into string
		String Viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
		//System.out.println(Viewletdata);
		
		//Edit the search field data
    	for(int j=0; j<=TopicNameFromIcon.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
		
		//Verification condition
		if(Viewletdata.contains(TopicNameFromIcon))
		{
			System.out.println("Topic is created successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Topics are added created using add icon");
		}
		else
		{
			System.out.println("Topic is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create topic using add icon");
			driver.findElement(By.xpath("Topic vielwr Failed")).click();
		}
		Thread.sleep(1000);	
	}
	
	@Parameters({"Dashboardname", "TopicNameFromIcon", "DescriptionFromIcon", "TopicUniquestringFromICon"})
	@TestRail(testCaseId=147)
	@Test(priority=12)
	public void CreateTopicFromPlusIcon(String Dashboardname, String TopicNameFromIcon, String DescriptionFromIcon, String TopicUniquestringFromICon, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);	
				
		topiccreation(TopicNameFromIcon, DescriptionFromIcon, TopicUniquestringFromICon, context, Manager1);	
			
	}
	
	
	@Test(priority=13)
	@TestRail(testCaseId=141)
	public static void CompareTopics(ITestContext context) throws InterruptedException
	{
		//Click on clear all check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(LowSleep);         
		
		//Get the First object Name
		String compare1 = driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("First obj name is: " +compare1);                                            
		Thread.sleep(LowSleep);
		
		//Get the second object name
		String compare2 = driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Second obj name is: " +compare2);
		Thread.sleep(LowSleep);
		
		// Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		
		// System.out.println("Cpmare to: " + compare1 + "::"+ compare2);
		String comparenameslist = compare1  +" Attribute Value"+ "::"+ compare2  +" Attribute Value";
		driver.findElement(By.linkText("Compare")).click();
		Thread.sleep(MediumSleep);
		System.out.println("Before names are: " +comparenameslist);


		// Reading comparing
		String aftercompare1 = driver.findElement(By.xpath("//span[2]/div")).getText();
		String aftercompare2 = driver.findElement(By.xpath("//span[3]/div")).getText();
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
		Thread.sleep(4000);	
		
	}
	
	
	@TestRail(testCaseId = 775)
	@Test(priority=14)
	public void CheckDifferencesForTopics(ITestContext context) throws InterruptedException
	{
		//Click on clear all check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(LowSleep);
		
		// Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Compare")).click();
		Thread.sleep(MediumSleep);
		
		// Check differences only option while compare
		driver.findElement(By.cssSelector(".differences .slider")).click();
		Thread.sleep(LowSleep);
			
		try {
			
			WebElement ee=driver.findElement(By.className("paddless"));
			List<WebElement> AttributesData=ee.findElements(By.tagName("div"));
			System.out.println("Divs size is: " +AttributesData.size());
			
			System.out.println("AttributesData count: " + AttributesData.size());
			int k =0;
			for (int i = 1; i < AttributesData.size(); i++) 
			{
				String style = AttributesData.get(i).getAttribute("style");
				System.out.println("Style is: " +style);
				
				if (!(style.contains("display: none"))) 
				{
					System.out.println("difference index :" +i);
					k=i+1;
					break;
				}
				
			}
			
			System.out.println("After for loop: " +k);

			
			boolean verifydiff = false;
			if(k>0)
			{
			String difference1=driver.findElement(By.xpath("//span[2]/div["+ k +"]")).getText();
			System.out.println("First value: " +difference1);
			String difference2=driver.findElement(By.xpath("//span[3]/div["+ k +"]")).getText();
			System.out.println("Second value: " +difference2);

			if(!(difference1.isEmpty() && difference2.isEmpty()))
			{
			for (int i = 1; i < AttributesData.size(); i++) 
			{
				String cls = AttributesData.get(i).getAttribute("style");
				//System.out.println("classname: "+ cls);
				if (!cls.contains("display: none")) 
					{
					System.out.println("index: " + i);
					String secondvalue;
					String firstvalue;
					
					int j=i+1;
					firstvalue = driver.findElement(By.xpath("//span[2]/div["+ j +"]")).getText();
					System.out.println("First value in loop: " + firstvalue);
					secondvalue = driver.findElement(By.xpath("//span[3]/div["+ j +"]")).getText();
					System.out.println("Second value in loop: " + secondvalue);
					
					if (!firstvalue.equalsIgnoreCase(secondvalue)) 
					{
						verifydiff = true;
					}
				}

				}
			
			}
			else
			{
			verifydiff=true;
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
	
	@Parameters({"Dashboardname", "TopicNameFromIcon", "DescriptionFromIcon", "TopicUniquestringFromICon", "CopyObjectNameForMultiple", "TopicUniquestringForMultipleCopy"})
	@TestRail(testCaseId=142)
	@Test(priority=15, dependsOnMethods= {"CreateTopicFromPlusIcon"})
	public void CopyAsFromCommandsForMultipleTopics(String Dashboardname, String TopicNameFromIcon, String DescriptionFromIcon, String TopicUniquestringFromICon, String CopyObjectNameForMultiple, String TopicUniquestringForMultipleCopy, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		topiccreation(TopicNameFromIcon, DescriptionFromIcon, TopicUniquestringFromICon, context, Manager2);
		
		//Search with the topic name from icon
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TopicNameFromIcon);
		Thread.sleep(LowSleep);
		
		//Select the multiple Topics and choose Copy as for multiple topics
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions Mousehovercopy=new Actions(driver);
		Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		Thread.sleep(LowSleep);
    	driver.findElement(By.linkText("Copy As...")).click();
    	Thread.sleep(LowSleep);
		
		//Get Existing string name
		String Objectname=driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).getAttribute("value");
		System.out.println("Existing object name is :" +Objectname);
		
		//Give the object name
    	driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).sendKeys(CopyObjectNameForMultiple);
    	Thread.sleep(LowSleep);
    	driver.findElement(By.xpath("//div[2]/div[2]/input")).sendKeys(TopicUniquestringForMultipleCopy);
    	Thread.sleep(LowSleep);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(LowSleep);
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
    	
    	//discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Combine the string
    	 FinaleCopyAsName=Objectname+CopyObjectNameForMultiple;
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinaleCopyAsName);
    	Thread.sleep(LowSleep);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	for(int j=0; j<=FinaleCopyAsName.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification condition
    	if(Subviewlet.contains(FinaleCopyAsName))
    	{
    		System.out.println("Multiple Topics are copied");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "CopyAs command is working fine for multiple topics");
    	}
    	else
    	{
    		System.out.println("Multiple Topics are not copied");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to Copy multiple topics using CopyAs command");
    		driver.findElement(By.xpath("Multiple Topics failed to copy")).click();
    	}
    	Thread.sleep(1000);		
	}
	
	@Parameters({"Dashboardname", "CopyObjectNameForMultiple"})
	@TestRail(testCaseId=143)
	@Test(priority=16, dependsOnMethods= {"CopyAsFromCommandsForMultipleTopics"})
	public void DeleteFromCommandsForMultipleTopics(String Dashboardname, String CopyObjectNameForMultiple, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Send the New name into field
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinaleCopyAsName);
    	Thread.sleep(LowSleep);
    	
    	//Select the multiple topics and choose Delete option for multiple topics
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions Mousehovercopy=new Actions(driver);
		Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Delete")).click();
		Thread.sleep(MediumSleep);
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	    	
    	//Refresh the view let
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(LowSleep);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	for(int j=0; j<=FinaleCopyAsName.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(FinaleCopyAsName))
    	{
    		System.out.println("Topics are not deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete multiple topics");
    		driver.findElement(By.xpath("Topics delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Topics are deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Deleting multiple topics is working fine");
    	}
    	Thread.sleep(1000);
    	
    	
	}
	
	@Parameters({"Dashboardname", "TopicNameFromIcon", "DescriptionFromIcon", "TopicUniquestringFromICon", "AddSubscriptionNameforMultiple", "MessageDataForMultiple", "PropertyNameForMultiple", "PropertyValueForMultiple"})
	@TestRail(testCaseId=144)
	@Test(priority=17)
	public void PublishFromCommandsForMultipleTopics(String Dashboardname, String TopicNameFromIcon, String DescriptionFromIcon, String TopicUniquestringFromICon, String AddSubscriptionNameforMultiple, String MessageDataForMultiple, String PropertyNameForMultiple, String PropertyValueForMultiple, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		AddsubscriptionForMultiple(AddSubscriptionNameforMultiple, TopicNameFromIcon, Manager1, Manager1Queuename);
		AddsubscriptionForMultiple(AddSubscriptionNameforMultiple, TopicNameFromIcon, Manager2, Manager2Queuename);
		
		//Show Empty queues
    	driver.findElement(By.xpath("//i[4]")).click();
    	Thread.sleep(LowSleep);
    	driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click();
    	Thread.sleep(LowSleep);
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(MediumSleep);
    	
    	//Search the queue
    	searchqueue1();
    	    	
    	//get the Current depth of the queue
    	String Queuedepth1=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
    	int BeforeQueue1result1 = Integer.parseInt(Queuedepth1);
		System.out.println(BeforeQueue1result1);
		
		//Clear search data
		Clearqueue1();
		
		//Search queue2
		searchqueue2();
	    	
    	//get the Current depth of the queue
    	String Queuedepth2=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
    	int BeforeQueue2result2 = Integer.parseInt(Queuedepth2);
		System.out.println(BeforeQueue2result2);
		
		//Search with the Topics
		driver.findElement(By.xpath("(//input[@type='text'])[9]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[9]")).sendKeys(TopicNameFromIcon);
		Thread.sleep(LowSleep);
		
		//Select the multiple topics and choose publish option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions Mousehovercopy=new Actions(driver);
		Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Publish")).click();
		Thread.sleep(MediumSleep);
				
		//Send the New name into field
		 driver.findElement(By.id("messageData")).sendKeys(MessageDataForMultiple);
		 Thread.sleep(LowSleep);
		    	
		 driver.findElement(By.id("propertyName")).sendKeys(PropertyNameForMultiple);
		 Thread.sleep(LowSleep);
		 driver.findElement(By.id("propertyValue")).sendKeys(PropertyValueForMultiple);
		 Thread.sleep(LowSleep);
		 driver.findElement(By.cssSelector(".btn-primary")).click();
		 Thread.sleep(MediumSleep);
    	
		 try
	    {
	    	driver.findElement(By.id("yes")).click();
	    	Thread.sleep(LowSleep);
	    	driver.findElement(By.cssSelector(".btn-danger")).click();
	    	Thread.sleep(LowSleep);
	    }
	    catch (Exception e)
	    {
	    	System.out.println("Error not present while publishing topic");
	    }
    	Thread.sleep(4000);
    	
    	//Remove the serach
    	for(int k=0; k<=TopicNameFromIcon.length(); k++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[9]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//search with queue1
    	searchqueue1();
    	
    	//Open the browse messages page and close it for queue1
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	driver.findElement(By.linkText("Browse messages")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Close the popup page
    	driver.findElement(By.xpath("//app-console-tabs/div/div/ul/li/div/div[2]/i")).click();
    	Thread.sleep(LowSleep);
    	
    	
    	//Refresh the queue viewlet
    	for(int i=0; i<=4; i++)
    	{
    		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    		Thread.sleep(4000);
    	}
    	    	
    	//get the Current depth of the queue
    	String Queuedepthforqueue1=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
    	//String Queuedepth1=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth0 +"]/div/span")).getText();
    	int Afterresult1 = Integer.parseInt(Queuedepthforqueue1);
		//System.out.println("Depth of the after publishing: " +Afterresult1);
		
		//Clear search1
		Clearqueue1();
		
		//search with queue1
    	searchqueue2();
    	
    	//Open the browse messages page and close it for queue1
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	driver.findElement(By.linkText("Browse messages")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Close the popup page
    	driver.findElement(By.xpath("//app-console-tabs/div/div/ul/li/div/div[2]/i")).click();
    	Thread.sleep(LowSleep);
    	    	
    	//Refresh the queue viewlet
    	for(int i=0; i<=4; i++)
    	{
    		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    		Thread.sleep(4000);
    	}
    	
    	//get the Current depth of the queue
    	String Queuedepthforqueue2=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
    	//String Queuedepth1=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth0 +"]/div/span")).getText();
    	int Afterresult2 = Integer.parseInt(Queuedepthforqueue2);
		//System.out.println("Depth of the after publishing: " +Afterresult2);
		
		//Clear search1
		Clearqueue2();
    	
		//Show Empty queues
    	driver.findElement(By.xpath("//i[4]")).click();
    	Thread.sleep(LowSleep);
    	driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click();
    	Thread.sleep(LowSleep);
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(MediumSleep);
    	
    	System.out.println("Before queue1 is: " +BeforeQueue1result1);
    	System.out.println("Before queue2 is: " +BeforeQueue2result2);
    	System.out.println("After queue1 is: " +Afterresult1);
    	System.out.println("After queue2 is: " +Afterresult2);
    	
    	if(BeforeQueue1result1!=Afterresult1 && BeforeQueue2result2 != Afterresult2)
    	{
    		System.out.println("Publish the messgae into queue is done for multipublish");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Publishing the topic messages to queue is working fine");
    	}
    	else
    	{
    		System.out.println("Publish the messgae into queue is failed for multuipublish");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to publish multiple messages int queue");
    		driver.findElement(By.xpath("Multi Publish failed")).click();
    	}
	}
	
	private void searchqueue1() throws InterruptedException
	{
		//Search the queue
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Manager1Queuename);
    	Thread.sleep(LowSleep);
	}
	
	private void searchqueue2() throws InterruptedException
	{
		//Search the queue
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Manager2Queuename);
    	Thread.sleep(LowSleep);
	}
	
	private void Clearqueue1()
	{
		for(int i=0; i<=Manager1Queuename.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
	}
	
	private void Clearqueue2()
	{
		for(int i=0; i<=Manager2Queuename.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
	}
	
	
	@Parameters({"Dashboardname", "MultipleDescription", "Communicationinfo"})
	@TestRail(testCaseId=145)
	@Test(priority=18)
	public void MultipleProperties(String Dashboardname, String MultipleDescription, String Communicationinfo, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Select the multiple topics and choose properties option for multiple topics
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(HighSleep);         
		
		//Give the description for multiple topics
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys(MultipleDescription);
		Thread.sleep(LowSleep);
		
		/*//Give the application id for multiple topics
		driver.findElement(By.id("communicationInfo")).clear();
		driver.findElement(By.id("communicationInfo")).sendKeys(Communicationinfo);*/
		
		//click on OK
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname,driver);
				
		//Open the properties for First topic
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		//Get the description and communication info for First topic
		String FirstDescription=driver.findElement(By.id("description")).getAttribute("value");
		//String FirstCommunicationinfo=driver.findElement(By.id("communicationInfo")).getAttribute("value");
		
		//close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
		che2.Deselectcheckbox(Dashboardname,driver);
		
		//Open the properties for second topic
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
				
		//Get the description and communication info for second topic
		String SecondDescription=driver.findElement(By.id("description")).getAttribute("value");
		//String SecondCommunicationinfo=driver.findElement(By.id("communicationInfo")).getAttribute("value");
		
		//close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Verification 
		//if(FirstDescription.equals(MultipleDescription) && FirstCommunicationinfo.equals(Communicationinfo) && SecondDescription.equals(MultipleDescription) && SecondCommunicationinfo.equals(Communicationinfo))
		if(FirstDescription.equals(MultipleDescription) && SecondDescription.equals(MultipleDescription))
		{
			System.out.println("Properites are Updated for multiple topics");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Properties are updated successfully for multiple topics");
		}
		else
		{
			System.out.println("Properites are not Updated for multiple topics");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Faile to update properties for multiple topics");
			driver.findElement(By.xpath("Properties updation failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=146)
	@Test(priority=19, dependsOnMethods= {"AddToFavoriteViewlet"})
	public static void AddToFavoriteForMultipleTopics(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Store the Topic Names into string
		String TopicName2=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		String TopicName3=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		WebElement fav=driver.findElement(By.linkText("Add to favorites..."));
		JavascriptExecutor addfav = (JavascriptExecutor)driver;
		addfav.executeScript("arguments[0].click();", fav);
		Thread.sleep(MediumSleep);
		
		//Select favorite viewlet
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div/span")).click();
		Thread.sleep(LowSleep); 
		
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
		Thread.sleep(MediumSleep);
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification of topics added to favorite viewlet
		if(Favdata.contains(TopicName2) && Favdata.contains(TopicName3))
		{
			System.out.println("Topic names are added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Topics are added successfully to Favorite viewlet");
		}
		else
		{
			System.out.println("Topic names are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add topics to Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
				
	}
	

	@Parameters({"Dashboardname"})
	@Test(priority=30)
	public static void Logout(String Dashboardname) throws InterruptedException
	{
		//Logout
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
	
	
	
	//Create Subscription Viewlet and Add Subscription
	
	@Parameters({"AddSubscriptionName", "TopicNameFromOptions", "TopicUniquestring"})
	public void Addsubscription(String AddSubscriptionName, String TopicNameFromOptions, String TopicUniquestring) throws InterruptedException
	{
		//Search with subscription name
		driver.findElement(By.xpath("(//input[@type='text'])[12]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[12]")).sendKeys(TopicNameFromOptions);
		Thread.sleep(LowSleep);
		
		//Get the manager name 
		String ManagerName=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();

		for(int j=0; j<=TopicNameFromOptions.length(); j++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[12]")).sendKeys(Keys.BACK_SPACE);
		}
				
		//Search with the manager name
		driver.findElement(By.xpath("(//input[@type='text'])[9]")).sendKeys(ManagerName);
		Thread.sleep(LowSleep);
		
		//click on check box and choose create subscription
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Create Subscription")).click();
		Thread.sleep(MediumSleep);
		
		//Give the Subscription name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(AddSubscriptionName);
		Thread.sleep(LowSleep);
		
		//Select the Topic name from the list
		/*driver.findElement(By.cssSelector(".ng-input > input")).click();
		driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div")).click();*/
		
		try 
		{
			driver.findElement(By.id("topicName")).click();
			Thread.sleep(LowSleep);
			List<WebElement> Topic=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Topic.size());	
			for (int i=0; i<Topic.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				//System.out.println("Radio button id:" + Topic.get(i).getAttribute("id"));
				String s=Topic.get(i).getText();
				if(s.equals(TopicNameFromOptions))
				{
					String id=Topic.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(1000);
		
		//Give the Topic String
		//driver.findElement(By.id("topicString")).sendKeys(TopicUniquestring);
		Thread.sleep(3000);
		
		//Click on Destination tab
		driver.findElement(By.linkText("Destination")).click();
		Thread.sleep(LowSleep);
		
		//Select WGS name
		Select DestinationWGS=new Select(driver.findElement(By.id("destinationGMName")));
		DestinationWGS.selectByVisibleText(DWGS);
		
		//Select WGS name
		/*driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).click();
		driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).sendKeys("DESKTOP-E1JT2VR");
		//span[@class, 'ng-option-label ng-star-inserted']*/	
	
		try 
		{
			driver.findElement(By.id("destinationNodeName")).click();
			Thread.sleep(LowSleep);
			List<WebElement> Node=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Node.size());	
			for (int i=0; i<Node.size();i++)
			{
				//System.out.println("Radio button text:" + Node.get(i).getText());
			//	System.out.println("Radio button id:" + Node.get(i).getAttribute("id"));
				String s=Node.get(i).getText();
				if(s.equals(Dnode))
				{
					String id=Node.get(i).getAttribute("id");
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
		
		//Select Manager value
		try 
		{
			driver.findElement(By.id("destinationQMName")).click();
			Thread.sleep(LowSleep);
			List<WebElement> Manager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
			//	System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				if(s.equals(DestinationManager))
				{
					String id=Manager.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(3000);
		
		//Select Queue name
		try 
		{
			driver.findElement(By.id("destinationQName")).click();
			Thread.sleep(LowSleep);
			List<WebElement> QueueName=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(QueueName.size());	
			for (int i=0; i<QueueName.size();i++)
			{
				//System.out.println("Radio button text:" + QueueName.get(i).getText());
				//System.out.println("Radio button id:" + QueueName.get(i).getAttribute("id"));
				String s=QueueName.get(i).getText();
				if(s.equals(DestinationQueue))
				{
					String id=QueueName.get(i).getAttribute("id");
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
		
		//Click on OK button
		driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(HighSleep);
    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    		Thread.sleep(LowSleep);
    	}
    	catch (Exception e)
    	{
    		System.out.println("Error popup is not displayed");
    	}
		
	}
	
	
	@Parameters({"AddSubscriptionNameforMultiple"})
	public void AddsubscriptionForMultiple(String AddSubscriptionNameforMultiple, String TopicNameFromIcon, String manager, String Queue) throws InterruptedException
	{	
		//Search with manager name
		driver.findElement(By.xpath("(//input[@type='text'])[12]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[12]")).sendKeys(manager);
		Thread.sleep(LowSleep);
		
		//click on check box and choose create subscription
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Create Subscription")).click();
		Thread.sleep(MediumSleep);
		
		//Give the Subscription name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(AddSubscriptionNameforMultiple);
		Thread.sleep(LowSleep);
		
		/*//Select the Topic name from the list
		driver.findElement(By.cssSelector(".ng-input > input")).click();
		driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div")).click();*/
		
		try 
		{
			driver.findElement(By.id("topicName")).click();
			Thread.sleep(LowSleep);
			List<WebElement> Topic=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(Topic.size());	
			for (int i=0; i<Topic.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				System.out.println("Radio button id:" + Topic.get(i).getAttribute("id"));
				String s=Topic.get(i).getText();
				if(s.equals(TopicNameFromIcon))
				{
					String id=Topic.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(3000);
		//Give the Topic String
		//driver.findElement(By.id("topicString")).sendKeys(TopicStringData);
		
		//Click on Destination tab
		driver.findElement(By.linkText("Destination")).click();
		Thread.sleep(LowSleep);
		
		//Select WGS name
		Select DestinationWGS=new Select(driver.findElement(By.id("destinationGMName")));
		DestinationWGS.selectByVisibleText(DWGS);
		
		//Select WGS name
		/*driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).click();
		driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).sendKeys("DESKTOP-E1JT2VR");
		//span[@class, 'ng-option-label ng-star-inserted']*/	
	
		try 
		{
			driver.findElement(By.id("destinationNodeName")).click();
			Thread.sleep(LowSleep);
			List<WebElement> Node=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(Node.size());	
			for (int i=0; i<Node.size();i++)
			{
				//System.out.println("Radio button text:" + Node.get(i).getText());
				System.out.println("Radio button id:" + Node.get(i).getAttribute("id"));
				String s=Node.get(i).getText();
				if(s.equals(Dnode))
				{
					String id=Node.get(i).getAttribute("id");
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
		
		//Select Manager value
		try 
		{
			driver.findElement(By.id("destinationQMName")).click();
			Thread.sleep(LowSleep);
			List<WebElement> Manager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				if(s.equals(manager))
				{
					String id=Manager.get(i).getAttribute("id");
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
		
		//Select Queue name
		try 
		{
			driver.findElement(By.id("destinationQName")).click();
			Thread.sleep(LowSleep);
			List<WebElement> QueueName=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(QueueName.size());	
			for (int i=0; i<QueueName.size();i++)
			{
				//System.out.println("Radio button text:" + QueueName.get(i).getText());
				System.out.println("Radio button id:" + QueueName.get(i).getAttribute("id"));
				String s=QueueName.get(i).getText();
				if(s.equals(Queue))
				{
					String id=QueueName.get(i).getAttribute("id");
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
		
		//Click on OK button
		driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    		Thread.sleep(LowSleep);
    	}
    	catch (Exception e)
    	{
    		System.out.println("Error popup is not displayed");
    	}
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
