package org.td024.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mailing-lists")
public class MailingListController {
    private final MailingListService mailingListService;

    public MailingListController(MailingListService mailingListService) {
        this.mailingListService = mailingListService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{mailingListCode}/emails")
    public void addEmail(@PathVariable String mailingListCode, @RequestParam String email) {
        mailingListService.addEmail(mailingListCode, email);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{mailingListCode}/emails")
    public void removeEmail(@PathVariable String mailingListCode, @RequestParam String email) {
        mailingListService.removeEmail(mailingListCode, email);
    }
}
