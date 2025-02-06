package uz.zaytun.cbuplugin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.zaytun.cbuplugin.utils.Message;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private String message;
    private Boolean success;
    private T data;

    public BaseResponse(T data) {
        this.success = true;
        this.message = Message.SUCCESS;
        this.data = data;
    }

    public BaseResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }
}
