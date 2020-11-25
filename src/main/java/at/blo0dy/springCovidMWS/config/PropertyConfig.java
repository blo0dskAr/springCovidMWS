package at.blo0dy.springCovidMWS.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("classpath:application.properties")
public class PropertyConfig {

  @Value("${gesamtstat.filepath}")
  private String gesamtStatfilePath ;
  @Value("${gesamtstat.sourcepath}")
  private String gesamtStatfetchURL;

  @Value("${krankenhausstat.filepath}")
  private String krankenhausStatfilePath ;
  @Value("${krankenhausstat.sourcepath}")
  private String krankenhausStatfetchURL;

}
