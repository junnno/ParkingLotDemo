package com.demo.parkinglot.service;

import com.demo.parkinglot.exception.ParkingLotFullException;
import com.demo.parkinglot.parkingslot.ParkingSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NearestParkingStrategy implements ParkingStrategy{

    @Override
    public ParkingSlot assignParkingSlot(List<List<ParkingSlot>> slots, int vehicleType, int terminalNumber) throws ParkingLotFullException {
        List<ParkingSlot> sortedDistances = slots.stream().flatMap(Collection::stream)
                .filter(ParkingSlot::isAvailable)
                .filter(slot -> slot.getSlotSize() >= vehicleType)
                .sorted(Comparator.comparing(slot ->
                        slot.getDistances().get(terminalNumber)
                )).collect(Collectors.toList());

        if(sortedDistances.isEmpty()) throw new ParkingLotFullException();

        return sortedDistances.get(0);
    }


}
