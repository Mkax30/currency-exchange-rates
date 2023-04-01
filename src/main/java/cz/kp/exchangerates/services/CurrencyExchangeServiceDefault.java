package cz.kp.exchangerates.services;

import cz.kp.exchangerates.models.CurrencyDTO;
import cz.kp.exchangerates.models.CurrencyExchangeDTO;
import cz.kp.exchangerates.models.ExchangeDataDTO;
import cz.kp.exchangerates.repositories.CurrencyExchangeRepository;
import cz.kp.exchangerates.repositories.ExchangeDataRepository;
import cz.kp.exchangerates.repositories.entities.Currency;
import cz.kp.exchangerates.repositories.entities.ExchangeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Service
public class CurrencyExchangeServiceDefault implements CurrencyExchangeService {

    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;
    @Autowired
    private ExchangeDataRepository exchangeDataRepository;

    @Override
    public List<CurrencyDTO> getAllCurrencies() {
        List<Currency> allCurrencies = currencyExchangeRepository.findAll();
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        for (Currency c : allCurrencies) {
            CurrencyDTO dto = new CurrencyDTO(c.getId(), c.getCountry(), c.getName(), c.getUnit(), c.getCode());
            currencyDTOList.add(dto);
        }
        return currencyDTOList;
    }

    @Override
    public List<ExchangeDataDTO> getDataByDate(String date) {
        // convert String to LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);

        List<ExchangeData> dataByDate = exchangeDataRepository.findByDate(localDate);
        List<ExchangeDataDTO> exchangeDataDTOList = new ArrayList<>();
        for (ExchangeData d : dataByDate) {
            ExchangeDataDTO dto = new ExchangeDataDTO(d.getId(), d.getIdCurrency(), d.getCode(), d.getDate(), d.getAmount());
            exchangeDataDTOList.add(dto);
        }
        return exchangeDataDTOList;
    }

    @Override
    public List<ExchangeDataDTO> getDataByCurrency(String currencyCode) {
        // find currency and its id
        Currency byCode = currencyExchangeRepository.findByCode(currencyCode);
        if (byCode == null) {
            return null;
        }
        Long idCurrency = byCode.getId();

        List<ExchangeData> dataByIdCurrency = exchangeDataRepository.findByIdCurrency(idCurrency);
        List<ExchangeDataDTO> exchangeDataDTOList = new ArrayList<>();
        for (ExchangeData d : dataByIdCurrency) {
            ExchangeDataDTO dto = new ExchangeDataDTO(d.getId(), d.getIdCurrency(), currencyCode, d.getDate(), d.getAmount());
            exchangeDataDTOList.add(dto);
        }
        return exchangeDataDTOList;
    }

    @Override
    public void saveOneMonthOfData() {
        for (int i = 0; i <= 31; i++) {
            LocalDate localDate = LocalDate.now().minusDays(i);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yyyy");
            String dateString = localDate.format(formatter);

            getCNBDataByDate(dateString);
        }
    }


    @Override
    public void getCNBDataByDate(String date) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://www.cnb.cz")
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, "charset=UTF-8")
                .defaultUriVariables(Collections.singletonMap("url", "https://www.cnb.cz"))
                .build();

        if (date != null) {
            date = "?date=" + date;
        }

        Mono<String> result = webClient.get()
                .uri("/cs/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.txt" + date)
                .retrieve()
                .bodyToMono(String.class)
                .log();

        // save all data into db
        saveCNBDataByDate(result.block());
    }

    public void saveCNBDataByDate(String inputText) {

        try (Scanner parser = new Scanner(inputText)) {
            // get date from date line
            String dateLine = parser.nextLine();
            String[] parsedDateLine = dateLine.split("\\s");
            String date = parsedDateLine[0];

            // convert String to LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yyyy");
            LocalDate localDate = LocalDate.parse(date, formatter);

            // jump over header line
            parser.nextLine();

            while (parser.hasNextLine()) {
                String nextLine = parser.nextLine();
                String[] strings = nextLine.split("\\|");

                String country = strings[0];
                String name = strings[1];
                String unit = strings[2];
                String code = strings[3];
                String amount = strings[4];

                Currency currency = new Currency(null, country, name, Integer.valueOf(unit), code);

                // check if currency code is already saved
                Currency findByCode = currencyExchangeRepository.findByCode(code);
                if (findByCode == null) {
                    findByCode = currencyExchangeRepository.save(currency);
                }

                ExchangeData exchangeData = exchangeDataRepository.findByIdCurrencyAndDate(findByCode.getId(), localDate);
                if (exchangeData == null) {
                    ExchangeData data = new ExchangeData(null, findByCode.getId(), findByCode.getCode(), localDate, Double.valueOf(amount.replace(',', '.')));
                    exchangeDataRepository.save(data);
                }
            }
        }
    }

    // NOT USED
    @Override
    public CurrencyDTO saveNewCurrency(CurrencyDTO currencyDTO) {
        if (currencyExchangeRepository.findByCode(currencyDTO.getCode()) != null) {
            return null;
        }
        Currency currency = new Currency(currencyDTO.getId(), currencyDTO.getCountry(), currencyDTO.getName(), currencyDTO.getUnit(), currencyDTO.getCode());
        Currency out = currencyExchangeRepository.save(currency);
        return new CurrencyDTO(out.getId(), out.getCountry(), out.getName(), out.getUnit(), out.getCode());
    }

    @Override
    public CurrencyExchangeDTO exchange(CurrencyExchangeDTO dto) {
/*        if (!validateCurrencies(dto.getCurrencyIn(), dto.getCurrencyOut())) {
            return null;
        }*/

        Currency codeIn = currencyExchangeRepository.findByCode(dto.getCurrencyIn());
        Currency codeOut = currencyExchangeRepository.findByCode(dto.getCurrencyOut());

        if (codeIn == null || codeOut == null) {
            return null;
        }

        ExchangeData dataIn = exchangeDataRepository.findByIdCurrencyAndDate(codeIn.getId(), dto.getDate());
        ExchangeData dataOut = exchangeDataRepository.findByIdCurrencyAndDate(codeOut.getId(), dto.getDate());

        double result = (dto.getAmountIn() / codeIn.getUnit()) * dataIn.getAmount();
        result = result / dataOut.getAmount();

        dto.setFinalAmount(result);

        return dto;
    }

/*    private boolean validateCurrencies(String currencyIn, String currencyOut) {
        // check if currencyIn and currencyOut are correct currencies
        List<CurrencyEnum> list = Arrays.asList(CurrencyEnum.values());
        if (!list.contains(CurrencyEnum.valueOf(currencyIn))) {
            return false;
        }
        return list.contains(CurrencyEnum.valueOf(currencyOut));
    }*/

}
