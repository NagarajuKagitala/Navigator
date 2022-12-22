package ApodGUI;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testrail.Settings;

public class RemoteQueueViewlet 
{
static WebDriver driver;
	
	@Parameters({"sDriver", "sDriverpath", "URL", "uname", "password", "Dashboardname", "wgs", "QueueTypeName"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String URL, String uname, String password, String Dashboardname, int wgs, String QueueTypeName) throws Exception
	{
		Settings.read();
		String urlstr=Settings.getSettingURL();
		URL= urlstr+URL;
		
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
		Thread.sleep(2000);
		
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		driver.findElement(By.id("createInitialViewlets")).click();
		
		
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByIndex(wgs);
		
		/*//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);
		
		//Edit viewlet 
		driver.findElement(By.xpath("//div/div/div[2]/div[2]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Change the queue type to Model
		Select Queuetype=new Select(driver.findElement(By.name("queueType")));
		Queuetype.selectByVisibleText(QueueTypeName);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
	}
	
	
	@Parameters({"Attributesdata"})
	@Test(priority=1)
	public static void ShowObjectAttributesForQueues(String Attributesdata) throws InterruptedException
	{
		//Select show object Attributes Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
		Thread.sleep(1000);
		
		String AttributeName=driver.findElement(By.cssSelector("th:nth-child(1)")).getText();
		System.out.println(AttributeName);
		
		//Verification Condition
		if(AttributeName.equalsIgnoreCase(Attributesdata))
		{
			System.out.println("Show Object Attribute page is opened");
		}
		else
		{
			System.out.println("Show Object Attribute page is not opened");
			driver.findElement(By.xpath("Show object attributes opening failed")).click();
		}
		//Close the show object attributes page
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);
		
		/*ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.QueuesAttributesVerification(driver, schemaName);*/
		
	}
		
	@Parameters({"ObjectName", "ObjectDescription"})
	@Test(priority=2)
	public static void QueueCommands(String ObjectName, String ObjectDescription) throws InterruptedException
	{
		//Select Commands option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions CopyMousehover=new Actions(driver);
		CopyMousehover.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]/ul/li")).click();
		Thread.sleep(1000);
		
		//Object Details
		driver.findElement(By.xpath("//div[2]/div/input")).sendKeys(ObjectName);
		driver.findElement(By.xpath("//div[2]/input")).sendKeys(ObjectDescription);
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(3000);
		
		//---------  Delete the Queue -----------
		//Take the Queue name whcih one you want to delete
		String Queuenamebefore=driver.findElement(By.xpath("//datatable-body-cell[3]/div/span")).getText();
		System.out.println(Queuenamebefore);
		
		//Select Commands option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions DeleteMousehover=new Actions(driver);
		DeleteMousehover.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]/ul/li[2]")).click();
		Thread.sleep(1000);
		
		//Delete option
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(4000);
		
		String Queuenameafterdelete=driver.findElement(By.xpath("//datatable-body-cell[3]/div/span")).getText();
		System.out.println(Queuenameafterdelete);
		
		if(Queuenamebefore.equalsIgnoreCase(Queuenameafterdelete))
		{
			System.out.println("Queue is not deleted");
			driver.findElement(By.xpath("Queue Delete failed")).click();
		}
		else
		{
			System.out.println("Queue is deleted Successfully");
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=3)
	public static void QueueProperties() throws InterruptedException
	{
		//Select Queue properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]")).click();
		Thread.sleep(1000);
		
		String ProperiesHeading=driver.findElement(By.cssSelector(".modal-title")).getText();
		System.out.println(ProperiesHeading);
		
		if(ProperiesHeading.contains("Properties"))
		{
			System.out.println("Queues properties page is opened");
		}
		else
		{
			System.out.println("Queues properties page is not opened");
			driver.findElement(By.xpath("Queues properties failed")).click();
		}
		
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(1000);
	}
	
	@Test(priority=4)
	public static void QueueEvents() throws InterruptedException
	{
		//Select Queue Events option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
		Thread.sleep(1000);
		
		//Events Popup page
		String Eventdetails=driver.findElement(By.cssSelector("th:nth-child(1)")).getText();
		//System.out.println(Eventdetails);
		
		//Verification condition
		if(Eventdetails.equalsIgnoreCase("Event #"))
		{
			System.out.println("Events page is opened");
		}
		else
		{
			System.out.println("Events page is not opened");
			driver.findElement(By.xpath("Events failed")).click();
		}
		
		//Clicking on Events Count
		try
		{
		if(driver.findElement(By.cssSelector("td:nth-child(1)")).isDisplayed())
		{
		driver.findElement(By.cssSelector("td:nth-child(1)")).click();
					
		driver.findElement(By.cssSelector("button:nth-child(2)")).click();
		Thread.sleep(1000);
		if(driver.getPageSource().contains("Detail"))
		{
			driver.findElement(By.cssSelector("button:nth-child(3)")).click();
			Thread.sleep(1000);
			driver.findElement(By.cssSelector("button.g-button-red")).click();
		}
		else
		{
		driver.findElement(By.cssSelector("button.g-button-red")).click();
		}
					
		//close events popup page
		driver.findElement(By.cssSelector("button.g-button-red")).click();
		Thread.sleep(1000);
		}
		else
		{
			//close events popup page
			driver.findElement(By.xpath("//app-console-tabs/div/div/ul/li/div/div[2]/i")).click();
			Thread.sleep(1000);
		}
		}
		
		catch (Exception e) {
            System.out.println("Events are not present");
        }
		
		//Close the Events popup page
		driver.findElement(By.xpath("//app-console-tabs/div/div/ul/li/div/div[2]/i")).click();
		Thread.sleep(1000);
	}
	
