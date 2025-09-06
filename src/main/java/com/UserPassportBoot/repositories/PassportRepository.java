package com.UserPassportBoot.repositories;


import com.UserPassportBoot.model.Passport;
import com.UserPassportBoot.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Integer> {
    Optional<Passport> findByOwner(User owner);

    @Query("SELECT p from Passport p join p.owner u order by u.dateOfBirth ASC")
    Page<Passport> findAllOrderByOwnerBirthDateAsc(Pageable pageable);

    @Query("select p from Passport p join p.owner u order by u.dateOfBirth DESC")
    Page<Passport> findAllOrderByOwnerBirthDateDesc(Pageable pageable);

    // поиск по части номера паспорта или целому номера
    @Query("select p from Passport p where p.number like concat(:numberPart, '%')")
    Page<Passport> searchPassportByNumberStartingWith(@Param("numberPart") String numberPart, Pageable pageable);

    Optional<Passport> findBySerialAndNumber(String serial, String number);
}
