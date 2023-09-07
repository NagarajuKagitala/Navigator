package Common;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ClearSelectionofCheckbox 
{
	
	public void Deselectcheckbox(String Dashboardname, WebDriver driver) throws InterruptedException
	{	
		  WebElement cla=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul")); 
		  List<WebElement> lis=cla.findElements(By.tagName("li"));
		  System.out.println("No of dashboards are: " +lis.size());
		  
		  for(WebElement li: lis) 
		  { 
			  //System.out.println("titles are: " +li.getAttribute("class")); 
			  WebElement fi=li.findElement(By.className("g-tab-title"));
			 // System.out.println("Names are: " +fi.getText());
			 // System.out.println("text content are: " +fi.getAttribute("textContent"));
			  
		  if(fi.getAttribute("textContent").equalsIgnoreCase("WorkSpace")) 
		  { 
			  WebElement element=fi;
			  JavascriptExecutor js = (JavascriptExecutor)driver;
			  js.executeScript("arguments[0].click();", element);
			  //Actions a=new Actions(driver); 
			  //a.click(fi).perform(); 
			  Thread.sleep(5000); 
			  break; 
		  } 
		  }
		  
		  WebElement cla1=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul")); 
		  List<WebElement> lis1=cla1.findElements(By.tagName("li"));
		  System.out.println("No of dashboards are: " +lis1.size());
		  
		  for(WebElement li1: lis1) 
		  { 
			  //System.out.println("titles are: "+li.getAttribute("class")); 
			  WebElement fi1=li1.findElement(By.className("g-tab-title"));
		     //System.out.println("Names are: " +fi.getText());
		  
		  if(fi1.getAttribute("textContent").equalsIgnoreCase(Dashboardname)) 
		  { 
			  WebElement element=fi1;
			  JavascriptExecutor js = (JavascriptExecutor)driver;
			  js.executeScript("arguments[0].click();", element);
			 // Actions a=new Actions(driver); 
			  //a.click(fi1).perform(); 
			  Thread.sleep(5000); 
			  break; 
			} 
		  }
		 
	}
	
	public void MoveDashboard(String NewDashboardname, WebDriver driver) throws InterruptedException
	{
		WebElement cla1=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis1=cla1.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis1.size());
		
		for(WebElement li1: lis1)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi1=li1.findElement(By.className("g-tab-title"));
			//System.out.println("Names are: " +fi.getText());
			
			 if(fi1.getAttribute("textContent").equalsIgnoreCase(NewDashboardname)) 
			  { 
				  WebElement element=fi1;
				  JavascriptExecutor js = (JavascriptExecutor)driver;
				  js.executeScript("arguments[0].click();", element);
				 // Actions a=new Actions(driver); 
				  //a.click(fi1).perform(); 
				  Thread.sleep(5000); 
				  break; 
				} 
		}
		
		WebElement cla=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis=cla.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis.size());
		
		for(WebElement li: lis)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi=li.findElement(By.className("g-tab-title"));
			//System.out.println("Names are: " +fi.getText());
			
			 if(fi.getAttribute("textContent").equalsIgnoreCase("WorkSpace")) 
			  { 
				  WebElement element=fi;
				  JavascriptExecutor js = (JavascriptExecutor)driver;
				  js.executeScript("arguments[0].click();", element);
				  //Actions a=new Actions(driver); 
				  //a.click(fi).perform(); 
				  Thread.sleep(5000); 
				  break; 
			  }
		}
	}

}
