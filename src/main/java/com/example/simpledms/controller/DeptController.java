package com.example.simpledms.controller;

import com.example.simpledms.model.Dept;
import com.example.simpledms.service.DeptService;
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
public class DeptController {

//    CORS 보안 , 웹브라우저 기본 보안
//    한 사이트에서 포트를 하나만 사용해야함 , 포트를 여러개를 사용하면 보안때문에 막힘
//    @CrossOrigin(허용할 사이트주소) : CORS 보안을 풀어줌
//    @CrossOrigin(origins ="http://localhost:8081")  뷰 서버의 포트번호  8081 로 접근하는 것은 풀어줌

    @Autowired
    DeptService deptService;

//    프론트엔드에서 url(쿼리스트링)  ?  매개변수 전송방식 사용했으면 백엔드에서는 @RequestParam 으로 받음
//    프론트엔드 url (파라메타방식)  /{ }  매개변수 전송방식 사용했으면 백엔드에서는 @PathVariable 으로 받음
@GetMapping("/dept")
public ResponseEntity<Object> getDeptAll(@RequestParam(required = false) String dname,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "3") int size
) {

    try {
//            Pageable 객체 정의 ( page, size 값 설정 )
        Pageable pageable = PageRequest.of(page, size);

//            Page 객체 정의
        Page<Dept> deptPage= deptService.findAllByDnameContaining(dname, pageable);

        //                페이징 처리되는 findAllByDnameContaining()



//            맵 자료구조에 넣어서 전송
        Map<String, Object> response = new HashMap<>();
        response.put("dept", deptPage.getContent());
        response.put("currentPage", deptPage.getNumber());
        response.put("totalItems", deptPage.getTotalElements());
        response.put("totalPages", deptPage.getTotalPages());

        if (deptPage.isEmpty() == false) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    } catch (Exception e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}




    @DeleteMapping("/dept/all")
    public ResponseEntity<Object> removeAll(){
        try{
            deptService.removeAll();
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    insert 는 PostMapping
    @PostMapping("/dept")
    public ResponseEntity<Object> createDept(@RequestBody Dept dept){
        try{
            Dept dept1 = deptService.save(dept);
            return new ResponseEntity<>(dept1, HttpStatus.OK);
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/dept/{dno}")
    public ResponseEntity<Object> getDeptId(@PathVariable int dno){
        try{
            Optional<Dept> optionalDept = deptService.findById(dno);
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
    @PutMapping("/dept/{dno}")
    public ResponseEntity<Object> updateDept(@PathVariable int dno, @RequestBody Dept dept){
        try{
            Dept dept1 = deptService.save(dept);
            return new ResponseEntity<>(dept1, HttpStatus.OK);
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/dept/deletion/{dno}")
    public ResponseEntity<Object> removeDeptId(@PathVariable int dno){
        try{
            boolean bSuccess = deptService.removeById(dno);
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
