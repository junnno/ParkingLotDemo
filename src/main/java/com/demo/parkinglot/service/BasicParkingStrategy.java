package com.demo.parkinglot.service;

import com.demo.parkinglot.exception.ParkingLotFullException;
import com.demo.parkinglot.parkingslot.ParkingSlot;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasicParkingStrategy implements ParkingStrategy {

    @Override
    public ParkingSlot assignParkingSlot(List<List<ParkingSlot>> slots, int vehicleType, int terminalNumber) throws ParkingLotFullException {
        for (List<ParkingSlot> slot : slots) {
            for (ParkingSlot parkingSlot : slot) {
                if (parkingSlot.isAvailable() && parkingSlot.getSlotSize() >= vehicleType) {
                    return parkingSlot;
                }
            }
            System.out.println("\n");
        }
        throw new ParkingLotFullException();
    }

}
