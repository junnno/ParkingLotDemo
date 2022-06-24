package com.demo.parkinglot.controller;

import lombok.Data;

@Data
public class UnparkRequest {
    private String ticketId;
    private String exitDateTime;
}
