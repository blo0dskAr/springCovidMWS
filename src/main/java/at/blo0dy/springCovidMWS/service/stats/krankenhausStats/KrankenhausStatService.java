package at.blo0dy.springCovidMWS.service.stats.krankenhausStats;

import at.blo0dy.springCovidMWS.model.KrankenhausStat;

import java.time.LocalDate;
import java.util.List;

public interface KrankenhausStatService {



  KrankenhausStat findLastOccurenceByBundesland(List<KrankenhausStat> statList, String bundesland);


  List<KrankenhausStat> findKrankenhausStatData();

  List<KrankenhausStat> findKrankenhausStatDataByBundesland(String bundesland, LocalDate startDate, LocalDate endDate);

  List<KrankenhausStat> findLatestKrankenhausStatDataForBundeslaender();
}
