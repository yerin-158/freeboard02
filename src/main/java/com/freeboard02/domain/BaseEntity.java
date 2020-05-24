package com.freeboard02.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Setter
    protected long id;

    protected LocalDateTime createdAt;

    protected LocalDateTime updatedAt;

}
