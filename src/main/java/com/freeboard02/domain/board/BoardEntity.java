package com.freeboard02.domain.board;

import com.freeboard02.domain.BaseEntity;
import com.freeboard02.domain.user.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class BoardEntity extends BaseEntity {

    private UserEntity writer;

    @Setter
    private String contents;

    private String title;

    @Builder
    public BoardEntity(UserEntity writer, String contents, String title){
        this.writer = writer;
        this.contents = contents;
        this.title = title;
    }

    public BoardEntity update(BoardEntity newBoard){
        this.writer = newBoard.getWriter();
        this.contents = newBoard.getContents();
        this.title = newBoard.getTitle();
        return this;
    }
}
