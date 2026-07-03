package burunzhuy.entity;

import burunzhuy.entity.user.User;
import burunzhuy.enums.auction.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "auctions")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idea_id", nullable = false)
    private Idea idea;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @Column(length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "auction", targetEntity = AuctionHistory.class, fetch = FetchType.LAZY)
    private Set<AuctionHistory> history = new HashSet<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime finishedAt;
}