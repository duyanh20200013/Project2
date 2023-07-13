package com.javaspringboot.Project2.controller;

import com.javaspringboot.Project2.advice.CustomMapper;
import com.javaspringboot.Project2.advice.HttpResponse;
import com.javaspringboot.Project2.dto.GoodsDTO;
import com.javaspringboot.Project2.dto.GoodsTypeDTO;
import com.javaspringboot.Project2.exception.ExceptionHandling;
import com.javaspringboot.Project2.model.*;
import com.javaspringboot.Project2.payload.response.AreaResponse;
import com.javaspringboot.Project2.payload.response.GoodsTypeResponse;
import com.javaspringboot.Project2.payload.response.MessageResponse;
import com.javaspringboot.Project2.repository.BanRepository;
import com.javaspringboot.Project2.repository.GoodsRepository;
import com.javaspringboot.Project2.repository.GoodsTypeRepository;
import com.javaspringboot.Project2.repository.SupplierRepository;
import com.javaspringboot.Project2.service.ModelMapperService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600,allowCredentials = "true")
@RestController
@RequestMapping("/api/goods")
public class GoodsController extends ExceptionHandling {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private GoodsTypeRepository goodsTypeRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ModelMapperService mapperService;

    @GetMapping("/list")
    public ResponseEntity<List<GoodsTypeResponse>> getAll() {
        List<GoodsType> goodsTypes = goodsTypeRepository.findAll();
        return new ResponseEntity<>(mapperService.mapList(goodsTypes,customMapper), OK);
    }

    @PostMapping("/addGoodsType")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> add(@Valid @RequestBody GoodsTypeDTO addGoodsTypeRequest) {
        if(goodsTypeRepository.existsByName(addGoodsTypeRequest.getName())){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","GoodsType is already taken!"));
        }
        goodsTypeRepository.save(new GoodsType(addGoodsTypeRequest.getName(),addGoodsTypeRequest.getDescription()));
        return new ResponseEntity(new MessageResponse("Add succesfully"),HttpStatus.CREATED);
    }
//
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> add(@Valid @RequestBody GoodsDTO addGoodsRequest) {
        if(goodsRepository.existsByName(addGoodsRequest.getName())){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Goods is already taken!"));
        }
        GoodsType goodsType= goodsTypeRepository.findGoodsTypeByName(addGoodsRequest.getGoodsType_name());
        Optional<Supplier> supplier = supplierRepository.findById(addGoodsRequest.getSupplier_id());
        if(goodsType==null) {
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "", "GoodsType is not found"));
        }
        if(!supplier.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "", "Supplier is not found"));
        }
        goodsRepository.save(new Goods(addGoodsRequest.getName(),addGoodsRequest.getCount(),addGoodsRequest.getPrice()
                ,addGoodsRequest.getImage(),goodsType,supplier.get()));
        return new ResponseEntity(new MessageResponse("Add succesfully"),HttpStatus.CREATED);
    }
//
    @GetMapping("/changeCount")
    public ResponseEntity<?> updateCount(@RequestParam("id") Long id, @RequestParam int count,@RequestParam String method ) {
        Optional<Goods> goods = goodsRepository.findById(id);
        if(!goods.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Goods is not found"));
        }else{
            if(method.equals("Add")){
                goods.get().setCount(goods.get().getCount()+count);
            }else if(method.equals("Sub")){
                if(goods.get().getCount()<count){
                    return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                            "","Count Goods not enough"));
                }else{
                    goods.get().setCount(goods.get().getCount()-count);
                }
            }else{
                return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                        "","Method is belog Add or Sub"));
            }
        }
        goodsRepository.save(goods.get());
        return new  ResponseEntity(new MessageResponse("Change Count Goods succesfully"), OK);
    }
//
    //delete GoodsType
    @DeleteMapping("/deleteGoodsType")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteGoodsTypeById(@RequestParam("id") Long id) {
        Optional<GoodsType> goodsType = goodsTypeRepository.findById(id);
        if(!goodsType.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","GoodsType is not found"));
        }
        goodsTypeRepository.deleteById(id);
        return new  ResponseEntity(new MessageResponse("Delete succesfully"), OK);
    }
//
    //delete Goods
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteGoodsById(@RequestParam("id") Long id) {
        Optional<Goods> goods = goodsRepository.findById(id);
        if(!goods.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Goods is not found"));
        }
        goodsRepository.deleteById(id);
        return new  ResponseEntity(new MessageResponse("Delete succesfully"), OK);
    }

    public CustomMapper<GoodsType, GoodsTypeResponse> customMapper = goodsType -> {
        GoodsTypeResponse goodsTypeResponse = mapper.map(goodsType,GoodsTypeResponse.class);
        return goodsTypeResponse;
    };


}
