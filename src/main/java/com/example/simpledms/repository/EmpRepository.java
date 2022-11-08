package com.example.simpledms.repository;

import com.example.simpledms.model.Emp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EmpRepository extends JpaRepository<Emp,Integer> {

    //    like 검색은 기본제공하지 않으므로 새로 만듬
//    사원명으로 조회하는 like 검색
//    query method 방식
    List<Emp> findAllByEnameContaining(String ename);

}
