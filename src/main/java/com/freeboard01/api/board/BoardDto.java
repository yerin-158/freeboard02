package com.freeboard01.api.board;

import com.freeboard01.domain.board.BoardEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class BoardDto {

    private String user;
    private String password;
    private String contents;

    public BoardDto(BoardEntity board) {
        this.user = board.getUser();
        this.password = board.getPassword();
        this.contents = board.getContents();
    }

    public static BoardDto of(BoardEntity board) {
        return new BoardDto(board);
    }
}
