package kr.lineedu.lms.config.constant;

import kr.lineedu.lms.config.properties.AppProperties;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    public static String MASTER_PASSWORD;
    public static String INIT_PASSWORD;

    public PasswordUtil(AppProperties appProperties){
        MASTER_PASSWORD = appProperties.util().masterPassword();
        INIT_PASSWORD = appProperties.util().initPassword();
    }
}
