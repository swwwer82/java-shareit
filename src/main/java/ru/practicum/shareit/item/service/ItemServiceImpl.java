package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.util.BookingStatus;
import ru.practicum.shareit.exception.exeption.NotFoundException;
import ru.practicum.shareit.exception.exeption.NotValidRequestException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final RequestService requestService;

    @Override
    public ItemDto get(Long itemId, Long userId) {
        ItemDto itemDto = itemMapper.toItemDto(validationFindItemById(itemId));

        itemDto.setComments(commentMapper.toCommentDto(commentRepository.findAllByItem_id(itemId)));

        List<Booking> bookings = bookingRepository.findAllByItem_idAndStatusAndItem_owner_id(itemId, BookingStatus.APPROVED, userId);

        if (!bookings.isEmpty()) {

            LocalDateTime currentDateTime = LocalDateTime.now();

            bookings.stream()
                    .filter(b -> b.getStartDate().isBefore(currentDateTime))
                    .max(Comparator.comparing(Booking::getStartDate))
                    .ifPresent(lastBooking -> itemDto.setLastBooking(bookingMapper.toBookingItemDto(lastBooking)));

            bookings.stream()
                    .filter(b -> b.getStartDate().isAfter(currentDateTime))
                    .min(Comparator.comparing(Booking::getStartDate))
                    .ifPresent(nextBooking -> itemDto.setNextBooking(bookingMapper.toBookingItemDto(nextBooking)));
        }

        return itemDto;
    }

    @Override
    public List<ItemDto> getAll(Long userId, Pageable pageable) {

        Map<Long, ItemDto> itemMap = new HashMap<>(itemRepository.findAllByOwnerId(userId, pageable).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toMap(ItemDto::getId, itemDto -> itemDto)));

        List<Long> itemIds = itemMap.values().stream().map(ItemDto::getId).collect(Collectors.toList());

        List<Booking> bookings = bookingRepository.findAllByItem_idInAndStatusAndItem_owner_id(itemIds, BookingStatus.APPROVED, userId);

        if (!bookings.isEmpty()) {

            LocalDateTime currentDateTime = LocalDateTime.now();

            bookings.stream()
                    .filter(b -> b.getEndDate().isBefore(currentDateTime))
                    .max(Comparator.comparing(Booking::getEndDate))
                    .ifPresent(lastBooking -> itemMap.get(lastBooking.getItem().getId())
                            .setLastBooking(bookingMapper.toBookingItemDto(lastBooking)));

            bookings.stream()
                    .filter(b -> b.getStartDate().isAfter(currentDateTime))
                    .min(Comparator.comparing(Booking::getStartDate))
                    .ifPresent(nextBooking -> itemMap.get(nextBooking.getItem().getId())
                            .setNextBooking(bookingMapper.toBookingItemDto(nextBooking)));
        }

        return new ArrayList<>(itemMap.values());
    }

    @Override
    @Transactional
    public ItemDto create(Long userId, ItemCreateDto itemCreateDto) {
        User user = userService.validationFindUserById(userId);
        Item item = itemMapper.toItem(itemCreateDto);
        item.setOwner(user);

        if (itemCreateDto.getRequestId() != null) {
            item.setRequest(requestService.validateFindRequestById(itemCreateDto.getRequestId()));
        }

        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, Long itemId, ItemUpdateDto itemUpdateDto) {
        userService.validationFindUserById(userId);
        Item itemSaved = validationOwnerUserById(userId, itemId);
        Item item = itemMapper.toItem(itemUpdateDto);

        if (item.getName() != null) {
            itemSaved.setName(item.getName());
        }

        if (item.getDescription() != null) {
            itemSaved.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            itemSaved.setAvailable(item.getAvailable());
        }

        return itemMapper.toItemDto(itemSaved);
    }

    @Override
    public List<ItemDto> search(String text, Pageable pageable) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        return itemRepository.search(text, pageable).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Item validationFindItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Не найден объект с ид %d", itemId)));
    }

    @Override
    public Item validationOwnerUserById(Long userId, Long itemId) {
        Item item = validationFindItemById(itemId);
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new NotFoundException("Пользователь не является владельцем");
        }
        return item;
    }

    @Override
    @Transactional
    public CommentDto createComment(Long userId, Long itemId, CommentCreateDto commentCreateDto) {
        User user = userService.validationFindUserById(userId);
        Item item = validationFindItemById(itemId);

        List<Booking> bookings = bookingRepository.findAllByEndDateBeforeAndBooker_idAndItem_idAndStatus(LocalDateTime.now(),
                userId, itemId, BookingStatus.APPROVED);

        if (bookings.isEmpty()) {
            throw new NotValidRequestException("Объект не доступен");
        }

        Comment comment = commentRepository.save(commentMapper.toComment(userId, itemId, commentCreateDto));
        comment.setItem(item);
        comment.setAuthor(user);

        return commentMapper.toCommentDto(comment);
    }
}
