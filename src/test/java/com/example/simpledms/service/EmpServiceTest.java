package com.example.simpledms.service;

import com.example.simpledms.model.Emp;
import com.example.simpledms.repository.EmpRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmpServiceTest {

    @Mock
    private EmpRepository empRepository;

    @InjectMocks
    private EmpService empService;

    @DisplayName("findAll() 전체 조회 함수")
    @Test
    void findAll() {
        List<Emp> list = new ArrayList<>();

        list.add(Emp.builder()
                .eno(7788)
                .ename("홍길동")
                .job("SALES")
                .hiredate("1982-01-23 00:00:00")
                .salary(1300)
                .dno(10)
                .build()
        );

        list.add(Emp.builder()
                .eno(8888)
                .ename("이순신")
                .job("ACCOUNTING")
                .hiredate("1992-11-23 00:00:00")
                .salary(1500)
                .dno(20)
                .build()
        );

        given(empRepository.findAll()).willReturn(list);

        List<Emp> list1 = empService.findAll();

        assertThat(list1.get(0).getEno()).isEqualTo(list.get(0).getEno());
        assertThat(list1.get(1).getEno()).isEqualTo(list.get(1).getEno());

    }

    @DisplayName("removeAll() 전체 삭제 함수")
    @Test
    void removeAll() {
        empService.removeAll();
        verify(empRepository,times(1)).deleteAll();
    }

    @DisplayName("removeAll() 전체 삭제 함수")
    @Test
    void save() {
        Emp emp = Emp.builder()
                .eno(8888)
                .ename("이순신")
                .job("ACCOUNTING")
                .hiredate("1992-11-23 00:00:00")
                .salary(1500)
                .dno(20)
                .build();

        given(empRepository.save(any())).willReturn(emp);

        Emp emp1 = empService.save(emp);

        assertThat(emp1.getEname()).isEqualTo(emp.getEname());
    }

    @DisplayName("findById() 사원번호 조회 함수")
    @Test
    void findById() {
        Optional<Emp> optionalEmp = Optional.ofNullable(Emp.builder()
                .eno(8888)
                .ename("이순신")
                .job("ACCOUNTING")
                .hiredate("1992-11-23 00:00:00")
                .salary(1500)
                .dno(20)
                .build());

        given(empRepository.findById(anyInt())).willReturn(optionalEmp);

        Optional<Emp> optionalEmp1 = empService.findById(anyInt());

        assertThat(optionalEmp1.get().getEno()).isEqualTo(optionalEmp.get().getEno());
    }


    @DisplayName("removeById() 사원번호로 삭제하는 함수")
    @Test
    void removeById() {
        given(empRepository.existsById(anyInt())).willReturn(true);

        boolean bSuccess = empService.removeById(anyInt());

        verify(empRepository, times(1)).deleteById(anyInt());
        assertThat(bSuccess).isTrue();
    }

    @DisplayName("findAllByEnameContaining() 사원이름 검색 함수")
    @Test
    void findAllByEnameContaining() {

    List<Emp> list = new ArrayList<>();

    list.add(Emp.builder()
                    .eno(7788)
                    .ename("홍길동")
                    .job("MANAGER")
                    .dno(10)
            .build());

    given(empRepository.findAllByEnameContaining(any())).willReturn(list);

    List<Emp> list1 = empService.findAllByEnameContaining(any());

    assertThat(list1.get(0).getEname()).isEqualTo("홍길동");
    }
}