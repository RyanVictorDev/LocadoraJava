package com.locadora.springboot.dashboard.services;

import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.dashboard.DTOs.BooksMoreRented;
import com.locadora.springboot.dashboard.DTOs.RentsperRenterResponseDTO;
import com.locadora.springboot.dashboard.mappers.BookRentMapper;
import com.locadora.springboot.renters.models.RenterModel;
import com.locadora.springboot.renters.repositories.RenterRepository;
import com.locadora.springboot.rents.models.RentModel;
import com.locadora.springboot.rents.models.RentStatusEnum;
import com.locadora.springboot.rents.repositories.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServices {

    @Autowired
    RentRepository rentRepository;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    private BookRentMapper bookRentMapper;

    public int getNumberOfRentals(){
        List<RentModel> totalRents = rentRepository.findAll();
        int rentsQuantity = totalRents.size();

        return rentsQuantity;
    }

    public int getNumberOfRentalsLate(){
        List<RentModel> totalRentsLate = rentRepository.findAllByStatus(RentStatusEnum.LATE);
        int rentsLate = totalRentsLate.size();

        return rentsLate;
    }

    public int getDeliveredInTime(){
        List<RentModel> totalRentsLate = rentRepository.findAllByStatus(RentStatusEnum.IN_TIME);
        int rentsInTime = totalRentsLate.size();

        return rentsInTime;
    }

    public int getDeliveredWithDelay(){
        List<RentModel> totalRentsLate = rentRepository.findAllByStatus(RentStatusEnum.DELIVERED_WITH_DELAY);
        int rentsWithDelay = totalRentsLate.size();

        return rentsWithDelay;
    }

    public List<RentsperRenterResponseDTO> getRentsPerRenter() {
        List<RenterModel> renters = renterRepository.findAll();
        List<RentsperRenterResponseDTO> renterRentList = new ArrayList<>();

        for (RenterModel renter : renters) {
            List<RentModel> rents = rentRepository.findAllByRenterId(renter.getId());
            List<RentModel> rentsActive = rentRepository.findAllByRenterIdAndStatus(renter.getId(), RentStatusEnum.RENTED);
            renterRentList.add(new RentsperRenterResponseDTO(renter.getName(), rents.size(), rentsActive.size()));
        }

        return renterRentList;
    }

    public List<BooksMoreRented> getBooksMoreRented() {
        return bookRentMapper.toBooksMoreRentedList(bookRepository.findAll());
    }
}
