package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BookingItemDto {
    private final Long id;
    private final Long bookerId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
}
