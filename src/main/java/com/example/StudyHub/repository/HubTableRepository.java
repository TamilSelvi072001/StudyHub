package com.example.StudyHub.repository;

import com.example.StudyHub.model.HubTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HubTableRepository extends JpaRepository<HubTable, Long> {

    @Query("SELECT ht FROM HubTable ht JOIN FETCH ht.seats WHERE ht.hub.hubId = :hubId")
    List<HubTable> findByHubIdWithSeats(@Param("hubId") Long hubId);

}
