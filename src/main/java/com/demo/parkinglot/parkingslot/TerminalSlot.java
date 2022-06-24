package com.demo.parkinglot.parkingslot;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TerminalSlot extends ParkingSlot{

    public TerminalSlot() {
        super();
    }

    @Override
    public int getMultiplier() {
        return 0;
    }

    @Override
    public int getSlotSize() {
        return -1;
    }

    @Override
    public void init(int size, int x, int y, List<Integer> distances) {
        setSlotId("slotId" + UUID.randomUUID());
        setSlotSize(size);
        setX(x);
        setY(y);
        setAvailable(false);
        setTerminal(true);
    }
}
