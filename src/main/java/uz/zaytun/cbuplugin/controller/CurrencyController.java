package uz.zaytun.cbuplugin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.service.CurrencyFetcherService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CurrencyController {

    private final CurrencyFetcherService currencyFetcherService;

    @GetMapping("/currency")
    public ResponseEntity<BaseResponse<List<CurrencyDTO>>> fetchCurrencies(CurrencyDTO request) {
        return ResponseEntity.ok(currencyFetcherService.fetchCurrencies(request));
    }
}
