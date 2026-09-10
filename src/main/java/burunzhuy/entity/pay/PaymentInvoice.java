package burunzhuy.entity.pay;

import burunzhuy.enums.pay.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "payment_invoices")
public class PaymentInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_history_id", nullable = false)
    private WalletHistory walletHistory;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.WAITING;

    @Column
    private String url;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
