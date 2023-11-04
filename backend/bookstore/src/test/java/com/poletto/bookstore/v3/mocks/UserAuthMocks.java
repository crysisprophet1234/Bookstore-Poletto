package com.poletto.bookstore.v3.mocks;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserAuthMocks {

	public static RequestSpecification AdminPrivilegesUser() {

		Map<String, String> user = new HashMap<>();
		user.put("email", "admin@gmail.com");
		user.put("password", "123456");

		Response response = given()
				.basePath("api/auth/v3/authenticate")
				.contentType(ContentType.JSON)
				.body(user)
				.then()
				.when()
				.post()
				.then()
				.extract()
				.response();

		JsonPath jsonPath = JsonPath.from(response.getBody().asString());

		return new RequestSpecBuilder().addHeader("Authorization", "Bearer " + jsonPath.getString("token")).build();

	}

	public static RequestSpecification UserWithToken(String email) {

		Map<String, String> user = new HashMap<>();
		user.put("email", email);
		user.put("password", "123456");

		Response response = given()
				.basePath("api/auth/v3/authenticate")
				.contentType(ContentType.JSON)
				.body(user)
				.then()
				.when()
				.post()
				.then()
				.extract()
				.response();

		JsonPath jsonPath = JsonPath.from(response.getBody().asString());

		return new RequestSpecBuilder().addHeader("Authorization", "Bearer " + jsonPath.getString("token")).build();

	}

}
