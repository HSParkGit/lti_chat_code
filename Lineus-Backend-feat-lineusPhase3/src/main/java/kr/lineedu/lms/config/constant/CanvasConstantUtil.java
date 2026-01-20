package kr.lineedu.lms.config.constant;

import kr.lineedu.lms.config.properties.AppProperties;
import org.springframework.stereotype.Component;

@Component
public class CanvasConstantUtil {

	public static String SERVICE_URL;
	public static String CANVAS_URL;
	public static Long ROLE_NUMBER;

	public CanvasConstantUtil(AppProperties appProperties) {
		SERVICE_URL = appProperties.util().serviceUrl();
		CANVAS_URL = appProperties.util().canvasUrl();
		ROLE_NUMBER = appProperties.util().roleNumber();
	}
}
