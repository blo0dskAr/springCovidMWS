package at.blo0dy.springCovidMWS.service.stats.krankenhausStats;

import at.blo0dy.springCovidMWS.model.KrankenhausStat;
import at.blo0dy.springCovidMWS.repository.KrankenhausStatRepository;
import at.blo0dy.springCovidMWS.service.stats.StatService;
import at.blo0dy.springCovidMWS.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("krankenhausStatService")
public class KrankenhausStatServiceImpl implements StatService {

  KrankenhausStatRepository krankenhausStatRepository;

  @Autowired
  public KrankenhausStatServiceImpl(KrankenhausStatRepository krankenhausStatRepository) {
    this.krankenhausStatRepository = krankenhausStatRepository;
  }


  @Override
  public Date findLatestSavedDatum() {
    return krankenhausStatRepository.findLatestSavedDatum();
  }

  @Override
  public void initializeCSV(Path filePath) {

    DateTimeFormatter formatterwithTime = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    final String[] headers = {"Meldedat","TestGesamt", "MeldeDatum", "FZHosp", "FZICU",	"FZHospFree",	"FZICUFree", "BundeslandID", "Bundesland"};

    List<KrankenhausStat> statList = new ArrayList<>();
    Date datum = new Date();

    try {
      Iterable<CSVRecord> records = FileUtils.readSemikolonSeparatedCSV(filePath,headers);

      for (CSVRecord record: records) {
        try {
          datum = new SimpleDateFormat("dd.MM.yyyy").parse(record.get("Meldedat"));
        } catch (ParseException e) {
          log.warn("ParseException caught:");
          log.error(e.getMessage());
        }
        int testsGesamt = Integer.parseInt(record.get("TestGesamt"));
        int fzHosp = Integer.parseInt(record.get("FZHosp"));
        int fzIcu = Integer.parseInt(record.get("FZICU"));
        int fzHospFree = Integer.parseInt(record.get("FZHospFree"));
        int fzIcuFree = Integer.parseInt(record.get("FZICUFree"));
        String bundesland = record.get("Bundesland");

        statList.add(new KrankenhausStat(datum, bundesland, testsGesamt, fzHosp, fzIcu, fzHospFree, fzIcuFree ));
      }
    } catch (IOException e) {
      log.warn("IOException caught:");
      log.error(e.getMessage());
    }
    log.debug("CSV geparsed. " + statList.size() + " Datensaetze geladen. Persistiere");

    Date latestSavedDate = findLatestSavedDatum();
    if (latestSavedDate == null) {
      log.debug("Keine KrankenhausStat-Datensaetze in DB gefunden. Speichere alle...");
    } else {
      log.debug("Aktuellster KrankenhausStat-Datensatz in DB hat Datum: " + latestSavedDate.toString() + ". CSV wird gefiltert..");
      statList.removeIf(gesamtStat -> gesamtStat.getDatum().before(latestSavedDate) || gesamtStat.getDatum().equals(latestSavedDate));
      log.debug(statList.size() + " Datensätze verbleiben zum peristieren");
    }
    statList.forEach(gesamtStat -> krankenhausStatRepository.save(gesamtStat));
    log.debug("KrankenhausStat-CSV-Verarbeitung abgeschlossen.");

  }
}
