package com.UserPassportBoot.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "passport")
public class Passport {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "serial")
    @NotEmpty(message = "Serial can't be empty")
    @Size(min = 4, max = 4, message = "Serial must be 4 digits")
    private String serial;

    @Column(name = "number")
    @NotEmpty(message = "Number can't be empty")
    @Size(min = 6, max = 6, message = "Number must be 6 digits")
    private String number;

    @Setter
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @Setter
    @Column(name = "controldigit")
    private int controlDigit;

    @Setter
    @Column(name = "dateofreceipt")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfReceipt;

    @Setter
    @Column(name = "expirationdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    @Setter
    @Getter
    @Column(name = "statusofpassport")
    private String statusOfPassport;

    // при создании объекта сразу назначаются дата получения, дата окончания срока действия паспорта, control digit
    // , дата создания, отображается по паттерну

    public Passport() {
        this.dateOfReceipt = LocalDate.now();
        this.expirationDate = this.dateOfReceipt.plusYears(10);
        if(LocalDate.now().isAfter(expirationDate)){
            statusOfPassport = "Blocked";
        }
        else{
            statusOfPassport = "Active";
        }
    }

    private String setStatusOfPassport(){
        return LocalDate.now().isAfter(expirationDate) ? "Blocked" : "Active";
    }

    public Passport(String serial, String number, User owner) {
        this();
        this.serial = serial;
        this.number = number;
        this.owner = owner;
        this.controlDigit = calculateControlDigit(serial, number);

    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        if (serial == null || serial.length() != 4) {
            throw new IllegalArgumentException("Serial must be 4 digits");
        }
        this.serial = serial;
        this.controlDigit = calculateControlDigit(this.serial, this.number);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        if (number == null || number.length() != 6) {
            throw new IllegalArgumentException("Number must be 6 digits");
        }
        this.number = number;
        this.controlDigit = calculateControlDigit(this.serial, this.number);
    }

    public int getControlDigit() {
        return controlDigit;
    }

    @PrePersist
    @PreUpdate
    private void updateControlDigit() {
        this.controlDigit = calculateControlDigit(this.serial, this.number);
    }

    private int calculateControlDigit(String serial, String number) {
        if (serial == null || number == null || serial.length() != 4 || number.length() != 6) {
            System.out.println("Can't to count control digit, invalid serial and number");
        }

        String coefficients = "731731731";
        assert number != null;
        String fullNumber = serial + number.substring(0, 5);
        int result = 0;

        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(fullNumber.charAt(i));
            int coefficient = Character.getNumericValue(coefficients.charAt(i));
            result += digit * coefficient;
        }

        return result % 10;
    }

    @Override
    public String toString() {
        return "Passport{" +
                "id=" + id +
                ", serial='" + serial + '\'' +
                ", number='" + number + '\'' +
                ", controlDigit=" + controlDigit +
                ", dateOfReceipt='" + getFormattedDateOfReceipt() + '\'' +
                ", expirationDate='" + getFormattedExpirationDate() +
                ", status= '" + statusOfPassport +
                '}';
    }

    public User getOwner() {
        return owner;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDateOfReceipt() {
        return dateOfReceipt;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public String getFormattedDateOfReceipt() {
        return dateOfReceipt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getFormattedExpirationDate() {
        return expirationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }



    @Override
    public boolean equals(Object o){
        if(o == null || getClass() != o.getClass()) return false;
Passport passport =(Passport) o;
return id == passport.id;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}