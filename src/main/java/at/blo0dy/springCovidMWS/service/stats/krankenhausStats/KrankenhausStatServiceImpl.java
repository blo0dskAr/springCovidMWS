package at.blo0dy.springCovidMWS.service.stats.krankenhausStats;

import at.blo0dy.springCovidMWS.model.GesamtStat;
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
public class KrankenhausStatServiceImpl implements StatService, KrankenhausStatService {

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

    int testsGesamt, fzHosp, fzIcu, fzHospFree, fzIcuFree;
    int diffTests = 0;
    int diffFzHosp = 0;
    int diffFzIcu = 0;
    int hospGesamt = 0;
    int icuGesamt = 0;

    KrankenhausStat oldKrankenhausStat;

    DateTimeFormatter formatterwithTime = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    final String[] headers = {"Meldedat","TestGesamt", "MeldeDatum", "FZHosp", "FZICU",	"FZHospFree",	"FZICUFree", "BundeslandID", "Bundesland"};

    List<KrankenhausStat> statList = new ArrayList<>();
    Date datum = null;

    try {
      Iterable<CSVRecord> records = FileUtils.readSemikolonSeparatedCSV(filePath,headers);

      for (CSVRecord record: records) {
        try {
          datum = new SimpleDateFormat("dd.MM.yyyy").parse(record.get("Meldedat"));
        } catch (ParseException e) {
          log.warn("ParseException caught:");
          log.error(e.getMessage());
        }
        testsGesamt = Integer.parseInt(record.get("TestGesamt"));
        fzHosp = Integer.parseInt(record.get("FZHosp"));
        fzIcu = Integer.parseInt(record.get("FZICU"));
        fzHospFree = Integer.parseInt(record.get("FZHospFree"));
        fzIcuFree = Integer.parseInt(record.get("FZICUFree"));
        String bundesland = record.get("Bundesland").toLowerCase();
        if (bundesland.equals("alle")) {
          bundesland = "österreich";
        }

        try {
          oldKrankenhausStat = findLastOccurenceByBundesland(statList, bundesland);
          diffTests = testsGesamt - oldKrankenhausStat.getTestsGesamt();
          // teils korrekturen bei den Tests: Führt zu Minus Ergebnissen (meist wenn nachgeliefert)
          if (diffTests < 0) {
            diffTests = 0;
          }
          diffFzHosp = fzHosp - oldKrankenhausStat.getFzHosp();
          diffFzIcu = fzIcu - oldKrankenhausStat.getFzIcu();
          hospGesamt = fzHosp + fzHospFree;
          icuGesamt = fzIcu + fzIcuFree;
        } catch (NullPointerException e) {
          log.info("Noch keine Datensätze für Bundesland(" + bundesland + ") gefunden. Default=0 wird verwendet");
        }

        statList.add(new KrankenhausStat(datum, bundesland, testsGesamt, fzHosp, fzIcu, fzHospFree, fzIcuFree, diffTests, diffFzHosp, diffFzIcu, hospGesamt, icuGesamt ));
      }
    } catch (IOException e) {
      log.warn("IOException caught:");
      log.error(e.getMessage());
    }
    log.debug("CSV geparsed. " + statList.size() + " Datensaetze geladen. Persistiere");

    // Historische Daten aendern sich zu oft, zahlt sich ned aus
//    Date latestSavedDate = findLatestSavedDatum();
//    if (latestSavedDate == null) {
//      log.debug("Keine KrankenhausStat-Datensaetze in DB gefunden. Speichere alle...");
//    } else {
//      log.debug("Aktuellster KrankenhausStat-Datensatz in DB hat Datum: " + latestSavedDate.toString() + ". CSV wird gefiltert..");
//      statList.removeIf(gesamtStat -> gesamtStat.getDatum().before(latestSavedDate) || gesamtStat.getDatum().equals(latestSavedDate));
//      log.debug(statList.size() + " Datensätze verbleiben zum peristieren");
//    }
    krankenhausStatRepository.deleteAll();
    statList.forEach(gesamtStat -> krankenhausStatRepository.save(gesamtStat));
    log.debug("KrankenhausStat-CSV-Verarbeitung abgeschlossen.");

  }

  public KrankenhausStat findLastOccurenceByBundesland(List<KrankenhausStat> statList, String bundesland) {
    for ( int i = statList.size() - 1;  i >= 0; i-- ) {
      if (statList.get(i).getBundesland().equals(bundesland)) {
        return statList.get(i);
      }
    }
    return null;
  }

  @Override
  public List<KrankenhausStat> findKrankenhausStatData() {
    return krankenhausStatRepository.findKrankenhausStatData();
  }

  @Override
  public List<KrankenhausStat> findKrankenhausStatDataByBundesland(String bundesland) {
    return krankenhausStatRepository.findKrankenhausStatDataByBundesland(bundesland);
  }

  @Override
  public List<KrankenhausStat> findLatestKrankenhausStatDataForBundeslaender() {
    return krankenhausStatRepository.findLatestKrankenhausStatDataForBundeslaender() ;
  }
}
