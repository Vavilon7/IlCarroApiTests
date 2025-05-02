package illcarro.tests;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeMethod;

public class TestBase {

    // Токен, полученный через логин пользователя printuser3@gmail.com
    public final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoicHJpbnR1c2VyM0BnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTc0NjgwMjAzNywiaWF0IjoxNzQ2MjAyMDM3fQ.A1XBMckfCj0GEAOZcqLkUNa8vF2BLtDwyHNdaOCm1t0";
    public final String LOGIN_ENDPOINT = "/user/login/usernamepassword";

    @BeforeMethod
    public void init() {
        RestAssured.baseURI = "https://ilcarro-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }
}
