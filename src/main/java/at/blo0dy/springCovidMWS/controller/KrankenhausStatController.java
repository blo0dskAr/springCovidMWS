package at.blo0dy.springCovidMWS.controller;

import at.blo0dy.springCovidMWS.model.GesamtStat;
import at.blo0dy.springCovidMWS.model.KrankenhausStat;
import at.blo0dy.springCovidMWS.service.stats.krankenhausStats.KrankenhausStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


  @GetMapping({"/v1/", "/v1"})
  public List<KrankenhausStat> findGesamtStatData() {
    return krankenhausStatService.findKrankenhausStatData() ;
  }


  @GetMapping("/v1/{bundesland}")
  public List<KrankenhausStat> findGesamtStatDataByBundesland(@PathVariable("bundesland") String bundesland) {
    return krankenhausStatService.findKrankenhausStatDataByBundesland(bundesland) ;
  }







}
