package com.example.simpledms.model;

import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 추상클래스 abstract
// JPA 에서 자동으로 생성일자 / 수정일자를 만들어주는 클래스
@Getter
@MappedSuperclass  // 모델을 감시하다가 생성일자/수정일자를 자동으로 포함시키는 어노테이션
@EntityListeners(AuditingEntityListener.class) // 감시기능 부여
public abstract class BaseTimeEntity {

//    생성일자 속성
    private String insertTime;

//    수정일자 속성
    private String updateTime;

//    soft-delete 를 위한 속성 2개
    private String deleteTime;
    private String deleteYn;



//    감시함수
//    해당 대상 모델(엔티티)를 저장(insert)하기전 실행되는 함수
    @PrePersist
    void onPrePersist(){
        this.insertTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

//    대상 모델(엔티티)을 수정(update)하기전 실행되는 함수
    @PreUpdate
    void onPreUpdate(){
        this.updateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.insertTime = this.updateTime;
    }


}
