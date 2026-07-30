package burunzhuy.entity.auth;

import burunzhuy.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Types;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "password_restore")
public class PasswordRestore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JdbcTypeCode(Types.CHAR)
    @Column(columnDefinition = "CHAR(64)", nullable = false)
    private String token;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(nullable = false)
    private LocalDateTime expiredAt; // +30m
}
