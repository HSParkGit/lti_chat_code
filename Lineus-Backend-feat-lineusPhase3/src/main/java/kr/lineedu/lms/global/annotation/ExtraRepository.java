package kr.lineedu.lms.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 해당 Repository가 Extra PersistenceUnit 참조함을 의미하는 Annotation
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtraRepository {
    /**
     * DataSource Configuration Prefix
     */
    String prefix() default "";
}
