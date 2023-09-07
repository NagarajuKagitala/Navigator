package NavigatorCore;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testrail.Settings;
import testrail.TestClass;
@Listeners(TestClass.class)
public class Display_Schemas 
{
static WebDriver driver;
	
	static String Screenshotpath;
	static String WGSName;
	static String Manager1;
	static String Manager2;
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
		Manager1 =Settings.getManager1();
		Manager2 =Settings.getManager2();
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
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(HighSleep);
	}
	
	@Parameters({"schemaname","nodename","viewletname"})
	@Test(priority=1)
	public static void CreateSchema(String schemaname, String nodename, String viewletname) throws InterruptedException
	{
		//for clicking on settings button
		driver.findElement(By.xpath("//i[5]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//div/div[12]")).click();
		Thread.sleep(LowSleep);
		
		//for deleting the schema if exist
		WebElement k=driver.findElement(By.className("tables")).findElement(By.className("left")).findElement(By.className("schemas-table")).findElement(By.tagName("tbody"));
		List<WebElement> l=k.findElements(By.tagName("tr"));
		
		System.out.println("number of schemas are:" +l.size());
		for(WebElement m:l)
		{
			System.out.println("names of schemas:"+m.getText());
			if(m.getText().equalsIgnoreCase(schemaname))
			{
				m.click();
				driver.findElement(By.xpath("//div[3]/button[4]")).click();
				Thread.sleep(LowSleep);
				driver.findElement(By.id("accept-true")).click();
				Thread.sleep(LowSleep);
				break;
			}
		}
		
		driver.findElement(By.xpath("//app-modal-main-settings-display-schema/div/div/ng-select/div/span")).click();
		Thread.sleep(LowSleep);
		
		//for selcting node IBM MQ from dropdown
		WebElement a=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> b=a.findElements(By.tagName("div"));
		System.out.println("number of divs:"+b.size());
		
		for(WebElement c:b)
		{
			System.out.println("names of nodes are:"+c.getText());
			if(c.getText().equalsIgnoreCase(nodename))
			{
				c.click();
				break;
			}
		}
		
		
		driver.findElement(By.xpath("//app-modal-main-settings-display-schema/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(LowSleep);
		
		//for selecting viewlet type from dropdown
		WebElement p=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> q=p.findElements(By.tagName("div"));
		System.out.println("number of divs are:"+ q.size());
		
		for(WebElement r:q)
		{
			System.out.println("list od viewlets are:"+r.getText());
			if(r.getText().equalsIgnoreCase(viewletname))
			{
				r.click();
				break;
			}
		}
		
		//for creating schema
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.name("name")).sendKeys(schemaname);
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//div/div/div/div/table/tbody/tr/td")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//tr[7]/td")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div[2]/div[2]/button")).click();
		Thread.sleep(LowSleep);
		
		//for testing the schema is created or not
		StringBuffer buffer=new StringBuffer();
		WebElement a1=driver.findElement(By.className("tables")).findElement(By.className("left")).findElement(By.className("schemas-table")).findElement(By.tagName("tbody"));
		List<WebElement> a2=a1.findElements(By.tagName("tr"));
		System.out.println("number of schemas are:" +a2.size());
		for(WebElement a3:a2)
		{
			System.out.println("schema name:"+a3.getText());
			buffer.append(a3.getText());
			buffer.append(",");
		}
		
		String schemas=buffer.toString();
		System.out.println("List of schemas are: " +schemas);
		if(schemas.contains(schemaname))
		{
			System.out.println("schema is created");
		}
		else
		{
			System.out.println("schema is not created");
		}
		
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(LowSleep);
		
	}
	
	@Parameters({"schemaname","nodename","viewletname","ownername"})
	@Test(priority=2)
	public static void Changeowner(String schemaname, String nodename, String viewletname, String ownername) throws InterruptedException
	{
		//for clicking on settings button
		driver.findElement(By.xpath("//i[5]")).click();
		Thread.sleep(LowSleep);
		//for clicking on edit global settings
		driver.findElement(By.xpath("//button[contains(.,'Edit global settings')]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//div/div[14]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//app-modal-main-settings-user-object-ownership-management/div/ul/li[2]/div/div")).click();
		Thread.sleep(LowSleep);
		
		driver.findElement(By.xpath("//app-modal-display-schema-ownership-change/div/div/ng-select/div/span")).click();
		Thread.sleep(LowSleep);
		
		//for selcting node IBM MQ from dropdown
		WebElement a=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> b=a.findElements(By.tagName("div"));
		//System.out.println("number of divs:"+b.size());
		
		for(WebElement c:b)
		{
			//System.out.println("names of nodes are:"+c.getText());
			if(c.getText().equalsIgnoreCase(nodename))
			{
				c.click();
				break;
			}
		}
		driver.findElement(By.xpath("//app-modal-display-schema-ownership-change/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(LowSleep);
		
		//for selecting viewlet type from dropdown
		WebElement p=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> q=p.findElements(By.tagName("div"));
		//System.out.println("number of divs are:"+ q.size());
		
		for(WebElement r:q)
		{
			//System.out.println("list od viewlets are:"+r.getText());
			if(r.getText().equalsIgnoreCase(viewletname))
			{
				r.click();
				break;
			}
		}
		driver.findElement(By.xpath("//app-modal-user-object-ownership-table/div/div/input[2]")).click();
		Thread.sleep(LowSleep);
		
		//for searching the schema was there or not 
		WebElement k=driver.findElement(By.className("content-tab-holder")).findElement(By.className("ta")).
				findElement(By.tagName("app-modal-user-object-ownership-table")).findElement(By.className("container-fluid")).
				findElement(By.className("maf-container")).findElement(By.className("left-side")).findElement(By.tagName("table"));
		//System.out.println("My html data: " +k.getAttribute("innerHTML"));
		List<WebElement> l=k.findElements(By.tagName("tr"));
		System.out.println("schemas number:" +l.size());
		for(WebElement m:l)
		{
			//System.out.println("html data: " +m.getAttribute("innerHTML"));
			System.out.println("schemas name:"+m.getText());
			if(m.getText().contains(schemaname))
			{
				m.click();
				break;
			}
		}
		driver.findElement(By.xpath("//app-modal-user-object-ownership-table/div/div[3]/div/div/div/button")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//app-modal-user-ownership-selection-table/div/div/div/input")).sendKeys(ownername);
		Thread.sleep(LowSleep);      
		driver.findElement(By.xpath("//app-modal-user-ownership-selection-table/div/div/div/input[2]")).click();
		Thread.sleep(LowSleep);      
		driver.findElement(By.xpath("//button[contains(.,' Set Owner')]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(LowSleep);
		
		//for searching that owner changed or not
		driver.findElement(By.xpath("//app-modal-user-object-ownership-table/div/div/input")).sendKeys(schemaname);
		Thread.sleep(LowSleep);      
		driver.findElement(By.xpath("//app-modal-user-object-ownership-table/div/div/input[2]")).click();
		Thread.sleep(LowSleep);
		                                            
		String owner=driver.findElement(By.xpath("//td[3]")).getText();
		System.out.println("changing ownername:"+owner);
		if(owner.equalsIgnoreCase(ownername))
		{
			System.out.println("ownername is changed");
		}
		else
		{
			System.out.println("ownername is not changed");
		}
		
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(LowSleep);
		
	}
	
	@Test(priority=9)
	public static void Logout() throws InterruptedException
	{
		driver.findElement(By.xpath("//i[9]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.id("yesButton")).click();
		Thread.sleep(LowSleep);
		
		driver.close();
    }

}
