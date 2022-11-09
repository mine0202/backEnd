package com.example.simpledms.controller;

import com.example.simpledms.model.Customer;
import com.example.simpledms.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
//@CrossOrigin(origins ="http://localhost")
@RestController
@RequestMapping("/api")
public class CustomerController {

//    CORS 보안 , 웹브라우저 기본 보안
//    한 사이트에서 포트를 하나만 사용해야함 , 포트를 여러개를 사용하면 보안때문에 막힘
//    @CrossOrigin(허용할 사이트주소) : CORS 보안을 풀어줌
//    @CrossOrigin(origins ="http://localhost:8081")  뷰 서버의 포트번호  8081 로 접근하는 것은 풀어줌

    @Autowired
    CustomerService customerService;

//    프론트엔드에서 url(쿼리스트링)  ?  매개변수 전송방식 사용했으면 백엔드에서는 @RequestParam 으로 받음
//    프론트엔드 url (파라메타방식)  /{ }  매개변수 전송방식 사용했으면 백엔드에서는 @PathVariable 으로 받음
    @GetMapping("/customer")
//    @RequestParam 은 title 이 꼭 들어와야하므로 @RequestParam(required = false) 변경해줌
    public ResponseEntity<Object> getCustomerAll(@RequestParam(required = false) String email,
                                             @RequestParam(defaultValue= "0") int page,
                                             @RequestParam(defaultValue = "3") int size
                                             ){
        try{
            Pageable pageable = PageRequest.of(page,size);

            Page<Customer> customerPage =  customerService.findAllByEmailContaining(email, pageable);

            Map<String , Object> response = new HashMap<>();
            response.put("customer", customerPage.getContent());
            response.put("currentPage", customerPage.getNumber());
            response.put("totalItems", customerPage.getTotalElements());
            response.put("totalPages", customerPage.getTotalPages());

            if( customerPage.isEmpty()== false){
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



//    insert 는 PostMapping
    @PostMapping("/customer")
    public ResponseEntity<Object> createCustomer(@RequestBody Customer customer){
        try{
            Customer customer1 = customerService.save(customer);
            return new ResponseEntity<>(customer1, HttpStatus.OK);
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/customer/{cid}")
    public ResponseEntity<Object> getCustomerId(@PathVariable int cid){
        try{
            Optional<Customer> optionalCustomer = customerService.findById(cid);
            if(optionalCustomer.isPresent()){
//                optional 안에 데이터를 .get() 으로 꺼내서 뷰로 보냄
                return new ResponseEntity<>(optionalCustomer.get(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //     update
    @PutMapping("/customer/{cid}")
    public ResponseEntity<Object> updateCustomer(@PathVariable int cid, @RequestBody Customer customer){
        try{
            Customer customer1 = customerService.save(customer);
            return new ResponseEntity<>(customer1, HttpStatus.OK);
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/customer/deletion/{cid}")
    public ResponseEntity<Object> removeCustomerId(@PathVariable int cid){
        try{
            boolean bSuccess = customerService.removeById(cid);
            if ( bSuccess){
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
