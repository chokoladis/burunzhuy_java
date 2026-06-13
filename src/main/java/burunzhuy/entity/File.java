package burunzhuy.entity;

import burunzhuy.enums.file.EntityEnum;
import burunzhuy.enums.file.FilePurpose;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String originalName;
    @Column(nullable = false, length = 500)
    private String path;
    @Column(length = 10, nullable = false)
    private String ext;
//    todo
//    @Column(length = 50)
//    private String memoType = null;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private FilePurpose purpose;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
