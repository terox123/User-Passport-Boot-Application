package com.UserPassportBoot.repositories;


import com.UserPassportBoot.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // пользователи с младшего по старшего
    Page<User> findAllByOrderByDateOfBirthAsc(Pageable pageable);
    // пользователи со старшего по младшего
    Page<User> findAllByOrderByDateOfBirthDesc(Pageable pageable);
    Optional<User> findByEmail(String email);

    Optional<User> findByPassport_Id(int passportId);

    Optional<User> findByName(@NotBlank(message = "Name can't be empty") String name);
}
