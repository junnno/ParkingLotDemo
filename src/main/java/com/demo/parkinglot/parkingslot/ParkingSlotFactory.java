package com.demo.parkinglot.parkingslot;

import com.demo.parkinglot.parkingslot.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParkingSlotFactory extends BaseParkingSlotFactory{
    @Override
    public ParkingSlot createParkingSlot(int size, int x, int y, List<Integer> distances) {
        ParkingSlot parkingSlot;
        switch (size) {
            case 0:
                parkingSlot = new SmallParkingSlot();
                break;
            case 1:
                parkingSlot = new MediumParkingSlot();
                break;
            case 2:
                parkingSlot = new LargeParkingSlot();
                break;
            case -1:
                parkingSlot = new TerminalSlot();
                break;
            default: throw new IllegalArgumentException("No such parking lot size.");
        }
        parkingSlot.init(size, x, y, distances);
        return parkingSlot;
    }


}
