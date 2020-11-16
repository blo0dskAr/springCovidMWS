package at.blo0dy.springCovidMWS.service;

import at.blo0dy.springCovidMWS.model.BundeslandStat;
import at.blo0dy.springCovidMWS.repository.BundeslandStatRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BundesLandStatServiceImpl implements BundeslandStatService, Initializable {

  BundeslandStatRepository bundeslandStatRepository;

  @Autowired
  public BundesLandStatServiceImpl(BundeslandStatRepository bundeslandStatRepository) {
    this.bundeslandStatRepository = bundeslandStatRepository;
  }


  @Override
  public void initializeCSV(String filePath) {

    DateTimeFormatter formatterwithTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    final String[] headers = { "Time",	"Bundesland",	"BundeslandID",	"AnzEinwohner",	"AnzahlFaelle",	"AnzahlFaelleSum",
            "AnzahlFaelle7Tage",	"SiebenTageInzidenzFaelle",	"AnzahlTotTaeglich",	"AnzahlTotSum",
            "AnzahlGeheiltTaeglich",	"AnzahlGeheiltSum" };

    List<BundeslandStat> statList = new ArrayList<>();
    Date datum = new Date();

    log.debug("BundeslandStat-CSV wird verarbeitet");

    try {
      Reader reader = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
      Iterable<CSVRecord> records = CSVFormat.newFormat(';')
              .withHeader(headers)
              .withFirstRecordAsHeader()
              .parse(reader);
      for (CSVRecord record : records) {
        try {
          datum = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(record.get("Time"));
        } catch (ParseException e) {
          log.warn("ParseException caught:");
          log.error(e.getMessage());
        }
        String bundesland = record.get("Bundesland");
        int anzahlEinwohner = Integer.parseInt(record.get("AnzEinwohner"));
        int anzahlNeueFaelle = Integer.parseInt(record.get("AnzahlFaelle"));
        int anzahlFaelleGesamt = Integer.parseInt(record.get("AnzahlFaelleSum"));
        int anzahlFaelle7Tage = Integer.parseInt(record.get("AnzahlFaelle7Tage"));
        BigDecimal inzidenz7Tage = BigDecimal.valueOf(Double.valueOf(record.get("SiebenTageInzidenzFaelle").replaceAll(",", ".")));
        int anzahlGeheilt = Integer.parseInt(record.get("AnzahlGeheiltTaeglich"));
        int anzahlGeheiltSum = Integer.parseInt(record.get("AnzahlGeheiltSum"));

        statList.add(new BundeslandStat(datum, bundesland, anzahlEinwohner, anzahlNeueFaelle, anzahlFaelleGesamt,
                anzahlFaelle7Tage, inzidenz7Tage, anzahlGeheilt, anzahlGeheiltSum));
      }
    } catch (IOException e) {
      log.warn("IOException caught:");
      log.error(e.getMessage());
    }
    log.debug("CSV geparsed. " + statList.size() + " Datensaetze geladen. Persistiere");

    Date latestSavedDate = findLatestSavedDatum();
    if (latestSavedDate == null) {
      log.debug("Keine BundeslandStat-Datensaetze in DB gefunden. Speichere alle...");
    } else {
      log.debug("Aktuellster BundeslandStat-Datensatz in DB hat Datum: " + latestSavedDate.toString() + ". CSV wird gefiltert..");
      statList.removeIf(bundeslandStat -> bundeslandStat.getDatum().before(latestSavedDate) || bundeslandStat.getDatum().equals(latestSavedDate));
      log.debug(statList.size() + " Datensätze verbleiben zum peristieren");
      // save wo datum groesser is
    }
    statList.forEach(bundeslandStat -> bundeslandStatRepository.save(bundeslandStat));
    log.debug("BundeslandStat-CSV-Verarbeitung abgeschlossen.");
  }

  public Date findLatestSavedDatum() {
    return bundeslandStatRepository.findLatestSavedDatum();
  }



}
