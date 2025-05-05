package illcarro.tests;

import illcaro.dto.AuthRequestDto;
import illcaro.dto.AuthResponseDto;
import illcaro.dto.ErrorDto;
import illcarro.tests.data.RegisterDataProvider;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class RegistrationTests extends TestBase {
    // Успешная регистрация с выводом токена
    @Test
    public void printRegistration1() {
        String email = "user" + System.currentTimeMillis() + "@gmail.com";

        AuthRequestDto user = AuthRequestDto.builder()
                .username(email)
                .password("Password@3")
                .firstName("Irina")
                .lastName("Tishman")
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/user/registration/usernamepassword")
                .then()
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200);
        AuthResponseDto auth = response.as(AuthResponseDto.class);
        Assert.assertNotNull(auth.getToken());

        System.out.println(" User registered: " + email);
        System.out.println(" Token: " + auth.getToken());
    }

    // Повтор регистрации с другими данными
    @Test
    public void printRegistration2() {
        String email = "user" + System.currentTimeMillis() + "@gmail.com";

        AuthRequestDto user = AuthRequestDto.builder()
                .username(email)
                .password("Password@2")
                .firstName("Print")
                .lastName("Test")
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/user/registration/usernamepassword")
                .then()
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200);
        AuthResponseDto auth = response.as(AuthResponseDto.class);
        Assert.assertNotNull(auth.getToken());

        System.out.println(" Registered: " + email);
        System.out.println(" Token: " + auth.getToken());
    }

    // Параметризованный тест с рандомными пользователями
    @Test(dataProvider = "randomUsers", dataProviderClass = RegisterDataProvider.class)
    public void registerRandomUser(AuthRequestDto user) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/user/registration/usernamepassword")
                .then()
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200);
        AuthResponseDto auth = response.as(AuthResponseDto.class);
        Assert.assertNotNull(auth.getToken());

        System.out.printf(" Registered: %s | Name: %s %s | Token: %s\n",
                user.getUsername(), user.getFirstName(), user.getLastName(), auth.getToken());
    }

    // (дополнительно, по желанию) тест с невалидными данными
    @Test()
    public void registrationWithInvalidDataShouldFail() {
        AuthRequestDto user = AuthRequestDto.builder()
                .username("invalid") // неправильный email
                .password("123")
                .firstName("")
                .lastName("")
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/user/registration/usernamepassword")
                .then()
                .extract().response();

        Assert.assertEquals(response.statusCode(), 400);

        String body = response.getBody().asString();
        if (!body.isBlank() && body.trim().startsWith("{")) {
            ErrorDto error = response.as(ErrorDto.class);
            Assert.assertEquals(error.getStatus(), 400);
            Assert.assertTrue(error.getMessage().toString().contains("username"));
        } else {
            Assert.fail("Expected JSON error body but got something else");
        }
    }
}