package uz.zaytun.cbuplugin.controller;

import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<BaseResponse<List<CurrencyDTO>>> getCurrencies(CurrencyDTO request) {
        return ResponseEntity.ok(cbuService.getCurrencies(request));
    }
}
