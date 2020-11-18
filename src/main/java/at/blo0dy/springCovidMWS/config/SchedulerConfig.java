package at.blo0dy.springCovidMWS.config;

import at.blo0dy.springCovidMWS.model.GesamtStat;
import at.blo0dy.springCovidMWS.model.KrankenhausStat;
import at.blo0dy.springCovidMWS.service.GesamtStatServiceImpl;
import at.blo0dy.springCovidMWS.service.StatService;
import at.blo0dy.springCovidMWS.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

  @Qualifier("gesamtStatService")
  StatService gesamtStatService;

  @Qualifier("krankenhausStatService")
  StatService krankenhausStatService;

  @Autowired
  public SchedulerConfig(StatService gesamtStatService, StatService krankenhausStatService) {
    this.gesamtStatService = gesamtStatService;
    this.krankenhausStatService = krankenhausStatService;
  }

  @Scheduled(fixedDelay = 1000 * 60 * 60)
  public void runFileHandlingScheduler() {
    //FileUtils.checkAndCreateFolder(GesamtStat.FILEPATH);
    //FileUtils.saveDataFile(GesamtStat.FETCHURL, GesamtStat.FILEPATH);
    gesamtStatService.initializeCSV(GesamtStat.FILEPATH);

    //FileUtils.checkAndCreateFolder(KrankenhausStat.FILEPATH);
    //FileUtils.saveDataFile(KrankenhausStat.FETCHURL, KrankenhausStat.FILEPATH);
    krankenhausStatService.initializeCSV(KrankenhausStat.FILEPATH);
  }
}
