package at.blo0dy.springCovidMWS.service;

import java.nio.file.Path;
import java.util.Date;

public interface StatService {

  Date findLatestSavedDatum();

  void initializeCSV(Path filePath);

}
