package com.demo.parkinglot.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.demo.parkinglot.util.ParkingLotStatics.*;
import static com.demo.parkinglot.util.ParkingLotStatics.FLAT_RATE_MINUTES;
import static com.demo.parkinglot.util.ParkingLotUtil.*;
import static java.time.format.DateTimeFormatter.ofPattern;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "ticket")
public class ParkingTicket implements FeeCalculator{

    @Id
    @Column(name = "ticketId")
    String ticketId;

    @Column(name = "parkingSlotId")
    String parkingSlotId;

    @Column(name = "vehicleType")
    int vehicleType;

    @Column(name = "plateNumber")
    String plateNumber;

    @Column(name = "amountPaid")
    Double amountPaid;

    @Column(name = "timeIn")
    LocalDateTime timeIn;

    @Column(name = "timeOut")
    LocalDateTime timeOut;


    public ParkingTicket(String plateNumber, int vehicleType, String parkingSlot,
                         LocalDateTime timeIn) {
        this.plateNumber = plateNumber;
        this.vehicleType = vehicleType;
        this.parkingSlotId = parkingSlot;
        this.timeIn = timeIn;
        this.ticketId = "ticketId" + UUID.randomUUID();
    }

    public Double checkOut(int vehicleTypeFee, String dateInput) {
        this.setTimeOut(LocalDateTime.parse(dateInput, ofPattern(DATE_TIME_INPUT_FORMAT)));
        return this.computeTotal(this, vehicleTypeFee);
    }

    @Override
    public double computeTotal(ParkingTicket ticket, int vehicleTypeFee) {
        double totalAmount = 0;
        double totalTimeMinutes = ChronoUnit.MINUTES.between(timeIn, timeOut);
        double totalTimeDays = Math.floor(ChronoUnit.DAYS.between(timeIn, timeOut));

        double exceedingHoursFee = 0;

        if(totalTimeDays>=1) {
            exceedingHoursFee = getExceedingHours(totalTimeMinutes , totalTimeDays * DAY_IN_MINUTES, vehicleTypeFee);
        } else {
            exceedingHoursFee = isWithinFlatRate(timeIn, timeOut) ? FLAT_RATE_FEE  :
                    FLAT_RATE_FEE + getExceedingHours(totalTimeMinutes, FLAT_RATE_MINUTES, vehicleTypeFee);
        }

        totalAmount = exceedingHoursFee + getDailyPenaltyFee(totalTimeDays);

        double balance = totalAmount;

        if (amountPaid != null) {
            balance = totalAmount - amountPaid;
        }

        this.setAmountPaid(totalAmount);
        return balance;
    }


}
