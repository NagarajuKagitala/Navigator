package Common;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class Viewlets 
{
	
	public void IBMMQViewletForNode(WebDriver driver, int ViewletValue, String ViewletName, String WGSName, String HostNameFromIcon) throws InterruptedException
	{
		//Click on Viewlet button
		driver.findElement(By.id("add-viewlet")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click();
		Thread.sleep(4000);
		
		//Select IBM MQ product
		//Select IBM=new Select(driver.findElement(By.name("productType")));
		//IBM.selectByVisibleText("IBM MQ");
		//Thread.sleep(4000); 
		
		driver.findElement(By.cssSelector(".field-workgroup-input > .ng-select-container")).click();
		Thread.sleep(3000);  
		
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
		Thread.sleep(3000);
		
		//Create Manager                  
		driver.findElement(By.cssSelector(".object-type:nth-child("+ ViewletValue +")")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(ViewletName);
		
		//Select WGS type
		//Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		//WGSSelection.selectByVisibleText(WGSName);
		
		driver.findElement(By.xpath("//div/div/div[2]/div/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(3000);   
		
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
		Thread.sleep(2000);
		
		if(ViewletName.equalsIgnoreCase("Node viewlet"))
		{
			//Select node value 
			driver.findElement(By.xpath("//div[2]/div/div[2]/div/ng-select/div/span")).click();
			Thread.sleep(2000);
			 try 
				{
		        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
					List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
					System.out.println(mdivs.size());	
					
					for (WebElement mdi : mdivs)
					{
						if(mdi.getText().equals(HostNameFromIcon))
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
		        Thread.sleep(1000);
			
		}
		
		//driver.findElement(By.cssSelector(".btn-primary")).click();
		driver.findElement(By.id("save-viewlet")).click();
		Thread.sleep(10000);
		
	}
	
	public void IBMMQViewlet(WebDriver driver, int ViewletValue, String ViewletName, String WGSName, String Node_Hostname) throws InterruptedException
	{
		//Click on Viewlet button
		driver.findElement(By.id("add-viewlet")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click();
		Thread.sleep(4000);
		
		//Select IBM MQ product
		//Select IBM=new Select(driver.findElement(By.name("productType")));
		//IBM.selectByVisibleText("IBM MQ");
		//Thread.sleep(4000); 
		
		driver.findElement(By.cssSelector(".field-workgroup-input > .ng-select-container")).click();
		Thread.sleep(3000);  
		
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
		Thread.sleep(3000);
		
		//Create Manager                  
		driver.findElement(By.cssSelector(".object-type:nth-child("+ ViewletValue +")")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(ViewletName);
		Thread.sleep(2000);
		
		//Select WGS type
		//Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		//WGSSelection.selectByVisibleText(WGSName);
		
		driver.findElement(By.xpath("//div/div/div[2]/div/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(3000);   
		
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
		Thread.sleep(4000);
		
		//Select node value
		driver.findElement(By.xpath("//div[2]/div/div[2]/div/ng-select/div/span")).click();
		Thread.sleep(4000);
		 try 
			{
	        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
				List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
				System.out.println(mdivs.size());	
				
				for (WebElement mdi : mdivs)
				{
					if(mdi.getText().equals(Node_Hostname))
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
	        Thread.sleep(1000);
				
		//driver.findElement(By.cssSelector(".btn-primary")).click();
		driver.findElement(By.id("save-viewlet")).click();
		Thread.sleep(10000);
		
	}
	
	public void KafkaViewlet(WebDriver driver, int ViewletValue, String ViewletName, String WGSName, String KafkaNodeName) throws InterruptedException
	{
		//Click on Viewlet button
		driver.findElement(By.id("add-viewlet")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click();
		
		//Select IBM MQ product
		//Select Kafka=new Select(driver.findElement(By.name("productType")));
		//Kafka.selectByVisibleText("Kafka");
		//Thread.sleep(4000); 
		
		driver.findElement(By.xpath("//app-modal-add-viewlet/div/div/div/div/ng-select/div/span")).click();
		Thread.sleep(3000);  
		
		WebElement drop=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div=drop.findElements(By.tagName("div")); 
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement di : div)
		{
			//System.out.println("text is :" +di.getText());
			if(di.getText().equalsIgnoreCase("Kafka"))
			{
				di.click();
				break;
			}	
		}
		Thread.sleep(3000);
		
		//Create Manager
		driver.findElement(By.cssSelector(".object-type:nth-child("+ ViewletValue +")")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(ViewletName);
		Thread.sleep(3000);
		
		//Select WGS type
		//Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		//WGSSelection.selectByVisibleText(WGSName);
		
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(3000);     
		
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
		Thread.sleep(2000);
		
		//Select node value
		driver.findElement(By.xpath("//div[2]/div/div[2]/div/ng-select/div/span")).click();
		Thread.sleep(4000);    
		try 
		{
			List<WebElement> Manager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				//System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				//System.out.println(s);
				if(s.equals(KafkaNodeName))
				{
					String id=Manager.get(i).getAttribute("id");
					Thread.sleep(2000);
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		driver.findElement(By.id("save-viewlet")).click();
		//driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(10000);
		
	}
	
	public void KafkaViewletForNode(WebDriver driver, int ViewletValue, String ViewletName, String WGSName, String KafkaNodeName) throws InterruptedException
	{
		//Click on Viewlet button
		driver.findElement(By.id("add-viewlet")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click();
		
		//Select IBM MQ product
		//Select Kafka=new Select(driver.findElement(By.name("productType")));
		//Kafka.selectByVisibleText("Kafka");
		//Thread.sleep(4000); 
		
		driver.findElement(By.xpath("//app-modal-add-viewlet/div/div/div/div/ng-select/div/span")).click();
		Thread.sleep(3000);  
		
		WebElement drop=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div=drop.findElements(By.tagName("div")); 
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement di : div)
		{
			//System.out.println("text is :" +di.getText());
			if(di.getText().equalsIgnoreCase("Kafka"))
			{
				di.click();
				break;
			}	
		}
		Thread.sleep(3000);
		
		//Create Manager
		driver.findElement(By.cssSelector(".object-type:nth-child("+ ViewletValue +")")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(ViewletName);
		
		//Select WGS type
		//Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		//WGSSelection.selectByVisibleText(WGSName);
		
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(4000);   
		
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
		Thread.sleep(2000);
		
		if(ViewletName.equalsIgnoreCase("Kafka Node Viewlet"))
		{
			//Select node value 
			driver.findElement(By.xpath("//div[2]/div/div[2]/div/ng-select/div/span")).click();
			Thread.sleep(2000);
			 try 
				{
		        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
					List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
					System.out.println(mdivs.size());	
					
					for (WebElement mdi : mdivs)
					{
						if(mdi.getText().equals(KafkaNodeName))
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
		        Thread.sleep(1000);
			
		}
		driver.findElement(By.id("save-viewlet")).click();
		//driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(10000);
		
	}
	
	public void EMSViewlet(WebDriver driver, int ViewletValue, String ViewletName, String EMS_WGSNAME, String EMSNode) throws InterruptedException
	{
		//Click on Viewlet button
		driver.findElement(By.id("add-viewlet")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click(); 
		
		//Select EMS product
		//Select EMS=new Select(driver.findElement(By.name("productType")));
		//EMS.selectByVisibleText("EMS");
		//Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".field-workgroup-input > .ng-select-container")).click();
		Thread.sleep(3000);  
		
		WebElement drop=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div=drop.findElements(By.tagName("div")); 
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement di : div)
		{
			//System.out.println("text is :" +di.getText());
			if(di.getText().equalsIgnoreCase("EMS"))
			{
				di.click();
				break;
			}	
		}
		Thread.sleep(3000);
		
		//Create Channel
		driver.findElement(By.cssSelector(".object-type:nth-child("+ ViewletValue +")")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(ViewletName);
		
		//Select WGS type
		//Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		//WGSSelection.selectByVisibleText(EMS_WGSNAME);
		
		
		driver.findElement(By.xpath("//div/div/div[2]/div/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(6000);   
		
		WebElement dropw=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> divw=dropw.findElements(By.tagName("div")); 
		//System.out.println("Number of divs are: " +divw.size());
		
		for(WebElement diw : divw)
		{
			//System.out.println("WGS text is :" +diw.getText());
			if(diw.getText().equalsIgnoreCase(EMS_WGSNAME))
			{
				diw.click();
				break;
			}	
		}
		Thread.sleep(4000);
		
		//Select node value
		driver.findElement(By.xpath("//div[2]/div/div[2]/div/ng-select/div/span")).click();
		Thread.sleep(6000);
		try 
		{
			List<WebElement> Manager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				//System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				//System.out.println(s);
				if(s.equals(EMSNode))
				{
					String id=Manager.get(i).getAttribute("id");
					Thread.sleep(2000);
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		
		
		driver.findElement(By.id("save-viewlet")).click();
		//driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(10000);
	}
	
	public void IIBViewlet(WebDriver driver, int ViewletValue, String ViewletName, String IIB_WGSNAME, String IIBNode) throws InterruptedException
	{
		//Click on Viewlet button
		driver.findElement(By.id("add-viewlet")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click(); 
		
		//Select EMS product
		//Select EMS=new Select(driver.findElement(By.name("productType")));
		//EMS.selectByVisibleText("EMS");
		//Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".field-workgroup-input > .ng-select-container")).click();
		Thread.sleep(3000);  
		
		WebElement drop=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div=drop.findElements(By.tagName("div")); 
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement di : div)
		{
			//System.out.println("text is :" +di.getText());
			if(di.getText().equalsIgnoreCase("IIB"))
			{
				di.click();
				break;
			}	
		}
		Thread.sleep(3000);
		
		//Create Channel
		driver.findElement(By.cssSelector(".object-type:nth-child("+ ViewletValue +")")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(ViewletName);
		Thread.sleep(3000);
		
		//Select WGS type
		//Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		//WGSSelection.selectByVisibleText(EMS_WGSNAME);
		
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(3000);          
		
		WebElement dropw=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> divw=dropw.findElements(By.tagName("div")); 
		//System.out.println("Number of divs are: " +divw.size());
		
		for(WebElement diw : divw)
		{
			//System.out.println("WGS text is :" +diw.getText());
			if(diw.getText().equalsIgnoreCase(IIB_WGSNAME))
			{
				diw.click();
				break;
			}	
		}
		Thread.sleep(4000);
	
		
		//Select node value
		driver.findElement(By.xpath("//div[2]/div/div[2]/div/ng-select/div/span")).click();
		Thread.sleep(4000);
		try 
		{
			List<WebElement> Manager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				//System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				//System.out.println(s);
				if(s.equals(IIBNode))
				{
					String id=Manager.get(i).getAttribute("id");
					Thread.sleep(2000);
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		driver.findElement(By.id("save-viewlet")).click();
		//driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(10000);
		
	}

}
