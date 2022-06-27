package com.demo.parkinglot.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.demo.parkinglot.util.ParkingLotStatics.DAY_IN_MINUTES;
import static com.demo.parkinglot.util.ParkingLotStatics.FLAT_RATE_MINUTES;

public class ParkingLotUtil {

    public static double getDailyPenaltyFee(double totalDays) {
        return totalDays * 5000;
    }

    public static double getExceedingHours(double totalTimeMinutes, double minimum, int vehicleTypeFee) {
        return Math.ceil(((totalTimeMinutes) - minimum) / 60D) * vehicleTypeFee;
    }

    public static boolean isWithinFlatRate(LocalDateTime timeIn, LocalDateTime timeOut) {
        return ChronoUnit.MINUTES.between(timeIn, timeOut) <= FLAT_RATE_MINUTES;
    }

    public static double daysToMinutes(double days){
        return days * DAY_IN_MINUTES;
    }
}
