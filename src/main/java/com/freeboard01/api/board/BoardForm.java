package com.freeboard01.api.board;

import com.freeboard01.domain.board.BoardEntity;
import com.freeboard01.domain.user.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardForm {

    private String contents;
    private String title;

    @Builder
    public BoardForm(String contents, String title){
        this.contents = contents;
        this.title = title;
    }

    public BoardEntity convertBoardEntity(UserEntity user){
        return BoardEntity.builder()
                .writer(user)
                .contents(this.contents)
                .title(this.title)
                .build();
    }
}
