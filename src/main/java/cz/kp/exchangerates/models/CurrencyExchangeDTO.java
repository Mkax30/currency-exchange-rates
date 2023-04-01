package cz.kp.exchangerates.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class CurrencyExchangeDTO {

    private String currencyIn;
    private String currencyOut;
    private Double amountIn;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "d.MM.yyyy")
    private LocalDate date;
    private Double finalAmount;

    public CurrencyExchangeDTO() {
    }

    public CurrencyExchangeDTO(String currencyIn, String currencyOut, Double amountIn, LocalDate date, Double finalAmount) {
        this.currencyIn = currencyIn;
        this.currencyOut = currencyOut;
        this.amountIn = amountIn;
        this.date = date;
        this.finalAmount = finalAmount;
    }

    public String getCurrencyIn() {
        return currencyIn;
    }

    public void setCurrencyIn(String currencyIn) {
        this.currencyIn = currencyIn;
    }

    public String getCurrencyOut() {
        return currencyOut;
    }

    public void setCurrencyOut(String currencyOut) {
        this.currencyOut = currencyOut;
    }

    public Double getAmountIn() {
        return amountIn;
    }

    public void setAmountIn(Double amountIn) {
        this.amountIn = amountIn;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(Double finalAmount) {
        this.finalAmount = finalAmount;
    }

    @Override
    public String toString() {
        return "CurrencyExchangeDTO{" +
                "currencyIn='" + currencyIn + '\'' +
                ", currencyOut='" + currencyOut + '\'' +
                ", amountIn=" + amountIn +
                ", date=" + date +
                ", finalAmount=" + finalAmount +
                '}';
    }
}
