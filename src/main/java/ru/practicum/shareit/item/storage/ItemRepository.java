package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("FROM Item it WHERE it.owner LIKE ?1")
    List<Item> findByOwner(User user);

    @Query("FROM Item it WHERE LOWER (it.name) LIKE CONCAT('%', LOWER(?1), '%') OR LOWER(it.description) LIKE CONCAT('%', LOWER(?1), '%') AND it.available=true")
    List<Item> findByNameOrDescriptionContaining(String text);

}
