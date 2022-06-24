package com.demo.parkinglot.parkingslot;

import java.util.List;
public abstract class BaseParkingSlotFactory {
        public abstract ParkingSlot createParkingSlot(int size, int y, int x, List<Integer> distances);
}
