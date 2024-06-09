package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long userId, Pageable pageable);

    @Query("SELECT i " +
            "FROM Item as i " +
            "WHERE i.available = true AND " +
            "(LOWER(i.name) LIKE LOWER(CONCAT('%', ?1, '%') ) OR " +
            "LOWER(i.description) LIKE LOWER(CONCAT('%', ?1, '%') ))")
    List<Item> search(String text, Pageable pageable);

    List<Item> findAllByRequest_idIn(List<Long> requestId);

    List<Item> findAllByRequest_id(Long requestId);
}