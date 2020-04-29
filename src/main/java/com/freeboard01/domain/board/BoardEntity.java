package com.freeboard01.domain.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "board")
@NoArgsConstructor
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

    @Column
    private String contents;

    @Builder
    public BoardEntity(String user, String password, String contents){
        this.user = user;
        this.password = password;
        this.contents = contents;
    }
}
