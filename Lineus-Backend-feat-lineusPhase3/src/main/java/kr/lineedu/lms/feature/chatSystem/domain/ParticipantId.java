package kr.lineedu.lms.feature.chatSystem.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ParticipantId implements Serializable {
    private String chatId;
    private Long userId;
}

