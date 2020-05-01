package com.freeboard01.domain.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
public class BoardServiceUnitTest {

    @InjectMocks
    private BoardService sut;

    @Mock
    private BoardRepository mockBoardRepo;

    @Test
    public void delete1() {
        final Long ID = 999L;

        BoardEntity entity = BoardEntity.builder().password("1234").user("user").contents("contents").title("title").build();
        entity.setId(ID);

        given(mockBoardRepo.findById(anyLong())).willReturn(Optional.of(entity));

        sut.delete(ID, "wrongPassword");
        verify(mockBoardRepo, never()).deleteById(anyLong());
    }

    @Test
    public void delete2() {
        final Long ID = 999L;
        final String PASSWORD = "myPass";
        BoardEntity entity = BoardEntity.builder().password(PASSWORD).user("user").contents("contents").title("title").build();
        entity.setId(ID);

        given(mockBoardRepo.findById(anyLong())).willReturn(Optional.of(entity));
        doNothing().when(mockBoardRepo).deleteById(anyLong());

        sut.delete(ID, PASSWORD);
        verify(mockBoardRepo, times(1)).deleteById(anyLong());
    }

}
