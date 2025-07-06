package org.td024.service;

import org.springframework.stereotype.Service;
import org.td024.dao.ReservationRepo;
import org.td024.entity.Interval;
import org.td024.entity.Reservation;
import org.td024.entity.Workspace;
import org.td024.exception.ConflictException;
import org.td024.exception.NoContentException;
import org.td024.exception.NotFoundException;
import org.td024.dto.EditReservation;
import org.td024.dto.MakeReservation;

import java.util.List;
import java.util.Optional;

@Service
public final class ReservationService {

    private final ReservationRepo repository;
    private final WorkspaceService workspaceService;

    public ReservationService(ReservationRepo repository, WorkspaceService workspaceService) {
        this.repository = repository;
        this.workspaceService = workspaceService;
    }

    public List<Reservation> getAllReservations(Integer workspaceId) {
        List<Reservation> reservations = workspaceId == null ? repository.findAll() : repository.findAllByWorkspaceId(workspaceId);
        if (reservations.isEmpty()) throw new NoContentException("No Reservations Made Yet");
        return reservations;
    }

    public Reservation getReservationById(int id) {
        Optional<Reservation> reservation = repository.findById(id);
        if (reservation.isEmpty()) throw new NotFoundException("Reservation Not Found!");
        return reservation.get();
    }

    public int makeReservation(MakeReservation makeReservation) {
        int spaceId = makeReservation.getWorkspaceId();
        Interval interval = makeReservation.getInterval();

        if (!workspaceService.isAvailable(spaceId, interval)) throw new ConflictException("Reserved Workspace Cannot Be Reserved!");
        Workspace workspace = workspaceService.getWorkspaceById(spaceId);

        String name = makeReservation.getName();

        Reservation reservation = new Reservation(name, workspace, interval);
        reservation = repository.save(reservation);

        return reservation.getId();
    }

    public void editReservation(int id, EditReservation editReservation) {
        Reservation reservation = getReservationById(id);
        reservation.setName(editReservation.getName());
        repository.save(reservation);
    }

    public void cancelReservation(int id) {
        repository.deleteById(id);
    }
}
