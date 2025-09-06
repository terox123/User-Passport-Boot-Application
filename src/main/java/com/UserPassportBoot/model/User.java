package com.UserPassportBoot.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@Entity
@Table(name = "java_users")
public class User {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Setter
    @Min(value = 0)
    @Max(value = 130)
    @Column(name = "age")
    private int age;


    @Setter
    @Column(name = "username")
    @NotBlank(message = "Name can't be empty")
    private String name;

    @Setter
    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Email should be valid")
    @Column(name = "email")
    private String email;

    @Setter
    @Column(name = "created_at", updatable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime createdAt;

@Setter
@Column(name="date_of_birth")
@Past(message = "Date should be in past")
@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

@Setter
@Column(name = "gender")
@NotEmpty(message = "Gender can't be empty")
private String gender;

@Setter
@Column(name = "password", length = 100)  // шифрование bCrypt
private String password;


@Setter
@Getter
@Column(name = "role")
@NotEmpty(message = "Role can't be empty")
private String role;
    // при удалении пользователя паспорт удалится, при обновлении данных, например user, данные в passport
    // также обновятся о user
    @Getter
    @OneToOne(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Passport passport;


    public User() {
        this.createdAt = LocalDateTime.now();
    }


    public User(String name, int age, String email, LocalDate date_of_birth, String gender, String password, String role) {
        this();
        this.name = name;
        this.age = age;
        this.email = email;
        this.dateOfBirth = date_of_birth;
        this.gender = gender;
        this.password = password;
        this.role = role;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
        passport.setOwner(this);
    }

    public String getFormattedCreatedAt() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        return createdAt.format(dateTimeFormatter);
    }

    @Override
    public String toString() {
        return "User  {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Age='" + age + '\'' +
                ", email='" + email + '\'' +
                ", createdAt='" + getFormattedCreatedAt() + '\'' +
                ", Date of Birth='" + dateOfBirth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + '\'' +
                ", Gender='" + gender + '\'' +
                ", Password='" + password + '\'' +
                ", Role='" + role +
                '}';
    }




    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
