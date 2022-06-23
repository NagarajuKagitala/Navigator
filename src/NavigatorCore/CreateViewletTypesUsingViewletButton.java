package NavigatorCore;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
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
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
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
public class CreateViewletTypesUsingViewletButton 
{ 
	String FinalProcess="";
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName;
	static String Low;
	static String Medium;
	static String High;
	static int LowSleep;
	static int MediumSleep;
	static int HighSleep;
	static String UploadDashboard;
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		WGSName =Settings.getWGSNAME(); 
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
		UploadDashboard=Settings.getUploadDashboard();
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
		
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		
		
		/*driver.findElement(By.id("createInitialViewlets")).click();
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByIndex(Integer.parseInt(WGS_INDEX));
		
		//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(MediumSleep);
	}
	
	@TestRail(testCaseId=814)
	@Parameters({"Nodename"})
	@Test(priority=1)
	public void CreateViewletUsingObjectCheckbox(String Nodename, ITestContext context) throws InterruptedException
	{	
		//Click on Viewlet
		driver.findElement(By.id("add-viewlet")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click(); 
			
		driver.findElement(By.cssSelector(".field-workgroup-input > .ng-select-container")).click();
		Thread.sleep(LowSleep);  
		
		WebElement drop=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div=drop.findElements(By.tagName("div")); 
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement di : div)
		{
			//System.out.println("text is :" +di.getText());
			if(di.getText().equalsIgnoreCase("IBM MQ"))
			{
				di.click();
				break;
			}	
		}
		Thread.sleep(LowSleep);
		
		//Create a Node
		driver.findElement(By.cssSelector("div.object-type")).click();
		driver.findElement(By.name("viewletName")).clear();
		//Thread.sleep(2000);
		driver.findElement(By.name("viewletName")).sendKeys(Nodename);
		
		//Select WGS
		driver.findElement(By.xpath("//div/div/div[2]/div/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(LowSleep);   
		
		WebElement dropw=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> divw=dropw.findElements(By.tagName("div")); 
		//System.out.println("Number of divs are: " +divw.size());
		
		for(WebElement diw : divw)
		{
			//System.out.println("WGS text is :" +diw.getText());
			if(diw.getText().equalsIgnoreCase(WGSName))
			{
				diw.click();
				break;
			}	
		}
		Thread.sleep(LowSleep);
		
		driver.findElement(By.id("save-viewlet")).click();
		//driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
		
		if(driver.getPageSource().contains(Nodename))
		{
			System.out.println("Node Viewlet is created");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Viewlet is created");
		}
		else
		{
			System.out.println("Node viewlet is not created");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Viewlet is not created");
			driver.findElement(By.xpath("Not created")).click();
		}
	}
	
	@TestRail(testCaseId=797)
	@Test(priority=2)
	public void CreateATemporaryViewletUsingSearchCheckbox(ITestContext context) throws InterruptedException
	{
		//Click on Viewlet
		//driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.id("add-viewlet")).click();
		
		//Click on Temporary viewlet check box
		driver.findElement(By.id("temp")).click();
		
		//Click on Create Button
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click(); 
		Thread.sleep(LowSleep);
		
		String ViewletName=driver.findElement(By.name("viewletName")).getText();
		System.out.println(ViewletName);
		
		//Select WGS
		driver.findElement(By.xpath("//div/div/div[2]/div/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(LowSleep);   
		
		WebElement dropw=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> divw=dropw.findElements(By.tagName("div")); 
		//System.out.println("Number of divs are: " +divw.size());
		
		for(WebElement diw : divw)
		{
			//System.out.println("WGS text is :" +diw.getText());
			if(diw.getText().equalsIgnoreCase(WGSName))
			{
				diw.click();
				break;
			}	
		}
		Thread.sleep(LowSleep);
		
		driver.findElement(By.id("save-viewlet")).click();
		//driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
		
		if(driver.getPageSource().contains(ViewletName))
		{
			System.out.println("Temporary Viewlet is created");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Created temporary viewlet");
		}
		else
		{
			System.out.println("Temporary viewlet is not created");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Temporary viewlet is not created");
			driver.findElement(By.xpath("Temporary viewlet Not created")).click();
		}
	}
	
	@TestRail(testCaseId=815)
	@Parameters({"FavoriteViewletName"})
	@Test(priority=3)
	public void CreateFavoriteViewletUsingCheckbox(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		//Create favorite viewlet
		//driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.id("add-viewlet")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		
		//select WGS
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
		
		//Submit
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(HighSleep);
		
		if(driver.getPageSource().contains(FavoriteViewletName))
		{
			System.out.println("Favorite Viewlet is created");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Favorite viewlet created");
		}
		else
		{
			System.out.println("Favorite viewlet is not created");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Favorite viewlet not created");
			driver.findElement(By.xpath("Not created Favorite")).click();
		}
	}
	
	@TestRail(testCaseId=816)
	@Parameters({"Nodename"})
	@Test(priority=4)
	public void OpenAnExistingCheckbox(String Nodename, ITestContext context) throws InterruptedException
	{
		//Delete Existing viewlet
		driver.findElement(By.id("dropdownMenuButton")).click();
		driver.findElement(By.linkText("Remove viewlet")).click();
		
		//Confirmation yes button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(MediumSleep);
		
		//Click on Viewlet
		//driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.id("add-viewlet")).click();
		
		driver.findElement(By.id("existing")).click();
		
		driver.findElement(By.id("viewlet-type-ok")).click();
		Thread.sleep(HighSleep);
		Thread.sleep(HighSleep);
		
		//Search with viewlet name
		driver.findElement(By.xpath("//app-modal-add-viewlet-existing/div/div/div/div/input")).sendKeys(Nodename);
		Thread.sleep(HighSleep);
		
		//Select viewlet
		driver.findElement(By.xpath("//datatable-body-cell[2]/div/div")).click();
		Thread.sleep(MediumSleep);
		
		driver.findElement(By.xpath("//app-modal-add-viewlet-existing/div/div[2]/button[2]")).click();
		Thread.sleep(HighSleep);
		
		if(driver.getPageSource().contains(Nodename))
		{
			System.out.println("Open existing viewlet checkbox is working fine");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Checkbox is working");
		}
		else
		{
			System.out.println("Open existing viewlet checkbox is not working");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Checkbox is not working");
			driver.findElement(By.id("Open existing check box condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId=798)
	@Parameters({"Dashboardname"})
	@Test(priority=5)
	public void CreateTemporaryViewletUsingTemporaryViewletCheckbox(String Dashboardname, ITestContext context) throws Exception
	{
		Settings.read();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		//Click on Viewlet button
		driver.findElement(By.xpath("//button[3]")).click();
		
		//select temporary viewlet checkbox
		//driver.findElement(By.id("temp")).click();
		
		//click on Ok button
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click(); 
		Thread.sleep(MediumSleep);
		
		//Select Temp checkbox
		boolean temp=driver.findElement(By.name("isTemporary")).isSelected();
		System.out.println("Check box status: " +temp);
		
		if(temp)
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			driver.findElement(By.name("isTemporary")).click();
		}
		
		//Select WGS type
		driver.findElement(By.xpath("//div/div/div[2]/div/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(LowSleep);   
		
		WebElement dropw=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> divw=dropw.findElements(By.tagName("div")); 
		//System.out.println("Number of divs are: " +divw.size());
		
		for(WebElement diw : divw)
		{
			//System.out.println("WGS text is :" +diw.getText());
			if(diw.getText().equalsIgnoreCase(WGSName))
			{
				diw.click();
				break;
			}	
		}
		Thread.sleep(LowSleep);
		
		//click on Save changes button
		driver.findElement(By.id("save-viewlet")).click();
		//driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
		
		//get the viewlet title from viewlet
		String ViewletTitle=driver.findElement(By.xpath("//div[4]/app-viewlet/div/div[2]/div/div/div")).getText();
		System.out.println("Temporary Viewlet title is: " +ViewletTitle);
		
		//Logout
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		driver.findElement(By.id("yesButton")).click();
		Thread.sleep(MediumSleep);
		
		//Login again
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(HighSleep);
		
		//Go to that dashboard
		WebElement cla=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis=cla.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis.size());
		
		for(WebElement li: lis)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi=li.findElement(By.className("g-tab-title"));
			System.out.println("Names are: " +fi.getText());
			
			if(fi.getText().equalsIgnoreCase(Dashboardname))
			{
				fi.click();
				break;
			}
		}
		Thread.sleep(LowSleep);
		
		/*//Get the viewlet title from viewlet
		String Viewletdata=driver.findElement(By.id("main-page")).getText();
		System.out.println("After logout Temporary Viewlet data is: " +Viewletdata);*/
		
