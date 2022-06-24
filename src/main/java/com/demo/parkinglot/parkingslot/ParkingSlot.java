package com.demo.parkinglot.parkingslot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public abstract class ParkingSlot {

    private String slotId;
    private int x;
    private int y;

    private int slotSize;
    private boolean isAvailable;
    private boolean isTerminal;

    private List<Integer> distances;

    public void init(int size, int x, int y, List<Integer> distances) {
        this.slotId = "slotId" + UUID.randomUUID();
        this.x = x;
        this.y = y;
        this.slotSize = size;
        this.distances = distances;
        this.isAvailable = true;
        this.isTerminal = false;
    }

    public abstract int getMultiplier();


}
