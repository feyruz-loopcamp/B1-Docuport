package app.docuport.step_definitions;

import app.docuport.pages.HomePage;
import app.docuport.pages.LoginPage;
import app.docuport.pages.ProfilePage;
import app.docuport.utilities.ConfigurationReader;
import app.docuport.utilities.DocuportApiUtil;
import app.docuport.utilities.Driver;
import app.docuport.utilities.Environment;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class DocuportApiStepDefs {

    public static final Logger LOG = LogManager.getLogger();
    String baseUrl = Environment.BASE_URL;
    String accessToken;
    Response response;

    LoginPage loginPage = new LoginPage();

    HomePage homePage = new HomePage();

    ProfilePage profilePage = new ProfilePage();

    @Given("User logged in to Docuport api as advisor role")
    public void user_logged_in_to_Docuport_api_as_advisor_role() {

        String email = Environment.ADVISOR_EMAIL;
        String password = Environment.ADVISOR_PASSWORD;
        LOG.info("Authorizing adviser user - email: " + email + " - password: " + password);
        LOG.info("Environment base url: " + baseUrl);

        accessToken = DocuportApiUtil.getAccessToken(email, password);

        if (accessToken == null || accessToken.isEmpty()) {
            LOG.error("Could not authorize user in authorization server");
            fail("Could not authorize user in authorization server");// Assert.fail() -- this is from JUnit
        } else {
            LOG.info("Access token: " + accessToken);
        }


    }

    @Given("User sends GET request to {string} with query param {string} email address")
    public void user_sends_GET_request_to_with_query_param_email_address(String endpoint, String userType) {

        String emailAddress = "";

        // You can implement the other user types on your own.
        switch (userType){
            case "advisor":
                emailAddress = Environment.ADVISOR_EMAIL;
                break;
            case "client":
                emailAddress = Environment.CLIENT_EMAIL;
                break;
            default:
                LOG.info("The user type is invalid or that user has not been implemented.");
        }

        response = given().accept(ContentType.JSON)
                .and().header("Authorization", accessToken)
                .and().queryParam("EmailAddress", emailAddress)
                .when().get(baseUrl + endpoint);

        response.then().log().all();


    }

    @Then("status code should be {int}")
    public void status_code_should_be(int expStatusCode) {
        assertEquals("Invalid Status Code", expStatusCode, response.statusCode()); // Here we can have a message
        response.then().statusCode(expStatusCode); // This is doing the same thing as above with no Message
    }

    @Then("content type is {string}")
    public void content_type_is(String expContentType) {
        response.then().contentType(ContentType.JSON);
        assertEquals("Content type not matching: Expected was: " +expContentType + " but actual was " + response.contentType()
                , expContentType
                , response.contentType());
    }

    @Then("role is {string}")
    public void role_is(String expRole) {
        assertEquals(expRole, response.path("items[0].roles[0].name"));

        // This will do the same thing. We are just doing as practive
        JsonPath jsonPath = response.jsonPath();
        assertEquals(expRole, jsonPath.getString("items[0].roles[0].name"));

        // De-Serialization

    }


    @Given("User logged in to Docuport app as advisor role")
    public void user_logged_in_to_Docuport_app_as_advisor_role() {
        Driver.getDriver().get(Environment.URL);
        loginPage.login(Environment.ADVISOR_EMAIL, Environment.ADVISOR_PASSWORD);
        // Not the best option.
        //assertTrue(Driver.getDriver().getCurrentUrl().equals("https://beta.docuport.app/"));
    }

    @When("User goes to profile page")
    public void user_goes_to_profile_page() {
        homePage.goToProfilePage();
        // assert that Profile element/text/Title is displayed
        assertTrue(profilePage.profileTitle.isDisplayed());
    }

    @Then("User should see same info on UI and API")
    public void user_should_see_same_info_on_UI_and_API() {

        String fullNameUI = profilePage.fullName.getText();  //Batch1 Group1
        String [] fullNameArr = fullNameUI.split(" "); // [0], [1]
        Map <String, String> uIuserInfo = new HashMap<>();
        uIuserInfo.put("firstName", "Batch1");
        uIuserInfo.put("lastName", "Group1");
        String role =profilePage.role.getText();
        uIuserInfo.put("role", role);

        System.out.println("UI user info: " + uIuserInfo);

        // assertion between            UI           and            API
        assertEquals(uIuserInfo.get("firstName"), response.path("items[0].firstName"));
        assertEquals(uIuserInfo.get("lastName"), response.path("items[0].lastName"));
        assertEquals(uIuserInfo.get("role").toLowerCase(), response.path("items[0].roles[0].name"));

    }

}
