package at.blo0dy.springCovidMWS.service.stats.gesamtStats;

import at.blo0dy.springCovidMWS.model.GesamtStat;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface GesamtStatService {

   Date findLatestSavedDatum();

  Map<Date, Integer> findNeueFaelleByBundesland(String bundesland);

  List<GesamtStat> findGesamtStatData();

  List<GesamtStat> findGesamtStatDataByBundesland(String bundesland);

  GesamtStat findLastOccurenceByBundesland(List<GesamtStat> statlist, String bundesland);

  List<GesamtStat> findLatestGesamtStatDataForBundeslaender();

}
