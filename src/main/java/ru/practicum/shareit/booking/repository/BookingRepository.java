package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.util.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item i " +
            "WHERE i.id = ?1 AND NOT(?2 > b.endDate) AND NOT(?3 < b.startDate)")
    List<Booking> findAllIntersectionTime(Long itemId, LocalDateTime startDate, LocalDateTime endDate);

    List<Booking> findAllByItem_idAndStatusAndItem_owner_id(Long itemId, BookingStatus status, Long userId);

    List<Booking> findAllByItem_idInAndStatusAndItem_owner_id(List<Long> itemIds, BookingStatus status, Long userId);

    List<Booking> findAllByBooker_idOrderByStartDateDesc(Long userId);

    List<Booking> findAllByStatusAndBooker_idOrderByStartDateDesc(BookingStatus status, Long userId);

    List<Booking> findAllByStartDateAfterAndBooker_idOrderByStartDateDesc(LocalDateTime dateTime, Long userId);

    List<Booking> findAllByEndDateBeforeAndBooker_idOrderByStartDateDesc(LocalDateTime dateTime, Long userId);

    List<Booking> findAllByEndDateBeforeAndBooker_idAndItem_idAndStatus(LocalDateTime dateTime, Long bookerId,
                                                                        Long itemId, BookingStatus status);

    List<Booking> findAllByStartDateBeforeAndEndDateAfterAndBooker_idOrderByStartDateDesc(LocalDateTime dateTimeFrom,
                                                                                          LocalDateTime dateTimeTo, Long userId);

    List<Booking> findAllByItem_Owner_idOrderByStartDateDesc(Long userId);

    List<Booking> findAllByStatusAndItem_Owner_idOrderByStartDateDesc(BookingStatus status, Long userId);

    List<Booking> findAllByStartDateAfterAndItem_Owner_idOrderByStartDateDesc(LocalDateTime dateTime, Long userId);

    List<Booking> findAllByEndDateBeforeAndItem_Owner_idOrderByStartDateDesc(LocalDateTime dateTime, Long userId);

    List<Booking> findAllByStartDateBeforeAndEndDateAfterAndItem_Owner_idOrderByStartDateDesc(LocalDateTime dateTimeFrom,
                                                                                              LocalDateTime dateTimeTo, Long userId);
}