package kr.lineedu.lms.feature.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryImpl {

    Optional<User> findByStateAndId(Integer state, Long id);

    Optional<User> findByStateAndLoginId(Integer state, String loginId);

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByLmsUserId(Long lmsUserId);
}

