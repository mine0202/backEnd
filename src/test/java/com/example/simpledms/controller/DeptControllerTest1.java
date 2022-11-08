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

/**
 * packageName : com.example.simpledms.controller
 * fileName : DeptControllerTest
 * author : ds
 * date : 2022-11-03
 * description : Controller 테스트 (Junit)
 * 요약 :
 * 단위(기능, 함수) 테스트 : JUnit 5 라이브러리 이용,
 * 스프링부트 프로젝트를 생성하면 자동으로 JUNIT 라이브러리가 설치됨
 * MVC 디자인패턴안에서 테스팅 :
 * 1) Controller 테스트
 * 2) Service 테스트
 * 3) Repository 테스트
 * MVC : 서로 역할 분리해서( 가짜 객체를 이용(모킹) ) 코딩,
 * 테스팅 : 서로 MVC 클래스간에 의존관계를 끊어서(격리시켜서) 독립적으로 테스트하는 것이 핵심
 * 1) 컨트롤러 테스트 : 서비스 객체역할을 대신해주는 가짜 객체를(Mocking) 불러서 테스트 함
 *
 * @WebMvcTest(controllers = 대상_컨트롤러명.class) :
 * @ExtendWith(SpringExtension.class 또는 가짜_객체.class) :
 * 테스팅할 때 스프링부트 기능 이용할 것인지를 정의 ( SpringExtension.class )
 * MockMvc : 가짜 객체 클래스명 , @Autowired 로 객체를 하나 받아와야함
 * @MockBean : 대상 변수에 가짜 객체를 넣어줌
 * 2) 테스트 절차 :
 * 1) 전제( given ) : 테스트에 대한 사전 조건 정의(결과 기대값 정의)
 * given(함수()).willReturn(기댓값)
 * 2) 실행( when, perform() ) : 실제 테스트가 진행됨(테스트 함수 실행)
 * perform(get(url)) : get 방식 테스트 실행
 * perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json데이터))
 * : post 방식 테스트 실행
 * perform(put(url).contentType(MediaType.APPLICATION_JSON).content(json데이터))
 * : put 방식 테스트 실행
 * perform(delete(url)) : delete 방식 테스트 실행
 * 3) 결과점검(then, andExpect(점검함수들()) ) : 테스트 결과를 알려줌, 테스팅 결과 점검
 * 점검함수 종류 :
 * status().isOK() : 상태 메세지가 OK 로 나오는가?
 * jsonPath(json_객체_경로).value(값) : json 객체 경로에 그 값이 있는가?
 * 그 외 기타 등등 : header(), cookie(), view(), model() 등등
 * 참고) jsonPath() : json 객체의 경로를 탐색하는 라이브러리 함수
 * $ : json 루트 경로
 * .(닷) : 속성명을 접근하는 접근자
 * ex) {
 * "dname": "SALES",
 * "loc": "SEOUL"
 * }
 * => jsonPath($.dname).value("SALES") == "SALES"
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2022-11-03         ds          최초 생성
 */
// @ExtendWith : 컨트롤러 테스트를 위한 어노테이션 , URL 관련된 기능들을 사용할 수 있게함
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DeptController.class)
class DeptControllerTest1 {

    //    가짜 객체 받기
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeptService deptService; // 서비스에 가짜객체 넣기

