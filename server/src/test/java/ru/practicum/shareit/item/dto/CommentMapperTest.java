package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

class CommentMapperTest {

    @Test
    void getComment() {

        CommentDto commentDto = CommentDto.builder().text("text").created(LocalDateTime.now()).build();

        Comment actual = CommentMapper.getComment(commentDto);
    }

    @Test
    void getCommentDto() {

        Comment comment = Comment.builder()
                .id(1L)
                .text("text")
                .created(LocalDateTime.now())
                .author(User.builder().name("Ivan").build())
                .build();

        CommentDto actual = CommentMapper.getCommentDto(comment);
    }
}