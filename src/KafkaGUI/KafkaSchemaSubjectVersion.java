package KafkaGUI;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
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

import Common.ClearSelectionofCheckbox;
import Common.Dashboard;
import Common.KafkaLogin;
import Common.LogoutForAll;
import Common.Viewlets;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class KafkaSchemaSubjectVersion 
{
		static WebDriver driver;
		static String Screenshotpath;
		static String DownloadPath;
		static String WGSName;
		static String KafkaNodeName;
		static String Low;
		static String Medium;
		static String High;
		static int LowSleep;
		static int MediumSleep;
		static int HighSleep;
		static String SchemaCluster;
		static String SchemaRegistry;
		
		
		@BeforeTest
		public void beforeTest() throws Exception {
			System.out.println("BeforeTest");
			Settings.read();
			
			Screenshotpath =Settings.getScreenshotPath();
			DownloadPath =Settings.getDownloadPath();
			WGSName =Settings.getWGSNAME();
			KafkaNodeName =Settings.getKafkaNodeName();
			Low =Settings.getLow();
			Medium =Settings.getMedium();
			High =Settings.getHigh();
			LowSleep=Integer.parseInt(Low);
			MediumSleep =Integer.parseInt(Medium);
			HighSleep=Integer.parseInt(High);
			SchemaCluster=Settings.getSchemaCluster();
			SchemaRegistry=Settings.getSchemaRegistry();
			
		}
		
		@Parameters({"sDriver", "sDriverpath", "Dashboardname", "ViewletValue", "ViewletName"})
		@Test
		public static void Login(String sDriver, String sDriverpath, String Dashboardname, int ViewletValue, String ViewletName) throws Exception
		{
			try {
			
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
			
			driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
				
			 KafkaLogin login=new KafkaLogin(); 
			 boolean islogin=login.KafkaLoginPage(driver, sDriver, sDriverpath); 
			 
			 if(islogin)
			 {
				 System.out.println("login passed");
				 
				   //Delete if dashboard exists with same name
					Dashboard ob=new Dashboard();
					ob.DeleteExistDashboard(driver, Dashboardname);
				
				  //Create New Dashboard
					driver.findElement(By.cssSelector("div.block-with-border")).click();
					driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
											
					//Create viewlet button
					driver.findElement(By.xpath("//button[@type='submit']")).click();
					Thread.sleep(4000);
					
					// ---- Creating Channel Viewlet ----		
					Viewlets obj=new Viewlets();
					obj.KafkaViewlet(driver, ViewletValue, ViewletName, WGSName, KafkaNodeName);
			 }
			 else
			 {
				 System.out.println("Login done"); 
			 }
			}
			
			catch(Exception ee)
			{
				//System.out.println("Exceptio is: " +ee.getStackTrace());
				ee.printStackTrace();
			}
				
		}
		
		@Parameters({"ViewletValue1", "ViewletName1"})
		@Test(priority=1)
		public void SchemaSubjectViewlet(int ViewletValue1, String ViewletName1) throws InterruptedException
		{
			Viewlets obj=new Viewlets();
			obj.KafkaViewlet(driver, ViewletValue1, ViewletName1, WGSName, KafkaNodeName);
		}
		
		@Parameters({"Dashboardname", "SchemaName"})
		@TestRail(testCaseId = 1182)
		@Test(priority=11)
		public void ShowObjectAttributes(String Dashboardname, String SchemaName, ITestContext context) throws InterruptedException
		{
			try
			{
				KafkaObjectAttributes obj=new KafkaObjectAttributes();
				obj.SchemaSubjectVersionShowObjectAttributes(Dashboardname, driver, SchemaName);
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "object attibutes opened with the selected kafka schema");
			}
			catch(Exception e)
			{
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed object attibutes page");
				driver.findElement(By.id("Attributes failed")).click();
			}
		}
		
		@Parameters({"Dashboardname"})
		@Test(priority=2)
		@TestRail(testCaseId = 1183)
		public void Properties(String Dashboardname, ITestContext context) throws InterruptedException
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			try
			{
			//Select properties option
	    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
	    	driver.findElement(By.linkText("Properties...")).click();
	    	Thread.sleep(8000);
	    		    	
	    	WebElement El=driver.findElement(By.tagName("app-text-input")).findElement(By.tagName("div")).findElement(By.tagName("div"));
	    	System.out.println(El.getAttribute("innerHTML"));
	    	
	    	String Namefield=El.getAttribute("innerHTML");
	    				
			//Verification Condition
			if(Namefield.contains("disabled"))
			{
				System.out.println("The Schema subject version name field is Disabled");
				context.setAttribute("Status",1);
	    		context.setAttribute("Comment", "Schema subject version properties are working fine");
				driver.findElement(By.cssSelector(".btn-danger")).click();
			}
			else
			{
				System.out.println("The Schema subject version name field is Enabled");
				context.setAttribute("Status",5);
	    		context.setAttribute("Comment", "Schema subject version properties are not working properly");
				driver.findElement(By.cssSelector(".btn-danger")).click();
				driver.findElement(By.xpath("Name field is disabled")).click();
				
			}
			Thread.sleep(1000); 
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		@Parameters({"Dashboardname", "SubjectName"})
		@TestRail(testCaseId = 1184)
		@Test(priority=3)
		public void CreateKafkaSchemaSubject(String Dashboardname, String SubjectName, ITestContext context) throws InterruptedException
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			try
			{
			//Click on + icon
			driver.findElement(By.xpath("//img[@title='Add Kafka Schema Subject']")).click();
			
			//Select WGS			
			driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/ng-select/div/span")).click();
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
						
			//Select cluster
			driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div[2]/ng-select/div/span")).click();
	        try 
			{
	        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
				List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
				System.out.println(mdivs.size());	
				
				for (WebElement mdi : mdivs)
				{
					if(mdi.getText().equals(SchemaCluster))
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
	        
	        //Select Schema registry
	        driver.findElement(By.xpath("//div[2]/ng-select[2]/div/span")).click();
	        
	        try 
			{
	        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
				List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
				System.out.println(mdivs.size());	
				
				for (WebElement mdi : mdivs)
				{
					if(mdi.getText().equals(SchemaRegistry))
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
	        driver.findElement(By.xpath("//button[contains(.,'Select path')]")).click();
			Thread.sleep(4000);
			
			//Give the subject name
			driver.findElement(By.id("subjectName")).sendKeys(SubjectName);
			
			//Click on save button
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
			Thread.sleep(MediumSleep);
			
			try
			{
				driver.findElement(By.id("yes")).click();
			}
			catch (Exception e)
			{
				System.out.println("No Exception occured");
				
			}
			
			//Refresh viewlet
			for(int i=0; i<=3; i++)
			{
				driver.findElement(By.xpath("//div[2]/app-viewlet/div/div[2]/div/div/div/div[2]/div/div/i")).click();
				Thread.sleep(6000);
			}
			
			//Serach with empty queue name
			driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys(SubjectName);
			Thread.sleep(4000);
			
			//Store the first queue name into string
			String ResultSchemaname=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[7]/div/span")).getText();
			System.out.println(ResultSchemaname);
			
			//Edit the search
			for(int k=0; k<=SubjectName.length(); k++)
			{
				driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys(Keys.BACK_SPACE);
			}
			Thread.sleep(1000);
			
			if(ResultSchemaname.equalsIgnoreCase(SubjectName))
			{
				System.out.println("Schema subject is added successfully from icon");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "Schema subject is added successfully using add icon");
			}
			else
			{
				System.out.println("Schema subject is not added");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Faile to add Schema subject using add icon");
				driver.findElement(By.id("Add Schema subject failed")).click();
			}
			
			}
			catch(Exception ee)
			{
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Faile to add Schema subject using add icon");
				driver.findElement(By.id("Add Schema subject failed")).click();
			}
		}
		
		@TestRail(testCaseId = 1185)
		@Parameters({"Dashboardname", "SubjectName"})
		@Test(priority=4, dependsOnMethods = {"CreateKafkaSchemaSubject"})
		public void DeleteSchemaSubjectVersion(String Dashboardname, String SubjectName, ITestContext context) throws InterruptedException
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			//Search with name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(SubjectName);
			Thread.sleep(4000);
			
			//Select delete option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
	    	driver.findElement(By.linkText("Delete")).click();
	    	Thread.sleep(8000);
	    	
	    	//Click on confirmation yes button
	    	driver.findElement(By.id("accept-true")).click();
	    	Thread.sleep(MediumSleep);
	    	
	    	//Refresh viewlet
			for(int i=0; i<=5; i++)
			{
				driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
				Thread.sleep(6000);
			}
	    	
	    	//Store the schema subject name after deleting the subject
			String ViewletData=driver.findElement(By.xpath("//datatable-body")).getText();
			System.out.println(ViewletData);
			
			//Edit the search
			for(int k=0; k<=SubjectName.length(); k++)
			{
				driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
			}
			Thread.sleep(1000);
			
			if(ViewletData.contains(SubjectName))
			{
				System.out.println("Schema subject version is not deleted");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to delete Schema subject");
				driver.findElement(By.xpath("Schema subject version Delete failed")).click();
			}
			else
			{
				
				System.out.println("Schema subject version is deleted Successfully");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Schema subject version deleted Successfully");
			}
		}
		
		@Parameters({"Dashboardname"})
		@Test(priority=27)
		public static void Logout(String Dashboardname) throws InterruptedException
		{
			LogoutForAll lo=new LogoutForAll();
			lo.LogoutMethod(driver, Dashboardname);
		}
		
		private static boolean checkprogress() throws InterruptedException {
			try {
				WebElement progressBar = driver.findElement(By.cssSelector(".progress-bar"));
				while (progressBar.isDisplayed()) {
					System.out.println("Progress bar loading....");
					Thread.sleep(1000);
				}
			} catch (StaleElementReferenceException e) {
				// TODO: handle exception
				return false;
			}
			return false;
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
