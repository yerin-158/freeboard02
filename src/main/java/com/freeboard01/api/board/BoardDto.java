package com.freeboard01.api.board;

import com.freeboard01.domain.board.BoardEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BoardDto{

    private long id;
    private String user;
    private String password;
    private String contents;
    private String title;

    public BoardDto(BoardEntity board) {
        this.id = board.getId();
        this.user = board.getUser();
        this.password = board.getPassword();
        this.contents = board.getContents();
        this.title = board.getTitle();
    }

    public static BoardDto of(BoardEntity board) {
        return new BoardDto(board);
    }
}
