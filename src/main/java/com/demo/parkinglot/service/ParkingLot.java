package com.demo.parkinglot.service;


import com.demo.parkinglot.exception.*;
import com.demo.parkinglot.parkingslot.BaseParkingSlotFactory;
import com.demo.parkinglot.parkingslot.ParkingSlot;
import com.demo.parkinglot.repository.ParkingTicket;
import com.demo.parkinglot.repository.ParkingTicketRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.demo.parkinglot.ParkingLotStatics.CONTINUITY_LIMIT_HOURS;
import static com.demo.parkinglot.ParkingLotStatics.DATE_TIME_INPUT_FORMAT;
import static java.time.format.DateTimeFormatter.ofPattern;

@Getter
@Setter
@Service
public class ParkingLot {

    @Value("${lot.length}")
    private int length;
    @Value("${lot.width}")
    private int width;
    @Value("#{'${lot.sizes}'.split(',')}")
    private List<Integer> lotSizesList;

    @Value("#{'${lot.distances}'.split('\\|')}")
    private List<String> distancesPropertyString;

    @Value("#{'${lot.vehicleTypes}'.split(',')}")
    private List<Integer> vehicleTypes;

    @Value("#{'${lot.terminalNames}'.split(',')}")
    private List<Integer> terminalNames;
    @Autowired
    ParkingTicketRepository parkingTicketRepository;

    @Autowired
    @Qualifier("nearestParkingStrategy")
    private ParkingStrategy strategy;

    @Autowired
    BaseParkingSlotFactory parkingSlotFactory;

    List<List<ParkingSlot>> parkingSlots = new ArrayList<>();
    private List<List<Integer>> distancesList = new ArrayList<>();
    private List<ParkingSlot> terminalsList = new ArrayList<>();

    @PostConstruct
    public void init(){
        clearLot();
        initializeDistances();
        initializeParkingSlots();
    }

    private void initializeParkingSlots() {
        for(int x=0, index = 0;x<length;x++) {
            List<ParkingSlot> slotList = new ArrayList<>();
            for(int y=0;y<width;y++){
                ParkingSlot slot = parkingSlotFactory.createParkingSlot(
                        lotSizesList.get(index), y ,x, distancesList.get(index));
                if(slot.isTerminal()) terminalsList.add(slot);
                index++;
                slotList.add(slot);
            }
            parkingSlots.add(slotList);
            if(index == lotSizesList.size()) break;
        }
    }

    private void initializeDistances() {
        distancesList = distancesPropertyString.stream()
                .map(distance -> Arrays.stream(distance.split(","))
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList())
                ).collect(Collectors.toList());
    }

    private void clearLot() {
        parkingSlots = new ArrayList<>();
    }

    public void printLot(){
        parkingSlots.forEach(slotList ->
            slotList.forEach(slot ->
                System.out.print(
                " size:" + slot.getSlotSize()
                + " [" + slot.getX() + ","
                + slot.getY() + "]"
                + " [" + slot.isAvailable() + "]"
                + " \t")
            )
        );
    }

    public synchronized ParkingTicket park(String plateNumber, int vehicleType, int terminalNumber, String entryDateTime) throws ParkingLotFullException, InvalidParkingException, InvalidVehicleTypeException, TerminalNotFoundException {
        validateInput(vehicleType, terminalNumber);

        ParkingSlot parkingSlot = strategy.assignParkingSlot(parkingSlots, vehicleType, terminalNumber);
        LocalDateTime timeIn = LocalDateTime.parse(entryDateTime, ofPattern(DATE_TIME_INPUT_FORMAT));
        ParkingTicket ticket =  parkingTicketRepository.findByPlateNumberAndTimeOutGreaterThan(plateNumber, timeIn.minusHours(CONTINUITY_LIMIT_HOURS));

        if(ticket != null){
            if (isVehicleParked(ticket)) throw new InvalidParkingException();
            ticket.setTimeOut(null);
        } else {
            ticket = new ParkingTicket(plateNumber, vehicleType, parkingSlot.getSlotId(), timeIn);
        }

        parkingSlot.setAvailable(false);
        parkingTicketRepository.save(ticket);
        return ticket;
    }

    private void validateInput(int vehicleType, int terminalNumber) throws InvalidVehicleTypeException, TerminalNotFoundException {
        if(!vehicleTypes.contains(vehicleType)) throw new InvalidVehicleTypeException();
        if(!terminalNames.contains(terminalNumber)) throw new TerminalNotFoundException();
    }

    private boolean isVehicleParked(ParkingTicket ticket) {
        return ticket.getTimeOut() == null;
    }

    public ParkingSlot getParkingSlotById(String id) throws ParkingSlotNotFoundException {
        for(int i=0; i<length; i++){
            for(int j=0; j<width; j++){
                if(id.equalsIgnoreCase(parkingSlots.get(i).get(j).getSlotId())) return parkingSlots.get(i).get(j);
            }
        }
        throw new ParkingSlotNotFoundException();
    }

    public Double unpark(String ticketId, String exitDateTime) throws ParkingSlotNotFoundException, InvalidTicketException {
        Optional<ParkingTicket> parkingTicket = parkingTicketRepository.findById(ticketId);
        if(parkingTicket.isEmpty()) throw new InvalidTicketException();

        ParkingTicket ticket = parkingTicket.get();
        String parkingSlotId = ticket.getParkingSlotId();

        ParkingSlot slot = getParkingSlotById(parkingSlotId);
        Double amount = ticket.checkOut(slot.getMultiplier(), exitDateTime);

        parkingTicketRepository.save(ticket);
        slot.setAvailable(true);
        return amount;
    }

}
