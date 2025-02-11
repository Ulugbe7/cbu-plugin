package uz.zaytun.cbuplugin.exception;

import lombok.Getter;
import uz.zaytun.cbuplugin.domain.enumuration.CbuErrors;

@Getter
public class CustomException extends RuntimeException {

    private final String message;
    private final CbuErrors error;

    public CustomException(CbuErrors error, String message) {
        super(message);
        this.error = error;
        this.message = message;
    }
}
