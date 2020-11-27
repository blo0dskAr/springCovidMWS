package at.blo0dy.springCovidMWS.config;

import at.blo0dy.springCovidMWS.service.stats.StatService;
import at.blo0dy.springCovidMWS.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableScheduling
public class SchedulerConfig {

  @Qualifier("gesamtStatService")
  StatService gesamtStatService;

  @Qualifier("krankenhausStatService")
  StatService krankenhausStatService;

  PropertyConfig propertyConfig;

  @Autowired
  public SchedulerConfig(StatService gesamtStatService, StatService krankenhausStatService, PropertyConfig propertyConfig) {
    this.gesamtStatService = gesamtStatService;
    this.krankenhausStatService = krankenhausStatService;
    this.propertyConfig = propertyConfig;
  }

  @Scheduled(fixedDelay = 1000 * 60 * 60)
  public void runFileHandlingScheduler() {

    Path gesamtStatPath = Paths.get(propertyConfig.getGesamtStatfilePath());
    Path krankenhausStatPath = Paths.get(propertyConfig.getKrankenhausStatfilePath());



    FileUtils.checkAndCreateFolder(gesamtStatPath);
    FileUtils.saveDataFile(propertyConfig.getGesamtStatfetchURL(), gesamtStatPath);
    gesamtStatService.initializeCSV(gesamtStatPath);

    FileUtils.checkAndCreateFolder(krankenhausStatPath);
    FileUtils.saveDataFile(propertyConfig.getKrankenhausStatfetchURL(), krankenhausStatPath);
    krankenhausStatService.initializeCSV(krankenhausStatPath);
  }
}
