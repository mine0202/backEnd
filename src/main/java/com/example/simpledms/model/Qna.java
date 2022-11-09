package com.example.simpledms.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name="TB_QNA")
@SequenceGenerator(
        name= "SQ_QNA_GENERATOR"    // 자바에서 사용하는 이름
        , sequenceName = "SQ_QNA"   // 데이터베이스에서 사용하는 이름
        , initialValue = 1           // 초기값
        , allocationSize = 1         // 증가값
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
// soft delete : 삭제하는 척만 하기 (화면에서는 안보이고 DB는 데이터를 삭제하지 않음)
// 법정 의무 보관 기간을 위해 실제 데이터를 삭제하지 않음
@Where(clause = "DELETE_YN = 'N'")
//@SQLDelete(sql="UPDATE 문")  delete 문이 실행되지 않고, 매개변수의 update문이 실행되게함
//@Where(clause = "강제조건") 대상클래스에 @붙이면 sql 문 실행시 강제 조건이 붙어 실행됨
@SQLDelete(sql="UPDATE TB_QNA SET DELETE_YN = 'Y', DELETE_TIME = TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE QNO = ?")
public class Qna extends BaseTimeEntity{

//    CREATE TABLE TB_QNA
//            (
//                    QNO          NUMBER NOT NULL PRIMARY KEY,
//                    QUESTION    VARCHAR2(255),
//    ANSWER      VARCHAR2(255),
//    QUESTIONER  VARCHAR2(255),
//    ANSWERER    VARCHAR2(255),
//    DELETE_YN   VARCHAR2(1) DEFAULT 'N',
//    INSERT_TIME VARCHAR2(255),
//    UPDATE_TIME VARCHAR2(255),
//    DELETE_TIME VARCHAR2(255)
//);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
                    , generator = "SQ_QNA_GENERATOR"
    )
    private Integer qno;

    @Column(columnDefinition = "VARCHAR2(255)")
    private String question;

    @Column(columnDefinition = "VARCHAR2(255)")
    private String answer;

    @Column(columnDefinition = "VARCHAR2(255)")
    private String questioner;

    @Column(columnDefinition = "VARCHAR2(255)")
    private String answerer;

}
