package org.td024.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.td024.auth.entity.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, String> {
}
