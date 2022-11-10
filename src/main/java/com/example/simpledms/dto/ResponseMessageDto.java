package com.example.simpledms.dto;


import lombok.*;

//message Dto
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessageDto {

//    클라이언트 (Vue ) 쪽으로 전달할 메세지
    private String message;

}
