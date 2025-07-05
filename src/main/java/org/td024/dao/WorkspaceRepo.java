package org.td024.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.td024.entity.Workspace;

import java.util.Date;
import java.util.List;

@Repository
public interface WorkspaceRepo extends JpaRepository<Workspace, Integer> {
    @Query("""
            SELECT w
            FROM Workspace w
            WHERE (SELECT COUNT(r.id)
                   FROM Reservation r
                   WHERE r.workspace.id = w.id
                     AND NOT (r.interval.endTime < :startTime OR :endTime < r.interval.startTime)) = 0
            """)
    List<Workspace> findAvailableWorkspaces(Date startTime, Date endTime);

    @Query("""
            SELECT (SELECT COUNT(r.id)
                    FROM Reservation r
                             JOIN Workspace w ON r.workspace.id = w.id
                    WHERE r.workspace.id = w.id
                      AND NOT (r.interval.endTime < :startTime OR :endTime < r.interval.startTime)) = 0
            """)
    boolean isWorkspaceAvailable(int id, Date startTime, Date endTime);
}
