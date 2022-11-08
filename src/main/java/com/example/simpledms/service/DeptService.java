package com.example.simpledms.service;

import com.example.simpledms.model.Dept;
import com.example.simpledms.repository.DeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// 부서업무 서비스 클래스
@Service
public class DeptService {

    @Autowired
    DeptRepository deptRepository;  // JPA CRUD 함수가있음

    //    전체 조회 함수
    public List<Dept> findAll() {
//         JPA 가 findAll 등을 자동으로 만들어줌
        List<Dept> list = deptRepository.findAll();
        return list;
    }

    public void removeAll() {
        deptRepository.deleteAll();
    }

    //    저장함수
    public Dept save(Dept dept) {
        Dept dept1 = deptRepository.save(dept);
        return dept1;
    }

//  ID 로 조회 함수
    public Optional findById(int dno){
        Optional<Dept> optionalDept = deptRepository.findById(dno);

//        데이터 가공
        Dept dept = optionalDept.get();

        dept.setDname(dept.getDname() +"_해킹");

//        가공데이터를 다시 옵셔널에 넣기
        optionalDept = Optional.ofNullable(dept);

        return optionalDept;
    }

    public boolean removeById(int dno) {
//        있으면 삭제 실행
        if(deptRepository.existsById(dno)) {
            deptRepository.deleteById(dno);
            return true;
        }
//        없으면 false 리턴
        return false;
    }


    //    조회 함수
    public List<Dept> findAllByDnameContaining(String dname) {
        List<Dept> list = deptRepository.findAllByDnameContaining(dname);
        return list;
    }

}