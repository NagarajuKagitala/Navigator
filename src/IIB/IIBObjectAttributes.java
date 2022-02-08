package IIB;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Common.ClearSelectionofCheckbox;

public class IIBObjectAttributes 
{
	public void IIBBrokerShowObjectAttributes(String Dashboardname, WebDriver driver, String schemaName) throws InterruptedException
	{
		//Clearing selection of object
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(6000);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(4000);
		
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
		Thread.sleep(10000);
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		Thread.sleep(6000); 
				
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[2]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[2]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println("Viewlet data is: " +finaldata);
		System.out.println("----------------------------------");
			
		//clear checkboxes
		driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
		Thread.sleep(6000);
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
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
				
		//Edit the search field data
	    for(int j=0; j<=name.length(); j++)
	    {
	    	
	    driver.findElement(By.xpath("(//input[@type='text'])[2]")).sendKeys(Keys.BACK_SPACE);
	    }
	    Thread.sleep(4000);
		
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
				
		//Refresh the Viewlet
		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
		Thread.sleep(4000);
	}
	
}
