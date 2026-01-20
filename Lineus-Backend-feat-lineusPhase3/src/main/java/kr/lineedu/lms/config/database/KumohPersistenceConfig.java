package kr.lineedu.lms.config.database;

import jakarta.persistence.EntityManagerFactory;
import kr.lineedu.lms.global.annotation.ExtraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableJpaRepositories(
        basePackages = {
                "kr.lineedu.lms.feature.attendance.domain",
                "kr.lineedu.lms.feature.attendanceOnlineSetting.domain",
                "kr.lineedu.lms.feature.attendanceSetting.domain",
                "kr.lineedu.lms.feature.grade.api",
                "kr.lineedu.lms.feature.assignmentGroupGrade.api",
                "kr.lineedu.lms.feature.gradeSetting.api",
                "kr.lineedu.lms.feature.discussionTopics.domain",
                "kr.lineedu.lms.feature.help.domain",
                "kr.lineedu.lms.feature.user.domain",
                "kr.lineedu.lms.feature.userhistory.domain",
                "kr.lineedu.lms.feature.helpcenter.domain",
                "kr.lineedu.lms.feature.bbsctt.domain",
                "kr.lineedu.lms.feature.media.domain",
                "kr.lineedu.lms.feature.content",
                "kr.lineedu.lms.feature.school.domain",
                "kr.lineedu.lms.feature.panopto.domain.video",
                "kr.lineedu.lms.feature.panopto.domain.onlineAttendance",
                "kr.lineedu.lms.feature.panopto.domain.attendance",
                "kr.lineedu.lms.feature.reminder.domain",
                "kr.lineedu.lms.feature.agenda.domain",
                "kr.lineedu.lms.feature.todo.domain",
                "kr.lineedu.lms.feature.chatSystem.domain",
                "kr.lineedu.lms.feature.systemGroup.domain",
                "kr.lineedu.lms.feature.groupproject.repository",
                "kr.lineedu.lms.feature.groupProjectSubmission.domain",
                "kr.lineedu.lms.feature.file.domain.repository",
                "kr.lineedu.lms.feature.groupprojectrubric.repository",
                "kr.lineedu.lms.feature.groupprojectgroups.repo",
                "kr.lineedu.lms.feature.groupprojectprogress.repo"
        },
        entityManagerFactoryRef = KumohPersistenceConfig.PRIMARY_ENTITY_MANAGER_FACTORY,
        transactionManagerRef = KumohPersistenceConfig.PRIMARY_TRANSACTION_MANAGER,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = ExtraRepository.class)
)
@EntityScan(basePackages = {
        "kr.lineedu.lms.feature.panopto.domain.video",   // Video 엔티티 패키지 경로
        "kr.lineedu.lms.feature.panopto.domain.attendance", // Attendance 엔티티 패키지 경로
        "kr.lineedu.lms.feature.groupproject.model",
        "kr.lineedu.lms.feature.groupprojectrubric.model",
        "kr.lineedu.lms.feature.chatSystem.domain"  // Chat system entities

})
@RequiredArgsConstructor
public class KumohPersistenceConfig {
    public static final String PRIMARY_DATASOURCE_CONFIG_PREFIX = "kumoh";
    public static final String PRIMARY_PERSISTENCE_UNIT = PRIMARY_DATASOURCE_CONFIG_PREFIX + "PersistenceUnit";
    public static final String PRIMARY_DATASOURCE = PRIMARY_DATASOURCE_CONFIG_PREFIX + "DataSource";
    public static final String PRIMARY_ENTITY_MANAGER_FACTORY = PRIMARY_DATASOURCE_CONFIG_PREFIX + "EntityManagerFactory";
    public static final String PRIMARY_TRANSACTION_MANAGER = PRIMARY_DATASOURCE_CONFIG_PREFIX + "TransactionManager";
    public static final String[] BASE_PACKAGE = {
                        "kr.lineedu.lms.feature.attendanceOnlineSetting.domain",
                        "kr.lineedu.lms.feature.attendance.domain",
                        "kr.lineedu.lms.feature.attendanceSetting.domain",
                        "kr.lineedu.lms.feature.grade.model",
                        "kr.lineedu.lms.feature.assignmentGroupGrade.model",
                        "kr.lineedu.lms.feature.gradeSetting.model",
                        "kr.lineedu.lms.feature.gradePoint.model",
                        "kr.lineedu.lms.feature.help.domain",
                        "kr.lineedu.lms.feature.discussionTopics.domain",
                        "kr.lineedu.lms.feature.user.domain",
                        "kr.lineedu.lms.feature.userhistory.domain",
                        "kr.lineedu.lms.feature.helpcenter.domain",
                        "kr.lineedu.lms.feature.bbsctt.domain",
                        "kr.lineedu.lms.feature.media.domain",
                        "kr.lineedu.lms.feature.content",
                        "kr.lineedu.lms.feature.school.domain",
                        "kr.lineedu.lms.feature.panopto.domain.video",
                        "kr.lineedu.lms.feature.panopto.domain.onlineAttendance",
                        "kr.lineedu.lms.feature.panopto.domain.attendance",
                        "kr.lineedu.lms.feature.reminder.domain",
                        "kr.lineedu.lms.feature.agenda.domain",
                        "kr.lineedu.lms.feature.todo.domain",
                        "kr.lineedu.lms.feature.chatSystem.domain",
                        "kr.lineedu.lms.feature.systemGroup.domain",
                        "kr.lineedu.lms.feature.groupproject.model",
                        "kr.lineedu.lms.feature.groupProjectSubmission.domain",
                        "kr.lineedu.lms.feature.file.domain",
                        "kr.lineedu.lms.feature.groupprojectrubric.model",
                        "kr.lineedu.lms.feature.groupprojectgroups.model",
                        "kr.lineedu.lms.feature.groupprojectprogress.model"

};
    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;
    private final EntitySearchHelper entitySearchHelper;
    private final Environment environment;

