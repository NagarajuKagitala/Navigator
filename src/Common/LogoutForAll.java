package Common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LogoutForAll 
{
	public void LogoutMethod(WebDriver driver, String Dashboardname) throws InterruptedException
	{
		//Deleting dashboard
		Dashboard obj=new Dashboard();
		obj.DeleteExistDashboard(driver, Dashboardname);
		
		
		//Logout option
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		Thread.sleep(4000);
		driver.findElement(By.id("yesButton")).click();
		driver.close();
		
	}
	
	public void LogoutMethodforDashboardOptions(WebDriver driver, String EMSDashboardname) throws InterruptedException
	{
		//Deleting dashboard
		Dashboard obj=new Dashboard();
		obj.DeleteExistDashboard(driver, EMSDashboardname);
		
		
		//Logout option
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		Thread.sleep(4000);
		driver.findElement(By.id("yesButton")).click();
		driver.close();
		
	}

}
