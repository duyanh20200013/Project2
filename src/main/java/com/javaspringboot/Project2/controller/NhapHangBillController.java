package com.javaspringboot.Project2.controller;

import com.javaspringboot.Project2.advice.CustomMapper;
import com.javaspringboot.Project2.advice.HttpResponse;
import com.javaspringboot.Project2.dto.GoodsDTO;
import com.javaspringboot.Project2.dto.GoodsTypeDTO;
import com.javaspringboot.Project2.dto.NhapHangBillDTO;
import com.javaspringboot.Project2.dto.NhapHangBillDetailDTO;
import com.javaspringboot.Project2.exception.ExceptionHandling;
import com.javaspringboot.Project2.model.*;
import com.javaspringboot.Project2.payload.response.AreaResponse;
import com.javaspringboot.Project2.payload.response.GoodsTypeResponse;
import com.javaspringboot.Project2.payload.response.MessageResponse;
import com.javaspringboot.Project2.payload.response.NhapHangBillResponse;
import com.javaspringboot.Project2.repository.*;
import com.javaspringboot.Project2.service.ModelMapperService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600,allowCredentials = "true")
@RestController
@RequestMapping("/api/nhapHang")
public class NhapHangBillController extends ExceptionHandling {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private NhapHangBillRepository nhapHangBillRepository;

    @Autowired
    private NhapHangBillDetailRepository nhapHangBillDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ModelMapperService mapperService;

    @GetMapping("/list")
    public ResponseEntity<List<NhapHangBillResponse>> getAll() {
        List<NhapHangBill> nhapHangBills = nhapHangBillRepository.findAll();
        return new ResponseEntity<>(mapperService.mapList(nhapHangBills,customMapper), OK);
    }

    @GetMapping("/find")
    public ResponseEntity<?> getById(@RequestParam("id") Long id) {
        Optional<NhapHangBill> nhapHangBill = nhapHangBillRepository.findById(id);
        if(!nhapHangBill.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","NhapHangBill is not found!"));
        }else{
            return new ResponseEntity(mapperService.mapObject(nhapHangBill.get(),customMapper),HttpStatus.OK);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(Authentication authentication, @Valid @RequestBody NhapHangBillDTO addNhapHangBillRequest)  {
        String userName = authentication.getName();
        User user = userRepository.findUserByUsername(userName);
        if(user==null){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Nhan vien is not found!"));
        }
        NhapHangBill nhapHangBill = new NhapHangBill(new java.util.Date(),user);
        Set<NhapHangBillDetail> nhapHangBillDetails = new HashSet<>();
        Set<NhapHangBillDetailDTO> nhapHangBillDetailDTOs = addNhapHangBillRequest.getNhapHangBillDetail();
        if(nhapHangBillDetailDTOs.isEmpty()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","NhapHangBillDetail is not empty!"));
        }
        int tongTien =0;
        for(NhapHangBillDetailDTO nhapHangBillDetailDTO: nhapHangBillDetailDTOs){
            Optional<Goods> goods = goodsRepository.findById(nhapHangBillDetailDTO.getGoods_id());
            if(!goods.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                   "","A Goods that does not exist!"));
            }
            tongTien=tongTien+nhapHangBillDetailDTO.getPrice();
            NhapHangBillDetail nhapHangBillDetail = new NhapHangBillDetail(nhapHangBillDetailDTO.getCount(),nhapHangBillDetailDTO.getPrice(),goods.get());
            nhapHangBillDetail.setNhapHangBill(nhapHangBill);
            nhapHangBillDetails.add(nhapHangBillDetail);
        }
        nhapHangBill.setTongTien(tongTien);
        nhapHangBill.setNhapHangBillDetails(nhapHangBillDetails);
        try {
            nhapHangBillRepository.save(nhapHangBill);
            for(NhapHangBillDetailDTO nhapHangBillDetailDTO: nhapHangBillDetailDTOs){
                Optional<Goods> goods = goodsRepository.findById(nhapHangBillDetailDTO.getGoods_id());
                goods.get().setCount(goods.get().getCount()+nhapHangBillDetailDTO.getCount());
                goodsRepository.save(goods.get());
            }
            return new ResponseEntity(new MessageResponse("Create succesfully"), HttpStatus.CREATED);
        } catch (ConstraintViolationException e){
            List<HttpResponse> errors = new ArrayList<>();
            Stream<ConstraintViolation<?>> violationStream = e.getConstraintViolations().stream();
            violationStream.forEach(violation -> {
                errors.add(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, violation.getPropertyPath().toString(), violation.getMessage()));
            });
            return new ResponseEntity(errors.get(0), HttpStatus.BAD_REQUEST);
        }
    }

//    //
    //delete nhapHangBill
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteById(@RequestParam("id") Long id) {
        Optional<NhapHangBill> nhapHangBill = nhapHangBillRepository.findById(id);
        if(!nhapHangBill.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","NhapHangBill is not found"));
        }
        Set<NhapHangBillDetail> nhapHangBillDetails = nhapHangBill.get().getNhapHangBillDetails();
        for(NhapHangBillDetail nhapHangBillDetail:nhapHangBillDetails){
            Goods goods = nhapHangBillDetail.getGoods();
            goods.setCount(goods.getCount()-nhapHangBillDetail.getCount());
            goodsRepository.save(goods);
            nhapHangBillDetailRepository.delete(nhapHangBillDetail);
        }
        nhapHangBillRepository.deleteById(id);
        return new  ResponseEntity(new MessageResponse("Delete succesfully"), OK);
    }

    public CustomMapper<NhapHangBill, NhapHangBillResponse> customMapper = nhapHangBill -> {
        NhapHangBillResponse nhapHangBillResponse = mapper.map(nhapHangBill,NhapHangBillResponse.class);
        return nhapHangBillResponse;
    };


}
