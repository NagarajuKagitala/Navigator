package ApodGUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import javax.xml.parsers.ParserConfigurationException;
import org.testng.xml.Parser;
import org.testng.xml.XmlSuite;

import org.testng.TestNG;
//import org.xml.sax.SAXException;


public class TestMultiple 
{

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
	//PrintStream fileOut = new PrintStream("Log.txt");
	//System.setOut(fileOut);
	
	//System.console();

	TestNG testng = new TestNG(); 
	//testng.setXmlSuites((List <XmlSuite>)(new Parser("MultipleFilesRunning.xml").parse()));
	//testng.setXmlSuites(singletonList());
	//testng.setXmlSuites((List <XmlSuite>)(new Parser("MultipleFilesRunning.xml").parse()));
	//testng.setSuiteThreadPoolSize(1);
	
	List<String> suitefiles=new ArrayList<String>();

	// Add xml file which you have to execute
	suitefiles.add("./xmls/MultipleFilesRunning.xml");
	//suitefiles.add("./NavigatorCore.xml");

	// now set xml file for execution
	testng.setTestSuites(suitefiles);
	testng.run();
	
	
	
	
  
    }
}
