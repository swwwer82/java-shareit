package ru.practicum.shareit.pagination;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exceptions.exception.NotValidRequestException;

public class PaginationCustom {

    public static Pageable getPageableFromIndex(int fromIndex, int size) {
        if (fromIndex < 0 || size < 1) {
            throw new NotValidRequestException("Некорректные параметры пагинации");
        }
        return PageRequest.of(fromIndex / size, size);
    }
}
