package com.example.simpledms.repository;


import com.example.simpledms.model.FileDb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileDbRepository extends JpaRepository<FileDb, Integer> {

    Page<FileDb> findAllByFileTitleContaining(String fileTitle, Pageable pageable);

}
