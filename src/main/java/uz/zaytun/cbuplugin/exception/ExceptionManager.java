package uz.zaytun.cbuplugin.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;

@Slf4j
@ControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(CustomException.class)
    public <T> ResponseEntity<BaseResponse<T>> handleCustomException(CustomException ex) {
        return ResponseEntity.badRequest()
                .body(new BaseResponse<>(false, ex.getMessage(), ex.getError()));
    }
}
