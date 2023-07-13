package com.javaspringboot.Project2.controller;

import com.javaspringboot.Project2.advice.CustomMapper;
import com.javaspringboot.Project2.advice.HttpResponse;
import com.javaspringboot.Project2.dto.SupplierDTO;
import com.javaspringboot.Project2.exception.ExceptionHandling;
import com.javaspringboot.Project2.exception.domain.UserNotFoundException;
import com.javaspringboot.Project2.model.Supplier;
import com.javaspringboot.Project2.model.User;
import com.javaspringboot.Project2.payload.request.UpdateInforUserRequest;
import com.javaspringboot.Project2.payload.response.MessageResponse;
import com.javaspringboot.Project2.payload.response.SupplierResponse;
import com.javaspringboot.Project2.repository.SupplierRepository;
import com.javaspringboot.Project2.service.ModelMapperService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


import static org.springframework.http.HttpStatus.OK;

@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600,allowCredentials = "true")
@RestController
@RequestMapping("/api/supplier")
public class SupplierController extends ExceptionHandling {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ModelMapperService mapperService;

    @GetMapping("/list")
    public ResponseEntity<List<SupplierResponse>> getAll() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return new ResponseEntity<>(mapperService.mapList(suppliers,customMapper), OK);
    }

    @GetMapping("/find")
    public ResponseEntity<?> findByPhone(@RequestParam String phone){
        Supplier supplier = supplierRepository.findSupplierByPhone(phone);
        if(supplier!=null){
            return new ResponseEntity<>(mapperService.mapObject(supplier,customMapper), OK);
        }else{
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Supplier is not found"));
        }
    }
//
    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody SupplierDTO addSupplierRequest) {
        if(supplierRepository.existsByPhone(addSupplierRequest.getPhone())){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Supplier is already taken!"));
        }
        supplierRepository.save(new Supplier(addSupplierRequest.getName(),addSupplierRequest.getPhone(),addSupplierRequest.getDiaChi()));
        return new ResponseEntity(new MessageResponse("Add succesfully"),HttpStatus.CREATED);
    }
//
    @PutMapping("/update")
    public ResponseEntity<?> updateById(@RequestParam("id") Long id,@Valid @RequestBody SupplierDTO supplier) {
        Optional<Supplier> _supplier = supplierRepository.findById(id);
        if(!_supplier.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Supplier is not found"));
        } else {
            _supplier.get().setName(supplier.getName());
            _supplier.get().setPhone(supplier.getPhone());
            _supplier.get().setDiaChi(supplier.getDiaChi());
            supplierRepository.save(_supplier.get());
            return new ResponseEntity(new MessageResponse("Update succesfully"), OK);
        }
    }
//
    //delete supplier
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteById(@RequestParam("id") Long id){
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if(!supplier.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Supplier is not found!"));
        }
        supplierRepository.deleteById(id);
        return new  ResponseEntity(new MessageResponse("Delete succesfully"), OK);

    }
//
    public CustomMapper<Supplier, SupplierResponse> customMapper = supplier -> {
        SupplierResponse supplierResponse = mapper.map(supplier,SupplierResponse.class);
        return supplierResponse;
    };


}