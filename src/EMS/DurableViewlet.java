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
import Common.Dashboard;
import Common.LogoutForAll;
import Common.Viewlets;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class DurableViewlet 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String EMS_WGS_INDEX;
	static String EMS_WGSNAME;
	static String SelectTopicName;
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
		EMS_WGS_INDEX =Settings.getEMS_WGS_INDEX();
		EMS_WGSNAME =Settings.getEMS_WGSNAME();
		SelectTopicName = Settings.getSelectTopicName(); 
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
	
	@Test(priority=1)
	@TestRail(testCaseId=230)
    @Parameters({"ViewletValue", "ViewletName"})
	public static void AddDurableViewlet(int ViewletValue, String ViewletName, ITestContext context) throws InterruptedException
	{
		Viewlets obj=new Viewlets();
		obj.EMSViewlet(driver, ViewletValue, ViewletName, EMS_WGSNAME, EMSNode);
		
		if(driver.getPageSource().contains(ViewletName))
		{
			System.out.println("Durable Viewlet is created");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Durable viewlet is created successfully");
		}
		else
		{
			System.out.println("Durable viewlet is not created");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Faile to add Durable viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
		Thread.sleep(2000);
			
    }
	
	@Parameters({"Dashboardname", "schemaName"})
	@TestRail(testCaseId=232)
	@Test(priority=13)
	public static void ShowObjectAttributesForDurable(String Dashboardname, String schemaName, ITestContext context) throws InterruptedException
	{		
		try {
		EMSObjects obj=new EMSObjects();
		obj.ObjectAttributesVerificationForEMS(Dashboardname, driver, schemaName, EMS_WGSNAME);
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "Show object attribute option for Durable viewlet is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Faile to show object attributes for Durable viewlet");
			driver.findElement(By.id("Attributes failed")).click();
		}
	}
	
	@Parameters({"Dashboardname", "DurableName"})
	@TestRail(testCaseId=231)
	@Test(priority=2)
	public void AddDurableFromPlusIcon(String Dashboardname, String DurableName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Click on + icon
		driver.findElement(By.id("add-object")).click();
		Thread.sleep(3000);
		
		//Select WGS
		/*
		 * Select WGS=new Select(driver.findElement(By.xpath("//select")));
		 * WGS.selectByVisibleText(EMS_WGSNAME); Thread.sleep(3000);
		 */
		
		/*driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/ng-select/div/span")).click();
		Thread.sleep(3000);
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
		
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(2000);*/
		
		/*
		 * //Select Node
		 * driver.findElement(By.xpath("//ng-select[2]/div/span")).click(); try {
		 * WebElement
		 * ChannelauthNode=driver.findElement(By.className("ng-dropdown-panel")).
		 * findElement(By.className("ng-dropdown-panel-items")); List<WebElement>
		 * divs=ChannelauthNode.findElements(By.tagName("div"));
		 * System.out.println(divs.size()); for (WebElement di : divs) {
		 * if(di.getText().equals(EMSNode)) { di.click(); break; } } } catch(Exception
		 * ex) { ex.printStackTrace(); } Thread.sleep(2000);
		 */
		
		//Select Manager
		driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(3000);
        try 
		{
        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
			List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
			System.out.println(mdivs.size());	
			
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
		
		//Durable Name
		driver.findElement(By.id("durableName")).sendKeys(DurableName);
		
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
				System.out.println("s"+s);
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
		Thread.sleep(1000);
		
		//click on Create button
    	driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
    	Thread.sleep(20000);
    	
    	try 
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e)
		{
			System.out.println("Error popup page is not displayed");
		}
		
		for(int j=0; j<=3; j++)      
		{
			driver.findElement(By.cssSelector(".no-loading-spinner")).click();
			Thread.sleep(4000);
		}
    	System.out.println("in");
    	
    	//Store the Topic viewlet data into string
    	String Viewletdata=driver.findElement(By.cssSelector(".datatable-body")).getText();
    	System.out.println(Viewletdata);
    	
    	if(Viewletdata.contains(DurableName))
		{
			System.out.println("Durable is added");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Durable viewlet is created successfully using add Icon");
		}
		else
		{
			System.out.println("Durable is not added");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add Durable viewlet using add Icon");
			driver.findElement(By.id("Durable add Failed")).click();
		}
		Thread.sleep(2000);	
	}
	
	@Parameters({"Dashboardname", "DurableName"})
	@TestRail(testCaseId=233)
	@Test(priority=12, dependsOnMethods= {"AddDurableFromPlusIcon"})
	public void DeleteCommand(String Dashboardname, String DurableName,ITestContext context) throws InterruptedException
	{ 
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		Thread.sleep(2000);
		
		//Search the bridge name
		driver.findElement(By.cssSelector(".main-filter-by-input")).clear();
		driver.findElement(By.cssSelector(".main-filter-by-input")).sendKeys(DurableName);
		Thread.sleep(1000);
		
		//Select Delete option from command
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	Thread.sleep(1000);
    	Actions Mousehour=new Actions(driver);
    	Mousehour.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Delete")).click();
    	Thread.sleep(6000);
    	
    	//click on yes button
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(1000);
    	    	
    	//Refresh the viewlet
    	driver.findElement(By.cssSelector(".no-loading-spinner")).click();
    	Thread.sleep(3000);
    	
    	//Store the viewlet data into string
    	String viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println(viewletdata);
    	
    	//Clear the search data
    	for(int j=0; j<=DurableName.length(); j++)
    	{
    		driver.findElement(By.xpath("((//input[@type='text'])[4]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(1000);
    	
    	//verification
    	if(viewletdata.contains(DurableName))
    	{
    		System.out.println("Durable is not deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete Durable viewlet using delete command");
    		driver.findElement(By.xpath("Durable delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Durable is deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Durable viewlet is deleted successfully using delete command");
    	}
    	Thread.sleep(1000);
	}
	@Parameters({"Dashboardname"})
	@Test(priority=4)
	@TestRail(testCaseId=234)
	public void Purge(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		try {
		//Select Purge option from command
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	Actions Mousehourpurge=new Actions(driver);
    	Mousehourpurge.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Purge")).click();
    	Thread.sleep(6000);
    	
    	//click on yes button
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(1000);
    	context.setAttribute("Status",1);
		context.setAttribute("Comment", "Purge option for Durable viewlet is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Purge option for Durable viewlet is not working properly");
		}
		
	}
	@Parameters({"Dashboardname"})
	@Test(priority=5)
	@TestRail(testCaseId=235)
	public void Properties(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Properties option
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	driver.findElement(By.linkText("Properties...")).click();
    	Thread.sleep(6000);
    	
    	//storing the name field status into boolean
		boolean NameField=driver.findElement(By.id("durableName")).isEnabled();
		System.out.println(NameField);
		
		//Verification Condition
		if(NameField == false)
		{
			System.out.println("The Durable name field is Disabled");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Durable viewlet properties are working fine");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(4000); 
		}
		else
		{
			System.out.println("The Durable name field is Enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Durable viewlet properties are not working properly");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			driver.findElement(By.xpath("Name field is disabled")).click();
			Thread.sleep(4000);
		}
		
	}
	@Parameters({"Dashboardname"})
	@Test(priority=6)
	@TestRail(testCaseId=236)
    public static void DurableEvents(String Dashboardname, ITestContext context) throws InterruptedException 
    {
		AllEvents obj=new AllEvents();
		obj.Events(Dashboardname, driver, context);
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=237)
	@Test(priority=7)
	public static void AddToFavoriteViewlet(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
    	//Store the Route Name
		String DurableName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		                                             
		//Create Favorite viewlet
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
		
		if(Favdata.contains(DurableName))
		{
			System.out.println("Bridge name is added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Durable viewlet is added successfully to Favorite viewlet");
		}
		else
		{
			System.out.println("Bridge name is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add Durable viewlet to Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=8)
	@TestRail(testCaseId=238)
	public void DeleteMultiples(String Dashboardname, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Store the Durable names into string
		String FirstName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		String SecondName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Select delete option from command for multiple
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions Mousehour=new Actions(driver);
		Mousehour.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Delete")).click();
		Thread.sleep(4000);
		
		//Click on Yes button
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(6000);	
		
		//Store the viewlet data into string
    	String viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println(viewletdata);
    	
    	//verification
    	if(viewletdata.contains(FirstName) || viewletdata.contains(SecondName))
    	{
    		System.out.println("Multiple Durables are not deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete Multiple Durable");
    		driver.findElement(By.xpath("Multiple Durables delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Multiple Durables are deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple Durables are deleted successfully");
    	}
    	Thread.sleep(1000);
		
	}
	@Parameters({"Dashboardname"})
	@Test(priority=9)
	@TestRail(testCaseId=239)
	public void MultiplePurge(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		try {
		//Select delete option from command for multiple
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions Mousehourpurge=new Actions(driver);
		Mousehourpurge.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Purge")).click();
		Thread.sleep(1000);
		
		//Click on Yes button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(4000);
		
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "Multiple purge option is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Multiple purge option is not working properly");
		}
		
	}
	@Parameters({"Dashboardname"})
	@Test(priority=10)
	@TestRail(testCaseId=240)
	public void MultipleProperties(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Priority option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(6000);
		
		WebElement ele=driver.findElement(By.id("durableName"));
		Actions a=new Actions(driver);
		a.moveToElement(ele).perform();
	
		//List<WebElement> data= (List<WebElement>) driver.findElement(By.tagName("app-text-input-tooltip")).findElements(By.xpath("//table//tr"));
		//String Tooltipdata=driver.findElement(By.tagName("app-text-input-tooltip")).getText();
		String Tooltipdata=driver.findElement(By.tagName("ngb-tooltip-window")).getText();
		
		System.out.println("Multiple Properties data: " +Tooltipdata);
		
		
		//Click on yes button
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(10000);
    	
    	//Clearing selection of object
    	ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
    	che1.Deselectcheckbox(Dashboardname, driver);
    	
    	//Open the properties of first durable
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Store the Topic name into string
		//String FirstDurablename=driver.findElement(By.xpath("//ng-select/div")).getText();
		String FirstDurablename=driver.findElement(By.id("durableName")).getAttribute("value");
		System.out.println("First durable data: " +FirstDurablename);
		
		//Click on yes button
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(10000);
    	
    	//Clearing selection of object
    	ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
    	che2.Deselectcheckbox(Dashboardname, driver);
    	
    	//Open the properties of Second durable
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Store the Topic name into string
		//String SecondDurablename=driver.findElement(By.xpath("//ng-select/div")).getText();
		String SecondDurablename=driver.findElement(By.id("durableName")).getAttribute("value");
		System.out.println("Second durable data: " +SecondDurablename);
		
		//Click on yes button
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(10000);
    	
    	//Verification
    	if(Tooltipdata.contains(FirstDurablename) && Tooltipdata.contains(SecondDurablename))
    	{
    		System.out.println("Multiple durable properties are fine");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Multiple durable properties are working fine");
    	}
    	else
    	{
    		System.out.println("Multiple durable properties are not fine");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Multiple durable properties are not working properly");
    		driver.findElement(By.id("multiple durables failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=241)
	@Test(priority=11, dependsOnMethods= {"AddToFavoriteViewlet"})
	public static void AddToFavoriteForMultipleDurables(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Store the Name list names
		String DurableName2=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		String DurableName3=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Select compare option
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
		
		if(Favdata.contains(DurableName2) && Favdata.contains(DurableName3))
		{
			System.out.println("Multiple Durables are added to the Favorite viewlet");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Multiple durable are added to favorite viewlet");
		}
		else
		{
			System.out.println("Multiple Durables are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "failed to add Multiple durable to favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
	}
	@Parameters({"Dashboardname"})
	@Test(priority=18)
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

	
