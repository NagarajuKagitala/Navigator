package NavigatorCore;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.io.FileHandler;

//import org.apache.commons.io.*;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
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
import Common.Viewlets;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;


@Listeners(TestClass.class)
public class AttributeFilterCondition 
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
	
	@Parameters({"sDriver", "sDriverpath"})
	@Test
	public static void Login(String sDriver, String sDriverpath) throws Exception
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
	}
	
	@Parameters({"Dashboardname", "ViewletValue", "ViewletName"})
	@Test(priority=1)
	public static void AddDashboard(String Dashboardname, int ViewletValue, String ViewletName) throws Exception
	{
		//Delete if dashboard exists with same name
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboard(driver, Dashboardname);
		
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		
		
		/*driver.findElement(By.id("createInitialViewlets")).click();
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByVisibleText(wgs);
		
		//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(MediumSleep);
		
		//--------- Create Queue viewlet-----------
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		Thread.sleep(LowSleep);
	}
	
	@Parameters({"ConditionName", "AttributeName", "CompareOperation", "ConditionData", "RowValue"})
	@TestRail(testCaseId = 348)
	@Test(priority=2)
	public static void AddAttributeFilterCondition(String ConditionName, String AttributeName, String CompareOperation, String ConditionData, String RowValue, ITestContext context) throws Exception
	{	
		try
		{
		//Edit Viewlet page
		driver.findElement(By.id("dropdownMenuButton")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		Thread.sleep(LowSleep); 
		
		//Add attribute filter
		driver.findElement(By.cssSelector("button.btn-white-round")).click();
		Thread.sleep(LowSleep); 
		
		WebElement el=driver.findElement(By.className("maf-table-filters")).findElement(By.tagName("table"));
		List<WebElement> rows=el.findElements(By.tagName("tr"));
		//System.out.println("No of rows are :" +rows.size());
		
		for(WebElement r:rows)
		{
			//System.out.println("classes are: "+r.getAttribute("class"));
			if(r.getAttribute("class").contains("ng-star-inserted"))
			{
			System.out.println("text :" +r.getText());
					
			if(r.getText().contains(ConditionName))
			{
				r.click();
				Thread.sleep(LowSleep);
				driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
				Thread.sleep(MediumSleep);
				driver.findElement(By.id("accept-true")).click();
				Thread.sleep(LowSleep); 
				break;
			}
			}
			
		}
						
		//Click on Add button
		driver.findElement(By.xpath("//button[contains(.,'Add...')]")).click();
		Thread.sleep(LowSleep);
		
		//Filter name
		driver.findElement(By.cssSelector(".filter-title")).sendKeys(ConditionName);
		driver.findElement(By.xpath("//div[5]/button")).click();
		Thread.sleep(LowSleep);
		
		WebElement ta=driver.findElement(By.className("wrapper-mod-manage-attribute-filter-add-available-attr")).findElement(By.className("mafaaa-container")).findElement(By.className("mafaaa-container-table-wrapper")).findElement(By.tagName("table")).findElement(By.tagName("tbody"));
		List<WebElement> trs=ta.findElements(By.tagName("tr"));
		System.out.println("Number of trs: " +trs.size());
		
		for(WebElement ele:trs)
		{
			WebElement td=ele.findElement(By.tagName("td"));
			//System.out.println("text is: " +td.getText());
			
			if(td.getText().equalsIgnoreCase("Maximum Depth"))
			{
				td.click();
				Thread.sleep(MediumSleep);
				break;
			}
		}
		/*
		 * //Select the attribute String
		 * attribute=driver.findElement(By.xpath("//tr[18]/td/div")).getText();
		 * System.out.println("Attribute name is: " +attribute);
		 * 
		 * if(attribute.equalsIgnoreCase("Maximum Depth")) {
		 * driver.findElement(By.xpath("//tr[18]/td/div")).click();
		 * Thread.sleep(MediumSleep); } else {
		 * driver.findElement(By.xpath("//tr[12]/td/div")).click();
		 * Thread.sleep(MediumSleep); }
		 */
		
		//driver.findElement(By.xpath("//td[contains(.,'Maximum Depth')]")).click();
		driver.findElement(By.xpath("//app-mod-manage-attribute-filter-add-available-attr/div/div/div[3]/button")).click();
		Thread.sleep(MediumSleep);
		
		//Comparison operater name
		Select Compare=new Select(driver.findElement(By.xpath("//td[2]/select")));
		Compare.selectByVisibleText(CompareOperation);
		
		/*//Filter the data
		driver.findElement(By.xpath("//app-mod-manage-attribute-filter-add-available-attr/div/div/div/div/input")).sendKeys(RowValue);
		driver.findElement(By.xpath("//td/div")).click();
		
		driver.findElement(By.xpath("(//button[@type='button'])[11]")).click();
		Thread.sleep(2000);*/
		
		//Enter the Condition value 
		driver.findElement(By.cssSelector(".filter-value")).click();
		driver.findElement(By.cssSelector(".filter-value")).sendKeys(ConditionData);
		Thread.sleep(LowSleep);
		
		//Click on Ok
		driver.findElement(By.xpath("//div[5]/button[2]")).click();
		Thread.sleep(MediumSleep);
		
		//Select the added one
		WebElement el1=driver.findElement(By.className("maf-table-filters")).findElement(By.tagName("table"));
		List<WebElement> rows1=el1.findElements(By.tagName("tr"));
		//System.out.println("No of rows are :" +rows1.size());
		
		for(WebElement r1:rows1)
		{
			if(r1.getAttribute("class").contains("ng-star-inserted"))
			{
			System.out.println("text :" +r1.getText());
					
			if(r1.getText().contains(ConditionName))
			{
				r1.click();
				Thread.sleep(MediumSleep);
				break;
			}
			}
			
		}
		
		//driver.findElement(By.xpath("//td/input")).click();
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		Thread.sleep(LowSleep);
		
		//Click on Apply
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
		
		String Noofqueues=driver.findElement(By.xpath("//datatable-footer/div/div/span")).getText();
		String[] Index1=Noofqueues.split(" Visible");
		String[] Index2=Index1[0].split(": ");
		int k=Integer.parseInt(Index2[1]);
		for(int i=1; i<=k; i++)
		{
			String Result=driver.findElement(By.xpath("//datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[7]/div/span")).getText();
			int integer=Integer.parseInt(Result);
			System.out.println("Values are: " +integer+ " ConditionData is: " +ConditionData);
			if(integer==Integer.parseInt(ConditionData))
			{
				
				System.out.println("Attribute filter is working fine");
			}
		}
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Inactive Check box is Working fine");
		}
		catch (Exception e)
		{
			System.out.println("Exception occured");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Attribute filter condition not working");
			driver.findElement(By.xpath("Attribute filter condition failed")).click();
		}
	}
	
	
	@Parameters({"Dashboardname"})
	@Test(priority=3)
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
