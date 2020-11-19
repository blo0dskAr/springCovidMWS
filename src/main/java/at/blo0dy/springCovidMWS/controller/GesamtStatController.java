package at.blo0dy.springCovidMWS.controller;

import at.blo0dy.springCovidMWS.model.GesamtStat;
import at.blo0dy.springCovidMWS.service.stats.gesamtStats.GesamtStatServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/covid/api")
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

  @GetMapping({"/v1/", "/v1"})
  public List<GesamtStat> findGesamtStatData() {
    return gesamtStatService.findGesamtStatData() ;
  }


  @GetMapping("/v1/{bundesland}")
  public List<GesamtStat> findGesamtStatDataByBundesland(@PathVariable("bundesland") String bundesland) {
    return gesamtStatService.findGesamtStatDataByBundesland(bundesland) ;
  }




}
