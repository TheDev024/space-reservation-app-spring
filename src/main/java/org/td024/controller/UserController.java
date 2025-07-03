package org.td024.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.td024.entity.Reservation;
import org.td024.service.ReservationService;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final ReservationService reservationService;

    public UserController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String index(Model model) {
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "user";
    }

    @PostMapping("/cancel-reservation/{id}")
    public String cancelReservation(@PathVariable("id") int id) {
        reservationService.cancelReservation(id);
        return "redirect:/user";
    }
}
