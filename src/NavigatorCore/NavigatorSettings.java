package NavigatorCore;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
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
public class NavigatorSettings 
{
	static WebDriver driver;
	
	static String Screenshotpath;
	static String WGSName;
	static String Manager1;
	static String Manager2;
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
		Manager1 =Settings.getManager1();
		Manager2 =Settings.getManager2();
		Node_Hostname =Settings.getNode_Hostname();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
	}
	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "wgs", "Favwgs", "FavoriteViewletName"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname, String wgs, String Favwgs, String FavoriteViewletName) throws Exception
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
		
		//Delete if dashboard exists with same name
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboard(driver, Dashboardname);
		
		//---------- Create New Dashboard ---
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		//driver.findElement(By.id("iv_yes")).click();
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(MediumSleep);	
		
		//---- Create Queue viewlet ----- 
		CreateQueueViewlet();
		
		//---- Create Channel Viewlet ---
		CreateChannelViewlet();
		
		//------------- Create Favorite Viewlet ---------
		
		//Add Favorite Viewlet
		//driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.id("add-viewlet")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.id("fav")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		Thread.sleep(LowSleep);
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		Thread.sleep(LowSleep);
		
		//Deselect WGS 
		driver.findElement(By.xpath("//span[2]/i")).click();
		Thread.sleep(LowSleep);
		
		//select WGS
		//driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div/div[2]/div/ng-select/div/span")).click();
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
		
		//Submit
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(LowSleep);
		
		//Go to edit viewlet
		driver.findElement(By.id("dropdownMenuButton")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Edit viewlet")).click();
		Thread.sleep(LowSleep);
		
		//Update result limit
		driver.findElement(By.xpath("//input[@type='number']")).clear();
		driver.findElement(By.xpath("//input[@type='number']")).sendKeys("1000");
		Thread.sleep(LowSleep);
		
		//Click on Apply changes
		driver.findElement(By.xpath("//button[contains(.,'Apply changes')]")).click();
		Thread.sleep(HighSleep);
		
		//Restoring the Default Settings
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click();
		Thread.sleep(MediumSleep);
		//Click on confirmation yes
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(HighSleep);
		
	}
	
	@Test(priority=1)
	@TestRail(testCaseId=327)
	public static void ShowInActiveChannelsCheckbox(ITestContext context) throws InterruptedException
	{
		try
		{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		
		//Show Inactive channels check box
		WebElement Checkbox=driver.findElement(By.id("inactive-channels"));
		//System.out.println("Status of channels checkbox is: " +Checkbox.isSelected());
		if(Checkbox.isSelected())
		{
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
			System.out.println("Already selected");
			Thread.sleep(MediumSleep);
		}
		else
		{
			Checkbox.click();
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		Thread.sleep(HighSleep);
		
		String NoofChannels=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-footer/div/div/span")).getText();
		String[] Index1=NoofChannels.split(" Visible");
		String[] Index2=Index1[0].split(": ");
		int k=Integer.parseInt(Index2[1]);
		System.out.println("viewlet size is: " +k);
		
		for(int i=2; i<=k; i++)
		{                                              
			String Result=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper["+ i +"]/datatable-body-row/div[2]/datatable-body-cell[7]/div/span")).getText();
			//int integer=Integer.parseInt(Result);  
			System.out.println("Values are: " +Result);
			if(Result.equalsIgnoreCase("Inactive"))
			{
				System.out.println("Inactive Check box is Working fine");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Inactive Check box is Working fine");
				break;
			}
		}
		}
		catch (Exception e)
		{
			System.out.println("Inactive Check box is not Working fine");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Inactive Check box is not Working properly");
			driver.findElement(By.xpath("Checkbox not working")).click();
		}		
		Thread.sleep(1000);
	}
	
	@Test(priority=2)
	@TestRail(testCaseId=328)
	public static void ShowEmptyQueuesCheckbox(ITestContext context) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		
		//Show Empty Queues check box
		WebElement Checkbox=driver.findElement(By.id("empty-queues"));
		if(Checkbox.isSelected())
		{
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		else
		{
			Checkbox.click();
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		Thread.sleep(MediumSleep);
		try
		{
			
		for(int i=2; i<=10; i++)
		{
		String Depth=driver.findElement(By.xpath("//datatable-row-wrapper["+ i +"]/datatable-body-row/div[2]/datatable-body-cell[6]/div/span")).getText();
		int result = Integer.parseInt(Depth);     
		System.out.println("Result values are: "+result);
		if(result==0)
		{
			System.out.println("Show Empty Queues Check box is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show Empty Queues Check box is working fine");
			break;
		}
		else
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show Empty Queues Check box is not working properly");
		}
		
		}
		}
		catch (Exception e)
		{
			System.out.println("Exception occured");
			driver.findElement(By.id("Show Empty queues checkbox failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=3)
	@TestRail(testCaseId=329)
	public static void ShowTemporaryDynamicQueues(ITestContext context) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		
		//Show Show managers for default schema check box
		WebElement SystemCheckbox=driver.findElement(By.id("system-objects"));
		if(SystemCheckbox.isSelected())
		{
			
			//driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		else
		{
			SystemCheckbox.click();
			//driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		Thread.sleep(MediumSleep);
		
		//Show Empty Queues check box
		WebElement Checkbox=driver.findElement(By.id("temporary-dynamic-queues"));
		if(Checkbox.isSelected())
		{
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		else
		{
			Checkbox.click();
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		Thread.sleep(HighSleep);
		
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys("INQ");
		Thread.sleep(LowSleep);
		String INQ=driver.findElement(By.xpath("//datatable-body")).getText();
		
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys("HASH");
		Thread.sleep(LowSleep);
		String HASH=driver.findElement(By.xpath("//datatable-body")).getText();
		
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys("NSQ");
		Thread.sleep(LowSleep);
		String NSQ=driver.findElement(By.xpath("//datatable-body")).getText();
		
		for(int j=0; j<=4; j++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		if(INQ.contains("INQ") || HASH.contains("HASH") || NSQ.contains("NSQ"))
		{
			System.out.println("Show Temporary Dynamic Queues Check box is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show Temporary Dynamic Queues Check box is working fine");
		}
		else
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show Temporary Dynamic Queues Check box is not working properly");
			//driver.findElement(By.id("Show Temporary Dynamic Queues Check box")).click();
		}	
	}
	
	@Parameters({"FavoriteViewletName", "objpath1", "objpath2"})
	@TestRail(testCaseId=330)
	@Test(priority=4)
	public static void ShowFullNamesForFavoriteShortcuts(String FavoriteViewletName, String objpath1, String objpath2, ITestContext context) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		
		//Show Empty Queues check box
		WebElement Checkbox=driver.findElement(By.id("full-names-for-favorites"));
		if(Checkbox.isSelected())
		{
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		else
		{
			Checkbox.click();
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		Thread.sleep(HighSleep);
		
		//Select Add to favorite option in queue
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(LowSleep);
		
			
		//Select favorite viewlet  
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div/span")).click();
		Thread.sleep(MediumSleep); 
		
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
		
		String FullNameForFavorite=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println(FullNameForFavorite);               
		
		if(FullNameForFavorite.contains(objpath1) || FullNameForFavorite.contains(objpath2))
		{
			System.out.println("Show Full name for Favorite Short cuts is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show Full name for Favorite Short cuts is working fine");
		}
		else
		{
			System.out.println("Show Full name for Favorite Short cuts is not working fine");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show Full name for Favorite Short cuts is not working properly");
			driver.findElement(By.xpath("Condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "objpath1", "objpath2"})
	@TestRail(testCaseId=331)
	@Test(priority=5)
	public static void ShowFullNamesForSearchResultsObjects(String Dashboardname, String objpath1, String objpath2, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		
		//Show Empty Queues check box
		WebElement Checkbox=driver.findElement(By.id("full-names-for-search-results"));
		if(Checkbox.isSelected())
		{
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		else
		{
			Checkbox.click();
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		Thread.sleep(HighSleep);
		
		String SearchQueuename=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println(SearchQueuename);
		
		if(SearchQueuename.contains(objpath1) || SearchQueuename.contains(objpath2))
		{
			System.out.println("Show Full name For Search results checkbox is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show Full name For Search results checkbox is working fine");
		}
		else
		{
			System.out.println("Show Full name For Search results checkbox is not working fine");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show Full name For Search results checkbox is not working properly");
			driver.findElement(By.xpath("Condition failed")).click();
		}
		Thread.sleep(1000);
	
	}
	
	@Test(priority=6)
	@TestRail(testCaseId=332)
	public static void ShowSystemObjectsOnly(ITestContext context) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		
		//Show Show managers for default schema check box
		WebElement Checkbox=driver.findElement(By.id("system-objects"));
		if(Checkbox.isSelected())
		{
			Checkbox.click();
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		else
		{
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		Thread.sleep(HighSleep);
		
		String ColumnValues=driver.findElement(By.xpath("//datatable-body")).getText();
	    //System.out.println(ColumnValues);
		
		 if(ColumnValues.contains("SYSTEM."))
		    {
		    	
				System.out.println("Show System Objects only checkbox is not working fine");
		    	context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Show System Objects only checkbox is not working properly");
		    	driver.findElement(By.xpath("Condition failed")).click();
		    	
		    }
		    else
		    {
		    	System.out.println("Show System Objects only checkbox is working fine");
		    	context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Show System Objects only checkbox is working fine");
				
				
		    }
		
		/*for(int i=2; i<=10; i++)
	    {
	    String ColumnValues=driver.findElement(By.xpath("//datatable-row-wrapper["+ i +"]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span")).getText();
	    System.out.println(ColumnValues);
	   // int result = Integer.parseInt(ColumnValues);
	    if(ColumnValues.contains("SYSTEM."))
	    {
	    	
			System.out.println("Show System Objects only checkbox is not working fine");
	    	context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show System Objects only checkbox is not working properly");
	    	driver.findElement(By.xpath("Condition failed")).click();
	    	
	    }
	    else
	    {
	    	System.out.println("Show System Objects only checkbox is working fine");
	    	context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show System Objects only checkbox is working fine");
			
			
	    }
	    }*/
	}
	
	@Parameters({"Manager3", "Manager4"})
	@TestRail(testCaseId=333)
	@Test(priority=7)
	public static void ShowObjectSearchresultsFromActiveManagersOnly(String Manager3, String Manager4, ITestContext context) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.id("inactive-channels")).click();
		Thread.sleep(LowSleep);
		//driver.findElement(By.id("empty-queues")).click();
		driver.findElement(By.id("temporary-dynamic-queues")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.id("full-names-for-favorites")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.id("full-names-for-search-results")).click();
		Thread.sleep(LowSleep);
		
		//Show Show managers for default schema check box
		WebElement Checkbox=driver.findElement(By.id("active-managers-only"));
		if(Checkbox.isSelected())
		{
			Checkbox.click();
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		else
		{
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		Thread.sleep(HighSleep);
		try
		{
			
		boolean verified=false;
		for(int i=2; i<=10; i++)                       
		{
		String Managernames=driver.findElement(By.xpath("//datatable-row-wrapper["+ i +"]/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
		System.out.println("List of Managers: " +Managernames);
		
		if(Managernames.equalsIgnoreCase(Manager1) || Managernames.equalsIgnoreCase(Manager2))
		{
			verified=true;
			break;
		}
		
		}
		if(verified)
		{
			System.out.println("Show object search results for Active queue managers condition working");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show object search results for Active queue managers condition is working fine");
		}
		else
		{
			System.out.println("Show object search results for Active queue managers condition is not working fine");
			
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show object search results for Active queue managers condition not working properly");
			driver.findElement(By.xpath("Condition failed")).click();
		}
		}
		catch (Exception e)
		{
			System.out.println("Exception Occured");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show object search results for Active queue managers condition not working properly" +e.getMessage());
			driver.findElement(By.xpath("Condition failed")).click();
			
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=8)
	@TestRail(testCaseId=334)
	public static void ShowManagersForDefaultSchemas(ITestContext context) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		
		driver.findElement(By.id("empty-queues")).click();
		Thread.sleep(LowSleep);
		
		//Show Show managers for default schema check box
		WebElement Checkbox=driver.findElement(By.id("manager-for-default-schemas"));
		if(Checkbox.isSelected())
		{
			Checkbox.click();
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		else
		{
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		Thread.sleep(HighSleep);
		
		boolean verified=false;
	    for(int i=4; i<=10; i++)
	    {
	    String Columnheading=driver.findElement(By.xpath("//datatable-header-cell["+ i +"]/div")).getText();
	    System.out.println("List of coloums are: " +Columnheading);
	    
	    if(Columnheading.equalsIgnoreCase("Manager Name"))
		{
			verified=true;
			break;
		}
	    }
	    
	    if(verified)
	    {
	    	System.out.println("Show manager for Default schemas checkbox failed");
	    	context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show manager for Default schemas checkbox failed");
	    	driver.findElement(By.xpath("Condition failed")).click();
	    }
	    else
	    {
	    	System.out.println("Show manager for Default schems checkbox function is working fine");
	    	context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show manager for Default schems checkbox function is working fine");
	    }
	    
	    Thread.sleep(1000);   
	} 
	
	@Test(priority=9)
	@TestRail(testCaseId=990)
	public static void ShowLogoutWindow(ITestContext context) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
			
		//Show Logout window check box
		WebElement Checkbox=driver.findElement(By.id("showAgain"));
		if(Checkbox.isSelected())
		{
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		else
		{
			Checkbox.click();
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		Thread.sleep(HighSleep);
		
		//click on logout
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		
		//Store the popup page title into string
		String LogoutTitle=driver.findElement(By.cssSelector(".modal-title")).getText();
		System.out.println("Logout popup title is: " +LogoutTitle);
		
		if(LogoutTitle.contains("Log Out"))
		{
			System.out.println("Show logout window checkbox is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show Logout window function is working fine");
		}
		else
		{
			System.out.println("Show logout window checkbox is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show Logout window function is not working");
			driver.findElement(By.id("cancelButton")).click();
			driver.findElement(By.id("Show logout window failed")).click();
		}
		
		//Close the popup page
		driver.findElement(By.id("cancelButton")).click();
		Thread.sleep(3000);
		
	}
	
	@TestRail(testCaseId=1169)
	@Parameters({"Dashboardname"})
	@Test(priority=10)
	public void CollapseAllViewletOnLogin(String Dashboardname, ITestContext context) throws Exception
	{
		Settings.read();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		//click on settings icon
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
			
		//Show Logout window check box
		WebElement Collapseviewlet=driver.findElement(By.id("collapse-all-viewlets"));
		if(Collapseviewlet.isSelected())
		{
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		else
		{
			Collapseviewlet.click();
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		Thread.sleep(HighSleep);
		
		//Logout option
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		Thread.sleep(4000);
		driver.findElement(By.id("yesButton")).click();
		
		//Login again
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(HighSleep);
		
		//Go to that dashboard
		WebElement cla=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis=cla.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis.size());
		
		for(WebElement li: lis)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi=li.findElement(By.className("g-tab-title"));
			System.out.println("Names are: " +fi.getText());
			
			if(fi.getText().equalsIgnoreCase(Dashboardname))
			{
				 WebElement element=fi;
				 JavascriptExecutor js = (JavascriptExecutor)driver;
				 js.executeScript("arguments[0].click();", element);
				break;
			}
		}
		Thread.sleep(MediumSleep);
		
		String title=driver.findElement(By.xpath("//app-viewlet/div/div[2]/div/div/div/div/i")).getAttribute("title");
		System.out.println("title is: " +title);
	
		if(title.contains("Expand"))
		{
			System.out.println("Collapse all viewlets on login checkbox is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Collapse all viewlets on login function is working fine");
		}
		else
		{
			System.out.println("Collapse all viewlets on login checkbox is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Collapse all viewlets on login function is not working");
			driver.findElement(By.id("Collapse all viewlets on login failed")).click();
		}
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=16)
	public void Logout(String Dashboardname) throws InterruptedException 
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//Restoring the Default Settings
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click();
		Thread.sleep(MediumSleep);
		//Click on confirmation yes
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(HighSleep);
				
		//Delete the dashboard 
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, Dashboardname);
	}
	
	public static void CreateQueueViewlet() throws InterruptedException
	{
		//Click on Viewlet button
		driver.findElement(By.xpath("//button[3]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click();
		Thread.sleep(LowSleep);
		
		//Select IBM MQ product
		//Select IBM=new Select(driver.findElement(By.name("productType")));
		//IBM.selectByVisibleText("IBM MQ");
		//Thread.sleep(4000); 
		
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
		driver.findElement(By.cssSelector(".object-type:nth-child(3)")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys("Local Queue");
		
		//Select WGS type
		//Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		//WGSSelection.selectByVisibleText(WGSName);
		
		driver.findElement(By.xpath("//app-mod-select-input-connection-list/ng-select/div/span")).click();
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
		Thread.sleep(LowSleep);
		
		//Select node value  
		driver.findElement(By.xpath("//ng-select/div/span[2]")).click();
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
				
		//driver.findElement(By.cssSelector(".btn-primary")).click();
		driver.findElement(By.id("save-viewlet")).click();
		Thread.sleep(HighSleep);
		
	}
	
	public static void CreateChannelViewlet() throws InterruptedException
	{
		//Click on Viewlet button
		driver.findElement(By.xpath("//button[3]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click();
		Thread.sleep(LowSleep);
		
		//Select IBM MQ product
		//Select IBM=new Select(driver.findElement(By.name("productType")));
		//IBM.selectByVisibleText("IBM MQ");
		//Thread.sleep(4000); 
		
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
		driver.findElement(By.cssSelector(".object-type:nth-child(4)")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys("Channel");
		
		//Select WGS type
		//Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		//WGSSelection.selectByVisibleText(WGSName);
		
		driver.findElement(By.xpath("//app-mod-select-input-connection-list/ng-select/div/span")).click();
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
		Thread.sleep(4000);
		
		//Select node value
		driver.findElement(By.xpath("//ng-select/div/span[2]")).click();
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
				
		//driver.findElement(By.cssSelector(".btn-primary")).click();
		driver.findElement(By.id("save-viewlet")).click();
		Thread.sleep(HighSleep);
		
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
