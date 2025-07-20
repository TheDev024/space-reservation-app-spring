package org.td024.controller;

import org.springframework.stereotype.Service;
import org.td024.dao.MailingListRepo;
import org.td024.entity.MailingList;
import org.td024.exception.ConflictException;
import org.td024.exception.NotFoundException;
import org.td024.service.EmailService;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@Service
public class MailingListService {
    private final MailingListRepo mailingListRepo;
    private final EmailService emailService;

    public MailingListService(MailingListRepo mailingListRepo, EmailService emailService) {
        this.mailingListRepo = mailingListRepo;
        this.emailService = emailService;
    }

    public MailingList getMailingList(String mailingListCode) {
        Optional<MailingList> mailingList = mailingListRepo.findById(mailingListCode);
        if (mailingList.isEmpty()) throw new NotFoundException("Mailing List Not Found");
        return mailingList.get();
    }

    public void addEmail(String mailingListCode, String email) {
        MailingList mailingList = getMailingList(mailingListCode);
        HashSet<String> members = mailingList.getMembers();
        boolean added = members.add(email);
        if (!added) throw new ConflictException("Email Already Exists In The Mailing List");
        mailingList.setMembers(members);
        mailingListRepo.save(mailingList);
    }

    public void removeEmail(String mailingListCode, String email) {
        MailingList mailingList = getMailingList(mailingListCode);
        HashSet<String> members = mailingList.getMembers();
        members.remove(email);
        mailingListRepo.save(mailingList);
    }

    public void notifyMembers(String mailingListCode, Map<String, String> data) {
        MailingList mailingList = getMailingList(mailingListCode);

        String subject = mailingList.getName();
        String message = mailingList.getTemplate();

        for (String key : data.keySet()) {
            message = message.replace("[[" + key + "]]", data.get(key));
        }

        HashSet<String> members = mailingList.getMembers();

        for (String member : members) {
            emailService.sendEmail(member, subject, message, false);
        }
    }
}
