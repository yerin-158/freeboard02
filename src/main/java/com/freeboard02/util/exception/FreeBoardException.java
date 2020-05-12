package com.freeboard02.util.exception;

import com.freeboard02.domain.BaseExceptionType;
import lombok.Getter;

public class FreeBoardException extends RuntimeException {

    @Getter
    private BaseExceptionType exceptionType;

    public FreeBoardException(BaseExceptionType exceptionType){
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }

}
