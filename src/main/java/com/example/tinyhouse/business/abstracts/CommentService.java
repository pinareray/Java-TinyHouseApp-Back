package com.example.tinyhouse.business.abstracts;

import com.example.tinyhouse.core.utilities.results.DataResult;
import com.example.tinyhouse.core.utilities.results.Result;
import com.example.tinyhouse.entities.dtos.CommentCreateDto;
import com.example.tinyhouse.entities.dtos.CommentUpdateDto;
import com.example.tinyhouse.entities.dtos.CommentListDto;

import java.util.List;

public interface CommentService {

    Result add(CommentCreateDto dto);

    Result update(CommentUpdateDto dto);

    Result delete(int id, int userId);

    DataResult<List<CommentListDto>> getByHouseId(int houseId);

    DataResult<List<CommentListDto>> getAll();
}
