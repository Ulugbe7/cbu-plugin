package uz.zaytun.cbuplugin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.zaytun.cbuplugin.domain.data.ResponseLog;

@Repository
public interface ResponseLogRepository extends JpaRepository<ResponseLog, Long> {
}
