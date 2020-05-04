package com.freeboard01.domain.board;

import com.freeboard01.api.user.UserForm;
import com.freeboard01.domain.user.UserEntity;
import com.freeboard01.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
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

    @Mock
    private UserRepository mockUserRepo;

    @Test
    @DisplayName("잘못된 비밀번호를 입력한 경우에는 삭제를 수행하지 않는다.")
    public void delete1() {
        final Long ID = 999L;

        UserEntity userEntity = UserEntity.builder().accountId("mockUser").password("mockPass").build();
        UserForm wrongUserForm = UserForm.builder().accountId("mockUser").password("wrongUser").build();
        BoardEntity boardEntity = BoardEntity.builder().contents("contents").title("title").writer(userEntity).build();
        boardEntity.setId(ID);

        given(mockBoardRepo.findById(anyLong())).willReturn(Optional.of(boardEntity));

        sut.delete(ID, wrongUserForm);
        verify(mockBoardRepo, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("올바른 비밀번호를 입력한 경우 삭제를 수행한다.")
    public void delete2() {
        final Long ID = 999L;
        final String PASSWORD = "myPass";

        UserEntity userEntity = UserEntity.builder().accountId("mockUser").password(PASSWORD).build();
        UserForm userForm = UserForm.builder().accountId("mockUser").password(PASSWORD).build();
        BoardEntity entity = BoardEntity.builder().writer(userEntity).contents("contents").title("title").build();
        entity.setId(ID);

        given(mockUserRepo.findByAccountId(anyString())).willReturn(userEntity);
        given(mockBoardRepo.findById(anyLong())).willReturn(Optional.of(entity));
        doNothing().when(mockBoardRepo).deleteById(anyLong());

        sut.delete(ID, userForm);
        verify(mockBoardRepo, times(1)).deleteById(anyLong());
    }

}
