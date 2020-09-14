package data;



import java.util.HashMap;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class DP {
	
	@DataProvider(name="Data_Put")
	public static Object[][] dataForPut() {
		HashMap<String, Object> header = new HashMap<String, Object>();
		header.put("Content-type", "application/json; charset=UTF-8");
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("id", 1);
		param.put("title", "abc");
		param.put("body", "xyz");
		param.put("userId", 1);
		
		
		Object[][] obj = {
				{  header, param }
		};
		
		return obj;
	}

	@DataProvider(name="Data_Post")
	public static Object[][] dataForPost() {
		HashMap<String, Object> header = new HashMap<String, Object>();
		header.put("Content-type", "application/json; charset=UTF-8");
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("title", "foo");
		param.put("body", "xybarz");
		param.put("userId", 1);
		
		
		Object[][] obj = {
				{  header, param }
		};
		
		return obj;
	}

	@DataProvider(name="Data_Delete")
	public static Object[][] dataForDelete() {
		HashMap<String, Object> header = new HashMap<String, Object>();
		header.put("Content-type", "application/json; charset=UTF-8");
		
		
		Object[][] obj = {
				{  header, 1 }
		};
		
		return obj;
	}

	@DataProvider(name="Data_Get")
	public static Object[][] dataForGet() {
		HashMap<String, Object> header = new HashMap<String, Object>();
		header.put("Content-type", "application/json; charset=UTF-8");
		
		
		Object[][] obj = {
				{  header, 1 }
		};
		
		return obj;
	}
	
	
	
}
