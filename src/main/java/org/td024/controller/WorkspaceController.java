package org.td024.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.td024.dto.CreateWorkspace;
import org.td024.dto.EditWorkspace;
import org.td024.entity.Workspace;
import org.td024.enums.WorkspaceType;
import org.td024.service.WorkspaceService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {
    private final WorkspaceService service;

    public WorkspaceController(WorkspaceService service) {
        this.service = service;
    }

    @GetMapping
    public List<Workspace> getAllWorkspaces(@RequestParam(required = false) WorkspaceType type, @RequestParam(required = false) String nameQ, @RequestParam(required = false) String addressQ, @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice) {
        return service.getAllWorkspaces(type, nameQ, addressQ, minPrice, maxPrice);
    }

    @GetMapping("/available")
    public List<Workspace> getAvailableWorkspaces(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startTime, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endTime) {
        return service.getAvailableWorkspaces(startTime, endTime);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "workspaces", key = "#id")
    public Workspace getWorkspaceById(@PathVariable int id) {
        return service.getWorkspaceById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public int createWorkspace(@RequestBody CreateWorkspace workspace) {
        return service.createWorkspace(workspace);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @CacheEvict(value = "workspaces", key = "#id")
    public void editWorkspace(@PathVariable int id, @RequestBody EditWorkspace editWorkspace) {
        service.editWorkspace(id, editWorkspace);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "workspaces", key = "#id")
    public void deleteWorkspace(@PathVariable String id) {
        service.deleteWorkspace(Integer.parseInt(id));
    }
}
