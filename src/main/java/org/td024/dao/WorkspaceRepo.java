package org.td024.dao;

import org.springframework.stereotype.Repository;
import org.td024.entity.Reservation;
import org.td024.entity.Workspace;
import org.td024.exception.WorkspaceIsReservedException;

import java.util.List;

@Repository
public final class WorkspaceRepo extends Repo<Workspace> {
    private final ReservationRepo reservationRepository;

    public WorkspaceRepo(ReservationRepo reservationRepository) {
        super(Workspace.class);
        this.reservationRepository = reservationRepository;
    }

    public boolean delete(int id) {
        List<Reservation> reservations = reservationRepository.getAllByWorkspace(id);
        if (reservations != null && !reservations.isEmpty())
            throw new WorkspaceIsReservedException("Workspace Has Dependent Reservations; ID: " + id);
        return super.delete(id);
    }
}
