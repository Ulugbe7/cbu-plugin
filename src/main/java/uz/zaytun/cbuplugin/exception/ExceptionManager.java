package uz.zaytun.cbuplugin.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;

import java.util.List;

@Slf4j
@ControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseResponse<List<CurrencyDTO>>> handleCustomException(CustomException ex) {
        HttpStatus httpStatus = switch (ex.getError()) {
            case CLIENT_ERROR -> HttpStatus.BAD_REQUEST;
            case SERVER_ERROR -> HttpStatus.SERVICE_UNAVAILABLE;
            case CONNECTION_TIMEOUT_ERROR, READ_TIMEOUT_ERROR -> HttpStatus.GATEWAY_TIMEOUT;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
        return ResponseEntity.status(httpStatus)
                .body(new BaseResponse<>(false, ex.getMessage(), ex.getError()));
    }
}
