package cz.kp.exchangerates.models;

import jakarta.persistence.*;

import java.time.LocalDate;

public class ExchangeDataDTO {

    private Long id;
    private Long idCurrency;
    private String code;
    private LocalDate date;
    private Double amount;

    public ExchangeDataDTO() {
    }

    public ExchangeDataDTO(Long id, Long idCurrency, String code, LocalDate date, Double amount) {
        this.id = id;
        this.idCurrency = idCurrency;
        this.code = code;
        this.date = date;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCurrency() {
        return idCurrency;
    }

    public void setIdCurrency(Long idCurrency) {
        this.idCurrency = idCurrency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ExchangeDataDTO{" +
                "id=" + id +
                ", idCurrency=" + idCurrency +
                ", code='" + code + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }
}
