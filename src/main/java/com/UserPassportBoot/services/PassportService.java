package com.UserPassportBoot.services;


import com.UserPassportBoot.model.Passport;
import com.UserPassportBoot.model.User;
import com.UserPassportBoot.repositories.PassportRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PassportService {
    private final PassportRepository passportRepository;


    @Autowired
    public PassportService(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;

    }

    public Page<Passport> allPassports(Pageable pageable) {
        return passportRepository.findAll(pageable);
    }

    public Passport showByIdOfPassport(int id) {
        return passportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Passport with this id "+ id +" not found"));
    }



public Page<Passport> searchPassportByStartingWith(String characters, Pageable pageable){
        return passportRepository.searchPassportByNumberStartingWith(characters, pageable);
}

    public Passport findByOwner(User owner) {
    return passportRepository.findByOwner(owner).
            orElseThrow(() -> new EntityNotFoundException("Passport with owner " + owner + "not found"));

    }


    @Transactional
    public void save(Passport passport) {
        passportRepository.save(passport);
    }

    @Transactional
    public void update(int id, Passport passportToUpdate) {
        passportToUpdate.setId(id);
        passportRepository.save(passportToUpdate);

    }

    @Transactional
    public void delete(int id) {
        passportRepository.deleteById(id);
    }
}
