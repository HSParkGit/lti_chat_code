package kr.lineedu.lms.feature.chatSystem.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, String> {

    Page<Message> findByChatIdOrderByTimestampDesc(String chatId, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.chatId = :chatId AND m.senderId != :userId " +
           "AND (:lastReadTimestamp IS NULL OR m.timestamp > :lastReadTimestamp) " +
           "ORDER BY m.timestamp ASC")
    List<Message> findUnreadMessagesByTimestamp(@Param("chatId") String chatId, @Param("userId") Long userId, 
                                                 @Param("lastReadTimestamp") java.time.LocalDateTime lastReadTimestamp);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.chatId = :chatId AND m.senderId != :userId " +
           "AND (:lastReadTimestamp IS NULL OR m.timestamp > :lastReadTimestamp)")
    Long countUnreadMessagesByTimestamp(@Param("chatId") String chatId, @Param("userId") Long userId, 
                                        @Param("lastReadTimestamp") java.time.LocalDateTime lastReadTimestamp);

    Optional<Message> findFirstByChatIdOrderByTimestampDesc(String chatId);

    Page<Message> findByChatIdAndTimestampAfterOrderByTimestampDesc(String chatId, java.time.LocalDateTime timestamp, Pageable pageable);
}

