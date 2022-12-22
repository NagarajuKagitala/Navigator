package NavigatorCore;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import Common.Viewlets;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class ViewletFilterOptions 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName;
	static String Node_Hostname;
	static String Manager1; 
	static String Manager1Queuename;
	static String Manager="";
	static String Object="";
	static int countvalue=0;
	static int syscount=0;
	static String ResultLimitvalue="";
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
		Manager1 =Settings.getManager1();
		Manager1Queuename =Settings.getManager1Queuename();
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
		Thread.sleep(LowSleep);
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
						
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(MediumSleep);
		
		//--------- Create Queue viewlet-----------
		//Click on Viewlet		
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		Thread.sleep(LowSleep);
		
		 //Restore Default settings
		  driver.findElement(By.cssSelector(".fa-cog")).click();
		  Thread.sleep(LowSleep);
		  driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click(); 
		  Thread.sleep(LowSleep);
		  driver.findElement(By.id("accept-true")).click();
		  driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		  Thread.sleep(HighSleep);
	}
	
	@TestRail(testCaseId=1013)
	@Test(priority=1)
	public void AdvancedViewletFiletercheckbox(ITestContext context) throws InterruptedException
	{
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		
		//Show Empty Queues check box
		WebElement Checkbox=driver.findElement(By.id("advanced-filtering"));
		if(Checkbox.isSelected())
		{
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		else
		{
			Checkbox.click();
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		Thread.sleep(HighSleep);
		
		//Store criteria into string
		String Filter=driver.findElement(By.cssSelector(".col-lg-3 b")).getText();
		System.out.println("Filter data is: " +Filter);
		
		if(Filter.contains("Criteria"))
		{
			System.out.println("Advanced filter check box is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "viewlet filter checkbox is working fine");
		}
		else
		{
			System.out.println("Advanced filter checkbox is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "viewlet filter checkbox is not working fine");
		    driver.findElement(By.xpath("viewlet filter is failed")).click();
		}
	}
	
	@TestRail(testCaseId=1014)
	@Test(priority=2)
	public void ManagerFilter(ITestContext context) throws InterruptedException
	{
		//Click on manager drop down and select manager
		driver.findElement(By.xpath("//div[2]/div/div/ng-select/div/span[2]")).click();
		Thread.sleep(MediumSleep);
		
		try 
		{
        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
			List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
			System.out.println(mdivs.size());	
			
			for (WebElement mdi : mdivs)
			{
				//System.out.println("Managers: " +mdi.getText());
				if(mdi.getText().equals(Manager1))
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
        Thread.sleep(HighSleep);
        
        //Get the results count into string
        String res=driver.findElement(By.cssSelector(".pull-left")).getText();
       // System.out.println("Count :" +res);
        
        String[] cou = res.split(" "); 
        int count=Integer.parseInt(cou[1]);
        System.out.println("values: "+count);
        if(count>=10)
        {
        for(int i=2; i<11; i++)
        {
        	String ManagerResult=driver.findElement(By.xpath("//datatable-row-wrapper["+ i +"]/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
        	System.out.println("Managers are: " +ManagerResult);
        	
        	if(ManagerResult.equalsIgnoreCase(Manager1))
        	{
        		System.out.println("manager filter working fine");
        		context.setAttribute("Status",1);
    			context.setAttribute("Comment", "Manager filter is working fine");
        	}
        	else
        	{
        		System.out.println("Manager filter not working");
        		context.setAttribute("Status",5);
    			context.setAttribute("Comment", "Manager filter is not working fine");
    			ClearManager();
    			driver.findElement(By.id("manager filter failed")).click();
        	}
        }
        
        ClearManager();
        Thread.sleep(LowSleep);
        }
        else
        {
        	 for(int i=2; i<10; i++)
             {                                                    
             	String ManagerResult=driver.findElement(By.xpath("//datatable-row-wrapper["+ i +"]/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
             	System.out.println("Else condition Managers are: " +ManagerResult);
             	
             	if(ManagerResult.equalsIgnoreCase(Manager1))
             	{
             		System.out.println("manager filter working fine");
             		context.setAttribute("Status",1);
         			context.setAttribute("Comment", "Manager filter is working fine");
             	}
             	else
             	{
             		System.out.println("Manager filter not working");
             		context.setAttribute("Status",5);
         			context.setAttribute("Comment", "Manager filter is not working fine");
         			ClearManager();
         			driver.findElement(By.id("manager filter failed")).click();
             	}
             }
             
             ClearManager();
             Thread.sleep(3000);
        	
        }
	}
	
	
	
	@TestRail(testCaseId=1015)
	@Test(priority=3)
	public void ObjectFilter(ITestContext context) throws InterruptedException
	{
		//Get the Object name
		Object=driver.findElement(By.xpath("//datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Object name is: "+Object);
		
		//Send object name
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/div/input")).sendKeys(Object);
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/div/input")).sendKeys(Keys.ENTER);
		Thread.sleep(MediumSleep);
		
		//Get the results count into string
        String res=driver.findElement(By.cssSelector(".pull-left")).getText();
        //System.out.println("Count :" +res);
        
        String[] cou = res.split(" "); 
        int count=Integer.parseInt(cou[1]);
        System.out.println("values: "+count);
        if(count<=10)
        {
        	String ObjectResult=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
        	System.out.println("object filter results are: " +ObjectResult);
        	
        	if(ObjectResult.equalsIgnoreCase(Object) || ObjectResult.contains(Object))
        	{
        		System.out.println("Object filter working fine");
        		context.setAttribute("Status",1);
    			context.setAttribute("Comment", "object filter is working fine");
        	}
        	else
        	{
        		System.out.println("Object filter not working");
        		context.setAttribute("Status",5);
    			context.setAttribute("Comment", "Object filter is not working fine");
    			ClearObject();
    			driver.findElement(By.id("Object filter failed")).click();
        	}
        
    	ClearObject();
	}
        else
        {
            	String ObjectResult=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
            	System.out.println("Object filter Managers are: " +ObjectResult);
            	
            	if(ObjectResult.equalsIgnoreCase(Object) || ObjectResult.contains(Object))
            	{
            		System.out.println("Object filter working fine");
            		context.setAttribute("Status",1);
        			context.setAttribute("Comment", "object filter is working fine");
            	}
            	else
            	{
            		System.out.println("Object filter not working");
            		context.setAttribute("Status",5);
        			context.setAttribute("Comment", "Object filter is not working fine");
        			ClearObject();
        			driver.findElement(By.id("Object filter failed")).click();
            	}
            
        	ClearObject();
        }
        
	}
	
	@TestRail(testCaseId = 1047)
	@Parameters({"ConditionName", "CompareOperation"})
	@Test(priority=4)
	public void AttributeFilter(String ConditionName, String CompareOperation, ITestContext context) throws InterruptedException
	{
		try
		{
			//Get the depth value into string
			String Depth=driver.findElement(By.xpath("//datatable-body-cell[7]/div/span")).getText();
			System.out.println("Depth is: " +Depth);
			Thread.sleep(LowSleep);
			
		//Click on attribute filter
		driver.findElement(By.xpath("//div[1]/app-viewlet/div/div[2]/div/div/div[3]/div/div/div/button/i")).click();
		Thread.sleep(LowSleep);
				
		WebElement el=driver.findElement(By.className("maf-table-filters")).findElement(By.tagName("table"));
		List<WebElement> rows=el.findElements(By.tagName("tr"));
		//System.out.println("No of rows are :" +rows.size());
		
		for(WebElement r:rows)
		{
			if(r.getAttribute("class").contains("ng-star-inserted"))
			{
			System.out.println("text :" +r.getText());
					
			if(r.getText().contains(ConditionName))
			{
				r.click();
				driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
				Thread.sleep(MediumSleep);
				driver.findElement(By.id("accept-true")).click();
				break;
			}
			}
			
		}
		Thread.sleep(HighSleep);
		
		//Click on Add button
		driver.findElement(By.xpath("//button[contains(.,'Add...')]")).click();
		Thread.sleep(LowSleep);
		
		//Filter name
		driver.findElement(By.cssSelector(".filter-title")).sendKeys(ConditionName);
		driver.findElement(By.xpath("//div[5]/button")).click();
		Thread.sleep(LowSleep);
		
		//Search with attribute name
		driver.findElement(By.xpath("//app-mod-manage-attribute-filter-add-available-attr/div/div/div/div/input")).sendKeys("Maximum Depth");
		Thread.sleep(LowSleep);
		
		//Click on required attribute
		driver.findElement(By.xpath("//td/div")).click();
		Thread.sleep(LowSleep);
		 
		/*
		 * //Select the attribute String
		 * attribute=driver.findElement(By.xpath("//tr[12]/td/div")).getText();
		 * System.out.println("Attribute name is: " +attribute);
		 * 
		 * if(attribute.equalsIgnoreCase("Maximum Depth")) {
		 * driver.findElement(By.xpath("//tr[12]/td/div")).click();
		 * Thread.sleep(MediumSleep); } else {
		 * driver.findElement(By.xpath("//tr[23]/td/div")).click();
		 * Thread.sleep(MediumSleep); }
		 */
		
		//driver.findElement(By.xpath("//td[contains(.,'Maximum Depth')]")).click();
		driver.findElement(By.xpath("//app-mod-manage-attribute-filter-add-available-attr/div/div/div[3]/button")).click();
		Thread.sleep(MediumSleep);
		
		//Comparison operater name
		Select Compare=new Select(driver.findElement(By.xpath("//select")));
		Compare.selectByVisibleText(CompareOperation);
		Thread.sleep(LowSleep);
		
		/*//Filter the data
		driver.findElement(By.xpath("//app-mod-manage-attribute-filter-add-available-attr/div/div/div/div/input")).sendKeys(RowValue);
		driver.findElement(By.xpath("//td/div")).click();
		
		driver.findElement(By.xpath("(//button[@type='button'])[11]")).click();
		Thread.sleep(2000);*/
		
		//Enter the Condition value 
		driver.findElement(By.cssSelector(".filter-value")).click();
		driver.findElement(By.cssSelector(".filter-value")).sendKeys(Depth);
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
		Thread.sleep(HighSleep);
		
		String Noofqueues=driver.findElement(By.xpath("//datatable-footer/div/div/span")).getText();
		//System.out.println("no of Queues are: " +Noofqueues);
		
		String[] Index1=Noofqueues.split(" Visible");
		String[] Index2=Index1[0].split(": ");
		int k=Integer.parseInt(Index2[1]);
		System.out.println("No of results are: " +k);
		for(int i=2; i<=12; i++)
		{
			String Result=driver.findElement(By.xpath("//datatable-row-wrapper["+ i +"]/datatable-body-row/div[2]/datatable-body-cell[7]/div/span")).getText();
			int integer=Integer.parseInt(Result);     
			System.out.println("Values are: " +integer+ " ConditionData is: " +Depth);
			if(integer==Integer.parseInt(Depth))
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
			driver.findElement(By.xpath("//div[3]/div/div/div/input")).click();
			Thread.sleep(4000);
			driver.findElement(By.xpath("Attribute filter condition failed")).click();
		}
		
		//Deselect the condition     
		driver.findElement(By.xpath("//div[3]/div/div/div/input")).click();
		Thread.sleep(LowSleep);
		
	}
	
	
	@TestRail(testCaseId=1016)
	@Test(priority=5)
	public void EmptyCheckboxFilter(ITestContext context) throws InterruptedException
	{
		JavascriptExecutor Js1 = (JavascriptExecutor) driver;
		Js1.executeScript("window.scrollBy(0,100)"); 
		
		driver.findElement(By.xpath("//div[4]/div[2]/input")).clear();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//div[4]/div[2]/input")).click();
		Thread.sleep(LowSleep);
    	driver.findElement(By.xpath("//div[4]/div[2]/input")).sendKeys("10000");
    	Thread.sleep(LowSleep);
    	driver.findElement(By.xpath("//div[4]/div[2]/input")).sendKeys(Keys.ENTER);
    	Thread.sleep(HighSleep);
    	
		boolean empty=driver.findElement(By.xpath("(//input[@type='checkbox'])[2]")).isSelected();
		System.out.println("Checkbox status is: " +empty);
		
		if(empty)
		{
			System.out.println("Already selected");
			//Get the results count into string
	        String res=driver.findElement(By.cssSelector(".pull-left")).getText();
	       // System.out.println("Count :" +res);
	        
	        String[] cou = res.split(" "); 
	        countvalue=Integer.parseInt(cou[1]);
	        System.out.println("Intial count: "+countvalue);
	        Thread.sleep(MediumSleep);
	        
	        driver.findElement(By.xpath("(//input[@type='checkbox'])[2]")).click();
	        Thread.sleep(HighSleep);
		}
		else
		{
			//Get the results count into string
	        String res=driver.findElement(By.cssSelector(".pull-left")).getText();
	       // System.out.println("Count :" +res);
	        
	        String[] cou = res.split(" "); 
	        countvalue=Integer.parseInt(cou[1]);
	        System.out.println("Initial count: "+countvalue);
	        
			driver.findElement(By.xpath("(//input[@type='checkbox'])[2]")).click();
			 Thread.sleep(HighSleep);
		}
		
		//Get the results count into string
        String res1=driver.findElement(By.cssSelector(".pull-left")).getText();
        //System.out.println("Count :" +res1);
        
        String[] cou1 = res1.split(" "); 
        int CountAfter=Integer.parseInt(cou1[1]);
        System.out.println("Final count: "+CountAfter);
		
        if(CountAfter==countvalue)
        {
        	System.out.println("Empty checkbox failed");
        	context.setAttribute("Status",5);
			context.setAttribute("Comment", "Empty checkbox filter is not working");
			CheckEmptyCheckbox();
			driver.findElement(By.id("empty checkbox filter failed")).click();
        }
        else
        {
        	System.out.println("Empty checkbox is working fine");
        	context.setAttribute("Status",1);
			context.setAttribute("Comment", "Empty checkbox filter is working");
        }
		CheckEmptyCheckbox();
	}
	
	
	@TestRail(testCaseId=1017)
	@Test(priority=6)
	public void SystemCheckboxFilter(ITestContext context) throws InterruptedException
	{
		boolean empty=driver.findElement(By.xpath("//div[2]/div[2]/input")).isSelected();
		
		if(empty)
		{
			System.out.println("Already selected");
			//Get the results count into string
	        String res=driver.findElement(By.cssSelector(".pull-left")).getText();
	        //System.out.println("Count :" +res);
	        
	        String[] cou = res.split(" "); 
	        syscount=Integer.parseInt(cou[1]);
	        System.out.println("Intial count: "+syscount);
	        
	        driver.findElement(By.xpath("//div[2]/div[2]/input")).click();
	        Thread.sleep(MediumSleep);
		}
		else
		{
			//Get the results count into string
	        String res=driver.findElement(By.cssSelector(".pull-left")).getText();
	       // System.out.println("Count :" +res);
	        
	        String[] cou = res.split(" "); 
	        syscount=Integer.parseInt(cou[1]);
	        System.out.println("Initial count: "+syscount);
	        
			driver.findElement(By.xpath("//div[2]/div[2]/input")).click();
			 Thread.sleep(MediumSleep);
		}
		
		//Get the results count into string
        String res1=driver.findElement(By.cssSelector(".pull-left")).getText();
        //System.out.println("Count :" +res1);
        
        String[] cou1 = res1.split(" "); 
        int CountAfter=Integer.parseInt(cou1[1]);
        System.out.println("Final count: "+CountAfter);
		
        if(CountAfter==syscount)
        {
        	System.out.println("System checkbox failed");
        	context.setAttribute("Status",5);
			context.setAttribute("Comment", "System checkbox filter is not working");
			UncheckSystemCheckbox();
			driver.findElement(By.id("System checkbox filter failed")).click();
        }
        else
        {
        	System.out.println("System checkbox is working fine");
        	context.setAttribute("Status",1);
			context.setAttribute("Comment", "System checkbox filter is working");
        }
		UncheckSystemCheckbox();
	}
	
	@TestRail(testCaseId=1018)
	@Test(priority=7)
	public void ResultLimitFilter(ITestContext context) throws InterruptedException
	{
		 String res=driver.findElement(By.cssSelector(".pull-left")).getText();
	    // System.out.println("Count :" +res);
	        
	     String[] cou = res.split(" "); 
	     int Initialsyscount=Integer.parseInt(cou[1]);
	     System.out.println("Intial count: "+Initialsyscount);
	        
	    if(Initialsyscount==1)
	    {
	    	System.out.println("viewlet has 1 record only");
	    }
	    else
	    {
	    	JavascriptExecutor Js1 = (JavascriptExecutor) driver;
			Js1.executeScript("window.scrollBy(0,100)");
			
	    	ResultLimitvalue=driver.findElement(By.xpath("//div[4]/div[2]/input")).getAttribute("value");
	    	System.out.println("Initial limit is: " +ResultLimitvalue);
	    	
	    	driver.findElement(By.xpath("//div[4]/div[2]/input")).clear();
	    	Thread.sleep(MediumSleep);
	    	driver.findElement(By.xpath("//div[4]/div[2]/input")).click();
	    	Thread.sleep(LowSleep);
	    	driver.findElement(By.xpath("//div[4]/div[2]/input")).sendKeys("1");
	    	 Thread.sleep(MediumSleep);
	    	driver.findElement(By.xpath("//div[4]/div[2]/input")).sendKeys(Keys.ENTER);
	    	Thread.sleep(MediumSleep);
	    	
	    	 String res1=driver.findElement(By.cssSelector(".pull-left")).getText();
		     //System.out.println("Count :" +res1);
		        
		     String[] cou1 = res1.split(" "); 
		     int Finalsyscount=Integer.parseInt(cou1[1]);
		     System.out.println("Final count: "+Finalsyscount);
		     
		     if(Finalsyscount==1)
		     {
		    	 System.out.println("Result limit is working fine");
		    	 context.setAttribute("Status",1);
				 context.setAttribute("Comment", "Result limit filter is working");
		     }
		     else
		     {
		    	 System.out.println("Result limit is not working");
		    	 context.setAttribute("Status",5);
				 context.setAttribute("Comment", "Result limit filter is not working");
				 driver.findElement(By.xpath("//div[4]/div[2]/input")).clear();
				 Thread.sleep(MediumSleep);
				 driver.findElement(By.xpath("//div[4]/div[2]/input")).click();
				 Thread.sleep(LowSleep);
			     driver.findElement(By.xpath("//div[4]/div[2]/input")).sendKeys("1000");
			     Thread.sleep(MediumSleep);
			     driver.findElement(By.xpath("//div[4]/div[2]/input")).sendKeys(Keys.ENTER);
			     Thread.sleep(MediumSleep);
			     
			     driver.findElement(By.id("Result limit failed")).click();
		     }
	    }
	    
	    driver.findElement(By.xpath("//div[4]/div[2]/input")).clear();
	    Thread.sleep(MediumSleep);
	    driver.findElement(By.xpath("//div[4]/div[2]/input")).click();
	    Thread.sleep(LowSleep);
    	driver.findElement(By.xpath("//div[4]/div[2]/input")).sendKeys("1000");
    	 Thread.sleep(MediumSleep);
    	driver.findElement(By.xpath("//div[4]/div[2]/input")).sendKeys(Keys.ENTER);
    	Thread.sleep(MediumSleep);
		
	}
	
	@TestRail(testCaseId = 1048)
	@Test(priority=8)
	public void ViewletCheckboxClear(ITestContext context) throws InterruptedException 
	{
		WebElement checkbox=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"));
		
		checkbox.click();
		Thread.sleep(MediumSleep);
		
		boolean status=checkbox.isSelected();
		System.out.println("Check box status is: " +status);
		
		//Click on clear check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(MediumSleep);
		
		boolean Afterstatus=checkbox.isSelected();
		System.out.println("Check box status is after clear: " +Afterstatus);
		
		if(!Afterstatus==status)
		{
			System.out.println("All checkboxes clear option is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "All checkboxes clear option is working fine");
		}
		else
		{
			System.out.println("All checkboxes clear option is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "All checkboxes clear option is not working");
			driver.findElement(By.id("All checkboxes clear failed")).click();
		
		}
	}

	@Parameters({"Dashboardname"})
	@Test(priority=30)
	public static void Logout(String Dashboardname) throws Exception
	{
		// Changing the Settings 
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click();
		Thread.sleep(MediumSleep);
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(HighSleep);
		
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, Dashboardname);
	}
	
	public void ClearManager() throws InterruptedException
	{
		//Click on manager dropdown and remove selected manager
		driver.findElement(By.xpath("//div[2]/div/div/ng-select/div/div/div[3]/input")).click();
		driver.findElement(By.xpath("//div[2]/div/div/ng-select/div/div/div[3]/input")).sendKeys(Keys.BACK_SPACE);
		Thread.sleep(LowSleep);
		
		//Click on header in the navigator
		driver.findElement(By.xpath("//app-header/div")).click();
		Thread.sleep(MediumSleep);
		
		
		/*
		 * WebElement
		 * els=driver.findElement(By.className("ng-select-container")).findElement(By.
		 * className("ng-value-container")); List<WebElement>
		 * tag=els.findElements(By.tagName("span")); System.out.println("Size is: "
		 * +tag.size());
		 * 
		 * for(WebElement e : tag) { System.out.println("Title is :"
		 * +e.getAttribute("title")); System.out.println("html :"
		 * +e.getAttribute("innerHTML")); if(e.getAttribute("innerHTML").contains("�"))
		 * { e.click(); System.out.println("Element clicked"); break; } }
		 */
	}
	
	private void ClearObject() throws InterruptedException 
	{
		//Clear object name
        for(int i=1; i<=Object.length(); i++)
        {
        	driver.findElement(By.xpath("//div[2]/div/div/div[2]/div/input")).sendKeys(Keys.BACK_SPACE);
        	Thread.sleep(1000);            
        }
        driver.findElement(By.xpath("//div[2]/div/div/div[2]/div/input")).sendKeys(Keys.ENTER);
        Thread.sleep(MediumSleep);
	}
	
	public void CheckEmptyCheckbox() throws InterruptedException
	{
		boolean empty=driver.findElement(By.xpath("(//input[@type='checkbox'])[2]")).isSelected();
		Thread.sleep(LowSleep);
		System.out.println("Status at the end: " +empty);
		
		if(empty)
		{
			System.out.println("Already selected");
		}
		else
		{	        
			driver.findElement(By.xpath("(//input[@type='checkbox'])[2]")).click();
			Thread.sleep(MediumSleep);
		}
	}
	
	public void UncheckSystemCheckbox() throws InterruptedException
	{
		boolean empty=driver.findElement(By.xpath("//div[2]/div[2]/input")).isSelected();
		
		if(empty)
		{
			System.out.println("Already selected");
			driver.findElement(By.xpath("//div[2]/div[2]/input")).click();
			Thread.sleep(MediumSleep);
		}
		else
		{	        
			
		}
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
