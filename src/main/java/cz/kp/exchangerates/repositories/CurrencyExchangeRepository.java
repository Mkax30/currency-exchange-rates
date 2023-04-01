package cz.kp.exchangerates.repositories;

import cz.kp.exchangerates.repositories.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyExchangeRepository extends JpaRepository<Currency, Long> {

    Currency findByCode(@Param("code") String code);

}
