package EMS;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import Common.ClearSelectionofCheckbox;
import testrail.Settings;

public class EMSObjects 
{
	public void ObjectAttributesVerificationforManager(String Dashboardname, WebDriver driver, String schemaName, String EMS_WGSNAME) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(3000);
		
		WebElement ee=driver.findElement(By.className("paddless"));
		List<WebElement> AttributesData1=ee.findElements(By.tagName("div"));
		//System.out.println("Divs size is: " +AttributesData1.size());
		//System.out.println("AttributesData count: " + AttributesData1.size());
		
		String links[]=new String[AttributesData1.size()]; 
		int k=1;
		StringBuilder ListOfAttributes = new StringBuilder();
		for (WebElement ee1 : AttributesData1)
		{
			//System.out.println("List: " +ee1.getText());
			 links[k]=ee1.getText();
			 String verify= ee1.getText();
			 if(!verify.isEmpty())
		        {
		        	String None= "None";
		        	if(!(verify.contains(None) || verify.contains("Last Updated") || verify.contains("EMS Server URL") || verify.contains("Attributes")))
		        	{
		        		//System.out.println("Printing attribute: " +ListOfAttributes);
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
			
		}
   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");  	   
		System.out.println("Final list:" +ListOfAttributesPresent);
		//System.out.println("Attributes are: " +ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		/*//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
		System.out.println(managername);*/
				
		//Store the name into string
		String name=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		Thread.sleep(2000);
		
		//Create User Schema         
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		Thread.sleep(2000);
		
		try
		{
			//Click on existing schema name
			driver.findElement(By.xpath("//tr[contains(.,'"+ schemaName +"')]")).click();
			Thread.sleep(2000);
			
			//Click on Delete button
			driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
			Thread.sleep(3000);
			
			//Click on confirmation yes button
			driver.findElement(By.id("accept-true")).click();
			Thread.sleep(4000);
		}
		catch(Exception e)
		{
			System.out.println("Schema is not existing with same name");
		}
		
		//Click on add button
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
			if(FinalListOfAttributes.contains("EMS Server URL"))
			{
				
			}
			else if(FinalListOfAttributes.contains("Last Updated"))
			{
				
			}
			else
			{  
				driver.findElement(By.xpath("//td[contains(.,'"+ FinalListOfAttributes +"')]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(1000);
			}
		}
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(10000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).sendKeys(managername);
		Thread.sleep(2000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);*/
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.cssSelector("datatable-body.datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		//driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		WebElement l=driver.findElement(By.className("gridding")).findElement(By.tagName("div"));
		List<WebElement> sp=l.findElements(By.tagName("span"));
		//System.out.println("size of spans: " +sp.size());
		
		StringBuilder buffer = new StringBuilder();
		for(WebElement sv : sp)
		{
			String AttributeNames=sv.getAttribute("class");
			//System.out.println("Attribute name: " +AttributeNames);
			//System.out.println("class name: " +sv.getAttribute("class"));
			
			if(AttributeNames.contains("ng-star-inserted"))
			{
				List<WebElement> divs=sv.findElements(By.tagName("div"));
				//System.out.println("values divs size is :" +divs.size());
				
				String ObjectAttributes[]=new String[divs.size()];
				int i=1;
				
				for (WebElement FinalData : divs)
				{
					//System.out.println(tdElement.getText());
					ObjectAttributes[i]=FinalData.getText();
		   	        String verify= FinalData.getText();
		   	        
		   	        if(!verify.isEmpty()) 
		   	        {
		   	    	 String None= "None";   
		   	    	 if(!(verify.contains(None) || verify.contains("hours") || verify.contains("EMS Server URL") || verify.contains("Attribute Value")))
		   	    	 {
		   	         buffer.append(ObjectAttributes[i]);
		    	     buffer.append('\n');
		    	     }
		   	    	 }
		   	     }
			}
		}
		
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Values=buffer.toString();
		Values=Values.trim();
		System.out.println("Attributes popup page values: " +Values);
		
		//Verification				
		if(Values.contains(finaldata) || finaldata.contains(Values))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
		
		//clear search data
		for(int j=0; j<=name.length(); j++)
    	{
    	
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(2000);
		
		//Refresh the Viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(4000);		
	}
	
	public void ObjectAttributesVerification(String Dashboardname, WebDriver driver, String schemaName, String EMS_WGSNAME) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(2000);
		
		WebElement ee=driver.findElement(By.className("paddless"));
		List<WebElement> AttributesData1=ee.findElements(By.tagName("div"));
		//System.out.println("Divs size is: " +AttributesData1.size());
		//System.out.println("AttributesData count: " + AttributesData1.size());
		
		String links[]=new String[AttributesData1.size()]; 
		int k=1;
		StringBuilder ListOfAttributes = new StringBuilder();
		for (WebElement ee1 : AttributesData1)
		{
			//System.out.println("List: " +ee1.getText());
			 links[k]=ee1.getText();
			 String verify= ee1.getText();
			 if(!verify.isEmpty())
		        {
		        	String None= "None";
		        	if(!(verify.contains(None) || verify.contains("Store") || verify.contains("Message trace") || verify.contains("Is Sender name") || verify.contains("Last Event Time") || verify.contains("Last Updated") || verify.contains("Attributes") || verify.contains("JNDI Names") || verify.contains("Import Transports")))
		        	{
		        		//System.out.println("Printing attribute: " +ListOfAttributes);
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
			
		}
   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");  	   
		System.out.println("Final list:" +ListOfAttributesPresent);
		//System.out.println("Attributes are: " +ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		/*//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
		System.out.println(managername);*/
				
		//Store the name into string
		String name=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		Thread.sleep(2000);
		
		//Create User Schema
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		Thread.sleep(2000);
		
		try
		{
			//Click on existing schema name
			driver.findElement(By.xpath("//tr[contains(.,'"+ schemaName +"')]")).click();
			Thread.sleep(2000);
			
			//Click on Delete button
			driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
			Thread.sleep(3000);
			
			//Click on confirmation yes button
			driver.findElement(By.id("accept-true")).click();
			Thread.sleep(4000);
		}
		catch(Exception e)
		{
			System.out.println("Schema is not existing with same name");
		}
		
		//Click on add button
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
			System.out.println("values are : " +FinalListOfAttributes);
			if(FinalListOfAttributes.contains("Pending messages count"))
			{
				driver.findElement(By.xpath("//td[contains(.,'Pend messages count')]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(1000);
			}
			else if(FinalListOfAttributes.contains("Pending messages size (KB)"))
			{
				driver.findElement(By.xpath("//td[contains(.,'Pend messages size (KB)')]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(1000);
			}
			else if(FinalListOfAttributes.contains("Store") || FinalListOfAttributes.contains("Message trace") || FinalListOfAttributes.contains("Is Sender name"))
			{
				
			}
			else if(FinalListOfAttributes.contains("Topic name"))
			{
				driver.findElement(By.xpath("//td[contains(.,'Topic Name')]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
			}
			else
			{
				driver.findElement(By.xpath("//td[contains(.,'"+ FinalListOfAttributes +"')]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(1000);      
			}   
		}
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(10000);
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).sendKeys(managername);
		Thread.sleep(2000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);*/
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.cssSelector("datatable-body.datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		WebElement l=driver.findElement(By.className("gridding")).findElement(By.tagName("div"));
		List<WebElement> sp=l.findElements(By.tagName("span"));
		//System.out.println("size of spans: " +sp.size());
		
		StringBuilder buffer = new StringBuilder();
		for(WebElement sv : sp)
		{
			String AttributeNames=sv.getAttribute("class");
			//System.out.println("Attribute name: " +AttributeNames);
			//System.out.println("class name: " +sv.getAttribute("class"));
			
			if(AttributeNames.contains("ng-star-inserted"))
			{
				List<WebElement> divs=sv.findElements(By.tagName("div"));
				//System.out.println("values divs size is :" +divs.size());
				
				String ObjectAttributes[]=new String[divs.size()];
				int i=1;
				
				for (WebElement FinalData : divs)
				{
					//System.out.println(tdElement.getText());
					ObjectAttributes[i]=FinalData.getText();
		   	        String verify= FinalData.getText();
		   	        
		   	        if(!verify.isEmpty()) 
		   	        {
		   	    	 String None= "None";   
		   	    	 if(!(verify.contains(None) || verify.contains("Attribute Value")))
		   	    	 {
		   	         buffer.append(ObjectAttributes[i]);
		    	     buffer.append('\n');
		    	     }
		   	    	 }
		   	     }
			}
		}
		
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Values=buffer.toString();
		Values=Values.trim();
		System.out.println("Attributes popup page values: " +Values);
		
		//Verification		
		if(Values.contains(finaldata) || finaldata.contains(Values))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
		
		//clear search data
		for(int j=0; j<=name.length(); j++)
    	{
    	
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(2000);
		
		//Refresh the Viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(4000);		
	}
	
	
	public void EMSQueueObjectAttributesVerification(String Dashboardname, WebDriver driver, String schemaName, String EMS_WGSNAME) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		//driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(2000);
		Actions Mousehovercopy=new Actions(driver);
		Mousehovercopy.moveToElement(driver.findElement(By.linkText("Show Object Attributes"))).click();
		Thread.sleep(1000);
		
		WebElement ee=driver.findElement(By.className("paddless"));
		//WebElement ee=driver.findElement(By.cssSelector("//div[@id='consoleMainBody']/app-console-tab/div/div[2]/div[3]/div/div/div/span/div"));
		List<WebElement> AttributesData1=ee.findElements(By.tagName("div"));
		//System.out.println("Divs size is: " +AttributesData1.size());
		//System.out.println("AttributesData count: " + AttributesData1.size());
		
		String links[]=new String[AttributesData1.size()]; 
		int k=1;
		StringBuilder ListOfAttributes = new StringBuilder();
		for (WebElement ee1 : AttributesData1)
		{
			//System.out.println("List: " +ee1.getText());
			 links[k]=ee1.getText();
			 String verify= ee1.getText();
			 if(!verify.isEmpty())
		        {
		        	String None= "None";
		        	
		        	if(!(verify.contains(None) || verify.contains("Last Event Time") || verify.contains("Last Updated") || verify.contains("Attributes") || verify.contains("JNDI Names") || verify.contains("Import Transports")))
		        	{
		        		//System.out.println("Printing attribute: " +ListOfAttributes);
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
			
		}
   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");  	   
		System.out.println("Final list:" +ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		/*//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
		System.out.println(managername);*/
		
		//Store the name into string
		String name=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		Thread.sleep(2000);
		
		//Create User Schema
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		Thread.sleep(2000);
		
		try
		{
			//Click on existing schema name
			driver.findElement(By.xpath("//tr[contains(.,'"+ schemaName +"')]")).click();
			Thread.sleep(2000);
			
			//Click on Delete button
			driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
			Thread.sleep(3000);
			
			//Click on confirmation yes button
			driver.findElement(By.id("accept-true")).click();
			Thread.sleep(4000);
		}
		catch(Exception e)
		{
			System.out.println("Schema is not existing with same name");
		}
		
		//Click on add button
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
			//System.out.println("Attribute is: " +FinalListOfAttributes);
			String FinalString="";
			if(FinalListOfAttributes.contains("'From' Queue name"))
			{
				FinalString=FinalListOfAttributes.replace("'", "\"");
				
			}
			else if(FinalListOfAttributes.contains("'To' Queue name"))
			{
				FinalString=FinalListOfAttributes.replace("'", "\"");
			}
			else
			{
				FinalString=FinalListOfAttributes;
			}
			//System.out.println("FinalString is: " +FinalString); 
			driver.findElement(By.xpath("//td[contains(.,'"+ FinalString +"')]")).click();
			driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
			//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
			Thread.sleep(4000);
		}
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(10000);
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).sendKeys(managername);
		Thread.sleep(2000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);*/
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("((//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("((//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.cssSelector("datatable-body.datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		WebElement l=driver.findElement(By.className("gridding")).findElement(By.tagName("div"));
		List<WebElement> sp=l.findElements(By.tagName("span"));
		//System.out.println("size of spans: " +sp.size());
		
		StringBuilder buffer = new StringBuilder();
		for(WebElement sv : sp)
		{
			String AttributeNames=sv.getAttribute("class");
			//System.out.println("Attribute name: " +AttributeNames);
			//System.out.println("class name: " +sv.getAttribute("class"));
			
			if(AttributeNames.contains("ng-star-inserted"))
			{
				List<WebElement> divs=sv.findElements(By.tagName("div"));
				//System.out.println("values divs size is :" +divs.size());
				
				String ObjectAttributes[]=new String[divs.size()];
				int i=1;
				
				for (WebElement FinalData : divs)
				{
					//System.out.println(tdElement.getText());
					ObjectAttributes[i]=FinalData.getText();
		   	        String verify= FinalData.getText();
		   	        
		   	        if(!verify.isEmpty()) 
		   	        {
		   	    	 String None= "None";   
		   	    	 if(!(verify.contains(None) || verify.contains("Attribute Value")))
		   	    	 {
		   	         buffer.append(ObjectAttributes[i]);
		    	     buffer.append('\n');
		    	     }
		   	    	 }
		   	     }
			}
		}
		
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Values=buffer.toString();
		Values=Values.trim();
		System.out.println("Attributes popup page values: " +Values);
		
		//Verification		
		if(finaldata.contains(Values) || Values.contains(finaldata))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
		
		//clear search data
		for(int j=0; j<=name.length(); j++)
    	{
    	
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(2000);
		
		//Refresh the Viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(4000);		
	}
	
	public void ChannelObjectAttributesVerification(String Dashboardname, WebDriver driver, String schemaName, String EMS_WGSNAME) throws InterruptedException
	{			
		//Store the name into string
		String name=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		Thread.sleep(2000);
		
		//Create User Schema
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		Thread.sleep(2000);
		
		try
		{
			//Click on existing schema name
			driver.findElement(By.xpath("//tr[contains(.,'"+ schemaName +"')]")).click();
			Thread.sleep(2000);
			
			//Click on Delete button
			driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
			Thread.sleep(3000);
			
			//Click on confirmation yes button
			driver.findElement(By.id("accept-true")).click();
			Thread.sleep(4000);
		}
		catch(Exception e)
		{
			System.out.println("Schema is not existing with same name");
		}
		
		//Click on add button
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000);
		
		driver.findElement(By.xpath("//button[contains(.,'Add all ')]")).click();
		
		//Select Active Attribute
		driver.findElement(By.cssSelector("td[title=\"Active\"]")).click();
		
		//Click on Move up button
		driver.findElement(By.xpath("//div[4]/div/button")).click();
		driver.findElement(By.xpath("//div[4]/div/button")).click();
		Thread.sleep(1000);
		
		//Remove Last update option
		driver.findElement(By.xpath("//td[contains(.,'Last Updated')]")).click();
		driver.findElement(By.xpath("//button[contains(.,' Remove')]")).click();
		Thread.sleep(2000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).sendKeys(managername);
		Thread.sleep(2000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);*/
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.cssSelector("datatable-body.datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		WebElement l=driver.findElement(By.className("gridding")).findElement(By.tagName("div"));
		List<WebElement> sp=l.findElements(By.tagName("span"));
		//System.out.println("size of spans: " +sp.size());
		
		StringBuilder buffer = new StringBuilder();
		for(WebElement sv : sp)
		{
			String AttributeNames=sv.getAttribute("class");
			//System.out.println("Attribute name: " +AttributeNames);
			//System.out.println("class name: " +sv.getAttribute("class"));
			
			if(AttributeNames.contains("ng-star-inserted"))
			{
				List<WebElement> divs=sv.findElements(By.tagName("div"));
				//System.out.println("values divs size is :" +divs.size());
				
				String ObjectAttributes[]=new String[divs.size()];
				int i=1;
				
				for (WebElement FinalData : divs)
				{
					//System.out.println(tdElement.getText());
					ObjectAttributes[i]=FinalData.getText();
		   	        String verify= FinalData.getText();
		   	        
		   	        if(!verify.isEmpty()) 
		   	        {
		   	    	 String None= "None";   
		   	    	 if(!(verify.contains(None) || verify.contains("hours") || verify.contains("Attribute Value")))
		   	    	 {
		   	         buffer.append(ObjectAttributes[i]);
		    	     buffer.append('\n');
		    	     }
		   	    	 }
		   	     }
			}
		}
		
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Values=buffer.toString();
		Values=Values.trim();
		System.out.println("Attributes popup page values: " +Values);
		
		//Verification				
		if(finaldata.contains(Values) || Values.contains(finaldata))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
		
		//clear search data
		for(int j=0; j<=name.length(); j++)
    	{
    	
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(2000);
		
		//Refresh the Viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(4000);		
	}
	
	public void TransportObjectAttributesVerification(String Dashboardname, WebDriver driver, String schemaName) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		WebElement ee=driver.findElement(By.className("paddless"));
		List<WebElement> AttributesData1=ee.findElements(By.tagName("div"));
		//System.out.println("Divs size is: " +AttributesData1.size());
		//System.out.println("AttributesData count: " + AttributesData1.size());
		
		String links[]=new String[AttributesData1.size()]; 
		int k=1;
		StringBuilder ListOfAttributes = new StringBuilder();
		for (WebElement ee1 : AttributesData1)
		{
			//System.out.println("List: " +ee1.getText());
			 links[k]=ee1.getText();
			 String verify= ee1.getText();
			 if(!verify.isEmpty())
		        {
		        	String None= "None";
		        	
		        	if(!(verify.contains(None) || verify.contains("Last Event Time") || verify.contains("Last Updated") || verify.contains("Attributes")))
		        	{
		        		//System.out.println("Printing attribute: " +ListOfAttributes);
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
			
		}
   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");  	   
		System.out.println("Final list:" +ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		
		//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println(managername);
		
		//Store the name into string
		String name=driver.findElement(By.xpath("//datatable-body-cell[3]/div/span")).getText();
		Thread.sleep(2000);
		
		//Create User Schema
		driver.findElement(By.xpath("//div[3]/img")).click();
		Thread.sleep(2000);
		
		try
		{
			//Click on existing schema name
			driver.findElement(By.xpath("//tr[contains(.,'"+ schemaName +"')]")).click();
			Thread.sleep(2000);
			
			//Click on Delete button
			driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
			Thread.sleep(3000);
			
			//Click on confirmation yes button
			driver.findElement(By.id("accept-true")).click();
			Thread.sleep(4000);
		}
		catch(Exception e)
		{
			System.out.println("Schema is not existing with same name");
		}
		
		//Click on add button
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
		driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
		Thread.sleep(1000);
		}
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.id("dropdownMenuButton")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).sendKeys(managername);
		Thread.sleep(2000);
		
	
		try 
		{
			//driver.findElement(By.id("destinationQMName")).click();
			List<WebElement> Manager=driver.findElement(By.className("ng-select")).findElements(By.className("ng-option"));
			System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				System.out.println(s);
				if(s.equals(DestinationManager))
				{
					String id=Manager.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);*/
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		WebElement l=driver.findElement(By.className("gridding")).findElement(By.tagName("div"));
		List<WebElement> sp=l.findElements(By.tagName("span"));
		//System.out.println("size of spans: " +sp.size());
		
		StringBuilder buffer = new StringBuilder();
		for(WebElement sv : sp)
		{
			String AttributeNames=sv.getAttribute("class");
			//System.out.println("Attribute name: " +AttributeNames);
			//System.out.println("class name: " +sv.getAttribute("class"));
			
			if(AttributeNames.contains("ng-star-inserted"))
			{
				List<WebElement> divs=sv.findElements(By.tagName("div"));
				//System.out.println("values divs size is :" +divs.size());
				
				String ObjectAttributes[]=new String[divs.size()];
				int i=1;
				
				for (WebElement FinalData : divs)
				{
					//System.out.println(tdElement.getText());
					ObjectAttributes[i]=FinalData.getText();
		   	        String verify= FinalData.getText();
		   	        
		   	        if(!verify.isEmpty()) 
		   	        {
		   	    	 String None= "None";   
		   	    	 if(!(verify.contains(None) || verify.contains("Attribute Value")))
		   	    	 {
		   	         buffer.append(ObjectAttributes[i]);
		    	     buffer.append('\n');
		    	     }
		   	    	 }
		   	     }
			}
		}
		
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Values=buffer.toString();
		Values=Values.trim();
		System.out.println("Attributes popup page values: " +Values);
		
		//Verification				
		if(Values.contains(finaldata))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
		
		//clear search data
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).click();
		driver.findElement(By.name("queueMngr")).clear();
		driver.findElement(By.xpath("//app-modal-title/div")).click();
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);*/
		
		//Refresh the Viewlet
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(4000);		
	}
	
	public void ObjectAttributesVerificationForEMS(String Dashboardname, WebDriver driver, String schemaName, String EMS_WGSNAME) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(4000);
		
		WebElement ee=driver.findElement(By.className("paddless"));
		List<WebElement> AttributesData1=ee.findElements(By.tagName("div"));
		//System.out.println("Divs size is: " +AttributesData1.size());
		//System.out.println("AttributesData count: " + AttributesData1.size());
		
		String links[]=new String[AttributesData1.size()]; 
		int k=1;
		StringBuilder ListOfAttributes = new StringBuilder();
		for (WebElement ee1 : AttributesData1)
		{
			// System.out.println("List: " +ee1.getText());
			 links[k]=ee1.getText();
			 String verify= ee1.getText();
			 if(!verify.isEmpty())
		        {
		        	String None= "None";
		        	
		        	if(!(verify.contains(None) || verify.contains("Last Event Time") || verify.contains("Last Updated") || verify.contains("Attributes")))
		        	{
		        		//System.out.println("Printing attribute: " +ListOfAttributes);
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
			
		}
   	    
   	    String AttributeValues=ListOfAttributes.toString();
   	   // System.out.println("My list is: " +AttributeValues);
		String[] ListOfAttributesPresent = AttributeValues.split(",");  	   
		System.out.println("Final list:" +ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();
						
		//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
		System.out.println(managername);
				
		//Store the name into string
		String name=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		Thread.sleep(2000);
		
		//Create User Schema
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		Thread.sleep(2000);         
		
		try
		{
			//Click on existing schema name
			driver.findElement(By.xpath("//tr[contains(.,'"+ schemaName +"')]")).click();
			Thread.sleep(2000);
			
			//Click on Delete button
			driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
			Thread.sleep(3000);
			
			//Click on confirmation yes button
			driver.findElement(By.id("accept-true")).click();
			Thread.sleep(4000);
		}
		catch(Exception e)
		{
			System.out.println("Schema is not existing with same name");
		}
		
		//Click on add button
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
			
		driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
		//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
		Thread.sleep(1000);
		}
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).sendKeys(managername);
		Thread.sleep(2000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);*/
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Store the data into particular string
		String finaldata=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		WebElement l=driver.findElement(By.className("gridding")).findElement(By.tagName("div"));
		List<WebElement> sp=l.findElements(By.tagName("span"));
		//System.out.println("size of spans: " +sp.size());
		
		StringBuilder buffer = new StringBuilder();
		for(WebElement sv : sp)
		{
			String AttributeNames=sv.getAttribute("class");
			System.out.println("Attribute name: " +AttributeNames);
			//System.out.println("class name: " +sv.getAttribute("class"));
			
			if(AttributeNames.contains("ng-star-inserted"))
			{
				List<WebElement> divs=sv.findElements(By.tagName("div"));
				//System.out.println("values divs size is :" +divs.size());
				
				String ObjectAttributes[]=new String[divs.size()];
				int i=1;
				
				for (WebElement FinalData : divs)
				{
					//System.out.println(tdElement.getText());
					ObjectAttributes[i]=FinalData.getText();
		   	        String verify= FinalData.getText();
		   	        
		   	        if(!verify.isEmpty()) 
		   	        {
		   	    	 String None= "None";   
		   	    	 if(!(verify.contains(None) || verify.contains("Attribute Value")))
		   	    	 {
		   	         buffer.append(ObjectAttributes[i]);
		    	     buffer.append('\n');
		    	     }
		   	    	 }
		   	     }
			}
		}
		
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Values=buffer.toString();
		Values=Values.trim();
		System.out.println("Attributes popup page values: " +Values);
		
		//Verification				
		if(finaldata.contains(Values) || Values.contains(finaldata))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
		
		//clear search data
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).click();
		driver.findElement(By.name("queueMngr")).clear();
		driver.findElement(By.xpath("//app-modal-title/div")).click();
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);*/
		
		//Refresh the Viewlet
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(4000);		
	}

	
}
