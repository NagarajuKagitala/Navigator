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
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

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
public class CommInfoViewlet 
{
    static WebDriver driver;
	
	static String Screenshotpath;
	static String WGSName;
	static String Manager1;
	static String Dnode;
	static String Node_Hostname;
	static String DestinationManager;
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
		Dnode =Settings.getDnode();
		Node_Hostname =Settings.getNode_Hostname();
		DestinationManager =Settings.getDestinationManager();
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
		Thread.sleep(2000);
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
		
		
		/*driver.findElement(By.id("createInitialViewlets")).click();
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByIndex(Integer.parseInt(WGS_INDEX));*/
		
		/*//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(MediumSleep);
	}
	
	@Test(priority=1)
	@TestRail(testCaseId=1033)
    @Parameters({"ViewletName", "ViewletValue"})
	public static void AddCommInfoViewlet(String ViewletName, int ViewletValue, ITestContext context) throws InterruptedException
	{
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		
		if(driver.getPageSource().contains(ViewletName))
		{
			System.out.println("Comm info Viewlet is created");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Comm info viewlet is created successfully");
		}
		else
		{
			System.out.println("Comm inof viewlet is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create Comm info viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}			
    }
	
	@TestRail(testCaseId = 1034)
	@Parameters({"Dashboardname", "SchemaName"})
	@Test(priority=20)
	public void ShowObjectAttributes(String Dashboardname, String SchemaName, ITestContext context)
	{
		try {
			//Objects Verification
			ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
			obj.CommunicationInfoAttributes(Dashboardname, driver, SchemaName, WGSName);
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Object attributes working fine");
			}
			catch(Exception e)
			{
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Exception occured while showing object attributes, Check details: " + e.getMessage());
				driver.findElement(By.id("Objects verification failed")).click();
				driver.findElement(By.id("Attributes failed")).click();
			}
	}
	
	@TestRail(testCaseId = 1035)
	@Test(priority=2)
	public void Properties(ITestContext context) throws InterruptedException
	{
		//Click on clear all check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(LowSleep);
		
		//Select properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(LowSleep);
				
		//Store the editable function in to a string
		boolean FieldNamevalue=driver.findElement(By.id("name")).isEnabled();
		System.out.println(FieldNamevalue);
		
		//Verification
		if(FieldNamevalue == false)
		{
			 System.out.println("Communication info Name field is UnEditable");
			 context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Communication info Name field is UnEditable, condition working fine");
				
			 driver.findElement(By.cssSelector(".btn-primary")).click();
			 Thread.sleep(MediumSleep);
		}
		else
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Condition failed, Communication info name is in editable");
			System.out.println("Communication info Name field is Editable");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(MediumSleep);
			driver.findElement(By.xpath("Communication info name edit function Failed")).click();
			
		}
		Thread.sleep(1000);
		
	}
	
	@TestRail(testCaseId = 1036)
	@Parameters({"CopyAsName"})
	@Test(priority=3)
	public void CopyAsOfCommumicationInfo(String CopyAsName, ITestContext context) throws InterruptedException
	{
		//Click on clear all check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(LowSleep);
		
		//Select Copy as From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	driver.findElement(By.linkText("Copy As...")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Give the copy as name
    	driver.findElement(By.id("name")).clear();
    	driver.findElement(By.id("name")).sendKeys(CopyAsName);
    	Thread.sleep(LowSleep);
		
    	//Click on OK button
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(LowSleep);
    	}
    	
    	//Search with the copy as name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyAsName);
    	Thread.sleep(LowSleep);
    	
    	//Store the viewlet data into string
    	String CommInfoviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	//System.out.println(CommInfoviewlet);
    	
    	//Edit the search field data
    	for(int j=0; j<=CopyAsName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(LowSleep);
    	
    	//Verification condition
    	if(CommInfoviewlet.contains(CopyAsName))
    	{
    		System.out.println("Communication info is copied");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Communication info is copied using CopyAs");
    	}
    	else
    	{
    		System.out.println("Communication info is not copied");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to copy Communication info using CopyAs");
    		driver.findElement(By.xpath("Communication info failed to copy")).click();
    	}
    	Thread.sleep(1000);	
    	
	}
	
	@TestRail(testCaseId = 1037)
	@Parameters({"CopyAsName"})
	@Test(priority=4,dependsOnMethods= {"CopyAsOfCommumicationInfo"})
	public void DeleteCommunicationInfo(String CopyAsName, ITestContext context) throws InterruptedException
	{
		//Click on clear all check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(LowSleep);
		
		//Search with the copy as name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyAsName);
    	Thread.sleep(LowSleep);
    	
    	//Select Delete From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	driver.findElement(By.linkText("Delete")).click();
    	Thread.sleep(LowSleep);
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(LowSleep);
    	}
    	
    	//Store the viewlet data into string
    	String CommInfoviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	//System.out.println(CommInfoviewlet);
    	
    	//Search with the new name
		for(int j=0; j<=CopyAsName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(LowSleep);
    	
    	//Verification of Subscription delete
    	if(CommInfoviewlet.contains(CopyAsName))
    	{
    		System.out.println("Communication info is not deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete Communication info using delete command");
    		driver.findElement(By.xpath("Communication info delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Communication info is deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Communication info is deleted using delete command");
    	}
    	Thread.sleep(1000);
	}
	
	@TestRail(testCaseId = 1038)
	@Parameters({"FavoriteViewletName"})
	@Test(priority=5)
	public void AddCommunicationInfoToFavoriteViewlet(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{		
		//Store Communication info name into string
		String CommInfoName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
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
		
		//Click on clear all check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(LowSleep);
		
		//click on checkbox and choose to Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(LowSleep);
		
		//Select favorite viewlet
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
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification of listener added to favorite viewlet
		if(Favdata.contains(CommInfoName))
		{
			System.out.println("Communication info name is added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "The Communication info name is added to Favorite viewlet");
		}
		else
		{
			System.out.println("Communication info name is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add Communication info name to Favorite viewlet");
		
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@TestRail(testCaseId = 1039)
	@Parameters({"Dashboardname"})
	@Test(priority=6)
	public void CompareCommunicationInfos(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Click on clear all check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(LowSleep);
		
		//Get the First object Name
		String compare1 = driver.findElement(By.xpath("//datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("First obj name is: " +compare1);
		
		//Get the second object name
		String compare2 = driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Second obj name is: " +compare2);
		Thread.sleep(2000);
		
		// Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		
		// System.out.println("Cpmare to: " + compare1 + "::"+ compare2);
		String comparenameslist = compare1  +" Attribute Value"+ "::"+ compare2  +" Attribute Value";
		driver.findElement(By.linkText("Compare")).click();
		Thread.sleep(LowSleep);
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
	
	@TestRail(testCaseId = 1040)
	@Parameters({"Dashboardname"})
	@Test(priority=7)
	public void DifferencesOfCommunicationInfos(String Dashboardname, ITestContext context) throws InterruptedException
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
		Thread.sleep(LowSleep);
		
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
				System.out.println("classname: "+ cls);
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
	
	@TestRail(testCaseId = 1041)
	@Parameters({"FavoriteViewletName"})
	@Test(priority=8)
	public void AddMultipleCommumicationInfosToFavoriteViewlet(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{		
		//Store the Comm info into strings 
		String CommInfo2=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		String CommInfo3=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Click on clear all check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(LowSleep);
		
		//Select Two Comm info and choose Add to favorite viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(LowSleep);
			
		//Select favorite viewlet
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
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification of listeners added to favorite to favorite viewlet
		if(Favdata.contains(CommInfo2) && Favdata.contains(CommInfo3))
		{
			System.out.println("Multiple Communicatio info names are added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple Communicatio info properties added successfully to Favorite viewlet");
		}
		else
		{
			System.out.println("Multiple Communicatio info names are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add Multiple Communicatio info properties to Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);	
	}
	
	@TestRail(testCaseId = 1042)
	@Parameters({"NewCommunicationInfoName", "GroupAddress", "CommInfoPort", "MessageHistory"})
	@Test(priority=10)
	public void AddCommunicationInfoUsingPlusIcon(String NewCommunicationInfoName, String GroupAddress, String CommInfoPort, String MessageHistory, ITestContext context) throws InterruptedException
	{
		//Click on clear all check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(LowSleep);
		
		try
		{
		//Click on + Icon for creating the process
		driver.findElement(By.xpath("//img[@title='Add CommInfo']")).click();
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
				if(mdi.getText().equals(DestinationManager))
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
		Thread.sleep(LowSleep);
		
		//Enter the Communication info name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(NewCommunicationInfoName);
		Thread.sleep(LowSleep);
		
		//Enter the Group address
		driver.findElement(By.id("grpAddress")).clear();
		driver.findElement(By.id("grpAddress")).sendKeys(GroupAddress);
		Thread.sleep(LowSleep);
		
		//Enter the Comm info port number
		driver.findElement(By.id("port")).clear();
		driver.findElement(By.id("port")).sendKeys(CommInfoPort);
		Thread.sleep(LowSleep);
		
		//Enter message history value
		driver.findElement(By.id("msgHistory")).clear();
		driver.findElement(By.id("msgHistory")).sendKeys(MessageHistory);
		Thread.sleep(LowSleep);
		
		//Click on Submit the process
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(MediumSleep);
				
		try
		{
			driver.findElement(By.id("yes")).click();
			driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
			Thread.sleep(LowSleep);
			try
			{
				driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
				Thread.sleep(LowSleep);
			}
			catch (Exception e1)
			{
				
			}
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
		
		//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(LowSleep);
    	}
		
		//Search with the added Comm info name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(NewCommunicationInfoName);
    	Thread.sleep(LowSleep);
		
		//Store the Comm info viewlet data into string
		String CommInfodata=driver.findElement(By.xpath("//datatable-body")).getText();
		
		//Edit the search field data
    	for(int j=0; j<=NewCommunicationInfoName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
		
		//Verification 
		if(CommInfodata.contains(NewCommunicationInfoName))
		{
			System.out.println("Communication info is created from the Icon option");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Communication info viewlet is created successfully using add icon");
		}
		else
		{
			System.out.println("Communication info is not created from the Icon option");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to create Communication info viewlet using add icon");
			//driver.findElement(By.cssSelector(".btn-danger")).click();
			driver.findElement(By.xpath("Communication info creation failed")).click();
		}
		}
		
		catch (Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception while creating the Communication info, check details: "+ e.getMessage());
			System.out.println("Unable to create the Communication info");
			driver.findElement(By.xpath("Communication info creation failed")).click();
		}
		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=21)
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
