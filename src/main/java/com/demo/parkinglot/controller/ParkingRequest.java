package com.demo.parkinglot.controller;

import lombok.Data;

@Data
public class ParkingRequest {
    private String ticketId;
    private String plateNumber;
    private Integer vehicleType;
    private Integer terminalNumber;
    private String entryDateTime;
    private String exitDateTime;
}
