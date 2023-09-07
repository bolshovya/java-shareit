package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItem(Item item);

    List<Booking> findAllByBookerAndStatusOrderByStartDesc(User booker, BookingState status, Pageable pageable);

    List<Booking> findAllByBookerOrderByStartDesc(User booker, Pageable pageable);

    List<Booking> findAllByBookerAndEndBeforeOrderByStartDesc(User booker, LocalDateTime time, Pageable pageable);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(User booker,
                                                                           LocalDateTime time1,
                                                                           LocalDateTime time2,
                                                                           Pageable pageable);

    List<Booking> findAllByBookerAndEndAfterOrderByStartDesc(User booker, LocalDateTime time, Pageable pageable);

    List<Booking> findAllByBookerAndStatusAndStartBefore(User booker, BookingState status, LocalDateTime time);

    List<Booking> findAllByItemOwnerAndStatusOrderByStartDesc(User booker, BookingState status, Pageable pageable);

    List<Booking> findAllByItemOwnerOrderByStartDesc(User booker, Pageable pageable);

    List<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(User booker, LocalDateTime time, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(User booker,
                                                                              LocalDateTime time1,
                                                                              LocalDateTime time2,
                                                                              Pageable pageable);

    List<Booking> findAllByItemOwnerAndEndAfterOrderByStartDesc(User booker, LocalDateTime time, Pageable pageable);


}
