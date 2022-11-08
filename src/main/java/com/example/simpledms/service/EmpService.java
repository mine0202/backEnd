package com.example.simpledms.service;

import com.example.simpledms.model.Emp;
import com.example.simpledms.repository.EmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// 부서업무 서비스 클래스
@Service
public class EmpService {

    @Autowired
    EmpRepository empRepository;  // JPA CRUD 함수가있음

//    전체 조회 함수
    public List<Emp> findAll(){
//         JPA 가 findAll 등을 자동으로 만들어줌
        List<Emp> list = empRepository.findAll();
        return list;
    }

    public void removeAll(){
        empRepository.deleteAll();
    }

    public Emp save(Emp emp){
        Emp emp1 = empRepository.save(emp);
        return emp1;
    }

//    optional  null 방지객체
//     주요함수 :  .get() 안에있는 객체 꺼내기
//              .isPresent()  안에 객체 있는지 확인
    public Optional findById(int eno){
        Optional<Emp> optionalEmp = empRepository.findById(eno);
        return optionalEmp;
    }


    public boolean removeById(int eno){
        if(empRepository.existsById(eno)){
            empRepository.deleteById(eno);
            return true;
        }

        return false;
    }

    //    조회 함수
    public List<Emp> findAllByEnameContaining(String ename) {
        List<Emp> list = empRepository.findAllByEnameContaining(ename);
        return list;
    }


}
