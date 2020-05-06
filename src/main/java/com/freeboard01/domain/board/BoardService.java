package com.freeboard01.domain.board;

import com.freeboard01.api.board.BoardForm;
import com.freeboard01.api.user.UserForm;
import com.freeboard01.domain.user.UserEntity;
import com.freeboard01.domain.user.UserRepository;
import com.freeboard01.domain.user.enums.UserRole;
import com.freeboard01.domain.user.specification.IsHaveAdminRoles;
import com.freeboard01.domain.user.specification.IsWriterEqualToUserLoggedIn;
import com.freeboard01.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class BoardService {

    private BoardRepository boardRepository;
    private UserRepository userRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    public Page<BoardEntity> get(Pageable pageable) {
        return boardRepository.findAll(PageUtil.convertToZeroBasePageWithSort(pageable));
    }

    public BoardEntity post(BoardForm boardForm, UserForm userForm) {
        UserEntity user = userRepository.findByAccountId(userForm.getAccountId());
        if (user == null) {
            new Exception();
        }
        return boardRepository.save(boardForm.convertBoardEntity(user));
    }

    public Boolean update(BoardForm boardForm, UserForm userForm, long id) {
        UserEntity user = userRepository.findByAccountId(userForm.getAccountId());
        if (user == null) {
            new Exception();
        }
        BoardEntity target = boardRepository.findById(id).get();
        if (IsWriterEqualToUserLoggedIn.confirm(target.getWriter(), user) || IsHaveAdminRoles.confirm(user)) {
            target.update(boardForm.convertBoardEntity(target.getWriter()));
            return true;
        }
        return false;
    }

    public boolean delete(long id, UserForm userForm) {
        UserEntity user = userRepository.findByAccountId(userForm.getAccountId());
        if (user == null) {
            new Exception();
        }
        BoardEntity target = boardRepository.findById(id).get();
        if (IsWriterEqualToUserLoggedIn.confirm(target.getWriter(), user) || IsHaveAdminRoles.confirm(user)) {
            boardRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
