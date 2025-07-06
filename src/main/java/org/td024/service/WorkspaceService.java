package org.td024.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.td024.dao.WorkspaceRepo;
import org.td024.dto.CreateWorkspace;
import org.td024.dto.EditWorkspace;
import org.td024.entity.Interval;
import org.td024.entity.Workspace;
import org.td024.exception.ConflictException;
import org.td024.exception.NoContentException;
import org.td024.exception.NotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class WorkspaceService {

    private final WorkspaceRepo repository;

    public WorkspaceService(WorkspaceRepo repository) {
        this.repository = repository;
    }

    public List<Workspace> getAllWorkspaces() {
        List<Workspace> workspaces = repository.findAll();
        if (workspaces.isEmpty()) throw new NoContentException("No Workspace Exists Yet!");
        return workspaces;
    }

    public List<Workspace> getAvailableWorkspaces(Date startTime, Date endTime) {
        return repository.findAvailableWorkspaces(startTime, endTime);
    }

    public Workspace getWorkspaceById(int id) {
        Optional<Workspace> workspace = repository.findById(id);
        if (workspace.isEmpty()) throw new NotFoundException("Workspace not found!");
        return workspace.get();
    }

    @Transactional
    public int createWorkspace(CreateWorkspace createWorkspace) {
        Workspace workspace = convertToWorkspace(createWorkspace);
        workspace = repository.save(workspace);
        return workspace.getId();
    }

    @Transactional
    public void editWorkspace(int id, EditWorkspace createWorkspace) {
        if (!repository.existsById(id)) throw new NotFoundException("Workspace not found!");

        Workspace workspace = getWorkspaceById(id);

        workspace.setName(createWorkspace.getName());
        workspace.setPrice(createWorkspace.getPrice());

        repository.save(workspace);
    }

    @Transactional
    public void deleteWorkspace(int id) {
        if (repository.isReserved(id)) throw new ConflictException("Reserved Workspace Cannot Be Deleted!");
        repository.deleteById(id);
    }

    public boolean isAvailable(int id, Interval interval) {
        return repository.isWorkspaceAvailable(id, interval.getStartTime(), interval.getEndTime());
    }

    private Workspace convertToWorkspace(CreateWorkspace createWorkspace) {
        Workspace workspace = new Workspace();

        workspace.setName(createWorkspace.getName());
        workspace.setType(createWorkspace.getType());
        workspace.setAddress(createWorkspace.getAddress());
        workspace.setPrice(createWorkspace.getPrice());

        return workspace;
    }
}
