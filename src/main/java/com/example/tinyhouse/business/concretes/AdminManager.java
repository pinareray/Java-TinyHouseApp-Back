package com.example.tinyhouse.business.concretes;

import com.example.tinyhouse.business.abstracts.AdminService;
import com.example.tinyhouse.core.utilities.results.DataResult;
import com.example.tinyhouse.core.utilities.results.SuccessDataResult;
import com.example.tinyhouse.dataAccess.abstracts.PaymentDao;
import com.example.tinyhouse.dataAccess.abstracts.ReservationDao;
import com.example.tinyhouse.dataAccess.abstracts.UserDao;
import com.example.tinyhouse.entities.dtos.AdminSummaryDto;
import com.example.tinyhouse.entities.dtos.SystemStatisticsDto;
import com.example.tinyhouse.entities.enums.ReservationStatus;
import com.example.tinyhouse.entities.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AdminManager implements AdminService {

    // ───────────────────────────────────────────────────────────────
    private final UserDao        userDao;
    private final ReservationDao reservationDao;
    private final PaymentDao     paymentDao;
    // ───────────────────────────────────────────────────────────────

    @Autowired
    public AdminManager(UserDao u, ReservationDao r, PaymentDao p) {
        this.userDao        = u;
        this.reservationDao = r;
        this.paymentDao     = p;
    }

    @Override
    public DataResult<AdminSummaryDto> getSystemSummary() {
        AdminSummaryDto summary = new AdminSummaryDto();

        summary.setTotalUsers((int) userDao.count());
        summary.setTotalHosts(userDao.countByRole(UserRole.HOST));
        summary.setTotalRenters(userDao.countByRole(UserRole.RENTER));
        summary.setActiveReservations(
                reservationDao.countByStatus(ReservationStatus.APPROVED)
        );

        YearMonth now = YearMonth.now();
        // ▶ Stored procedure çağrısı:
        Integer  resCount = reservationDao.getMonthlyReservations(now.getYear(), now.getMonthValue());
        BigDecimal incomeBd = paymentDao.getMonthlyIncome(now.getYear(), now.getMonthValue());

        summary.setMonthlyReservations(resCount != null ? resCount : 0);
        summary.setMonthlyIncome(incomeBd != null ? incomeBd.doubleValue() : 0.0);

        return new SuccessDataResult<>(summary);
    }

    @Override
    public DataResult<SystemStatisticsDto> getSystemStatistics() {
        SystemStatisticsDto dto = new SystemStatisticsDto();

        Map<String, Integer> userCounts = new HashMap<>();
        userCounts.put("ADMIN",  userDao.countByRole(UserRole.ADMIN));
        userCounts.put("HOST",   userDao.countByRole(UserRole.HOST));
        userCounts.put("RENTER", userDao.countByRole(UserRole.RENTER));
        dto.setUserCounts(userCounts);

        String[] months = {
                "Ocak","Şubat","Mart","Nisan","Mayıs","Haziran",
                "Temmuz","Ağustos","Eylül","Ekim","Kasım","Aralık"
        };

        Map<String,Integer>   monthlyRes   = new LinkedHashMap<>();
        Map<String,Double>    monthlyInc   = new LinkedHashMap<>();
        int currentYear = YearMonth.now().getYear();

        for (int m = 1; m <= 12; m++) {
            // ✔ SP çağrısı
            Integer count = reservationDao.getMonthlyReservations(currentYear, m);
            BigDecimal total = paymentDao.getMonthlyIncome(currentYear, m);

            monthlyRes.put(months[m-1], count != null ? count : 0);
            monthlyInc.put(months[m-1], total  != null ? total.doubleValue() : 0.0);
        }

        dto.setMonthlyReservations(monthlyRes);
        dto.setMonthlyIncome(monthlyInc);

        return new SuccessDataResult<>(dto, "Sistem istatistikleri getirildi");
    }
}
