package at.blo0dy.springCovidMWS.repository;

import at.blo0dy.springCovidMWS.model.GesamtStat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface GesamtStatRepository extends CrudRepository<GesamtStat, Long> {

  @Query(value = "select max(gs.datum) from gesamt_stat gs",
         nativeQuery = true)
  Date findLatestSavedDatum();

  @Query(value = "select gs.datum, gs.anzahl_neue_faelle from gesamt_stat gs" +
                 " where gs.bundesland = ?1 " +
                 " order by gs.datum ; ", nativeQuery = true)
  List<Object[]> findNeueFaelleByBundesland(String bundesland);
}
