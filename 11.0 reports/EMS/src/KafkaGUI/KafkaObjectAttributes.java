package KafkaGUI;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Verify;

import Common.ClearSelectionofCheckbox;

public class KafkaObjectAttributes 
{
	
	public void NodeShowObjectAttributes(String Dashboardname, WebDriver driver, String SchemaName) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		//------- Get the Attributes names and Store into array -----------
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
		
		//Store the name into string
		String name=driver.findElement(By.xpath("//datatable-body-cell[3]/div/span")).getText();
		Thread.sleep(2000);                      
		
		//------------- Create user Schema With the stored attributes ------- 
		//Create User Schema           
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		Thread.sleep(2000);
		
		try
		{
			//Click on existing schema name
			driver.findElement(By.xpath("//tr[contains(.,'"+ SchemaName +"')]")).click();
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
		driver.findElement(By.name("name")).sendKeys(SchemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
			if(FinalListOfAttributes.contains("Cluster Mask") || FinalListOfAttributes.contains("Topic Mask"))
			{
				
			}
			else
			{
				driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(1000);
			}
		}
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(14000);
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		Thread.sleep(2000); 
		
		//------ Apply filter with the node name ------------------		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		// --------------- Store the Attribute values into array ----------------
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
		   	    	 if(!(verify.contains(None) || verify.contains("PM") || verify.contains("hours") || verify.contains("*") || verify.contains("Attribute Value")))
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
				
		// ----- Compare both attribute values ------------
		if(Values.contains(finaldata) || finaldata.contains(Values))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
	}
	
	public void ShowObjectAttributes(String Dashboardname, WebDriver driver, String SchemaName) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		//------- Get the Attributes names and Store into array -----------
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
		        	
		        	if(!(verify.contains(None) || verify.contains("Replicas not in sync") || verify.contains("Partition IDs") || verify.contains("Last Updated") || verify.contains("Attributes")))
		        	{
		        		//System.out.println("Printing attribute: " +ListOfAttributes);
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
			
		}
   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");
		//System.out.println("Final list:" +ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();                      
		
		//------------- Create user Schema With the stored attributes ------- 
		//Create User Schema           
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		Thread.sleep(2000);
		
