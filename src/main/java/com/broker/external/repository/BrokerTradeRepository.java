package com.broker.external.repository;

import com.broker.external.entity.BrokerTradeEntity;
import com.broker.external.entity.BrokerTradeStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface BrokerTradeRepository extends JpaRepository<BrokerTradeEntity, String> {

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE BrokerTradeEntity b SET b.status = :status WHERE b.id = :id")
    int updateStatus(@Param("status") BrokerTradeStatusEntity status, @Param("id") String id);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE BrokerTradeEntity b SET b.status = :status, b.reason = :reason WHERE b.id = :id")
    int updateStatus(@Param("status") BrokerTradeStatusEntity status,
                     @Param("reason") String reason,
                     @Param("id") String id);
}
