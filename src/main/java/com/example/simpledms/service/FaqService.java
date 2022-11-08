package com.example.simpledms.service;

import com.example.simpledms.model.Faq;
import com.example.simpledms.repository.FaqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// 부서업무 서비스 클래스
@Service
public class FaqService {

    @Autowired
    FaqRepository faqRepository;

    //    전체 조회 함수
//    public List<Faq> findAll() {
////         JPA 가 findAll 등을 자동으로 만들어줌
//        List<Faq> list = faqRepository.findAll();
//        return list;
//    }

    public void removeAll() {
        faqRepository.deleteAll();
    }

    //    저장함수
    public Faq save(Faq faq) {
        Faq faq1 = faqRepository.save(faq);
        return faq1;
    }

//  ID 로 조회 함수
    public Optional findById(int no){
        Optional<Faq> optionalFaq = faqRepository.findById(no);
        return optionalFaq;
    }

    public boolean removeById(int no) {
//        있으면 삭제 실행
        if(faqRepository.existsById(no)) {
            faqRepository.deleteById(no);
            return true;
        }
//        없으면 false 리턴
        return false;
    }


    //    조회 함수
    public Page<Faq> findAllByTitleContaining(String title, Pageable pageable) {
        Page<Faq> page = faqRepository.findAllByTitleContaining(title,pageable);
        return page;
    }

}
