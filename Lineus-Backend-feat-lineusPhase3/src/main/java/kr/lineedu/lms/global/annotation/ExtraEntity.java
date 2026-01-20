package kr.lineedu.lms.global.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 해당 엔티티가 Extra PersistenceUnit 참조함을 의미하는 애노테이션
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ExtraEntity {

    /**
     * DataSource Configuration Prefix
     */
    String prefix() default "";

}
