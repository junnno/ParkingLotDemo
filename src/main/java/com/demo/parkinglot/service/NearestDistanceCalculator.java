package com.demo.parkinglot.service;

import com.demo.parkinglot.parkingslot.ParkingSlot;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class NearestDistanceCalculator implements DistanceCalculator{

    @Override
    public List<List<Long>> getDistanceFromTerminals(List<List<ParkingSlot>> parkingSlots, List<ParkingSlot> terminals) {
        List<List<Long>> distanceFromTerminals = new ArrayList<>();
        parkingSlots.forEach(slots -> slots.forEach(slot -> {
            List<Long> distanceList = new ArrayList<>();
            for(ParkingSlot terminal : terminals) {
                Long distance = getDistance(terminal, slot);
                distanceList.add(distance);
            }
            distanceFromTerminals.add(distanceList);
        }));

        for(List<Long> slot : distanceFromTerminals){
            System.out.println(Arrays.toString(slot.toArray()));
        }
        return distanceFromTerminals;
    }

    public Long getDistance(ParkingSlot terminal, ParkingSlot slot){
        return Math.round(Math.hypot(Math.abs(slot.getY() - terminal.getY()), Math.abs(slot.getX() - terminal.getX())));
    }
}
