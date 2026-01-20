package kr.lineedu.lms.global.base;

import static java.time.LocalDateTime.*;

import java.time.LocalDateTime;

import kr.lineedu.lms.config.jwt.JwtPrincipal;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import kr.lineedu.lms.feature.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

	@Column(columnDefinition = "text")
	private String description;

	@Column
	@CreatedBy
	private String createdBy;

	@CreatedDate
	@Column
	private LocalDateTime createdAt;

	@Column
	@LastModifiedBy
	private String updatedBy;

	@LastModifiedDate
	@Column
	private LocalDateTime updatedAt;

	@Column
	private String deletedBy;
	//    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
	@Column
	private LocalDateTime deletedAt;

	@Column
	private Character deletedFlag;

	public void setDeleteInfo(User user) {
		this.deletedBy = String.valueOf(user.getId());
		this.deletedAt = now();
		this.deletedFlag = 'Y';
	}

	public void prePersist() {
		this.deletedFlag = this.deletedFlag == null ? 'N' : this.deletedFlag;
		this.createdAt = this.createdAt == null ? LocalDateTime.now() : this.createdAt;
		this.createdBy = this.createdBy == null ? "ADMIN" : this.createdBy;
	}

	protected Long loginUserIdFromSecurityContext(){
		return ((JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
	}
}
