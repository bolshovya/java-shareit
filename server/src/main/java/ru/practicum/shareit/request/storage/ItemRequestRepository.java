package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequestorId(Long userId, Pageable pageable);

    List<ItemRequest> findAllByRequestor(User requestor, Pageable pageable);

    List<ItemRequest> findAllByRequestorNot(User requestor, Pageable pageable);

    Optional<ItemRequest> findByRequestorId(Long userId);

    Optional<ItemRequest> findByRequestor(User requestor);
}
