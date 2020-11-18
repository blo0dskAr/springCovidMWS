package at.blo0dy.springCovidMWS.service;

import at.blo0dy.springCovidMWS.model.GesamtStat;
import at.blo0dy.springCovidMWS.repository.GesamtStatRepository;
import at.blo0dy.springCovidMWS.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GesamtStatServiceImpl implements StatService {

  GesamtStatRepository gesamtStatRepository;

  @Autowired
  public GesamtStatServiceImpl(GesamtStatRepository gesamtStatRepository) {
    this.gesamtStatRepository = gesamtStatRepository;
  }


  @Override
  public void initializeCSV(Path filePath) {

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
        int anzahlEinwohner = Integer.parseInt(record.get("AnzEinwohner"));
        int anzahlNeueFaelle = Integer.parseInt(record.get("AnzahlFaelle"));
        int anzahlFaelleGesamt = Integer.parseInt(record.get("AnzahlFaelleSum"));
        int anzahlFaelle7Tage = Integer.parseInt(record.get("AnzahlFaelle7Tage"));
        BigDecimal inzidenz7Tage = BigDecimal.valueOf(Double.valueOf(record.get("SiebenTageInzidenzFaelle").replaceAll(",", ".")));
        int anzahlNeueTote = Integer.parseInt(record.get("AnzahlTotTaeglich"));
        int anzahlToteGesamt = Integer.parseInt(record.get("AnzahlTotSum"));
        int anzahlGeheilt = Integer.parseInt(record.get("AnzahlGeheiltTaeglich"));
        int anzahlGeheiltSum = Integer.parseInt(record.get("AnzahlGeheiltSum"));

        statList.add(new GesamtStat(datum, bundesland, anzahlEinwohner, anzahlNeueFaelle, anzahlFaelleGesamt,
                anzahlFaelle7Tage, inzidenz7Tage, anzahlNeueTote, anzahlToteGesamt, anzahlGeheilt, anzahlGeheiltSum));
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

  public Date findLatestSavedDatum() {
    return gesamtStatRepository.findLatestSavedDatum();
  }



  public Map<Date, Integer> findNeueFaelleByBundesland(String bundesland) {
    List<Object[]> myList =  gesamtStatRepository.findNeueFaelleByBundesland(bundesland.toLowerCase());

    Map<Date, Integer> data = new TreeMap<Date, Integer>();

    Date datum = null;
    for (Object[] o : myList) {
      try {
        datum = new SimpleDateFormat("yyyy-MM-dd").parse(o[0].toString());
      } catch (ParseException e) {
        log.warn("Parse Exception from dataBase caught");
        e.getMessage();
      }
      Integer anzahl = Integer.parseInt(o[1].toString());
      if (datum != null) {
        data.put(datum, anzahl);
      }
    }

    log.info(data.toString());
    return data ;
  }


}
