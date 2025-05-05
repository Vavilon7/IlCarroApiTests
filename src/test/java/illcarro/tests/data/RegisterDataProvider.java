package illcarro.tests.data;

import illcaro.dto.AuthRequestDto;
import org.testng.annotations.DataProvider;

import java.util.Random;

public class RegisterDataProvider {
    // DataProvider с генерацией рандомных email'ов
    @DataProvider(name = "randomUsers")
    public Object[][] provideRandomUsers() {
        Object[][] data = new Object[3][1];
        for (int i = 0; i < 3; i++) {
            String email = "autouser" + System.currentTimeMillis() + new Random().nextInt(1000) + "@mail.com";
            AuthRequestDto user = AuthRequestDto.builder()
                    .username(email)
                    .password("Password@123")
                    .firstName("User" + i)
                    .lastName("Test")
                    .build();
            data[i][0] = user;
        }
        return data;
    }
}
