package EMS;

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
public class RouteViewlet {
	
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName;
	static String EMS_WGSNAME;
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
		EMS_WGSNAME =Settings.getEMS_WGSNAME();
		EMSNode=Settings.getEMSNode();
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
		
		/*
		driver.findElement(By.id("createInitialViewlets")).click();
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
		Thread.sleep(3000);
	}
	
	@Test(priority=1)
	@TestRail(testCaseId=202)
    @Parameters({"ViewletName", "ViewletValue"})
	public static void AddRouteViewlet(String ViewletName, int ViewletValue,  ITestContext context) throws InterruptedException
	{
		Viewlets obj=new Viewlets();
		obj.EMSViewlet(driver, ViewletValue, ViewletName, EMS_WGSNAME, EMSNode);
		
		if(driver.getPageSource().contains(ViewletName))
		{
			System.out.println("Route Viewlet is created");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Route viewlet is created successfully");
		}
		else
		{
			System.out.println("Route viewlet is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create route viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
			
    }
	
	@Parameters({"Dashboardname", "RouteNameFromIcon", "ConnectionURLNameField"})
	@TestRail(testCaseId=213)
	@Test(priority=2)
	public void AddRouteFromPlusIcon(String Dashboardname, String RouteNameFromIcon, String ConnectionURLNameField, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Click on + icon
		driver.findElement(By.xpath("//img[@title='Add EMS Route']")).click();
		
		//Select WGS
	/*	driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/ng-select/div/span")).click();
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
		Thread.sleep(2000);*/
		
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
		Thread.sleep(4000);
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
        Thread.sleep(1000);
		
		//Click on Select path
        driver.findElement(By.xpath("//button[contains(.,'Select path')]")).click();
		Thread.sleep(2000);
		
		//Route Name
		driver.findElement(By.id("name")).sendKeys(RouteNameFromIcon);
		
		//Connection URL
		driver.findElement(By.id("connectionURL")).sendKeys(ConnectionURLNameField);
		
		//Close the window
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(10000);
		
		try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(3000);
			driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
			Thread.sleep(3000);
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
		
		//Store the viewlet data into string
		String ViewletData=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println();
		
