package uz.zaytun.cbuplugin.domain.data;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "response_log")
public class ResponseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "response")
    private String response;

    @OneToOne
    @JoinColumn(name = "request_log_id")
    private RequestLog requestLog;

    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate = Instant.now();
}
