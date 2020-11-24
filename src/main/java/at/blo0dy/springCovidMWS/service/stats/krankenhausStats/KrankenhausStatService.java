package at.blo0dy.springCovidMWS.service.stats.krankenhausStats;

import at.blo0dy.springCovidMWS.model.KrankenhausStat;

import java.util.List;

public interface KrankenhausStatService {



  KrankenhausStat findLastOccurenceByBundesland(List<KrankenhausStat> statList, String bundesland);


}
