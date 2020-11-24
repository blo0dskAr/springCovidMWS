package at.blo0dy.springCovidMWS.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Data
@Entity
@Table(name = "gesamt_stat")
@AllArgsConstructor
@NoArgsConstructor
public class GesamtStat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "datum")
  @Temporal(TemporalType.DATE)
  @JsonFormat(pattern = "dd.MM.yyyy")
  private Date datum;
  @Column(name = "bundesland")
  private String bundesland;
  @Column(name = "anzahl_einwohner")
  private int anzahlEinwohner;
  @Column(name = "anzahl_neue_faelle")
  private int anzahlNeueFaelle;
  @Column(name = "diff_neue_faelle")
  private int diffNeueFaelle;
  @Column(name = "anzahl_faelle_gesamt")
  private int anzahlFaelleGesamt;
  @Column(name = "anzahl_faelle_7tage")
  private int anzahlFaelle7Tage;
  @Column(name = "diff_faelle_7tage")
  private int diffFaelle7Tage;
  @Column(name = "inzidenz_7tage")
  private BigDecimal inzidenz7Tage;
  @Column(name = "diff_inzidenz_7tage")
  private BigDecimal diffInzidenz7Tage;
  @Column(name = "anzahl_neue_tote")
  private int anzahlNeueTote;
  @Column(name = "diff_neue_tote")
  private int diffNeueTote;
  @Column(name = "anzahl_tote_gesamt")
  private int anzahlToteGesamt;
  @Column(name = "anzahl_geheilt")
  private int anzahlGeheilt;
  @Column(name = "diff_geheilt")
  private int diffGeheilt;
  @Column(name = "anzahl_geheilt_gesamt")
  private int anzahlGeheiltGesamt;
  @Column(name = "anzahl_aktive")
  int anzahlAktive;

  public static final Path FILEPATH = Paths.get("H:/covidApp/CovidFaelle_Timeline.csv");
  public static final Path FILENAME = FILEPATH.getFileName();
  public static final String FETCHURL= "https://covid19-dashboard.ages.at/data/" + FILENAME;

  public GesamtStat(Date datum, String bundesland, int anzahlEinwohner, int anzahlNeueFaelle, int diffNeueFaelle, int anzahlFaelleGesamt, int anzahlFaelle7Tage, int diffFaelle7Tage,
                    BigDecimal inzidenz7Tage, BigDecimal diffInzidenz7Tage, int anzahlNeueTote, int diffNeueTote, int anzahlToteGesamt, int anzahlGeheilt, int diffGeheilt, int anzahlGeheiltGesamt, int anzahlAktive) {
    this.datum = datum;
    this.bundesland = bundesland;
    this.anzahlEinwohner = anzahlEinwohner;
    this.anzahlNeueFaelle = anzahlNeueFaelle;
    this.diffNeueFaelle = diffNeueFaelle;
    this.anzahlFaelleGesamt = anzahlFaelleGesamt;
    this.anzahlFaelle7Tage = anzahlFaelle7Tage;
    this.diffFaelle7Tage = diffFaelle7Tage;
    this.inzidenz7Tage = inzidenz7Tage;
    this.diffInzidenz7Tage = diffInzidenz7Tage;
    this.anzahlNeueTote = anzahlNeueTote;
    this.diffNeueTote = diffNeueTote;
    this.anzahlToteGesamt = anzahlToteGesamt;
    this.anzahlGeheilt = anzahlGeheilt;
    this.diffGeheilt = diffGeheilt;
    this.anzahlGeheiltGesamt = anzahlGeheiltGesamt;
    this.anzahlAktive = anzahlAktive;
  }
}