		if(driver.getPageSource().contains(ViewletTitle))
		{
			System.out.println("Temporary viewlet functionality failed from Checkbox");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Temporary viewlet creation failed");
			driver.findElement(By.id("Temporary failed")).click();
		}
		else
		{
			System.out.println("Temporary viewlet functionality Working using edit viewlet option");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Temporary viewlet is created using editviewlet page option");
		}
		
	}
	
	@TestRail(testCaseId=817)
	@Parameters({"NewViewletName"})
	@Test(priority=6)
	public void EditViewletOption(String NewViewletName, ITestContext context) throws InterruptedException
	{		
		//Click on Dropdown menu
		driver.findElement(By.id("dropdownMenuButton")).click();
		
		//select Edit viewlet option
		driver.findElement(By.linkText("Edit viewlet")).click();
		Thread.sleep(LowSleep);
		
		//Change the viewlet name
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(NewViewletName);
		
		//Change viewlet type to queues
		driver.findElement(By.cssSelector(".object-type:nth-child(3)")).click();
		Thread.sleep(LowSleep);
		
		//Increase the result limit
		driver.findElement(By.xpath("//input[@type='number']")).clear();
		driver.findElement(By.xpath("//input[@type='number']")).sendKeys("10000");
		Thread.sleep(LowSleep);
		
		//click on Apply changes
		//driver.findElement(By.id("save-viewlet")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
		
		//Store the viewlet name into string
		String ViewletName=driver.findElement(By.cssSelector(".title-table")).getText();
		System.out.println("Viewlet Name is:" +ViewletName);
		
		if(ViewletName.equalsIgnoreCase(NewViewletName))
		{
			System.out.println("Viewlet Edit is working fine");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Edit viewlet option working");
		}
		else
		{
			System.out.println("Viewlet Edit is not working");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Edit viewlet option not working");
			driver.findElement(By.id("Edit option failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId=1170)
	@Test(priority=7)
	public void ManageFilteredColumns(ITestContext context) throws InterruptedException
	{
		//Click on Dropdown menu
		driver.findElement(By.id("dropdownMenuButton")).click();
		
		//Choose manage filter column value 
		try
		{
			driver.findElement(By.linkText("Manage Filtered Columns")).click();
			Thread.sleep(MediumSleep);
		}
		catch (Exception e)
		{
			driver.findElement(By.linkText("Stop Managing Filtered Columns")).click();
			Thread.sleep(LowSleep);
			
			//Click on Dropdown menu
			driver.findElement(By.id("dropdownMenuButton")).click();
			
			driver.findElement(By.linkText("Manage Filtered Columns")).click();
			Thread.sleep(MediumSleep);
		}
		
		//Choose column 
		driver.findElement(By.xpath("//span/i")).click();
		
		//Choose filter by chosen icon
		driver.findElement(By.xpath("//div[4]/div/i")).click();
		Thread.sleep(MediumSleep);
		
		//Enter input value
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys("999999999");
		Thread.sleep(LowSleep);
		
		//Get the viewlet data
		String Results=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println("Viewlet data is: " +Results);
		
		//Enter input value
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.ENTER);
		Thread.sleep(MediumSleep);
		
		//Clear filter by chosen 
		driver.findElement(By.xpath("//div[4]/div/i")).click();
		Thread.sleep(LowSleep);
		
		//Choose clear all selected filters icon
		driver.findElement(By.xpath("//div[4]/i")).click();
		Thread.sleep(LowSleep);
		
		
		//Click on Dropdown menu
		driver.findElement(By.id("dropdownMenuButton")).click();
		
		//Stop manage filter
		driver.findElement(By.linkText("Stop Managing Filtered Columns")).click();
		Thread.sleep(MediumSleep);
		
		if(Results.contains("No data to display"))
		{
			System.out.println("Manage filter is working fine");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Manage filter is working fine");
		}
		else
		{
			System.out.println("Manage filter is not working");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Manage filter is not working");
    		driver.findElement(By.id("Manage filter column failed")).click();
		}
		
	}
	
	
	@TestRail(testCaseId=1171)
	@Test(priority=8)
	public void ManageFrozenColumns(ITestContext context) throws InterruptedException
	{
		//Click on Dropdown menu
		driver.findElement(By.id("dropdownMenuButton")).click();
		
		//Choose manage filter column value 
		try
		{
			driver.findElement(By.linkText("Manage Frozen Columns")).click();
			Thread.sleep(MediumSleep);
		}
		catch (Exception e)
		{
			driver.findElement(By.linkText("Stop Managing Frozen Columns")).click();
			Thread.sleep(LowSleep);
			
			//Click on Dropdown menu
			driver.findElement(By.id("dropdownMenuButton")).click();
			
			driver.findElement(By.linkText("Manage Frozen Columns")).click();
			Thread.sleep(MediumSleep);
		}
		
		//get the frozen column name
		String FrozenColumnName=driver.findElement(By.xpath("//datatable-header-cell[7]")).getText();
		System.out.println("column name is before frozen: " +FrozenColumnName);
		
		//Click on frozen icon in column
		driver.findElement(By.xpath("//datatable-header-cell[7]/div/span/i")).click();
		Thread.sleep(MediumSleep);
		
		//Get results column name
		String ResultColumnName=driver.findElement(By.xpath("//datatable-header-cell[4]/div")).getText();
		System.out.println("column name is after frozen: " +ResultColumnName);
		
		//Clear frozen columns
		driver.findElement(By.xpath("//div[4]/i")).click();
		Thread.sleep(LowSleep);
		
		//Click on Dropdown menu
		driver.findElement(By.id("dropdownMenuButton")).click();
		
		//Stop manage filter
		driver.findElement(By.linkText("Stop Managing Frozen Columns")).click();
		Thread.sleep(MediumSleep);
		
		if(ResultColumnName.equalsIgnoreCase(FrozenColumnName))
		{
			System.out.println("Manage frozen column is working fine");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Manage frozen column is working fine");
		}
		else
		{
			System.out.println("Manage frozen column is not working");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Manage frozen column is not working");
    		driver.findElement(By.id("Manage frozen column failed")).click();
		}
	}
	
	@TestRail(testCaseId=818)
	@Test(priority=9)
	public void DeleteViewletOption(ITestContext context) throws InterruptedException
	{
		//Store the viewlet name into string
		String BeforeDeleteViewletName=driver.findElement(By.cssSelector(".title-table")).getText();
		System.out.println("Viewlet Name is:" +BeforeDeleteViewletName);
		
		//Click on Dropdown menu
		driver.findElement(By.id("dropdownMenuButton")).click();
		
		//select Edit viewlet option
		driver.findElement(By.linkText("Delete viewlet")).click();
		Thread.sleep(1000);
		
		//Confirmation ok button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(MediumSleep);
		
		//Store the viewlet name into string
		String AfterDeleteViewletName=driver.findElement(By.cssSelector(".block-title-row")).getText();
		System.out.println("Viewlet page data:" +AfterDeleteViewletName);
		
		if(AfterDeleteViewletName.equalsIgnoreCase(BeforeDeleteViewletName))
		{
			System.out.println("Delete option is not working");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Delete viewlet option not working");
			driver.findElement(By.id("Delete viewlet option failed")).click();
		}
		else
		{
			System.out.println("Delete option is working fine");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Delete viewlet option working");
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId=991)
	@Test(priority=10)
	public static void ExportDataToCSV(ITestContext context) throws InterruptedException
	{
		try
		{
		//Click on Dropdown menu
		driver.findElement(By.id("dropdownMenuButton")).click();
		
		//select export to csv option
		driver.findElement(By.linkText("Export data to CSV")).click();
		Thread.sleep(HighSleep);
		
		System.out.println("Data is exported to csv");
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Data is exported to csv");
		
		}
		catch(Exception e)
		{
			System.out.println("Data is not exported to csv");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Data is not exported to csv");
			driver.findElement(By.id("export to csv failed")).click();
		}
	}
	
	
	@Test(priority=11)
	public void ImportViewletFromFileCheckbox() throws InterruptedException
	{
		//Click on viewlet and choose import viewlet check box
		driver.findElement(By.id("add-viewlet")).click();
		driver.findElement(By.id("importViewlet")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		Thread.sleep(4000);
		
		//Click on choose file button
		WebElement element=driver.findElement(By.xpath("//div[2]/input"));
		Thread.sleep(LowSleep);
		
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].click();", element);
		Thread.sleep(HighSleep);
		
		try
		{
		//Loading the file into queue by using robot class
		String filepath1=System.getProperty("user.dir") + "\\" + UploadDashboard;
		StringSelection stringSelection = new StringSelection(filepath1);
		System.out.println("File path is: " +filepath1);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection,null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(HighSleep);
		}
		catch(Exception e)
		{
			System.out.println("error code is: " +e.getStackTrace());
		}
		
		//Click on import button
		driver.findElement(By.xpath("//button[contains(.,'Import')]")).click();
		Thread.sleep(MediumSleep);
		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=20)
	public static void Logout(String Dashboardname) throws InterruptedException
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
