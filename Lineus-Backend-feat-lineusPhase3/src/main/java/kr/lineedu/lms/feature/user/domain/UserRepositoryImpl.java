package kr.lineedu.lms.feature.user.domain;

import java.util.Optional;

public interface UserRepositoryImpl {
    // Removed findByStateAndUserNumber - User entity doesn't have userNumber property
    // Use findByStateAndLoginId instead if needed
    Optional<User> findByStateAndId(Integer state, Long id);
    Optional<User> findByStateAndLoginId(Integer state, String loginId);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByLmsUserId(Long lmsUserId);
}

