package com.freeboard02.domain.board;

import com.freeboard02.api.PageDto;
import com.freeboard02.api.board.BoardDto;
import com.freeboard02.api.board.BoardForm;
import com.freeboard02.api.user.UserForm;
import com.freeboard02.domain.board.enums.BoardExceptionType;
import com.freeboard02.domain.board.enums.SearchType;
import com.freeboard02.domain.user.UserEntity;
import com.freeboard02.domain.user.UserMapper;
import com.freeboard02.domain.user.enums.UserExceptionType;
import com.freeboard02.domain.user.specification.HaveAdminRoles;
import com.freeboard02.domain.user.specification.IsWriterEqualToUserLoggedIn;
import com.freeboard02.util.PageUtil;
import com.freeboard02.util.exception.FreeBoardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class BoardService {

    private BoardMapper boardMapper;
    private UserMapper userMapper;

    @Autowired
    public BoardService(BoardMapper boardMapper, UserMapper userMapper) {
        this.boardMapper = boardMapper;
        this.userMapper = userMapper;
    }

    public int getTotalSize() {
        return boardMapper.findTotalSize();
    }

    public int getTotalSizeForSearch(String keyword, SearchType type) {
        return boardMapper.findTotalSizeForSearch(type.name(), keyword);
    }

    public List<BoardEntity> get(Pageable pageable) {
        return boardMapper.findAllWithPaging(pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
    }

    public void post(BoardForm boardForm, UserForm userForm) {
        UserEntity user = Optional.of(userMapper.findByAccountId(userForm.getAccountId())).orElseThrow(() -> new FreeBoardException(UserExceptionType.NOT_FOUND_USER));
        boardMapper.save(boardForm.convertBoardEntity(user));
    }

    public void update(BoardForm boardForm, UserForm userForm, long id) {
        UserEntity user = Optional.of(userMapper.findByAccountId(userForm.getAccountId())).orElseThrow(() -> new FreeBoardException(UserExceptionType.NOT_FOUND_USER));
        BoardEntity target = Optional.of(boardMapper.findById(id).get()).orElseThrow(() -> new FreeBoardException(BoardExceptionType.NOT_FOUNT_CONTENTS));

        if (IsWriterEqualToUserLoggedIn.confirm(target.getWriter(), user) == false && HaveAdminRoles.confirm(user) == false) {
            throw new FreeBoardException(BoardExceptionType.NO_QUALIFICATION_USER);
        }

        target.update(boardForm.convertBoardEntity(target.getWriter()));
    }

    public void delete(long id, UserForm userForm) {
        UserEntity user = Optional.of(userMapper.findByAccountId(userForm.getAccountId())).orElseThrow(() -> new FreeBoardException(UserExceptionType.NOT_FOUND_USER));
        BoardEntity target = Optional.of(boardMapper.findById(id).get()).orElseThrow(() -> new FreeBoardException(BoardExceptionType.NOT_FOUNT_CONTENTS));

        if (IsWriterEqualToUserLoggedIn.confirm(target.getWriter(), user) == false && HaveAdminRoles.confirm(user) == false) {
            throw new FreeBoardException(BoardExceptionType.NO_QUALIFICATION_USER);
        }

        boardMapper.deleteById(id);
    }

    public PageDto<BoardDto> search(Pageable pageable, String keyword, SearchType type) {
        List<BoardEntity> boardEntities = new ArrayList<>();
        int totalSize = 0;
        if (type.equals(SearchType.WRITER)) {
            List<UserEntity> userEntityList = userMapper.findByAccountIdLike(keyword);
            if (userEntityList.size() == 0){
                return PageDto.of(0, pageable, null);
            }
            boardEntities = boardMapper.findAllByWriterIn(userEntityList, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
            totalSize = boardMapper.findTotalSizeForWriterSearch(userEntityList);
        } else if (type.equals(SearchType.ALL) || type.equals(SearchType.CONTENTS) || type.equals(SearchType.TITLE)) {
            boardEntities = boardMapper.findAll(type.name(), keyword, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
            totalSize = boardMapper.findTotalSizeForSearch(type.name(), keyword);
        }
        List<BoardDto> boardDtoList = boardEntities.stream().map(boardEntity -> BoardDto.of(boardEntity)).collect(Collectors.toList());
        return PageDto.of(totalSize, pageable, boardDtoList);
    }
}
