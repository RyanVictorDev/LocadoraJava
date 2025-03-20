package com.locadora.springboot.dashboard.services;

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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public int getNumberOfRentals(int numberOfMonths){
        List<RentModel> totalRents = rentRepository.findAll();

        int rentsQuantity = (int) totalRents.stream()
                .filter(rent -> !rent.getRentDate().isBefore(LocalDate.now().minusMonths(numberOfMonths))
                        && !rent.getRentDate().isAfter(LocalDate.now())).count();

        return rentsQuantity;
    }

    public int getNumberOfRentalsLate(int numberOfMonths){
        List<RentModel> totalRentsLate = rentRepository.findAllByStatus(RentStatusEnum.LATE);
        int rentsLate = (int) totalRentsLate.stream()
                .filter(rent -> !rent.getRentDate().isBefore(LocalDate.now().minusMonths(numberOfMonths))
                        && !rent.getRentDate().isAfter(LocalDate.now())).count();

        return rentsLate;
    }

    public int getDeliveredInTime(int numberOfMonths){
        List<RentModel> totalRentsInTime = rentRepository.findAllByStatus(RentStatusEnum.IN_TIME);
        int rentsInTime = (int) totalRentsInTime.stream()
                .filter(rent -> !rent.getRentDate().isBefore(LocalDate.now().minusMonths(numberOfMonths))
                        && !rent.getRentDate().isAfter(LocalDate.now())).count();

        return rentsInTime;
    }

    public int getDeliveredWithDelay(int numberOfMonths){
        List<RentModel> totalRentsDeliveredLate = rentRepository.findAllByStatus(RentStatusEnum.DELIVERED_WITH_DELAY);
        int rentsWithDelay = (int) totalRentsDeliveredLate.stream()
                .filter(rent -> !rent.getRentDate().isBefore(LocalDate.now().minusMonths(numberOfMonths))
                        && !rent.getRentDate().isAfter(LocalDate.now())).count();

        return rentsWithDelay;
    }

    public Page<RentsperRenterResponseDTO> getRentsPerRenter(int page) {
        int size = 5;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        List<RenterModel> renters = renterRepository.findAllByIsDeletedFalse(Sort.by(Sort.Direction.DESC, "id"));
        List<RentsperRenterResponseDTO> renterRentList = new ArrayList<>();

        for (RenterModel renter : renters) {
            List<RentModel> rents = rentRepository.findAllByRenterId(renter.getId());
            List<RentModel> rentsActive = rentRepository.findAllByRenterIdAndStatus(renter.getId(), RentStatusEnum.RENTED);
            renterRentList.add(new RentsperRenterResponseDTO(renter.getName(), rents.size(), rentsActive.size()));
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), renterRentList.size());

        Page<RentsperRenterResponseDTO> pageResult = new PageImpl<>(
            renterRentList.subList(start, end), pageable, renterRentList.size()
        );

        return pageResult;
    }

    public List<BooksMoreRented> getBooksMoreRented(int numberOfMonths) {
        return bookRentMapper.toBooksMoreRentedList(bookRepository.findAll(), numberOfMonths);
    }
}
