package org.td024.service;

import org.springframework.stereotype.Service;
import org.td024.dao.ReservationRepo;
import org.td024.entity.Interval;
import org.td024.entity.Reservation;
import org.td024.entity.Workspace;
import org.td024.exception.NotFoundException;

import java.util.List;

@Service
public final class ReservationService {

    private final ReservationRepo repository;
    private final WorkspaceService workspaceService;

    public ReservationService(ReservationRepo repository, WorkspaceService workspaceService) {
        this.repository = repository;
        this.workspaceService = workspaceService;
    }

    public List<Reservation> getAllReservations() {
        return repository.getAll();
    }

    public void makeReservation(String name, int spaceId, Interval interval) {
        Workspace workspace;

        try {
            workspace = workspaceService.getWorkspaceById(spaceId);
        } catch (NotFoundException e) {
            System.out.println("Workspace not found!");
            return;
        }

        if (!workspaceService.isAvailable(spaceId, interval)) {
            System.out.println("Workspace is not available!");
            return;
        }

        Reservation reservation = new Reservation(name, workspace, interval);
        int id = repository.save(reservation);

        System.out.println("Reservation created successfully!\nReservation ID: " + id);
    }

    public void cancelReservation(int id) {
        boolean deleted = repository.delete(id);
        if (deleted) System.out.println("Reservation cancelled successfully!");
        else System.out.println("Reservation not found!");
    }
}