		try
		{
			//Click on existing schema name
			driver.findElement(By.xpath("//tr[contains(.,'"+ SchemaName +"')]")).click();
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
		driver.findElement(By.name("name")).sendKeys(SchemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
			System.out.println("Attribute is: " +FinalListOfAttributes);
			if(FinalListOfAttributes.equalsIgnoreCase("Group ID"))
			{
				driver.findElement(By.xpath("//td[contains(.,'Group Id')]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(1000);
			}
			else if(FinalListOfAttributes.equalsIgnoreCase("Cluster ID"))
			{
				driver.findElement(By.xpath("//td[contains(.,'Cluster Id')]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(1000);
			}
			else if(FinalListOfAttributes.equalsIgnoreCase("Controller ID"))
			{
				driver.findElement(By.xpath("//td[contains(.,'Controller Id')]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(1000);
			}
			else
			{
				driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(1000);
			}
		}
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(14000);
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		Thread.sleep(2000); 
						
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println("Final list is :" +finaldata);
		System.out.println("----------------------------------");
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		// --------------- Store the Attribute values into array ----------------
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
		   	    	 if(!(verify.contains(None) || verify.contains("PM") || verify.contains("hours") || verify.contains("Attribute Value")))
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
		System.out.println("Show object page values are: " +Values);
				
		// ----- Compare both attribute values ------------
		if(finaldata.contains(Values) || Values.contains(finaldata))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
	}
	
	public void ClusterShowObjectAttributes(String Dashboardname, WebDriver driver, String SchemaName) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		//------- Get the Attributes names and Store into array -----------
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
		        	
		        	if(!(verify.contains(None) || verify.contains("Last Updated") || verify.contains("Replicas not in sync") || verify.contains("Attributes")))
		        	{
		        		//System.out.println("Printing attribute: " +ListOfAttributes);
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
			
		}
   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");  	    
		//System.out.println("Final list:" +ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();                      
		
		//------------- Create user Schema With the stored attributes ------- 
		//Create User Schema           
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		Thread.sleep(2000);
		
		try
		{
			//Click on existing schema name
			driver.findElement(By.xpath("//tr[contains(.,'"+ SchemaName +"')]")).click();
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
		driver.findElement(By.name("name")).sendKeys(SchemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
			if(FinalListOfAttributes.equalsIgnoreCase("Group ID"))
			{
				driver.findElement(By.xpath("//td[contains(.,'Group Id')]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(1000);
			}
			else if(FinalListOfAttributes.equalsIgnoreCase("Cluster ID"))
			{
				driver.findElement(By.xpath("//td[contains(.,'Cluster Id')]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(1000);
			}
			else if(FinalListOfAttributes.equalsIgnoreCase("Controller ID"))
			{
				driver.findElement(By.xpath("//td[contains(.,'Controller Id')]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(1000);
			}
			else
			{
				driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(1000);
			}
		}
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(14000);
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		Thread.sleep(2000); 
						
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println("Final list is :" +finaldata);
		System.out.println("----------------------------------");
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		// --------------- Store the Attribute values into array ----------------
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
					
					ObjectAttributes[i]=FinalData.getText();
		   	        String verify= FinalData.getText();
		   	     //System.out.println("attribute Value: " +verify);
		   	        
		   	        if(!verify.isEmpty()) 
		   	        {
		   	    	 String None= "None";                            
		   	    	 if(!(verify.contains(None) || verify.contains("PM") || verify.contains("hours") || verify.contains("Attribute Value")))
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
		System.out.println("SHow object page values are: " +Values);
		
		// ----- Compare both attribute values ------------
		if(Values.contains(finaldata) || finaldata.contains(Values))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
	}
	
	public void SchemaShowObjectAttributes(String Dashboardname, WebDriver driver, String SchemaName) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		//------- Get the Attributes names and Store into array -----------
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
		        	
		        	if(!(verify.contains(None) || verify.contains("Replicas not in sync") || verify.contains("Partition IDs") || verify.contains("Last Updated") || verify.contains("Attributes")))
		        	{
		        		//System.out.println("Printing attribute: " +ListOfAttributes);
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
			
		}
   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");
		//System.out.println("Final list:" +ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();                      
						
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println("Final list is :" +finaldata);
		System.out.println("----------------------------------");
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		// --------------- Store the Attribute values into array ----------------
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
		   	    	 if(!(verify.contains(None) || verify.contains("PM") || verify.contains("hours") || verify.contains("Attribute Value")))
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
		System.out.println("Show object page values are: " +Values);
				
		// ----- Compare both attribute values ------------
		if(finaldata.contains(Values) || Values.contains(finaldata))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
	}
	
	public void SchemaSubjectShowObjectAttributes(String Dashboardname, WebDriver driver, String SchemaName) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		//------- Get the Attributes names and Store into array -----------
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
		        	
		        	if(!(verify.contains(None) || verify.contains("Replicas not in sync") || verify.contains("Partition IDs") || verify.contains("Last Updated") || verify.contains("Attributes")))
		        	{
		        		//System.out.println("Printing attribute: " +ListOfAttributes);
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
			
		}
   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");
		//System.out.println("Final list:" +ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();                      
		
		//------------- Create user Schema With the stored attributes ------- 
		//Create User Schema           
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		Thread.sleep(2000);
		
		try
		{
			WebElement tab=driver.findElement(By.className("schemas-table")).findElement(By.tagName("tbody"));
			List<WebElement> trs=tab.findElements(By.tagName("tr"));
			System.out.println("no of trs: " +trs.size());
			
			for(WebElement fi:trs)
			{
				System.out.println("text is: " +fi.getText());
				String name=fi.getText();
				
				if(name.equalsIgnoreCase(SchemaName))
				{
					fi.click();
					Thread.sleep(4000);
					//Click on Delete button
					driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
					Thread.sleep(3000);
					
					//Click on confirmation yes button
					driver.findElement(By.id("accept-true")).click();
					Thread.sleep(4000);
				}
			}		
			
		}
		catch(Exception e)
		{
			System.out.println("Schema is not existing with same name");
		}
		
		//Click on add button
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(SchemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
			System.out.println("Attribute is: " +FinalListOfAttributes);
			if(FinalListOfAttributes.equalsIgnoreCase("Manager Name"))
			{
				driver.findElement(By.xpath("//td[contains(.,'Cluster Name')]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(1000);
			}
			
			else
			{
				driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(1000);
			}
		}
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(14000);
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		Thread.sleep(2000); 
						
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println("Final list is :" +finaldata);
		System.out.println("----------------------------------");
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		// --------------- Store the Attribute values into array ----------------
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
		   	    	 if(!(verify.contains(None) || verify.contains("PM") || verify.contains("hours") || verify.contains("Attribute Value")))
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
		System.out.println("Show object page values are: " +Values);
				
		// ----- Compare both attribute values ------------
		if(finaldata.contains(Values) || Values.contains(finaldata))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
	}
	
	
	public void SchemaSubjectVersionShowObjectAttributes(String Dashboardname, WebDriver driver, String SchemaName) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		//------- Get the Attributes names and Store into array -----------
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
		        	
		        	if(!(verify.contains(None) || verify.contains("Replicas not in sync") || verify.contains("Partition IDs") || verify.contains("Last Updated") || verify.contains("Attributes")))
		        	{
		        		//System.out.println("Printing attribute: " +ListOfAttributes);
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
			
		}
   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");
		//System.out.println("Final list:" +ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();                      
						
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println("Final list is :" +finaldata);
		System.out.println("----------------------------------");
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		// --------------- Store the Attribute values into array ----------------
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
		   	    	 if(!(verify.contains(None) || verify.contains("PM") || verify.contains("Attribute Value")))
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
		System.out.println("Show object page values are: " +Values);
				
		// ----- Compare both attribute values ------------
		if(finaldata.contains(Values) || Values.contains(finaldata))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
	}
		
	
}
