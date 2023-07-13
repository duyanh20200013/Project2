package com.javaspringboot.Project2.controller;

import com.javaspringboot.Project2.advice.CustomMapper;
import com.javaspringboot.Project2.advice.HttpResponse;
import com.javaspringboot.Project2.dto.AreaDTO;
import com.javaspringboot.Project2.dto.BanDTO;
import com.javaspringboot.Project2.enumm.EStatusBan;
import com.javaspringboot.Project2.exception.ExceptionHandling;
import com.javaspringboot.Project2.model.Area;
import com.javaspringboot.Project2.model.Ban;
import com.javaspringboot.Project2.payload.response.AreaResponse;
import com.javaspringboot.Project2.payload.response.MessageResponse;
import com.javaspringboot.Project2.repository.AreaRepository;
import com.javaspringboot.Project2.repository.BanRepository;
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
@RequestMapping("/api/ban")
public class BanController extends ExceptionHandling {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private BanRepository banRepository;

    @Autowired
    private ModelMapperService mapperService;

    @GetMapping("/list")
    public ResponseEntity<List<AreaResponse>> getAll() {
        List<Area> area = areaRepository.findAll();
        return new ResponseEntity<>(mapperService.mapList(area,customMapper), OK);
    }

    @PostMapping("/addArea")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> add(@Valid @RequestBody AreaDTO addAreaRequest) {
        if(areaRepository.existsByName(addAreaRequest.getName())){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Area is already taken!"));
        }
        areaRepository.save(new Area(addAreaRequest.getName(),addAreaRequest.getDescription()));
        return new ResponseEntity(new MessageResponse("Add succesfully"),HttpStatus.CREATED);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> add(@Valid @RequestBody BanDTO addBanRequest) {
        if(banRepository.existsByName(addBanRequest.getName())){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Table is already taken!"));
        }
        Area area= areaRepository.findAreaByName(addBanRequest.getArea_name());
        if(area!=null){
            banRepository.save(new Ban(addBanRequest.getName(),EStatusBan.EMPTY,area));
            return new ResponseEntity(new MessageResponse("Add succesfully"),HttpStatus.CREATED);
        }else{
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Area is not found"));
        }
    }

    @GetMapping("/changeStatus")
    public ResponseEntity<?> updateStatus(@RequestParam("id") Long id) {
        Optional<Ban> ban = banRepository.findById(id);
        if(!ban.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Table is not found"));
        }else{
            if(ban.get().getStatus()==EStatusBan.EMPTY){
                ban.get().setStatus(EStatusBan.FULL);
            }else{
                ban.get().setStatus(EStatusBan.EMPTY);
            }
        }
        banRepository.save(ban.get());
        return new  ResponseEntity(new MessageResponse("Change Status succesfully"), OK);
    }

    //delete Area
    @DeleteMapping("/deleteArea")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAreaById(@RequestParam("id") Long id) {
        Optional<Area> area = areaRepository.findById(id);
        if(!area.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Area is not found"));
        }
        areaRepository.deleteById(id);
        return new  ResponseEntity(new MessageResponse("Delete succesfully"), OK);
    }

    //delete Ban
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteBanById(@RequestParam("ban_id") Long id) {
        Optional<Ban> ban = banRepository.findById(id);
        if(!ban.isPresent()){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Table is not found"));
        }
        banRepository.deleteById(id);
        return new  ResponseEntity(new MessageResponse("Delete succesfully"), OK);
    }

    public CustomMapper<Area, AreaResponse> customMapper = area -> {
        AreaResponse areaResponse = mapper.map(area,AreaResponse.class);
        return areaResponse;
    };


}
