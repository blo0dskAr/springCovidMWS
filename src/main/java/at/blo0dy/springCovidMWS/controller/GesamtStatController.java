package at.blo0dy.springCovidMWS.controller;

import at.blo0dy.springCovidMWS.model.GesamtStat;
import at.blo0dy.springCovidMWS.service.stats.gesamtStats.GesamtStatServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/covid/api/gesamt")
public class GesamtStatController {

  @Qualifier("gesamtStatService")
  GesamtStatServiceImpl gesamtStatService;

  @Autowired
  public GesamtStatController(GesamtStatServiceImpl gesamtStatService) {
    this.gesamtStatService = gesamtStatService;
  }

  @GetMapping("/v1/neueFaelle/{bundesland}")
  public Map<Date, Integer> findNeueFaelleByBundesland(@PathVariable("bundesland") String bundesland) {
    return gesamtStatService.findNeueFaelleByBundesland(bundesland) ;
  }

  @GetMapping("/v1/getData")
  public List<GesamtStat> findGesamtStatData() {
    return gesamtStatService.findGesamtStatData() ;
  }


  @GetMapping("/v1/getData/{bundesland}")
  public List<GesamtStat> findGesamtStatDataByBundesland(@PathVariable("bundesland") String bundesland,
                                                         @RequestParam("startDate") LocalDate startDate,
                                                         @RequestParam("endDate") LocalDate endDate) {
    return gesamtStatService.findGesamtStatDataByBundesland(bundesland, startDate, endDate) ;
  }

  @GetMapping("v1/getLatestDate")
  public Date findLatestDate() {
    return gesamtStatService.findLatestSavedDatum();
  }

  @GetMapping("v1/getLatestDataBundeslaender")
  public List<GesamtStat> findLatestGesamtStatDataForBundeslaender() {
    return gesamtStatService.findLatestGesamtStatDataForBundeslaender();
  }




}
