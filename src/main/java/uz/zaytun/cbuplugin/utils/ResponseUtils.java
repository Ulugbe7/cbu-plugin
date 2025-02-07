package uz.zaytun.cbuplugin.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.enumuration.CbuErrors;

@Slf4j
public class ResponseUtils {

    private ResponseUtils() {
    }

    public static <T> BaseResponse<T> validate(ResponseEntity<T> response) {
        if (response == null) {
            return new BaseResponse<>(false, "Response is null", CbuErrors.NULL_RESPONSE_ERROR);
        }

        HttpStatusCode status = response.getStatusCode();
        T body = response.getBody();

        if (body == null) {
            return new BaseResponse<>(false, "Body is null", CbuErrors.NULL_BODY_ERROR);
        }

        if (status.is2xxSuccessful()) {
            return new BaseResponse<>(body);
        }

        if (status.is4xxClientError()) {
            return new BaseResponse<>(false, response.toString(), CbuErrors.CLIENT_ERROR);
        }

        if (status.is5xxServerError()) {
            return new BaseResponse<>(false, response.toString(), CbuErrors.SERVER_ERROR);
        }

        return new BaseResponse<>(false, response.toString(), CbuErrors.UNKNOWN_ERROR);
    }
}
