package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("DELETE FROM Item AS it WHERE it.owner like ?1")
    void deleteByOwner(long id);

    List<Item> findByOwner(Long userId);

}
