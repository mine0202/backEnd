package com.example.simpledms.dto.fileDb;


import lombok.*;

//fileDb Dto
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFileDto {
//    모델 클래스 속성들
    private Integer fid;
    private String fileTitle;
    private String fileContent;
    private String fileName;
    private String fileType;
//    private byte[] fileData;  필요없음


//   가공된 속성
    private Integer fileSize; // 이미지 크기
    private String fileUrl; // 이미지 다운로드 url



}
