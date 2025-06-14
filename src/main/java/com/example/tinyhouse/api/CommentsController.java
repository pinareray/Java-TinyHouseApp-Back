package com.example.tinyhouse.api;

import com.example.tinyhouse.business.abstracts.CommentService;
import com.example.tinyhouse.core.utilities.results.DataResult;
import com.example.tinyhouse.core.utilities.results.Result;
import com.example.tinyhouse.entities.dtos.CommentCreateDto;
import com.example.tinyhouse.entities.dtos.CommentListDto;
import com.example.tinyhouse.entities.dtos.CommentUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {

    private final CommentService commentService;

    @Autowired
    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public ResponseEntity<Result> add(@RequestBody CommentCreateDto dto) {
        return ResponseEntity.ok(commentService.add(dto));
    }

    @PutMapping("/update")
    public ResponseEntity<Result> update(@RequestBody CommentUpdateDto dto) {
        return ResponseEntity.ok(commentService.update(dto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Result> delete(@RequestParam int id, @RequestParam int userId) {
        return ResponseEntity.ok(commentService.delete(id, userId));
    }

    @GetMapping("/getByHouseId")
    public ResponseEntity<DataResult<List<CommentListDto>>> getByHouseId(@RequestParam int houseId) {
        return ResponseEntity.ok(commentService.getByHouseId(houseId));
    }

    @GetMapping("/getAll")
    public ResponseEntity<DataResult<List<CommentListDto>>> getAll() {
        return ResponseEntity.ok(commentService.getAll());
    }
}
