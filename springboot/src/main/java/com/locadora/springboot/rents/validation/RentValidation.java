package com.locadora.springboot.rents.validation;

import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.exceptions.CustomValidationException;
import com.locadora.springboot.renters.models.RenterModel;
import com.locadora.springboot.renters.repositories.RenterRepository;
import com.locadora.springboot.rents.DTOs.CreateRentRequestDTO;
import com.locadora.springboot.rents.DTOs.UpdateRentRecordDTO;
import com.locadora.springboot.rents.models.RentModel;
import com.locadora.springboot.rents.models.RentStatusEnum;
import com.locadora.springboot.rents.repositories.RentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@Component
public class RentValidation {

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RentRepository rentRepository;

    public void create(CreateRentRequestDTO data){
        validateRenterId(data);
        validateRentRepeated(data);
        validateRentLate(data);
        validateBookId(data);
        validateDeadLine(data);
    }

    public void update(UpdateRentRecordDTO data, int id){
        validateRenterIdUpdate(data);
        validateRentRepeatedUpdate(data);
        validateRentLateUpdate(data);
        validateBookIdUpdate(data);
        validateDeadLineUpdate(data, id);
    }

    private void validateRenterId(CreateRentRequestDTO data){
        if (renterRepository.findById(data.renterId()).isEmpty()){
            throw new CustomValidationException("Locatário não encontrado");
        }

        RenterModel renter = renterRepository.findById(data.renterId()).get();

        if (renter.isDeleted()){
            throw new CustomValidationException("Locatário não encontrado");
        }
    }

    private void validateRenterIdUpdate(UpdateRentRecordDTO data){
        if (renterRepository.findById(data.renterId()).isEmpty()){
            throw new CustomValidationException("Locatário não encontrado");
        }
    }

    private void validateBookId(CreateRentRequestDTO data){
        if (bookRepository.findById(data.bookId()).isEmpty()){
            throw new CustomValidationException("Livro não encontrado");
        }

        BookModel book = bookRepository.findById(data.bookId()).get();

        if (book.isDeleted()){
            throw new CustomValidationException("Livro não encontrado");
        }
    }

    private void validateBookIdUpdate(UpdateRentRecordDTO data){
        if (bookRepository.findById(data.bookId()).isEmpty()){
            throw new CustomValidationException("Livro não encontrado");
        }
    }

    private void validateDeadLine(CreateRentRequestDTO data){
        if (data.deadLine().isAfter(LocalDate.now().plusDays(30))){
            throw new CustomValidationException("Devolução não pode ser mais de 30 dias.");
        } else if (data.deadLine().isBefore(LocalDate.now())) {
            throw new CustomValidationException("Devolução não pode ser no passado.");
        }
    }

    private void validateDeadLineUpdate(UpdateRentRecordDTO data, int id) {
        RentModel rentPass = rentRepository.findById(id).get();
        if (!Objects.equals(rentPass.getDeadLine(), data.deadLine())){
            if (data.deadLine().isAfter(LocalDate.now().plusDays(30))) {
                throw new CustomValidationException("Devolução não pode ser mais de 30 dias.");
            } else if (data.deadLine().isBefore(LocalDate.now())) {
                throw new CustomValidationException("Devolução não pode ser no passado.");
            }
        }
    }

    public void validateBookTotalQuantity(BookModel data){
        if (data.getTotalQuantity() <= 0){
            throw new CustomValidationException("Não existem livros disponíveis");
        }
    }

    private void validateRentRepeated(CreateRentRequestDTO data){
        if (rentRepository.existsByRenterIdAndBookIdAndStatus(data.renterId(), data.bookId(), RentStatusEnum.RENTED)){
            throw new CustomValidationException("O locatário já alugou este livro.");
        }
    }

    private void validateRentRepeatedUpdate(UpdateRentRecordDTO data){
        if (rentRepository.existsByRenterIdAndBookIdAndStatus(data.renterId(), data.bookId(), RentStatusEnum.RENTED)){
            throw new CustomValidationException("O locatário já alugou este livro.");
        }
    }

    private void validateRentLate(CreateRentRequestDTO data){
        if (rentRepository.existsByRenterIdAndStatus(data.renterId(), RentStatusEnum.LATE)){
            throw new CustomValidationException("Locatário está com aluguel atrasado.");
        }
    }

    private void validateRentLateUpdate(UpdateRentRecordDTO data){
        if (rentRepository.existsByRenterIdAndStatus(data.renterId(), RentStatusEnum.LATE)){
            throw new CustomValidationException("Locatário está com aluguel atrasado.");
        }
    }

    public void deliveredValidate(int id){
        RentModel rent = rentRepository.findById(id).get();
        if (rent.getStatus() == RentStatusEnum.DELIVERED || rent.getStatus() == RentStatusEnum.DELIVERED_WITH_DELAY || rent.getStatus() == RentStatusEnum.IN_TIME){
            throw new CustomValidationException("O aluguel já foi reembolsado");
        }
    }

    public void setRentStatus(RentModel rent){
        if (rent.getDevolutionDate() == null){

            if (rent.getDeadLine().isBefore(LocalDate.now())) {
                rent.setStatus(RentStatusEnum.LATE);
                rentRepository.save(rent);
            } else if (rent.getDevolutionDate() == null) {
                rent.setStatus(RentStatusEnum.RENTED);
                rentRepository.save(rent);
            }

        } else {

            if (rent.getDevolutionDate().isAfter(rent.getDeadLine())) {
                rent.setStatus(RentStatusEnum.DELIVERED_WITH_DELAY);
                rentRepository.save(rent);
            } else {
                rent.setStatus(RentStatusEnum.IN_TIME);
                rentRepository.save(rent);
            }
        }
    }
}
