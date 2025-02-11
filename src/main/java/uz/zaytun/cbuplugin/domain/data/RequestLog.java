package uz.zaytun.cbuplugin.domain.data;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "request_log")
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "method")
    private String method;

    @Column(name = "ip")
    private String ip;

    @Column(name = "query_params", columnDefinition = "text")
    private String queryParams;

    @Column(name = "request_body", columnDefinition = "text")
    private String requestBody;

    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate = Instant.now();
}
