package com.demo.parkinglot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, String> {
    ParkingTicket findByPlateNumberAndTimeOutGreaterThan(String plateNumber, LocalDateTime timeOut);
}
