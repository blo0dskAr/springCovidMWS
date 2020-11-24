package at.blo0dy.springCovidMWS.repository;

import at.blo0dy.springCovidMWS.model.KrankenhausStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface KrankenhausStatRepository extends JpaRepository<KrankenhausStat, Long> {

  @Query(value = "select max(bls.datum) from krankenhaus_stat bls",
          nativeQuery = true)
  Date findLatestSavedDatum();

  @Query(value = "select * from krankenhaus_stat gs ;", nativeQuery = true)
  List<KrankenhausStat> findKrankenhausStatData();


  @Query(value ="select * from krankenhaus_stat gs " +
          " where gs.bundesland = ?1 ; ", nativeQuery = true)
  List<KrankenhausStat> findKrankenhausStatDataByBundesland(String bundesland);

}
