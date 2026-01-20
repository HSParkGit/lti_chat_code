package kr.lineedu.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.cdimascio.dotenv.Dotenv;

@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
public class  BaseApplication {


    public static void main(String[] args) {

         // Load .env
         Dotenv dotenv = Dotenv.load();

         // Pass them as system properties (Spring will resolve ${} from here)
         dotenv.entries().forEach(entry ->
                 System.setProperty(entry.getKey(), entry.getValue())
         );
 
        SpringApplication.run(BaseApplication.class, args);
    }
}
