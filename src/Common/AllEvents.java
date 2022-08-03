package Common;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITestContext;

public class AllEvents 
{
	
	public void Events(String Dashboardname, WebDriver driver, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select channel Events option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Events...")).click();
		Thread.sleep(25000);
		
		//Events Popup page
		String Eventdetails=driver.findElement(By.cssSelector(".custom-column-H-L-border")).getText();
		System.out.println("event name: " +Eventdetails);
				
		//Verification condition
		if(Eventdetails.equalsIgnoreCase("Event #"))
		{
			System.out.println("Events page is opened");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Events page is opened and working fine");
		}
		else
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to open events page");
			System.out.println("Events page is not opened");
			driver.findElement(By.xpath("Events failed")).click();
		}
				
		//Clicking on Events Count
		try 
		{
			if(driver.findElement(By.xpath("//span/div[2]")).isDisplayed())
			{
				String Eventcount=driver.findElement(By.xpath("//span/div[2]")).getText();
				System.out.println(Eventcount);
				driver.findElement(By.xpath("//span/div[2]")).click();
				Thread.sleep(16000);
				
				//Click on daignostic tab
				driver.findElement(By.xpath("//button[contains(.,'Diagnostic')]")).click();
				Thread.sleep(6000);
				
				//get the vents count and store the into string
				String DignosticCount=driver.findElement(By.name("event#")).getAttribute("value");
				System.out.println("Daignostic events count:" +DignosticCount);
				
				if(Eventcount.equalsIgnoreCase(DignosticCount))
				{
					System.out.println("Events count is matched");
					context.setAttribute("Status", 1);
					context.setAttribute("Comment", "Event Count is Matched and working fine");
					//Close the Event details page
					//driver.findElement(By.xpath("//app-mod-event-details/div/div[2]/button")).click();
					WebElement close=driver.findElement(By.xpath("//app-mod-event-details/div/div[2]/button"));
					JavascriptExecutor js = (JavascriptExecutor)driver;
					js.executeScript("arguments[0].click();", close);
				}
				else
				{
					System.out.println("Events count is not matched");
					context.setAttribute("Status", 5);
					context.setAttribute("Comment", "Got exception while opening events page, check details: ");
					driver.findElement(By.xpath("//app-mod-event-details/div/div[2]/button")).click();
					driver.findElement(By.cssSelector(".close-button")).click();
					driver.findElement(By.id("Events count failed")).click();
				}
				
			}
		}
		catch (Exception e)
		{
			System.out.println("Events are not present");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Events not found");
		}
				
		//Close the events popup page 
		driver.findElement(By.cssSelector(".close-button")).click();
		
	}

}
