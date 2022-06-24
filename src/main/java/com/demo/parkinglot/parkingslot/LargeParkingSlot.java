package com.demo.parkinglot.parkingslot;

public class LargeParkingSlot extends ParkingSlot{

    public LargeParkingSlot() {
        super();
    }

    @Override
    public int getMultiplier() {
        return 100;
    }

    @Override
    public int getSlotSize() {
        return 2;
    }


}
