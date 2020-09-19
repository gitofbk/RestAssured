package common;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import data.DP;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseRest extends DP {
	public static RequestSpecification request;
	protected Response response;
	private String uri;
	private static ExtentHtmlReporter reporter;
	protected static ExtentReports report;
	private ExtentTest test;
	protected String newLine = "<BR>";
	protected String seperator = " : ";
	protected String startBold = "<B>";
	protected String endBold = "</B>";

	@BeforeSuite
	public void initialize() {
		reporter = new ExtentHtmlReporter("report.html");
		report = new ExtentReports();
		report.attachReporter(reporter);
	}

	@AfterSuite
	public void tearDown() {
		report.flush();
	}

	@BeforeMethod
	@Parameters({ "uri", "resource" })
	public void beforeTest(@Optional("https://jsonplaceholder.typicode.com") String uri,
			@Optional("/posts") String resource, Method name) {

		this.uri = uri;
		startTest(name.getName());
		String message = createRequest(uri + resource);
		log(true, message);

	}

	@AfterMethod
	public void afterMethod() {

		flush();
	}

	protected void validate(int statuscode, String type) {
		if (statuscode == response.getStatusCode()) {

			log(true, getStatusCode(response));
		} else {
			log(false, "Status Code Validation" + newLine + "Expected" + seperator + bold(statuscode) + newLine
					+ "Actual" + seperator + bold(response.getStatusCode()));
		}

		if (type.equals(response.getContentType())) {
			log(true, getContentType(response));
		} else {
			log(false, "Content Type Validation" + seperator + "Expected" + seperator + bold(type) + newLine + "Actual"
					+ seperator + bold(response.getContentType()));
		}
		
	}
	
	protected void validateRecordCount(int n)
	{
		int size=0;
		System.out.println(response.jsonPath().getJsonObject("$").getClass());
		if(response.jsonPath().getJsonObject("$").getClass().equals(ArrayList.class))
		{
			size=response.jsonPath().getList("$").size();
		}else if(response.jsonPath().getJsonObject("$").getClass().equals(LinkedHashMap.class))
			size=1;
			
		if(size==n)
			log(true,"Number of records returned is "+n);
		else
			log(false,"Number of records Validation"+seperator
					+newLine+"Expected"+seperator+n
					+newLine+"Actual"+seperator+size);
			log(true, printResponse(response));

	}

	protected void validateBody( Object id) {
		validateRecordCount(1);
		String actual = response.jsonPath().getString("id");
		if(actual.equals(id.toString()))
			log(true, bold("id" + seperator + id));
		else
			log(false,"Expected" + seperator + bold(id) + newLine + "Actual" + seperator
					+ bold(actual));
		log(true, printResponse(response));
	}

	protected void validateSchema(File f)
	{
		response.then().assertThat().body(matchesJsonSchema(f));
		try {
			log(true,"Response Body matches with schema:"+newLine+FileUtils.readFileToString(f));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	protected void validateBody(HashMap<String, Object> params) {
		validateRecordCount(1);
		
		String message;
		for (Map.Entry<String, Object> param : params.entrySet()) {

			String expected = param.getValue().toString();
			String actual = response.jsonPath().getString(param.getKey());
			message = "Response Body Validation" + seperator + param.getKey();
			if (actual.equals(expected)) {
				log(true, bold(param.getKey() + seperator + actual));
			} else
				log(false, message + newLine + "Expected" + seperator + bold(expected) + newLine + "Actual" + seperator
						+ bold(actual));
		}
		log(true, printResponse(response));
	}

	protected String bold(Object s) {
		return startBold + s + endBold;
	}

	protected String bold(int i) {
		return startBold + i + endBold;
	}

	protected void flush() {
		report.flush();
	}

	private void startTest(String name) { // TODO Auto-generated method stub
		test = report.createTest(name);
	}

	protected void log(Boolean result, String details) {

		if (result)
			test.log(Status.PASS, details);
		else
			test.log(Status.FAIL, details);
	}

	public String createRequest(String uri) {
		RestAssured.baseURI = uri;
		request = RestAssured.given();
		return "Created Request with URI : " + uri;
	}

	public void createRequest(String uri, String header, String value) {
		RestAssured.baseURI = uri;
		request = RestAssured.given();
		request.header(header, value);
	}

	public void addHeaders(HashMap<String, Object> headers) {
		// TODO Auto-generated method stub
		String message = "Adding below headers : ";
		for (Map.Entry<String, Object> header : headers.entrySet()) {

			request.header(header.getKey(), header.getValue());
			message = message + newLine + header.getKey() + seperator + startBold + header.getValue() + endBold;
		}
		log(true, message);
	}

	public Response get() {
		response = request.get();
		return response;
	}

	public Response get(Object i) {
		response = request.pathParam("path", i).get("/{path}");

		return response;
	}

	public Response delete(Object i) {
		response = request.pathParam("path", i).delete("/{path}");

		return response;
	}

	public Response post(String body) {
		response = request.body(body).post();
		log(true, "Request Body" + seperator + newLine + startBold + body + endBold);
		return response;
	}

	public Response put(String body) {
		response = request.body(body).post();
		log(true, "Request Body" + seperator + newLine + startBold + body + endBold);
		return response;
	}

	public String printResponse(Response response) {
		System.out.println(response.getStatusCode());
		System.out.println(response.getContentType());
		return "Content" + seperator + newLine + startBold + response.prettyPrint() + endBold;
	}

	public String getStatusCode(Response response) {
		System.out.println(response.getStatusCode());
		return "Status Code" + seperator + startBold + response.getStatusCode() + endBold;
	}

	public String getContentType(Response response) {

		System.out.println(response.getContentType());
		return "Content Type" + seperator + startBold + response.getContentType() + endBold;
	}
}
