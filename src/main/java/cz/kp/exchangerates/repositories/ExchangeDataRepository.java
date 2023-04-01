package cz.kp.exchangerates.repositories;

import cz.kp.exchangerates.repositories.entities.ExchangeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeDataRepository extends JpaRepository<ExchangeData, Long> {

    @Query("SELECT e FROM ExchangeData e Where e.idCurrency = ?1 and e.date = ?2")
    ExchangeData findByIdCurrencyAndDate(@Param("idCurrency") Long idCurrency, @Param("date") LocalDate date);

    List<ExchangeData> findByDate(@Param("date") LocalDate date);

    List<ExchangeData> findByIdCurrency(@Param("idCurrency") Long idCurrency);

}
