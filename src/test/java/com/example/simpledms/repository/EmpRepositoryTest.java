package com.example.simpledms.repository;

import com.example.simpledms.model.Emp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmpRepositoryTest {

    @Autowired
    private EmpRepository empRepository;

    @Test
    void findAllByEnameContaining() {

        Optional<Emp> optionalEmp = Optional.ofNullable(Emp.builder()
                .eno(8888)
                .ename("이순신")
                .job("ACCOUNTING")
                .hiredate("1992-11-23 00:00:00")
                .salary(1500)
                .dno(20)
                .build());

        Emp emp = empRepository.save(optionalEmp.get());

        List<Emp> list = empRepository.findAllByEnameContaining("이순신");

        assertThat(list.get(0).getEname()).isEqualTo("이순신");

    }

    @Test
    void save(){
        Optional<Emp> optionalEmp = Optional.ofNullable(Emp.builder()
                .eno(8888)
                .ename("이순신")
                .job("ACCOUNTING")
                .hiredate("1992-11-23 00:00:00")
                .salary(1500)
                .dno(20)
                .build());

        Emp emp = empRepository.save(optionalEmp.get());

        assertThat(emp.getEname()).isEqualTo("이순신");
    }

    @Test
    void deleteAll(){
        Optional<Emp> optionalEmp = Optional.ofNullable(Emp.builder()
                .eno(8888)
                .ename("이순신")
                .job("ACCOUNTING")
                .hiredate("1992-11-23 00:00:00")
                .salary(1500)
                .dno(20)
                .build());

        Emp emp = empRepository.save(optionalEmp.get());

        empRepository.deleteAll();

        assertThat(empRepository.findAll()).isEqualTo(Collections.emptyList());

    }
}