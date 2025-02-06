package uz.zaytun.cbuplugin.utils;

import org.springframework.http.ResponseEntity;
import uz.zaytun.cbuplugin.dto.BaseResponse;

public class ResponseValidator {

    public static BaseResponse<?> validate(ResponseEntity<?> response) {
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return new BaseResponse<>(response.getBody());
        }
        if (response.getStatusCode().is4xxClientError()) {
            return new BaseResponse<>(false, Message.CLIENT_ERROR);
        }
        if (response.getStatusCode().is5xxServerError()) {
            return new BaseResponse<>(false, Message.SERVER_ERROR);
        }
        return new BaseResponse<>(false, Message.UNKNOWN_ERROR);
    }
}
