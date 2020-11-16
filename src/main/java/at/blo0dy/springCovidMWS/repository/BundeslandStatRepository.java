package at.blo0dy.springCovidMWS.repository;

import at.blo0dy.springCovidMWS.model.BundeslandStat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BundeslandStatRepository extends CrudRepository<BundeslandStat, Long> {

  @Query(value = "select max(bls.datum) from bundesland_stat bls",
         nativeQuery = true)
  Date findLatestSavedDatum();

}
