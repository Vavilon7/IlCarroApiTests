package illcaro.dto;
import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@ToString
@Builder

public class AuthResponseDto {
    @SerializedName("accessToken")
    private String token;
}
