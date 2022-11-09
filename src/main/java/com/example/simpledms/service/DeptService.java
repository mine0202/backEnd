package com.example.simpledms.service;

import com.example.simpledms.model.Dept;
import com.example.simpledms.repository.DeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// 부서업무 서비스 클래스
@Service
public class DeptService {

    @Autowired
    DeptRepository deptRepository;  // JPA CRUD 함수가있음

    //    전체 조회 함수를 변경 findAll(Pageable pageable)
    public Page<Dept> findAll(Pageable pageable) {
//         JPA 가 findAll 등을 자동으로 만들어줌
        Page<Dept> page = deptRepository.findAll(pageable);
        return page;
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


    //    조회 함수 ( 페이징 처리 )
    public Page<Dept> findAllByDnameContaining(String dname, Pageable pageable) {
        Page<Dept> page = deptRepository.findAllByDnameContaining(dname, pageable);
        return page;
    }

}
