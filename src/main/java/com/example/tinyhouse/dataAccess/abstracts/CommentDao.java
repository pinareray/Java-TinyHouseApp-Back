package com.example.tinyhouse.dataAccess.abstracts;

import com.example.tinyhouse.entities.concretes.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao extends JpaRepository<Comment, Integer> {
    List<Comment> findByHouse_Id(int houseId);
    List<Comment> findByUser_Id(int userId);
    int countByHouse_Id(int houseId);

    @Query("SELECT AVG(c.rating) FROM Comment c WHERE c.house.id = :houseId")
    Double averageRatingByHouseId(@Param("houseId") int houseId);

}
