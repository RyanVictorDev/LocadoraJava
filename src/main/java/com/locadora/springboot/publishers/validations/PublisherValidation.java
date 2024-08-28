package com.locadora.springboot.publishers.validations;

import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.exceptions.CustomValidationException;
import com.locadora.springboot.publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.springboot.publishers.DTOs.UpdatePublisherRecordDTO;
import com.locadora.springboot.publishers.repositories.PublisherRepository;
import com.locadora.springboot.rents.models.RentStatusEnum;
import com.locadora.springboot.rents.repositories.RentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@AllArgsConstructor
@Component
public class PublisherValidation {

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private BookRepository bookRepository;

    public void validName(CreatePublisherRequestDTO data){
        if (publisherRepository.findByName(data.name())!= null){
            throw new CustomValidationException("Name alredy in use.");
        }
    }

    public void validNameUpdate(UpdatePublisherRecordDTO data){
        if (publisherRepository.findByName(data.name())!= null){
            throw new CustomValidationException("Name alredy in use.");
        }
    }

    public void validEmail(CreatePublisherRequestDTO data){
        if (publisherRepository.findByEmail(data.email()) != null){
            throw new CustomValidationException("Email alredy in use.");
        }
    }

    public void validEmailUpdate(UpdatePublisherRecordDTO data){
        if (publisherRepository.findByEmail(data.email()) != null){
            throw new CustomValidationException("Email alredy in use.");
        }
    }

    public void validTelephone(CreatePublisherRequestDTO data){
        if (publisherRepository.findByTelephone(data.telephone())!= null){
            throw new CustomValidationException("This telephone is alredy in use.");
        }
    }

    public void validTelephoneUpdate(UpdatePublisherRecordDTO data){
        if (publisherRepository.findByTelephone(data.telephone())!= null){
            throw new CustomValidationException("This telephone is alredy in use.");
        }
    }

    public void validSite(CreatePublisherRequestDTO data){
        if (!Objects.equals(data.site(), "")){
            if (publisherRepository.findBySite(data.site()) != null){
                throw new CustomValidationException("This site is already in use");
            }
        }
    }

    public void validSiteUpdate(UpdatePublisherRecordDTO data){
        if (publisherRepository.findBySite(data.site()) != null){
            throw new CustomValidationException("This site is already in use");
        }
    }

    public void validDeletePublisher(int id) {
        var books = bookRepository.findByPublisherId(id);
        for (var book : books) {
            if (rentRepository.existsByBookIdAndStatus(book.getId(), RentStatusEnum.RENTED)) {
                throw new CustomValidationException("Cannot delete publisher. There are books currently rented out.");
            }
        }
    }

}
