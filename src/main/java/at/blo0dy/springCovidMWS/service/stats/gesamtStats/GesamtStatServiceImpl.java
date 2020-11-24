package at.blo0dy.springCovidMWS.service.stats.gesamtStats;

import at.blo0dy.springCovidMWS.model.GesamtStat;
import at.blo0dy.springCovidMWS.repository.GesamtStatRepository;
import at.blo0dy.springCovidMWS.service.stats.StatService;
import at.blo0dy.springCovidMWS.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("gesamtStatService")
@Slf4j
public class GesamtStatServiceImpl implements StatService, GesamtStatService {

  GesamtStatRepository gesamtStatRepository;

  @Autowired
  public GesamtStatServiceImpl(GesamtStatRepository gesamtStatRepository) {
    this.gesamtStatRepository = gesamtStatRepository;
  }


  @Override
  public void initializeCSV(Path filePath) {

    int anzahlEinwohner, anzahlNeueFaelle, anzahlFaelleGesamt, anzahlFaelle7Tage, anzahlNeueTote, anzahlToteGesamt, anzahlGeheilt, anzahlGeheiltGesamt;
    int diffNeueFaelle = 0;
    int diffNeueTote = 0;
    int diffGeheilt  = 0 ;
    int diffFaelle7Tage = 0;
    int anzahl_aktive = 0;
    BigDecimal inzidenz7Tage;
    BigDecimal diffInzidenz7Tage = BigDecimal.ZERO;

    GesamtStat oldGesamtStat;

    DateTimeFormatter formatterwithTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    final String[] headers = { "Time",	"Bundesland",	"BundeslandID",	"AnzEinwohner",	"AnzahlFaelle",	"AnzahlFaelleSum",
            "AnzahlFaelle7Tage",	"SiebenTageInzidenzFaelle",	"AnzahlTotTaeglich",	"AnzahlTotSum",
            "AnzahlGeheiltTaeglich",	"AnzahlGeheiltSum" };

    List<GesamtStat> statList = new ArrayList<>();
    Date datum = null;

    log.debug("GesamtStat-CSV wird verarbeitet");

    try {
      Iterable<CSVRecord> records = FileUtils.readSemikolonSeparatedCSV(filePath, headers);
      for (CSVRecord record : records) {
        try {
//          datum = LocalDate.parse(record.get("Time"));
          datum = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(record.get("Time"));
        } catch (ParseException e) {
          log.warn("ParseException caught:");
          log.error(e.getMessage());
        }
        String bundesland = record.get("Bundesland").toLowerCase();
        anzahlEinwohner = Integer.parseInt(record.get("AnzEinwohner"));
        anzahlNeueFaelle = Integer.parseInt(record.get("AnzahlFaelle"));
        anzahlFaelleGesamt = Integer.parseInt(record.get("AnzahlFaelleSum"));
        anzahlFaelle7Tage = Integer.parseInt(record.get("AnzahlFaelle7Tage"));
        inzidenz7Tage = BigDecimal.valueOf(Double.valueOf(record.get("SiebenTageInzidenzFaelle").replaceAll(",", ".")));
        anzahlNeueTote = Integer.parseInt(record.get("AnzahlTotTaeglich"));
        anzahlToteGesamt = Integer.parseInt(record.get("AnzahlTotSum"));
        anzahlGeheilt = Integer.parseInt(record.get("AnzahlGeheiltTaeglich"));
        anzahlGeheiltGesamt = Integer.parseInt(record.get("AnzahlGeheiltSum"));

        try {
          oldGesamtStat = findLastOccurenceByBundesland(statList, bundesland);
          diffNeueFaelle = anzahlNeueFaelle - oldGesamtStat.getAnzahlNeueFaelle();
          diffFaelle7Tage = anzahlFaelle7Tage - oldGesamtStat.getAnzahlFaelle7Tage();
          diffInzidenz7Tage = inzidenz7Tage.subtract(oldGesamtStat.getInzidenz7Tage());
          diffNeueTote = anzahlNeueTote - oldGesamtStat.getAnzahlNeueTote();
          diffGeheilt = anzahlGeheilt - oldGesamtStat.getAnzahlGeheilt();
          anzahl_aktive = anzahlFaelleGesamt-anzahlGeheiltGesamt-anzahlToteGesamt;
        } catch (NullPointerException e) {
          log.info("Noch keine Datensätze für Bundesland(" + bundesland + ") gefunden. Default=0 wird verwendet");
        }


        statList.add(new GesamtStat(datum, bundesland, anzahlEinwohner, anzahlNeueFaelle, diffNeueFaelle, anzahlFaelleGesamt, anzahlFaelle7Tage, diffFaelle7Tage,
                inzidenz7Tage, diffInzidenz7Tage, anzahlNeueTote, diffNeueTote, anzahlToteGesamt, anzahlGeheilt, diffGeheilt, anzahlGeheiltGesamt, anzahl_aktive));
      }
    } catch (IOException e) {
      log.warn("IOException caught:");
      log.error(e.getMessage());
    }
    log.debug("CSV geparsed. " + statList.size() + " Datensaetze geladen. Persistiere");

    Date latestSavedDate = findLatestSavedDatum();
    if (latestSavedDate == null) {
      log.debug("Keine GesamtStat-Datensaetze in DB gefunden. Speichere alle...");
    } else {
      log.debug("Aktuellster GesamtStat-Datensatz in DB hat Datum: " + latestSavedDate.toString() + ". CSV wird gefiltert..");
      statList.removeIf(gesamtStat -> gesamtStat.getDatum().before(latestSavedDate) || gesamtStat.getDatum().equals(latestSavedDate));
      log.debug(statList.size() + " Datensätze verbleiben zum peristieren");
    }
    statList.forEach(gesamtStat -> gesamtStatRepository.save(gesamtStat));
    log.debug("GesamtStat-CSV-Verarbeitung abgeschlossen.");
  }

  @Override
  public Date findLatestSavedDatum() {
    return gesamtStatRepository.findLatestSavedDatum();
  }

  @Override
  public GesamtStat findLastOccurenceByBundesland(List<GesamtStat> statList, String bundesland) {
    for ( int i = statList.size() - 1;  i >= 0; i-- ) {
      if (statList.get(i).getBundesland().equals(bundesland)) {
        return statList.get(i);
      }
    }
    return null;
  }


  @Override
  public Map<Date, Integer> findNeueFaelleByBundesland(String bundesland) {
    List<Object[]> myList =  gesamtStatRepository.findNeueFaelleByBundesland(bundesland.toLowerCase());

    Map<Date, Integer> data = new TreeMap<Date, Integer>();

    Date datum = null;
    for (Object[] o : myList) {
      try {
        datum = new SimpleDateFormat("yyyy-MM-dd").parse(o[0].toString());
      } catch (ParseException e) {
        log.warn("Parse Exception from dataBase caught (Date)");
        e.getMessage();
      }
      Integer anzahl = Integer.parseInt(o[1].toString());
      if (datum != null) {
        data.put(datum, anzahl);
      }
    }
    return data ;
  }

  @Override
  public List<GesamtStat> findGesamtStatData() {
    return gesamtStatRepository.findGesamtStatData() ;
  }

  @Override
  public List<GesamtStat> findGesamtStatDataByBundesland(String bundesland) {
    return gesamtStatRepository.findGesamtStatDataByBundesland(bundesland);
  }


}
