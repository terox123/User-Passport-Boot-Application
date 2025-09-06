package com.UserPassportBoot;

import com.UserPassportBoot.model.Passport;
import com.UserPassportBoot.model.User;
import com.UserPassportBoot.repositories.PassportRepository;
import com.UserPassportBoot.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class PassportRepositoryTest {

    @Autowired
    private PassportRepository passportRepository;

    @Autowired
    private UserRepository userRepository;

private User testUser1;
    @BeforeEach
    void setUp() {
        passportRepository.deleteAll();
        userRepository.deleteAll();

         testUser1 = new User("John", 35,"john@example.com", LocalDate.of(1990, 5, 15), "Male", "password123", "USER");
        User testUser2 = new User("Alice", 40,"alice@example.com", LocalDate.of(1985, 8, 22), "Female", "password456", "USER");
        User testUser3 = new User("Bob", 30,"bob@example.com", LocalDate.of(1995, 3, 10), "Male", "password789", "ADMIN");
        userRepository.saveAll(List.of(testUser1, testUser2, testUser3));

        Passport testPassport1 = new Passport("9219", "762108", testUser1);
        Passport testPassport2 = new Passport("9217", "762100", testUser2);
        Passport testPassport3 = new Passport("9215", "762208", testUser3);
        passportRepository.saveAll(List.of(testPassport1, testPassport2, testPassport3));
    }

    @Test
    void findByOwner_ShouldReturnPassport_WhenExists() {
        Optional<Passport> foundPassport = passportRepository.findByOwner(testUser1);

        assertThat(foundPassport).isPresent();
        assertThat(foundPassport.get().getOwner().getName()).isEqualTo("John");
        assertThat(foundPassport.get().getSerial()).isEqualTo("9219");
    }

    @Test
    void findByOwner_ShouldReturnEmpty_WhenNotExists() {
        User newUser = new User("New", 25,"new@example.com", LocalDate.of(2000, 1, 1), "Male", "password", "USER");
        userRepository.save(newUser);

        Optional<Passport> foundPassport = passportRepository.findByOwner(newUser);
        assertThat(foundPassport).isEmpty();
    }

    @Test
    void findAllOrderByOwnerBirthDateAsc_ShouldReturnPassportsInAscendingOrder() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Passport> passportsPage = passportRepository.findAllOrderByOwnerBirthDateAsc(pageable);
        List<Passport> passports = passportsPage.getContent();

        assertThat(passports).hasSize(3);
        assertEquals("Alice", passports.get(0).getOwner().getName()); // 1985 - самый старший
        assertEquals("John", passports.get(1).getOwner().getName());  // 1990
        assertEquals("Bob", passports.get(2).getOwner().getName());   // 1995 - самый младший
    }

    @Test
    void findAllOrderByOwnerBirthDateDesc_ShouldReturnPassportsInDescendingOrder() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Passport> passportsPage = passportRepository.findAllOrderByOwnerBirthDateDesc(pageable);
        List<Passport> passports = passportsPage.getContent();

        assertThat(passports).hasSize(3);
        assertEquals("Bob", passports.get(0).getOwner().getName());   // 1995 - самый младший
        assertEquals("John", passports.get(1).getOwner().getName());  // 1990
        assertEquals("Alice", passports.get(2).getOwner().getName()); // 1985 - самый старший
    }

    @Test
    void searchPassportByNumberStartingWith_ShouldReturnMatchingPassports() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Passport> passportsPage = passportRepository.searchPassportByNumberStartingWith("7621", pageable);
        List<Passport> passports = passportsPage.getContent();

        assertThat(passports).hasSize(2);
        assertThat(passports).extracting(Passport::getNumber)
                .containsExactlyInAnyOrder("762108", "762100");
    }

    @Test
    void searchPassportByNumberStartingWith_ShouldReturnEmpty_WhenNoMatches() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Passport> passportsPage = passportRepository.searchPassportByNumberStartingWith("9999", pageable);
        List<Passport> passports = passportsPage.getContent();

        assertThat(passports).isEmpty();
    }


    @Test
    void findBySerialAndNumber_ShouldReturnUser_WhenExists(){
        Optional<Passport> foundPassport = passportRepository.findBySerialAndNumber("9219", "762108");

        assertThat(foundPassport).isPresent();
        assertThat(foundPassport.get().getOwner().getName()).isEqualTo("John");
    }




}