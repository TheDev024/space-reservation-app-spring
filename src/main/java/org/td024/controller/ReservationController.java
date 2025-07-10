package org.td024.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.td024.auth.entity.AppUser;
import org.td024.dto.EditReservation;
import org.td024.dto.MakeReservation;
import org.td024.dto.ReservationDto;
import org.td024.entity.Reservation;
import org.td024.mapper.ReservationMapper;
import org.td024.service.ReservationService;

import java.util.Date;
import java.util.List;

@RestController
@SecurityRequirement(name = "bearer")
public class ReservationController {

    private final ReservationService service;
    private final ReservationMapper reservationMapper;

    public ReservationController(ReservationService service, ReservationMapper reservationMapper) {
        this.service = service;
        this.reservationMapper = reservationMapper;
    }

    @GetMapping("/reservations")
    public List<ReservationDto> getAllReservations(
            @RequestParam(required = false) Integer workspaceId,
            @RequestParam(required = false) String nameQ,
            @RequestParam(required = false) Date startTime,
            @RequestParam(required = false) Date endTime,
            @RequestParam(required = false) String username
    ) {
        List<Reservation> reservations = service.getAllReservations(workspaceId, nameQ, startTime, endTime, username);
        return reservations.stream().map(reservationMapper::toDto).toList();
    }

    @GetMapping("/reservations/my")
    public List<ReservationDto> getAllReservations(
            @RequestParam(required = false) Integer workspaceId,
            @RequestParam(required = false) String nameQ,
            @RequestParam(required = false) Date startTime,
            @RequestParam(required = false) Date endTime,
            @AuthenticationPrincipal AppUser principal
    ) {
        List<Reservation> reservations = service.getAllReservations(workspaceId, nameQ, startTime, endTime, principal.getUsername());
        return reservations.stream().map(reservationMapper::toDto).toList();
    }

    @GetMapping("/workspaces/{workspaceId}/reservations")
    public List<ReservationDto> getAllReservationsByWorkspace(@PathVariable int workspaceId, @RequestParam(required = false) String nameQ, @RequestParam(required = false) Date startTime, @RequestParam(required = false) Date endTime) {
        List<Reservation> reservations = service.getAllReservations(workspaceId, nameQ, startTime, endTime, null);
        return reservations.stream().map(reservationMapper::toDto).toList();
    }

    @GetMapping("/workspaces/{workspaceId}/reservations/{id}")
    @Cacheable(value = "reservations", key = "#id")
    public ReservationDto getReservationById(@PathVariable int id, @PathVariable int workspaceId) {
        Reservation reservationById = service.getReservationById(id, workspaceId);
        return reservationMapper.toDto(reservationById);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/workspaces/{workspaceId}/reservations")
    public int makeReservation(@RequestBody @Valid MakeReservation makeReservation, @PathVariable int workspaceId, @AuthenticationPrincipal AppUser principal) {
        return service.makeReservation(workspaceId, makeReservation, principal);
    }

    @PutMapping("/workspaces/{workspaceId}/reservations/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @CacheEvict(value = "reservations", key = "#id")
    public void editReservation(@PathVariable int id, @RequestBody @Valid EditReservation editReservation, @PathVariable int workspaceId) {
        service.editReservation(id, workspaceId, editReservation);
    }

    @DeleteMapping("/workspaces/{workspaceId}/reservations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "reservations", key = "#id")
    public void cancelReservation(@PathVariable int id, @PathVariable int workspaceId) {
        service.cancelReservation(id, workspaceId);
    }
}
