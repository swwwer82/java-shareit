package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingItemDto {
    private final Long id;
    private final Long bookerId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
}