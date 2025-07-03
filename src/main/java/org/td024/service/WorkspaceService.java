package org.td024.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.td024.dao.ReservationRepo;
import org.td024.dao.WorkspaceRepo;
import org.td024.entity.Interval;
import org.td024.entity.Reservation;
import org.td024.entity.Workspace;
import org.td024.exception.NotFoundException;
import org.td024.exception.WorkspaceSaveFailed;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@Service
public class WorkspaceService {

    private final WorkspaceRepo repository;
    private ReservationRepo reservationRepository;

    private final BiFunction<Integer, Interval, Boolean> isAvailable = (Integer id, Interval interval) -> {
        List<Reservation> reservations = reservationRepository.getAllByWorkspace(id);
        return reservations.stream().noneMatch(reservation -> Interval.isOverlap(interval, reservation.getInterval()));
    };

    public WorkspaceService(WorkspaceRepo repository, ReservationRepo reservationRepository) {
        this.repository = repository;
        this.reservationRepository = reservationRepository;
    }

    public Workspace getWorkspaceById(int id) throws NotFoundException {
        Optional<Workspace> workspace = repository.getById(id);
        if (workspace.isEmpty()) throw new NotFoundException("Workspace not found!");
        return workspace.get();
    }

    public List<Workspace> getAllWorkspaces() {
        return repository.getAll();
    }

    public List<Workspace> getAvailableWorkspaces(Interval interval) {
        List<Workspace> workspaces = repository.getAll();
        return workspaces.stream().filter(workspace -> isAvailable(workspace.getId(), interval)).toList();
    }

    @Transactional
    public int createWorkspace(Workspace workspace) {
        return repository.save(workspace);
    }

    @Transactional
    public void editWorkspace(int id, Workspace workspace) throws WorkspaceSaveFailed {
        if (!workspaceExists(id)) {
            System.out.println("Workspace not found!");
            throw new WorkspaceSaveFailed("Couldn't save workspace!");
        }

        workspace.setId(id);
        repository.save(workspace);
    }

    @Transactional
    public void deleteWorkspace(int id) {
        repository.delete(id);
    }

    public boolean workspaceExists(int id) {
        return repository.getById(id).isPresent();
    }

    public boolean isAvailable(int id, Interval interval) {
        return isAvailable.apply(id, interval);
    }
}
