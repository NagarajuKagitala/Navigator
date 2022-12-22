package EMS;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class Node
{
	static WebDriver driver;
	
	@Parameters({"sDriver","sDriverpath","URL","uname","password"})
	@Test(priority=0)
	public static void Login(String sDriver,String sDriverpath, String URL, String uname, String password) throws InterruptedException
	{
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
		System.setProperty(sDriver,sDriverpath);
		driver=new ChromeDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.ie.driver"))
		{
			System.setProperty(sDriver,sDriverpath);
			driver=new InternetExplorerDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.edge.driver"))
		{
			System.setProperty(sDriver,sDriverpath);
			driver=new EdgeDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.firefox.driver"))
		{
			System.setProperty(sDriver,sDriverpath);
			driver=new FirefoxDriver();
		}
		
		//for getting URL
		driver.get(URL);
		Thread.sleep(9000);
		
		//for maximize window
		driver.manage().window().maximize();
		
		//for authorizations
		driver.findElement(By.xpath("//div[2]/button")).click();
		Thread.sleep(3000);
		
		driver.findElement(By.name("username")).sendKeys(uname);
		Thread.sleep(3000);
		driver.findElement(By.name("password")).sendKeys(password);
		Thread.sleep(5000);
		driver.findElement(By.xpath("//form/div[2]/button")).click();
		Thread.sleep(6000);
				
		String btnvalue1 = driver.findElement(By.xpath("//div[2]/button")).getText();
		String btnvalue2 = driver.findElement(By.xpath("//button[contains(.,'Logout')]")).getText();
		
		if(btnvalue1.equals(btnvalue2))
		{
			System.out.println("login is unsuccessful");
			driver.findElement(By.xpath("fail the testcase"));
		}
		else
		{
			System.out.println("login is successfull");
		}
		
		driver.findElement(By.xpath("//button[2]")).click();
		Thread.sleep(4000);

	}
	@Parameters({"nodename","emsserver","tcpIp"})
	@Test(priority=1)
	public static void createEMSmanagers(String nodename,String emsserver, String tcpIp) throws InterruptedException
	{
		//for creating ems node data
		driver.findElement(By.cssSelector("#operations-ems\\\\\\.remoteMgr-createEmsRemoteMgr .arrow")).click();		
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div/div[2]/button")).click();
		Thread.sleep(3000);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//textarea")).clear();
		Thread.sleep(2000);
		System.out.println("reached first");
		
		String course="{\r\n"
			    + "\"wgsName\": \""+"MQM"+"\",\r\n"
				+ "  \"nodeName\": \""+nodename+"\",\r\n"
				+ "  \"managerName\": \""+emsserver+"\",\r\n"
				+ " \"general\":{\r\n"
				+ "    \"serverUrl\": \""+tcpIp+"\",\r\n"
				+ "    \"emsUser\": \""+"admin"+"\"\r\n"
				+ "  }\r\n"
				+ "}";
		
		System.out.println("Input datais:" +course);
		
		//driver.findElement(By.xpath("//textarea")).click();
		driver.findElement(By.xpath("//textarea")).sendKeys(course);
		Thread.sleep(30000);
		System.out.println("reached here too");
		
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(5000);
		System.out.println("finally reached");
		Thread.sleep(4000);
		
		//scroll down to result
		JavascriptExecutor js1 = (JavascriptExecutor) driver;
		WebElement Element1 = driver.findElement(By.xpath("//div[4]/div[2]/div/div/table/tbody/tr/td"));
		//System.out.println("reached here too");
		js1.executeScript("arguments[0].scrollIntoView();", Element1);
		System.out.println("reached here");
		Thread.sleep(5000);
		
		//for comparing the result
		
		String responsedata=driver.findElement(By.xpath("//div[4]/div[2]/div/div/table/tbody/tr/td")).getText();
		System.out.println(responsedata);
		int responsecode=Integer.parseInt(responsedata);
		
		String curldata=driver.findElement(By.xpath("//div[2]/pre")).getText();
		System.out.println(curldata);
		if(curldata.contains(nodename) && responsecode==201)
		{
			System.out.println("node is created");
		}
		else
		{
			System.out.println("node is not created");
			//driver.findElement(By.xpath("fail the testcase")).click();
		}
		
		driver.findElement(By.cssSelector("#operations-ems\\\\\\.remoteMgr-createEmsRemoteMgr .arrow")).click();
		Thread.sleep(3000);
	}
		
	@Parameters({"nodename","emsserver"})
	@Test(priority=2)
	public static void readEMSmanagers(String nodename,String emsserver) throws InterruptedException
	{
		//for reading ems node data
		driver.findElement(By.cssSelector("#operations-ems\\\\\\.remoteMgr-readEmsRemoteMgr .opblock-summary-control")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div/div[2]/button")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//td[2]/input")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//td[2]/input")).sendKeys(nodename);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//tr[2]/td[2]/input")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//tr[2]/td[2]/input")).sendKeys(emsserver);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(3000);	
		//scroll down to result
		JavascriptExecutor js1 = (JavascriptExecutor) driver;
		WebElement Element1 = driver.findElement(By.xpath("//div[4]/div[2]/div/div/table/tbody/tr/td"));
		js1.executeScript("arguments[0].scrollIntoView();", Element1);
		Thread.sleep(5000);
				
				//for comparing the results
		String responsedata=driver.findElement(By.xpath("//div[4]/div[2]/div/div/table/tbody/tr/td")).getText();
		int responsecode=Integer.parseInt(responsedata);
		//System.out.println(responsecode);
		Thread.sleep(5000);		
		String curldata=driver.findElement(By.xpath("//div[2]/pre")).getText();
		//System.out.println(curldata);
		if(curldata.contains(nodename) && responsecode==200)
		{
			System.out.println("readnodedata is success");
		}
		else
		{
			System.out.println("readnodedata is failed");
			driver.findElement(By.xpath("fail the testcase")).click();
		}
				
	    driver.findElement(By.cssSelector("#operations-ems\\\\\\.remoteMgr-readEmsRemoteMgr .opblock-summary-control")).click();
		Thread.sleep(3000);
		
	}
	@Parameters({"nodename","emsserver"})
	@Test(priority=3)
	public static void searchEMSmanagers(String nodename,String emsserver) throws InterruptedException
	{
		//for reading ems node data
		driver.findElement(By.cssSelector("#operations-ems\\\\\\.remoteMgr-searchEmsRemoteMgrs .arrow")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div/div[2]/button")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//td[2]/input")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//td[2]/input")).sendKeys(nodename);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//tr[2]/td[2]/input")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//tr[2]/td[2]/input")).sendKeys(emsserver);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(3000);	
		//scroll down to result
		JavascriptExecutor js1 = (JavascriptExecutor) driver;
		WebElement Element1 = driver.findElement(By.xpath("//div[4]/div[2]/div/div/table/tbody/tr/td"));
		js1.executeScript("arguments[0].scrollIntoView();", Element1);
		Thread.sleep(5000);
				
				//for comparing the results
		String responsedata=driver.findElement(By.xpath("//div[4]/div[2]/div/div/table/tbody/tr/td")).getText();
		int responsecode=Integer.parseInt(responsedata);
		System.out.println(responsecode);
		Thread.sleep(5000);		
		String curldata=driver.findElement(By.xpath("//div[2]/pre")).getText();
		System.out.println(curldata);
		if(curldata.contains(nodename) && responsecode==200)
		{
			System.out.println("searchnodedata is success");
		}
		else
		{
			System.out.println("searchnodedata is failed");
			driver.findElement(By.xpath("fail the testcase")).click();
		}
				
	    driver.findElement(By.cssSelector("#operations-ems\\\\\\.remoteMgr-searchEmsRemoteMgrs .arrow")).click();
		Thread.sleep(3000);
		
	}
	@Parameters({"nodename","emsserver"})
	@Test(priority=6)
	public static void changeEMSmanagers(String nodename,String emsserver) throws InterruptedException
	{
		//for reading ems node data
		driver.findElement(By.cssSelector("#operations-ems\\\\\\.remoteMgr-changeEmsRemoteMgr .arrow")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div/div[2]/button")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//td[2]/input")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//td[2]/input")).sendKeys(nodename);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//tr[2]/td[2]/input")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//tr[2]/td[2]/input")).sendKeys(emsserver);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(3000);	
		//scroll down to result
		JavascriptExecutor js1 = (JavascriptExecutor) driver;
		WebElement Element1 = driver.findElement(By.xpath("//div[4]/div[2]/div/div/table/tbody/tr/td"));
		js1.executeScript("arguments[0].scrollIntoView();", Element1);
		Thread.sleep(5000);
	}
	
	
	
	@Parameters({"nodename","emsserver"})
	@Test(priority=5)
	public static void delEMSmanagers(String nodename,String emsserver) throws InterruptedException
	{
		//for reading ems node data
		driver.findElement(By.cssSelector("#operations-ems\\\\\\.remoteMgr-deleteEmsRemoteMgr .arrow")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div/div[2]/button")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//td[2]/input")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//td[2]/input")).sendKeys(nodename);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//tr[2]/td[2]/input")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//tr[2]/td[2]/input")).sendKeys(emsserver);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(3000);	
		//scroll down to result
		JavascriptExecutor js1 = (JavascriptExecutor) driver;
		WebElement Element1 = driver.findElement(By.xpath("//div[4]/div[2]/div/div/table/tbody/tr/td"));
		js1.executeScript("arguments[0].scrollIntoView();", Element1);
		Thread.sleep(5000);
				
		//for comparing the results
		String responsedata=driver.findElement(By.xpath("//div[4]/div[2]/div/div/table/tbody/tr/td")).getText();
		int responsecode=Integer.parseInt(responsedata);
		System.out.println(responsecode);
		Thread.sleep(5000);		
		String curldata=driver.findElement(By.xpath("//div[2]/pre")).getText();
		System.out.println(curldata);
		if(curldata.contains(nodename) && responsecode==204)
		{
			System.out.println("delnodedata is success");
		}
		else
		{
			System.out.println("delnodedata is failed");
			driver.findElement(By.xpath("fail the testcase")).click();
		}
				
	    driver.findElement(By.cssSelector("#operations-ems\\\\\\.remoteMgr-deleteEmsRemoteMgr .arrow")).click();
		Thread.sleep(3000);
		
	}
	
	@Test(priority=12)
	public static void Logout() throws InterruptedException
	{
		driver.findElement(By.xpath("//div[2]/button")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//button[2]")).click();
		Thread.sleep(3000);
		driver.close();
	}
}