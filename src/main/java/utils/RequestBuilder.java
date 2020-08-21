package utils;

import java.io.File;
import java.io.FileInputStream;

import static io.restassured.RestAssured.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import static org.hamcrest.Matchers.equalTo;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.joda.time.LocalDate;
import org.json.simple.JSONObject;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.lessThan;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Method;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojoRequest.CreateUser;

public class RequestBuilder extends ExcelUtils {

	RequestSpecification requestSpec = null;
	static PrintStream stream=null;
	Response response=null;
	RequestSpecBuilder spec=null;
	static FileInputStream in=null;
	private static String propertyFileLocation=System.getProperty("user.dir")+"\\src\\test\\java\\resources\\global.properties";
	static Properties prop=null;
	List<Filter>filters=null;
	String requestClassesDir="pojoRequest";
	String responseClassesDir="pojoResponse";
	boolean duplicate=false;
	String className=null;
 Object requestObject=new Object();
 Object responseObject=new Object();
	public RequestBuilder(String exceLocation) {
		super(exceLocation);
		// TODO Auto-generated constructor stub
	}
	
	private void basicSpec() {
		spec=new RequestSpecBuilder();
		spec.setBaseUri(getProperty(propertyFileLocation,"baseURL")).build();
				
		}
	
	private static String getProperty(String propertyFile,String key) {
		if(in==null) {
		try {
			in=new FileInputStream(propertyFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		
		if(prop==null) {
		prop=new Properties();
		try {
			prop.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return prop.getProperty(key);
		
	}
	
	private Map<String, String> returnBodyMap(String keys) {
		Map<String, String> bodyMap = new HashMap<>();
		String[] keysPair = keys.split(",");
		for (int i = 0; i < keysPair.length; i++) {
			bodyMap.put(keysPair[i].split("=")[0], keysPair[i].split("=")[1]);
		}
		return bodyMap;
	}

	public void buildRequest(String resource, String sheetName) throws FileNotFoundException {
		Map<String, Integer> headers = getHeaders(sheetName);
		basicSpec();
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		Method MethodType = null;
		String resourceAddress = null;
		int rowNumber =-1;
		if(stream==null) {
			stream=new PrintStream(new File(System.getProperty("user.dir")+"\\src\\test\\java\\logs\\logs.txt"));
		ResponseLoggingFilter.logResponseTo(stream);
		RequestLoggingFilter.logRequestTo(stream);
				
		}
		
		Map<String,Integer>resoucesList=getFirstColumnValues(sheetName);
		if (resoucesList.containsKey(resource)) {
			rowNumber=resoucesList.get(resource);
			

			for (int j = 1; j < getSheet().getRow(rowNumber).getLastCellNum(); j++) {
				if ((j == headers.get("Resource Address"))) {
					resourceAddress=returnCellValue(rowNumber,j);
				
				}
				if (j == headers.get("Method")) {
					if (returnCellValue(rowNumber, j).toString().equalsIgnoreCase("Get")) {
						MethodType = Method.GET;
						
					}
					else if (returnCellValue(rowNumber, j).toString().equalsIgnoreCase("Post")) {
						MethodType = Method.POST;
					
					}
					else if (returnCellValue(rowNumber, j).toString().equalsIgnoreCase("put")) {
						MethodType = Method.PUT;
					}
					else if (returnCellValue(rowNumber, j).toString().equalsIgnoreCase("delete")) {
						MethodType = Method.DELETE;
					}
					continue;

				}
				if (j == headers.get("QueryParam")&&!returnCellValue(rowNumber, j).equalsIgnoreCase("")) {
						Map<String, String> queryParam = returnBodyMap(returnCellValue(rowNumber, j));
						requestSpec=spec.addQueryParams(queryParam).build();
						continue;
					
				}

				if (j == headers.get("Header")&&!returnCellValue(rowNumber, j).equalsIgnoreCase("")) {
						Map<String, String> headerParam = returnBodyMap(returnCellValue(rowNumber, j));
						requestSpec=spec.addHeaders(headerParam).build();
						continue;
					

				}
				if (j == headers.get("File")&&!returnCellValue(rowNumber, j).equalsIgnoreCase("")) {

				}
				if (j == headers.get("PathParam")&&!returnCellValue(rowNumber, j).equalsIgnoreCase("")) {
						Map<String, String> pathParam = returnBodyMap(returnCellValue(rowNumber, j));
						requestSpec=spec.addPathParams(pathParam).build();
						continue;
					

				}
				if (j == headers.get("Body")&&!returnCellValue(rowNumber, j).equalsIgnoreCase("")) {
					
					try {
						
						className=requestClassesDir+"."+getSheet().getRow(rowNumber).getCell(headers.get("Resource Name")).getStringCellValue().trim();
					
						requestObject=Class.forName(className).newInstance();
						Field[] fields=requestObject.getClass().getDeclaredFields();
						int totalFields=fields.length;
					
							Map<String,String>keys=returnBodyMap(returnCellValue(rowNumber,j));
							Iterator<String>keysValue=keys.keySet().iterator();			
							while(keysValue.hasNext()) {
								for(int i=0;i<totalFields;i++) {
									String nextKey=keysValue.next();
									if(fields[i].getName().equalsIgnoreCase(nextKey)) {
										fields[i].set(requestObject,keys.get(nextKey));
									}
									
								}
							}
					
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					requestSpec=spec.setBody(requestObject).build();
					continue;
				}
			
		
			}
		}
int runmode=headers.get("Runmode");

			
			if(returnCellValue(rowNumber,runmode).equalsIgnoreCase("true")) {
				String className=responseClassesDir+"."+getSheet().getRow(rowNumber).getCell(headers.get("Resource Name")).getStringCellValue().trim();
				switch(MethodType) {
				case POST:
					response=given().log().all().spec(requestSpec).when().post(resourceAddress);
					response.then().log().all();
					
				
					break;
				
			case GET:
				response=given().log().all().spec(requestSpec).when().get(resourceAddress);
				response.then().log().all();
					
			
					break;
				
			case PUT:
				response=given().log().all().spec(requestSpec).when().put(resourceAddress);
				response.then().log().all();
					try {
						response.as(Class.forName(className));
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
					break;
				
				case DELETE:
					response=given().log().all().spec(requestSpec).when().delete(resourceAddress);
					response.then().log().all();
						try {
							response.as(Class.forName(className));
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				
					break;
				
				case PATCH:
					response=given().log().all().spec(requestSpec).when().patch(resourceAddress);
					response.then().log().all();
					
				
					break;
				}
		
			}
			else {
				throw new SkipException("Skipping test case"+className.split(this.responseClassesDir)[1].split("\\.")[1]);
			}
			
	}
}
