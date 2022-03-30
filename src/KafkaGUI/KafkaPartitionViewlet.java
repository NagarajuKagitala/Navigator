package KafkaGUI;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
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
public class KafkaPartitionViewlet 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName;
	static String FinalTopicname="";
	static String UploadFilepath;
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
		UploadFilepath =Settings.getUploadFilepath();
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
	
	@Parameters({"Dashboardname"})
	@Test(priority=1)
	@TestRail(testCaseId = 929)
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
	
	@Parameters({"Dashboardname", "SchemaName"})
	@TestRail(testCaseId = 928)
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
	@TestRail(testCaseId = 930)
	@Test(priority=2)
	public static void Events(String Dashboardname, ITestContext context) throws InterruptedException
	{
		AllEvents obj=new AllEvents();
		obj.Events(Dashboardname, driver, context);
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=931)
	@Test(priority=3)
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
		Thread.sleep(2000);
		
		//Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(8000);
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
			System.out.println("Partition name is added to the Favorite viewlet");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Partition name added successfully to favorite viewlet");
		}
		else
		{
			System.out.println("Partition name is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to add Partition name added to favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "MessageData"})
	@TestRail(testCaseId = 932)
	@Test(priority=4)
	public void PutmessageintoPartition(String Dashboardname, String MessageData, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Find the topic current depth
		WebElement Depth=driver.findElement(By.xpath("//datatable-body-cell[10]/div/span"));
		String depthbefore=Depth.getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select put new message Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Put New Message")).click();
		Thread.sleep(4000);
				
		//Message data
		//driver.findElement(By.id("encoding-text-9")).click();
		driver.findElement(By.xpath("//textarea")).sendKeys(MessageData);
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		Thread.sleep(8000);
		
		//refresh viewlet
		for(int i=0; i<=1; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
				
		//verification of message
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell[10]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		int Final=result1-1;
		System.out.println(Final);
				
		//Message increment condition
		if(Final==result)
		{
			System.out.println("The new message was successfully added into the Queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Successfully added new message to queue");
		}
		else
		{
			System.out.println("The new message was not added into the Queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add new message to queue");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 933)
	@Test(priority=5)
	public void LoadmessagetoPartitionFromFile(String Dashboardname, ITestContext context) throws InterruptedException, AWTException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Find the topic current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell[10]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select Load from file Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Load From File...")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(4000);
				
		//Loading a file from the load file option
		String filepath=System.getProperty("user.dir") + "\\" + UploadFilepath;
		StringSelection stringSelection = new StringSelection(filepath);
		//StringSelection stringSelection = new StringSelection(UploadFilepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(9000);
	    
	    //refresh viewlet
		for(int i=0; i<=1; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
	        
	    //verification of message
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell[10]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		System.out.println(result1);
				
		//Message increment condition
		if(result1==result)
		{
			System.out.println("The file is not uploaded successfully from icon");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to upload messages");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
		else
		{
			System.out.println("The file is uploaded successfully from icon");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "File uploaded successfully using icon");
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 934)
	@Test(priority=6)
	public void PartitionExportMessages(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		try {
			//Export All Messages As MMF
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions MMFMousehour=new Actions(driver);
			MMFMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
			MMFMousehour.moveToElement(driver.findElement(By.linkText("Export All Messages..."))).perform();
			driver.findElement(By.linkText("As .MMF")).click();
			Thread.sleep(2000);                                       
			
			//Click on Yes button
			driver.findElement(By.cssSelector(".btn-group > .ng-star-inserted")).click();
			Thread.sleep(8000);
			
			//Clearing selection of object
			ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
			che1.Deselectcheckbox(Dashboardname, driver);
			
			//Export All Messages as TXT
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions TXTMousehour=new Actions(driver);
			TXTMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
			TXTMousehour.moveToElement(driver.findElement(By.linkText("Export All Messages..."))).perform();
			driver.findElement(By.linkText("As .TXT")).click();
			Thread.sleep(1000);
			
			//Click on Yes button
			driver.findElement(By.cssSelector(".btn-group > .ng-star-inserted")).click();
			Thread.sleep(8000);
			
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages exported successfully");
			}catch(Exception e)
			{
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Got exception while exporting messages, check details: "+ e.getMessage());
				driver.findElement(By.id("Export failed")).click();
			}
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 935)
	@Test(priority=7)
	public void PartitionCopyAllMessages(String Dashboardname, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Second topic name
		String SecondTopicName=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(SecondTopicName);
				
		//Store the Target topic name into string
		String SourcetopicDepth=driver.findElement(By.xpath("//datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[10]/div/span")).getText();
		int SourceDepth=Integer.parseInt(SourcetopicDepth);
		System.out.println("Source depth is: " +SourceDepth);
		
		//Store the Target topic name into string
		String TargettopicDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[10]/div/span")).getText();
		int TargetCopy=Integer.parseInt(TargettopicDepth);
		System.out.println("Target depth is: " +TargetCopy);
		
		//Copy All Messages
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions CopyAllMessagesMousehour=new Actions(driver);
		CopyAllMessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Copy All")).click();
		Thread.sleep(2000);
		
		//Search with target queue name 
		driver.findElement(By.xpath("//div[2]/div/div/div/input")).sendKeys(SecondTopicName);
		Thread.sleep(MediumSleep);
		driver.findElement(By.xpath("//td/div/span/input")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		//refresh viewlet
		for(int i=0; i<=1; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
						
		//Getting the Second topic depth after copying the message
		String FinalDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[10]/div/span")).getText();
		int FinalResultAfterCopy=Integer.parseInt(FinalDepth);
		System.out.println(FinalResultAfterCopy);
		
		int Final=TargetCopy+SourceDepth;
		System.out.println("Source and destination are :" +Final);
		
		//Verification
		if(Final==FinalResultAfterCopy)
		{
			System.out.println("Message is copied to destination topic");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages copied to destination topic successfully");
		
		}
		else
		{
			System.out.println("Message is not copied to destination topic");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to copy messages");
			driver.findElement(By.xpath("Copy message is Failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 936)
	@Test(priority=8)
	public void ClearAllMessagesFromPartition(String Dashboardname, ITestContext context) throws Exception
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Clear All option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions DeleteAllMessagesMousehour=new Actions(driver);
		DeleteAllMessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Clear All")).click();	
		
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(8000);
			
		for(int i=1; i<=3; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
		//Store the depth value into string after deleting the message from the topic
		WebElement DepthafterDelete=driver.findElement(By.xpath("//datatable-body-cell[10]/div/span"));
		String depthafter=DepthafterDelete.getText();
		
		int result1 = Integer.parseInt(depthafter);
		System.out.println("Output is: " +result1);
				
		//Message decrement condition
		if(result1==0)
		{
			System.out.println("The new message was successfully deleted from the topic");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages deleted from topic successfully");
		}
		else
		{
			System.out.println("The new message was not deleted from the topic");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to delete messages");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
	}
	
	
	@TestRail(testCaseId = 937)
	@Test(priority=9)
	public void ComparePartitions(ITestContext context) throws InterruptedException
	{
		CompareObjects com=new CompareObjects();
		com.Compare(driver, WGSName, context);
	}
	
	
	@TestRail(testCaseId = 938)
	@Test(priority=10)
	public void DifferencesOfPartitions(ITestContext context) throws InterruptedException
	{
		DifferenceOfObjects diff=new DifferenceOfObjects();
		diff.Differences(driver, context);
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=939)
	@Test(priority=11, dependsOnMethods= {"AddToFavoriteViewlet"})
	public static void AddToFavoriteForMultiplePartitions(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Store the Bridge names into string
		String BridgeName2=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		String BridgeName3=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
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
		 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(1000);
		 * driver.findElement(By.
		 * cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue"
		 * )).click(); Thread.sleep(1000);
		 */
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification of Bridges added into favorite viewlet
		if(Favdata.contains(BridgeName2) && Favdata.contains(BridgeName3))
		{
			System.out.println("Multiple Partition are added to the Favorite viewlet");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Multiple Partition are added successfully to favorite viewlet");
		}
		else
		{
			System.out.println("Multiple Partition are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to add Multiple Partition to favorite viewlet");
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
