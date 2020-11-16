package at.blo0dy.springCovidMWS.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Data
@Entity
@Table(name = "krankenhaus_stat")
@AllArgsConstructor
@NoArgsConstructor
public class KrankenhausStat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "datum")
  @Temporal(TemporalType.DATE)
  private Date datum;
  @Column(name = "bundesland")
  private String bundesland;
  @Column(name = "tests_gesamt")
  private int testsGesamt;
  @Column(name = "fz_hosp")
  private int fzHosp;
  @Column(name = "fz_icu")
  private int fzIcu;
  @Column(name = "fz_hosp_free")
  private int fzHospFree;
  @Column(name = "fz_icu_free")
  private int fzIcuFree;

  public static final Path FILEPATH = Paths.get("H:/covidApp/CovidFallzahlen.csv");
  public static final Path FILENAME = FILEPATH.getFileName();
  public static final String FETCHURL= "https://covid19-dashboard.ages.at/data/" + FILENAME;

  public KrankenhausStat(Date datum, String bundesland, int testsGesamt, int fzHosp, int fzIcu, int fzHospFree, int fzIcuFree) {
    this.datum = datum;
    this.bundesland = bundesland;
    this.testsGesamt = testsGesamt;
    this.fzHosp = fzHosp;
    this.fzIcu = fzIcu;
    this.fzHospFree = fzHospFree;
    this.fzIcuFree = fzIcuFree;
  }
}
