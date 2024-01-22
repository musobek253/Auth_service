package com.musobek.auth_service.controller;

import com.musobek.auth_service.entity.Room;
import com.musobek.auth_service.response.RoomResponse;
import com.musobek.auth_service.service.impl.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@RequestMapping("/api/v-1/room")
@RequiredArgsConstructor
@RestController
public class RoomController {
    private final IRoomService roomService;

    @PostMapping("/add")
    public ResponseEntity<RoomResponse> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice
    ) throws SQLException, IOException {
        Room saveRoom = roomService.addRoom(photo, roomType, roomPrice);
        RoomResponse roomresponse = new RoomResponse(saveRoom.getId(), saveRoom.getRoomType(), saveRoom.getRoomPrice());
        return ResponseEntity.ok(roomresponse);
    }

    @GetMapping("/getRoomTypes")
    public List<String> getRoomTypes() {
        return roomService.getRoomTypes();
    }

    }