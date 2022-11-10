package com.example.simpledms.repository;


import com.example.simpledms.model.GalleryDb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GalleryDbRepository extends JpaRepository<GalleryDb, Integer> {

    Page<GalleryDb> findAllByGalleryTitleContaining(String galleryTitle, Pageable pageable);

}
