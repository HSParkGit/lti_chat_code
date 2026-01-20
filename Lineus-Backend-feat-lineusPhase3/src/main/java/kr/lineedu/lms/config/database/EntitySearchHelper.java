package kr.lineedu.lms.config.database;

import jakarta.persistence.Entity;
import kr.lineedu.lms.global.annotation.ExtraEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 데이터 소스에 포함된 엔티티 패키지를 조회하는 Helper 클래스
 */
@Component
@RequiredArgsConstructor
public class EntitySearchHelper {

    private final String[] basePackage = KumohPersistenceConfig.BASE_PACKAGE;

    public Set<Class<?>> getEntities() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

        return provider.findCandidateComponents(Arrays.toString(basePackage)).stream()
                .map(beanDefinition -> {
                    try {
                        return Class.forName(beanDefinition.getBeanClassName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
    }

    /**
     * Primary EntityManager에서 관리할 Entity 검색
     *
     * @return Primary EntityManager가 관리하는 엔티티들이 속한 패키지 목록
     */
    public Set<String> findEntityPackagesOfPrimary() {
        Set<Class<?>> primaryEntities = getEntitiesExcluding(ExtraEntity.class);
        return extractPackageNames(primaryEntities);
    }

    /**
     * Extra EntityManager에서 관리할 Entity 검색
     *
     * @param prefix 엔티티의 prefix
     * @return Extra EntityManager가 관리하는 엔티티들이 속한 패키지 목록
     */
    public Set<String> findEntityPackagesOfExtra(String prefix) {
        Set<Class<?>> extraEntities = getEntitiesMatchingPrefix(prefix);
        return extractPackageNames(extraEntities);
    }

    private Set<Class<?>> getEntitiesExcluding(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> entityClasses = getEntities();  // 모든 Entity 가져오기
        Set<Class<?>> excludedClasses = getEntitiesWithAnnotation(annotationClass);  // ExtraEntity 어노테이션이 붙은 클래스 제외
        entityClasses.removeAll(excludedClasses);
        return entityClasses;
    }

    private Set<Class<?>> getEntitiesMatchingPrefix(String prefix) {
        return getEntitiesWithAnnotation(ExtraEntity.class).stream()
                .filter(entityClass -> {
                    ExtraEntity extraEntityAnnotation = entityClass.getAnnotation(ExtraEntity.class);
                    return extraEntityAnnotation.prefix().equals(prefix);
                })
                .collect(Collectors.toSet());
    }

    private Set<Class<?>> getEntitiesWithAnnotation(Class<? extends Annotation> annotationClass) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(annotationClass));

        return provider.findCandidateComponents(Arrays.toString(basePackage)).stream()
                .map(beanDefinition -> {
                    try {
                        return Class.forName(beanDefinition.getBeanClassName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
    }

    private Set<String> extractPackageNames(Set<Class<?>> entityClasses) {
        return entityClasses.stream()
                .map(Class::getPackage)
                .map(Package::getName)
                .collect(Collectors.toSet());
    }
}
