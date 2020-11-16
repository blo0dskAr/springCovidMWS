package at.blo0dy.springCovidMWS.repository;

import at.blo0dy.springCovidMWS.model.GesamtStat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface GesamtStatRepository extends CrudRepository<GesamtStat, Long> {

  @Query(value = "select max(bls.datum) from gesamt_stat bls",
         nativeQuery = true)
  Date findLatestSavedDatum();

}
