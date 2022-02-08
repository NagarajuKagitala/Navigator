package Tickets;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Common.ClearSelectionofCheckbox;
import Common.Dashboard;
import Common.LogoutForAll;
import Common.Viewlets;
import testrail.Settings;

public class ResolvedIssues 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName;
	static String Q_QueueName;
	static String Dnode;
	static String DestinationManager;
	static String FinalQueuename="";
	static String Node_Hostname;
	
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
		
		//Login page
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(20000);
		
		//Delete if dashboard exists with same name
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboard(driver, Dashboardname);
		
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);
		
		//--------- Create Queue viewlet-----------
		//Click on Viewlet		
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		Thread.sleep(4000);
		
		 //Restore Default settings
		  driver.findElement(By.cssSelector(".fa-cog")).click();
		  driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click(); 
		  Thread.sleep(4000);
		  driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		  Thread.sleep(4000);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=1)
	public void CustomProeprties23999(String Dashboardname) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//open properties of First Queue
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Go to custom properties tab
		driver.findElement(By.linkText("Custom Attributes")).click();
		Thread.sleep(2000);
		
		//Add custom property
		driver.findElement(By.xpath("//div[3]/div/input")).sendKeys("CustomAttribute");
		driver.findElement(By.xpath("//div[3]/div[2]/input")).sendKeys("FirstQueue");
		
		//Click on Add button
		driver.findElement(By.xpath("//button[contains(.,'Add')]")).click();
		Thread.sleep(2000);
		
		//Click on ok button
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(4000);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//open properties of Second Queue
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Go to custom properties tab
		driver.findElement(By.linkText("Custom Attributes")).click();
		Thread.sleep(2000);
		
		//Add custom property
		driver.findElement(By.xpath("//div[3]/div/input")).sendKeys("CustomAttribute");
		driver.findElement(By.xpath("//div[3]/div[2]/input")).sendKeys("SecondQueue");
		
		//Click on Add button
		driver.findElement(By.xpath("//button[contains(.,'Add')]")).click();
		Thread.sleep(2000);
		
		//Click on ok button
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(4000);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
		che2.Deselectcheckbox(Dashboardname, driver);
		
		//open attributes of two queues
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(3000);
		
		//Go to custom properties tab
		driver.findElement(By.linkText("Custom Attributes")).click();
		Thread.sleep(2000);
		
		WebElement ele=driver.findElement(By.id("value_0"));
		Actions a=new Actions(driver);
		a.moveToElement(ele).perform();
	
		String Tooltipdata=driver.findElement(By.tagName("ngb-tooltip-window")).getText();
		System.out.println("Tooltip data is: "+Tooltipdata);
		
				
		//Click on ok button
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		
		if(Tooltipdata.contains("FirstQueue") && Tooltipdata.contains("SecondQueue"))
		{
			System.out.println("Custom properties working fine");
		}
		else
		{
			System.out.println("Custom properties not working");
			driver.findElement(By.id("23999 failed")).click();
		}
		
	}
	
	
	@Parameters({"Dashboardname"})
	@Test(priority=2)
	public void MaxSearchRes23469(String Dashboardname) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
		che2.Deselectcheckbox(Dashboardname, driver);
		
		driver.findElement(By.cssSelector(".fa-cog")).click();
		
		//click on Edi global settings
		driver.findElement(By.xpath("//button[contains(.,'Edit global settings')]")).click();
		Thread.sleep(2000);
		
		//get the result data into string
		String Result=driver.findElement(By.xpath("//legend")).getText();
		System.out.println("Results are :" +Result);
		
		//click on cancel button
		driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
		Thread.sleep(3000);
		
		if(Result.contains("Default Search Settings"))
		{
			System.out.println("23469 working fine");
		}
		else
		{
			System.out.println("23469 working fine");
			driver.findElement(By.id("23469 failed")).click();
		}
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=30)
	public static void Logout(String Dashboardname) throws Exception
	{
		// Changing the Settings 
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(3000);
		
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, Dashboardname);
	}

}
