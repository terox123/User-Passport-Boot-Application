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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PassportRepository passportRepository;

    @BeforeEach
    void setUp() {
userRepository.deleteAll();
passportRepository.deleteAll();

        User testUser1 = new User("John", 35,"john@example.com", LocalDate.of(1990, 5, 15), "Male", "password123", "USER");
        User testUser2 = new User("Alice", 40,"alice@example.com", LocalDate.of(1985, 8, 22), "Female", "password456", "USER");
        User testUser3 = new User("Bob", 30,"bob@example.com", LocalDate.of(1995, 3, 10), "Male", "password789", "ADMIN");


        userRepository.saveAll(List.of(testUser1, testUser2, testUser3));
        Passport testPassport1 = new Passport("9219", "762108", testUser1);
        passportRepository.save(testPassport1);
    }


    @Test
    void findByName_ShouldReturnUser_WhenUserExists() {
        Optional<User> foundUser = userRepository.findByName("John");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("John");
        assertThat(foundUser.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenUserDoesNotExist() {
        Optional<User> foundUser = userRepository.findByName("NonExistentUser");

        assertThat(foundUser).isEmpty();
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenEmailExists() {
        Optional<User> foundUser = userRepository.findByEmail("alice@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("alice@example.com");
        assertThat(foundUser.get().getName()).isEqualTo("Alice");
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenEmailDoesNotExist() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertThat(foundUser).isEmpty();
    }

    @Test
    void findAllByOrderByDateOfBirthAsc_ShouldReturnUsersInAscendingOrder() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> usersPage = userRepository.findAllByOrderByDateOfBirthAsc(pageable);
        List<User> users = usersPage.getContent();

        assertEquals(3, users.size());
        assertEquals("Alice", users.get(0).getName()); // 1985
        assertEquals("John", users.get(1).getName());  // 1990
        assertEquals("Bob", users.get(2).getName());   // 1995
    }

    @Test
    void findAllByOrderByDateOfBirthDesc_ShouldReturnUsersInDescendingOrder() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> usersPage = userRepository.findAllByOrderByDateOfBirthDesc(pageable);
        List<User> users = usersPage.getContent();

        assertEquals(3, users.size());
        assertEquals("Bob", users.get(0).getName());   // 1995
        assertEquals("John", users.get(1).getName());  // 1990
        assertEquals("Alice", users.get(2).getName()); // 1985
    }

    @Test
    void findAllByOrderByDateOfBirthAsc_ShouldReturnEmptyPage_WhenNoUsersExist() {
        userRepository.deleteAll();
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> usersPage = userRepository.findAllByOrderByDateOfBirthAsc(pageable);

        assertThat(usersPage.getContent().size()).isEqualTo(0);
        assertThat(usersPage.getTotalElements()).isEqualTo(0);
    }

    @Test
    void findAllByOrderByDateOfBirthDesc_ShouldReturnEmptyPage_WhenNoUsersExist() {
        userRepository.deleteAll();
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> usersPage = userRepository.findAllByOrderByDateOfBirthDesc(pageable);

        assertThat(usersPage.getContent().size()).isEqualTo(0);
        assertThat(usersPage.getTotalElements()).isEqualTo(0);
    }

    @Test
    void save_ShouldCreateNewUser() {
        User newUser = new User("NewUser", 25,"new@example.com",  LocalDate.of(2000, 1, 1), "Female", "password", "USER");

        User savedUser = userRepository.save(newUser);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("NewUser");
        assertThat(userRepository.count()).isEqualTo(4);
    }

    @Test
    void update_ShouldUpdateExistingUser() {
        User userToUpdate = userRepository.findByName("John").get();
        userToUpdate.setName("JohnUpdated");
        userToUpdate.setEmail("john.updated@example.com");

        User updatedUser = userRepository.save(userToUpdate);

        assertThat(updatedUser.getName()).isEqualTo("JohnUpdated");
        assertThat(updatedUser.getEmail()).isEqualTo("john.updated@example.com");

        Optional<User> foundUser = userRepository.findByName("JohnUpdated");
        assertThat(foundUser).isPresent();
    }

    @Test
    void delete_ShouldRemoveUser() {
        User userToDelete = userRepository.findByName("Bob").get();
        long initialCount = userRepository.count();

        userRepository.delete(userToDelete);

        assertThat(userRepository.count()).isEqualTo(initialCount - 1);
        assertThat(userRepository.findByName("Bob")).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllUsers() {

        List<User> allUsers = userRepository.findAll();


        assertThat(allUsers.size()).isEqualTo(3);
        assertTrue(allUsers.stream().anyMatch(user -> user.getName().equals("John")));
        assertTrue(allUsers.stream().anyMatch(user -> user.getName().equals("Alice")));
        assertTrue(allUsers.stream().anyMatch(user -> user.getName().equals("Bob")));
    }

    @Test
    void findById_ShouldReturnUser_WhenIdExists() {

        User user = userRepository.findByName("John").get();

        Optional<User> foundUser = userRepository.findById(user.getId());


        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("John");
    }

    @Test
    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {

        Optional<User> foundUser = userRepository.findById(999);


        assertThat(foundUser).isEmpty();
    }

    @Test
    void existsById_ShouldReturnTrue_WhenUserExists() {

        User user = userRepository.findByName("Alice").get();


        boolean exists = userRepository.existsById(user.getId());


        assertThat(exists).isTrue();
    }

    @Test
    void existsById_ShouldReturnFalse_WhenUserDoesNotExist() {

        boolean exists = userRepository.existsById(999);


        assertThat(exists).isFalse();
    }


    @Test
    void findByPassportId_ShouldReturnUser_WhenUserExists(){
        Passport passport = passportRepository.findAll().get(0);
        Optional<User> found = userRepository.findByPassport_Id(passport.getId());

        assertThat(found).isPresent();


    }

    @Test
    void findByPassportId_ShouldReturnEmpty_WhenPassportDoesNotExist(){
        Optional<User> found = userRepository.findByPassport_Id(999999);
        assertThat(found).isEmpty();
    }


}