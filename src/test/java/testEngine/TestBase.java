package testEngine;

import org.testng.annotations.BeforeTest;

import utils.RequestBuilder;

public class TestBase {
	public static RequestBuilder requestBuilder=null;
	private static String testDataFile=System.getProperty("user.dir")+"\\src\\test\\java\\resouces\\Google Apis.xlsx";
	@BeforeTest
	public void setup() {
		requestBuilder=new RequestBuilder(testDataFile);
	}
	

}
