package kr.lineedu.lms.config.feign;

import java.time.format.DateTimeFormatter;

import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

@Configuration
public class FeignDateTimeFormatConfiguration {
	@Bean
	public FeignFormatterRegistrar localDateFeignFormatterRegister() {
		return registry -> {
			DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
			registrar.setUseIsoFormat(true);
			registrar.setDateTimeFormatter(DateTimeFormatter.ISO_DATE_TIME);
			registrar.registerFormatters(registry);
		};
	}
}
