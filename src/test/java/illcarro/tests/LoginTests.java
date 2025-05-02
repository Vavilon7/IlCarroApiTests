package illcarro.tests;

import illcaro.dto.AuthRequestDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.notNullValue;

public class LoginTests extends TestBase {

    //  Успешный логин: проверяем статус 200 и печатаем accessToken
    @Test
    public void loginAndPrintToken() {
        AuthRequestDto user = AuthRequestDto.builder()
                .username("printuser3@gmail.com")
                .password("Password@3")
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .extract().response();

        System.out.println("===== loginAndPrintToken =====");
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Token: " + response.jsonPath().getString("accessToken"));
        System.out.println("=================================");
    }

    // Успешный логин: проверка, что в теле есть ключ "accessToken"
    @Test
    public void loginWithTokenCheck() {
        AuthRequestDto user = AuthRequestDto.builder()
                .username("printuser3@gmail.com")
                .password("Password@3")
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .extract().response();

        System.out.println("===== loginWithTokenCheck =====");
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Token Present: " + (response.jsonPath().get("accessToken") != null));
        System.out.println("=================================");
    }

    // Неуспешный логин: неправильный пароль → ожидаем 401 Unauthorized
    @Test
    public void loginWithInvalidPasswordShouldFail() {
        AuthRequestDto user = AuthRequestDto.builder()
                .username("printuser3@gmail.com")
                .password("WrongPassword")
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .statusCode(401)
                .body("message", containsString("Login or Password incorrect"))
                .extract().response();

        System.out.println("===== loginWithInvalidPasswordShouldFail =====");
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Message: " + response.jsonPath().getString("message"));
        System.out.println("=================================");
    }

    // Проверка наличия ключа accessToken в JSON-ответе
    @Test
    public void loginResponseShouldContainAccessToken() {
        AuthRequestDto user = AuthRequestDto.builder()
                .username("printuser3@gmail.com")
                .password("Password@3")
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .statusCode(200)
                .body("$", hasKey("accessToken"))
                .extract().response();

        System.out.println("===== loginResponseShouldContainAccessToken =====");
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Access Token Present: " + response.jsonPath().getString("accessToken"));
        System.out.println("=================================");
    }
}