package com.demo.parkinglot.repository;

public interface FeeCalculator {
    double computeTotal(ParkingTicket ticket, int vehicleTypeFee);
}
