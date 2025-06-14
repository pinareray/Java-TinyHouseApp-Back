package com.example.tinyhouse.business.concretes;

import com.example.tinyhouse.business.abstracts.HouseService;
import com.example.tinyhouse.business.constants.HouseMessages;
import com.example.tinyhouse.business.constants.UserMessages;
import com.example.tinyhouse.core.utilities.results.*;
import com.example.tinyhouse.dataAccess.abstracts.CommentDao;
import com.example.tinyhouse.dataAccess.abstracts.HouseDao;
import com.example.tinyhouse.dataAccess.abstracts.UserDao;
import com.example.tinyhouse.entities.concretes.House;
import com.example.tinyhouse.entities.concretes.User;
import com.example.tinyhouse.entities.dtos.*;
import com.example.tinyhouse.entities.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HouseManager implements HouseService {

    private final HouseDao houseDao;
    private final UserDao userDao;
    private final CommentDao commentDao;

    @Autowired
    public HouseManager(HouseDao houseDao, UserDao userDao, CommentDao commentDao) {
        this.houseDao = houseDao;
        this.userDao = userDao;
        this.commentDao = commentDao;
    }

    @Override
    public DataResult<List<HouseListDto>> getAll(int requesterId) {
        DataResult<User> requesterResult = getValidRequester(requesterId, UserRole.HOST, UserRole.ADMIN, UserRole.RENTER);
        if (!requesterResult.isSuccess()) {
            return new ErrorDataResult<>(requesterResult.getMessage());
        }

        List<House> houses = houseDao.findAll();
        List<HouseListDto> dtoList = houses.stream()
                .map(house -> {
                    int commentCount = commentDao.countByHouse_Id(house.getId());
                    Double average = commentDao.averageRatingByHouseId(house.getId());
                    double avgRating = average != null ? average : 0.0;

                    HouseListDto dto = new HouseListDto(
                            house.getId(),
                            house.getTitle(),
                            house.getDescription(),
                            house.getPrice(),
                            house.getLocation(),
                            house.getStatus(),
                            house.getAvailableFrom(),
                            house.getAvailableTo(),
                            commentCount,
                            avgRating,
                            new UserDto(
                                    house.getHost().getId(),
                                    house.getHost().getFirstName(),
                                    house.getHost().getLastName(),
                                    house.getHost().getEmail(),
                                    house.getHost().getRole(),
                                    house.getHost().isActive()
                            )
                    );
                    return dto;
                }).collect(Collectors.toList());
        return new SuccessDataResult<>(dtoList, HouseMessages.HOUSE_LISTED);
    }

    @Override
    public DataResult<HouseDto> getById(int houseId, int requesterId) {
        DataResult<User> requesterResult = getValidRequester(requesterId, UserRole.HOST, UserRole.ADMIN, UserRole.RENTER);
        if (!requesterResult.isSuccess()) {
            return new ErrorDataResult<>(requesterResult.getMessage());
        }

        Optional<House> houseOpt = houseDao.findById(houseId);
        if (houseOpt.isEmpty()) {
            return new ErrorDataResult<>(HouseMessages.HOUSE_NOT_FOUND);
        }

        House house = houseOpt.get();

        List<CommentDto> commentDtos = house.getCommentList().stream().map(c -> {
            CommentDto dto = new CommentDto();
            dto.setId(c.getId());
            dto.setContent(c.getContent());
            dto.setRating(c.getRating());
            dto.setUserFullName(c.getUser().getFirstName() + " " + c.getUser().getLastName());
            return dto;
        }).collect(Collectors.toList());

        int count = commentDtos.size();
        double avg = commentDtos.stream().mapToInt(CommentDto::getRating).average().orElse(0.0);

        HouseDto dto = new HouseDto(house);
        dto.setCommentCount(count);
        dto.setAverageRating(avg);
        dto.setComments(commentDtos);

        dto.setHost(new UserDto(  // <-- Bunu ekle
                house.getHost().getId(),
                house.getHost().getFirstName(),
                house.getHost().getLastName(),
                house.getHost().getEmail(),
                house.getHost().getRole(),
                house.getHost().isActive()
        ));

        return new SuccessDataResult<>(dto, HouseMessages.HOUSE_FOUND);
    }

    @Override
    public DataResult<HouseDto> add(HouseCreateDto dto) {
        DataResult<User> requesterResult = getValidRequester(dto.getRequesterId(), UserRole.HOST, UserRole.ADMIN);
        if (!requesterResult.isSuccess()) {
            return new ErrorDataResult<>(requesterResult.getMessage());
        }

        Optional<User> host = userDao.findById(dto.getHostId());
        if (host.isEmpty()) {
            return new ErrorDataResult<>(HouseMessages.HOST_NOT_FOUND);
        }

        House house = new House();
        house.setTitle(dto.getTitle());
        house.setDescription(dto.getDescription());
        house.setPrice(dto.getPrice());
        house.setLocation(dto.getLocation());
        house.setStatus(dto.getStatus());
        house.setAvailableFrom(dto.getAvailableFrom());
        house.setAvailableTo(dto.getAvailableTo());
        house.setHost(host.get());

        houseDao.save(house);

        return new SuccessDataResult<>(new HouseDto(house), HouseMessages.HOUSE_ADDED);
    }

    @Override
    public DataResult<HouseDto> update(HouseUpdateDto dto) {
        DataResult<User> requesterResult = getValidRequester(dto.getRequesterId(), UserRole.HOST, UserRole.ADMIN);
        if (!requesterResult.isSuccess()) {
            return new ErrorDataResult<>(requesterResult.getMessage());
        }

        Optional<House> houseOpt = houseDao.findById(dto.getId());
        if (houseOpt.isEmpty()) {
            return new ErrorDataResult<>(HouseMessages.HOUSE_NOT_FOUND);
        }

        House house = houseOpt.get();
        house.setTitle(dto.getTitle());
        house.setDescription(dto.getDescription());
        house.setPrice(dto.getPrice());
        house.setLocation(dto.getLocation());
        house.setStatus(dto.getStatus());
        house.setAvailableFrom(dto.getAvailableFrom());
        house.setAvailableTo(dto.getAvailableTo());

        houseDao.save(house);

        return new SuccessDataResult<>(new HouseDto(house), HouseMessages.HOUSE_UPDATED);
    }

    @Override
    public Result delete(int houseId, int requesterId) {
        DataResult<User> requesterResult = getValidRequester(requesterId, UserRole.HOST, UserRole.ADMIN);
        if (!requesterResult.isSuccess()) {
            return new ErrorResult(requesterResult.getMessage());
        }

        if (!houseDao.existsById(houseId)) {
            return new ErrorResult(HouseMessages.HOUSE_NOT_FOUND);
        }

        houseDao.deleteById(houseId);
        return new SuccessResult(HouseMessages.HOUSE_DELETED);
    }


    @Override
    public DataResult<List<HouseListDto>> getByHostId(int hostId, int requesterId) {
        DataResult<User> requesterResult = getValidRequester(requesterId, UserRole.HOST, UserRole.ADMIN);
        if (!requesterResult.isSuccess()) {
            return new ErrorDataResult<>(requesterResult.getMessage());
        }

        List<House> houses = houseDao.findByHost_Id(hostId);

        List<HouseListDto> dtoList = houses.stream()
                .map(h -> {
                    int commentCount = commentDao.countByHouse_Id(h.getId());
                    Double average = commentDao.averageRatingByHouseId(h.getId());
                    double avgRating = average != null ? average : 0.0;

                    return new HouseListDto(
                            h.getId(),
                            h.getTitle(),
                            h.getDescription(),
                            h.getPrice(),
                            h.getLocation(),
                            h.getStatus(),
                            h.getAvailableFrom(),
                            h.getAvailableTo(),
                            commentCount,
                            avgRating,
                            new UserDto(
                                    h.getHost().getId(),
                                    h.getHost().getFirstName(),
                                    h.getHost().getLastName(),
                                    h.getHost().getEmail(),
                                    h.getHost().getRole(),
                                    h.getHost().isActive()
                            )
                    );
                })
                .collect(Collectors.toList());

        return new SuccessDataResult<>(dtoList);
    }



    private DataResult<User> getValidRequester(int requesterId, UserRole... allowedRoles) {
        Optional<User> userOpt = userDao.findById(requesterId);

        if (userOpt.isEmpty()) {
            return new ErrorDataResult<>(null, UserMessages.USER_NOT_FOUND);
        }

        User user = userOpt.get();

        boolean hasAccess = false;
        for (UserRole role : allowedRoles) {
            if (user.getRole() == role) {
                hasAccess = true;
                break;
            }
        }

        if (!hasAccess) {
            return new ErrorDataResult<>(null, UserMessages.UNAUTHORIZED_ACTION);
        }

        return new SuccessDataResult<>(user);
    }

    @Override
    public DataResult<List<HouseListDto>> getActiveHouses(int requesterId) {
        List<House> activeHouses = houseDao.findByStatus("Aktif");

        List<HouseListDto> dtos = activeHouses.stream().map(h -> {
            int commentCount = commentDao.countByHouse_Id(h.getId());
            Double average = commentDao.averageRatingByHouseId(h.getId());
            double avgRating = average != null ? average : 0.0;

            return new HouseListDto(
                    h.getId(),
                    h.getTitle(),
                    h.getDescription(),
                    h.getPrice(),
                    h.getLocation(),
                    h.getStatus(),
                    h.getAvailableFrom(),
                    h.getAvailableTo(),
                    commentCount,
                    avgRating,
                    new UserDto(
                            h.getHost().getId(),
                            h.getHost().getFirstName(),
                            h.getHost().getLastName(),
                            h.getHost().getEmail(),
                            h.getHost().getRole(),
                            h.getHost().isActive()
                    )
            );
        }).collect(Collectors.toList());

        return new SuccessDataResult<>(dtos);
    }

}
