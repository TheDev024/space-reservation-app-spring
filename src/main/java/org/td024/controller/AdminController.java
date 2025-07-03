package org.td024.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.td024.entity.Reservation;
import org.td024.entity.Workspace;
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
        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("workspaces", workspaces);
        model.addAttribute("reservations", reservations);
        return "admin";
    }
}
