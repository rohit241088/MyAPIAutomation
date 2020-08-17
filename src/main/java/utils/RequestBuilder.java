package utils;

import java.io.File;
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
import java.util.concurrent.TimeUnit;
import static org.hamcrest.Matchers.equalTo;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.joda.time.LocalDate;
import org.json.simple.JSONObject;
import static org.hamcrest.Matchers.lessThan;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.CreateUser;

public class RequestBuilder extends ExcelUtils {

	private RequestSpecification requestSpec = null;
private Object object=null;
	public RequestBuilder(String exceLocation) {
		super(exceLocation);
		// TODO Auto-generated constructor stub
	}

	
	private Map<String, String> returnBodyMap(String keys) {
		Map<String, String> bodyMap = new HashMap<>();
		String[] keysPair = keys.split(",");
		for (int i = 0; i < keysPair.length; i++) {
			bodyMap.put(keysPair[i].split("=")[0], keysPair[i].split("=")[0]);
		}
		return bodyMap;
	}

	public void buildRequest(String resource, String sheetName) {
		Map<String, Integer> headers = getHeaders(sheetName);
		RequestSpecification requestspec=null;
		RequestSpecBuilder req = new RequestSpecBuilder();
		requestspec=req.build();
		Method MethodType = null;
		String resourceAddress = null;
		int rowNumber =-1;
		if (getFirstColumnValues(sheetName).containsKey(resource)) {
			rowNumber=getFirstColumnValues(sheetName).get(resource);
			

			for (int j = 1; j < getSheet().getRow(rowNumber).getLastCellNum(); j++) {
				if ((j == headers.get("Resource Address"))) {
					resourceAddress=returnCellValue(rowNumber,j);
				}
				if (j == headers.get("Method")) {
					if (returnCellValue(rowNumber, j).toString().equalsIgnoreCase("Get")) {
						MethodType = Method.GET;
					}
					if (returnCellValue(rowNumber, j).toString().equalsIgnoreCase("Post")) {
						MethodType = Method.POST;
					}
					if (returnCellValue(rowNumber, j).toString().equalsIgnoreCase("put")) {
						MethodType = Method.PUT;
					}
					if (returnCellValue(rowNumber, j).toString().equalsIgnoreCase("delete")) {
						MethodType = Method.DELETE;
					}

				}
				if (j == headers.get("QueryParam")) {
					if (!returnCellValue(rowNumber, j).toString().equalsIgnoreCase("")) {
						Map<String, String> queryParam = returnBodyMap(returnCellValue(rowNumber, j));
						requestspec=req.addQueryParams(queryParam).build();
					}
				}

				if (j == headers.get("Header")) {
					if (!returnCellValue(rowNumber, j).toString().equalsIgnoreCase("")) {
						Map<String, String> headerParam = returnBodyMap(returnCellValue(rowNumber, j));
						requestspec=req.addHeaders(headerParam).build();
					}

				}
				if (j == headers.get("File")) {

				}
				if (j == headers.get("PathParam")) {
					if (!returnCellValue(rowNumber, j).toString().equalsIgnoreCase("")) {
						Map<String, String> pathParam = returnBodyMap(returnCellValue(rowNumber, j));
						requestspec=req.addPathParams(pathParam).build();
					}

				}
				if (j == headers.get("Body")) {

					try {
						object=Class.forName(getSheet().getRow(rowNumber).getCell(headers.get("Resouce Name")).getStringCellValue()).newInstance();
						Field[] fields=object.getClass().getDeclaredFields();
						int totalFields=fields.length;
					
							Map<String,String>keys=returnBodyMap(returnCellValue(rowNumber,j));
							Iterator<String>keysValue=keys.keySet().iterator();			
							while(keysValue.hasNext()) {
								for(int i=0;i<totalFields;i++) {
									String nextKey=keysValue.next();
									if(fields[i].getName().equalsIgnoreCase(nextKey)) {
										fields[i].set(object,keys.get(nextKey));
									}
									
								}
							}
					
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					requestspec=req.setBody(object).build();
				}
			
		
			}
		}
	for(int i=0;i<getSheet().getRow(rowNumber).getLastCellNum();i++) {
		
			
			if(returnCellValue(rowNumber,i).equalsIgnoreCase("true")) {
				if(MethodType==Method.GET) {
					given().spec(requestSpec).when().get(resourceAddress);
				}
				if(MethodType==Method.POST) {
					given().spec(requestSpec).when().post(resourceAddress);
				}
				if(MethodType==Method.PUT) {
					given().spec(requestSpec).when().put(resourceAddress);
				}
				if(MethodType==Method.DELETE) {
					given().spec(requestSpec).when().delete(resourceAddress);
				}
				if(MethodType==Method.PATCH) {
					given().spec(requestSpec).when().patch(resourceAddress);
				}
			}
		}
	}
}
