package com.example.simpledms.controller;

import com.example.simpledms.model.Faq;
import com.example.simpledms.service.FaqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
//@CrossOrigin(origins ="http://localhost")
@RestController
@RequestMapping("/api")
public class FaqController {

//    CORS 보안 , 웹브라우저 기본 보안
//    한 사이트에서 포트를 하나만 사용해야함 , 포트를 여러개를 사용하면 보안때문에 막힘
//    @CrossOrigin(허용할 사이트주소) : CORS 보안을 풀어줌
//    @CrossOrigin(origins ="http://localhost:8081")  뷰 서버의 포트번호  8081 로 접근하는 것은 풀어줌

    @Autowired
    FaqService faqService;

//    프론트엔드에서 url(쿼리스트링)  ?  매개변수 전송방식 사용했으면 백엔드에서는 @RequestParam 으로 받음
//    프론트엔드 url (파라메타방식)  /{ }  매개변수 전송방식 사용했으면 백엔드에서는 @PathVariable 으로 받음
    @GetMapping("/faq")
//    @RequestParam 은 title 이 꼭 들어와야하므로 @RequestParam(required = false) 변경해줌
    public ResponseEntity<Object> getDeptAll(@RequestParam(required = false) String title){
        try{
            List<Faq> list = Collections.emptyList();  // null 대신 만들어져 있는 초기화용 빈 리스트를 사용
            if( title == null){
        //            1. title 이 null 일 경우 전체검색
                list = faqService.findAll();
            }
        //            2. title 이 값이 있을 경우 부서명검색
            else{
                list = faqService.findAllByTitleContaining(title);
            }

            if( list.isEmpty()== false){
                return new ResponseEntity<>(list,HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/faq/all")
    public ResponseEntity<Object> removeAll(){
        try{
            faqService.removeAll();
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    insert 는 PostMapping
    @PostMapping("/faq")
    public ResponseEntity<Object> createDept(@RequestBody Faq faq){
        try{
            Faq faq1 = faqService.save(faq);
            return new ResponseEntity<>(faq1, HttpStatus.OK);
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/faq/{no}")
    public ResponseEntity<Object> getDeptId(@PathVariable int no){
        try{
            Optional<Faq> optionalDept = faqService.findById(no);
            if(optionalDept.isPresent()){
//                optional 안에 데이터를 .get() 으로 꺼내서 뷰로 보냄
                return new ResponseEntity<>(optionalDept.get(), HttpStatus.OK);
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
    @PutMapping("/faq/{no}")
    public ResponseEntity<Object> updateDept(@PathVariable int no, @RequestBody Faq faq){
        try{
            Faq faq1 = faqService.save(faq);
            return new ResponseEntity<>(faq1, HttpStatus.OK);
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/faq/deletion/{no}")
    public ResponseEntity<Object> removeDeptId(@PathVariable int no){
        try{
            boolean bSuccess = faqService.removeById(no);
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
