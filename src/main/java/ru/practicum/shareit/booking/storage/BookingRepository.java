package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerAndStatusOrderByStartDesc(User booker, BookingStatus status);

    List<Booking> findAllByBookerOrderByStartDesc(User booker);

    List<Booking> findAllByBookerAndEndBeforeOrderByStartDesc(User booker, LocalDateTime time);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(User booker, LocalDateTime time1, LocalDateTime time2);

    List<Booking> findAllByBookerAndEndAfterOrderByStartDesc(User booker, LocalDateTime time);
}
