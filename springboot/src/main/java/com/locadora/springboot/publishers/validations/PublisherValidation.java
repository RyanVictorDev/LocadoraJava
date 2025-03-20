package com.locadora.springboot.publishers.validations;

import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.exceptions.CustomValidationException;
import com.locadora.springboot.publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.springboot.publishers.DTOs.UpdatePublisherRecordDTO;
import com.locadora.springboot.publishers.models.PublisherModel;
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

    public void create (CreatePublisherRequestDTO data){
        validName(data);
        validEmail(data);
        validTelephone(data);
        validSite(data);
    }

    public void update (UpdatePublisherRecordDTO data, int id){
        validNameUpdate(data, id);
        validEmailUpdate(data, id);
        validTelephoneUpdate(data, id);
        validSiteUpdate(data, id);
    }

    private void validName(CreatePublisherRequestDTO data){
        if (data.name() == "" || data.name() == null){
            throw new CustomValidationException("O nome não pode estar vazio.");
        }
        if (publisherRepository.findByNameAndIsDeletedFalse(data.name())!= null){
            throw new CustomValidationException("Nome já em uso.");
        }
    }

    private void validNameUpdate(UpdatePublisherRecordDTO data, int id){
        PublisherModel publisher = publisherRepository.findById(id).get();

        if (data.name() == "" || data.name() == null){
            throw new CustomValidationException("O nome não pode estar vazio.");
        }
        if (!Objects.equals(publisher.getName(), data.name())){
            if (publisherRepository.findByNameAndIsDeletedFalse(data.name())!= null){
                throw new CustomValidationException("Nome já em uso.");
            }
        }
    }

    private void validEmail(CreatePublisherRequestDTO data){
        if (data.email() == "" || data.email() == null){
            throw new CustomValidationException("O email não pode estar vazio.");
        }
        if (publisherRepository.findByEmailAndIsDeletedFalse(data.email()) != null){
            throw new CustomValidationException("E-mail já em uso.");
        }
    }

    private void validEmailUpdate(UpdatePublisherRecordDTO data, int id){
        PublisherModel publisher = publisherRepository.findById(id).get();

        if (data.email() == "" || data.email() == null){
            throw new CustomValidationException("O email não pode estar vazio.");
        }
        if (!Objects.equals(publisher.getEmail(), data.email())){
            if (publisherRepository.findByEmailAndIsDeletedFalse(data.email()) != null){
                throw new CustomValidationException("E-mail já em uso.");
            }
        }
    }

    private void validTelephone(CreatePublisherRequestDTO data){
        if (data.telephone() == "" || data.telephone() == null){
            throw new CustomValidationException("O telefone não pode estar vazio.");
        }
        if (publisherRepository.findByTelephoneAndIsDeletedFalse(data.telephone())!= null){
            throw new CustomValidationException("Este telefone já está em uso.");
        }
    }

    private void validTelephoneUpdate(UpdatePublisherRecordDTO data, int id){
        PublisherModel publisher = publisherRepository.findById(id).get();

        if (data.telephone() == "" || data.telephone() == null){
            throw new CustomValidationException("O telefone não pode estar vazio.");
        }
        if (!Objects.equals(publisher.getTelephone(), data.telephone())){
            if (publisherRepository.findByTelephoneAndIsDeletedFalse(data.telephone())!= null){
                throw new CustomValidationException("Este telefone já está em uso.");
            }
        }
    }

    private void validSite(CreatePublisherRequestDTO data){
        if (!Objects.equals(data.site(), "")){
            if (publisherRepository.findBySiteAndIsDeletedFalse(data.site()) != null){
                throw new CustomValidationException("Este site já está em uso.");
            }
        }
    }

    private void validSiteUpdate(UpdatePublisherRecordDTO data, int id){
        PublisherModel publisher = publisherRepository.findById(id).get();

        if (!Objects.equals(data.site(), "")) {
            if (!Objects.equals(publisher.getSite(), data.site())){
                if (publisherRepository.findBySiteAndIsDeletedFalse(data.site()) != null) {
                    throw new CustomValidationException("Este site já está  em uso.");
                }
            }
        }
    }

    public void validDeletePublisher(int id) {
        var books = bookRepository.findByPublisherId(id);
        for (var book : books) {
            if (rentRepository.existsByBookIdAndStatus(book.getId(), RentStatusEnum.RENTED)) {
                throw new CustomValidationException("Não é possível excluir a editora. Existem livros atualmente alugados.");
            }
            if (rentRepository.existsByBookId(book.getId())){
                throw new CustomValidationException("Não é possível excluir a editora. Existem livros ligados a ela.");
            }
        }
        if (!books.isEmpty()){
            throw new CustomValidationException("Não é possível excluir a editora. Existem livros ligados a ela.");
        }
    }
}
