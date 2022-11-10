package com.example.simpledms.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name="TB_GALLERY")
@SequenceGenerator(
        name= "SQ_GALLERY_GENERATOR"    // 자바에서 사용하는 이름
        , sequenceName = "SQ_GALLERY"   // 데이터베이스에서 사용하는 이름
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
@Where(clause = "DELETE_YN = 'N'")
@SQLDelete(sql="UPDATE TB_GALLERY SET DELETE_YN = 'Y', DELETE_TIME = TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE GID = ?")
public class GalleryDb extends BaseTimeEntity{
//
//    -- Upload Gallery Table
//    CREATE TABLE TB_GALLERY
//            (
//                    GID                  NUMBER NOT NULL PRIMARY KEY,
//                    GALLERY_TITLE        VARCHAR2(1000),
//    GALLERY_FILE_NAME    VARCHAR2(1000),
//    GALLERY_TYPE         VARCHAR2(1000),
//    GALLERY_DATA         BLOB,
//

    @Id  // 테이블에 하나이상의 기본키가 필요하므로 변하지 않는 값을 기본키로 만듬
    @GeneratedValue(strategy = GenerationType.SEQUENCE   // 시퀀스사용
                    , generator = "SQ_GALLERY_GENERATOR"
    )
    private Integer gid;

    @Column(columnDefinition = "VARCHAR2(1000)")
    private String galleryTitle;

    @Column(columnDefinition = "VARCHAR2(1000)")
    private String galleryFileName;

    @Column(columnDefinition = "VARCHAR2(1000)")  // CLOB 4000 byte 보다 큰 문자열 일때 사용하는 자료형
    private String galleryType;

    // BLOB 이미지, 동영상 등을 저장하는 자료형 (2진파일(binary file))
    @Lob
    @Column   //  Oracle 은 BLOB 를 사용
    private byte[] galleryData;

//    id(기본키)  제외 생성자
    public GalleryDb(String galleryTitle, String galleryFileName, String galleryType, byte[] galleryData) {
        this.galleryTitle = galleryTitle;
        this.galleryFileName = galleryFileName;
        this.galleryType = galleryType;
        this.galleryData = galleryData;
    }
}
