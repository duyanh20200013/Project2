package com.javaspringboot.Project2.controller;

import com.javaspringboot.Project2.advice.CustomMapper;
import com.javaspringboot.Project2.advice.HttpResponse;
import com.javaspringboot.Project2.dto.*;
import com.javaspringboot.Project2.enumm.EStatusBan;
import com.javaspringboot.Project2.enumm.EStatusBill;
import com.javaspringboot.Project2.exception.ExceptionHandling;
import com.javaspringboot.Project2.model.*;
import com.javaspringboot.Project2.payload.request.UpdateBanHangBillRequest;
import com.javaspringboot.Project2.payload.response.*;
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
@RequestMapping("/api/banHang")
public class BanHangBillController extends ExceptionHandling {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private BanHangBillRepository banHangBillRepository;

    @Autowired
    private BanHangBillDetailRepository banHangBillDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private BanRepository banRepository;

    @Autowired
    private ModelMapperService mapperService;

    @GetMapping("/list")
    public ResponseEntity<?> getAll(@RequestParam("status") EStatusBill status) {
        List<BanHangBill> banHangBills = banHangBillRepository.findBanHangBillByStatus(status);
        return new ResponseEntity<>(mapperService.mapList(banHangBills,customMapper), OK);
    }

//    @GetMapping("/find")
//    public ResponseEntity<?> getById(@RequestParam("id") Long id) {
//        Optional<NhapHangBill> nhapHangBill = nhapHangBillRepository.findById(id);
//        if(!nhapHangBill.isPresent()){
//            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
//                    "","NhapHangBill is not found!"));
//        }else{
//            return new ResponseEntity(mapperService.mapObject(nhapHangBill.get(),customMapper),HttpStatus.OK);
//        }
//    }
    @PostMapping("/add")
    public ResponseEntity<?> add( @Valid @RequestBody BanHangBillDTO addBanHangBillRequest)  {
        Optional<Ban> ban = banRepository.findById(addBanHangBillRequest.getBan_id());
        if(!ban.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Table is not found"));
        }
        if(ban.get().getStatus() == EStatusBan.FULL){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Table is Full"));
        }
        BanHangBill banHangBill = new BanHangBill(new java.util.Date(),0, EStatusBill.ORDER,ban.get());
        Set<BanHangBillDetail> banHangBillDetails = new HashSet<>();
        Set<BanHangBillDetailDTO> banHangBillDetailDTOs = addBanHangBillRequest.getBanHangBillDetail();
        if(banHangBillDetailDTOs.isEmpty()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","BanHangBillDetail is not empty!"));
        }
        int tongTien=0;
        for(BanHangBillDetailDTO banHangBillDetailDTO:banHangBillDetailDTOs){
            Optional<Goods> goods = goodsRepository.findById(banHangBillDetailDTO.getGoods_id());
            if(!goods.isPresent()){
                return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                        "","A Goods that does not exist!"));
            }
            if(banHangBillDetailDTO.getCount()<=0 || banHangBillDetailDTO.getCount()>goods.get().getCount()){
                return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                        "","Count is not correct!"));
            }
            int price =banHangBillDetailDTO.getCount() * goods.get().getPrice();
            tongTien=tongTien+price;
            BanHangBillDetail banHangBillDetail = new BanHangBillDetail(banHangBillDetailDTO.getCount(),price,goods.get(),EStatusBill.ORDER);
            banHangBillDetail.setBanHangBill(banHangBill);
            banHangBillDetails.add(banHangBillDetail);
        }
        banHangBill.setTongTien(tongTien);
        banHangBill.setDiemTichLuy(tongTien / 100);
        banHangBill.setBanHangBillDetails(banHangBillDetails);
        try {
            Customer customer = customerRepository.findCustomerByPhone(addBanHangBillRequest.getCustomer().getPhone());
            if(customer == null){
                customer = new Customer(addBanHangBillRequest.getCustomer().getName(),addBanHangBillRequest.getCustomer().getPhone(),0);
                customerRepository.save(customer);
            }
            banHangBill.setCustomer(customer);
            ban.get().setStatus(EStatusBan.FULL);
            banRepository.save(ban.get());
            banHangBillRepository.save(banHangBill);
            for(BanHangBillDetailDTO banHangBillDetailDTO:banHangBillDetailDTOs){
                Optional<Goods> goods = goodsRepository.findById(banHangBillDetailDTO.getGoods_id());
                goods.get().setCount(goods.get().getCount()-banHangBillDetailDTO.getCount());
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

    @GetMapping("/updateStatus")
    public ResponseEntity<?> updateStatus(Authentication authentication,@RequestParam("id") Long id, @RequestParam("status") EStatusBill status,@RequestParam("giamGia") int giamGia) {
        Optional<BanHangBill> banHangBill = banHangBillRepository.findById(id);
        if(!banHangBill.isPresent()) {
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "", "BanHangBill is not found!"));
        }if(status == EStatusBill.PREPARE){
               banHangBill.get().setStatus(EStatusBill.PREPARE);
               for(BanHangBillDetail banHangBillDetail : banHangBill.get().getBanHangBillDetails()){
                   if(banHangBillDetail.getStatus()!=EStatusBill.SUCCESS){
                       banHangBillDetail.setStatus(EStatusBill.SUCCESS);
                       banHangBillDetailRepository.save(banHangBillDetail);
                   }
               }
               banHangBillRepository.save(banHangBill.get());
                return new ResponseEntity(new MessageResponse("Update Bill Prepare Successfully"),OK);
            }
        if(status == EStatusBill.CANCEL){
                banHangBill.get().setStatus(EStatusBill.PREPARE);
                banHangBillRepository.save(banHangBill.get());
                for(BanHangBillDetail banHangBillDetail : banHangBill.get().getBanHangBillDetails()){
                   Goods goods = banHangBillDetail.getGoods();
                   goods.setCount(goods.getCount()+banHangBillDetail.getCount());
                   goodsRepository.save(goods);
                }
                return new ResponseEntity(new MessageResponse("Cancel Bill Successfully"), OK);
            }
        String userName = authentication.getName();
        User user = userRepository.findUserByUsername(userName);
        banHangBill.get().setStatus(EStatusBill.SUCCESS);
        banHangBill.get().setUser(user);
        Customer customer = banHangBill.get().getCustomer();
        if(giamGia!=0) {
            banHangBill.get().setGiamGia(giamGia);
        }
        customer.setDiemTichLuy(customer.getDiemTichLuy()-giamGia+banHangBill.get().getDiemTichLuy());
        customerRepository.save(customer);
        Ban ban = banHangBill.get().getBan();
        ban.setStatus(EStatusBan.EMPTY);
        banRepository.save(ban);
        banHangBillRepository.save(banHangBill.get());
        return new ResponseEntity(mapperService.mapObject(banHangBill.get(),customMapper),HttpStatus.OK);
    }

    @GetMapping("/changeTable")
    public ResponseEntity<?> changeTable(@RequestParam("BillId") Long id1,@RequestParam("TableId") Long id2) {
        Optional<BanHangBill> banHangBill = banHangBillRepository.findById(id1);
        if(!banHangBill.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","BanHangBill is not found"));
        }
        Optional<Ban> ban = banRepository.findById(id2);
        if(!ban.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Table is not found"));
        }
        if(ban.get().getStatus()==EStatusBan.FULL){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Table is full!"));
        }
        ban.get().setStatus(EStatusBan.FULL);
        banHangBill.get().getBan().setStatus(EStatusBan.EMPTY);
        banRepository.save(ban.get());
        banRepository.save(banHangBill.get().getBan());
        banHangBill.get().setBan(ban.get());
        banHangBillRepository.save(banHangBill.get());
        return new  ResponseEntity(new MessageResponse("Change Table succesfully"), OK);
    }

    @GetMapping("/mergeBill")
    public ResponseEntity<?> mergeBill(@RequestParam("Bill1") Long id1,@RequestParam("Bill2") Long id2) {
        Optional<BanHangBill> banHangBill1 = banHangBillRepository.findById(id1);
        Optional<BanHangBill> banHangBill2 = banHangBillRepository.findById(id2);
        if(!banHangBill1.isPresent() || !banHangBill2.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","BanHangBill is not found"));
        }
        Set<BanHangBillDetail> banHangBillDetails2=banHangBill2.get().getBanHangBillDetails();
        Set<BanHangBillDetail> banHangBillDetails1=banHangBill1.get().getBanHangBillDetails();
        for(BanHangBillDetail banHangBillDetail:banHangBillDetails2){
            banHangBillDetail.setBanHangBill(banHangBill1.get());
            banHangBillDetails1.add(banHangBillDetail);
        }
        banHangBill2.get().setBanHangBillDetails(null);
        Ban ban = banHangBill2.get().getBan();
        ban.setStatus(EStatusBan.EMPTY);
        banRepository.save(ban);

        banHangBill1.get().setBanHangBillDetails(banHangBillDetails1);
        banHangBill1.get().setTongTien(banHangBill1.get().getTongTien()+banHangBill2.get().getTongTien());
        banHangBill1.get().setDiemTichLuy(banHangBill1.get().getDiemTichLuy()+banHangBill2.get().getDiemTichLuy());
        banHangBillRepository.save(banHangBill2.get());
        banHangBillRepository.delete(banHangBillRepository.findById(id2).get());
        banHangBillRepository.save(banHangBill1.get());
        return new  ResponseEntity(new MessageResponse("Merge Bill succesfully"), OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateById(@Valid @RequestBody UpdateBanHangBillRequest updateBanHangBillRequest) {
        Optional<BanHangBill> banHangBill = banHangBillRepository.findById(updateBanHangBillRequest.getId());
        if(!banHangBill.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","BanHangBill is not found"));
        }
        Set<BanHangBillDetailDTO> banHangBillDetailDTOs = updateBanHangBillRequest.getBanHangBillDetail();
        if(banHangBillDetailDTOs.isEmpty()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","BanHangBillDetail is not empty!"));
        }
        Set<BanHangBillDetail> banHangBillDetails = banHangBill.get().getBanHangBillDetails();
        int tongTien = banHangBill.get().getTongTien();
        for(BanHangBillDetailDTO banHangBillDetailDTO:banHangBillDetailDTOs){
            Optional<Goods> goods = goodsRepository.findById(banHangBillDetailDTO.getGoods_id());
            if(!goods.isPresent()){
                return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                        "","A Goods that does not exist!"));
            }
            if(banHangBillDetailDTO.getCount()<=0 || banHangBillDetailDTO.getCount()>goods.get().getCount()){
                return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                        "","Count is not correct!"));
            }
            int price =banHangBillDetailDTO.getCount() * goods.get().getPrice();
            tongTien=tongTien+price;
            BanHangBillDetail banHangBillDetail = new BanHangBillDetail(banHangBillDetailDTO.getCount(),price,goods.get(),EStatusBill.ORDER);
            banHangBillDetail.setBanHangBill(banHangBill.get());
            banHangBillDetails.add(banHangBillDetail);
        }
        banHangBill.get().setTongTien(tongTien);
        banHangBill.get().setDiemTichLuy(tongTien / 100);
        banHangBill.get().setStatus(EStatusBill.ORDER);
        banHangBill.get().setBanHangBillDetails(banHangBillDetails);
        try {
            banHangBillRepository.save(banHangBill.get());
            for(BanHangBillDetailDTO banHangBillDetailDTO:banHangBillDetailDTOs){
                Optional<Goods> goods = goodsRepository.findById(banHangBillDetailDTO.getGoods_id());
                goods.get().setCount(goods.get().getCount()-banHangBillDetailDTO.getCount());
                goodsRepository.save(goods.get());
            }
            return new ResponseEntity(new MessageResponse("Update succesfully"), HttpStatus.CREATED);
        } catch (ConstraintViolationException e){
            List<HttpResponse> errors = new ArrayList<>();
            Stream<ConstraintViolation<?>> violationStream = e.getConstraintViolations().stream();
            violationStream.forEach(violation -> {
                errors.add(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, violation.getPropertyPath().toString(), violation.getMessage()));
            });
            return new ResponseEntity(errors.get(0), HttpStatus.BAD_REQUEST);
        }
    }


    public CustomMapper<BanHangBill, BanHangBillResponse> customMapper = banHangBill -> {
        BanHangBillResponse banHangBillResponse = mapper.map(banHangBill,BanHangBillResponse.class);
        return banHangBillResponse;
    };


}
