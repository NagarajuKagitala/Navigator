package KafkaGUI;

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
import Common.KafkaLogin;
import Common.LogoutForAll;
import Common.Viewlets;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class KafkaTopicViewlet 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName;
	static String FinalTopicname="";
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
		
		//Restoring the Default Settings
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click(); 
		Thread.sleep(4000);
		driver.findElement(By.id("accept-true")).click();
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(3000);	
		
		/*
		 * //Check Show Empty Queues check box
		 * driver.findElement(By.cssSelector(".fa-cog")).click();
		 * driver.findElement(By.id("empty-kafka-topics")).click();
		 * driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		 * Thread.sleep(2000);
		 */
	
		
		//put the messages into empty queues for testing
		for(int m=1; m<=2; m++)
		{
			//Select the put new message option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions PutMessagesMousehour=new Actions(driver);
			PutMessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
			driver.findElement(By.linkText("Put New Message")).click();
			Thread.sleep(1000);
			
			//Select the number of messages
			driver.findElement(By.name("generalNumberOfMsgs")).click();
			driver.findElement(By.name("generalNumberOfMsgs")).clear();
			driver.findElement(By.name("generalNumberOfMsgs")).sendKeys("2");
			
			//Put a message data
			//driver.findElement(By.id("encoding-text-9")).click();
			driver.findElement(By.xpath("//textarea")).sendKeys("Test Message");
			driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
			Thread.sleep(4000);
			
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
	
	@Parameters({"Dashboardname"})
	@Test(priority=1)
	@TestRail(testCaseId = 904)
	public static void BrowseMessages(String Dashboardname, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Browse Messages Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Browse messages")).click();
		Thread.sleep(4000);               
		
		//Store the browse message page value into string
		String BrowseMessagespage=driver.findElement(By.cssSelector(".g-row-head:nth-child(2)")).getText();
		System.out.println(BrowseMessagespage);
		
		//verification
		if(BrowseMessagespage.equalsIgnoreCase("Offset"))
		{
			System.out.println("Message browse page is opened");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Message browse option working fine");
		}
		else
		{
			System.out.println("Message browse page is not opened");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Faile to load message browse option");
			driver.findElement(By.cssSelector(".close-button")).click();
			driver.findElement(By.xpath("Message browse page is failed")).click();
		}
		//Close the popup page
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 905)
	@Test(priority=2)
	public void BrowsePartitions(String Dashboardname, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Take the Topic name
		String TopicName=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println("Topic name is: " +TopicName);
		
		//Select Browse Partitions Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Browse partitions")).click();
		Thread.sleep(4000);
		
		WebElement e=driver.findElement(By.className("tab-content"));
		String DashboardData=e.getText();
		System.out.println("Data1 :" +DashboardData);
		
		
		String PartitionName="Topic "+TopicName+" Partitions" ;
		System.out.println("Final Name is: " +PartitionName);
		
		//System.out.println("Page source is: " +driver.getPageSource());
		if(DashboardData.contains(PartitionName))
		{
			System.out.println("Browse Partitions is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Message browse option working fine");
		}
		else
		{
			System.out.println("Browse partitions failes");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Faile to load message browse option");
			driver.findElement(By.id("Partition browse failed")).click();
		}
	}
		
		@Parameters({"Dashboardname", "SchemaName"})
		@Test(priority=14)
		public void ShowObjectAttributes(String Dashboardname, String SchemaName, ITestContext context) throws InterruptedException
		{
			try
			{
				KafkaObjectAttributes obj=new KafkaObjectAttributes();
				obj.ShowObjectAttributes(Dashboardname, driver, SchemaName);
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
		@Test(priority=3)
		@TestRail(testCaseId=906)
		public void Properties(String Dashboardname, ITestContext context) throws InterruptedException
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			try {
				// Properties option selection
				driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
				driver.findElement(By.linkText("Properties...")).click();
				Thread.sleep(2000);

				// Store the editable function in to a string
				boolean FieldNamevalue = driver.findElement(By.id("name")).isEnabled();
				System.out.println(FieldNamevalue);

				// Verification
				if (FieldNamevalue == false) {
					System.out.println("Broker Name field is UnEditable");
					context.setAttribute("Status", 1);
					context.setAttribute("Comment", "Broker Name field is UnEditable");
					driver.findElement(By.cssSelector(".btn-danger")).click();
					Thread.sleep(8000);
				} else {
					System.out.println("Broker Name field is Editable");
					context.setAttribute("Status", 5);
					context.setAttribute("Comment", "Broker Name field is Editable");
					driver.findElement(By.cssSelector(".btn-danger")).click();
					Thread.sleep(8000);
					driver.findElement(By.xpath("Broker name edit function Failed")).click();
				}
			} catch (Exception e) {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Getting exception in properties page, check details: " + e.getMessage());
				System.out.println("Getting exception in properties page");
				driver.findElement(By.xpath("Broker name edit function Failed")).click();
			}
		}
		
		
		@Parameters({"Dashboardname", "TopicNameFromOptions"})
		@TestRail(testCaseId = 907)
		@Test(priority=4)
		public void CreateKafkaTopicFromOptions(String Dashboardname, String TopicNameFromOptions, ITestContext context) throws InterruptedException
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			/*
			 * //Check Show Empty Queues check box
			 * driver.findElement(By.cssSelector(".fa-cog")).click();
			 * driver.findElement(By.id("empty-kafka-topics")).click();
			 * driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
			 * Thread.sleep(2000);
			 */
			
			try
			{
			//Select show object Attributes Option
				driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Create Topic")).click();
			Thread.sleep(1000);
			
			//Create Queue Window
			driver.findElement(By.id("name")).sendKeys(TopicNameFromOptions);
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(8000);
					
			try
			{
				driver.findElement(By.id("yes")).click();
			}
			catch (Exception e)
			{
				System.out.println("No Exception occured");
			}
			Thread.sleep(2000);
			
			for(int i=0; i<=2; i++)
			{
				driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
				Thread.sleep(6000);
			}
						
			//Serach with empty queue name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TopicNameFromOptions);
			Thread.sleep(1000);
			
			//Store the first queue name into string
			String Firstqueue=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
			System.out.println(Firstqueue);
			
			//Edit the search
			for(int k=0; k<=TopicNameFromOptions.length(); k++)
			{
				driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
			}
			Thread.sleep(1000);
			
			if(Firstqueue.equalsIgnoreCase(TopicNameFromOptions))
			{
				System.out.println("Topic is added successfully");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Topic added successfully");
			}
			else
			{
				System.out.println("Topic is added not added");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to add Topic");
				driver.findElement(By.id("Add Topic failed")).click();
			}
			}
			
			catch (Exception e)
			{
				System.out.println("Exception occured while creating a Topic");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Exception occured while adding Topic, check details: " + e.getMessage());
				if(driver.findElement(By.cssSelector(".btn-danger")).isDisplayed())
				{
					driver.findElement(By.cssSelector(".btn-danger")).click();
				}
			}
			Thread.sleep(2000);
		}
		
		
		@Parameters({"Dashboardname", "ObjectName"})
		@TestRail(testCaseId = 908)
		@Test(priority=5)
		public static void CopyAsTopicCommands(String Dashboardname, String ObjectName, ITestContext context) throws InterruptedException
		{	
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			//Take the Queue name whcih one you want to delete
			String Topicnamebefore=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
			System.out.println(Topicnamebefore);
			
			//Select copy as option from Commands
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions CopyMousehover=new Actions(driver);
			CopyMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
			driver.findElement(By.linkText("Copy As...")).click();
			Thread.sleep(1000);
			
			//Object Details
			driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).sendKeys(ObjectName);
		
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(8000);
			
			try
			{
				driver.findElement(By.id("yes")).click();
				Thread.sleep(3000);
				driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
				Thread.sleep(3000);
			}
			catch (Exception e)
			{
				System.out.println("Error popup is not displayed");
			}
			
			//refresh viewlet
			for(int i=0; i<=2; i++)
			{
				driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
				Thread.sleep(6000);
			}
					
			FinalTopicname=Topicnamebefore + ObjectName;
			
			//Search with empty queue name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalTopicname);
			Thread.sleep(1000);
			
			//Store the Queue name after deleting the Queue
			String ViewletData=driver.findElement(By.xpath("//datatable-body")).getText();
			System.out.println("Viewlet data is: " +ViewletData);
			
			for(int i=0; i<=FinalTopicname.length(); i++)
			{
				driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
			}
			Thread.sleep(2000);
			
			//Restoring the Default Settings
			driver.findElement(By.cssSelector(".fa-cog")).click();
			driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click(); 
			Thread.sleep(4000);
			driver.findElement(By.id("accept-true")).click();
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
			Thread.sleep(3000);	
			
			if(ViewletData.contains(FinalTopicname))
			{
				System.out.println("Copy as option is working fine");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Copy as working");
			}
			else
			{
				System.out.println("Copy as option is not working");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Copy as failed");
				driver.findElement(By.xpath("Copy As failed")).click();
			}
					
		}
		
		@Parameters({"Dashboardname"})
		@TestRail(testCaseId = 906)
		@Test(priority=6)
		public void DeleteTopicFromCommandsOption(String Dashboardname, ITestContext context) throws InterruptedException
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			//Search with empty queue name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalTopicname);
			Thread.sleep(1000);
					
			//Select Commands option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions DeleteMousehover=new Actions(driver);
			DeleteMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
			driver.findElement(By.linkText("Delete")).click();
			Thread.sleep(1000);
			
			//Delete option
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(8000);
			
			
			try
			{
			if(driver.findElement(By.xpath("//app-mod-errors-display/div/button")).isDisplayed())
			{
				driver.findElement(By.xpath("//app-mod-errors-display/div/button")).click();
				
				//Click on Cancel button
				driver.findElement(By.xpath("//div[3]/button")).click();
			}
			
			else
			{
				//Store the Queue name after deleting the Queue
				String ViewletData=driver.findElement(By.xpath("//datatable-body")).getText();
				System.out.println(ViewletData);
				
				//Edit the search
				for(int k=0; k<=FinalTopicname.length(); k++)
				{
					driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
				}
				Thread.sleep(1000);
				
				if(ViewletData.contains(FinalTopicname))
				{
					System.out.println("Topic is not deleted");
					context.setAttribute("Status", 5);
					context.setAttribute("Comment", "Failed to delete Topic");
					driver.findElement(By.xpath("Topic Delete failed")).click();
				}
				else
				{
					
					System.out.println("Topic is deleted Successfully");
					context.setAttribute("Status", 1);
					context.setAttribute("Comment", "Topic deleted Successfully");
				}
				Thread.sleep(1000);
			}
			}
			catch (Exception e) 
			{
				//Store the Queue name after deleting the Queue
				String ViewletData=driver.findElement(By.xpath("//datatable-body")).getText();
				System.out.println(ViewletData);
				
				//Edit the search
				for(int k=0; k<=FinalTopicname.length(); k++)
				{
					driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
				}
				Thread.sleep(1000);
				
				if(ViewletData.contains(FinalTopicname))
				{
					System.out.println("Topic is not deleted");
					context.setAttribute("Status", 5);
					context.setAttribute("Comment", "Failed to delete Topic");
					driver.findElement(By.xpath("Topic Delete failed")).click();
				}
				else
				{
					
					System.out.println("Topic is deleted Successfully");
					context.setAttribute("Status", 1);
					context.setAttribute("Comment", "Topic deleted Successfully");
				}
				Thread.sleep(1000);
				
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Message deleted"); 
	        }	
		}
		
		@Parameters({"Dashboardname"})
		@TestRail(testCaseId = 910)
		@Test(priority=7)
		public static void Events(String Dashboardname, ITestContext context) throws InterruptedException
		{
			AllEvents obj=new AllEvents();
			obj.Events(Dashboardname, driver, context);
		}
		
		@Parameters({"Dashboardname", "FavoriteViewletName"})
		@TestRail(testCaseId=911)
		@Test(priority=8)
		public static void AddToFavoriteViewlet(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
		{	
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
	    	//Store the Bridge Name into string
			String TopicName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
			
			//Create favorite viewlet
			driver.findElement(By.xpath("//button[3]")).click();
			driver.findElement(By.id("fav")).click();
			driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
			
			//Viewlet Name
			driver.findElement(By.name("viewlet-name")).click();
			driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
			
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
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(6000);
			driver.findElement(By.linkText("Add to favorites...")).click();
			Thread.sleep(MediumSleep);

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
			 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(2000);
			 * driver.findElement(By.
			 * cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue"
			 * )).click(); Thread.sleep(2000);
			 */
			
			//Storing the Favorite Viewlet data
			String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
			
			//Verification of Bridge added to favorite viewlet
			if(Favdata.contains(TopicName))
			{
				System.out.println("Topic name is added to the Favorite viewlet");
				context.setAttribute("Status",1);
	    		context.setAttribute("Comment", "Topic name added successfully to favorite viewlet");
			}
			else
			{
				System.out.println("Topic name is not added to the Favorite viewlet");
				context.setAttribute("Status",5);
	    		context.setAttribute("Comment", "Failed to add Topic name added to favorite viewlet");
				driver.findElement(By.xpath("Favorite condition failed")).click();
			}
			Thread.sleep(1000);
		}
		
		@Parameters({"Dashboardname", "IconTopicName"})
		@TestRail(testCaseId = 912)
		@Test(priority=9)
		public void CreateTopicFromPlusIcon(String Dashboardname, String IconTopicName, ITestContext context) throws InterruptedException
		{	
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			//Check Show Empty Queues check box
			driver.findElement(By.cssSelector(".fa-cog")).click();
			boolean kafka=driver.findElement(By.id("empty-kafka-topics")).isSelected();
			
			if(kafka)
			{
				System.out.println("check box already selected");
			}
			else
			{
				driver.findElement(By.id("empty-kafka-topics")).click();
				Thread.sleep(MediumSleep);
			}
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
			Thread.sleep(2000);	
			
				try
				{
				//Click on + icon
				driver.findElement(By.xpath("//img[@title='Add Kafka Topic']")).click();
				
				//Select WGS
				/*
				 * Select WGS=new Select(driver.findElement(By.xpath("//select")));
				 * WGS.selectByVisibleText(WGSName); Thread.sleep(3000);
				 */
				
				driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/ng-select/div/span")).click();
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
				
				/*
				 * //Select Node
				 * driver.findElement(By.xpath("//ng-select[2]/div/span")).click(); try {
				 * WebElement
				 * ChannelauthNode=driver.findElement(By.className("ng-dropdown-panel")).
				 * findElement(By.className("ng-dropdown-panel-items")); List<WebElement>
				 * divs=ChannelauthNode.findElements(By.tagName("div"));
				 * System.out.println(divs.size()); for (WebElement di : divs) {
				 * if(di.getText().equals(KafkaNodeName)) { di.click(); break; } } }
				 * catch(Exception ex) { ex.printStackTrace(); } Thread.sleep(2000);
				 */
				
				//Select cluster
				driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div[2]/ng-select/div/span")).click();
		        try 
				{
		        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
					List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
					System.out.println(mdivs.size());	
					
					for (WebElement mdi : mdivs)
					{
						if(mdi.getText().equals("Cluster"))
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
				Thread.sleep(4000);
				
				//Create Topic Window
				driver.findElement(By.id("name")).sendKeys(IconTopicName);
				Thread.sleep(3000);
				driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
				Thread.sleep(8000);
						
				try
				{
					driver.findElement(By.id("yes")).click();
				}
				catch (Exception e)
				{
					System.out.println("No Exception occured");
					
				}
				
				//Refresh viewlet
				for(int i=0; i<=5; i++)
				{
					driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
					Thread.sleep(6000);
				}
				
				//Serach with empty queue name
				driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(IconTopicName);
				Thread.sleep(1000);
				
				//Store the first queue name into string
				String Firstqueue=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
				System.out.println(Firstqueue);
				
				//Edit the search
				for(int k=0; k<=IconTopicName.length(); k++)
				{
					driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
				}
				Thread.sleep(1000);
				
				//Restoring the Default Settings
				driver.findElement(By.cssSelector(".fa-cog")).click();
				driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click(); 
				Thread.sleep(8000);
				driver.findElement(By.id("accept-true")).click();
				driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
				Thread.sleep(10000);	
				
				if(Firstqueue.equalsIgnoreCase(IconTopicName))
				{
					System.out.println("Topic is added successfully from icon");
					context.setAttribute("Status",1);
					context.setAttribute("Comment", "Topic is added successfully using add icon");
				}
				else
				{
					System.out.println("Topic is added not added");
					context.setAttribute("Status",5);
					context.setAttribute("Comment", "Faile to add Topic using add icon");
					driver.findElement(By.id("Add Topic failed")).click();
				}
				}
				
				catch (Exception e)
				{
					System.out.println("Exception occured while creating Topic from the Icon");
					context.setAttribute("Status",5);
					context.setAttribute("Comment", "Got exception while adding Topic using add icon, check details: "+ e.getMessage());
					if(driver.findElement(By.cssSelector(".btn-danger")).isDisplayed())
					{
						driver.findElement(By.cssSelector(".btn-danger")).click();
						
					}
					
				}
				Thread.sleep(2000);		
		}
		
		
		@TestRail(testCaseId = 913)
		@Test(priority=10)
		public void Compare(ITestContext context) throws InterruptedException
		{
			CompareObjects comp=new CompareObjects();
			comp.Compare(driver, WGSName, context);
		}
		
		
		@TestRail(testCaseId = 914)
		@Test(priority=11)
		public void Differences(ITestContext context) throws InterruptedException
		{
			DifferenceOfObjects diff=new DifferenceOfObjects();
			diff.Differences(driver, context);
		}
		
		@Parameters({"Dashboardname"})
		@TestRail(testCaseId = 915)
		@Test(priority=12)
		public void MultipleTopicProperties(String Dashboardname, ITestContext context) throws InterruptedException
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			//Select the multiple processes and choose Add to favorite viewlet option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Properties...")).click();
			Thread.sleep(8000);
			
			WebElement ele=driver.findElement(By.id("name"));
			Actions a=new Actions(driver);
			a.moveToElement(ele).perform();
			
			String Tooltipdata=driver.findElement(By.tagName("ngb-tooltip-window")).getText();
			
			System.out.println("Multiple Properties data: " +Tooltipdata);
			
			
			//Click on yes button
	    	driver.findElement(By.cssSelector(".btn-primary")).click();
	    	Thread.sleep(10000);
			
	    	//Clearing selection of object
	    	ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
	    	che1.Deselectcheckbox(Dashboardname, driver);
	    	
			//Open the properties for First process
	    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Properties...")).click();
			Thread.sleep(2000);
			
			//Get the description and application id for First process
			String Firsttopicname=driver.findElement(By.id("name")).getAttribute("value");
			System.out.println("First topic:" +Firsttopicname);
						
			//close the properties page
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			
			//Clearing selection of object
			ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
			che2.Deselectcheckbox(Dashboardname, driver);
			
			//Open the properties for First process
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Properties...")).click();
			Thread.sleep(2000);
					
			//Get the description and application id for First process
			String Secondtopicname=driver.findElement(By.id("name")).getAttribute("value");
			System.out.println("First topic:" +Secondtopicname);
			
			//close the properties page
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			
			//Verification 
			if(Tooltipdata.contains(Firsttopicname) && Tooltipdata.contains(Secondtopicname))
			{
				System.out.println("Properites are Updated for multiple process");
				context.setAttribute("Status",1);
	    		context.setAttribute("Comment", "Updating multiple process names is working fine");
			}
			else
			{
				System.out.println("Properites are not Updated for multiple process");
				context.setAttribute("Status",5);
	    		context.setAttribute("Comment", "Failed to update multiple process names");
				driver.findElement(By.xpath("Properties updation failed")).click();
			}
			Thread.sleep(1000);
			
		}
		
		@Parameters({"Dashboardname", "FavoriteViewletName"})
		@TestRail(testCaseId = 916)
		@Test(priority=13, dependsOnMethods= {"AddToFavoriteViewlet"})
		public static void AddToFavoriteForMultipleTopics(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			//Store Queue names
			String Topic2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
			String Topic3=driver.findElement(By.xpath("//datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
			
			//Select Queue Events option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(4000);
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(4000);
			driver.findElement(By.linkText("Add to favorites...")).click();
			Thread.sleep(2000);
			
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
			 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(2000);
			 * driver.findElement(By.
			 * cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue"
			 * )).click(); Thread.sleep(2000);
			 */
			
		
			//Store favorite viewlet data into string
			String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
			
			//Verification
			if(Favdata.contains(Topic2)  && Favdata.contains(Topic3))
			{
				System.out.println("Multiple Queues are added to Favorite viewlet");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Multiple Queues are added to Favorite viewlet");
			}
			else
			{
				System.out.println("Multiple Queues are not added to Favorite viewlet");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Faile to add Multiple Queues to Favorite viewlet");
				driver.findElement(By.xpath("Multiple queues to fav failed")).click();
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
