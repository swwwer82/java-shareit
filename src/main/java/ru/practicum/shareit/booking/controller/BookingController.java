package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.util.BookingState;
import ru.practicum.shareit.pagination.PaginationCustom;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @RequestBody BookingCreateDto bookingCreateDto) {
        log.info("Создание бронирования {}, Id пользователя {}", bookingCreateDto, userId);
        BookingDto bookingDto = bookingService.create(userId, bookingCreateDto);
        return ResponseEntity.ok(bookingDto);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<BookingDto> approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PathVariable Long bookingId,
                                                     @RequestParam boolean approved) {
        log.info("Пользователь {} изменил  подтвержение {} бронирования {}", userId, approved, bookingId);
        BookingDto bookingDto = bookingService.approveBooking(userId, bookingId, approved);
        return ResponseEntity.ok(bookingDto);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<BookingDto> get(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long bookingId) {
        log.info("Получение бронирования {} пользователя {}", bookingId, userId);
        BookingDto bookingDto = bookingService.getById(userId, bookingId);
        return ResponseEntity.ok(bookingDto);
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "all") String state,
                                                   @RequestParam(value = "from", defaultValue = "0") int fromIndex,
                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Получить все бронирования бронировщика {}, состояние {}", userId, state);
        List<BookingDto> bookingDtos = bookingService.getAll(userId, BookingState.fromStringIgnoreCase(state), false,
                PaginationCustom.getPageableFromIndex(fromIndex, size));
        return ResponseEntity.ok(bookingDtos);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getAllOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(defaultValue = "all") String state,
                                                        @RequestParam(value = "from", defaultValue = "0") int fromIndex,
                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Получить все бронирования владельца {}, состояние {}", userId, state);
        List<BookingDto> bookingDtos = bookingService.getAll(userId, BookingState.fromStringIgnoreCase(state), true,
                PaginationCustom.getPageableFromIndex(fromIndex, size));
        return ResponseEntity.ok(bookingDtos);
    }
}