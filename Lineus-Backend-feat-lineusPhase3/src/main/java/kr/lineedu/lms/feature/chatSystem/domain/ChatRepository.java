package kr.lineedu.lms.feature.chatSystem.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, String> {

    @Query("SELECT c FROM Chat c JOIN c.participants p WHERE p.userId = :userId AND p.deleted = FALSE ORDER BY c.createdAt DESC")
    List<Chat> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Chat c JOIN c.participants p1 JOIN c.participants p2 " +
           "WHERE c.type = 'direct' AND p1.userId = :userId1 AND p2.userId = :userId2 " +
           "AND p1.deleted = FALSE AND p2.deleted = FALSE ORDER BY c.createdAt DESC")
    List<Chat> findDirectChatsBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    @Query("SELECT c FROM Chat c JOIN c.participants p1 JOIN c.participants p2 " +
           "WHERE c.type = 'direct' AND p1.userId = :userId1 AND p2.userId = :userId2 ORDER BY c.createdAt DESC")
    List<Chat> findDirectChatsBetweenUsersIncludingDeleted(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    @Query("SELECT c FROM Chat c WHERE c.chatId = :chatId AND EXISTS " +
           "(SELECT p FROM Participant p WHERE p.chatId = :chatId AND p.userId = :userId AND p.deleted = FALSE)")
    Optional<Chat> findByIdAndUserId(@Param("chatId") String chatId, @Param("userId") Long userId);

    @Query("SELECT c FROM Chat c WHERE c.inviteLink = :inviteLink")
    Optional<Chat> findByInviteLink(@Param("inviteLink") String inviteLink);
}

