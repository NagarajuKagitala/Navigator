package ApodGUI;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Common.ClearSelectionofCheckbox;
import testrail.Settings;


public class ObjectsVerificationForAllViewlets 
{
	
	public void WGSAttributes(String NewDashboardname, WebDriver driver, String schemaName, String WGSName) throws InterruptedException
	{
		//Open dash board
		ClearSelectionofCheckbox obj=new ClearSelectionofCheckbox();
		obj.MoveDashboard(NewDashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		//------- Get the Attributes names and Store into array -----------
		List<WebElement> Attributescolumn = driver.findElements(By.xpath("//span/div")); 
		String links[]=new String[Attributescolumn.size()]; 
   	    int k=1;
   	    StringBuilder ListOfAttributes = new StringBuilder();
   	    
   	    for (WebElement tdElement : Attributescolumn)
   	    {
   	    	//System.out.println(tdElement.getText());
	        links[k]=tdElement.getText();
	        String verify= tdElement.getText();
	        
	        if(!verify.isEmpty())
	        {
	        	String None= "None";
	        	
	        	if(!(verify.contains(None) || verify.contains("Last Updated") || verify.contains("License Expiration") ||verify.contains("Time left until next discovery")))
	        	{
	        		//System.out.println("Printing attribute: " +ListOfAttributes);
	        		ListOfAttributes.append(links[k]);
		    		ListOfAttributes.append(',');
	        	}
	        }
   	    }
   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");
		//System.out.println("Final attributes list:" +ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();
				
		//Store the name into string
		String name=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		Thread.sleep(2000);                       
		
		//------------- Create user Schema With the stored attributes ------- 
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
		
		//Click on Add button
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		Thread.sleep(2000);
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000);
		
		String Objects=driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div/table/tbody")).getText();
		//System.out.println("Objects list is: " +Objects);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
			//System.out.println("Attribute is: " +FinalListOfAttributes);
			if(Objects.contains(FinalListOfAttributes))
			{
				driver.findElement(By.xpath("//td[contains(.,'"+ FinalListOfAttributes +"')]")).click();
				//driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(3000);
			}
		}
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(2000); 
				
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println("Viewlet data is: " +finaldata);
		System.out.println("----------------------------------");
		
		//Open dash board
		ClearSelectionofCheckbox obj1=new ClearSelectionofCheckbox();
		obj1.MoveDashboard(NewDashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		// --------------- Store the Attribute values into array ----------------
		List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr/td[2]"));
		String ObjectAttributes[]=new String[AttributesData.size()];
		int i=1;
		StringBuilder buffer = new StringBuilder();
		for (WebElement FinalData : AttributesData)
		{
			//System.out.println(tdElement.getText());
			ObjectAttributes[i]=FinalData.getText();
   	        String verify= FinalData.getText();
   	        
   	        if(!verify.isEmpty()) 
   	        {
   	    	 String None= "None";                            
   	    	 if(!(verify.contains(None) || verify.contains("hours")))
   	    	 {
   	         buffer.append(ObjectAttributes[i]);
    	     buffer.append('\n');
    	     }
   	    	 }
   	     }
		//System.out.println(buffer);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Values=buffer.toString();
		System.out.println("Attributes popup page values: " +Values);
		
		//Edit the search field data
	    for(int j=0; j<=name.length(); j++)
	    {
	    	
	    driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
	    }
	    Thread.sleep(4000);
		
		// ----- Compare both attribute values ------------
		if(Values.contains(finaldata))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
				
		//Refresh the Viewlet
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(4000);
	}
	
	public void NodeAttributes(String Dashboardname, WebDriver driver, String SchemaName) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
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
		String name=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
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
		
