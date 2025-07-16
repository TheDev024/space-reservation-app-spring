package org.td024.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.td024.entity.MailingList;

public interface MailingListRepo extends JpaRepository<MailingList, String> {
}
