package org.td024.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.td024.entity.Reservation;

import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
    List<Reservation> findAllByWorkspaceId(int id);
}
