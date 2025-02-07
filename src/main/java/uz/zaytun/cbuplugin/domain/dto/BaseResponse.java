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
    private CbuErrors error;
    private String description;
    private Boolean success;
    private T data;

    public BaseResponse(T data) {
        this.success = true;
        this.description = "Success";
        this.error = CbuErrors.SUCCESS;
        this.data = data;
    }

    public BaseResponse(Boolean success, String description, CbuErrors error) {
        this.success = success;
        this.description = description;
        this.error = error;
        this.data = null;
    }
}
