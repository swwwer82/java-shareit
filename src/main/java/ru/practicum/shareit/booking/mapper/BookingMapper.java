package ru.practicum.shareit.booking.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.util.BookingStatus;

import java.util.List;

@Mapper(componentModel = "spring", imports = {BookingStatus.class})

public interface BookingMapper {

    @Mapping(target = "item.id", source = "bookingCreateDto.itemId")
    @Mapping(source = "userId", target = "booker.id")
    @Mapping(target = "startDate", source = "bookingCreateDto.start")
    @Mapping(target = "endDate", source = "bookingCreateDto.end")
    @Mapping(target = "status", expression = "java(BookingStatus.WAITING)")
    Booking toBooking(Long userId, BookingCreateDto bookingCreateDto);

    @Mapping(source = "startDate", target = "start")
    @Mapping(source = "endDate", target = "end")
    BookingDto toBookingDto(Booking booking);

    List<BookingDto> toBookingDto(List<Booking> bookings);

    @Mapping(target = "bookerId", source = "booker.id")
    BookingItemDto toBookingItemDto(Booking booking);

    List<BookingItemDto> toBookingItemDto(List<Booking> bookings);
}