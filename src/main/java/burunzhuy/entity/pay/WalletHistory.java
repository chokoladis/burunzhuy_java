package burunzhuy.entity.pay;

import burunzhuy.entity.Auction;
import burunzhuy.entity.user.Role;
import burunzhuy.entity.user.User;
import burunzhuy.enums.pay.TypeTransaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "wallet_history")
public class WalletHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeTransaction type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
