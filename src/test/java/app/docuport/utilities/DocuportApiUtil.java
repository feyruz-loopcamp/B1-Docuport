package app.docuport.utilities;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

public class DocuportApiUtil {


    public static String getAccessToken(String email, String password){

        String jsonBody = "{\n" +
                "\"usernameOrEmailAddress\": \"" +email+"\",\n" +
                "\"password\": \"" + password + "\"\n" +
                "}";

        System.out.println(jsonBody);
        System.out.println(Environment.BASE_URL + "/api/v1/authentication/account/authenticate");

        String accessToken = given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .and().body(jsonBody)
                .when().post(Environment.BASE_URL + "/api/v1/authentication/account/authenticate")
                .then().assertThat().statusCode(200)
                .and().extract().path("user.jwtToken.accessToken");

//       System.out.println("accessToken = " + accessToken);
//        assertThat("accessToken is empty or null", accessToken, not(emptyOrNullString()));

        return "Bearer " + accessToken;
    }

}
