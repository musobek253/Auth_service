package com.musobek.auth_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "booked_room")
public class BookedRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    @Column(name = "check_in")
    private LocalDate checkInDate;
    @Column(name = "check_out")
    private LocalDate checkOutDate;
    @Column(name = "Guest_FullName")
    private String guestFullName;
    @Column(name = "Guest_Email")
    private String guestEmail;
    @Column(name = "Adults")
    private int NumberOfAdults;
    @Column(name = "Children")
    private int NumberOfChildren;
    @Column(name = "Total_Guest")
    private int totalNumberOfGuest;
    @Column(name = "Booking_Confirmation_Code")
    private String bookingConfirmationCode;

    @JoinColumn(name = "room_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    public void calculateTotalNumberOfGuest() {
        this.totalNumberOfGuest = this.NumberOfAdults + this.NumberOfChildren;
    }


    public void setNumberOfAdults(int numberOfAdults) {
        NumberOfAdults = numberOfAdults;
        calculateTotalNumberOfGuest();
    }

    public void setNumberOfChildren(int numberOfChildren) {
        NumberOfChildren = numberOfChildren;
        calculateTotalNumberOfGuest();
    }

    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}