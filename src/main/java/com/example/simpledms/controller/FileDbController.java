package com.example.simpledms.controller;

import com.example.simpledms.dto.ResponseMessageDto;
import com.example.simpledms.dto.fileDb.ResponseFileDto;
import com.example.simpledms.model.FileDb;
import com.example.simpledms.service.FileDbService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api")
public class FileDbController {

    @Autowired
    FileDbService fileDbService;


    // 파일 저장에서 DTO 생성 등등 이필요한데 라이브러리를 제공함.
//    modelmapper : model to dto 자동변환, dto to model 자동변환
    ModelMapper modelMapper = new ModelMapper();


    //    이미지 업로드 함수
    @PostMapping("/fileDb/upload")
    public ResponseEntity<Object> fileDbUploadFile(@RequestParam(required = false) String fileTitle,  // fileTitle 로 날아오면 fileTitle에 넣어라
                                                   @RequestParam(required = false) String fileContent,
                                                   @RequestParam("fileDb") MultipartFile fileDb
                                                   ) {


        String message = ""; // front end 전송할 메세지
//            디버깅
        log.debug("title{}:"+fileTitle);
        log.debug("fileContent{}:"+fileContent);
        log.debug("fileDb{}:"+fileDb);

        try {
            fileDbService.store(fileTitle,fileContent,fileDb);

            message = "Upload the file successfully: " + fileDb.getOriginalFilename();

            return new ResponseEntity<>(new ResponseMessageDto(message), HttpStatus.OK); // message 를 넣어서 보내기 위해 Dto 만듬

        } catch (Exception e) {
            message = "Could not upload the file: " + fileDb.getOriginalFilename();

            log.debug(e.getMessage());

            return new ResponseEntity<>(new ResponseMessageDto(message),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    파일 다운로드 함수
    @GetMapping("/fileDb/{fid}")
    public ResponseEntity<byte[]> getFile(@PathVariable int fid){

//        id 로 조회함수
        FileDb fileDb = fileDbService.getFiles(fid).get();  // optional 로 사용하여 .get 으로 값 꺼냄

//        첨부파일이 있다고 알려주면서
//        첨부파일 다운로드 : url Content-Type  규칙을 바꿔주어야함 , 헤더에 담아서
//                          바디에는 실제 이미지를 담음
        return  ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDb.getFileName() + "\"")
                .body(fileDb.getFileData());
    }


//    이미지 정보를 모두 가져오는 함수  , 쿼리스트링으로 받음 RequestParam  사용
    @GetMapping("/fileDb")
    public ResponseEntity<Object> getListFiles(@RequestParam(required = false) String fileTitle,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "3") int size
    ) {

        try {
//            Pageable 객체 정의 ( page, size 값 설정 )
            Pageable pageable = PageRequest.of(page, size);

//            upload 이미지 정보를 가져오는 함수, map () 배열을 조작 반복문이 내부적으로 작동
            Page<ResponseFileDto> fileDbPage = fileDbService
                    .findAllByFileTitleContaining(fileTitle,pageable)
                    .map(dbFile ->{
//                        안에서 자동 반복문 실행 .map()
//                        1. 다운로드 url 만들기
//                        url 만들어주는 클래스 ServletUriComponentsBuilder
                        String fileDownloadUri = ServletUriComponentsBuilder
//                                .fromCurrentRequest()  아님
                                .fromCurrentContextPath()  // 이미지 파일 경로
                                .path("/api/fileDb/")
                                .path(dbFile.getFid().toString())   //  "/api/fileDb/1" 이렇게 만들어짐
                                .toUriString(); // 마지막에 호출 ( URL 완성됨 )

//                        dbFile 매개 변수 안에 fileDb 가 들어있음

//                        modelmapper 로 fileDb 를 ResponseFileDto 로 변환
//                        modelMapper.map(소스모델, 타겟Dto.class)
                        ResponseFileDto fileDto = modelMapper.map(dbFile, ResponseFileDto.class);

//                        Dto 에 2개 남은 속성 처리, setter 를 이용 가공된 데이터 저장
                        fileDto.setFileSize(dbFile.getFileData().length);  // fileDto 에 이미지 파일의 사이즈 저장
                        fileDto.setFileUrl(fileDownloadUri);     // fileDto 에  url 저장
                        return fileDto;
                    });

//            맵 자료구조에 넣어서 전송
            Map<String, Object> response = new HashMap<>();
            response.put("fileDb", fileDbPage.getContent());
            response.put("currentPage", fileDbPage.getNumber());
            response.put("totalItems", fileDbPage.getTotalElements());
            response.put("totalPages", fileDbPage.getTotalPages());

            if (fileDbPage.isEmpty() == false) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/fileDb/deletion/{fid}")
    public ResponseEntity<Object> removeFileDb(@PathVariable int fid) {
        try {
            boolean bSuccess = fileDbService.removeById(fid);
            if (bSuccess) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
