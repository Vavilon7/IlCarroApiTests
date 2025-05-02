package illcaro.dto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDto {
    private String serialNumber;
    private String manufacture;
    private String model;
    private String year;
    private String fuel;
    private int seats;
    private String carClass;
    private int pricePerDay;
    private String about;
    private String city;
    private String country;
}