package at.blo0dy.springCovidMWS.config;

import at.blo0dy.springCovidMWS.service.Initializable;
import at.blo0dy.springCovidMWS.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

  Initializable initializable;

  @Autowired
  public SchedulerConfig(Initializable initializable) {
    this.initializable = initializable;
  }

  @Scheduled(fixedDelay = 1000 * 60)
  public void runFileHandlingScheduler() {
    FileUtils.checkAndCreateFolder();
    String fileToProcess = FileUtils.saveDataFile();
//    FileUtils.unzipFile();
    initializable.initializeCSV(fileToProcess);
  }
}
