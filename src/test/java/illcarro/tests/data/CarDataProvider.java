package illcarro.tests.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import illcaro.dto.CarDto;
import org.testng.annotations.DataProvider;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

public class CarDataProvider {
    @DataProvider(name = "carDataFromJson")
    public static Object[][] provideCarData() throws Exception {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<CarDto>>() {}.getType();
        FileReader reader = new FileReader("C:\\Developmend\\IlCarroApiTests\\src\\test\\resources\\dataCar\\cars.json");
        List<CarDto> cars = gson.fromJson(reader, listType);
        reader.close();

        // Генерация уникального serialNumber для каждой машины
        for (CarDto car : cars) {
            String randomSerial = "CAR-" + UUID.randomUUID();
            car.setSerialNumber(randomSerial);
        }

        Object[][] data = new Object[cars.size()][1];
        for (int i = 0; i < cars.size(); i++) {
            data[i][0] = cars.get(i);
        }
        return data;
    }
}