package org.td024.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.td024.entity.Interval;
import org.td024.entity.Workspace;
import org.td024.exception.InvalidTimeIntervalException;
import org.td024.model.MakeReservation;
import org.td024.service.ReservationService;
import org.td024.service.WorkspaceService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
    private final WorkspaceService workspaceService;
    private final ReservationService reservationService;

    public ReservationController(WorkspaceService workspaceService, ReservationService reservationService) {
        this.workspaceService = workspaceService;
        this.reservationService = reservationService;
    }

    @GetMapping
    public String index(Model model, @RequestParam(value = "startDay", required = false) Integer startDay, @RequestParam(value = "startMonth", required = false) Integer startMonth, @RequestParam(value = "startYear", required = false) Integer startYear, @RequestParam(value = "startHour", required = false) String startHour, @RequestParam(value = "endDay", required = false) Integer endDay, @RequestParam(value = "endMonth", required = false) Integer endMonth, @RequestParam(value = "endYear", required = false) Integer endYear, @RequestParam(value = "endHour", required = false) String endHour) throws ParseException, InvalidTimeIntervalException {
        Date startTime = null;
        if (startDay != null && startMonth != null && startYear != null && startHour != null) {
            startTime = parseDate(startDay, startMonth, startYear, startHour);
        }

        Date endTime = null;
        if (endDay != null && endMonth != null && endYear != null && endHour != null) {
            endTime = parseDate(endDay, endMonth, endYear, endHour);
        }

        if (startTime != null && endTime != null) {
            Interval interval = new Interval.IntervalBuilder().startTime(startTime).endTime(endTime).build();
            List<Workspace> workspaces = workspaceService.getAvailableWorkspaces(interval);

            model.addAttribute("workspaces", workspaces);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            model.addAttribute("startTime", dateFormat.format(startTime));
            model.addAttribute("endTime", dateFormat.format(endTime));
        }

        return "make-reservation";
    }

    @PostMapping("/{workspaceId}")
    public String reserve(@PathVariable("workspaceId") int workspaceId, @Valid @ModelAttribute("makeReservation")MakeReservation makeReservation) throws ParseException, InvalidTimeIntervalException {
        Date startTime = parseDate(makeReservation.getStartTime());
        Date endTime = parseDate(makeReservation.getEndTime());

        Interval interval = new Interval.IntervalBuilder().startTime(startTime).endTime(endTime).build();
        reservationService.makeReservation(makeReservation.getName(), workspaceId, interval);

        return "redirect:/user";
    }

    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable("id") int id) {
        reservationService.cancelReservation(id);
        return "redirect:/user";
    }

    private Date parseDate(int day, int month, int year, String hour) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = String.format("%4d-%02d-%02d %5s", year, month, day, hour);
        return dateFormat.parse(dateStr);
    }

    private Date parseDate(String time) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = String.format(time);
        return dateFormat.parse(dateStr);
    }
}
