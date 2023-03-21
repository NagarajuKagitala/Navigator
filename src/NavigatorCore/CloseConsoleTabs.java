package NavigatorCore;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
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
public class CloseConsoleTabs 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName;
	static String Q_QueueName;
	static String Dnode;
	static String DestinationManager;
	static String FinalQueuename="";
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
		Q_QueueName =Settings.getQ_QueueName();
		Dnode =Settings.getDnode();
		DestinationManager =Settings.getDestinationManager();
		Node_Hostname =Settings.getNode_Hostname();
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
		else if(sDriver.equalsIgnoreCase("webdriver.gecko.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver= new FirefoxDriver();
		}
		else
		{
			driver = new HtmlUnitDriver();
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
		
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		Thread.sleep(MediumSleep); 
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(LowSleep);
		
		//--------- Create Queue viewlet-----------
		//Click on Viewlet		
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		Thread.sleep(MediumSleep);
		
	}
	
	@TestRail(testCaseId = 1049)
	@Test(priority=1)
	public void CloseOtherConsole(ITestContext context) throws InterruptedException
	{	
		//Get the queue name into string
		String Queuename=driver.findElement(By.xpath("//datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Queue name is: " +Queuename);
		
		//Select Browse Messages Option
		String[] Managers = new String[10];
		for(int i=0; i<10; i++)
		{
			int k=i+1;
			//Get the Queue names                     
		    Managers[i]=driver.findElement(By.xpath("//datatable-row-wrapper["+ k +"]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
			System.out.println("Queue is : " +Managers[i]);
			
			String FirstMr=Managers[0];
			//System.out.println("initial manager name is: " +FirstMr);
			if(i>0)
			{
				if(FirstMr.equalsIgnoreCase(Managers[i]))
				{
					System.out.println("Queues are matched");
					
				}
				else
				{
					System.out.println("Index values is: " +i);
					driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
					Thread.sleep(LowSleep); 
					driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper["+ k +"]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
					Thread.sleep(LowSleep);
					driver.findElement(By.linkText("Browse messages")).click();
					Thread.sleep(LowSleep); 
					break;
				}
			}
			
		}
		Thread.sleep(LowSleep);
		
		WebElement ele1=driver.findElement(By.className("block-tabs-left")).findElement(By.tagName("ul"));
		List<WebElement> lis1=ele1.findElements(By.tagName("li"));
		
		StringBuffer buffer=new StringBuffer();
		for(WebElement list1 : lis1)
		{
			WebElement div1=list1.findElement(By.tagName("div")).findElement(By.className("g-tab-title"));
			//System.out.println("Title is :" +div1.getAttribute("title"));
			buffer.append(div1.getAttribute("title"));
			buffer.append(",");
						
		}
		
		//List of consoles
		String Consoles=buffer.toString();
		System.out.println("List of Consoles are: " +Consoles);
		
		WebElement ele=driver.findElement(By.className("block-tabs-left")).findElement(By.tagName("ul"));
		List<WebElement> lis=ele.findElements(By.tagName("li"));
		
		for(WebElement list : lis)
		{
			WebElement div=list.findElement(By.tagName("div")).findElement(By.className("g-tab-title"));
			System.out.println("Title is :" +div.getAttribute("title"));
			
			if(div.getAttribute("title").equalsIgnoreCase(Queuename))
			{
				Actions a=new Actions(driver);
				a.contextClick(div).perform();
				Thread.sleep(MediumSleep);
				break;
			}
			
		}
		
		//Chose close other option
		driver.findElement(By.xpath("//li[2]/a/div")).click();
		Thread.sleep(MediumSleep);
		
		WebElement ele2=driver.findElement(By.className("block-tabs-left")).findElement(By.tagName("ul"));
		List<WebElement> lis2=ele2.findElements(By.tagName("li"));
		
		StringBuffer buffer1=new StringBuffer();
		for(WebElement list2 : lis2)
		{
			WebElement div2=list2.findElement(By.tagName("div")).findElement(By.className("g-tab-title"));
			//System.out.println("Title is :" +div2.getAttribute("title"));
			buffer1.append(div2.getAttribute("title"));
			//buffer1.append(",");
						
		}
		
		//List of consoles
		String AfterCloseConsoles=buffer1.toString();
		System.out.println("List of Consoles are: " +AfterCloseConsoles);
		
		//Verification
		if(AfterCloseConsoles.equalsIgnoreCase(Queuename))
		{
			System.out.println("Console Close other is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Console Close other is working fine");
		}
		else
		{
			System.out.println("Console Close other is  not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Console Close other is  not working");
			driver.findElement(By.id("Close other failed")).click();
		}
		
	}
	
	@TestRail(testCaseId = 1050)
	@Test(priority=2)
	public void CloseAllConsole(ITestContext context) throws InterruptedException
	{
		
		WebElement Console=driver.findElement(By.id("consoleBtn"));
		System.out.println("Console before :" +Console.getText());
				
		WebElement ele=driver.findElement(By.className("block-tabs-left")).findElement(By.tagName("ul"));
		List<WebElement> lis=ele.findElements(By.tagName("li"));
		
		for(WebElement list : lis)
		{
			WebElement div=list.findElement(By.tagName("div")).findElement(By.className("g-tab-title"));
			System.out.println("Title is :" +div.getAttribute("title"));
			
			Actions a=new Actions(driver);
			a.contextClick(div).perform();
			Thread.sleep(MediumSleep);
			break;
			
		}
		
		//Chose close All option
		driver.findElement(By.linkText("Close All")).click();
		Thread.sleep(MediumSleep);
		
		System.out.println("Console after close all :" +Console.getText());
		String AfterCloseConsoles=Console.getText();
				
		//Verification
		if(AfterCloseConsoles.equalsIgnoreCase("Console"))
		{
			System.out.println("Console Close All is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Console Close All is working fine");
		}
		else
		{
			System.out.println("Console Close All is  not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Console Close All is  not working");
			driver.findElement(By.id("Close All failed")).click();
		}
		
	}
	
	
	@Parameters({"Dashboardname"})
	@Test(priority=30)
	public static void Logout(String Dashboardname) throws Exception
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