		//Verification
		if(ViewletData.contains(RouteNameFromIcon))
		{
			System.out.println("Route is added successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Route is created successfully using add Icon");
		}
		else
		{
			System.out.println("Route is not added");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create route using add Icon");
			driver.findElement(By.id("Route Add failed")).click();
		}
		Thread.sleep(2000);
		
	}
	
	@Parameters({"Dashboardname", "schemaName"})
	@TestRail(testCaseId=203)
	@Test(priority=15)
	public static void ShowObjectAttributesForRoute(String Dashboardname, String schemaName, ITestContext context) throws InterruptedException
	{		
		try {
			EMSObjects obj=new EMSObjects();
		obj.ObjectAttributesVerificationForEMS(Dashboardname, driver, schemaName, EMS_WGSNAME);
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "Show object attributes for route viewlet is working fine");
		}catch(Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to show object attributes for route viewlet, check details: "+ e.getMessage());
			driver.findElement(By.id("Objects attributes failed")).click();
		}
	}
	@Parameters({"Dashboardname"})
	@Test(priority=3)
	@TestRail(testCaseId=204)
	public void RouteStatus(String Dashboardname, ITestContext context) throws InterruptedException
	{	
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Refresh the viewlet
		driver.findElement(By.cssSelector(".no-loading-spinner")).click();
		Thread.sleep(2000);
		
		//Select Show Status option
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	driver.findElement(By.linkText("Show Routes Status")).click();
    	Thread.sleep(3000);
    	
    	//Store the Values into string
    	String Status1=driver.findElement(By.xpath("//tr[3]/td[2]")).getText();
    	System.out.println("Status1 :" +Status1);
    	
    	//Store the Status data into string 
    	String Status2=driver.findElement(By.xpath("//tr[4]/td[2]")).getText();
    	System.out.println("Status2 :" +Status2);
		
    	//Total data of the Status page
    	String Status=driver.findElement(By.xpath("//app-console-tab/div/div[2]/div[3]")).getText();
    	System.out.println("Final status :" +Status);
    	
    	if(Status.contains(Status1) && Status.contains(Status2))
    	{
    		System.out.println("Route status page is verified");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "route status is verified successfully");
    	}
    	else
    	{
    		System.out.println("Route status page is not verified");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to verify route status");
    		//Close the status Popup page
        	driver.findElement(By.cssSelector(".close-button")).click();
        	Thread.sleep(1000);
        	driver.findElement(By.id("Route status failed")).click();
    	}
    	
    	//Close the status Popup page
    	driver.findElement(By.cssSelector(".close-button")).click();
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "RouteNameFromIcon"})
	@TestRail(testCaseId=205)
	@Test(priority=4, dependsOnMethods= {"AddRouteFromPlusIcon"})
	public void DeleteCommand(String Dashboardname, String RouteNameFromIcon, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search option
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RouteNameFromIcon);
		Thread.sleep(2000);
		
		//Select Delete option
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	Actions Mousehour=new Actions(driver);
    	Mousehour.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Delete")).click();
    	Thread.sleep(3000);
    	
    	//Click on yes button
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(8000);
    	
    	//Clear the search data
    	for(int j=0; j<=RouteNameFromIcon.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}    	
    	Thread.sleep(1000);
    	
    	//Refresh the viewlet
    	driver.findElement(By.cssSelector(".no-loading-spinner")).click();
    	Thread.sleep(3000);
    	
    	//Store the viewlet data into string
    	String viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println(viewletdata);
    	
    	//verification
    	if(viewletdata.contains(RouteNameFromIcon))
    	{
    		System.out.println("Route is not deleted");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to delete route");
    		driver.findElement(By.xpath("Route delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Route is deleted");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Deletion of route is working fine");
    	}
    	Thread.sleep(1000);
	}
	@Parameters({"Dashboardname"})
	@Test(priority=5)
	@TestRail(testCaseId=206)
	public void Properties(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Properties option
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	driver.findElement(By.linkText("Properties...")).click();
    	Thread.sleep(4000);
    	
    	//storing the name field status into boolean
		boolean NameField=driver.findElement(By.id("name")).isEnabled();
		System.out.println(NameField);
		Thread.sleep(2000);
		
		//Verification Condition
		if(NameField == false)
		{
			System.out.println("The Route name field is Disabled");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Route properties are working fine");
			driver.findElement(By.xpath("//div[3]/button")).click();
			
		}
		else
		{
			System.out.println("The Route name field is Enabled");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to show route properties");
			driver.findElement(By.xpath("//div[3]/button")).click();
			Thread.sleep(4000);
			driver.findElement(By.xpath("Name field is disabled")).click();
			
		}
		Thread.sleep(4000);
	}
	@Parameters({"Dashboardname"})
	@Test(priority=6)
	@TestRail(testCaseId=207)
    public static void RouteEvents(String Dashboardname, ITestContext context) throws InterruptedException 
    {
		AllEvents obj=new AllEvents();
		obj.Events(Dashboardname, driver, context);     
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=208)
	@Test(priority=7)
	public static void AddToFavoriteViewlet(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
    	//Store the Route Name into string
		String RouteName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Create favorite viewlet
		driver.findElement(By.xpath("//button[3]")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		
		//Deselect WGS  
		driver.findElement(By.xpath("//span[2]/i")).click();  
		Thread.sleep(LowSleep);
		//Select WGS dropdown
		//driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div/div[2]/div/ng-select/div/span")).click();
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
		
		//Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(1000);

		
		//Select the favorite viewlet name
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div/span")).click();
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
		
		//Verification of Route added into favorite viewlet
		if(Favdata.contains(RouteName))
		{
			System.out.println("Route is added to the Favorite viewlet");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Adding route to favorite viewlet is working fine");
		}
		else
		{
			System.out.println("Route is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to add route to favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	
	@Test(priority=8)
	@TestRail(testCaseId=209)
	public static void CompareRoutes(ITestContext context) throws InterruptedException
	{
		CompareObjects com=new CompareObjects();
		com.Compare(driver, WGSName, context);
	}
	
	
	@TestRail(testCaseId = 782)
	@Test(priority=9)
	public void CheckDifferencesForRoutes(ITestContext context) throws InterruptedException
	{
		DifferenceOfObjects diff=new DifferenceOfObjects();
		diff.Differences(driver, context);
	}
	@Parameters({"Dashboardname"})
	@Test(priority=10)
	@TestRail(testCaseId=210)
	public void DeleteMultiples(String Dashboardname, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Store the Route names into string
		String FirstName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		String SecondName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		
		//Select Delete from commands option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions Mousehour=new Actions(driver);
		Mousehour.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Delete")).click();
		Thread.sleep(2000);
		
		//Click on yes button
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(8000);
    	
    	//Store the viewlet data into string
    	String viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println(viewletdata);
    	
    	//verification
    	if(viewletdata.contains(FirstName) || viewletdata.contains(SecondName))
    	{
    		System.out.println("Multiple Routes are not deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Deleting multiple routes is working fine");
    		driver.findElement(By.xpath("Multiple Routes delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Multiple Routes are deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete multiple routes");
    	}
    	Thread.sleep(1000);
		
	}
	
	@Parameters({"Dashboardname", "ConnectionURLName"})
	@TestRail(testCaseId=211)
	@Test(priority=11)
	public void MultipleProperties(String Dashboardname, String ConnectionURLName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Give Connection url
		driver.findElement(By.id("connectionURL")).clear();
		driver.findElement(By.id("connectionURL")).sendKeys(ConnectionURLName);
		System.out.println("Input url: " +ConnectionURLName);
		
		//close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Open the properties of first route
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Save the Connection URL value into string
		String FirstRoutedata=driver.findElement(By.id("connectionURL")).getAttribute("value");
		System.out.println("First Url: " +FirstRoutedata);
		
		//close the properties page
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(4000);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
		che2.Deselectcheckbox(Dashboardname, driver);
		
		//Open the properties of Second route
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Save the Connection URL value into string
		String SecondRoutedata=driver.findElement(By.id("connectionURL")).getAttribute("value");
		System.out.println("second Url: " +SecondRoutedata);
		
		//close the properties page
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(4000);
		
		//Verification
		if(FirstRoutedata.equals(ConnectionURLName) && SecondRoutedata.equals(ConnectionURLName))
		{
			System.out.println("multiple properties wotrking fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple route properties is working fine");
		}
		else
		{
			System.out.println("multiple properties are not wotrking");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to show multiple route properties");
			driver.findElement(By.id("multiple properties failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=212)
	@Test(priority=12, dependsOnMethods= {"AddToFavoriteViewlet"})
	public static void AddToFavoriteForMultipleRoutes(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Store the Route names into string
		String RouteName2=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Route 2 is: " +RouteName2);
		String RouteName3=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Route 3 is: " +RouteName3);
		
		//Select Add to favorite viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(4000);
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(2000);
		
		//Select the favorite viewlet name
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div/span")).click();
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
		
		//Verification of routes added into favorite viewlet
		if(Favdata.contains(RouteName2) && Favdata.contains(RouteName3))
		{
			System.out.println("Multiple Routes are added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple routes are added successfully to favorite viewlet");
		}
		else
		{
			System.out.println("Multiple Routes are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add multiple routes to favorite viewlet");
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