package com.example.simpledms.repository;

import com.example.simpledms.model.Dept;
import org.apache.catalina.LifecycleState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;



//@ExtendWith(SpringExtension.class)  테스트 할때 스프링 기능이 필요
@ExtendWith(SpringExtension.class)
//@DataJpaTest : Repository 를 테스트하기 위한 어노테이션,
//              DB가 필요 , 테스트후 자동 롤백(데이터를 insert / update / delete  했으면 명령취소 )
@DataJpaTest
// DB 접근을 위한 어노테이션  ,
//   데이터 베이스 : 내장 DB  ( 스프링부트 있음)
//  아래 옵션은 외장 DB ( 오라클 DB) 로 테스트 진행한다는 옵션
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeptRepositoryTest {

    @Autowired
    private DeptRepository deptRepository;


    @Test
    void findAllByDnameContaining() {

//        테스트 가짜 데이터 정의
        Optional<Dept> optionalDept = Optional.ofNullable(Dept.builder()
                .dno(10)
                .dname("SALES")
                .loc("SEOUL")
                .build()
        );

//        임시로 데이터를 insert
        Dept dept = deptRepository.save(optionalDept.get());

//        테스트실행
        List<Dept> list = deptRepository.findAllByDnameContaining("SALES");

//        테스트 검증
        assertThat(list.get(0).getDname()).isEqualTo("SALES");

    }


    @Test
    void save(){

//        테스트 가짜 데이터 정의
        Optional<Dept> optionalDept = Optional.ofNullable(Dept.builder()
                .dno(10)
                .dname("SALES")
                .loc("SEOUL")
                .build()
        );

//        임시로 데이터를 insert
        Dept dept = deptRepository.save(optionalDept.get());

//        테스트 검증 후 자동 데이터 insert 취소 , 롤백
        assertThat(dept.getDname()).isEqualTo("SALES");

    }

    @Test
    void deleteAll(){

//        테스트 가짜 데이터 정의
        Optional<Dept> optionalDept = Optional.ofNullable(Dept.builder()
                .dno(10)
                .dname("SALES")
                .loc("SEOUL")
                .build()
        );

//        임시로 데이터를 insert
        Dept dept = deptRepository.save(optionalDept.get());

//         테스트 실행 : 모두 삭제
        deptRepository.deleteAll();

//        테스트검증  , 전체 삭제후 데이터가 없는 것을 검증
        assertThat(deptRepository.findAll()).isEqualTo(Collections.emptyList());
    }
}