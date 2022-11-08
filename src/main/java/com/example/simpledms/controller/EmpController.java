package com.example.simpledms.controller;

import com.example.simpledms.model.Emp;
import com.example.simpledms.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
//@CrossOrigin(origins ="http://localhost")
@RestController
@RequestMapping("/api")
public class EmpController {

//    CORS 보안 , 웹브라우저 기본 보안
//    한 사이트에서 포트를 하나만 사용해야함 , 포트를 여러개를 사용하면 보안때문에 막힘
//    @CrossOrigin(허용할 사이트주소) : CORS 보안을 풀어줌
//    @CrossOrigin(origins ="http://localhost:8081")  뷰 서버의 포트번호  8081 로 접근하는 것은 풀어줌

    @Autowired
    EmpService empService;

    @GetMapping("/emp")
    public ResponseEntity<Object> getEmpAll(@RequestParam(required = false) String ename,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "3") int size
                                            ){

        try{
            Pageable pageable = PageRequest.of(page,size);


            Page<Emp> empPage= empService.findAllByEnameContaining(ename, pageable);

            Map<String , Object> response = new HashMap<>();
            response.put("emp", empPage.getContent());
            response.put("currentPage", empPage.getNumber());
            response.put("totalItems", empPage.getTotalElements());
            response.put("totalPages", empPage.getTotalPages());

                
            if( empPage.isEmpty()== false){
                return new ResponseEntity<>(response,HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/emp/all")
    public ResponseEntity<Object> removeAll(){
        try{
            empService.removeAll();
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/emp")
    public ResponseEntity<Object> createEmp(@RequestBody Emp emp){
        try{
            Emp emp1 = empService.save(emp);
            return new ResponseEntity<>(emp1,HttpStatus.OK);
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/emp/{eno}")
    public ResponseEntity<Object> findById(@PathVariable int eno){
        try{
            Optional<Emp> optionalEmp = empService.findById(eno);
            if( optionalEmp.isPresent()){
                return new ResponseEntity<>(optionalEmp.get(),HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/emp/{eno}")
    public ResponseEntity<Object> updateEmp(@PathVariable int eno , @RequestBody Emp emp){
        try{
            Emp emp1 = empService.save(emp);
            return new ResponseEntity<>(emp1,HttpStatus.OK);
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping("/emp/deletion/{eno}")
    public ResponseEntity<Object> removeById(@PathVariable int eno){
        try{
            boolean eSuccess = empService.removeById(eno);
            if(eSuccess){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
