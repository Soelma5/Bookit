package com.bookit.step_definitions;

import com.bookit.utilities.BookitUtils;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.DB_Util;
import com.bookit.utilities.Environment;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;

public class ApiStepDefs {

    String token;
    Response response;
    String emailGlobal;
    @Given("I logged Bookit api using as a {string}")
    public void i_logged_bookit_api_using_as_a(String role) {
        token = BookitUtils.generateTokenByRole(role);
        System.out.println("token = " + token);

        Map<String, String> credentials = BookitUtils.returnCredentials(role);
        emailGlobal = credentials.get("email");
    }
    @When("I sent get get request to {string} endpoint")
    public void i_sent_get_get_request_to_endpoint(String endpoint) {
        response = given()
        .accept(ContentType.JSON)
                .header("Authorization", token)
                .when().get(Environment.BASE_URL + endpoint);
    }
    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {

        assertEquals(expectedStatusCode, response.statusCode());
    }
    @Then("content type is {string}")
    public void content_type_is(String expectedContentType) {

        assertEquals(expectedContentType, response.contentType());
    }
    @Then("role is {string}")
    public void role_is(String expectedRole) {
        System.out.println("response.path(\"role\") = " + response.path("role"));
        assertEquals(expectedRole, response.path("role"));
    }
    @Then("the information about current user from api and database should match")
    public void the_information_about_current_user_from_api_and_database_should_match() {
        // GET DATA FROM API

        // last name
        JsonPath jsonPath = response.jsonPath();
        String actualLastName = jsonPath.getString("lastName");

        // first name
        String actualFirstName = jsonPath.getString("firstName");

        // role
        String actualRole = jsonPath.getString("role");

        // GET DATA FROM DB

        String query = "select firstname, lastname, role from users where email = '"+emailGlobal+"'";
        DB_Util.runQuery(query);
        Map<String, String> dbMap = DB_Util.getRowMap(1);
        System.out.println("dbMap = " + dbMap);

        String expectedFirstname = dbMap.get("firstname");
        String expectedLastname = dbMap.get("lastname");
        String expectedRole = dbMap.get("role");


        // ASSERTIONS

        assertEquals(expectedFirstname, actualFirstName);
        assertEquals(expectedLastname, actualLastName);
        assertEquals(expectedRole, actualRole);

    }
    }


