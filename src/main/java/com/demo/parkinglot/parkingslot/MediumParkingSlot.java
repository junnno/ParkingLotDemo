package com.demo.parkinglot.parkingslot;

public class MediumParkingSlot extends ParkingSlot{

    public MediumParkingSlot() {
        super();
    }

    @Override
    public int getMultiplier() {
        return 60;
    }

    @Override
    public int getSlotSize(){
        return 1;
    }
}
