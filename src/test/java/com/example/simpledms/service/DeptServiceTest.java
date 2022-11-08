package com.example.simpledms.service;

import com.example.simpledms.model.Dept;
import com.example.simpledms.repository.DeptRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

//  @ExtendWith(MockitoExtension.class)
//     서비스 클래스는Spring 기능이 필요없기 때문에 가짜 객체 MockitoExtension.class 사용
//  서비스쪽 테스트는 순수하게 단위 테스트만 실행하면됨 ( 스프링쪽 기능이 필요없음 )
@ExtendWith(MockitoExtension.class)
class DeptServiceTest {

//    가짜 repository 를 사용
    @Mock
    private DeptRepository deptRepository;

//    가짜 리파지토리를 사용해서 서비스를 사용할수 있게 만듬
    @InjectMocks
    private DeptService deptService;

    @DisplayName("findAll()  서비스 조회 함수 ")
    @Test
    void findAll() {

        List<Dept> list = new ArrayList<>();
        list.add(Dept.builder()
                .dno(10)
                .dname("SALES")
                .loc("SEOUL")
                .build()
        );
        list.add(Dept.builder()
                .dno(20)
                .dname("ACCOUNTING")
                .loc("BUSAN")
                .build()
        );

//        given() 기대값 설정
        given(deptRepository.findAll())
                .willReturn(list);

//        테스트 실행
        List<Dept> list1 = deptService.findAll();

//        결과검토  : assert()- 사용법 복잡 , assertThat()- 사용법 간소함
//        assertThat(테스트실행값).isEqualTo(기댓값);  일치하면 테스트 통과, 불일치하면 실패
        assertThat(list1.get(0).getDname()).isEqualTo(list.get(0).getDname());
        assertThat(list1.get(1).getDname()).isEqualTo(list.get(1).getDname());
    }

    @DisplayName("removeAll()  전체 삭제 함수 ")
    @Test
    void removeAll() {
//        테스트 실행
        deptService.removeAll();
//        위의 함수가 몇번 실행되었는지 확인
        verify(deptRepository, times(1)).deleteAll();
    }

    @DisplayName("save()  save 함수 ")
    @Test
    void save() {
//        기댓값 설정 : deptRepository 객체 이용
        Dept dept = Dept.builder()
                .dno(20)
                .dname("ACCOUNTING")
                .loc("BUSAN")
                .build();

        given(deptRepository.save(any())).willReturn(dept);

//        테스트 실행
        Dept dept1 = deptService.save(dept);

        assertThat(dept1.getDname()).isEqualTo(dept.getDname());
    }

    @DisplayName("findByid()  부서번호로 조회하는 함수 ")
    @Test
    void findById() {
        Optional<Dept> optionalDept = Optional.ofNullable(Dept.builder()
                .dno(10)
                .dname("SALES")
                .loc("SEOUL")
                .build()
        );

//        기댓값이 서비스 함수가 실행되면서 _해킹이 붙어 있음
        Optional<Dept> optionalDept2 = Optional.ofNullable(Dept.builder()
                .dno(10)
                .dname("SALES_해킹")
                .loc("SEOUL")
                .build()
        );

        given(deptRepository.findById(anyInt())).willReturn(optionalDept);

//        테스트 실행
        Optional<Dept> optionalDept1 = deptService.findById(anyInt());

//        결과 검증
        assertThat(optionalDept1.get().getDname()).isEqualTo(optionalDept.get().getDname());

    }

    @DisplayName("removeById()  부서번호로 삭제하는 함수 ")
    @Test
    void removeById() {
//        given()  : 가정, 전제 , 기댓값 설정
        given(deptRepository.existsById(anyInt())).willReturn(true);

//        테스트 실행
        boolean bSuccess = deptService.removeById(anyInt());  // 서비스 함수 삭제 실행

//      몇번 실행되는지 검토, true 가 나오는지 검토
        verify(deptRepository,times(1)).deleteById(anyInt());
        assertThat(bSuccess).isTrue();
    }

    @DisplayName("findAllByDnameContaining()  부서이름으로 조회하는 함수 ")
    @Test
    void findAllByDnameContaining() {

//        기댓값 설정
        List<Dept> list = new ArrayList<>();

        list.add(Dept.builder()
                .dno(10)
                .dname("SALES")
                .loc("SEOUL")
                .build()
        );

        list.add(Dept.builder()
                .dno(20)
                .dname("ACCOUNTING")
                .loc("PUSAN")
                .build()
        );

        given(deptRepository.findAllByDnameContaining(any())).willReturn(list);


//        테스트 실행
        List<Dept> list1 = deptService.findAllByDnameContaining(any());

//        결과검토  : assert()- 사용법 복잡 , assertThat()- 사용법 간소함
//        assertThat(테스트실행값).isEqualTo(기댓값);  일치하면 테스트 통과, 불일치하면 실패
        assertThat(list1.get(0).getDname()).isEqualTo(list.get(0).getDname());
        assertThat(list1.get(1).getDname()).isEqualTo(list.get(1).getDname());

    }
}