package com.musobek.auth_service.response;



import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
;
import java.util.Base64;
import java.util.List;
@Data
public class RoomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked;
    private String photo;
     private List<BookingResponse> bookingResponses;

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice,
                        boolean isBooked, byte[] photo, List<BookingResponse> bookingResponses) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = isBooked;
        this.photo = photo != null ? Base64.getEncoder().encodeToString(photo): null;
        this.bookingResponses = bookingResponses;
    }
}
