package org.td024.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.td024.entity.Reservation;
import org.td024.exception.NoContentException;
import org.td024.exception.NotFoundException;
import org.td024.model.EditReservation;
import org.td024.model.MakeReservation;
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
    public List<Reservation> getAllReservations(@RequestParam(required = false) Integer workspaceId) throws NoContentException {
        return reservationService.getAllReservations(workspaceId);
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable("id") int id) throws NotFoundException {
        return reservationService.getReservationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public int makeReservation(@RequestBody @Valid MakeReservation makeReservation) throws NotFoundException {
        return reservationService.makeReservation(makeReservation);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void editReservation(@PathVariable("id") int id, @RequestBody @Valid EditReservation editReservation) throws NotFoundException {
        reservationService.editReservation(id, editReservation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservation(@PathVariable("id") int id) {
        reservationService.cancelReservation(id);
    }
}
