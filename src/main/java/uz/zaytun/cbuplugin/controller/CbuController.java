package uz.zaytun.cbuplugin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.service.CbuService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CbuController {

    private final CbuService cbuService;

    @GetMapping("/currency")
    public ResponseEntity<BaseResponse<List<CurrencyDTO>>> getCurrencies() {
        var cbuResponse = cbuService.getCurrencies();
        return switch (cbuResponse.getMessage()) {
            case SUCCESS -> ResponseEntity.ok(cbuResponse);
            case CLIENT_ERROR -> ResponseEntity.badRequest().body(cbuResponse);
            case SERVER_ERROR -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(cbuResponse);
            case CONNECTION_TIMEOUT_ERROR, READ_TIMEOUT_ERROR ->
                    ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(cbuResponse);
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cbuResponse);
        };
    }
}
