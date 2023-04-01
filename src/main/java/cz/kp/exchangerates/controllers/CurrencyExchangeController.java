package cz.kp.exchangerates.controllers;

import cz.kp.exchangerates.models.CurrencyDTO;
import cz.kp.exchangerates.models.CurrencyExchangeDTO;
import cz.kp.exchangerates.models.ExchangeDataDTO;
import cz.kp.exchangerates.services.CurrencyExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8071")
@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyExchangeController {

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    @GetMapping
    public ResponseEntity<List<CurrencyDTO>> getAllCurrencies() {
        List<CurrencyDTO> c = new ArrayList<>(currencyExchangeService.getAllCurrencies());
        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @GetMapping("/dataByDate")
    public ResponseEntity<List<ExchangeDataDTO>> getDataByDate(@RequestParam String date) {
        List<ExchangeDataDTO> c = new ArrayList<>(currencyExchangeService.getDataByDate(date));
        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @GetMapping("/dataByCurrencyCode")
    public ResponseEntity<List<ExchangeDataDTO>> getDataByCurrency(@RequestParam String currencyCode) {
        List<ExchangeDataDTO> c = new ArrayList<>(currencyExchangeService.getDataByCurrency(currencyCode));
        return new ResponseEntity<>(c, HttpStatus.OK);
    }

/*    @PostMapping
    public ResponseEntity<CurrencyDTO> saveNewCurrency(@RequestBody CurrencyDTO currencyDTO) {
        CurrencyDTO c = currencyExchangeService.saveNewCurrency(currencyDTO);
        if (c == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(c, HttpStatus.CREATED);
    }*/

    @GetMapping("/exchange")
    public ResponseEntity<CurrencyExchangeDTO> exchange(@RequestBody CurrencyExchangeDTO currencyExchangeDTO) {
        CurrencyExchangeDTO e = currencyExchangeService.exchange(currencyExchangeDTO);
        if (e == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(e, HttpStatus.OK);
    }

    @PutMapping("/data/import")
    public ResponseEntity<Mono<String>> getCNBData() {
        currencyExchangeService.saveOneMonthOfData();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
