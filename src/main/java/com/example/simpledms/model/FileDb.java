package com.example.simpledms.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name="TB_FILE")
@SequenceGenerator(
        name= "SQ_FILE_GENERATOR"    // 자바에서 사용하는 이름
        , sequenceName = "SQ_FILE"   // 데이터베이스에서 사용하는 이름
        , initialValue = 1           // 초기값
        , allocationSize = 1         // 증가값
)
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
// soft delete : 삭제하는 척만 하기 (화면에서는 안보이고 DB는 데이터를 삭제하지 않음)
// 법정 의무 보관 기간을 위해 실제 데이터를 삭제하지 않음
@Where(clause = "DELETE_YN = 'N'")
//@SQLDelete(sql="UPDATE 문")  delete 문이 실행되지 않고, 매개변수의 update문이 실행되게함
//@Where(clause = "강제조건") 대상클래스에 @붙이면 sql 문 실행시 강제 조건이 붙어 실행됨
@SQLDelete(sql="UPDATE TB_FILE SET DELETE_YN = 'Y', DELETE_TIME = TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE FID = ?")
public class FileDb extends BaseTimeEntity{

    @Id  // 테이블에 하나이상의 기본키가 필요하므로 변하지 않는 값을 기본키로 만듬
    @GeneratedValue(strategy = GenerationType.SEQUENCE   // 시퀀스사용
                    , generator = "SQ_FILE_GENERATOR"
    )
    private Integer fid;

    @Column(columnDefinition = "VARCHAR2(1000)")
    private String fileTitle;

    @Column(columnDefinition = "VARCHAR2(1000)")
    private String fileContent;

    @Column(columnDefinition = "VARCHAR2(1000)")
    private String fileName;

    @Column(columnDefinition = "VARCHAR2(1000)")  // CLOB 4000 byte 보다 큰 문자열 일때 사용하는 자료형
    private String fileType;

    // BLOB 이미지, 동영상 등을 저장하는 자료형 (2진파일(binary file))
    @Lob
    @Column   //  Oracle 은 BLOB 를 사용
    private byte[] fileData;

//    id(기본키)  제외 생성자
    public FileDb(String fileTitle, String fileContent, String fileName, String fileType, byte[] fileData) {
        this.fileTitle = fileTitle;
        this.fileContent = fileContent;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileData = fileData;
    }
}
