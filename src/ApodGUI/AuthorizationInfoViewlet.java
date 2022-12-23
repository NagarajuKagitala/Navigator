package ApodGUI;

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
import org.openqa.selenium.chromium.ChromiumDriver;
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
public class AuthorizationInfoViewlet {
	
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName;
	static String Node_Hostname;
	static String Manager2;
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
		Node_Hostname =Settings.getNode_Hostname();
		Manager2 =Settings.getManager2();
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
		
		driver.manage().deleteAllCookies();
	    ((ChromiumDriver) driver).getSessionStorage().clear();
	    ((ChromiumDriver) driver).getLocalStorage().clear();
		
		//Login page
		driver.findElement(By.id("username")).sendKeys(uname);
		Thread.sleep(2000);
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
	
	@Test(priority=1)
	@TestRail(testCaseId=180)
    @Parameters({"ViewletName", "ViewletValue"})
	public static void AddAuthInfoViewlet(String ViewletName, int ViewletValue, ITestContext context) throws InterruptedException
	{
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		Thread.sleep(MediumSleep);
		
		if(driver.getPageSource().contains(ViewletName))
		{
			System.out.println("Authinfo Viewlet is created");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Authinfo viewlet is created successfully");
		}
		else
		{
			System.out.println("Authinfo viewlet is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create Authinfo viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
			
    }
	
	@TestRail(testCaseId = 1043)
	@Test(priority=2)
	public void PropertieOfAuthInfo(ITestContext context) throws InterruptedException
	{
		//Click on clear all check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(LowSleep);
		
		//Select channel properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		//Store the editable function in to a string
		boolean FieldNamevalue=driver.findElement(By.id("name")).isEnabled();
		System.out.println(FieldNamevalue);
		
		//Verification of name field is editable or not
		if(FieldNamevalue == false)
		{
			System.out.println("Auth info name field is UnEditable");
			 driver.findElement(By.cssSelector(".btn-primary")).click();
			 context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Auth info Name field is UnEditable");
			 Thread.sleep(LowSleep);
		}
		else
		{
			System.out.println("Auth info Name field is Editable");
			context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Auth info Name field is Editable");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("Auth info name edit function Failed")).click();
			
		}
		Thread.sleep(1000);
		
	}
	
	@TestRail(testCaseId = 1007)
	@Parameters({"Dashboardname"})
	@Test(priority=3)
	public void MQSCSnapshot(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Save the Auth info name into string
		String Authname=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println("Object name is: " +Authname);
				
		//Select MQSCSnapshot option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("MQSC"))).perform();
    	Thread.sleep(LowSleep);
    	driver.findElement(By.linkText("Snapshot...")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Get the snapshot data and store into string
    	String SnapshotData=driver.findElement(By.xpath("//textarea")).getText();
    	System.out.println("Snapshot data is: " +SnapshotData);
    	
    	//verification
    	if(SnapshotData.contains(Authname))
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
			driver.findElement(By.id("Auth info snapshot failed")).click();
    	}
	}
	
	@TestRail(testCaseId = 1008)
	@Test(priority=4, dependsOnMethods= {"MQSCSnapshot"})
	public void SaveAuthinfoSnapshot(ITestContext context)
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
	
	@Parameters({"Dashboardname", "AuthinfoAttributes", "schemaName"})
	@TestRail(testCaseId=181)
	@Test(priority=17)
	public static void ShowObjectAttributesForAuthInfo(String Dashboardname, String AuthinfoAttributes, String schemaName, ITestContext context) throws InterruptedException
	{
		try {
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.AuthinfoObjectAttributesVerification(Dashboardname, driver, schemaName, WGSName);
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "Show object attributes for AuthInfo viewlet is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to Show object attributes for AuthInfo viewlet, check details: "+ e.getMessage());
			driver.findElement(By.id("Attributes failed")).click();	
		}
	}
	
	@Parameters({"Dashboardname"})
    @Test(priority=5)
    @TestRail(testCaseId=182)
    public static void AuthInfoEvents(String Dashboardname, ITestContext context) throws InterruptedException 
    {
    	try
    	{
    		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
    		Thread.sleep(LowSleep);
    	}
    	catch (Exception e)
    	{
    		
    	}
    	    	
    	AllEvents obj=new AllEvents();
		obj.Events(Dashboardname, driver, context);
	}
	
    @Parameters({"FavoriteViewletName", "Dashboardname"})
    @TestRail(testCaseId=183)
	@Test(priority=6)
	public static void AddToFavoriteViewlet(String FavoriteViewletName, String Dashboardname, ITestContext context) throws InterruptedException
	{
    	//Store the Auth info name into string
		String AuthinfoName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
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
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(LowSleep);
		
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

		//Select the favorite viewlet name
		/*
		 * Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(1000);
		 * driver.findElement(By.
		 * cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue"
		 * )).click(); Thread.sleep(1000);
		 */
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification of Auth info added to favorite viewlet
		if(Favdata.contains(AuthinfoName))
		{
			System.out.println("Auth info is added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "AuthInfo is added successfully to favorite viewlet");
		}
		else
		{
			System.out.println("Auth info is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Faile to add AuthInfo to favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);

	}
	
	
	@Test(priority=7)
	@TestRail(testCaseId=184)
	public static void CompareAuthInfo(ITestContext context) throws InterruptedException
	{
		//Click on clear all check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(LowSleep);          
		
		//Get the First object Name
		String compare1 = driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println("First obj name is: " +compare1);                                            
		Thread.sleep(LowSleep);
		
		//Get the second object name
		String compare2 = driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Second obj name is: " +compare2);
		Thread.sleep(LowSleep);
		
		// Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
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
		Thread.sleep(LowSleep);	
		
	}
	
	
	@TestRail(testCaseId = 779)
	@Test(priority=8)
	public void CheckDifferencesForAuthInfos(ITestContext context) throws InterruptedException
	{
		//Click on clear all check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(LowSleep);
		
		// Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Compare")).click();
		Thread.sleep(MediumSleep);
		
		// Check differences only option while compare
		driver.findElement(By.cssSelector(".differences .slider")).click();
		Thread.sleep(MediumSleep);
			
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
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=185)
	@Test(priority=9, dependsOnMethods= {"AddToFavoriteViewlet"})
	public static void AddToFavoriteForMultipleAuthInfo(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{		
		//Store the Auth info names into strings
		String AuthInfo2=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		String AuthInfo3=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
				
		//Select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(LowSleep);
		
		//Select fav viewlet name
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
		 * //Select the favorite viewlet name Select fav=new
		 * Select(driver.findElement(By.cssSelector(".fav-select")));
		 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(1000);
		 * driver.findElement(By.
		 * cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue"
		 * )).click(); Thread.sleep(1000);
		 */
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification of Auth infos added to favorite viewlet
		if(Favdata.contains(AuthInfo2) && Favdata.contains(AuthInfo3))
		{
			System.out.println("Multiple Auth infos are added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Adding multiple AuthInfo to the favorite viewlet is working fine");
		}
		else
		{
			System.out.println("Multiple Auth infos are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add Multiple AuthInfo to the Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId = 1044)
	@Parameters({"AuthInfoName", "ConnectionName"})
	@Test(priority=10)
	public void CreateAuthInfoFromPlusIcon(String AuthInfoName, String ConnectionName, ITestContext context) throws InterruptedException
	{
		//Click on + icon
		driver.findElement(By.xpath("//img[@title='Add Auth Info']")).click();
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
		Thread.sleep(LowSleep);
				
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
		driver.findElement(By.id("select-path")).click();
		Thread.sleep(LowSleep);
		
		//Channel name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(AuthInfoName);
		
		//Click on LDAP tab
		driver.findElement(By.linkText("LDAP")).click();
		Thread.sleep(LowSleep);
		
		//give the connection name
		driver.findElement(By.id("connName")).sendKeys(ConnectionName);
		Thread.sleep(LowSleep);
		
		//Click on Ok button
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(MediumSleep);
				
		try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
			Thread.sleep(LowSleep);
		}
		catch(Exception e)
		{
			System.out.println("No error messages");
		}
		
		//Search with the added name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(AuthInfoName);
		Thread.sleep(LowSleep);
		
		//Get the viewlet data into string
		String Viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
		//System.out.println("Channel data is: " +Viewletdata);
		
		//Remove the search data
		for(int i=0; i<=AuthInfoName.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		//verification
		if(Viewletdata.contains(AuthInfoName))
		{
			System.out.println("AuthInfo is added");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "AuthInfo is created successfully");
		}
		else
		{
			System.out.println("AuthInfo is not added");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to create AuthInfo");
			driver.findElement(By.id("AuthInfo adding failed")).click();
		}
		
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
