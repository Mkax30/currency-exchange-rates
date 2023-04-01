package cz.kp.exchangerates.models;

public class CurrencyDTO {

    private Long id;
    private String country;
    private String name;
    private Integer unit;
    private String code;

    public CurrencyDTO() {
    }

    public CurrencyDTO(Long id, String country, String name, Integer unit, String code) {
        this.id = id;
        this.country = country;
        this.name = name;
        this.unit = unit;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "CurrencyDTO{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", name='" + name + '\'' +
                ", unit=" + unit +
                ", code='" + code + '\'' +
                '}';
    }
}
