package com.demo.parkinglot.controller;

import com.demo.parkinglot.exception.*;
import com.demo.parkinglot.repository.ParkingTicket;
import com.demo.parkinglot.service.ParkingLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parkinglot")
public class ParkingLotController {
    @Autowired
    ParkingLot parkingLot;

    @PostMapping("/park")
    public ParkingTicket park(@RequestBody ParkingRequest request) throws ParkingLotFullException, InvalidParkingException, InvalidVehicleTypeException, TerminalNotFoundException {
        return parkingLot.park(request.getPlateNumber(), request.getVehicleType(),
                request.getTerminalNumber(), request.getEntryDateTime());
    }


    @PostMapping("/unpark")
    public Double unpark(@RequestBody UnparkRequest request) throws ParkingSlotNotFoundException, InvalidTicketException {
        return parkingLot.unpark(request.getTicketId(), request.getExitDateTime());
    }
}
