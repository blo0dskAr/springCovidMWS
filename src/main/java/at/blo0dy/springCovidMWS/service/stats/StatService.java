package at.blo0dy.springCovidMWS.service.stats;

import java.nio.file.Path;
import java.util.Date;
import java.util.Map;

public interface StatService {

  Date findLatestSavedDatum();

  void initializeCSV(Path filePath);
}
