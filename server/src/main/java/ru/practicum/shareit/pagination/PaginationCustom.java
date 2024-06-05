package ru.practicum.shareit.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.exeption.InvalidRequestException;

public class PaginationCustom {

    public static Pageable getPageableFromIndex(int fromIndex, int size) {
        if (fromIndex < 0 || size < 1) {
            throw new InvalidRequestException("Некорректные параметры пагинации");
        }
        return PageRequest.of(fromIndex / size, size);
    }
}
