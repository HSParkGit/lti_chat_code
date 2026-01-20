package kr.lineedu.lms.feature.user.domain;

import jakarta.persistence.*;
import kr.lineedu.lms.global.base.BaseEntity;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, length = 320)
    private String loginId;

    @Column(name = "lms_user_id")
    private Long lmsUserId;

    @Column(nullable = false, length = 1024)
    private String password;

    @Column(length = 128)
    private String name;

    @Column(length = 20)
    private String division;

    @Column(name = "sub_division", length = 20)
    private String subDivision;

    @Column(length = 320)
    private String email;

    @Column(length = 128)
    private String mobile;

    @Column(name = "agreement_flag", length = 1)
    private Character agreementFlag;

    private Integer state;

    @Column(name = "site_alarm_flag", length = 1)
    private Character siteAlarmFlag;

    @Column(name = "email_alarm_flag", length = 1)
    private Character emailAlarmFlag;

    @Column(length = 10)
    private String birth;

    private Integer role;

    // Static factory methods based on test usage
    public static User toEncodedStudentUser(Object dto, String encodedPassword, String division, String subDivision) {
        // Implementation would use the DTO
        return User.builder()
            .password(encodedPassword)
            .division(division)
            .subDivision(subDivision)
            .build();
    }

    public static User toEncodedUser(Object dto, String encodedPassword) {
        return User.builder()
            .password(encodedPassword)
            .build();
    }

    public void updateUser(Object updateDto) {
        // Implementation would update fields from DTO
    }

    public void updateUserSetting(Object settingDto) {
        // Implementation would update settings from DTO
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void delete(User adminUser) {
        this.state = 3; // DELETED state
        setDeleteInfo(adminUser);
    }

    public void updateRole(Object role) {
        // Implementation would update role
    }

    public void updateLmsUserId(Long lmsUserId) {
        this.lmsUserId = lmsUserId;
    }

    public void updateAgreementFlag(Character flag) {
        this.agreementFlag = flag;
    }
}

