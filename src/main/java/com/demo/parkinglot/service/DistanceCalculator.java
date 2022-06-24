package com.demo.parkinglot.service;

import com.demo.parkinglot.parkingslot.ParkingSlot;

import java.util.List;
import java.util.Map;

public interface DistanceCalculator {
    List<List<Long>> getDistanceFromTerminals(List<List<ParkingSlot>> parkingSlots, List<ParkingSlot> terminals);
}
