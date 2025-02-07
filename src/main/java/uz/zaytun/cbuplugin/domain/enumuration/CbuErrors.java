package uz.zaytun.cbuplugin.domain.enumuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum CbuErrors {

    SUCCESS(1, new Message("Success", "Успешно", "Muvaffaqiyatli")),
    CLIENT_ERROR(2, new Message("Client error", "", "")),
    SERVER_ERROR(3, new Message("Server error", "", "")),
    UNKNOWN_ERROR(4, new Message("Unknown error", "", "")),
    CONNECTION_TIMEOUT_ERROR(5, new Message("Connection timeout error", "", "")),
    READ_TIMEOUT_ERROR(6, new Message("Read timeout error", "", "")),
    NULL_RESPONSE_ERROR(7, new Message("Null response error", "", "")),
    NULL_BODY_ERROR(8, new Message("Null body error", "", ""));

    private final Integer code;
    private final Message message;

    CbuErrors(Integer code, Message message) {
        this.code = code;
        this.message = message;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Message {
        String messageEn;
        String messageRu;
        String messageUz;
    }
}
