package burunzhuy.entity.pay;

import burunzhuy.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(precision = 12, scale = 2)
    private BigDecimal balance;
    @Column(length = 40)
    private String currency;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY)
    private Set<WalletHistory> history = new HashSet<>();
}
