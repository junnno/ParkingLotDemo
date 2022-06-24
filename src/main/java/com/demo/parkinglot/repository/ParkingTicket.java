package com.demo.parkinglot.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.demo.parkinglot.ParkingLotStatics.*;
import static java.time.format.DateTimeFormatter.ofPattern;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "ticket")
public class ParkingTicket {

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

    public Double checkOut(int multiplier, String dateInput) {
        this.setTimeOut(LocalDateTime.parse(dateInput, ofPattern(DATE_TIME_INPUT_FORMAT)));
        double totalAmount = 0;

        totalAmount = isWithinFlatRate() ? FLAT_RATE_FEE  : FLAT_RATE_FEE + getExceedingHours() * multiplier;

        double balance = totalAmount;

        if (amountPaid != null) {
            balance = totalAmount - amountPaid;
        }

        this.setAmountPaid(totalAmount);
        return balance;
    }

    private double getExceedingHours() {
        return Math.ceil(((ChronoUnit.MINUTES.between(this.getTimeIn(), this.getTimeOut())) - FLAT_RATE_MINUTES) / 60D);
    }

    private boolean isWithinFlatRate() {
        return ChronoUnit.MINUTES.between(this.getTimeIn(), this.getTimeOut()) <= FLAT_RATE_MINUTES;
    }

}
