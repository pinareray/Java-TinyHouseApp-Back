package com.example.tinyhouse.business.concretes;

import com.example.tinyhouse.business.abstracts.HouseService;
import com.example.tinyhouse.business.constants.HouseMessages;
import com.example.tinyhouse.business.constants.UserMessages;
import com.example.tinyhouse.core.utilities.results.*;
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

    @Autowired
    public HouseManager(HouseDao houseDao, UserDao userDao) {
        this.houseDao = houseDao;
        this.userDao = userDao;
    }

    @Override
    public DataResult<List<HouseListDto>> getAll(int requesterId) {
        DataResult<User> requesterResult = getValidRequester(requesterId, UserRole.HOST, UserRole.ADMIN, UserRole.RENTER);
        if (!requesterResult.isSuccess()) {
            return new ErrorDataResult<>(requesterResult.getMessage());
        }

        List<House> houses = houseDao.findAll();
        List<HouseListDto> dtoList = houses.stream()
                .map(house -> new HouseListDto(
                        house.getId(),
                        house.getTitle(),
                        house.getPrice(),
                        house.getLocation(),
                        house.getStatus()
                ))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(dtoList, HouseMessages.HOUSE_LISTED);
    }

    @Override
    public DataResult<HouseDto> getById(int houseId, int requesterId) {
        DataResult<User> requesterResult = getValidRequester(requesterId, UserRole.HOST, UserRole.ADMIN, UserRole.RENTER);
        if (!requesterResult.isSuccess()) {
            return new ErrorDataResult<>(requesterResult.getMessage());
        }

        return houseDao.findById(houseId)
                .<DataResult<HouseDto>>map(house -> new SuccessDataResult<>(
                        new HouseDto(house),
                        HouseMessages.HOUSE_FOUND
                ))
                .orElseGet(() -> new ErrorDataResult<>(HouseMessages.HOUSE_NOT_FOUND));
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
                .map(h -> new HouseListDto(h.getId(), h.getTitle(), h.getPrice(), h.getLocation(), h.getStatus()))
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

}
