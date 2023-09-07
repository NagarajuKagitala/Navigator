package EMS;

import java.io.File;
import java.util.List;

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

import Common.Dashboard;
import Common.LogoutForAll;
import Common.Viewlets;

import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class EMSJNDI 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String DownloadPath;
	static String UploadFilepath;
	static String UploadLargeFile;
	static String EMS_WGSNAME;
	static String EMSNode;
	static String Low;
	static String Medium;
	static String High;
	static int LowSleep;
	static int MediumSleep;
	static int HighSleep;
	
	@BeforeTest
	public void beforeTest() throws Exception 
	{
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		DownloadPath =Settings.getDownloadPath();
		UploadFilepath =Settings.getUploadFilepath();
		EMS_WGSNAME =Settings.getEMS_WGSNAME();
		UploadLargeFile =Settings.getUploadLargeFile();
		EMSNode =Settings.getEMSNode();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
	}
	
	@Parameters({"sDriver","sDriverpath","dashboardname"})
	@Test(priority=1)
	public static void Login(String sDriver,  String  sDriverpath, String dashboardname) throws Exception
	{
		Settings.read();
		String URL=Settings.getSettingURL();
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
		
		//for maximize window
		driver.manage().window().maximize();
		
		//for entering URL
		driver.get(URL);
		Thread.sleep(5000);
		
		//for entering login credentials
		
		driver.findElement(By.id("username")).clear();
		Thread.sleep(3000);
		driver.findElement(By.id("username")).sendKeys(uname);
		Thread.sleep(2000);
		driver.findElement(By.id("password")).sendKeys(password);
		Thread.sleep(3000);
		
		//clicking login button
		driver.findElement(By.name("submit")).click();
		Thread.sleep(10000);
		
		//for deleting the existing dashboard
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboard(driver, dashboardname);	
		
		//Create dashboard
		driver.findElement(By.id("add-tab-plus")).click();
		Thread.sleep(3000);
		driver.findElement(By.id("dashboardName")).sendKeys(dashboardname);
		Thread.sleep(4000);
		driver.findElement(By.name("add-tab")).click();
		Thread.sleep(4000);
		
	}
	
	
	
	@Parameters({"ViewletValue", "ViewletName"})
	@Test(priority=2)
	public static void CreateViewlet(int ViewletValue, String ViewletName, ITestContext context) throws InterruptedException
	{
		//Create viewlet
		Viewlets ob=new Viewlets();
		ob.EMSViewlet(driver, ViewletValue, ViewletName, EMS_WGSNAME, EMSNode);
		
		//verification
		if(driver.getPageSource().contains(ViewletName))
		{
			System.out.println("JNDI Viewlet is created");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "JNDI viewlet is created successfully");
		}
		else
		{
			System.out.println("JNDI viewlet is not created");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Faile to add JNDI viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
		
	}
	
	@Parameters({"JNDIname"})
	@Test(priority=4)
	public static void CreateJNDIplusicon(String JNDIname, ITestContext context) throws InterruptedException
	{
		driver.findElement(By.id("add-object")).click();
		Thread.sleep(3000);
		
		//for selecting WGS dropdown
		/*driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/ng-select/div/span")).click();
		Thread.sleep(3000);
		
		WebElement a=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement>b=a.findElements(By.tagName("div"));
		
		System.out.println("number of divs:"+b.size());
		for(WebElement c:b)
		{
			System.out.println("list of divs:"+c.getText());
			if(c.getText().equalsIgnoreCase(EMS_WGSNAME))
			{
				c.click();
				Thread.sleep(5000);
				break;
			}
			
		}*/
		//for selecting EMS Server dropdown
		driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(3000);
		
		WebElement l=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement>m=l.findElements(By.tagName("div"));
		
		System.out.println("number of divs:"+m.size());
		for(WebElement n:m)
		{
			System.out.println("list of divs:"+n.getText());
			if(n.getText().equalsIgnoreCase(EMSNode))
			{
				n.click();
				Thread.sleep(5000);
				break;
			}
			
		}
		driver.findElement(By.id("select-path")).click();
		Thread.sleep(3000);
		
		//for giving JNDI name
		driver.findElement(By.id("name")).sendKeys(JNDIname);
		Thread.sleep(4000);
		driver.findElement(By.id("url")).sendKeys("127.0.0.1");
		Thread.sleep(3000);
		
		//for clicking OK button
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(4000);
		
		try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
			Thread.sleep(3000);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(3000);
			
		}
		catch(Exception E)
		{
			System.out.println("object not exists");
		}
		
		//Search with name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(JNDIname);
		Thread.sleep(3000);
		
		//Get the result 
		String viewletData=driver.findElement(By.cssSelector(".datatable-body")).getText();
		//System.out.println("Viewlet data is: " +viewletData);
		
		for(int i=0; i<=JNDIname.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		if(viewletData.contains(JNDIname))
		{
			System.out.println("JNDI object is created using plus icon");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "JNDI object is created successfully");
		}
		else
		{
			System.out.println("JNDI object is not created using plus icon");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "JNDI object is not created");
    		driver.findElement(By.id("jndi object create failed")).click();
		}
	}
	
	@Parameters({"JNDIname"})
	@Test(priority=5)
	public static void Properties(String JNDIname, ITestContext context) throws InterruptedException
	{
		//search with the added jndi name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(JNDIname);
		Thread.sleep(3000);
		
		//for checking checkbox
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(3000);
		//click on properties
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(2000);
		
		boolean namefield=driver.findElement(By.id("name")).isEnabled();
		System.out.println("Name field is: " +namefield);
		
		//click on OK button
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(6000);
		
		//verification
		if(namefield==false)
		{
			System.out.println("Name field is disabled");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "JNDI properties working fine");
		}
		else
		{
			System.out.println("Name field is enabled");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "JNDI properties not working");
			driver.findElement(By.id("failed properties")).click();
		}
	}
	
	@Parameters({"favviewletname","JNDIname"})
	@Test(priority=6)
	public static void AddtoFavViewlet(String favviewletname, String JNDIname, ITestContext context) throws InterruptedException
	{
		//for clicking cross mark
		driver.findElement(By.xpath("//datatable-header-cell[2]")).click();
		Thread.sleep(3000); 
		
		//search with the added jndi name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(JNDIname);
		Thread.sleep(3000);
		
		//Get the object name
		String JNDIName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("jndi name is: " +JNDIname);
		
		//for checking checkbox
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//ul[2]/li")).click();
		Thread.sleep(2000);
		
		//click on create new viewlet button
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/button")).click();
		Thread.sleep(3000);
		driver.findElement(By.name("viewlet-name")).sendKeys(favviewletname);
		Thread.sleep(3000);
		
		//Deselect WGS  
		driver.findElement(By.xpath("//span[2]/i")).click();  
		Thread.sleep(LowSleep);
		//for wgs selection
		//driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div/div[2]/div/ng-select/div/span")).click();
		Thread.sleep(3000);
		
		WebElement a=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement>b=a.findElements(By.tagName("div"));
		
		System.out.println("number of divs:"+b.size());
		for(WebElement c:b)
		{
			System.out.println("list of divs:"+c.getText());
			if(c.getText().equalsIgnoreCase(EMS_WGSNAME))
			{
				c.click();
				Thread.sleep(5000);
				break;
			}
			
		}
		//click on OK button
		driver.findElement(By.xpath("(//button[@type='button'])[11]")).click();
		Thread.sleep(3000);
		
		//Select the created fav
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div/span")).click();
		Thread.sleep(3000); 
		
		WebElement drop=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div=drop.findElements(By.tagName("div"));
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement di : div)
		{
			//System.out.println("text is :" +di.getText());
			if(di.getText().equalsIgnoreCase(favviewletname))
			{
				di.click();
				break;
			}	
		}
		
		//click on save changes button
		driver.findElement(By.xpath("(//button[@type='button'])[9]")).click();
		Thread.sleep(6000);
		
		//Get the fav viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//verification
		if(Favdata.contains(JNDIname))
		{
			System.out.println("JNDI name is added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "JNDI viewlet is added successfully to Favorite viewlet");
		}
		else
		{
			System.out.println("JNDI name is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add JNDI viewlet to Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
	}
	
	@Parameters({"JNDIname"})
	@Test(priority=7)
	public static void DeleteEMSJNDI(String JNDIname, ITestContext context) throws InterruptedException
	{
		//for clicking cross mark
		driver.findElement(By.xpath("//datatable-header-cell[2]")).click();
		Thread.sleep(3000);
		
		//search with the added jndi name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(JNDIname);
		Thread.sleep(3000);
		
		//for checking checkbox
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(3000);
		
		//for mouse actions
		Actions a=new Actions(driver);
		a.moveToElement(driver.findElement(By.xpath("//li[contains(.,'Commands')]"))).perform();
		driver.findElement(By.xpath("(//li[@id=''])[2]")).click();
		Thread.sleep(3000);
		//for confirmation OK button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(5000);
		
		//get the viewlet data
		String viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println(viewletdata);
    	
    	//Clear the search data
    	for(int j=0; j<=JNDIname.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(1000);
    	
    	//verification
    	if(viewletdata.contains(JNDIname))
    	{
    		System.out.println("JNDIname is not deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete JNDIname viewlet using delete command");
    		driver.findElement(By.xpath("JNDIname delete failed")).click();
    	}
    	else
    	{
    		System.out.println("JNDIname is deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "JNDIname viewlet is deleted successfully using delete command");
    	}
		
		
	}
	
	
	
	@Parameters({"dashboardname"})
	@Test(priority=9)
	public static void Logout(String dashboardname) throws InterruptedException
	{
		//Delete dashboard
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboard(driver, dashboardname);
		
		//Logout
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, dashboardname);
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
