package illcarro.tests;

import illcaro.dto.AuthRequestDto;
import illcaro.dto.AuthResponseDto;
import illcaro.dto.ErrorDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;


public class LoginTests extends TestBase {

    // Успешный логин: получаем и печатаем accessToken
    @Test
    public void loginAndPrintToken() {
        AuthRequestDto user = AuthRequestDto.builder()
                .username("printuser3@gmail.com")
                .password("Password@3")
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post(LOGIN_ENDPOINT)
                .then()
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200);

        AuthResponseDto auth = response.as(AuthResponseDto.class);
        Assert.assertNotNull(auth.getToken(), "Token should not be null");

        System.out.println(" Login successful. Token: " + auth.getToken());
        System.out.println("**************************************************");
    }

    // Повторная проверка accessToken (альтернатива)
    @Test
    public void loginWithTokenCheck() {
        AuthRequestDto user = AuthRequestDto.builder()
                .username("printuser3@gmail.com")
                .password("Password@3")
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post(LOGIN_ENDPOINT)
                .then()
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200);
        AuthResponseDto auth = response.as(AuthResponseDto.class);
        Assert.assertNotNull(auth.getToken(), "Token should not be null");
        System.out.println("Token is present: " + auth.getToken());
        System.out.println("**************************************************");
    }

    // Неуспешный логин: неправильный пароль — ожидаем 401 и сообщение
    @Test
    public void loginWithInvalidPasswordShouldFail() {
        AuthRequestDto user = AuthRequestDto.builder()
                .username("printuser3@gmail.com")
                .password("WrongPassword")
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post(LOGIN_ENDPOINT)
                .then()
                .extract().response();

        int status = response.statusCode();
        Assert.assertEquals(status, 401, "Expected 401 Unauthorized");

        String body = response.getBody().asString();
        if (!body.isBlank() && body.trim().startsWith("{")) {
            ErrorDto error = response.as(ErrorDto.class);
            Assert.assertEquals(error.getStatus(), 401);
            Assert.assertTrue(error.getMessage().toString().contains("Login or Password incorrect"));
        } else {
            Assert.fail("Expected JSON error body but got something else");
            System.out.println("**************************************************");
        }

    }

    // Проверка на наличие accessToken в ответе
    @Test
    public void loginResponseShouldContainAccessToken() {
        AuthRequestDto user = AuthRequestDto.builder()
                .username("printuser3@gmail.com")
                .password("Password@3")
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post(LOGIN_ENDPOINT)
                .then()
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200);
        AuthResponseDto auth = response.as(AuthResponseDto.class);
        Assert.assertNotNull(auth.getToken(), "Access token must be present in response");
        System.out.println("Token is present: " + auth.getToken());
        System.out.println("**************************************************");
    }
}