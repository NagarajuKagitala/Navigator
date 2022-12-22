package NavigatorCore;

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
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
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
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class GlobalSettings 
{
static WebDriver driver;
	
	static String Screenshotpath;
	static String WGSName;
	static String Node_Hostname;
	static String Low;
	static String Medium;
	static String High;
	static int LowSleep;
	static int MediumSleep;
	static int HighSleep;
	static String ListOfEnvironments="";

	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		WGSName =Settings.getWGSNAME();
		Node_Hostname =Settings.getNode_Hostname();
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
		Thread.sleep(LowSleep);
		//driver.findElement(By.id("iv_yes")).click();
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(MediumSleep);
	}
	
	
	@Parameters({"GlobalNoticedata"})
	@TestRail(testCaseId=1162)
	@Test(priority=1)
	public void GlobalNotice(String GlobalNoticedata, ITestContext context) throws InterruptedException 
	{
		EditglobalNotice();
		
		//Get global notice text
		String Initialtext=driver.findElement(By.id("global-notice")).getText();
		System.out.println("Initial Notice is: " +Initialtext);
		
		//Verifiy the checkbox status
		boolean checkbox=driver.findElement(By.id("display-global-notice")).isSelected();
		System.out.println("check box status is: " +checkbox);
		
		if(checkbox)
		{
			System.out.println("check box already selected");
		}
		else
		{
			driver.findElement(By.id("display-global-notice")).click();
			Thread.sleep(LowSleep);
		}
		
		//Enter the notice data
		driver.findElement(By.id("global-notice")).clear();
		driver.findElement(By.id("global-notice")).sendKeys(GlobalNoticedata);
		Thread.sleep(LowSleep);
		
		//Click on save changes button
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(MediumSleep);
		
		//Get result data
		String ResultNotice=driver.findElement(By.xpath("//app-global-notice/div/div")).getText();
		System.out.println("Result data is: " +ResultNotice);
		
		//verification
		if(ResultNotice.equalsIgnoreCase(GlobalNoticedata))
		{
			System.out.println("Global notice is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Global notice is working");
		}
		else
		{
			System.out.println("Global notice is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Global notice is not working");
			EditglobalNotice();
			UncheckGlobalNotice();
			driver.findElement(By.id("global notice failed")).click();
		}
		
		//Edit Global notice
		EditglobalNotice();
		
		//Restore global notice
		UncheckGlobalNotice();
	}
	
	
	@Parameters({"EnvironmentData"})
	@TestRail(testCaseId=1163)
	@Test(priority=2)
	public void EnvironmentLevel(String EnvironmentData, ITestContext context) throws InterruptedException
	{
		try
		{
		//Goto environment level tab
		EnvironmentTab();
		
		//Delete existing environment level
		DeleteExistingEnvironment(EnvironmentData);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		//Click on Add button
		driver.findElement(By.xpath("//button[contains(.,' ADD')]")).click();
		Thread.sleep(LowSleep);
		
		//Give the environment data
		driver.findElement(By.id("env_level_txt")).clear();
		driver.findElement(By.id("env_level_txt")).sendKeys(EnvironmentData);
		Thread.sleep(LowSleep);
		
		//Click on save changes button
		driver.findElement(By.xpath("//app-modal-main-environment-level-properties/div[2]/div/div/div/button")).click();
		Thread.sleep(MediumSleep);
		
		//Select Environment level
		driver.findElement(By.xpath("//div[3]/div/ng-select/div/span")).click();
		Thread.sleep(MediumSleep);
		
		WebElement ele=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> divs=ele.findElements(By.tagName("div"));
		System.out.println("number of divs are: " +divs.size());
		
		for(WebElement div:divs)
		{
			System.out.println("env data is: " +div.getText());
			if(div.getText().equalsIgnoreCase(EnvironmentData))
			{
				div.click();
				break;
			}
		}
		
		Boolean checkbox=driver.findElement(By.id("env_level_display_chkbox")).isSelected();
		System.out.println("Check box status is: " +checkbox);
		
		if(checkbox)
		{
			System.out.println("Check box already selected");
		}
		else
		{
			driver.findElement(By.id("env_level_display_chkbox")).click();
		}
		
		//Click on save changes button
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(MediumSleep);
		
		//Get results text
		String ResultEnvironmentText=driver.findElement(By.xpath("//app-header/div")).getText();
		System.out.println("Result data is: " +ResultEnvironmentText);
		
		//verification
		if(ResultEnvironmentText.contains(EnvironmentData))
		{
			System.out.println("Environment text is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Environment text is working");
		}
		else
		{
			System.out.println("Environment text is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Environment text is not working");
			EnvironmentTab();
			UncheckEnvironement();
			driver.findElement(By.id("Environment text failed")).click();
		}
		
		//Environment tab
		EnvironmentTab();
		//Uncheck environment tab
		UncheckEnvironement();
	}
	
	@Parameters({"EnvironmentData", "UpdatedEnvironmentData"})
	@TestRail(testCaseId=1164)
	@Test(priority=3)
	public void UpdateEnvironmentLevel(String EnvironmentData, String UpdatedEnvironmentData, ITestContext context) throws InterruptedException
	{
		//go to environment tab
		EnvironmentTab();
		
		//Get the existing environments
		WebElement tab=driver.findElement(By.className("table-info"));
		List<WebElement> trs=tab.findElements(By.tagName("tr"));
		System.out.println("Number of trs: " +trs.size());
		
		boolean cond=false;
		for(WebElement tr:trs)
		{
			//System.out.println("class is: " +tr.getAttribute("class"));
			if(tr.getAttribute("class").contains("ng-star-inserted"))
			{
				List<WebElement> tds=tr.findElements(By.tagName("td"));
				System.out.println("tds size: " +tds.size());
				
				
				for(int i=0; i<tds.size(); i++)
				{
					WebElement webtd=tds.get(i);
					//System.out.println("td text is :" +webtd.getText());
					//System.out.println("index value: " +i);
				
						
					if(webtd.getText().contains(EnvironmentData))
					{
						//System.out.println("in :" +i);
						List<WebElement> td1=tds.get(i+1).findElements(By.tagName("button"));
						for(WebElement fin:td1)
						{
						if(fin.getAttribute("class").contains("btn-primary"))
						{
							System.out.println("button text is: " +fin.getText());
							fin.click();
							Thread.sleep(MediumSleep);
							//Give the environment data
							driver.findElement(By.id("env_level_txt")).clear();
							driver.findElement(By.id("env_level_txt")).sendKeys(UpdatedEnvironmentData);
							Thread.sleep(LowSleep);
							
							//Click on save changes button
							driver.findElement(By.xpath("//app-modal-main-environment-level-properties/div[2]/div/div/div/button")).click();
							Thread.sleep(MediumSleep);
							cond=true;
							break;
						}
						}
								
					}
					if(cond)
					{
						break;
					}
					}
					
				}
			if(cond)
			{
				break;
			}	
			}
		//Get list
		ListOfEnv();
		
		if(ListOfEnvironments.contains(UpdatedEnvironmentData))
		{
			System.out.println("Environment data update is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Environment data update is working");
		}
		else
		{
			System.out.println("Environment data update is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Environment data update is not working");
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
			Thread.sleep(MediumSleep);
			driver.findElement(By.id("Environment data update failed")).click();
		}
		
		
		//Click on save changes button
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(MediumSleep);
	}
	
	@Parameters({"UpdatedEnvironmentData"})
	@TestRail(testCaseId=1165)
	@Test(priority=4)
	public void DeleteEnvironmentLevel(String UpdatedEnvironmentData, ITestContext context) throws InterruptedException
	{
		//Environment tab
		EnvironmentTab();
		
		//Deleting existing one
		DeleteExistingEnvironment(UpdatedEnvironmentData);
		
		//Get the data into string
		ListOfEnv();
		
		if(ListOfEnvironments.contains(UpdatedEnvironmentData))
		{
			System.out.println("Environment data delete is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Environment data delete is not working");
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
			Thread.sleep(MediumSleep);
			driver.findElement(By.id("Environment data delete failed")).click();
		}
		else
		{
			System.out.println("Environment data delete is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Environment data delete is working");
			
		}
		
		//Click on save changes button
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(MediumSleep);
	}
	
	@TestRail(testCaseId=1172)
	@Test(priority=5)
	public void MinimumRefreshInterval(ITestContext context) throws InterruptedException
	{
		//Edit global settings
		EditGlobalSettings();
		
		//Get Refresh interval
		String RefreshInterval=driver.findElement(By.xpath("//div[2]/div/div/div/div/input")).getAttribute("value");
		System.out.println("Initial interval is: " +RefreshInterval);
		int IRI=Integer.parseInt(RefreshInterval);
		
		int MinRI=IRI+10;
		String Interval=Integer.toString(MinRI);
		System.out.println("Minimum interval is: " +Interval);
		
		//Give the minimum value
		driver.findElement(By.xpath("//div[2]/input")).clear();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//div[2]/input")).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath("//div[2]/input")).sendKeys(Interval);
		Thread.sleep(LowSleep);
		
		//Click on save changes button
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(MediumSleep);	
		
		//Edit Global settings
		EditGlobalSettings();
		
		//Get result value
		String RefreshIntervalResult=driver.findElement(By.xpath("//div[2]/div/div/div/div/input")).getAttribute("value");
		System.out.println("final interval is: " +RefreshIntervalResult);
		int ResultIRI=Integer.parseInt(RefreshIntervalResult);
		
		//Give the minimum value
		driver.findElement(By.xpath("//div[2]/input")).clear();
		Thread.sleep(MediumSleep);
		driver.findElement(By.xpath("//div[2]/input")).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath("//div[2]/input")).sendKeys("120");
		Thread.sleep(LowSleep);
		
		//Click on save changes button
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(MediumSleep);
		
		if(ResultIRI==MinRI)
		{
			System.out.println("Minimum refresh interval is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Minimum refresh interval is working");
		}
		else
		{
			System.out.println("Minimum refresh interval is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Minimum refresh interval is not working");
			driver.findElement(By.id("Minimum refresh interval failed")).click();
		}
	}
	
	@TestRail(testCaseId=1173)
	@Parameters({"Dashboardname", "NewOwner"})
	@Test(priority=6)
	public void DashboardOwnershipManagement(String Dashboardname, String NewOwner, ITestContext context) throws Exception
	{
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		driver.get(URL);
		
		//Login with user
		driver.findElement(By.id("username")).sendKeys(NewOwner);
		Thread.sleep(LowSleep);
		driver.findElement(By.id("password")).sendKeys("User");
		Thread.sleep(LowSleep);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(MediumSleep);
		
		//Click on cancel button
		driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
		Thread.sleep(LowSleep);
		
		driver.get(URL);
		
		//Login with main user
		driver.findElement(By.id("username")).sendKeys(uname);
		Thread.sleep(LowSleep);
		driver.findElement(By.id("password")).sendKeys(password);
		Thread.sleep(LowSleep);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(HighSleep);
		
		EditGlobalSettings();
		
		//Choose Dashboard ownership management tab
		driver.findElement(By.xpath("//div[12]")).click();
		Thread.sleep(MediumSleep);
		
		//Search with dashboard name
		driver.findElement(By.xpath("//app-modal-main-settings-dashboard-ownership-management/div/div/div[2]/input")).sendKeys(Dashboardname);
		driver.findElement(By.xpath("//app-modal-main-settings-dashboard-ownership-management/div/div/div[2]/input[2]")).click();
		Thread.sleep(MediumSleep);
		
		//Select check box
		driver.findElement(By.xpath("//td/input")).click();
		Thread.sleep(MediumSleep);
		
		//Click on change owner button
		driver.findElement(By.xpath("//button[contains(.,' Change Owner')]")).click();
		Thread.sleep(MediumSleep);
		
		//Search with owner
		driver.findElement(By.xpath("//app-modal-change-dashboard-owner/div/div/div[2]/input")).sendKeys(NewOwner);
		driver.findElement(By.xpath("//app-modal-change-dashboard-owner/div/div/div[2]/input[2]")).click();
		Thread.sleep(MediumSleep);
		
		//Set owner
		driver.findElement(By.xpath("//button[contains(.,' Set owner')]")).click();
		Thread.sleep(LowSleep);
		
		//Click on confirmation yes
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(MediumSleep);
		
		String OwnerName=driver.findElement(By.xpath("//td[3]")).getText();
		System.out.println("Owner ship name is: " +OwnerName);
		
		//Set back to Admin
		driver.findElement(By.xpath("//td/input")).click();
		Thread.sleep(MediumSleep);
		
		//Click on change owner button
		driver.findElement(By.xpath("//button[contains(.,' Change Owner')]")).click();
		Thread.sleep(MediumSleep);
		
		//Search with owner
		driver.findElement(By.xpath("//app-modal-change-dashboard-owner/div/div/div[2]/input")).sendKeys(uname);
		driver.findElement(By.xpath("//app-modal-change-dashboard-owner/div/div/div[2]/input[2]")).click();
		Thread.sleep(MediumSleep);
		
		//Set owner
		driver.findElement(By.xpath("//button[contains(.,' Set owner')]")).click();
		Thread.sleep(LowSleep);
		
		try
		{
			driver.findElement(By.xpath("//div[2]/div/div/table/tr[3]/td[2]/button")).click();
			Thread.sleep(LowSleep);
		}
		
		catch(Exception e)
		{
			
		}
		
		//Click on confirmation yes
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(MediumSleep);
		
		//Click on save changes
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(MediumSleep);
		
		
		if(OwnerName.equalsIgnoreCase(NewOwner))
		{
			System.out.println("Dashboard ownership management is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Dashboard ownership management is working");
		}
		else
		{
			System.out.println("Dashboard ownership management is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Dashboard ownership management is not working");
			driver.findElement(By.id("Dashboard ownership management failed")).click();
		}
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=30)
	public void Logout(String Dashboardname) throws InterruptedException 
	{
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, Dashboardname);

	}
	
	public void EditglobalNotice() throws InterruptedException
	{
		//Click on Settings icon
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(MediumSleep);
		
		//Click on Edit global settings icon
		driver.findElement(By.xpath("//button[contains(.,'Edit global settings')]")).click();
		Thread.sleep(LowSleep);
		
		//Click on Global notice tab
		driver.findElement(By.xpath("//div[7]")).click();
		Thread.sleep(LowSleep);
		
	}
	
	public void UncheckGlobalNotice() throws InterruptedException
	{
		//Verifiy the checkbox status
		boolean checkbox=driver.findElement(By.id("display-global-notice")).isSelected();
		System.out.println("check box status is: " +checkbox);
		
		if(checkbox)
		{
			driver.findElement(By.id("display-global-notice")).click();
			Thread.sleep(LowSleep);
		}
		else
		{
			
			System.out.println("check box not selected");
		}
		
		//Click on save changes button
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(HighSleep);	
	}
	
	public void EnvironmentTab() throws InterruptedException
	{
		//Click on Settings icon
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(MediumSleep);
		
		//Click on Edit global settings icon
		driver.findElement(By.xpath("//button[contains(.,'Edit global settings')]")).click();
		Thread.sleep(LowSleep);
		
		//Click on Environment tab
		driver.findElement(By.xpath("//div[10]")).click();
		Thread.sleep(LowSleep);
		
	}
	
	public void UncheckEnvironement() throws InterruptedException
	{
		//Verifiy the checkbox status
		boolean checkbox=driver.findElement(By.id("env_level_display_chkbox")).isSelected();
		System.out.println("check box status is: " +checkbox);
		
		if(checkbox)
		{
			driver.findElement(By.id("env_level_display_chkbox")).click();
			Thread.sleep(LowSleep);
		}
		else
		{
			
			System.out.println("check box not selected");
		}
		
		//Click on save changes button
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(HighSleep);	
	}
	
	public void DeleteExistingEnvironment(String EnvironmentData) throws InterruptedException
	{
		//Get the existing environments
		WebElement tab=driver.findElement(By.className("table-info"));
		List<WebElement> trs=tab.findElements(By.tagName("tr"));
		System.out.println("Number of trs: " +trs.size());
		
		boolean cond=false;
		for(WebElement tr:trs)
		{
			//System.out.println("class is: " +tr.getAttribute("class"));
			if(tr.getAttribute("class").contains("ng-star-inserted"))
			{
				List<WebElement> tds=tr.findElements(By.tagName("td"));
				System.out.println("tds size: " +tds.size());
				
				
				for(int i=0; i<tds.size(); i++)
				{
					WebElement webtd=tds.get(i);
					//System.out.println("td text is :" +webtd.getText());
					//System.out.println("index value: " +i);
				
						
					if(webtd.getText().contains(EnvironmentData))
					{
						//System.out.println("in :" +i);
						List<WebElement> td1=tds.get(i+1).findElements(By.tagName("button"));
						for(WebElement fin:td1)
						{
						if(fin.getAttribute("class").contains("btn-danger"))
						{
							System.out.println("button text is: " +fin.getText());
							fin.click();
							Thread.sleep(LowSleep);
							driver.findElement(By.id("accept-true")).click();
							Thread.sleep(LowSleep);
							cond=true;
							break;
						}
						}
								
					}
					if(cond)
					{
						break;
					}
					}
					
				}
			if(cond)
			{
				break;
			}	
			}
		
	}
	
	public void ListOfEnv()
	{
		//Get the existing environments
		WebElement tab=driver.findElement(By.className("table-info"));
		List<WebElement> trs=tab.findElements(By.tagName("tr"));
		System.out.println("Number of trs: " +trs.size());
		
		StringBuffer buffer=new StringBuffer();
		for(WebElement tr:trs)
		{
			//System.out.println("class is: " +tr.getAttribute("class"));
			if(tr.getAttribute("class").contains("ng-star-inserted"))
			{
				List<WebElement> tds=tr.findElements(By.tagName("td"));
				System.out.println("tds size: " +tds.size());
				
				
				for(WebElement td:tds)
				{
					String list=td.getText();
					System.out.println("td text is :" +list);
					buffer.append(list);
					buffer.append(",");
					
				}
			}
		}
		
		ListOfEnvironments=buffer.toString();
		System.out.println("List of environments are: " +ListOfEnvironments);
		
	}
	
	public void EditGlobalSettings() throws InterruptedException
	{
		//Click on Settings icon
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(MediumSleep);
		
		//Click on Edit global settings icon
		driver.findElement(By.xpath("//button[contains(.,'Edit global settings')]")).click();
		Thread.sleep(LowSleep);
		
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
				FileHandler.copy(scrFile, new File(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png"));
				//FileUtils.copyFile(scrFile, new File(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png"));
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
