package com.demo.parkinglot.service;

import com.demo.parkinglot.exception.ParkingLotFullException;
import com.demo.parkinglot.parkingslot.ParkingSlot;

import java.util.List;

public interface ParkingStrategy {
    ParkingSlot assignParkingSlot(List<List<ParkingSlot>> slots, int vehicleType, int terminalNumber) throws ParkingLotFullException;
}
