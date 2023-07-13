package com.javaspringboot.Project2.controller;

import com.javaspringboot.Project2.advice.CustomMapper;
import com.javaspringboot.Project2.advice.HttpResponse;
import com.javaspringboot.Project2.dto.CustomerDTO;
import com.javaspringboot.Project2.exception.ExceptionHandling;
import com.javaspringboot.Project2.exception.domain.CustomerNotFoundException;
import com.javaspringboot.Project2.exception.domain.UserNotFoundException;
import com.javaspringboot.Project2.model.Customer;
import com.javaspringboot.Project2.model.User;
import com.javaspringboot.Project2.payload.request.UpdateInforUserRequest;
import com.javaspringboot.Project2.payload.response.CustomerResponse;
import com.javaspringboot.Project2.payload.response.MessageResponse;
import com.javaspringboot.Project2.repository.CustomerRepository;
import com.javaspringboot.Project2.service.ModelMapperService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600,allowCredentials = "true")
@RestController
@RequestMapping("/api/customer")
public class CustomerController extends ExceptionHandling {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapperService mapperService;

    @GetMapping("/list")
    public ResponseEntity<List<CustomerResponse>> getAll() {
        List<Customer> customers = customerRepository.findAll();
        return new ResponseEntity<>(mapperService.mapList(customers,customMapper), OK);
    }

    @GetMapping("/find")
    public ResponseEntity<?> findByPhone(@RequestParam String phone) throws CustomerNotFoundException {
        Customer customer = customerRepository.findCustomerByPhone(phone);
        if(customer!=null){
            return new ResponseEntity<>(mapperService.mapObject(customer,customMapper), OK);
        }else{
            throw new CustomerNotFoundException(phone);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody CustomerDTO addCustomerRequest) {
        if(customerRepository.existsByPhone(addCustomerRequest.getPhone())){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Customer is already taken!"));
        }
        customerRepository.save(new Customer(addCustomerRequest.getName(),addCustomerRequest.getPhone(),0));
        return new ResponseEntity(new MessageResponse("Add succesfully"),HttpStatus.CREATED);
    }

    //delete customer
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteById(@RequestParam("id") Long id) throws CustomerNotFoundException{
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer==null){
            throw new CustomerNotFoundException(id.toString());
        }
        customerRepository.deleteById(id);
        return new  ResponseEntity(new MessageResponse("Delete succesfully"), OK);

    }

    public CustomMapper<Customer, CustomerResponse> customMapper = customer -> {
        CustomerResponse customerResponse = mapper.map(customer,CustomerResponse.class);
        return customerResponse;
    };


}
