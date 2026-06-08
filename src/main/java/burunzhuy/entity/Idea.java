package burunzhuy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GeneratedColumn;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "ideas")
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String shortDescription;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preview_id")
    private File preview;

    @Column(length = 1000, nullable = false)
    private String fullDescription;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceMin;

    @Column(precision = 12, scale = 2)
    private BigDecimal priceInstanceBuy;

    @OneToMany
    @JoinTable(
            name = "ideas_attaches",
            joinColumns = @JoinColumn(name = "idea_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private Set<File> attaches = new HashSet<>();
}
