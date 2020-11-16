package at.blo0dy.springCovidMWS.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "bundesland_stat")
@AllArgsConstructor
@NoArgsConstructor
public class BundeslandStat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "datum")
  @Temporal(TemporalType.DATE)
  private Date datum;
  @Column(name = "bundesland")
  private String bundesland;
  @Column(name = "anzahl_einwohner")
  private int anzahlEinwohner;
  @Column(name = "anzahl_neue_faelle")
  private int anzahlNeueFaelle;
  @Column(name = "anzahl_faelle_gesamt")
  private int anzahlFaelleGesamt;
  @Column(name = "anzahl_faelle_7tage")
  private int anzahlFaelle7Tage;
  @Column(name = "inzidenz_7tage")
  private BigDecimal inzidenz7Tage;
  @Column(name = "anzahl_geheilt")
  private int anzahlGeheilt;
  @Column(name = "anzahl_geheilt_gesamt")
  private int anzahlGeheiltSum;

  public BundeslandStat(Date datum, String bundesland, int anzahlEinwohner, int anzahlNeueFaelle,
                        int anzahlFaelleGesamt, int anzahlFaelle7Tage, BigDecimal inzidenz7Tage,
                        int anzahlGeheilt, int anzahlGeheiltSum) {
    this.datum = datum;
    this.bundesland = bundesland;
    this.anzahlEinwohner = anzahlEinwohner;
    this.anzahlNeueFaelle = anzahlNeueFaelle;
    this.anzahlFaelleGesamt = anzahlFaelleGesamt;
    this.anzahlFaelle7Tage = anzahlFaelle7Tage;
    this.inzidenz7Tage = inzidenz7Tage;
    this.anzahlGeheilt = anzahlGeheilt;
    this.anzahlGeheiltSum = anzahlGeheiltSum;
  }

}
