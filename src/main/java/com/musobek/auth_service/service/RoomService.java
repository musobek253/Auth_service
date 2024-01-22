package com.musobek.auth_service.service;

import com.musobek.auth_service.entity.Room;
import com.musobek.auth_service.repository.RoomRepository;
import com.musobek.auth_service.service.impl.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RoomService implements IRoomService
{
    private final RoomRepository roomRepository;

    @Override
    public Room addRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) throws IOException, SQLException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if (!photo.isEmpty()){
            byte[] photoByte = photo.getBytes();
            Blob photoBlob = new SerialBlob(photoByte);
            room.setPhoto(photoBlob);
        }
        return roomRepository.save(room);
    }

    @Override
    public List<String> getRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }
}
