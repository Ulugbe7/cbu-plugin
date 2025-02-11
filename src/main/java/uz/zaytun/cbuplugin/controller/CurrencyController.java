package uz.zaytun.cbuplugin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.service.CurrencyFetchService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CurrencyController {

    private final CurrencyFetchService currencyFetchService;

    @GetMapping("/currency")
    public ResponseEntity<BaseResponse<List<CurrencyDTO>>> fetchCurrencies(CurrencyDTO request) {
        log.info("REQUEST fetchCurrencies() with request: {}", request);
        return ResponseEntity.ok(currencyFetchService.fetchCurrencies(request));
    }
}
