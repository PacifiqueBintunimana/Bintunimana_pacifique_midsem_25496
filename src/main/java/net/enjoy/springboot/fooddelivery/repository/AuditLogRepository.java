package net.enjoy.springboot.fooddelivery.repository;
import net.enjoy.springboot.fooddelivery.entity.AuditLog;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{

    List<AuditLog> findByAction(String action);

    List<AuditLog> findByUsername(String username);

    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<AuditLog> findByActionAndUsername(String action, String username);

    long countByAction(String action);
//
//    List<AuditLog> findTopNByOrderByTimestampDesc(Integer n);
//
//    @Query("SELECT a FROM AuditLog a ORDER BY a.timestamp DESC")
//    List<AuditLog> findTopNByOrderByTimestampDesc(Pageable pageable);
//
//    Pageable pageable = PageRequest.of(0, N); // N is the number of records you want
//    List<AuditLog> results = auditLogRepository.findTopNByOrderByTimestampDesc(pageable);


}
