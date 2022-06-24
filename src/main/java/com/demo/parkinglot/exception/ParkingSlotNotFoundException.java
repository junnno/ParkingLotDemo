package com.demo.parkinglot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ParkingSlotNotFoundException extends RuntimeException implements ParkingLotException{
}
