package at.blo0dy.springCovidMWS.repository;

import at.blo0dy.springCovidMWS.model.KrankenhausStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface KrankenhausStatRepository extends JpaRepository<KrankenhausStat, Long> {

  @Query(value = "select max(ks.datum) from krankenhaus_stat ks",
          nativeQuery = true)
  Date findLatestSavedDatum();

  @Query(value = "select * from krankenhaus_stat ks ;", nativeQuery = true)
  List<KrankenhausStat> findKrankenhausStatData();


  @Query(value ="select * from krankenhaus_stat ks " +
          " where ks.bundesland = ?1 " +
          "   and ks.datum between ?2 " +
          "                    and ?3 ;", nativeQuery = true)
  List<KrankenhausStat> findKrankenhausStatDataByBundesland(String bundesland, LocalDate startDate, LocalDate endDate);



  @Query(value = "select * from krankenhaus_stat ks " +
          " where ks.datum = (select max(sub.datum) from krankenhaus_stat sub) " +
          "   and ks.bundesland != 'österreich' ;", nativeQuery = true)
  List<KrankenhausStat> findLatestKrankenhausStatDataForBundeslaender();
}
