package EMS;

import java.io.File;
import java.util.List;
import java.util.StringJoiner;
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

import ApodGUI.ObjectsVerificationForAllViewlets;
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
public class BridgeViewlet {
	
	String FinalBridgeName="";
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName;
	static String EMS_WGS_INDEX;
	static String EMS_WGSNAME;
	static String SourceName;
	static String TargetName;
	static String DeleteBridgeName;
	static String EMSNode;
	static String EMSQueueManager;
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
		EMS_WGS_INDEX =Settings.getEMS_WGS_INDEX();
		EMS_WGSNAME =Settings.getEMS_WGSNAME();
		SourceName =Settings.getSourceName();
		TargetName =Settings.getTargetName();
		DeleteBridgeName =Settings.getDeleteBridgeName();
		EMSNode =Settings.getEMSNode();
		EMSQueueManager =Settings.getEMSQueueManager();
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
		Thread.sleep(8000);
				
		//Delete existing dashboard
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboard(driver, Dashboardname);
		
		//Click on Create button
		//driver.findElement(By.xpath("//app-side-dashboard-menu/div/div/div[2]/div[2]")).click();				
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		Thread.sleep(4000);
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		
		
		/*driver.findElement(By.id("createInitialViewlets")).click();
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByIndex(Integer.parseInt(EMS_WGS_INDEX));
		
		//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);
	}
	
	@Parameters({"ViewletName", "ViewletValue"})
	@TestRail(testCaseId=219)
	@Test(priority=1)
	public static void AddBridgeViewlet(String ViewletName, int ViewletValue, ITestContext context) throws InterruptedException
	{	
		Thread.sleep(2000);
		Viewlets obj=new Viewlets();
		obj.EMSViewlet(driver, ViewletValue, ViewletName, EMS_WGSNAME, EMSNode);
		
		if(driver.getPageSource().contains(ViewletName))
		{
			System.out.println("Bridge Viewlet is created");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Bridge viewlet is created successfully");
		}
		else
		{
			System.out.println("Bridge viewlet is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create bridge viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
		Thread.sleep(2000);
			
    }
	 
	@Parameters({"Dashboardname", "SourceTypevalue", "TargetType"})
	@TestRail(testCaseId=222)
	@Test(priority=2)
	public void AddBridgeFromPlusIcon(String Dashboardname, String SourceTypevalue, String TargetType,  ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		//Click on + icon
		driver.findElement(By.xpath("//img[@title='Add EMS Bridge']")).click();
		
		//Select WGS
		/*
		 * Select WGS=new Select(driver.findElement(By.xpath("//select")));
		 * WGS.selectByVisibleText(EMS_WGSNAME); Thread.sleep(3000);
		 */
		
		driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/ng-select/div/span")).click();
		Thread.sleep(2000);
		try 
		{
			WebElement ChannelauthNode=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
			List<WebElement> divs=ChannelauthNode.findElements(By.tagName("div"));
			//System.out.println(divs.size());	
			for (WebElement di : divs)
			{					
				if(di.getText().equals(EMS_WGSNAME))
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
		Thread.sleep(3000);
		
		/*
		 * //Select Node
		 * driver.findElement(By.xpath("//ng-select[2]/div/span")).click();
		 * Thread.sleep(3000); try { WebElement
		 * ChannelauthNode=driver.findElement(By.className("ng-dropdown-panel")).
		 * findElement(By.className("ng-dropdown-panel-items")); List<WebElement>
		 * divs=ChannelauthNode.findElements(By.tagName("div"));
		 * //System.out.println(divs.size()); for (WebElement di : divs) {
		 * if(di.getText().equals(EMSNode)) { di.click(); break; } } } catch(Exception
		 * ex) { ex.printStackTrace(); } Thread.sleep(2000);
		 */
		
		//Select Manager
		driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div[2]/ng-select/div/span")).click();
        try 
		{
        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
			List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
			//System.out.println(mdivs.size());	
			
			for (WebElement mdi : mdivs)
			{
				if(mdi.getText().equals(EMSQueueManager))
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
        Thread.sleep(2000);
		
		//Click on Select path
        driver.findElement(By.xpath("//button[contains(.,'Select path')]")).click();
		Thread.sleep(5000);
		
		//Select Source type
		driver.findElement(By.xpath("//ng-select[@id='sourceType']/div/span")).click();
		 try 
			{
	        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
				List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
				//System.out.println(mdivs.size());	
				
				for (WebElement mdi : mdivs)
				{
					if(mdi.getText().equals(SourceTypevalue))
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
	        Thread.sleep(2000);
		
		
		//Source Name selection
	    driver.findElement(By.xpath("//ng-select[@id='sourceName']/div/span[2]")).click();
	    Thread.sleep(3000);
	    try 
		{
        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
			List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
			//System.out.println(mdivs.size());	
			
			for (WebElement mdi : mdivs)
			{
				if(mdi.getText().equals(SourceName))
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
        Thread.sleep(3000);
		
		//Select Target type
		driver.findElement(By.xpath("//ng-select[@id='targetType']/div/span")).click();
		Thread.sleep(3000);
		 try 
			{
	        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
				List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
				//System.out.println(mdivs.size());	
				
				for (WebElement mdi : mdivs)
				{
					if(mdi.getText().equals(TargetType))
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
	        Thread.sleep(3000);
				
		//Select Target Name
	        driver.findElement(By.xpath("//ng-select[@id='targetName']/div/span[2]")).click();
	        Thread.sleep(2000);
	        try 
			{
	        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
				List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
				//System.out.println(mdivs.size());	
				
				for (WebElement mdi : mdivs)
				{
					if(mdi.getText().equals(TargetName))
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
	        Thread.sleep(2000);
		
		//Click on Ok button
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(10000);
		
		try 
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e)
		{
			System.out.println("Error popup page is not displayed");
		}
		
		for(int j=0; j<=1; j++)      
		{
			driver.findElement(By.cssSelector(".no-loading-spinner")).click();
			Thread.sleep(4000);
		}
		
		//Store the Topic viewlet data into string
		String Viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
		//System.out.println("Bridge viewlet data is: " +Viewletdata);
		
		//Store the Bridge name into string
		String FinalSource=SourceTypevalue.toLowerCase();
		String FinalType=TargetType.toLowerCase();
		
		StringJoiner joiner = new StringJoiner(":"); 
		joiner.add(FinalSource);
		joiner.add(SourceName);
		joiner.add(FinalType);
		joiner.add(TargetName);
		
		FinalBridgeName = joiner.toString();
		System.out.println(FinalBridgeName);
	
		if(Viewletdata.contains(FinalBridgeName))
		{
			System.out.println("Bridge is added");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Bridge viewlet is created using add Icon");
		}
		else
		{
			System.out.println("Bridge is not added");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add bridge viewlet using add Icon");
			driver.findElement(By.id("Bridge add Failed")).click();
		}
		Thread.sleep(2000);	
	}
	
	@Parameters({"Dashboardname", "schemaName"})
	@TestRail(testCaseId=220)
	@Test(priority=11)
	public static void ShowObjectAttributesForBridge(String Dashboardname, String schemaName, ITestContext context) throws InterruptedException
	{	
		try {
			EMSObjects obj=new EMSObjects();
		obj.ObjectAttributesVerificationForEMS(Dashboardname, driver, schemaName, EMS_WGSNAME);
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "Show object attributes for Bridge viewlet is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to show object attributes for Bridge viewlet");
			driver.findElement(By.id("Attributes failed")).click();
		}
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId=221)
	@Test(priority=12, dependsOnMethods= {"AddBridgeFromPlusIcon"})
	public void DeleteCommand(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search the bridge name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalBridgeName);
		Thread.sleep(1000);
		
		//Select Delete from commands option
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehour=new Actions(driver);
    	Mousehour.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Delete")).click();
    	Thread.sleep(2000);
    	
    	//Click on yes button
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(6000);
    	    	
    	//Refresh the viewlet
    	driver.findElement(By.cssSelector(".no-loading-spinner")).click();
    	Thread.sleep(3000);
    	
    	//Store the viewlet data into string
    	String viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println(viewletdata);
    	
    	//Clear the search data
    	for(int j=0; j<=FinalBridgeName.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(1000);
    	
    	//verification
    	if(viewletdata.contains(FinalBridgeName))
    	{
    		System.out.println("Bridge is not deleted");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to delete bridge");
    		driver.findElement(By.xpath("Bridge delete failed")).click();
    		
    	}
    	else
    	{
    		System.out.println("Bridge is deleted");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Bridge is deleted successfuuly");
    		
    		
    	}
    	Thread.sleep(1000);
	}
	@Parameters({"Dashboardname"})
	@Test(priority=4)
	@TestRail(testCaseId=223)
	public void Properties(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select properties option
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	driver.findElement(By.linkText("Properties...")).click();
    	Thread.sleep(8000);
    	
    	//storing the name field status into boolean
		boolean NameField=driver.findElement(By.xpath("//ng-select/div")).isEnabled();
		System.out.println(NameField);
		
		//Verification Condition
		if(NameField == true)
		{
			System.out.println("The Bridge name field is Disabled");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Bridge properties are working fine");
			driver.findElement(By.cssSelector(".btn-primary")).click();
		}
		else
		{
			System.out.println("The Brideg name field is Enabled");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Bridge properties are not working properly");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			driver.findElement(By.xpath("Name field is disabled")).click();
			
		}
		Thread.sleep(1000); 
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=5)
	@TestRail(testCaseId=224)
    public static void BridgeEvents(String Dashboardname, ITestContext context) throws InterruptedException 
    {
		AllEvents obj=new AllEvents();
		obj.Events(Dashboardname, driver, context);
    }
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=225)
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
		
		//Select WGS dropdown
		driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div/div[2]/div/ng-select/div/span")).click();
		Thread.sleep(3000);
		
		WebElement drop1=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div1=drop1.findElements(By.tagName("div"));
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement di1 : div1)
		{
			//System.out.println("text is :" +di.getText());
			if(di1.getText().equalsIgnoreCase(EMS_WGSNAME))
			{
				di1.click();
				break;
			}	
		}
		Thread.sleep(2000);
		
		//Submit
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(2000);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(1000);

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
			System.out.println("Bridge name is added to the Favorite viewlet");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Bridge name added successfully to favorite viewlet");
		}
		else
		{
			System.out.println("Bridge name is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to add Bridge name added to favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=7)
	@TestRail(testCaseId=226)
	public static void CompareBridges(ITestContext context) throws InterruptedException
	{
		CompareObjects com=new CompareObjects();
		com.Compare(driver, WGSName, context);
	}
	
	@TestRail(testCaseId = 783)
	@Test(priority=8)
	public void CheckDifferencesForBridges(ITestContext context) throws InterruptedException
	{
		DifferenceOfObjects diff=new DifferenceOfObjects();
		diff.Differences(driver, context);
	}	
	@Parameters({"Dashboardname"})
	@Test(priority=13)
	@TestRail(testCaseId=227)
	public void DeleteMultiples(String Dashboardname, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Store the Bridge names into string          
		String FirstName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		String SecondName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Select Delete from Commands option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Mousehour=new Actions(driver);
		Mousehour.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Delete")).click();
		Thread.sleep(1000);
		
		//Click on yes button
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(1000);
    	
    	//Store the viewlet data into string
    	String viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println(viewletdata);
    	
    	//verification
    	if(viewletdata.contains(FirstName) || viewletdata.contains(SecondName))
    	{
    		System.out.println("Multiple Bridges are not deleted");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to delete Multiple Bridges");
    		driver.findElement(By.xpath("Multiple Bridges delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Multiple Bridges are deleted");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Multiple Bridges are deleted");
    	}
    	Thread.sleep(1000);
		
	}
	@Parameters({"Dashboardname", "SelectorInput"})
	@TestRail(testCaseId=228)
	@Test(priority=9)
	public void MultipleBridgeProperties(String Dashboardname, String SelectorInput, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(6000);
		
		/*WebElement ele=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span"));
		Actions a=new Actions(driver);
		a.moveToElement(ele).perform();
		System.out.println(ele.getAttribute("value"));
		Thread.sleep(1000);*/
		
		//Enter the data into selector field
		driver.findElement(By.id("targetSelector")).clear();
		driver.findElement(By.id("targetSelector")).sendKeys(SelectorInput);
		
		//Click on yes button
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(10000);
    	
    	for(int i=0; i<=10; i++)
    	{
    	//Refresh the viewlet
    		driver.findElement(By.cssSelector(".no-loading-spinner")).click();
    	    Thread.sleep(1000);
    	}
    	//Clearing selection of object
    	ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
    	che1.Deselectcheckbox(Dashboardname, driver);
    			
    	//Open the properties of first Bridge
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Get the Source type data and store into string
		String FirstBridge=driver.findElement(By.id("targetSelector")).getAttribute("value");
		System.out.println("Bridge selector name is: " +FirstBridge);
		
		//Click on yes button
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(2000);
    	
    	//Clearing selection of object
    	ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
    	che2.Deselectcheckbox(Dashboardname, driver);
    			
    	//Open the properties of second Bridge
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Get the Source type data and store into string
		String SecondBridge=driver.findElement(By.id("targetSelector")).getAttribute("value");
		System.out.println("Second bridge selector name is: " +SecondBridge);
		
		//Click on yes button
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(2000);
    	
    	//Verification condition
    	if(SelectorInput.equals(FirstBridge) && SelectorInput.equals(SecondBridge))
    	{
    		System.out.println("Multiple properties are verified");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Multiple Bridges are deleted");
    	}
    	else
    	{
    		System.out.println("Multiple properties are not verified");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to delete multiple bridges");
    		driver.findElement(By.id("Multiple properties failed")).click();
    	}
    	Thread.sleep(1000);
		
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=229)
	@Test(priority=10, dependsOnMethods= {"AddToFavoriteViewlet"})
	public static void AddToFavoriteForMultipleBridges(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Store the Bridge names into string             
		String BridgeName2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("bridge name :" +BridgeName2);
		String BridgeName3=driver.findElement(By.xpath("//datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Last bridge name :" +BridgeName3);
		
		//Select Add to favorite option
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
			System.out.println("Multiple Bridges are added to the Favorite viewlet");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Multiple Bridges are added successfully to favorite viewlet");
		}
		else
		{
			System.out.println("Multiple Bridges are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to add Multiple Bridges to favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=16)
	public void Logout(String Dashboardname) throws InterruptedException 
	{
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, Dashboardname);
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