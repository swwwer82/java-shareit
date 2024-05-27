package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.util.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class}, imports = {BookingStatus.class})
public interface BookingMapper {

    @Mapping(source = "userId", target = "booker.id")
    @Mapping(source = "bookingCreateDto.itemId", target = "item.id")
    @Mapping(source = "bookingCreateDto.start", target = "startDate")
    @Mapping(source = "bookingCreateDto.end", target = "endDate")
    @Mapping(target = "status", expression = "java(BookingStatus.WAITING)")
    Booking toBooking(Long userId, BookingCreateDto bookingCreateDto);

    @Mapping(source = "startDate", target = "start")
    @Mapping(source = "endDate", target = "end")
    BookingDto toBookingDto(Booking booking);

    List<BookingDto> toBookingDto(List<Booking> bookings);

    @Mapping(source = "booker.id", target = "bookerId")
    BookingItemDto toBookingItemDto(Booking booking);
}