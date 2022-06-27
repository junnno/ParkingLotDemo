package com.demo.parkinglot;

import com.demo.parkinglot.exception.*;
import com.demo.parkinglot.repository.ParkingTicket;
import com.demo.parkinglot.parkingslot.ParkingSlot;
import com.demo.parkinglot.repository.ParkingTicketRepository;
import com.demo.parkinglot.service.NearestDistanceCalculator;
import com.demo.parkinglot.service.ParkingLot;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ParkingLotTest {
    @Autowired
    private ParkingLot lot;

    @Autowired
    private NearestDistanceCalculator calculator;

    @Autowired
    private ParkingTicketRepository repository;

    @BeforeEach
    public void init(){
        lot.init();
        lot.printLot();
    }

    @Test
    @Transactional
    public void shouldPayFlatRate_givenCarParkedUnder3Hours() throws RuntimeException {
        String timeIn = "06/24/2022 15:30:00";
        ParkingTicket ticket = lot.park("AAA333",0, 1, timeIn);
        Double amount = lot.unpark(ticket.getTicketId(),"06/24/2022 17:30:00");
        assertEquals(40D, amount);
    }

    @Test
    @Transactional
    public void shouldPayPenalty_givenCarParkedFor26Hours() throws RuntimeException {
        String timeIn = "06/24/2022 15:30:00";
        ParkingTicket ticket = lot.park("AAA333",0, 1, timeIn);
        Double amount = lot.unpark(ticket.getTicketId(),"06/25/2022 17:30:00");
        assertEquals(5120D, amount);
    }

    @Test
    @Transactional
    public void shouldPayPenalty_givenCarParkedFor26AndHalfHours() throws RuntimeException {
        String timeIn = "06/24/2022 15:30:00";
        ParkingTicket ticket = lot.park("AAA333",0, 1, timeIn);
        Double amount = lot.unpark(ticket.getTicketId(),"06/25/2022 18:00:00");
        assertEquals(5180D, amount);
    }

    @Test
    @Transactional
    public void shouldFreeUpLargeParkingSpot_givenVehicleIsUnparked() throws RuntimeException {
        String timeIn = "06/19/2022 21:30:00";
        lot.park("AAA221",2, 1, timeIn);
        lot.park("BBB222",2, 1, timeIn);

        ParkingTicket ticket = lot.park("CCC223",2, 1, timeIn);
        String ticketId = ticket.getTicketId();

        lot.unpark(ticketId,"06/19/2022 23:30:00");

        ParkingTicket ticket2 = lot.park("CCC224",2, 1, timeIn);
        assertEquals("CCC224", ticket2.getPlateNumber());
    }

    @Test
    @Transactional
    public void shouldPayFlatRate_givenContinuousRateUnder3Hours() throws RuntimeException {
        String timeIn = "06/24/2022 15:30:00";
        ParkingTicket ticket = lot.park("AAA333",0, 1, timeIn);
        Double amount = lot.unpark(ticket.getTicketId(),"06/24/2022 17:30:00");
        assertEquals(40D, amount);

        String timeIn2 = "06/24/2022 18:29:59";
        ParkingTicket ticket2 = lot.park("AAA333",0, 2, timeIn2);
        Double amount2 = lot.unpark(ticket2.getTicketId(),"06/24/2022 18:30:00");
        assertEquals(0D, amount2);
    }

    @Test
    @Transactional
    public void shouldPayRemainingBalance_givenContinuousRateOver3Hours() throws RuntimeException {
        //assuming vehicle is assigned SmallParkingSlot as nearest on both park
        String timeIn = "06/24/2022 15:00:00";
        ParkingTicket ticket = lot.park("AAA333",0, 0, timeIn);
        Double amount = lot.unpark(ticket.getTicketId(),"06/24/2022 19:00:00");
        assertEquals(60D, amount);

        String timeIn2 = "06/24/2022 19:59:29";
        ParkingTicket ticket2 = lot.park("AAA333",0, 2, timeIn2);
        Double amount2 = lot.unpark(ticket2.getTicketId(),"06/24/2022 20:00:00");
        assertEquals(20D, amount2);
    }

    @Test
    public void shouldThrowParkingLotFullException_givenNoSpaceForLargeVehicleType() throws RuntimeException {
        String timeIn = "06/19/2022 21:30:00";
        lot.park("AAA222",2, 1, timeIn);
        lot.park("AAA223",2, 2, timeIn);
        lot.park("AAA224",2, 0, timeIn);

        lot.park("BBB225",0, 1, timeIn);
        lot.park("CCC226",1, 2, timeIn);

        assertThrows(ParkingLotFullException.class, () -> lot.park("AAA227",2, 1, timeIn));
    }


    @Ignore
    @Test
    public void testDistanceCalculation(){
        calculator.getDistanceFromTerminals(lot.getParkingSlots(), lot.getTerminalsList());
    }


    @Ignore
    @Test
    public void testSorting() {
        List<List<ParkingSlot>> slots = lot.getParkingSlots();
        List<ParkingSlot> sortedDistancesByFirstSlot = slots.stream().flatMap(Collection::stream)
                .filter(ParkingSlot::isAvailable)
                .sorted(Comparator.comparing(slot ->
                        slot.getDistances().get(0)
                )).collect(Collectors.toList());
    }

}