    /**
     * Primary 데이터 소스 생성
     *
     * @return DataSource
     */
    @Primary
    @Bean(name = PRIMARY_DATASOURCE)
    public DataSource kumohDataSource() {
        String url = environment.getProperty("spring.kumoh-datasource.jdbc-url", 
            environment.getProperty("spring.kumoh-datasource.url",
                "jdbc:mysql://127.0.0.1:3306/db_lineus?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8"));
        String username = environment.getProperty("spring.kumoh-datasource.username", "root");
        String password = environment.getProperty("spring.kumoh-datasource.password", "root");
        String driverClassName = environment.getProperty("spring.kumoh-datasource.driver-class-name", 
            "com.mysql.cj.jdbc.Driver");
        
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }

    /**
     * Primary 데이터 소스에 대한 EntityManagerFactory 생성
     *
     * @param builder    EntityManagerFactoryBuilder
     * @param dataSource DataSource
     * @return LocalContainerEntityManagerFactoryBean
     */
    @Primary
    @Bean(name = PRIMARY_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier(PRIMARY_DATASOURCE) DataSource dataSource) {

        // 엔티티 클래스 패키지 조회
        Set<String> entityPackages = entitySearchHelper.findEntityPackagesOfPrimary();

        // BASE_PACKAGE에 있는 패키지를 추가
        entityPackages.addAll(Set.of(KumohPersistenceConfig.BASE_PACKAGE));


        // 프로퍼티값 조회
        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings());
        return builder
                .dataSource(dataSource)
                .packages(entityPackages.toArray(new String[0]))  // 병합된 패키지 목록
                .persistenceUnit(PRIMARY_PERSISTENCE_UNIT)
                .properties(properties)
                .build();
    }


    /**
     * Primary 데이터 소스에 대한 TransactionManager 생성
     *
     * @param entityManagerFactory EntityManagerFactory
     * @return PlatformTransactionManager
     */
    @Primary
    @Bean(name = PRIMARY_TRANSACTION_MANAGER)
    public PlatformTransactionManager transactionManager(
            @Qualifier(PRIMARY_ENTITY_MANAGER_FACTORY) EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}