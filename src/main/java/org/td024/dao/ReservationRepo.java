package org.td024.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.td024.entity.Reservation;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
    @Query("""
            SELECT r
            FROM Reservation r
            WHERE (:workspaceId IS NULL OR r.workspace.id = :workspaceId)
                AND (:nameQ IS NULL OR r.name LIKE :nameQ)
                AND (:startTime IS NULL OR r.interval.startTime <= :startTime)
                AND (:endTime IS NULL OR r.interval.endTime >= :endTime)
                AND (:username IS NULL OR r.reservedBy.username >= :username)
            """)
    List<Reservation> findAll(Integer workspaceId, String nameQ, Date startTime, Date endTime, String username);

    Optional<Reservation> findByIdAndWorkspaceId(int id, int workspaceId);

    boolean existsByIdAndWorkspaceId(int id, int workspaceId);
}
