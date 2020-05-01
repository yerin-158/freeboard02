package com.freeboard01.domain.board;

import com.freeboard01.api.board.BoardForm;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "board")
@NoArgsConstructor
@DynamicUpdate
public class BoardEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @Column
    private String user;

    @Column
    private String password;

    @Setter
    @Column
    private String contents;

    @Column
    private String title;

    @Builder
    public BoardEntity(String user, String password, String contents, String title){
        this.user = user;
        this.password = password;
        this.contents = contents;
        this.title = title;
    }

    public BoardEntity update(BoardEntity newBoard){
        this.user = newBoard.getUser();
        this.password = newBoard.getPassword();
        this.contents = newBoard.getContents();
        this.title = newBoard.getTitle();
        return this;
    }
}
