package com.demo.parkinglot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ParkingLotFullException extends RuntimeException implements ParkingLotException {
}