	@Parameters({"FavoriteViewletName", "Favwgs" })
	@Test(priority=5)
	public void AddToFavoriteViewlet(String FavoriteViewletName, int Favwgs) throws InterruptedException
	{
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		
		Select wgsdropdown=new Select(driver.findElement(By.name("wgs")));
		wgsdropdown.selectByIndex(Favwgs);
		
		//Submit
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(2000);
		
		//Store Queue name
		String QueueFav=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span")).getText();
		
		//Select Queue Events option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[7]")).click();
		Thread.sleep(1000);
		
		//Select the favorite viewlet name
		Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(2000);
		
		//Store fav data
		String Favdata=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		if(Favdata.contains(QueueFav))
		{
			System.out.println("Queue is added to favorite viewlet");
		}
		else
		{
			System.out.println("Queue is not added to favorite viewlet");
			driver.findElement(By.xpath("Queue not add to favorite")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@Test(priority=6)
	public static void CompareQueues() throws InterruptedException
	{
		//Select Queue Events option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
		Thread.sleep(1000);
		
		String CompareQueues=driver.findElement(By.cssSelector("span.name")).getText();
		System.out.println(CompareQueues);
		
		if(CompareQueues.equalsIgnoreCase("Compare"))
		{
			System.out.println("Managers Compare page is opened");
		}
		else
		{
			System.out.println("Managers Compare page is opened");
			driver.findElement(By.xpath("Comparision failed")).click();
		}
		Thread.sleep(1000);
		
		//Differences only
		driver.findElement(By.cssSelector("div.differences > label.switch > span.slider.round")).click();
		
		try
		{
				
		String difference1=driver.findElement(By.xpath("//td[2]")).getText();
		String difference2=driver.findElement(By.xpath("//td[3]")).getText();
					
		if(difference1.equalsIgnoreCase(difference2))
		{
			System.out.println("Popup not showing the Differences");
			driver.findElement(By.xpath("Differences")).click();
		}
		else
		{
			System.out.println("Popup showing the Differences");
		}
		}
			
		catch (Exception e) {
	     // TODO Auto-generated catch block
	        System.out.println("No differences between Processes");
	       } 
		 
		//Closing the compare popup page
		driver.findElement(By.cssSelector(".close-button")).click();
	    Thread.sleep(1000);	
	}
	
	
	@Parameters({"FavoriteViewletName"})
	@Test(priority=7)
	public static void AddToFavoriteForMultipleQueues(String FavoriteViewletName) throws InterruptedException
	{
		//Store Queue names
		String Queue2=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span")).getText();
		String Queue3=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span")).getText();
		
		//Select Queue Events option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[6]")).click();
		Thread.sleep(1000);
		
		//Select the favorite viewlet name
		Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(2000);
		
	
		//Store fav data
		String Favdata=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		if(Favdata.contains(Queue2)  && Favdata.contains(Queue3))
		{
			System.out.println("Multiple Queues are added to Favorite viewlet");
		}
		else
		{
			System.out.println("Multiple Queues are not added to Favorite viewlet");
			driver.findElement(By.xpath("Multiple queues to fav failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"SearchInputData"})
	@Test(priority=8)
	public static void SearchFilter(String SearchInputData) throws InterruptedException
	{
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(SearchInputData);
		Thread.sleep(2000);
		
		String Viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
		//System.out.println(Viewletdata);
		            
	    if(Viewletdata.toUpperCase().contains(SearchInputData.toUpperCase()))
	    {
	       System.out.println("Search is working fine");
	    }
	    else
	    {
	       System.out.println("Search is not working fine");
	       driver.findElement(By.xpath("Search is failed")).click();
	    }
	    Thread.sleep(2000);
	   
	}
	
	
	@Test(priority=9)
	public static void Logout() throws Exception
	{
		// Changing the Settings 
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(1000);
		
		//Delete the Dashboard
		driver.findElement(By.xpath("//li[2]/div/div[2]/i")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		//Logout
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		driver.close();
	}

}
