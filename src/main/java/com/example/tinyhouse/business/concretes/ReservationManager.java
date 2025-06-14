package com.example.tinyhouse.business.concretes;

import com.example.tinyhouse.business.abstracts.ReservationService;
import com.example.tinyhouse.core.utilities.results.*;
import com.example.tinyhouse.dataAccess.abstracts.HouseDao;
import com.example.tinyhouse.dataAccess.abstracts.ReservationDao;
import com.example.tinyhouse.dataAccess.abstracts.UserDao;
import com.example.tinyhouse.entities.concretes.Comment;
import com.example.tinyhouse.entities.concretes.House;
import com.example.tinyhouse.entities.concretes.Reservation;
import com.example.tinyhouse.entities.concretes.User;
import com.example.tinyhouse.entities.dtos.*;
import com.example.tinyhouse.entities.enums.ReservationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationManager implements ReservationService {

    private final ReservationDao reservationDao;
    private final UserDao userDao;
    private final HouseDao houseDao;

    @Autowired
    public ReservationManager(ReservationDao reservationDao, UserDao userDao, HouseDao houseDao) {
        this.reservationDao = reservationDao;
        this.userDao = userDao;
        this.houseDao = houseDao;
    }

    @Override
    public DataResult<ReservationDto> add(ReservationCreateDto dto) {
        Optional<User> userOpt = userDao.findById(dto.getRenterId());
        Optional<House> houseOpt = houseDao.findById(dto.getHouseId());

        if (userOpt.isEmpty() || houseOpt.isEmpty()) {
            return new ErrorDataResult<>("Ev veya kullanıcı bulunamadı.");
        }

        Reservation reservation = new Reservation();
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDate.now());
        reservation.setUpdatedAt(LocalDate.now());
        reservation.setRenter(userOpt.get());
        reservation.setHouse(houseOpt.get());

        reservationDao.save(reservation);

        return new SuccessDataResult<>(mapToDto(reservation), "Rezervasyon başarıyla oluşturuldu.");
    }

    @Override
    public DataResult<ReservationDto> update(ReservationUpdateDto dto) {
        Optional<Reservation> opt = reservationDao.findById(dto.getId());
        if (opt.isEmpty()) {
            return new ErrorDataResult<>("Rezervasyon bulunamadı.");
        }

        Reservation res = opt.get();

        // Tarih alanlarına sadece DTO’da null değilse dokun
        if (dto.getStartDate() != null) {
            res.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            res.setEndDate(dto.getEndDate());
        }
        // Status alanına da aynı şekilde
        if (dto.getStatus() != null) {
            res.setStatus(dto.getStatus());
        }

        res.setUpdatedAt(LocalDate.now());
        reservationDao.save(res);

        return new SuccessDataResult<>(mapToDto(res), "Rezervasyon güncellendi.");
    }

    @Override
    public Result delete(int id, int requesterId) {
        Optional<Reservation> opt = reservationDao.findById(id);
        if (opt.isEmpty()) return new ErrorResult("Rezervasyon bulunamadı.");

        Reservation res = opt.get();

        if (res.getRenter().getId() != requesterId && res.getHouse().getHost().getId() != requesterId) {
            return new ErrorResult("Bu rezervasyonu silme yetkiniz yok.");
        }

        reservationDao.delete(res);
        return new SuccessResult("Rezervasyon silindi.");
    }

    @Override
    public DataResult<ReservationDto> getById(int id, int requesterId) {
        Optional<Reservation> opt = reservationDao.findById(id);
        if (opt.isEmpty()) return new ErrorDataResult<>("Rezervasyon bulunamadı.");

        Reservation res = opt.get();
        return new SuccessDataResult<>(mapToDto(res));
    }

    @Override
    public DataResult<List<ReservationListDto>> getAll(int requesterId) {
        List<ReservationListDto> list = reservationDao.findAll()
                .stream()
                .map(this::mapToListDto)
                .collect(Collectors.toList());
        return new SuccessDataResult<>(list);
    }

    @Override
    public DataResult<List<ReservationListDto>> getByUserId(int userId, int requesterId) {
        List<ReservationListDto> list = reservationDao.findByRenter_Id(userId)
                .stream()
                .map(this::mapToListDto)
                .collect(Collectors.toList());
        return new SuccessDataResult<>(list);
    }

    @Override
    public DataResult<List<ReservationListDto>> getByHouseId(int houseId, int requesterId) {
        List<ReservationListDto> list = reservationDao.findByHouse_Id(houseId)
                .stream()
                .map(this::mapToListDto)
                .collect(Collectors.toList());
        return new SuccessDataResult<>(list);
    }

    private ReservationDto mapToDto(Reservation r) {
        ReservationDto dto = new ReservationDto();
        dto.setId(r.getId());
        dto.setStartDate(r.getStartDate());
        dto.setEndDate(r.getEndDate());
        dto.setStatus(r.getStatus());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setUpdatedAt(r.getUpdatedAt());

        dto.setRenter(new UserDto(
                r.getRenter().getId(),
                r.getRenter().getFirstName(),
                r.getRenter().getLastName(),
                r.getRenter().getEmail(),
                r.getRenter().getRole(),
                r.getRenter().isActive()
        ));

        dto.setHouse(mapToHouseListDto(r.getHouse()));

        return dto;
    }

    private HouseListDto mapToHouseListDto(House house) {
        int commentCount = house.getCommentList().size();
        double averageRating = house.getCommentList().stream()
                .mapToInt(Comment::getRating)
                .average().orElse(0.0);

        HouseListDto dto = new HouseListDto();
        dto.setId(house.getId());
        dto.setTitle(house.getTitle());
        dto.setDescription(house.getDescription());
        dto.setPrice(house.getPrice());
        dto.setLocation(house.getLocation());
        dto.setStatus(house.getStatus());
        dto.setAvailableFrom(house.getAvailableFrom());
        dto.setAvailableTo(house.getAvailableTo());
        dto.setCommentCount(commentCount);
        dto.setAverageRating(averageRating);
        return dto;
    }

    private ReservationListDto mapToListDto(Reservation reservation) {
        // Kullanıcı DTO
        User renter = reservation.getRenter();
        UserListDto renterDto = new UserListDto();
        renterDto.setId(renter.getId());
        renterDto.setFirstName(renter.getFirstName());
        renterDto.setLastName(renter.getLastName());
        renterDto.setEmail(renter.getEmail());
        renterDto.setRole(renter.getRole());
        renterDto.setActive(renter.isActive());

        // Ev DTO
        House house = reservation.getHouse();
        int commentCount = house.getCommentList().size();
        double averageRating = house.getCommentList().stream()
                .mapToInt(Comment::getRating)
                .average()
                .orElse(0.0);

        HouseListDto houseDto = new HouseListDto();
        houseDto.setId(house.getId());
        houseDto.setTitle(house.getTitle());
        houseDto.setDescription(house.getDescription());
        houseDto.setPrice(house.getPrice());
        houseDto.setLocation(house.getLocation());
        houseDto.setStatus(house.getStatus());
        houseDto.setAvailableFrom(house.getAvailableFrom());
        houseDto.setAvailableTo(house.getAvailableTo());
        houseDto.setCommentCount(commentCount);
        houseDto.setAverageRating(averageRating);

        // Reservation DTO
        ReservationListDto dto = new ReservationListDto();
        dto.setId(reservation.getId());
        dto.setStartDate(reservation.getStartDate());
        dto.setEndDate(reservation.getEndDate());
        dto.setStatus(reservation.getStatus());
        dto.setRenter(renterDto);
        dto.setHouse(houseDto);

        return dto;
    }
}