    //    잭슨(jackson) 객체 생성 : 객체(모델) to Json, Json to 객체(모델) 자동 변환시켜주는 라이브러리
    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("getDeptAll() : 부서 모두 조회 함수 테스트")
    @Test
    void getDeptAll() throws Exception {
//        1) 가짜 데이터 설정
        List<Dept> list = new ArrayList<>();

//        builder 디자인 패턴 : 생성자를 대신해서 객체를 생성하는 기능,
//        장점 : 생성자보다 사용하기 편함, Lombok에서도 지원함(@Builder)
//    사용법 : 모델.builder()
//               .속성()
//               .속성2()
//                ...
//               .build()
        list.add(Dept.builder()
                .dno(10)
                .dname("SALES")
                .loc("SEOUL")
                .build() // .build() 마지막에 무조건 호출해서 객체가 생성됨
        );
        list.add(Dept.builder()
                .dno(20)
                .dname("ACCOUNTING")
                .loc("BUSAN")
                .build() // .build() 마지막에 무조건 호출해서 객체가 생성됨
        );

//        2) given 설정 : 가짜데이터를 결과로 미리 예측
        given(deptService.findAll())
                .willReturn(list);

//        3) when 설정  : 테스팅 실행 -> 결과  == 결과로 미리 예측 확인 ( 동일 : OK, 틀리면 : 에러 )
//        1) url /api/dept 인가?
        mockMvc.perform(get("/api/dept"))
//                2) 실행 후 OK 메세지가 나오는가?
                .andExpect(status().isOk())
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

    @DisplayName("createDept() : 부서 생성 함수 테스트")
    @Test
    void createDept() throws Exception {
        Dept dept = Dept.builder()
                .dno(10)
                .dname("SALES")
                .loc("SEOUL")
                .build();

        given(deptService.save(any()))
                .willReturn(dept);

//        테스팅 실행
        //when
        mockMvc.perform(post("/api/dept")
                        .contentType(MediaType.APPLICATION_JSON)
//                objectMapper.writeValueAsString(객체) : 객체 to Json 변환 => 문자열 또 변환
                        .content(objectMapper.writeValueAsString(dept)))
                .andExpect(status().isOk())
                .andDo(print()); // print() 테스트 과정을 화면에 출력하는 함수
    }

    @DisplayName("getDeptId() : 부서번호로 조회 함수 테스트")
    @Test
    void getDeptId() throws Exception {
//        Optional 객체 :
//             1) Optional 객체에 넣기 함수 : Optional.ofNullable(객체);
//             2) Optional 객체에 빼기 함수 : 옵셔널객체.get()
//             3) Optional 객체에 있는지 확인하는 함수 : 옵셔널객체.isPresent()
        //        1) 가짜 데이터 설정
        Optional<Dept> optionalDept = Optional.ofNullable(Dept.builder()
                .dno(10)
                .dname("SALES")
                .loc("SEOUL")
                .build());

//        2) given 설정 : 가짜데이터를 결과로 미리 예측
        given(deptService.findById(anyInt()))
                .willReturn(optionalDept);

//        3) when 설정  : 테스팅 실행 -> 결과  == 결과로 미리 예측 확인 ( 동일 : OK, 틀리면 : 에러 )
//        1) url /api/dept 인가?
        mockMvc.perform(get("/api/dept/10"))
//                2) 실행 후 OK 메세지가 나오는가?
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("updateDept() : 부서번호로 수정 함수 테스트")
    @Test
    void updateDept() throws Exception {
        Dept dept = Dept.builder()
                .dno(10)
                .dname("SALES2")
                .loc("SEOUL2")
                .build();

        given(deptService.save(any()))
                .willReturn(dept);

//        테스팅 실행
        //when
        mockMvc.perform(put("/api/dept/10")
                        .contentType(MediaType.APPLICATION_JSON)
//                objectMapper.writeValueAsString(객체) : 객체 to Json 변환 => 문자열 또 변환
                        .content(objectMapper.writeValueAsString(dept)))
                .andExpect(status().isOk())
                .andDo(print()); // print() 테스트 과정을 화면에 출력하는 함수
    }

    @DisplayName("deleteDept() : 부서번호로 삭제 함수 테스트")
    @Test
    void deleteDept() throws Exception {

//        2) given 설정 : 가짜데이터를 결과로 미리 예측
        given(deptService.removeById(anyInt()))
                .willReturn(true);

//        3) when 설정  : 테스팅 실행 -> 결과  == 결과로 미리 예측 확인 ( 동일 : OK, 틀리면 : 에러 )
        mockMvc.perform(delete("/api/dept/deletion/10"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}