package uz.zaytun.cbuplugin.domain.enumuration;

import lombok.Getter;

@Getter
public enum CbuErrors {

    SUCCESS(1, "Success", "Успешно", "Muvaffaqiyatli"),
    CLIENT_ERROR(2, "Client error", "", ""),
    SERVER_ERROR(3, "Server error", "", ""),
    UNKNOWN_ERROR(4, "Unknown error", "", ""),
    NULL_RESPONSE_ERROR(5, "Null response error", "", ""),
    NULL_BODY_ERROR(6, "Null body error", "", "");

    private final Integer code;
    private final String messageEn;
    private final String messageRu;
    private final String messageUz;

    CbuErrors(Integer code, String messageEn, String messageRu, String messageUz) {
        this.code = code;
        this.messageEn = messageEn;
        this.messageRu = messageRu;
        this.messageUz = messageUz;
    }
}
