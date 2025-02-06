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
            log.debug("Response is null");
            return new BaseResponse<>(false, CbuErrors.NULL_RESPONSE_ERROR);
        }

        HttpStatusCode status = response.getStatusCode();
        T body = response.getBody();

        log.debug("Response status: {}, body: {}", status, body);

        if (body == null) {
            return new BaseResponse<>(false, CbuErrors.NULL_BODY_ERROR);
        }

        if (status.is2xxSuccessful()) {
            return new BaseResponse<>(body);
        }
        if (status.is4xxClientError()) {
            return new BaseResponse<>(false, CbuErrors.CLIENT_ERROR);
        }
        if (status.is5xxServerError()) {
            return new BaseResponse<>(false, CbuErrors.SERVER_ERROR);
        }

        return new BaseResponse<>(false, CbuErrors.UNKNOWN_ERROR);
    }
}
