package scripts;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import common.BaseRest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class InvalidCase extends BaseRest {
	Response response;

	@Test
	public void testInvalidPost() {
		response = get();
		validate(404,"application/json; charset=utf-8");
	}
}
