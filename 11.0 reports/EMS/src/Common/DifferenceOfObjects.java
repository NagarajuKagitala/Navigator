package Common;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;

public class DifferenceOfObjects 
{
	public void Differences(WebDriver driver, ITestContext context) throws InterruptedException
	{
		//Click on clear all check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(2000);
		
		// Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(4000);
		driver.findElement(By.linkText("Compare")).click();
		Thread.sleep(6000);
		
		// Check differences only option while compare
		driver.findElement(By.cssSelector(".differences .slider")).click();
		Thread.sleep(4000);
			
		try {
			
			WebElement ee=driver.findElement(By.className("paddless"));
			List<WebElement> AttributesData=ee.findElements(By.tagName("div"));
			System.out.println("Divs size is: " +AttributesData.size());
			
			System.out.println("AttributesData count: " + AttributesData.size());
			int k =0;
			for (int i = 1; i < AttributesData.size(); i++) 
			{
				String style = AttributesData.get(i).getAttribute("style");
				System.out.println("Style is: " +style);
				
				if (!(style.contains("display: none"))) 
				{
					System.out.println("difference index :" +i);
					k=i+1;
					break;
				}
				
			}
			
			System.out.println("After for loop: " +k);

			
			boolean verifydiff = false;
			if(k>0)
			{
			String difference1=driver.findElement(By.xpath("//span[2]/div["+ k +"]")).getText();
			System.out.println("First value: " +difference1);
			String difference2=driver.findElement(By.xpath("//span[3]/div["+ k +"]")).getText();
			System.out.println("Second value: " +difference2);

			if(!(difference1.isEmpty() && difference2.isEmpty()))
			{
			for (int i = 1; i < AttributesData.size(); i++) 
			{
				String cls = AttributesData.get(i).getAttribute("style");
				//System.out.println("classname: "+ cls);
				if (!cls.contains("display: none")) 
					{
					System.out.println("index: " + i);
					String secondvalue;
					String firstvalue;
					
					int j=i+1;
					firstvalue = driver.findElement(By.xpath("//span[2]/div["+ j +"]")).getText();
					System.out.println("First value in loop: " + firstvalue);
					secondvalue = driver.findElement(By.xpath("//span[3]/div["+ j +"]")).getText();
					System.out.println("Second value in loop: " + secondvalue);
					
					if (!firstvalue.equalsIgnoreCase(secondvalue)) 
					{
						verifydiff = true;
					}
				}

				}
			
			}
			else
			{
			verifydiff=true;
			}
			}
			else
			{
			verifydiff=true;
			}

			//System.out.println("");
			if (!verifydiff) {
				System.out.println("Popup showing the same values Differences");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Differences is not working");
				driver.findElement(By.xpath("Differences")).click();
			} else {
				System.out.println("Popup showing the Different values");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Showing the different values");

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Popup showing the same values Differences");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception while differentiate object values, check details: " + e.getMessage());
			driver.findElement(By.cssSelector(".close-button")).click();
			driver.findElement(By.xpath("Differences")).click();
		}
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);
		
	}
	
	
	public void DifferencesforChannel(WebDriver driver, ITestContext context) throws InterruptedException
	{
		//Click on clear all check box button
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(2000);
		
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys("Sender");
		Thread.sleep(3000);
		
		// Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(4000);
		driver.findElement(By.linkText("Compare")).click();
		Thread.sleep(6000);
		
		// Check differences only option while compare
		driver.findElement(By.cssSelector(".differences .slider")).click();
		Thread.sleep(4000);
			
		try {
			
			WebElement ee=driver.findElement(By.className("paddless"));
			List<WebElement> AttributesData=ee.findElements(By.tagName("div"));
			System.out.println("Divs size is: " +AttributesData.size());
			
			System.out.println("AttributesData count: " + AttributesData.size());
			int k =0;
			for (int i = 1; i < AttributesData.size(); i++) 
			{
				String style = AttributesData.get(i).getAttribute("style");
				System.out.println("Style is: " +style);
				
				if (!(style.contains("display: none"))) 
				{
					System.out.println("difference index :" +i);
					k=i+1;
					break;
				}
				
			}
			
			System.out.println("After for loop: " +k);

			boolean verifydiff = false;
			String difference1=driver.findElement(By.xpath("//span[2]/div["+ k +"]")).getText();
			System.out.println("First value: " +difference1);
			String difference2=driver.findElement(By.xpath("//span[3]/div["+ k +"]")).getText();
			System.out.println("Second value: " +difference2);

			if(!(difference1.isEmpty() && difference2.isEmpty()))
			{
			for (int i = 1; i < AttributesData.size(); i++) 
			{
				String cls = AttributesData.get(i).getAttribute("style");
				System.out.println("classname: "+ cls);
				if (!cls.contains("display: none")) 
					{
					System.out.println("index: " + i);
					String secondvalue;
					String firstvalue;
					
					int j=i+1;
					firstvalue = driver.findElement(By.xpath("//span[2]/div["+ j +"]")).getText();
					System.out.println("First value in loop: " + firstvalue);
					secondvalue = driver.findElement(By.xpath("//span[3]/div["+ j +"]")).getText();
					System.out.println("Second value in loop: " + secondvalue);
					
					if (!firstvalue.equalsIgnoreCase(secondvalue)) 
					{
						verifydiff = true;
					}
				}

				}
			
			}
			else
			{
			verifydiff=true;
			}

			//System.out.println("");
			if (!verifydiff) {
				System.out.println("Popup showing the same values Differences");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Differences is not working");
				driver.findElement(By.xpath("Differences")).click();
			} else {
				System.out.println("Popup showing the Different values");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Showing the different values");

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Popup showing the same values Differences");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception while differentiate object values, check details: " + e.getMessage());
			driver.findElement(By.cssSelector(".close-button")).click();
			driver.findElement(By.xpath("Differences")).click();
		}
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);	
		
	}

}
