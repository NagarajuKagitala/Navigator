package NavigatorCore;


import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
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

import Common.Dashboard;
import Common.LogoutForAll;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class DashboardOptions 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName_CreateDashboard;
	static String EMSWGS_CreateDashboard;
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
		WGSName_CreateDashboard =Settings.getWGSName_CreateDashboard();
		EMSWGS_CreateDashboard =Settings.getEMSWGS_CreateDashboard();
		Node_Hostname =Settings.getNode_Hostname();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
	}
		
	@Parameters({"sDriver", "sDriverpath", "EMSDashboardname"})
	@Test
	public void Login(String sDriver, String sDriverpath, String EMSDashboardname) throws Exception
	{
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		//Select the required browser for running the script
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new ChromeDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.edge.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new EdgeDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.ie.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new InternetExplorerDriver();
		}
		else
		{
			System.setProperty(sDriver, sDriverpath);
			FirefoxOptions options=new FirefoxOptions();
			options.setCapability("marionette", false);
			driver=new FirefoxDriver(options);
		}
		
		//Enter the URL into browser and Maximize the browser 
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		
		//Login
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(HighSleep);	
		
		Dashboard object=new Dashboard();
		object.DeleteExistDashboardForDashboardOptions(driver, EMSDashboardname);
	}
	
	@TestRail(testCaseId=754)
	@Parameters({"Dashboardname"})
	@Test(priority=1)
	public void CreateWGSDashboard(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		driver.findElement(By.id("iv_yes")).click();
		Thread.sleep(MediumSleep);
		
		driver.findElement(By.xpath("//div[3]/div/div[2]/ng-select/div/span")).click();
		
		WebElement drop=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div=drop.findElements(By.tagName("div")); 
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement ele:div)
		{			
			if(ele.getText().equalsIgnoreCase(WGSName_CreateDashboard))
			{
					ele.click();
					Thread.sleep(2000);
					break;
				
			}
		}
		
		
		/*
		 * //Work group server selection Select dd=new
		 * Select(driver.findElement(By.xpath("//div[2]/ng-select/div/div")));
		 * Thread.sleep(2000); dd.selectByVisibleText(WGSName_CreateDashboard);
		 * Thread.sleep(4000);
		 */
				
		/*//Select Node
		driver.findElement(By.name("node")).click();
		driver.findElement(By.name("node")).sendKeys(Node);
		
		//Select QM
		driver.findElement(By.name("queueMngr")).click();
		driver.findElement(By.name("queueMngr")).sendKeys(QueueManager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[contains(.,'Create')]")).click();
		Thread.sleep(MediumSleep);
		
		WebElement cla=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis=cla.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis.size());
		
		StringBuffer buffer=new StringBuffer();
		for(WebElement li: lis)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi=li.findElement(By.className("g-tab-title"));
			//System.out.println("Names are: " +fi.getText());
			buffer.append(fi.getText());
			buffer.append(",");
		}
		
		String DashboardNames=buffer.toString();
		System.out.println("List of dashboards are: " +DashboardNames);
		
		if(DashboardNames.contains(Dashboardname))
		{
			System.out.println("Dashboard is created successfully");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Dashboard is created successfully");
		}
		else
		{
			System.out.println("Dashboard is not created");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to create Dashboard");
			driver.findElement(By.id("verification failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId=755)
	@Parameters({"EMSDashboardname"})
	@Test(priority=2)
	public void CreateEMSDashboard(String EMSDashboardname, ITestContext context) throws InterruptedException
	{
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(EMSDashboardname);
		driver.findElement(By.id("iv_yes")).click();
		Thread.sleep(4000);
		
		driver.findElement(By.xpath("//div[3]/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(3000);
		
		WebElement drop=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div=drop.findElements(By.tagName("div")); 
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement ele:div)
		{			
			if(ele.getText().equalsIgnoreCase(EMSWGS_CreateDashboard))
			{
					ele.click();
					Thread.sleep(2000);
					break;
				
			}
		}
		
		/*
		 * //Work group server selection Select dd=new
		 * Select(driver.findElement(By.xpath("//div[2]/ng-select/div/div")));
		 * Thread.sleep(2000); dd.selectByVisibleText(EMSWGS_CreateDashboard);
		 */
		
		/*//Select node
		driver.findElement(By.name("node")).click();
		driver.findElement(By.name("node")).sendKeys(EMSNode);
		
		//Select manager
		driver.findElement(By.name("queueMngr")).click();
		driver.findElement(By.name("queueMngr")).sendKeys(EMSQueueManager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[contains(.,'Create')]")).click();
		Thread.sleep(MediumSleep);
		
		WebElement cla=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis=cla.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis.size());
		
		StringBuffer buffer=new StringBuffer();
		for(WebElement li: lis)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi=li.findElement(By.className("g-tab-title"));
			//System.out.println("Names are: " +fi.getText());
			buffer.append(fi.getText());
			buffer.append(",");
		}
		
		String DashboardNames=buffer.toString();
		System.out.println("List of dashboards are: " +DashboardNames);
		
		if(DashboardNames.contains(EMSDashboardname))
		{
			System.out.println("EMS Dashboard is created successfully");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "EMS Dashboard is created successfully");
		}
		else
		{
			System.out.println("EMS Dashboard is not created");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to create EMS Dashboard");
			driver.findElement(By.id("EMS Verification failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId=756)
	@Parameters({"Dashboardname"})
	@Test(priority=3)
	public void SetAsDeafaultOption(String Dashboardname, ITestContext context) throws Exception
	{
		
		Settings.read();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		WebElement cla1=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis1=cla1.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis1.size());
		
		for(WebElement li1: lis1)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi1=li1.findElement(By.className("g-tab-title"));
			System.out.println("Names are: " +fi1.getText());
			
			if(fi1.getText().equalsIgnoreCase(Dashboardname))
			{
				 WebElement element=fi1;
				 JavascriptExecutor js = (JavascriptExecutor)driver;
				 js.executeScript("arguments[0].click();", element);
				 Thread.sleep(5000);
				 Actions a=new Actions(driver);
				 a.contextClick(element).perform();
				break;
			} 
		}
				
		//Click on Set as default
		driver.findElement(By.linkText("Set as default")).click();
		Thread.sleep(MediumSleep);
		
		//Click on signout button
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		driver.findElement(By.id("yesButton")).click();
		
		this.login(uname, password);
		Thread.sleep(MediumSleep);
		
		WebElement cla2=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis2=cla2.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis2.size());
		
		for(WebElement li2: lis2)
		{
			//System.out.println("titles are: " +li1.getAttribute("class"));
			
			if(li2.getAttribute("class").contains("active"))
			{
				WebElement fi3=li2.findElement(By.className("g-tab-title"));
				//System.out.println("Names are: " +fi2.getText());
				
				if(fi3.getText().equalsIgnoreCase(Dashboardname))
				{
					System.out.println("Default dashboard is set ");
					context.setAttribute("Status", 1);
					context.setAttribute("Comment", "set Dashboard as default successfully");
					break;
				}
				else
				{
					System.out.println("Default dashboard is not set ");
					this.SetWorkspaceAsDefault();
					context.setAttribute("Status", 5);
					context.setAttribute("Comment", "Failed to set default Dashboard");
					driver.findElement(By.id("default dashbord failed")).click();
					break;
				}
			}
			//System.out.println("Names are: " +fi.getText());
		}
		
		//Set again Work space as default
		this.SetWorkspaceAsDefault();
	}
	
	@TestRail(testCaseId=992)
	@Test(priority=4)
	@Parameters({"Dashboardname"})
	public void SetASDefaultTemplate(String Dashboardname, ITestContext context) throws InterruptedException
	{
		WebElement cla1=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis1=cla1.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis1.size());
		
		for(WebElement li1: lis1)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi1=li1.findElement(By.className("g-tab-title"));
			System.out.println("Names are: " +fi1.getText());
			
			if(fi1.getText().equalsIgnoreCase(Dashboardname))
			{
				 WebElement element=fi1;
				 JavascriptExecutor js = (JavascriptExecutor)driver;
				 js.executeScript("arguments[0].click();", element);
				 Actions a=new Actions(driver);
				 a.contextClick(element).perform();
				 break;
			} 
		}
		
		//Select set as template option
		driver.findElement(By.linkText("Set as dashboard template")).click();
		Thread.sleep(LowSleep);
		
		//click on Add dashboard icon
		driver.findElement(By.cssSelector(".btn-add")).click();
		
		//Click on yes checkbox
		driver.findElement(By.id("iv_yes")).click();
		Thread.sleep(LowSleep);
		
		//click on viewlets template dropdown
		driver.findElement(By.xpath("//div[3]/div/div/ng-select/div/span")).click();
		
		WebElement ChannelauthNode=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> divs=ChannelauthNode.findElements(By.tagName("div"));
		System.out.println(divs.size());
		
		StringBuffer buffer=new StringBuffer();
		for (WebElement di : divs)
		{
			buffer.append(di.getText());
			buffer.append(",");
		}
				
		String Templates=buffer.toString();
		System.out.println("List of templates are: " +Templates);
		
		//click on cancel button
		driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
		Thread.sleep(LowSleep);
		
		if(Templates.contains(Dashboardname))
		{
			System.out.println("Set as default is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "set as default template working");
		}
		else
		{
			System.out.println("Set as default is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "set as default template not working");
			driver.findElement(By.id("Set as default failed")).click();
		}
	}
	
	@TestRail(testCaseId=757)
	@Test(priority=5)
	@Parameters({"NewDashboardName", "Dashboardname"})
	public void RenameDashboard(String NewDashboardName, String Dashboardname, ITestContext context) throws InterruptedException
	{
		WebElement cla1=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis1=cla1.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis1.size());
		
		for(WebElement li1: lis1)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi1=li1.findElement(By.className("g-tab-title"));
			//System.out.println("Names are: " +fi.getText());
			
			if(fi1.getText().equalsIgnoreCase(Dashboardname))
			{
				 WebElement element=fi1;
				 JavascriptExecutor js = (JavascriptExecutor)driver;
				 js.executeScript("arguments[0].click();", element);
				 Actions a=new Actions(driver);
				 a.contextClick(element).perform();
				break;
			} 
		}
		
		//Click on rename
		driver.findElement(By.linkText("Rename")).click();
		Thread.sleep(LowSleep);
		
		//Give the new name of dashboard
		driver.findElement(By.name("tabName")).clear();
		driver.findElement(By.name("tabName")).sendKeys(NewDashboardName);
		
		//Click on Ok button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
		
		WebElement cla2=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis2=cla2.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis2.size());
		
		StringBuffer buffer=new StringBuffer();
		for(WebElement li2: lis2)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi2=li2.findElement(By.className("g-tab-title"));
			//System.out.println("Names are: " +fi.getText());
			buffer.append(fi2.getText());
			buffer.append(",");
		}
		
		String DashboardNames=buffer.toString();
		System.out.println("List of dashboards are: " +DashboardNames);
		
		if(DashboardNames.contains(NewDashboardName))
		{
			System.out.println("Dashboard is Renamed");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Dashboard is renamed successfully");
		}
		else
		{
			System.out.println("Dashboard is not Renamed");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to rename Dashboard");
			driver.findElement(By.id("Rename failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId=796)
	@Parameters({"NewDashboardName"})
	@Test(priority=6)
	public void DeleteDashboardUsingCrossIcon(String NewDashboardName, ITestContext context) throws InterruptedException
	{
		//Delete the dashboard 
		try
		{
			driver.findElement(By.cssSelector(".active .g-tab-btn-close-block")).click();
			//driver.findElement(By.cssSelector(".fa-times")).click();
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(HighSleep);
			
			WebElement cla1=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
			List<WebElement> lis1=cla1.findElements(By.tagName("li"));
			System.out.println("No of dashboards are: " +lis1.size());
			
			StringBuffer buffer=new StringBuffer();
			for(WebElement li1: lis1)
			{
				//System.out.println("titles are: " +li.getAttribute("class"));
				WebElement fi1=li1.findElement(By.className("g-tab-title"));
				//System.out.println("Names are: " +fi.getText());
				buffer.append(fi1.getText());
				buffer.append(",");
			}
			
			String DashboardNames=buffer.toString();
			System.out.println("List of dashboards are: " +DashboardNames);
			
			if(DashboardNames.contains(NewDashboardName))
			{
				System.out.println("Dashboard is not Deleted");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to Delete Dashboard");
				driver.findElement(By.id("Delete failed")).click();
			}
			else
			{
				System.out.println("Dashboard is Deleted");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Dashboard is deleted successfully");
			}
		}
		catch (Exception e)
		{
			System.out.println("Dashboards are not present");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to Delete Dashboard");
			driver.findElement(By.id("Delete failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@TestRail(testCaseId=1147)
	@Parameters({"EMSDashboardname"})
	@Test(priority=7)
	public void DeleteDashboardUsingDeleteOption(String EMSDashboardname, ITestContext context) throws InterruptedException
	{
		WebElement cla=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis=cla.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis.size());
		
		for(WebElement li: lis)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi=li.findElement(By.className("g-tab-title"));
			//System.out.println("Names are: " +fi.getText());
			
			if(fi.getText().equalsIgnoreCase(EMSDashboardname))
			{
				Actions a=new Actions(driver);
				a.contextClick(fi).perform();
				Thread.sleep(5000);
				break;
			}
		}
		
		//Click on delete
		driver.findElement(By.linkText("Delete Dashboard")).click();
		Thread.sleep(LowSleep);
		
		//Click on confirmation yes button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(MediumSleep);
		
		try
		{
			//click on dashboard button
			driver.findElement(By.id("add-dashboard")).click();
			Thread.sleep(4000);
			
		WebElement cla1=driver.findElement(By.tagName("app-mod-manage-dashboard"));
		List<WebElement> lis1=cla1.findElements(By.tagName("div"));
		System.out.println("No of dashboards are: " +lis.size());
		
		StringBuffer buffer=new StringBuffer();
		for(WebElement li1: lis1)
		{
			//System.out.println("innet: " +li.getAttribute("innerHTML"));
			String clas1=li1.getAttribute("class");
			//System.out.println("class name : " +clas);
			
			if(clas1.contains("maf-container"))
			{
				WebElement lef1=li1.findElement(By.className("left-side")).findElement(By.tagName("table"));
				List<WebElement> trs1=lef1.findElements(By.tagName("tr"));
				System.out.println("number of trs: " +trs1.size());
				
				for(WebElement fi1:trs1)
				{
					buffer.append(fi1.getText());
					buffer.append(",");
				}
				break;
			}	
		}
		
		String DashboardNames=buffer.toString();
		System.out.println("List of dashboards are: " +DashboardNames);
		
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
		Thread.sleep(4000);
		
		if(DashboardNames.contains(EMSDashboardname))
		{
			System.out.println("Dashboard is not Deleted using option");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to Delete Dashboard using option");
			driver.findElement(By.id("Delete failed")).click();
		}
		else
		{
			System.out.println("Dashboard is Deleted using option");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Dashboard is deleted successfully using option");
		}
		
		}
		
		catch(Exception e)
		{
			System.out.println("Dashboard is not existing with same name");
			driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
		}
		
	}
	
	@TestRail(testCaseId=1148)
	@Parameters({"NewDashboardName"})
	@Test(priority=8)
	public void RemoveDashboardUsingOption(String NewDashboardName, ITestContext context) throws InterruptedException
	{
		//click on dashboard button
		driver.findElement(By.id("add-dashboard")).click();
		Thread.sleep(4000);
		
		WebElement cla=driver.findElement(By.tagName("app-mod-manage-dashboard"));
		List<WebElement> lis=cla.findElements(By.tagName("div"));
		System.out.println("No of dashboards are: " +lis.size());
		
		for(WebElement li: lis)
		{
			//System.out.println("innet: " +li.getAttribute("innerHTML"));
			String clas=li.getAttribute("class");
			//System.out.println("class name : " +clas);
			
			if(clas.contains("maf-container"))
			{
				WebElement lef=li.findElement(By.className("left-side")).findElement(By.tagName("table"));
				List<WebElement> trs=lef.findElements(By.tagName("tr"));
				System.out.println("number of trs: " +trs.size());
				
				for(WebElement fi:trs)
				{
					System.out.println("Dashboard names are: " +fi.getText());
					if(fi.getText().equalsIgnoreCase(NewDashboardName))
					{
						Actions a=new Actions(driver);
						a.click(fi).perform();
						Thread.sleep(5000);
						
						//click on add to current view button
						driver.findElement(By.xpath("//button[contains(.,' Add To Current View')]")).click();
						Thread.sleep(6000);
						break;
					}
				}
				break;
			}
		}
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
		Thread.sleep(4000);
		
		//select dashboard and choose remove option
		WebElement cla1=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis1=cla1.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis1.size());
		
		for(WebElement li1: lis1)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi1=li1.findElement(By.className("g-tab-title"));
			//System.out.println("Names are: " +fi.getText());
			
			if(fi1.getText().equalsIgnoreCase(NewDashboardName))
			{
				WebElement element=fi1;
				JavascriptExecutor js = (JavascriptExecutor)driver;
				js.executeScript("arguments[0].click();", element);
				Thread.sleep(5000);
				 
				Actions a=new Actions(driver);
				a.contextClick(element).perform();
				Thread.sleep(5000);
				break;
			}
		}
		
		//choose remove option
		driver.findElement(By.linkText("Remove Dashboard")).click();
		Thread.sleep(MediumSleep);
		
		//click on confirmation yes button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(MediumSleep);
		
		WebElement cla2=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis2=cla2.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis2.size());
		
		StringBuffer buffer=new StringBuffer();
		for(WebElement li2: lis2)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi2=li2.findElement(By.className("g-tab-title"));
			//System.out.println("Names are: " +fi.getText());
			buffer.append(fi2.getText());
			buffer.append(",");
		}
		
		String DashboardNames=buffer.toString();
		System.out.println("List of dashboards are: " +DashboardNames);
		
		if(DashboardNames.contains(NewDashboardName))
		{
			System.out.println("Dashboard is not removed");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to remove Dashboard");
			driver.findElement(By.id("Remove failed")).click();
		}
		else
		{
			System.out.println("Dashboard is Removed");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Dashboard is Removed successfully");
		}
	}
	
	@Parameters({"NewDashboardName"})
	@Test(priority=20)
	public void Logout(String NewDashboardName) throws InterruptedException
	{
		SelectDashboard(NewDashboardName);
		//Logout
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethodforDashboardOptions(driver, NewDashboardName);
	
	}
	
	public void SelectDashboard(String EMSDashboardname) throws InterruptedException
	{
		
		WebElement cla=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis=cla.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis.size());
		
		for(WebElement li: lis)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi=li.findElement(By.className("g-tab-title"));
			//System.out.println("Names are: " +fi.getText());
			
			if(fi.getText().equalsIgnoreCase(EMSDashboardname))
			{
				WebElement element=fi;
				JavascriptExecutor js = (JavascriptExecutor)driver;
				js.executeScript("arguments[0].click();", element);
				Thread.sleep(5000);
				break;
			}
		}
	}
	
	
	public void SetWorkspaceAsDefault() throws InterruptedException
	{
		//Click on Work space dashboard
		WebElement Workspace=driver.findElement(By.xpath("//li[contains(.,'WorkSpace')]"));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].click();", Workspace);
		Actions a=new Actions(driver);
		a.contextClick(Workspace).perform();
		
		//Select set default option
		driver.findElement(By.linkText("Set as default")).click();
		Thread.sleep(2000);
	}
	
	
	@Parameters({"uname", "password"})
	public void login(String uname, String password) throws InterruptedException
	{
		//Login
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
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
