package Common;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class Dashboard 
{
	public void DeleteExistDashboard(WebDriver driver, String Dashboardname) throws InterruptedException
	{
		try
		{
			//click on dashboard button
			driver.findElement(By.id("add-dashboard")).click();
			Thread.sleep(4000);
			
		WebElement cla=driver.findElement(By.tagName("app-mod-manage-dashboard"));
		List<WebElement> lis=cla.findElements(By.tagName("div"));
		System.out.println("No of dashboards are: " +lis.size());
		
		
		for(WebElement li: lis)
		{
			//System.out.println("innet: " +li.getAttribute("innerHTML"));
			String clas=li.getAttribute("class");
			//System.out.println("class name : " +clas);
			
			if(clas.contains("maf-container"))
			{
				WebElement lef=li.findElement(By.className("left-side")).findElement(By.tagName("table"));
				List<WebElement> trs=lef.findElements(By.tagName("tr"));
				System.out.println("number of trs: " +trs.size());
				
				for(WebElement fi:trs)
				{
					if(fi.getText().equalsIgnoreCase(Dashboardname))
					{
						Actions a=new Actions(driver);
						a.click(fi).perform();
						Thread.sleep(5000);
						
						//Delete the existing dashboard
						driver.findElement(By.xpath("//button[contains(.,' Delete')]")).click();
						//driver.findElement(By.cssSelector(".fa-times")).click();
						driver.findElement(By.id("accept-true")).click();
						Thread.sleep(8000);
						break;
					}
				}
				break;
			}
					
			
		}
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
		Thread.sleep(4000);
		}
		
		catch(Exception e)
		{
			System.out.println("Dashboard is not existing with same name");
			driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
		}
	}
	
	
	public void DeleteExistDashboardForWGSExpert(WebDriver driver, String NewDashboardname) throws InterruptedException
	{
		try
		{
			
			//click on dashboard button
			driver.findElement(By.id("add-dashboard")).click();
			Thread.sleep(4000);
			
		WebElement cla=driver.findElement(By.tagName("app-mod-manage-dashboard"));
		List<WebElement> lis=cla.findElements(By.tagName("div"));
		System.out.println("No of dashboards are: " +lis.size());
		
		
		for(WebElement li: lis)
		{
			//System.out.println("innet: " +li.getAttribute("innerHTML"));
			String clas=li.getAttribute("class");
			//System.out.println("class name : " +clas);
			
			if(clas.contains("maf-container"))
			{
				WebElement lef=li.findElement(By.className("left-side")).findElement(By.tagName("table"));
				List<WebElement> trs=lef.findElements(By.tagName("tr"));
				System.out.println("number of trs: " +trs.size());
				
				for(WebElement fi:trs)
				{
					if(fi.getText().equalsIgnoreCase(NewDashboardname))
					{
						Actions a=new Actions(driver);
						a.click(fi).perform();
						Thread.sleep(5000);
						
						//Delete the existing dashboard
						driver.findElement(By.xpath("//button[contains(.,' Delete')]")).click();
						//driver.findElement(By.cssSelector(".fa-times")).click();
						driver.findElement(By.id("accept-true")).click();
						Thread.sleep(8000);
						break;
					}
				}
				break;
			}
					
			
		}
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
		Thread.sleep(4000);
		}
		
		catch(Exception e)
		{
			System.out.println("Dashboard is not existing with same name");
			driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
			//System.out.println(e.getStackTrace());
		}
	}
	
	public void DeleteExistDashboardForDashboardOptions(WebDriver driver, String EMSDashboardname) throws InterruptedException
	{
		
		String[] EMSDashboardname1= {"EMS Dashboard", "DashboardRenamed", "Dashboard Verficaton"};
		for(int i=0; i<=2; i++)
		{
		try
		{
			
			//click on dashboard button
			driver.findElement(By.id("add-dashboard")).click();
			Thread.sleep(4000);
			
		WebElement cla=driver.findElement(By.tagName("app-mod-manage-dashboard"));
		List<WebElement> lis=cla.findElements(By.tagName("div"));
		System.out.println("No of dashboards are: " +lis.size());
		
		
		for(WebElement li: lis)
		{
			//System.out.println("innet: " +li.getAttribute("innerHTML"));
			String clas=li.getAttribute("class");
			//System.out.println("class name : " +clas);
			
			if(clas.contains("maf-container"))
			{
				WebElement lef=li.findElement(By.className("left-side")).findElement(By.tagName("table"));
				List<WebElement> trs=lef.findElements(By.tagName("tr"));
				System.out.println("number of trs: " +trs.size());
				
				for(WebElement fi:trs)
				{
					if(fi.getText().equalsIgnoreCase(EMSDashboardname1[i]))
					{
						Actions a=new Actions(driver);
						a.click(fi).perform();
						Thread.sleep(5000);
						
						//Delete the existing dashboard
						driver.findElement(By.xpath("//button[contains(.,' Delete')]")).click();
						//driver.findElement(By.cssSelector(".fa-times")).click();
						driver.findElement(By.id("accept-true")).click();
						Thread.sleep(8000);
						break;
					}
				}
				break;
			}
					
			
		}
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
		Thread.sleep(4000);
		}
		
		catch(Exception e)
		{
			System.out.println("Dashboard is not existing with same name");
			driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
			//System.out.println(e.getStackTrace());
		}
		}
	}

}
