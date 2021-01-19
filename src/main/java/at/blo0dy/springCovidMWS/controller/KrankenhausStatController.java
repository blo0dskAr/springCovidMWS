package at.blo0dy.springCovidMWS.controller;

import at.blo0dy.springCovidMWS.model.KrankenhausStat;
import at.blo0dy.springCovidMWS.service.stats.krankenhausStats.KrankenhausStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/covid/api/krankenhaus")
public class KrankenhausStatController {

  @Qualifier("krankenhausStatService")
  KrankenhausStatService krankenhausStatService;

  @Autowired
  public KrankenhausStatController(KrankenhausStatService krankenhausStatService) {
    this.krankenhausStatService = krankenhausStatService;
  }


  @GetMapping("/v1/getData")
  public List<KrankenhausStat> findGesamtStatData() {
    return krankenhausStatService.findKrankenhausStatData() ;
  }


  @GetMapping("/v1/getData/{bundesland}")
  public List<KrankenhausStat> findGesamtStatDataByBundesland(@PathVariable("bundesland") String bundesland,
                                                              @RequestParam("startDate") LocalDate startDate,
                                                              @RequestParam("endDate") LocalDate endDate) {
    return krankenhausStatService.findKrankenhausStatDataByBundesland(bundesland, startDate, endDate) ;
  }


  @GetMapping("v1/getLatestDataBundeslaender")
  public List<KrankenhausStat> findLatestKrankenhausStatDataForBundeslaender() {
    return krankenhausStatService.findLatestKrankenhausStatDataForBundeslaender();
  }




}
