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
import Common.LogoutForAll;
import Common.Viewlets;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class ChannelAuthRecordsViewlet 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName;
	static String Dnode;
	static String Manager2;
	static String Node_Hostname;
	static String Low;
	static String Medium;
	static String High;
	static int LowSleep;
	static int MediumSleep;
	static int HighSleep;
	static boolean Objectnamefiled;
	static boolean Objectname;
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		WGSName =Settings.getWGSNAME();
		Dnode =Settings.getDnode();
		Manager2 =Settings.getManager2();
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
	
	 @TestRail(testCaseId=888)
	@Parameters({"ViewletName", "ViewletValue"})
	@Test(priority=1)
	public static void AddChannelAuthrecordsViewlet(String ViewletName, int ViewletValue, ITestContext context) throws InterruptedException
	{
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		
		if(driver.getPageSource().contains(ViewletName))
		{
			System.out.println("Channel Auth Viewlet is created");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Channel Auth record Viewlet created successfully");
		}
		else
		{
			System.out.println("Channel auth viewlet is not created");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "failed to add channel auth record Viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
		Thread.sleep(2000);
			
    }
	 
	 @TestRail(testCaseId=532)
	 @Parameters({"Dashboardname", "ChannelAuthNameFromOptions", "UserList", "ChannelAuthNameOptions"})
	 @Test(priority=2)
		public void CreateChannelAuthRecordFromOptions(String Dashboardname, String ChannelAuthNameFromOptions, String UserList, String ChannelAuthNameOptions, ITestContext context) throws InterruptedException
		{
		    //Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			try
			{
			//Select create process option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Create ChAuthRec")).click();
			Thread.sleep(MediumSleep);
			
			
			//Give the process name
			try
			{
			driver.findElement(By.id("name")).clear();
		    driver.findElement(By.id("name")).sendKeys(ChannelAuthNameFromOptions);
			Thread.sleep(LowSleep);
			}
			catch(Exception E1)
			{
				System.out.println("dropdown selection executed");
				driver.findElement(By.xpath("//app-ng-select-input/div/div/ng-select/div/span")).click();
				Thread.sleep(LowSleep);
				
				WebElement a=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
				List<WebElement>b=a.findElements(By.tagName("div"));
				System.out.println("number of divs are"+b.size());
				
				for(WebElement c:b)
				{
					//System.out.println("Channel names: " +c.getText());
					if(c.getText().equals(ChannelAuthNameOptions))
					{
						c.click();
						Thread.sleep(6000);
						break;
					}
				}
			}
			
			//Go to Block tab
			driver.findElement(By.linkText("Block")).click();
			Thread.sleep(LowSleep);
			
			//Give the User list name
			driver.findElement(By.id("userIdList")).sendKeys(UserList);	
			Thread.sleep(LowSleep);
			
			//Click on Submit the ChannelAuth
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(HighSleep);
						
			try
			{
				driver.findElement(By.id("yes")).click();
				driver.findElement(By.cssSelector(".btn-primary")).click();
				try
				{
					driver.findElement(By.id("yes")).click();
					driver.findElement(By.cssSelector(".btn-danger")).click();
				}
				catch (Exception e)
				{
					System.out.println("OK button is not working");
				}
			}
			catch (Exception e)
			{
				System.out.println("Error popup is not present");
			}
			
			//Refresh viewlet      
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(LowSleep);
			
			//Store the process viewlet data into string
			String ChannelAuthdata=driver.findElement(By.xpath("//datatable-body")).getText();
			//System.out.println(ChannelAuthdata);
			 
			//Verification
			if(ChannelAuthdata.contains(ChannelAuthNameFromOptions) || ChannelAuthdata.contains(ChannelAuthNameOptions))
			{
				System.out.println("Channel auth record is created from the options");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "Channel auth record is created");
			}
			else
			{
				System.out.println("Channel auth record is not created from the options");
				//driver.findElement(By.cssSelector(".btn-danger")).click();
				driver.findElement(By.xpath("Channel auth record creation failed")).click();
			}
			}
			
			catch (Exception e)
			{
				//Click on Close button
				System.out.println("Unable to create the Channel auth record from options");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Channel auth record is added");
				driver.findElement(By.xpath("Channel auth record creation failed")).click();
				
			}
		}
	 
	 
	    @Parameters({"Dashboardname", "ChannelAuthNameFromOptions", "ChannelAuthNameOptions"})
	    @TestRail(testCaseId=533)
		@Test(priority=3)
		public void DeleteChannelAuthRecordFromCommands(String Dashboardname, String ChannelAuthNameFromOptions, String ChannelAuthNameOptions, ITestContext context) throws InterruptedException
		{
	    	//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname,driver);
			
			try
			{
	    	//Search with added channelAuth record
	    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
	    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ChannelAuthNameFromOptions);
	    	Thread.sleep(LowSleep);
	    	
			//Select Delete From commands
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
	    	Actions Mousehoverdelete=new Actions(driver);
	    	Mousehoverdelete.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
	    	driver.findElement(By.linkText("Delete")).click();
	    	Thread.sleep(LowSleep);
			
	    	//Click on Yes
	    	driver.findElement(By.cssSelector(".btn-primary")).click();
	    	Thread.sleep(MediumSleep);
	    	
	    	//Search with the new name
			for(int j=0; j<=ChannelAuthNameFromOptions.length(); j++)
	    	{
				driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
	    	}
	    	Thread.sleep(LowSleep);
			}
			catch(Exception ee)
			{
				//Search with added channelAuth record
		    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ChannelAuthNameOptions);
		    	Thread.sleep(LowSleep);
		    	
				//Select Delete From commands
				driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		    	Actions Mousehoverdelete=new Actions(driver);
		    	Mousehoverdelete.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		    	driver.findElement(By.linkText("Delete")).click();
		    	Thread.sleep(LowSleep);
				
		    	//Click on Yes
		    	driver.findElement(By.cssSelector(".btn-primary")).click();
		    	Thread.sleep(MediumSleep);
		    	
		    	//Search with the new name
				for(int j=0; j<=ChannelAuthNameFromOptions.length(); j++)
		    	{
					driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		    	}
				
			}
	    	
	    	//Store the viewlet data into string
	    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
	    	//System.out.println(Subviewlet);
	    	
	    	//Verification of Subscription delete
	    	if(Subviewlet.contains(ChannelAuthNameFromOptions) || Subviewlet.contains(ChannelAuthNameOptions))
	    	{
	    		System.out.println("Channel Auth record is not deleted");
	    		context.setAttribute("Status",5);
				context.setAttribute("Comment", "Channel auth record is not deleted");
	    		driver.findElement(By.xpath("Channel auth record delete failed")).click();
	    	}
	    	else
	    	{
	    		System.out.println("Channel Auth record is deleted");
	    		context.setAttribute("Status",1);
				context.setAttribute("Comment", "Channel auth record is deleted");
	    	}
	    	Thread.sleep(1000);
		}
	    
	    @Parameters({"Dashboardname"})
	    @TestRail(testCaseId = 1011)
	    @Test(priority=4)
		public void MQSCSnapshot(String Dashboardname, ITestContext context) throws InterruptedException
		{
	    	//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname,driver);
			
			//Save the Auth info name into string
			String ChannelAuthname=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
			System.out.println("Object name is: " +ChannelAuthname);
			
			//Select MQSCSnapshot option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
	    	Actions Mousehovercopy=new Actions(driver);
	    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("MQSC"))).perform();
	    	driver.findElement(By.linkText("Snapshot...")).click();
	    	Thread.sleep(LowSleep);
	    	
	    	//Get the snapshot data and store into string
	    	String SnapshotData=driver.findElement(By.xpath("//textarea")).getText();
	    	System.out.println("Snapshot data is: " +SnapshotData);
	    	
	    	//verification
	    	if(SnapshotData.contains(ChannelAuthname))
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
				driver.findElement(By.id("channel Auth snapshot failed")).click();
	    	}
		}
		
	    @TestRail(testCaseId = 1012)
		@Test(priority=5, dependsOnMethods= {"MQSCSnapshot"})
		public void SaveChannelAuthSnapshot(ITestContext context)
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
	    @TestRail(testCaseId=534)
	    @Test(priority=6)
		public void ChannelAuthProperties(String Dashboardname, ITestContext context) throws InterruptedException
		{
	    	//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname,driver);
			
	    	try
	    	{
	    		//Close snapshot popup window
	    		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
	    	}
	    	catch (Exception e)
	    	{
	    		
	    	}
	    	
			//Select Properties option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Properties...")).click();
			Thread.sleep(MediumSleep);
			
			try
			{
			//Store the Name field status into boolean
			Objectnamefiled=driver.findElement(By.id("name")).isEnabled();
			System.out.println(Objectnamefiled);
			
			}
			catch(Exception ee)
			{
				Objectname=driver.findElement(By.xpath("//app-ng-select-input/div/div/ng-select/div")).isEnabled();
				System.out.println("Dropdown field: " +Objectname);
			}
			
			//Verification Condition
			if(Objectnamefiled == false || Objectname ==false)
			{
				System.out.println("The Channel Auth name is Disabled");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "Channel auth record properties working fine");
				driver.findElement(By.cssSelector(".btn-primary")).click();
				
			}
			else
			{
				System.out.println("The Channel Auth name is Enable");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Channel auth record properties not working");
				driver.findElement(By.cssSelector(".btn-primary")).click();
				driver.findElement(By.xpath("Channel auth name field is Enable")).click();
			}
			
			Thread.sleep(4000);
		}
		
	    @Parameters({"Dashboardname"})
	    @TestRail(testCaseId=535)
		@Test(priority=7)
		public static void ChannelAuthEvents(String Dashboardname, ITestContext context) throws InterruptedException
		{
	    	AllEvents obj=new AllEvents();
			obj.Events(Dashboardname, driver, context);
		}
		
	    @TestRail(testCaseId=536)
		@Parameters({"Dashboardname", "FavoriteViewletName"})
		@Test(priority=8)
		public static void AddToFavoriteViewlet(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
		{	
	    	//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname,driver);
			
			//Store process name into String 
			String ChannelAuthName=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
			
			//Create favorite viewlet
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
			driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div/div[2]/div/ng-select/div/span")).click();
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
			Thread.sleep(LowSleep);
			
			
			//Select Add to Favorites option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(MediumSleep);
			driver.findElement(By.linkText("Add to favorites...")).click();
			Thread.sleep(LowSleep);
			
			//Select the favorite viewlet name
			driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div")).click();
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
			Thread.sleep(HighSleep);
			
			/*
			 * Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
			 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(1000);
			 * driver.findElement(By.
			 * cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue"
			 * )).click(); Thread.sleep(1000);
			 */
			
			//Storing the Favorite Viewlet data
			String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
			
			//Verification of process added to the favorite viewlet
			if(Favdata.contains(ChannelAuthName))
			{
				System.out.println("Channel Auth record name is added to the Favorite viewlet");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "Channel auth record is added to favorite viewlet");
			}
			else
			{
				System.out.println("Channel Auth record name is not added to the Favorite viewlet");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Channel auth record is not added to favorite viewlet");
				driver.findElement(By.xpath("Favorite condition failed")).click();
			}
			Thread.sleep(2000);
		}
		
	    @TestRail(testCaseId=537)
		@Parameters({"Dashboardname", "ChannelAuthNameFromIcon", "UserList", "ChannelAuthNameIcon"})
		@Test(priority=9)
		public void CreateChannelAuthRecordFromPlusIcon(String Dashboardname, String ChannelAuthNameFromIcon, String UserList, String ChannelAuthNameIcon, ITestContext context) throws InterruptedException
		{
	    	//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname,driver);
			
			//Click on + icon present in the viewlet
			driver.findElement(By.xpath("//img[@title='Add ChAuthRec']")).click();
			Thread.sleep(LowSleep);
			
			//Select WGS
			/*
			 * Select WGS=new Select(driver.findElement(By.xpath("//select")));
			 * WGS.selectByVisibleText(WGSName); Thread.sleep(3000);
			 */
			
			driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/ng-select/div/span")).click();
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
			
			/*
			 * //Select Node
			 * driver.findElement(By.xpath("//ng-select[2]/div/span")).click(); try {
			 * WebElement
			 * ChannelauthNode=driver.findElement(By.className("ng-dropdown-panel")).
			 * findElement(By.className("ng-dropdown-panel-items")); List<WebElement>
			 * divs=ChannelauthNode.findElements(By.tagName("div"));
			 * System.out.println(divs.size()); for (WebElement di : divs) {
			 * if(di.getText().equals(Dnode)) { di.click(); break; } } } catch(Exception ex)
			 * { ex.printStackTrace(); } Thread.sleep(2000);
			 */
			
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
					if(mdi.getText().equals(Manager2))
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
			
			//Give the name of the Channel auth name
			try
			{
			driver.findElement(By.id("name")).clear();
		    driver.findElement(By.id("name")).sendKeys(ChannelAuthNameFromIcon);
			Thread.sleep(LowSleep);
			}
			catch(Exception E21)
			{
				System.out.println("dropdown selection executed");
				driver.findElement(By.xpath("//app-ng-select-input/div/div/ng-select/div/span")).click();
				Thread.sleep(LowSleep);
				
				WebElement a=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
				List<WebElement>b=a.findElements(By.tagName("div"));
				System.out.println("number of divs are"+b.size());
				
				for(WebElement c:b)
				{
					//System.out.println("Channel names are: " +c.getText());
					if(c.getText().equals(ChannelAuthNameIcon))
					{
						c.click();
						Thread.sleep(6000);
						break;
					}
				}
			}
			
			
			//Go to Block tab
			driver.findElement(By.linkText("Block")).click();
			Thread.sleep(LowSleep);
			
			//Give the User list name
			driver.findElement(By.id("userIdList")).sendKeys(UserList);	
			Thread.sleep(LowSleep);
			
			//Click on Submit the ChannelAuth
			driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
			Thread.sleep(HighSleep);
						
			try
			{
				driver.findElement(By.id("yes")).click();
				driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
				Thread.sleep(LowSleep);
				
				try
				{
					driver.findElement(By.id("yes")).click();
					driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
					Thread.sleep(LowSleep);
				}
				catch (Exception e)
				{
					System.out.println("OK button is not working");
				}
			}
			catch (Exception e)
			{
				System.out.println("Error popup is not present");
			}
			Thread.sleep(1000);
			
			//Edit the search field data
			//driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
	    	//driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ChannelAuthNameFromIcon);
	    	//Thread.sleep(LowSleep);
	    	
			
			//Store the Topic viewlet data into string
			String Viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
			//System.out.println(Viewletdata);
			
			//Edit the search field data
	    	//for(int j=0; j<=ChannelAuthNameFromIcon.length(); j++)
	    	//{
	    	
	    	//driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
	    	//}
	    	Thread.sleep(LowSleep);
			
			//Verification condition
			if(Viewletdata.contains(ChannelAuthNameFromIcon) || Viewletdata.contains(ChannelAuthNameIcon))
			{
				System.out.println("Channel auth record is created successfully from ICon");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "Channel auth record is added created using add icon");
			}
			else
			{
				System.out.println("Channel auth record  is not created");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed to create Channel auth record using add icon");
				driver.findElement(By.xpath("Channel auth record Failed From Icon")).click();
			}
			Thread.sleep(1000);		
		}
	    
	    
	    @Test(priority=10)
		@TestRail(testCaseId = 949)
		public static void CompareChannelAuths(ITestContext context) throws InterruptedException
		{
			CompareObjects com=new CompareObjects();
			com.Compare(driver, WGSName, context);
		}
		
	   
		@TestRail(testCaseId = 950)
		@Test(priority=11)
		public void CheckDifferencesForChannelAuths(ITestContext context) throws InterruptedException
		{
			DifferenceOfObjects diff=new DifferenceOfObjects();
			diff.Differences(driver, context);
		}
		
	    @TestRail(testCaseId=538)
		@Parameters({"Dashboardname", "DeletedChannelAuthRecords"})
		@Test(priority=12)
		public void DeleteMultipleChannelAuthRecordsFromCommands(String Dashboardname, String DeletedChannelAuthRecords, ITestContext context) throws InterruptedException
		{
	    	//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname,driver);
			
			//Send the New name into field
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
	    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(DeletedChannelAuthRecords);
	    	Thread.sleep(LowSleep);
	    	
	    	//Select the multiple processes and choose Add to favorite viewlet option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions Mousehoverdelete=new Actions(driver);
	    	Mousehoverdelete.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
	    	driver.findElement(By.linkText("Delete")).click();
			Thread.sleep(LowSleep);
			
	    	//Click on Yes
	    	driver.findElement(By.cssSelector(".btn-primary")).click();
	    	Thread.sleep(MediumSleep);
	    	
	    	//Refresh the viewlet
	    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
	    	Thread.sleep(MediumSleep);
	    	
	    	//Store the viewlet data into string
	    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
	    	//System.out.println(Subviewlet);
	    	
	    	for(int i=0; i<=DeletedChannelAuthRecords.length(); i++)
	    	{
	    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
	    	}
	    	
	    	//Verification of Subscription delete
	    	if(Subviewlet.contains(DeletedChannelAuthRecords))
	    	{
	    		System.out.println("Multiple channel auth records are not deleted");
	    		context.setAttribute("Status",5);
				context.setAttribute("Comment", "multiple Channel auth records are not deleted");
	    		driver.findElement(By.xpath("Channel auth records delete failed")).click();
	    	}
	    	else
	    	{
	    		System.out.println("Multiple channel auth records are deleted");
	    		context.setAttribute("Status",1);
				context.setAttribute("Comment", "Channel auth records are deleted");
	    	}
	    	Thread.sleep(1000);	    	
		}
		
	    @TestRail(testCaseId=539)
		@Parameters({"Dashboardname", "MultipleDescription"})
		@Test(priority=13)
		public void MultipleChannelAuthRecordsProperties(String Dashboardname, String MultipleDescription, ITestContext context) throws InterruptedException
		{
	    	//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname,driver);
			
			//Select the multiple processes and choose Add to favorite viewlet option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Properties...")).click();
			Thread.sleep(LowSleep);
			
			//Give the description for multiple channel auth records
			driver.findElement(By.id("chAuthDescription")).clear();
			driver.findElement(By.id("chAuthDescription")).sendKeys(MultipleDescription);
			Thread.sleep(LowSleep);
						
			//click on OK
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(MediumSleep);
			
			//Clearing selection of object
			ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
			che1.Deselectcheckbox(Dashboardname,driver);
			
			//Open the properties for First process
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.linkText("Properties...")).click();
			Thread.sleep(LowSleep);
			
			//Get the description and application id for First process
			String FirstDescription=driver.findElement(By.id("chAuthDescription")).getAttribute("value");
						
			//close the properties page
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(MediumSleep);
			
			//Clearing selection of object
			ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
			che2.Deselectcheckbox(Dashboardname,driver);
			
			//Open the properties for First process
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.linkText("Properties...")).click();
			Thread.sleep(LowSleep);
					
			//Get the description and application id for First process
			String SecondDescription=driver.findElement(By.id("chAuthDescription")).getAttribute("value");
						
			//close the properties page
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(MediumSleep);
			
			//Verification 
			if(FirstDescription.equals(MultipleDescription) && SecondDescription.equals(MultipleDescription))
			{
				System.out.println("Properites are Updated for multiple Channel auth records");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "multiple Channel auth records are updated");
			}
			else
			{
				System.out.println("Properites are not Updated for multiple channel auth records");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "multiple Channel auth records are not updated");
				driver.findElement(By.xpath("Properties updation failed")).click();
			}
			Thread.sleep(1000);
		}
		
	    @TestRail(testCaseId=540)
		@Parameters({"Dashboardname", "FavoriteViewletName"})
		@Test(priority=14)
		public static void AddToFavoriteForMultipleChannelAuthRecords(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
		{			
	    	//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname,driver);
			
			//Store the process Names into strings
			String ChannelAuthRecordName2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
			System.out.println(ChannelAuthRecordName2);                             
			String ChannelAuthRecordName3=driver.findElement(By.xpath("//datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
			System.out.println(ChannelAuthRecordName3);                
			
			//Select the multiple processes and choose Add to favorite viewlet option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Add to favorites...")).click();
			Thread.sleep(LowSleep);
			
			//Select the favorite viewlet name
			driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div")).click();
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
			
			/*
			 * Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
			 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(1000);
			 * driver.findElement(By.
			 * cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue"
			 * )).click(); Thread.sleep(6000);
			 */
			
			//Storing the Favorite Viewlet data
			String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
			
			//Verify the multiple processes added to favorite viewlet
			if(Favdata.contains(ChannelAuthRecordName2) && Favdata.contains(ChannelAuthRecordName3))
			{
				System.out.println("Multiple Channel auth names are added to the Favorite viewlet");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "Multiple Channel auth records are added to favorite viewlet");
			}
			else
			{
				System.out.println("Multiple Channel auth names are not added to the Favorite viewlet");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Multiple Channel auth records are not added to favorite viewlet");
				driver.findElement(By.xpath("Favorite condition failed")).click();
			}
			Thread.sleep(1000);	
			
		}
	    
	 @TestRail(testCaseId=960)
	 @Parameters({"Dashboardname", "SchemaName"})
	 @Test(priority=15)
	 public void ShowObjectAttributes(String Dashboardname, String SchemaName, ITestContext context)
	 {
		 try {
				ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
				obj.ChannelAuthRecordAttributes(Dashboardname, driver, SchemaName, WGSName);
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Show object attributes working fine");
				
				}catch(Exception e)
				{
					context.setAttribute("Status", 5);
					context.setAttribute("Comment", "Got exception while showing object attributes, check details: "+  e.getMessage());
				}
	 }
	 
	 @Parameters({"Dashboardname"})
	 @Test(priority=16)
	 public void Logout(String Dashboardname) throws InterruptedException 
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
