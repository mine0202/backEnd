package com.example.simpledms.controller;

import com.example.simpledms.model.Emp;
import com.example.simpledms.service.EmpService;
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
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@ExtendWith(SpringExtension.class) : 컨트롤러 테스트를 위한 어노테이션, url 관련 기능들을 사용할 수있게함
@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = EmpController.class)    controllers=테스트하는 컨트롤클래스 , 입력하면 하나만 , 입력안하면 전부
@WebMvcTest(controllers = EmpController.class)
class EmpControllerTest {

    //    가짜객체 받기
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpService empService; // 서비스에 가짜객체 넣기
    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("getEmpAll() : 부서모두 조회 함수 테스트") // 가독성을 위해서 넣음, 없어도됨
    @Test
    void getEmpAll() throws Exception {
//        1. 가짜데이터 설정
        List<Emp> list = new ArrayList<>();
//        build 디자인 패턴 : 생성자를 대신해서 객체를 생성하는 기능
//         장점 : 생성자보다 사용하기 편함, Lombok 에서도 지원함 ( @Builder , @AllArgsConstructor  )
//        모델.builder().속성1().속성2().....build()
        list.add(Emp.builder()
                .eno(7788)
                .ename("홍길동")
                .job("SALES")
                        .hiredate("1982-01-23 00:00:00")
                        .salary(1300)
                        .dno(10)
                .build()
        );

//        2. given 설정 : 가짜데이터를 결과로 미리 예측
        given(empService.findAll())
                .willReturn(list);
//        3. when 설정  : 테스팅 실행 -> 결과 와 미리예측한 결과가 동일한지 확인, 동일 ok , 틀리면 에러
//          1) url 점검
        mockMvc.perform(get("/api/emp"))  // 테스트 실행
//                2) ok 메세지가 나오는가
                .andExpect(status().isOk())     // 결과 검토
//                3) Content-Type 이 application_json 인가
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 4)  json 객체 0번 ename 값은 SALES 인가
                .andExpect(jsonPath("$.[0].ename").value("홍길동"))
                .andDo(print());
    }

    @Test
    void removeAll() throws Exception {
//        given() 기대값 설정 (  void 함수 , 리턴값이 없음 )
//        willDoNothing  리턴값이 없는 함수 앞에 사용
//        willDoNothing().given(서비스객체 ). 함수명()   : 리턴값이 없는 함수에 기댓값 설정하는 방법
        willDoNothing().given(empService).removeAll();


        mockMvc.perform(delete("/api/emp/all"))  // 테스트 실행
                        .andExpect(status().isOk())
                        .andDo(print());
    }

    @DisplayName("createEmp()  부서생성 함수")
    @Test
    void createEmp() throws Exception {
        Emp emp = Emp.builder()
                .eno(7788)
                .ename("홍길동")
                .job("SALES")
                .hiredate("1982-01-23 00:00:00")
                .salary(1300)
                .dno(10)
                .build();

        given(empService.save(any())) // 정수일때는 anyInt() , 다른때는 any()
                .willReturn(emp);

//                        .content("{ \"dno\" : 10, \"ename\" : \"홍길동\", \"job\": \"SALES\"}"))

        mockMvc.perform(post("/api/emp")
                        .contentType(MediaType.APPLICATION_JSON)
                        . content(objectMapper.writeValueAsString(emp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ename").value("홍길동"))
                .andDo(print());  // 테스트과정을 화면에 출력
    }

    @Test
    void getEmpId() throws Exception {
//        Optional.ofNullable(객체); 넣기 함수
//        Optional.get(); 빼기 함수
//        Optional.isPresent();  옵셔널 객체에 있는지 확인하는 함수
        Optional<Emp> optionalEmp = Optional.ofNullable(Emp.builder()
                .eno(7788)
                .ename("홍길동")
                .job("SALES")
                .hiredate("1982-01-23 00:00:00")
                .salary(1300)
                .dno(10)
                .build()
        );

        given(empService.findById(anyInt()))
                .willReturn(optionalEmp);

        mockMvc.perform(get("/api/emp/10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ename").value("홍길동"))
                .andDo(print());
    }


    @DisplayName("updateEmp()  부서수정 함수")
    @Test
    void updateEmp() throws Exception {
        Emp emp = Emp.builder()
                .eno(7788)
                .ename("홍길동")
                .job("SALES")
                .hiredate("1982-01-23 00:00:00")
                .salary(1300)
                .dno(10)
                .build();

        given(empService.save(any())) // 정수일때는 anyInt() , 다른때는 any()
                .willReturn(emp);

        mockMvc.perform(put("/api/emp/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp)))
                .andExpect(status().isOk())
                .andDo(print());  // 테스트과정을 화면에 출력
    }

    @DisplayName("reboveEmpId() 부서번호로 삭제 함수 ")
    @Test
    void removeEmpId() throws Exception {


        given(empService.removeById(anyInt()))
                .willReturn(true);

        mockMvc.perform(delete("/api/emp/deletion/8888")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}