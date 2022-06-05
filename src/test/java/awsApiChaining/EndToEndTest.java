package awsApiChaining;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndToEndTest {

	Response response;
	String BaseURI = "http://34.204.43.159:8088/employees";

	@Test
	public void test() {

		response = GetMethodAll();
		Assert.assertEquals(response.getStatusCode(), 200);

						
		response = PostMethod("New employee", "AWS test", 55990, "AWS_test@xyz.com");
		Assert.assertEquals(response.getStatusCode(), 201);
		JsonPath jpath = response.jsonPath();
		int empId = jpath.get("id");
		System.out.println(empId);

		response = PutMethod("Update employee", "AWS-test", 5990, "AWS_test@xyz.com", empId);
		Assert.assertEquals(response.getStatusCode(), 200);
		jpath = response.jsonPath();
		String name = jpath.getString("firstName");
		Assert.assertEquals(name, "Update employee");

		response = DeleteMethod(empId);
		Assert.assertEquals(response.getStatusCode(), 200);

		response = GetMethod(empId);
		Assert.assertEquals(response.getStatusCode(), 400);
		jpath = response.jsonPath();
		String message = jpath.getString("message");
		Assert.assertEquals(message, "Entity Not Found");

	}

	public Response GetMethodAll() {

		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();

		Response response = request.get();
		return response;
	}

	public Response PostMethod(String fname, String lname, int salary, String email) {

		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();

		// Create map object with data
		HashMap<String, Object> mapObj = new HashMap<String, Object>();
		mapObj.put("firstName", fname);
		mapObj.put("lastName", lname);
		mapObj.put("salary", salary);
		mapObj.put("email", email);

		Response response = request.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(mapObj)
				.post();

		return response;
	}

	public Response PutMethod(String fname, String lname, int salary, String email, int empid) {

		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();

		// Create map object with data
		HashMap<String, Object> mapObj = new HashMap<String, Object>();
		mapObj.put("firstName", fname);
		mapObj.put("lastName", lname);
		mapObj.put("salary", salary);
		mapObj.put("email", email);

		Response response = request.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(mapObj)
				.put("/" + empid);

		return response;
	}

	public Response DeleteMethod(int empid) {

		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();

		Response response = request.delete("/" + empid);
		return response;
	}

	public Response GetMethod(int empid) {

		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();

		Response response = request.get("/" + empid);
		return response;
	}
}
