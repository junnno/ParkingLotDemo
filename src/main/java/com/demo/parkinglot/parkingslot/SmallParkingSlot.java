package com.demo.parkinglot.parkingslot;

public class SmallParkingSlot extends ParkingSlot{

    public SmallParkingSlot() {
        super();
    }

    @Override
    public int getMultiplier() {
        return 20;
    }

    @Override
    public int getSlotSize() {
        return 0;
    }

}
