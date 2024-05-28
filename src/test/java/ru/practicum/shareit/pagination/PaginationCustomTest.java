package ru.practicum.shareit.pagination;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.exeption.NotValidRequestException;

import static org.junit.jupiter.api.Assertions.*;

class PaginationCustomTest {

    @Test
    void testGetPageableFromIndex() {
        Pageable actualPageableFromIndex = PaginationCustom.getPageableFromIndex(1, 3);

        assertEquals(0, actualPageableFromIndex.getPageNumber());
        assertEquals(3, actualPageableFromIndex.getPageSize());
        Sort sort = actualPageableFromIndex.getSort();
        assertTrue(sort.toList().isEmpty());
        Pageable firstResult = actualPageableFromIndex.first();
        assertEquals(actualPageableFromIndex, firstResult);
        assertSame(sort, ((PageRequest) firstResult.next()).previous().next().previous().getSort());
    }

    @Test
    void testGetPageableFromIndex2() {
        assertThrows(NotValidRequestException.class, () -> PaginationCustom.getPageableFromIndex(0, 0));
    }

    @Test
    void testGetPageableFromIndex3() {
        assertThrows(NotValidRequestException.class, () -> PaginationCustom.getPageableFromIndex(-1, 1));
    }
}
