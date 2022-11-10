package com.example.simpledms.dto.gallery;


import lombok.*;

//fileDb Dto
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGalleryDto {
//    모델 클래스 속성들
    private Integer gid;
    private String galleryTitle;
    private String galleryFileName;
    private String galleryType;

//    private byte[] galleryData;  필요없음


//   가공된 속성
    private Integer fileSize; // 이미지 크기
    private String fileUrl; // 이미지 다운로드 url



}
