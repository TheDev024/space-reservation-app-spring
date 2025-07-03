package org.td024.controller;

import jakarta.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.td024.entity.Reservation;
import org.td024.entity.Workspace;
import org.td024.exception.WorkspaceIsReservedException;
import org.td024.exception.WorkspaceSaveFailed;
import org.td024.model.CreateWorkspace;
import org.td024.service.ReservationService;
import org.td024.service.WorkspaceService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final WorkspaceService workspaceService;
    private final ReservationService reservationService;

    public AdminController(WorkspaceService workspaceService, ReservationService reservationService) {
        this.workspaceService = workspaceService;
        this.reservationService = reservationService;
    }

    @GetMapping
    public String index(Model model) {
        uploadData(model);
        return "admin";
    }

    @PostMapping("/create-workspace")
    public String createWorkspace(Model model, @ModelAttribute @Valid CreateWorkspace createWorkspace) {
        int id = workspaceService.createWorkspace(modelToWorkspace(createWorkspace));
        model.addAttribute("success", "Workspace created successfully! ID: " + id);
        uploadData(model);
        return "admin";
    }

    @PostMapping("/edit-workspace/{id}")
    public String editWorkspace(@ModelAttribute("editWorkspace") @Valid CreateWorkspace createWorkspace, @PathVariable("id") int id, Model model) throws WorkspaceSaveFailed {
        workspaceService.editWorkspace(id, modelToWorkspace(createWorkspace));
        model.addAttribute("success", "Workspace edited successfully!");
        uploadData(model);
        return "admin";
    }

    @PostMapping("/delete-workspace/{id}")
    public String deleteWorkspace(@PathVariable("id") int id, Model model) {
        workspaceService.deleteWorkspace(id);
        model.addAttribute("success", "Workspace deleted successfully!");
        uploadData(model);
        return "admin";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleDuplicateException(ConstraintViolationException e, Model model) {
        System.out.println(e.getLocalizedMessage());
        model.addAttribute("error", "Workspace already exists!");
        uploadData(model);
        return "admin";
    }

    @ExceptionHandler(WorkspaceSaveFailed.class)
    public String handleNotFoundException(WorkspaceSaveFailed e, Model model) {
        System.out.println(e.getLocalizedMessage());
        model.addAttribute("error", "Workspace save failed!");
        uploadData(model);
        return "admin";
    }

    @ExceptionHandler(WorkspaceIsReservedException.class)
    public String handleWorkspaceReservedException(WorkspaceIsReservedException e, Model model) {
        System.out.println(e.getLocalizedMessage());
        model.addAttribute("error", "Reserved Workspaces Cannot Be Deleted!");
        uploadData(model);
        return "admin";
    }

    private void uploadData(Model model) {
        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        List<Reservation> reservations = reservationService.getAllReservations();

        model.addAttribute("workspaces", workspaces);
        model.addAttribute("reservations", reservations);
    }

    private Workspace modelToWorkspace(CreateWorkspace createWorkspace) {
        return new Workspace(createWorkspace.getName(), createWorkspace.getAddress(), createWorkspace.getType(), createWorkspace.getPrice());
    }
}
