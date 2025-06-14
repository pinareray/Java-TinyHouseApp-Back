package com.example.tinyhouse.business.concretes;

import com.example.tinyhouse.business.abstracts.PaymentService;
import com.example.tinyhouse.core.utilities.results.*;
import com.example.tinyhouse.dataAccess.abstracts.PaymentDao;
import com.example.tinyhouse.dataAccess.abstracts.ReservationDao;
import com.example.tinyhouse.dataAccess.abstracts.UserDao;
import com.example.tinyhouse.entities.concretes.Comment;
import com.example.tinyhouse.entities.concretes.Payment;
import com.example.tinyhouse.entities.concretes.Reservation;
import com.example.tinyhouse.entities.concretes.User;
import com.example.tinyhouse.entities.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentManager implements PaymentService {

    private final PaymentDao paymentDao;
    private final ReservationDao reservationDao;
    private final UserDao userDao;

    @Autowired
    public PaymentManager(PaymentDao paymentDao, ReservationDao reservationDao, UserDao userDao) {
        this.paymentDao = paymentDao;
        this.reservationDao = reservationDao;
        this.userDao = userDao;
    }

    @Override
    public Result add(PaymentCreateDto dto) {
        Optional<User> userOpt = userDao.findById(dto.getUserId());
        Optional<Reservation> reservationOpt = reservationDao.findById(dto.getReservationId());

        if (userOpt.isEmpty() || reservationOpt.isEmpty()) {
            return new ErrorResult("Kullanıcı veya rezervasyon bulunamadı.");
        }

        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setUser(userOpt.get());
        payment.setReservation(reservationOpt.get());

        paymentDao.save(payment);
        return new SuccessResult("Ödeme başarıyla kaydedildi.");
    }

    @Override
    public Result delete(int id, int requesterId) {
        Optional<Payment> paymentOpt = paymentDao.findById(id);
        if (paymentOpt.isEmpty()) return new ErrorResult("Ödeme bulunamadı.");

        Payment payment = paymentOpt.get();

        if (payment.getUser().getId() != requesterId) {
            return new ErrorResult("Bu ödemeyi silme yetkiniz yok.");
        }

        paymentDao.delete(payment);
        return new SuccessResult("Ödeme silindi.");
    }

    @Override
    public DataResult<PaymentDto> getById(int id, int requesterId) {
        Optional<Payment> opt = paymentDao.findById(id);
        if (opt.isEmpty()) return new ErrorDataResult<>("Ödeme bulunamadı.");

        return new SuccessDataResult<>(mapToDto(opt.get()));
    }

    @Override
    public DataResult<List<PaymentListDto>> getAll(int requesterId) {
        List<PaymentListDto> list = paymentDao.findAll()
                .stream().map(this::mapToListDto).collect(Collectors.toList());
        return new SuccessDataResult<>(list);
    }

    @Override
    public DataResult<List<PaymentListDto>> getByUserId(int userId, int requesterId) {
        List<PaymentListDto> list = paymentDao.findByUser_Id(userId)
                .stream().map(this::mapToListDto).collect(Collectors.toList());
        return new SuccessDataResult<>(list);
    }

    @Override
    public DataResult<List<PaymentListDto>> getByReservationId(int reservationId, int requesterId) {
        List<PaymentListDto> list = paymentDao.findByReservation_Id(reservationId)
                .stream().map(this::mapToListDto).collect(Collectors.toList());
        return new SuccessDataResult<>(list);
    }

    @Override
    public DataResult<List<MonthlyIncomeDto>> getMonthlyIncomeByHostId(int hostId) {
        List<Payment> allPayments = paymentDao.findAll();

        List<MonthlyIncomeDto> monthlyIncomes = allPayments.stream()
                .filter(p -> p.getReservation() != null &&
                        p.getReservation().getHouse() != null &&
                        p.getReservation().getHouse().getHost().getId() == hostId)
                .collect(Collectors.groupingBy(
                        p -> p.getPaymentDate().getYear() + "-" + String.format("%02d", p.getPaymentDate().getMonthValue()),
                        Collectors.summingDouble(Payment::getAmount)
                ))
                .entrySet()
                .stream()
                .map(entry -> new MonthlyIncomeDto(entry.getKey(), entry.getValue()))
                .sorted((a, b) -> a.getMonth().compareTo(b.getMonth()))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(monthlyIncomes, "Aylık gelir başarıyla getirildi.");
    }

    private PaymentDto mapToDto(Payment p) {
        PaymentDto dto = new PaymentDto();
        dto.setId(p.getId());
        dto.setAmount(p.getAmount());
        dto.setPaymentDate(p.getPaymentDate());

        dto.setUser(new UserDto(
                p.getUser().getId(),
                p.getUser().getFirstName(),
                p.getUser().getLastName(),
                p.getUser().getEmail(),
                p.getUser().getRole(),
                p.getUser().isActive()
        ));

        dto.setReservation(mapToDto(p.getReservation())); // Yardımcı sınıf
        return dto;
    }

    private PaymentListDto mapToListDto(Payment p) {
        PaymentListDto dto = new PaymentListDto();
        dto.setId(p.getId());
        dto.setAmount(p.getAmount());
        dto.setPaymentDate(p.getPaymentDate());

        // status alanı modelde yoksa bu satır çıkarılmalı
        // dto.setStatus(p.getStatus());

        dto.setUser(new UserDto(
                p.getUser().getId(),
                p.getUser().getFirstName(),
                p.getUser().getLastName(),
                p.getUser().getEmail(),
                p.getUser().getRole(),
                p.getUser().isActive()
        ));

        dto.setReservation(mapToDto(p.getReservation()));
        return dto;
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

        dto.setHouse(new HouseListDto(
                r.getHouse().getId(),
                r.getHouse().getTitle(),
                r.getHouse().getDescription(),
                r.getHouse().getPrice(),
                r.getHouse().getLocation(),
                r.getHouse().getStatus(),
                r.getHouse().getAvailableFrom(),
                r.getHouse().getAvailableTo(),
                r.getHouse().getCommentList().size(),
                r.getHouse().getCommentList().stream()
                        .mapToInt(Comment::getRating).average().orElse(0.0),
                new UserDto(  // Host bilgisi
                        r.getHouse().getHost().getId(),
                        r.getHouse().getHost().getFirstName(),
                        r.getHouse().getHost().getLastName(),
                        r.getHouse().getHost().getEmail(),
                        r.getHouse().getHost().getRole(),
                        r.getHouse().getHost().isActive()
                )
        ));

        return dto;
    }
}
