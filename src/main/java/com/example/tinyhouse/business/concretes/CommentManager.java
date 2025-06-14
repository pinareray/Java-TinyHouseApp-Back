package com.example.tinyhouse.business.concretes;

import com.example.tinyhouse.business.abstracts.CommentService;
import com.example.tinyhouse.core.utilities.results.*;
import com.example.tinyhouse.dataAccess.abstracts.CommentDao;
import com.example.tinyhouse.dataAccess.abstracts.HouseDao;
import com.example.tinyhouse.dataAccess.abstracts.UserDao;
import com.example.tinyhouse.entities.concretes.Comment;
import com.example.tinyhouse.entities.concretes.House;
import com.example.tinyhouse.entities.concretes.User;
import com.example.tinyhouse.entities.dtos.CommentCreateDto;
import com.example.tinyhouse.entities.dtos.CommentListDto;
import com.example.tinyhouse.entities.dtos.CommentUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentManager implements CommentService {

    private final CommentDao commentDao;
    private final UserDao userDao;
    private final HouseDao houseDao;

    @Autowired
    public CommentManager(CommentDao commentDao, UserDao userDao, HouseDao houseDao) {
        this.commentDao = commentDao;
        this.userDao = userDao;
        this.houseDao = houseDao;
    }

    @Override
    public Result add(CommentCreateDto dto) {
        Optional<User> userOpt = userDao.findById(dto.getUserId());
        Optional<House> houseOpt = houseDao.findById(dto.getHouseId());

        if (userOpt.isEmpty() || houseOpt.isEmpty()) {
            return new ErrorResult("Kullanıcı veya ev bulunamadı.");
        }

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setRating(dto.getRating());
        comment.setUser(userOpt.get());
        comment.setHouse(houseOpt.get());

        commentDao.save(comment);
        return new SuccessResult("Yorum başarıyla eklendi.");
    }

    @Override
    public Result update(CommentUpdateDto dto) {
        Optional<Comment> commentOpt = commentDao.findById(dto.getId());

        if (commentOpt.isEmpty()) {
            return new ErrorResult("Yorum bulunamadı.");
        }

        Comment comment = commentOpt.get();

        if (comment.getUser().getId() != dto.getUserId()) {
            return new ErrorResult("Bu yorumu güncelleme yetkiniz yok.");
        }

        comment.setContent(dto.getContent());
        comment.setRating(dto.getRating());

        commentDao.save(comment);
        return new SuccessResult("Yorum güncellendi.");
    }

    @Override
    public Result delete(int id, int userId) {
        Optional<Comment> commentOpt = commentDao.findById(id);

        if (commentOpt.isEmpty()) {
            return new ErrorResult("Yorum bulunamadı.");
        }

        Comment comment = commentOpt.get();

        if (comment.getUser().getId() != userId) {
            return new ErrorResult("Bu yorumu silme yetkiniz yok.");
        }

        commentDao.delete(comment);
        return new SuccessResult("Yorum silindi.");
    }

    @Override
    public DataResult<List<CommentListDto>> getByHouseId(int houseId) {
        List<Comment> comments = commentDao.findByHouse_Id(houseId);

        List<CommentListDto> dtos = comments.stream().map(c -> {
            CommentListDto dto = new CommentListDto();
            dto.setId(c.getId());
            dto.setContent(c.getContent());
            dto.setRating(c.getRating());
            dto.setUserId(c.getUser().getId());
            dto.setUserFullName(c.getUser().getFirstName() + " " + c.getUser().getLastName());
            dto.setHouseId(c.getHouse().getId());
            dto.setHouseTitle(c.getHouse().getTitle());
            return dto;
        }).collect(Collectors.toList());

        return new SuccessDataResult<>(dtos, "Yorumlar listelendi.");
    }

    @Override
    public DataResult<List<CommentListDto>> getAll() {
        List<Comment> comments = commentDao.findAll();

        List<CommentListDto> dtos = comments.stream().map(c -> {
            CommentListDto dto = new CommentListDto();
            dto.setId(c.getId());
            dto.setContent(c.getContent());
            dto.setRating(c.getRating());
            dto.setUserId(c.getUser().getId());
            dto.setUserFullName(c.getUser().getFirstName() + " " + c.getUser().getLastName());
            dto.setHouseId(c.getHouse().getId());
            dto.setHouseTitle(c.getHouse().getTitle());
            return dto;
        }).collect(Collectors.toList());

        return new SuccessDataResult<>(dtos, "Tüm yorumlar listelendi.");
    }
}
