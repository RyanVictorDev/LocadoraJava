package com.locadora.springboot.dashboard.services;

import com.locadora.springboot.rents.models.RentModel;
import com.locadora.springboot.rents.models.RentStatusEnum;
import com.locadora.springboot.rents.repositories.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServices {

    @Autowired
    RentRepository rentRepository;

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
}
