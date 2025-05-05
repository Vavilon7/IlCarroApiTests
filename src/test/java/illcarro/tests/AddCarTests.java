package illcarro.tests;

import illcaro.dto.CarDto;
import illcaro.dto.ErrorDto;
import illcaro.dto.MessageDto;
import illcarro.tests.data.CarDataProvider;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;


public class AddCarTests extends TestBase {
    // Успешное добавление машины
    @Test(dataProvider = "carDataFromJson", dataProviderClass = CarDataProvider.class)
    public void addCarTest(CarDto car) {
        System.out.println("===== Sending Car Data =====");
        System.out.println(car);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", TOKEN)
                .body(car)
                .post("/cars")
                .then()
                .extract().response();

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("===== Server Response =====");
        response.prettyPrint();

        Assert.assertEquals(response.statusCode(), 200);

        // Десериализация успешного ответа
        MessageDto msg = response.as(MessageDto.class);
        Assert.assertEquals(msg.getMessage(), "Car added successfully");
    }

    // Без токена: ожидаем 401
    @Test
    public void addCarUnauthorizedTest() {
        Map<String, Object> car = new HashMap<>();
        car.put("serialNumber", "UNAUTH-TEST");
        car.put("brand", "Ford");
        car.put("model", "Focus");
        car.put("year", "2018");
        car.put("fuel", "Hybrid");
        car.put("seats", 5);
        car.put("carClass", "Standard");
        car.put("pricePerDay", 55);
        car.put("city", "Eilat");
        car.put("country", "Israel");
        car.put("manufacture", "Ford");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(car)
                .post("/cars")
                .then()
                .extract().response();

        int status = response.getStatusCode();
        System.out.println("[Unauthorized Test] Status Code: " + status);
        response.prettyPrint();

        Assert.assertTrue(status == 401 || status == 403,
                "Expected 401 or 403, but got: " + status);

        String body = response.getBody().asString();
        System.out.println("Response body: " + body);

        if (!body.isBlank() && body.trim().startsWith("{")) {
            ErrorDto error = response.as(ErrorDto.class);
            Assert.assertNotNull(error.getError(), "Expected 'error' field in JSON");
            Assert.assertNotNull(error.getMessage(), "Expected 'message' field in JSON");
        } else {
            System.out.println("Response body does not contain JSON — possibly empty or HTML. Skipping deserialization.");
        }
    }


    // Отсутствуют обязательные поля: ожидаем 400
    @Test
    public void addCarMissingFieldTest() {
        Map<String, Object> car = Map.of(
                "serialNumber", "MISS-FIELD-TEST",
                "brand", "Mazda",
                "model", "3",
                "year", "2022",
                "fuel", "Petrol",
                "seats", 5,
                "pricePerDay", 60,
                "city", "Nazareth",
                "country", "Israel"
                // manufacture и carClass отсутствуют
        );

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", TOKEN)
                .body(car)
                .post("/cars")
                .then()
                .extract().response();

        System.out.println("[Missing Field Test] Status Code: " + response.getStatusCode());
        response.prettyPrint();

        ErrorDto error = response.as(ErrorDto.class);
        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(error.getStatus(), 400);
        Assert.assertEquals(error.getError(), "Bad Request");
        Assert.assertTrue(error.getMessage().toString().contains("carClass") || error.getMessage().toString().contains("manufacture"));
    }
}