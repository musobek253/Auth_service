package com.musobek.auth_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomType;
    @Lob
    private Blob photo;
    private BigDecimal roomPrice;
    private boolean isBooked = false;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "room")
    private List<BookedRoom> bookedRooms;

    public  Room(){
        bookedRooms = new ArrayList<>();
    }

    public void addBookedRoom(BookedRoom bookedRoom){
        if (bookedRooms == null){
            bookedRooms = new ArrayList<>();
        }
        bookedRooms.add(bookedRoom);
        bookedRoom.setRoom(this);
        this.isBooked = true;
        String bookingCode = RandomStringUtils.randomNumeric(10);
        bookedRoom.setBookingConfirmationCode(bookingCode);

    }
}
