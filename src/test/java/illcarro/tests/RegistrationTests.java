package illcarro.tests;

import com.google.gson.Gson;
import illcaro.dto.AuthRequestDto;
import illcarro.tests.data.RegisterDataProvider;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.notNullValue;

public class RegistrationTests extends TestBase {
    private final Gson gson = new Gson();

    //  Тест 1: Печать JSON-ответа в виде строки
    @Test
    public void printRegistration1() {
        String randomEmail = "user" + System.currentTimeMillis() + "@gmail.com";

        AuthRequestDto user = AuthRequestDto.builder()
                .username(randomEmail)
                .password("Password@3")
                .firstName("Irina")
                .lastName("Tishman")
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/user/registration/usernamepassword")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("===== printRegistration1 =====");
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Email: " + randomEmail);
        System.out.println("JSON: " + response.asString());
        System.out.println("=================================");
    }

    //  Тест 2: Печать форматированного JSON-ответа
    @Test
    public void printRegistration2() {
        String randomEmail = "user" + System.currentTimeMillis() + "@gmail.com";

        AuthRequestDto user = AuthRequestDto.builder()
                .username(randomEmail)
                .password("Passwor5d@2")
                .firstName("Print")
                .lastName("Test")
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/user/registration/usernamepassword")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("===== printRegistration2 =====");
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Email: " + randomEmail);
        response.prettyPrint();
        System.out.println("=================================");
    }

    @Test(dataProvider = "randomUsers", dataProviderClass = RegisterDataProvider.class)
    public void registerRandomUser(AuthRequestDto user) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/user/registration/usernamepassword")
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .extract().response();

        // Вывод нужной информации
        System.out.println("********************************");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("accessToken: " + response.jsonPath().getString("accessToken"));
        System.out.println("********************************");
        System.out.println("RAW Java object: " + user);
        System.out.println("********************************");
        System.out.println("JSON response: " + response.asString());
        System.out.println("********************************");
        System.out.println("Parsed JSON via GSON:");
        System.out.println(gson.toJson(user));
        System.out.println("********************************");
        System.out.printf("Name: %s\nEmail: %s\nPassword: %s\n---------------\n",
                user.getFirstName(), user.getUsername(), user.getPassword());
    }
}