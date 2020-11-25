package at.blo0dy.springCovidMWS.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  @JsonFormat(pattern = "dd.MM.yyyy")
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
  @Column(name = "diff_tests")
  int diffTests;
  @Column(name = "diff_fz_hosp")
  int diffFzHosp;
  @Column(name = "diff_fz_icu")
  int diffFzIcu;
  @Column(name = "diff_hosp_gesamt")
  int hospGesamt;
  @Column(name = "diff_icu_gesamt")
  int icuGesamt ;

  public KrankenhausStat(Date datum, String bundesland, int testsGesamt, int fzHosp, int fzIcu, int fzHospFree, int fzIcuFree, int diffTests, int diffFzHosp, int diffFzIcu, int hospGesamt, int icuGesamt) {
    this.datum = datum;
    this.bundesland = bundesland;
    this.testsGesamt = testsGesamt;
    this.fzHosp = fzHosp;
    this.fzIcu = fzIcu;
    this.fzHospFree = fzHospFree;
    this.fzIcuFree = fzIcuFree;
    this.diffTests = diffTests;
    this.diffFzHosp = diffFzHosp;
    this.diffFzIcu = diffFzIcu;
    this.hospGesamt = hospGesamt;
    this.icuGesamt = icuGesamt;
  }
}
