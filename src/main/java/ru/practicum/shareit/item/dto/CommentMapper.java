package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.Comment;

public class CommentMapper {

    public static Comment getComment(CommentDto commentDto) {
        return Comment.builder()
                .text(commentDto.getText())
                .created(commentDto.getCreated())
                .build();
    }

    public static CommentDto getCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
