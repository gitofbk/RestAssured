package scripts;

import java.io.File;
import java.io.IOException;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import common.BaseRest;
import data.DP;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Assignment extends BaseRest {
	String uri;
	ExtentTest test;
	Response response;

	public String getParams(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		JSONObject requestParams = new JSONObject();
		for (Map.Entry<String, Object> param : params.entrySet())
			requestParams.put(param.getKey(), param.getValue());
		return requestParams.toJSONString();
	}

	

	@Test
	public void testGet() {

		response = get();
		validate(200,"application/json; charset=utf-8");
		validateRecordCount(100);
		
	}

	@Test(dataProvider = "Data_Get", dataProviderClass = DP.class)
	public void testGetWithFilePath(HashMap<String, Object> headers, Object path) {

		response = get(path);
		validate(200,"application/json; charset=utf-8");
		validateBody(path);
		File schema=new File("src/test/resources/responseschema.json");
		validateSchema(schema);
		
	}

	@Test(dataProvider = "Data_Post", dataProviderClass = DP.class)
	public void testPost(HashMap<String, Object> headers, HashMap<String, Object> params) {
		addHeaders(headers);
		response = post(getParams(params));
		validate(201,"application/json; charset=utf-8");
		validateBody(params);
		File schema=new File("src/test/resources/responseschema.json");
		validateSchema(schema);
	}

	@Test(dataProvider = "Data_Put", dataProviderClass = DP.class)
	public void testPut(HashMap<String, Object> headers, HashMap<String, Object> params) {
		addHeaders(headers);
		response = put(getParams(params));
		validate(200,"application/json; charset=utf-8");
		validateBody(params);
		File schema=new File("src/test/resources/responseschema.json");
		validateSchema(schema);
	}

	@Test(dataProvider = "Data_Delete", dataProviderClass = DP.class)
	public void testDelete(HashMap<String, Object> headers, Object path) {
		addHeaders(headers);
		response = delete(path);
		validate(200,"application/json; charset=utf-8");
		
	}

}
