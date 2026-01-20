package kr.lineedu.lms.feature.chatSystem.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, ParticipantId> {

    List<Participant> findByChatId(String chatId);

    List<Participant> findByChatIdAndDeletedFalse(String chatId);

    List<Participant> findByUserId(Long userId);

    Optional<Participant> findByChatIdAndUserId(String chatId, Long userId);

    Optional<Participant> findByChatIdAndUserIdAndDeletedFalse(String chatId, Long userId);

    @Query("SELECT p FROM Participant p WHERE p.userId = :userId")
    List<Participant> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(p) FROM Participant p WHERE p.chatId = :chatId")
    Long countByChatId(@Param("chatId") String chatId);
}