		String Objects=driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div/table/tbody")).getText();
		//System.out.println("Objects list is: " +Objects);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
			//System.out.println("Attribute is: " +FinalListOfAttributes);
			if(Objects.contains(FinalListOfAttributes))
			{
				driver.findElement(By.xpath("//td[contains(.,'"+ FinalListOfAttributes +"')]")).click();
				//driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(2000);
			}
		}
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		Thread.sleep(2000); 
		
		//------ Apply filter with the node name ------------------
		driver.findElement(By.id("dropdownMenuButton")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		Thread.sleep(3000);
		
		//Select node value
		driver.findElement(By.cssSelector(".ng-select-taggable .ng-arrow-wrapper")).click();
		Thread.sleep(3000);           
		 try 
			{
	        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
				List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
				System.out.println(mdivs.size());	
				
				for (WebElement mdi : mdivs)
				{
					if(mdi.getText().equals(name))
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
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
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
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
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
		
		
		//System.out.println(buffer);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Values=buffer.toString();
		System.out.println(Values);
		
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
		
		//Edit the search field data
	    for(int j=0; j<=name.length(); j++)
	    {
	    	
	    driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
	    }
	    Thread.sleep(4000);
		
		//Refresh the Viewlet
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(4000);
	}
	
	
		public void ManagerAttributes(String Dashboardname, WebDriver driver, String SchemaName, String Attributes, String WGSName) throws InterruptedException
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
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
			        	
			        	if(!(verify.contains(None) || verify.contains("Last Updated") || verify.contains("Creation Date") || verify.contains("Attributes")))
			        	{
			        		//System.out.println("Printing attribute: " +ListOfAttributes);
			        		ListOfAttributes.append(links[k]);
				    		ListOfAttributes.append(',');
			        	}
			        }
				
			}
				   	    
	   	    String AttributeValues=ListOfAttributes.toString();
			String[] ListOfAttributesPresent = AttributeValues.split(",");
			//System.out.println(ListOfAttributesPresent);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			//Store the name into string
			String name=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
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
			
			String Objects=driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div/table/tbody")).getText();
			//System.out.println("Objects list is: " +Objects);
			
			//Add the Required attributes which are located in the Object attribute page
			for (String FinalListOfAttributes : ListOfAttributesPresent)
			{
				//System.out.println("object name is: " +Objects);
				
				if(Objects.contains(FinalListOfAttributes) || Objects.toLowerCase().contains(FinalListOfAttributes.toLowerCase()))
				{
					System.out.println("Attribute is: " +FinalListOfAttributes);
					if(FinalListOfAttributes.equalsIgnoreCase("Queue manager State"))
					{     
						driver.findElement(By.xpath("//td[contains(.,'Queue manager state')]")).click();
						driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
						//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
						Thread.sleep(1000);
					}
					else
					{
						driver.findElement(By.xpath("//td[contains(.,'"+ FinalListOfAttributes +"')]")).click();
						//driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
						Thread.sleep(1000);
						driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
						//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
						Thread.sleep(3000);
					}
					
				}				
			}
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(20000);
			driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
			Thread.sleep(2000);          
			
			//------ Apply filter with the node name ------------------
			driver.findElement(By.id("dropdownMenuButton")).click();
			driver.findElement(By.linkText("Edit viewlet")).click();
			
			//Select node value 
			driver.findElement(By.xpath("//div/div/div[2]/div/div[2]/div/ng-select/div/span")).click();
			Thread.sleep(2000);
			 try 
				{
		        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
					List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
					System.out.println(mdivs.size());	
					
					for (WebElement mdi : mdivs)
					{
						if(mdi.getText().equals(name))
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
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			
			//Search the viewlet data using name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
			Thread.sleep(2000);
			
			//Strore the data into particular string
			String finaldata=driver.findElement(By.cssSelector("datatable-body.datatable-body")).getText();
			System.out.println("Viewlet attribute values are: " +finaldata);
			System.out.println("----------------------------------");
			
			//Clearing selection of object
			ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
			che1.Deselectcheckbox(Dashboardname, driver);
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Show Object Attributes")).click();
			Thread.sleep(1000);
			
			//Search the viewlet data using name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
			Thread.sleep(2000);
			
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
			   	     try
			   	        {
			   	        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddhh.mm.ss");
			   	        	Date date = format.parse(verify);
			   	        }
			   	        catch(Exception e)
			   	        {
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
			}
			   	       
						
			//System.out.println(buffer);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			String Values=buffer.toString();
			System.out.println("Popup page values are: " +Values);
			
			// ----- Compare both attribute values ------------
			if(Values.contains(finaldata) ||finaldata.contains(Values))
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
			
			//Refresh the Viewlet
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
		
		public void QueuesAttributesVerification(String Dashboardname, WebDriver driver, String schemaName, String WGSName) throws InterruptedException
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]")).click();
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
			        	if(!(verify.contains(None) || verify.contains("Time Since Reset") || verify.contains("Last Updated") || verify.contains("Creation Time") || verify.contains("Creation Date") || verify.contains("Alteration Date") || verify.contains("Attributes")))
			        	{
			        		//System.out.println("Printing attribute: " +ListOfAttributes);
			        		ListOfAttributes.append(links[k]);
				    		ListOfAttributes.append(',');
			        	}
			        }
			}
				   	    
	   	    String AttributeValues=ListOfAttributes.toString();
			String[] ListOfAttributesPresent = AttributeValues.split(",");
			//System.out.println(ListOfAttributesPresent);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			//Store the name into string
			String name=driver.findElement(By.xpath("//datatable-body-cell[5]/div/span")).getText();
			Thread.sleep(2000);
			
			//Store the Queue name into string 
			String Queuename=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
			Thread.sleep(2000);
			
			//------------- Create user Schema With the stored attributes ------- 
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
			
			String Objects=driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div/table/tbody")).getText();
			//System.out.println("Objects list is: " +Objects);
			
			//Add the Required attributes which are located in the Object attribute page
			for (String FinalListOfAttributes : ListOfAttributesPresent)
			{
				//System.out.println("Attribute is: " +FinalListOfAttributes);
				if(Objects.contains(FinalListOfAttributes))
				{
					driver.findElement(By.xpath("//td[contains(.,'"+ FinalListOfAttributes +"')]")).click();
					//driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
					Thread.sleep(1000);
					driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
					//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
					Thread.sleep(1000);
				}
			}
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(12000);
			driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
			Thread.sleep(2000); 
			
			//------ Apply filter with the node name ------------------
			driver.findElement(By.id("dropdownMenuButton")).click();
			driver.findElement(By.linkText("Edit viewlet")).click();
			
			//Select node value 
			driver.findElement(By.xpath("//div/div/div[2]/div/div[2]/div/ng-select/div/span")).click();
			Thread.sleep(2000);
			 try 
				{
		        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
					List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
					System.out.println(mdivs.size());	
					
					for (WebElement mdi : mdivs)
					{
						if(mdi.getText().equals(name))
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
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			
			//Search the viewlet data using name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Queuename);
			Thread.sleep(2000);
			
			//Strore the data into particular string
			String finaldata=driver.findElement(By.cssSelector("datatable-body.datatable-body")).getText();
			System.out.println("Attributes in viewlet are: " +finaldata);
			System.out.println("----------------------------------");
			
			//Clearing selection of object
			ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
			che1.Deselectcheckbox(Dashboardname, driver);
			
			//Search the viewlet data using name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Queuename);
			Thread.sleep(2000);
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]")).click();
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
			   	     try
			   	        {
			   	        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddhh.mm.ss");
			   	        	Date date = format.parse(verify);
			   	        }
			   	        catch(Exception e)
			   	        {
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
			}
			
			driver.findElement(By.cssSelector(".close-button")).click();
			
			String Values=buffer.toString();
			Values=Values.trim();
			System.out.println("Attributes in popup are: " +Values);
			
			//clear search data
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			Thread.sleep(3000);
			
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
		
		public void ChannelAttributes(String Dashboardname, WebDriver driver, String schemaName, String Attributes, String WGSName) throws InterruptedException
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
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
			        	 if(!(verify.isEmpty() || verify.contains("Last Updated") || verify.contains("Last Event Time") || verify.contains("Alteration Time") || verify.contains("Header Compression") || verify.contains("Message Compression") || verify.contains("Attributes")))
			        	{
			        		//System.out.println("Printing attribute: " +ListOfAttributes);
			        		ListOfAttributes.append(links[k]);
				    		ListOfAttributes.append(',');
			        	}
			        }
				
			}
				   	    
	   	    String AttributeValues=ListOfAttributes.toString();
			String[] ListOfAttributesPresent = AttributeValues.split(",");
			//System.out.println(ListOfAttributesPresent);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			//Store the name into string
			String name=driver.findElement(By.xpath("//datatable-body-cell[5]/div/span")).getText();
			Thread.sleep(2000);
			
			//Store the Queue name into string 
			String Channelname=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
			Thread.sleep(2000);
			
			//------------- Create user Schema With the stored attributes ------- 
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
			
			String Objects=driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div/table/tbody")).getText();
			//System.out.println("Objects list is: " +Objects);
			
			//Add the Required attributes which are located in the Object attribute page
			for (String FinalListOfAttributes : ListOfAttributesPresent)
			{
				
				if(Objects.contains(FinalListOfAttributes))
				{
					System.out.println("Attribute is: " +FinalListOfAttributes);
					
					if(FinalListOfAttributes.contains("Transmission Queue") || FinalListOfAttributes.contains("Non-persistent Message Speed"))
				    {
				    	System.out.println("Extra object");
				    }
					else
					{
						driver.findElement(By.xpath("//td[contains(.,'"+ FinalListOfAttributes +"')]")).click();
						//driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
						Thread.sleep(1000);
						driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
						//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
						Thread.sleep(1000);
					}
				}
			}
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(8000);
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
			Thread.sleep(2000); 
			
			//------ Apply filter with the node name ------------------
			driver.findElement(By.id("dropdownMenuButton")).click();
			driver.findElement(By.linkText("Edit viewlet")).click();
			Thread.sleep(2000);
			
			//Select node value 
			driver.findElement(By.xpath("//div/div/div[2]/div/div[2]/div/ng-select/div/span")).click();
			Thread.sleep(2000);
			 try 
				{
		        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
					List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
					System.out.println(mdivs.size());	
					
					for (WebElement mdi : mdivs)
					{
						if(mdi.getText().equals(name))
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
			Thread.sleep(3000);
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			
			//Search the viewlet data using name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Channelname);
			Thread.sleep(2000);
			
			//Strore the data into particular string
			String finaldata=driver.findElement(By.cssSelector("datatable-body.datatable-body")).getText();
			System.out.println("Attribute values of viewlet: " +finaldata);
			System.out.println("----------------------------------");
			
			//Clearing selection of object
			ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
			che1.Deselectcheckbox(Dashboardname, driver);
			
			//Search the viewlet data using name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Channelname);
			Thread.sleep(2000);
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
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
			   	     try
			   	        {
			   	        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddhh.mm.ss");
			   	        	Date date = format.parse(verify);
			   	        }
			   	        catch(Exception e)
			   	        {
			   	        if(!verify.isEmpty()) 
			   	        {
			   	    	 String None= "None";
			   	    	if(!(verify.contains(None) || verify.contains("hours") || verify.contains("PM") || verify.contains("Attribute Value")))
			   	    	 {
			   	         buffer.append(ObjectAttributes[i]);
			    	     buffer.append('\n');
			    	     }
			   	    	 }
			   	        }
			   	     }
				}
			}
			
			//System.out.println(buffer);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			String Values=buffer.toString();
			Values=Values.trim();
			System.out.println("Attributes in popup page are: " +Values);
			
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
			
			//clear search data
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			Thread.sleep(3000);
			
			//Refresh the Viewlet
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
		
		public void ObjectAttributesVerification(String Dashboardname, WebDriver driver, String schemaName, String WGSName) throws InterruptedException
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
			        	
			        	if(!(verify.contains(None) || verify.contains("Attributes")))
			        	{
			        		//System.out.println("Printing attribute: " +ListOfAttributes);
			        		ListOfAttributes.append(links[k]);
				    		ListOfAttributes.append(',');
			        	}
			        }
				
			}
				   	    
	   	    String AttributeValues=ListOfAttributes.toString();
			String[] ListOfAttributesPresent = AttributeValues.split(",");
			//System.out.println(ListOfAttributesPresent);
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
			
			String Objects=driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div/table/tbody")).getText();
			//System.out.println("Objects list is: " +Objects);
			
			//Add the Required attributes which are located in the Object attribute page
			for (String FinalListOfAttributes : ListOfAttributesPresent)
			{
				//System.out.println("Attribute is: " +FinalListOfAttributes);
				if(Objects.contains(FinalListOfAttributes))
				{
					driver.findElement(By.xpath("//td[contains(.,'"+ FinalListOfAttributes +"')]")).click();
					//driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
					Thread.sleep(1000);
					driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
					//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
					Thread.sleep(3000);
				}
			}
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(8000);
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
			Thread.sleep(3000);
						
			//Search the viewlet data using name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
			Thread.sleep(2000);
			
			//Strore the data into particular string
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
			System.out.println(Values);
					
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
						
			//Refresh the Viewlet
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);		
		}
		
		public void TopicObjectAttributesVerification(String Dashboardname, WebDriver driver, String schemaName, String WGSName) throws InterruptedException
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
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
			        	
			        	if(!(verify.contains(None) || verify.contains("Attributes")))
			        	{
			        		//System.out.println("Printing attribute: " +ListOfAttributes);
			        		ListOfAttributes.append(links[k]);
				    		ListOfAttributes.append(',');
			        	}
			        }
				
			}
				   	    
	   	    String AttributeValues=ListOfAttributes.toString();
			String[] ListOfAttributesPresent = AttributeValues.split(",");
			//System.out.println(ListOfAttributesPresent);
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
			
			String Objects=driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div/table/tbody")).getText();
			//System.out.println("Objects list is: " +Objects);
			
			//Add the Required attributes which are located in the Object attribute page
			for (String FinalListOfAttributes : ListOfAttributesPresent)
			{
				//System.out.println("Attribute is: " +FinalListOfAttributes);
				if(Objects.contains(FinalListOfAttributes))
				{
					driver.findElement(By.xpath("//td[contains(.,'"+ FinalListOfAttributes +"')]")).click();
					//driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
					Thread.sleep(1000);
					driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
					//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
					Thread.sleep(3000);
				}
			}
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(8000);
			driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
			Thread.sleep(3000);
						
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
			Thread.sleep(2000);
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
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
			System.out.println(Values);
					
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
						
			//Refresh the Viewlet
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);		
		}
		
		public void ObjectAttributesVerificationForNameList(String Dashboardname, WebDriver driver, String schemaName, String WGSName) throws InterruptedException
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
			        	
			        	if(!(verify.contains(None) || verify.contains("Attributes")))
			        	{
			        		//System.out.println("Printing attribute: " +ListOfAttributes);
			        		ListOfAttributes.append(links[k]);
				    		ListOfAttributes.append(',');
			        	}
			        }
				
			}
				   	    
	   	    String AttributeValues=ListOfAttributes.toString();
			String[] ListOfAttributesPresent = AttributeValues.split(",");
			//System.out.println(ListOfAttributesPresent);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			//Store the Manager name into string 
			String managername=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
			System.out.println(managername);
			
			//Store the name into string
			String name=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
			Thread.sleep(2000);
			
			//Create User Schema
			driver.findElement(By.xpath("//div[2]/div[3]/img")).click();
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
			Thread.sleep(3000);
			
			//Give schema name
			driver.findElement(By.name("name")).sendKeys(schemaName);
			Thread.sleep(4000);
			
			String Objects=driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div/table/tbody")).getText();
			System.out.println("Objects list is: " +Objects);
			
			//Add the Required attributes which are located in the Object attribute page
			for (String FinalListOfAttributes : ListOfAttributesPresent)
			{
				System.out.println("Attribute is: " +FinalListOfAttributes);
				if(Objects.contains(FinalListOfAttributes))
				{
					driver.findElement(By.xpath("//td[contains(.,'"+ FinalListOfAttributes +"')]")).click();
					//driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
					Thread.sleep(1000);
					driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
					//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
					Thread.sleep(3000);
				}
			}
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(8000);
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
			Thread.sleep(3000);
						
			//Search the viewlet data using name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
			Thread.sleep(2000);
			
			//Strore the data into particular string
			String finaldata=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
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
			   	    	 if(!(verify.contains("Attribute Value")))
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
			System.out.println(Values);
					
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
						
			//Refresh the Viewlet
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);		
		}
		
		public void ObjectAttributesVerificationForService(String Dashboardname, WebDriver driver, String schemaName, String WGSName) throws InterruptedException
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(2000);
			driver.findElement(By.linkText("Show Object Attributes")).click();
			Thread.sleep(6000);
			
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
			        	
			        	if(!(verify.contains(None) || verify.contains("Last Updated") || verify.contains("Attributes")))
			        	{
			        		//System.out.println("Printing attribute: " +ListOfAttributes);
			        		ListOfAttributes.append(links[k]);
				    		ListOfAttributes.append(',');
			        	}
			        }
				
			}
			
				   	    
	   	    String AttributeValues=ListOfAttributes.toString();
			String[] ListOfAttributesPresent = AttributeValues.split(",");
			//System.out.println("List is: " +ListOfAttributesPresent);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			//Store the Manager name into string 
			String managername=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
			System.out.println(managername);
			
			//Store the name into string
			String name=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
			Thread.sleep(2000);
			
			//Create User Schema
			driver.findElement(By.xpath("//div[2]/div[3]/img")).click();
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
			Thread.sleep(4000);
			
			//Give schema name
			driver.findElement(By.name("name")).sendKeys(schemaName);
			Thread.sleep(4000);
			
			String Objects=driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div/table/tbody")).getText();
			//System.out.println("Objects list is: " +Objects);
			
			//Add the Required attributes which are located in the Object attribute page
			for (String FinalListOfAttributes : ListOfAttributesPresent)
			{
				//System.out.println("Attribute is: " +FinalListOfAttributes);
				if(Objects.contains(FinalListOfAttributes))
				{
					driver.findElement(By.xpath("//td[contains(.,'"+ FinalListOfAttributes +"')]")).click();
					//driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
					Thread.sleep(3000);
					driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
					//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
					Thread.sleep(3000);
				}
			}
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(8000);
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
			Thread.sleep(6000);
						
			//Search the viewlet data using name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
			Thread.sleep(4000);
			
			//Strore the data into particular string
			String finaldata=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
			System.out.println(finaldata);
			System.out.println("----------------------------------");
			
			//Clearing selection of object
			ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
			che1.Deselectcheckbox(Dashboardname, driver);
			
			//Search the viewlet data using name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
			Thread.sleep(4000);
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Show Object Attributes")).click();
			Thread.sleep(6000);
			
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
			System.out.println(Values);
					
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
						
			//Refresh the Viewlet
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);	   	
		}
		
		
	
	
	
	public void SubscriptionObjectAttributesVerification(String Dashboardname, WebDriver driver, String schemaName, String AddSubscriptionNameFromIcon, String WGSName) throws InterruptedException
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
		        	
		        	if(!(verify.contains(None) || verify.contains("Attributes")))
		        	{
		        		//System.out.println("Printing attribute: " +ListOfAttributes);
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
			
		}
		   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");
		//System.out.println(ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Managername="Manager Name";
		String Node="Node Name";
		
		//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
		System.out.println(managername);
		
		//Store the name into string  
		String name=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Field name is: " +name);
		Thread.sleep(4000);
		
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
		
		String Objects=driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div/table/tbody")).getText();
		//System.out.println("Objects list is: " +Objects);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
			//System.out.println("Attribute is: " +FinalListOfAttributes);
			if(Objects.contains(FinalListOfAttributes))
			{
				driver.findElement(By.xpath("//td[contains(.,'"+ FinalListOfAttributes +"')]")).click();
				//driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(3000);
			}
		}
		
				
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(3000);
		
				
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string 
		String finaldata=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Viewler attributes list is: " +finaldata);
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
		System.out.println(Values);
				
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
				
		//Refresh the Viewlet
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(4000);		
	}
	
	public void AuthinfoObjectAttributesVerification(String Dashboardname, WebDriver driver, String schemaName, String WGSName) throws InterruptedException
	{		
		String Managername="Manager Name";
		String Node="Node Name";
		
		//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
		System.out.println(managername);
				
		//Store the name into string
		String name=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(name);
		Thread.sleep(4000);
		
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
		
		//Click on Add button
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000); 
		
		//Add the Required attributes which are located in the Object attribute page
		driver.findElement(By.xpath("//button[contains(.,'Add all ')]")).click();
		driver.findElement(By.cssSelector("td[title=\""+ Managername +"\"]")).click();
		driver.findElement(By.xpath("//span[contains(.,' Remove')]")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.cssSelector("td[title=\""+ Node +"\"]")).click();
		driver.findElement(By.xpath("//span[contains(.,' Remove')]")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.cssSelector("td[title='Last Updated']")).click();
		driver.findElement(By.xpath("//span[contains(.,' Remove')]")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(3000);
				
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
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
		System.out.println(Values);
				
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
				
		//Refresh the Viewlet
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(4000);		
	}
	
	public void ClusterQMObjectAttributesVerification(String Dashboardname, WebDriver driver, String schemaName, String WGSName) throws InterruptedException
	{		
		String Managername="Manager Name";
		String Node="Node Name";
		String Last="Last Updated";
		
		//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
		System.out.println(managername);
				
		//Store the name into string
		String name=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(name);
		Thread.sleep(4000);
		
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
		driver.findElement(By.xpath("//button[contains(.,'Add all ')]")).click();
		driver.findElement(By.cssSelector("td[title=\""+ Managername +"\"]")).click();
		driver.findElement(By.xpath("//span[contains(.,' Remove')]")).click();
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector("td[title=\""+ Node +"\"]")).click();
		driver.findElement(By.xpath("//span[contains(.,' Remove')]")).click();
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector("td[title=\""+ Last +"\"]")).click();
		driver.findElement(By.xpath("//span[contains(.,' Remove')]")).click();
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(10000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(3000);
				
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
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
		System.out.println(Values);
				
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
				
		//Refresh the Viewlet
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(4000);		
	}
	
	
	
	public void ChannelAuthRecordAttributes(String Dashboardname, WebDriver driver, String SchemaName, String WGSName) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(6000);
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
		        	
		        	if(!(verify.contains(None) || verify.contains("Attributes")))
		        	
		        	{
		        		//System.out.println("Printing attribute: " +ListOfAttributes);
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
			
		}
		   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");
		//System.out.println(ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();
				
		//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
		System.out.println("Manager name: " +managername);
				
		//Store the name into string
		String name=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		Thread.sleep(2000);
		
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
		
		String Objects=driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div/table/tbody")).getText();
		//System.out.println("Objects list is: " +Objects);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
			//System.out.println("Attribute is: " +FinalListOfAttributes);
			if(Objects.contains(FinalListOfAttributes))
			{
				driver.findElement(By.xpath("//td[contains(.,'"+ FinalListOfAttributes +"')]")).click();
				//driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(3000);
			}
		}
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(3000);
				
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
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
		System.out.println(Values);
				
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
				
		//Refresh the Viewlet
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(4000);		
	}
	
	public void CommunicationInfoAttributes(String Dashboardname, WebDriver driver, String SchemaName, String WGSName) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(6000);
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
		        	
		        	if(!(verify.contains(None) || verify.contains("Attributes") || verify.contains("Alteration Time") || verify.contains("Group Address") || verify.contains("Last Updated")))
		        	
		        	{
		        		//System.out.println("Printing attribute: " +ListOfAttributes);
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
			
		}
		   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");
		//System.out.println(ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();
				
		//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
		System.out.println("Manager name: " +managername);
				
		//Store the name into string
		String name=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		Thread.sleep(2000);
		
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
		
		String Objects=driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div/table/tbody")).getText();
		//System.out.println("Objects list is: " +Objects);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
			//System.out.println("Attribute is: " +FinalListOfAttributes);
			if(Objects.contains(FinalListOfAttributes))
			{
				driver.findElement(By.xpath("//td[contains(.,'"+ FinalListOfAttributes +"')]")).click();
				//driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div/div[2]/button[2]")).click();
				//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
				Thread.sleep(3000);
			}
		}
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(3000);
		
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
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
		   	    	 if(!(verify.contains("Attribute Value") || verify.contains("hours") || verify.contains(".0") || verify.contains(".1") || verify.contains(".2") || verify.contains(".3") || verify.contains(".4") || verify.contains(".5") || verify.contains(".6") || verify.contains(".7") || verify.contains(".8") || verify.contains(".9")))
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
		System.out.println(Values);
				
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
		
		
		//Refresh the Viewlet
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(4000);		
	}
		
 }




