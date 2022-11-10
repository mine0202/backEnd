package com.example.simpledms.service;

import com.example.simpledms.model.FileDb;
import com.example.simpledms.repository.FileDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

// 부서업무 서비스 클래스
@Service
public class FileDbService {

    @Autowired
    FileDbRepository fileDbRepository;

    //    조회 함수
    public Page<FileDb> findAllFiles(Pageable pageable) {
        Page<FileDb> page = fileDbRepository.findAll(pageable);
        return page;
    }

    //    id 로 파일조회 , 1건 뿐이 안나오기때문에 페이징 처리가 필요없음
//    findById(기본키)  : JPA 제공하는 기본 함수
    public Optional<FileDb> getFiles(int id) {
        Optional<FileDb> optionalFileDb = fileDbRepository.findById(id);
        return optionalFileDb;
    }

    //    id  로 삭제 함수, 기본키
    public boolean removeById(int id) {
        if (fileDbRepository.existsById(id)) {
            fileDbRepository.deleteById(id);
            return true;
        }

        return false;
    }

    //    fileTitle  이미지명으로 like 조회 함수 ( 페이징 처리 )
    public Page<FileDb> findAllByFileTitleContaining(String fileTitle, Pageable pageable) {
        Page<FileDb> page = fileDbRepository.findAllByFileTitleContaining(fileTitle, pageable);
        return page;
    }

    //    이미지 저장 함수
    public FileDb store(String fileTitle,
                        String fileContent,
                        MultipartFile file ) throws IOException {

//      fileName 가져오기 : path ( 폴더경로) 제거후 순수한 파일이름 가져오기
//        .getOriginalFilename()    경로 / 파일명
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

//        1. FileDb 생성자에 경로등 여러정보를 저장
        FileDb fileDb = new FileDb(fileTitle,
                                    fileContent,
                                    fileName,
                                    file.getContentType(), // 이미지의 타입(jpg,png,..)
                                    file.getBytes());    // 이미지 크기(size)

//        2. 위의 FileDb 를 DB 저장 + return

        return fileDbRepository.save(fileDb);
    }
}
