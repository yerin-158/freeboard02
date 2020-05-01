package com.freeboard01.api.board;

import com.freeboard01.domain.board.BoardEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardForm {

    private String user;
    private String password;
    private String contents;
    private String title;

    @Builder
    public BoardForm(String user, String password, String contents, String title){
        this.user = user;
        this.password = password;
        this.contents = contents;
        this.title = title;
    }

    public BoardEntity convertBoardEntity(){
        return BoardEntity.builder()
                .user(this.user)
                .password(this.password)
                .contents(this.contents)
                .title(this.title)
                .build();
    }
}
