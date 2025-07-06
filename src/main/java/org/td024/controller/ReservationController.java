package org.td024.controller;

import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.td024.dto.EditReservation;
import org.td024.dto.MakeReservation;
import org.td024.entity.Reservation;
import org.td024.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> getAllReservations(@RequestParam(required = false) Integer workspaceId) {
        return reservationService.getAllReservations(workspaceId);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "reservations", key = "#id")
    public Reservation getReservationById(@PathVariable("id") int id) {
        return reservationService.getReservationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public int makeReservation(@RequestBody @Valid MakeReservation makeReservation) {
        return reservationService.makeReservation(makeReservation);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @CacheEvict(value = "reservations", key = "#id")
    public void editReservation(@PathVariable("id") int id, @RequestBody @Valid EditReservation editReservation) {
        reservationService.editReservation(id, editReservation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "reservations", key = "#id")
    public void cancelReservation(@PathVariable("id") int id) {
        reservationService.cancelReservation(id);
    }
}
