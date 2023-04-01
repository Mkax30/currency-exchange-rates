package cz.kp.exchangerates.services;

import cz.kp.exchangerates.models.CurrencyDTO;
import cz.kp.exchangerates.models.CurrencyExchangeDTO;
import cz.kp.exchangerates.models.ExchangeDataDTO;

import java.util.List;

public interface CurrencyExchangeService {

    List<CurrencyDTO> getAllCurrencies();

    List<ExchangeDataDTO> getDataByDate(String date);

    List<ExchangeDataDTO> getDataByCurrency(String currencyCode);

    void saveOneMonthOfData();

    void getCNBDataByDate(String date);

    /**
     * Saves new Currency. (NOT USED)
     * @param currencyDTO CurrencyDTO object
     * @return Currency entity object
     */
    CurrencyDTO saveNewCurrency(CurrencyDTO currencyDTO);

    CurrencyExchangeDTO exchange(CurrencyExchangeDTO currencyExchangeDTO);

}
