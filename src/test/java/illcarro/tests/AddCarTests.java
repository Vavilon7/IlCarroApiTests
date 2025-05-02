package illcarro.tests;

import illcaro.dto.CarDto;
import illcarro.tests.data.CarDataProvider;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;


public class AddCarTests extends TestBase {
    // Основной тест с параметризацией
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
    }

    // Тест: попытка добавить машину без токена (для проверки 401 Unauthorized)
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

        System.out.println("[Unauthorized Test] Status Code: " + response.getStatusCode());
        response.prettyPrint();
    }

    // Тест: добавление машины с некорректным полем (отсутствует required поле)
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
                // отсутствие поля manufacture и carClass
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
    }
}
