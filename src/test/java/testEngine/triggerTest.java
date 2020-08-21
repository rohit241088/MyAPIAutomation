package testEngine;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import utils.RequestBuilder;

public class triggerTest {
	static RequestBuilder build=null;

@BeforeTest
public void setup() {
	build=new RequestBuilder(System.getProperty("user.dir")+"\\src\\test\\java\\resources\\Google Apis.xlsx");
}


@DataProvider(name="resourcesData")
public Object[][] getResources(){
	Object[][] data=null;
	int rowNumber=build.getFirstColumnValues("ExcelSheet").size();
	int column=1;
	data=new Object[rowNumber][column];
	Iterator<String>allResources=build.getFirstColumnValues("ExcelSheet").keySet().iterator();
	for(int i=0;i<rowNumber;i++) {
		String value=allResources.next();
		
		data[i][column-1]=value;
	}
	return data;
}
	@Test(dataProvider="resourcesData")
	public void triggerRequest(String resourceName) {
		
		try {
				build.buildRequest(resourceName,"ExcelSheet");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
	}
}
