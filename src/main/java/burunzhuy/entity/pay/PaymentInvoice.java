package burunzhuy.entity.pay;

import burunzhuy.dto.pay.PaymentExtra;
import burunzhuy.enums.pay.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Entity
@Table(name = "payment_invoices")
public class PaymentInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String externalId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_history_id", nullable = false)
    private WalletHistory walletHistory;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column
    private BigDecimal actuallyPaid;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.WAITING;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private PaymentExtra extra;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(updatable = false)
    private LocalDateTime expirationAt;
}
