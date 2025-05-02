package illcaro.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class ErrorDto {
    private String timestamp;
    private int status;
    private String error;
    private Object message;
    private String path;
}

