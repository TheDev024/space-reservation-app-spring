package org.td024.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.td024.controller.MailingListService;
import org.td024.dao.WorkspaceRepo;
import org.td024.dto.CreateWorkspace;
import org.td024.dto.EditWorkspace;
import org.td024.entity.Interval;
import org.td024.entity.Workspace;
import org.td024.enums.WorkspaceType;
import org.td024.exception.ConflictException;
import org.td024.exception.NoContentException;
import org.td024.exception.NotFoundException;

import java.util.*;

@Service
public class WorkspaceService {

    private final WorkspaceRepo repository;
    private final MailingListService mailingListService;

    public WorkspaceService(WorkspaceRepo repository, MailingListService mailingListService) {
        this.repository = repository;
        this.mailingListService = mailingListService;
    }

    public List<Workspace> getAllWorkspaces(WorkspaceType type, String nameQ, String addressQ, Double minPrice, Double maxPrice) {
        nameQ = nameQ == null ? "" : nameQ;
        addressQ = addressQ == null ? "" : addressQ;

        List<Workspace> workspaces = repository.findAll(type, "%" + nameQ + "%", "%" + addressQ + "%", minPrice, maxPrice);
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

        notifyWorkspaceCreated(workspace);

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

        notifyWorkspaceDeleted(id);
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

    private void notifyWorkspaceCreated(Workspace workspace) {
        Map<String, String> data = new HashMap<>();
        data.put("name", workspace.getName());
        data.put("address", workspace.getAddress());
        data.put("price", workspace.getPrice().toString());
        data.put("type", workspace.getType().toString());
        mailingListService.notifyMembers("workspace-created", data);
    }

    private void notifyWorkspaceDeleted(int id) {
        Workspace workspace = getWorkspaceById(id);
        Map<String, String> data = new HashMap<>();
        data.put("id", String.valueOf(id));
        data.put("name", workspace.getName());
        data.put("address", workspace.getAddress());
        mailingListService.notifyMembers("workspace-deleted", data);
    }
}
