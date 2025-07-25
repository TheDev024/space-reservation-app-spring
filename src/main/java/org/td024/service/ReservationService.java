package org.td024.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.td024.auth.entity.AppUser;
import org.td024.dao.ReservationRepo;
import org.td024.dto.EditReservation;
import org.td024.dto.MakeReservation;
import org.td024.entity.Interval;
import org.td024.entity.Reservation;
import org.td024.entity.Workspace;
import org.td024.exception.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepo repository;
    private final WorkspaceService workspaceService;

    public ReservationService(ReservationRepo repository, WorkspaceService workspaceService) {
        this.repository = repository;
        this.workspaceService = workspaceService;
    }

    public List<Reservation> getAllReservations(Integer workspaceId, String nameQ, Date startTime, Date endTime, String username) {
        nameQ = nameQ == null ? "" : nameQ;
        List<Reservation> reservations = repository.findAll(workspaceId, "%" + nameQ + "%", startTime, endTime, username);

        if (reservations.isEmpty()) throw new NoContentException("No Reservations Found For The Given Criteria!");
        return reservations;
    }

    public Reservation getReservationById(int id, int workspaceId) {
        Optional<Reservation> reservation = repository.findByIdAndWorkspaceId(id, workspaceId);
        if (reservation.isEmpty()) throw new NotFoundException("Reservation Not Found!");
        return reservation.get();
    }

    @Transactional
    public int makeReservation(int workspaceId, MakeReservation makeReservation, AppUser principal) {
        Interval interval;
        try {
            interval = new Interval.IntervalBuilder().startTime(makeReservation.getStartTime()).endTime(makeReservation.getEndTime()).build();
        } catch (InvalidTimeIntervalException e) {
            throw new BadRequestException("Invalid Time Interval");
        }

        if (!workspaceService.isAvailable(workspaceId, interval))
            throw new ConflictException("Reserved Workspace Cannot Be Reserved!");
        Workspace workspace = workspaceService.getWorkspaceById(workspaceId);

        String name = makeReservation.getName();

        Reservation reservation = new Reservation(name, workspace, interval, principal);
        reservation = repository.save(reservation);

        return reservation.getId();
    }

    @Transactional
    public void editReservation(int id, int workspaceId, EditReservation editReservation) {
        Reservation reservation = getReservationById(id, workspaceId);
        if (reservation.getWorkspace().getId() != workspaceId) throw new NotFoundException("Reservation Not Found!");
        reservation.setName(editReservation.getName());
        repository.save(reservation);
    }

    @Transactional
    public void cancelReservation(int id, int workspaceId) {
        if (!repository.existsByIdAndWorkspaceId(id, workspaceId))
            throw new NotFoundException("Reservation Not Found!");
        repository.deleteById(id);
    }
}
