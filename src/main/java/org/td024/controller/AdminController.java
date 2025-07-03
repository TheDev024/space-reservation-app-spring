package org.td024.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.td024.entity.Reservation;
import org.td024.entity.Workspace;
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
        if (id != -1) model.addAttribute("success", "Workspace created successfully! ID: " + id);
        else model.addAttribute("error", "Workspace creation failed!");
        return "redirect:/admin";
    }

    @PostMapping("/edit-workspace/{id}")
    public String editWorkspace(@ModelAttribute("editWorkspace") @Valid CreateWorkspace createWorkspace, @PathVariable("id") int id) throws WorkspaceSaveFailed {
        workspaceService.editWorkspace(id, modelToWorkspace(createWorkspace));
        return "redirect:/admin";
    }

    @PostMapping("/delete-workspace/{id}")
    public String deleteWorkspace(@PathVariable("id") int id) {
        workspaceService.deleteWorkspace(id);
        return "redirect:/admin";
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
