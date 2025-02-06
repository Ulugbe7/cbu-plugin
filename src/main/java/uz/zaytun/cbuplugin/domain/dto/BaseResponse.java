package uz.zaytun.cbuplugin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.zaytun.cbuplugin.domain.enumuration.CbuErrors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private CbuErrors message;
    private Boolean success;
    private T data;

    public BaseResponse(T data) {
        this.success = true;
        this.message = CbuErrors.SUCCESS;
        this.data = data;
    }

    public BaseResponse(Boolean success, CbuErrors message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }
}
