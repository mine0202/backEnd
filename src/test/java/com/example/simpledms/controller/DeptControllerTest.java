package com.example.simpledms.controller;

import com.example.simpledms.model.Dept;
import com.example.simpledms.service.DeptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// Controller 테스트 (Junit)
// 스프링부트에서 기본 제공함
// MVC 디자인 패턴안에서 테스팅
//  1. COntroller 테스트
//  2. Service 테스트
//  3. Repository 테스트
//  MVC : 서로 역할 분리해서 코딩,
//  테스팅 : 서로 MVC 클래스 간에 의존관계를 끊어서(격리) 독립적으로 테스트하는 것이 핵심
//      컨트롤러 테스트 : 서비스 객체역할을 대신해주는 가짜객체(Mocking)를 불러서 테스트함
//@ExtendWith(SpringExtension.class) : 컨트롤러 테스트를 위한 어노테이션, url 관련 기능들을 사용할 수있게함
@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = DeptController.class)    controllers=테스트하는 컨트롤클래스 , 입력하면 하나만 , 입력안하면 전부
@WebMvcTest(controllers = DeptController.class)
class DeptControllerTest {

//    가짜객체 받기
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeptService deptService; // 서비스에 가짜객체 넣기
    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("getDeptAll() : 부서모두 조회 함수 테스트") // 가독성을 위해서 넣음, 없어도됨
    @Test
    void getDeptAll() throws Exception {
//        1. 가짜데이터 설정
        List<Dept> list = new ArrayList<>();
//        build 디자인 패턴 : 생성자를 대신해서 객체를 생성하는 기능
//         장점 : 생성자보다 사용하기 편함, Lombok 에서도 지원함 ( @Builder , @AllArgsConstructor  )
//        모델.builder().속성1().속성2().....build()
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

//        2. given 설정 : 가짜데이터를 결과로 미리 예측
        given(deptService.findAll())
                .willReturn(list);
//        3. when 설정  : 테스팅 실행 -> 결과 와 미리예측한 결과가 동일한지 확인, 동일 ok , 틀리면 에러
//          1) url 점검
        mockMvc.perform(get("/api/dept"))
//                2) ok 메세지가 나오는가
                .andExpect(status().isOk())
//                3) Content-Type 이 application_json 인가
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 4)  json 객체 0번 dname 값은 SALES 인가
                .andExpect(jsonPath("$.[0].dname").value("SALES"))
//                 4)  json 객체 1번 dname 값은 ACCOUNGTING 인가
                .andExpect(jsonPath("$.[1].dname").value("ACCOUNTING"))
                .andDo(print());
    }

    @Test
    void removeAll() throws Exception {
//        given() : 기댓값 설정 ( void 함수 , 리턴값이 없음 )
//        willDoNothing().given(서비스객체).함수명() : 리턴값이 없는 함수에 기댓값 설정하는 방법
        willDoNothing().given(deptService).removeAll();

        mockMvc.perform(delete("/api/dept/all")) // 2) 테스트 실행
                .andExpect(status().isOk())                // 3) 테스트 결과 검토
                .andDo(print());
    }


    @DisplayName("createDept()  부서생성 함수")
    @Test
    void createDept() throws Exception {
        Dept dept = Dept.builder()
                .dno(10)
                .dname("SALES")
                .loc("SEOUL")
                .build();

        given(deptService.save(any())) // 정수일때는 anyInt() , 다른때는 any()
                .willReturn(dept);

        mockMvc.perform(post("/api/dept")
                        .contentType(MediaType.APPLICATION_JSON)
//                objectMapper.writeValueAsString(객체) : 객체 to Json 변환 => 문자열 또 변환
                        .content(objectMapper.writeValueAsString(dept)))
//                .content("{ \"dno\" : 10, \"dname\" : \"SALES\", \"loc\": \"SEOUL\"}"))
                        .andExpect(status().isOk())
                        .andDo(print());  // 테스트과정을 화면에 출력
    }

    @Test
    void getDeptId() throws Exception {
//        Optional.ofNullable(객체); 넣기 함수
//        Optional.get(); 빼기 함수
//        Optional.isPresent();  옵셔널 객체에 있는지 확인하는 함수
        Optional<Dept> optionalDept = Optional.ofNullable(Dept.builder()
                                                            .dno(10)
                                                            .dname("SALES")
                                                            .loc("SEOUL")
                                                            .build()
                                                        );

        given(deptService.findById(anyInt()))
                .willReturn(optionalDept);

        mockMvc.perform(get("/api/dept/10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dname").value("SALES"))
                .andDo(print());
    }


    @DisplayName("updateDept()  부서수정 함수")
    @Test
    void updateDept() throws Exception {
        Dept dept = Dept.builder()
                .dno(10)
                .dname("SALES111")
                .loc("SEOUL111")
                .build();

        given(deptService.save(any())) // 정수일때는 anyInt() , 다른때는 any()
                .willReturn(dept);

        mockMvc.perform(put("/api/dept/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"dno\" : 10, \"dname\" : \"SALES111\", \"loc\": \"SEOUL111\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dname").value("SALES111"))
                .andDo(print());  // 테스트과정을 화면에 출력
    }

    @DisplayName("removeDeptId() 부서번호로 삭제 함수 ")
    @Test
    void removeDeptId() throws Exception {


        given(deptService.removeById(anyInt()))
                .willReturn(true);

        mockMvc.perform(delete("/api/dept/deletion/10")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(print());
    }
}