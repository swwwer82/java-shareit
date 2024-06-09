package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.util.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Valid @RequestBody BookingCreateDto bookingCreateDto) {
        log.info("Create booking {}, userid {}", bookingCreateDto, userId);
        return client.create(userId, bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long bookingId,
                                                 @RequestParam boolean approved) {
        log.info("User {} change approve {} booking {}", userId, approved, bookingId);
        return client.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable Long bookingId) {
        log.info("Get booking {} user {}", bookingId, userId);
        return client.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(defaultValue = "all") String state,
                                         @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int fromIndex,
                                         @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("Get All booking booker {}, state {}", userId, state);
        return client.getAll(userId, BookingState.fromStringIgnoreCase(state), false, fromIndex, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(defaultValue = "all") String state,
                                              @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int fromIndex,
                                              @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("Get All booking owner {}, state {}", userId, state);
        return client.getAll(userId, BookingState.fromStringIgnoreCase(state), true, fromIndex, size);
    }
}