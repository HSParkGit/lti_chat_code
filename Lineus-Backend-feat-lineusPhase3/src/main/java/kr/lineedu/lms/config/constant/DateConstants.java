package kr.lineedu.lms.config.constant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateConstants {

	public LocalDateTime NOW = LocalDateTime.now().withNano(0);
	public LocalDateTime TODAY_START_TIME = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).withNano(0);
	public LocalDateTime TODAY_END_TIME = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).withNano(0);
	public LocalDateTime TOMORROW_START_TIME = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIN).withNano(0);
	public LocalDateTime TOMORROW_END_TIME = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MAX).withNano(0);
	public LocalDateTime DAT_START_TIME = LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.MIN).withNano(0);
	public LocalDateTime DAT_END_TIME = LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.MAX).withNano(0);
}
