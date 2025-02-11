package uz.zaytun.cbuplugin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.zaytun.cbuplugin.domain.data.RequestLog;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
}
