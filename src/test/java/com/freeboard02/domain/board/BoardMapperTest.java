package com.freeboard02.domain.board;

import com.freeboard02.api.board.BoardForm;
import com.freeboard02.domain.board.enums.SearchType;
import com.freeboard02.domain.user.UserEntity;
import com.freeboard02.domain.user.UserMapper;
import com.freeboard02.domain.user.enums.UserRole;
import com.github.npathai.hamcrestopt.OptionalMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@Transactional
@Rollback(value = false)
public class BoardMapperTest {

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private UserMapper userMapper;

    private UserEntity user;

    private BoardEntity targetBoard;

    final int PAGE = 0;
    final int SIZE = 10;

    @BeforeEach
    public void init() {
        user = userMapper.findAll().get(3);
        targetBoard = BoardEntity.builder()
                .title(randomString())
                .contents(randomString())
                .writer(user).build();
    }

    @Test
    public void mapperInsert() {
        assertThat(targetBoard.getId(), equalTo(0L));
        boardMapper.save(targetBoard);
        assertThat(targetBoard.getId(), not(equalTo(0L)));
    }

    @Test
    public void mapperFindById() {
        boardMapper.save(targetBoard);

        BoardEntity entity = boardMapper.findById(targetBoard.getId()).get();

        assertThat(targetBoard.getTitle(), equalTo(entity.getTitle()));
        assertThat(targetBoard.getContents(), equalTo(entity.getContents()));
        assertThat(entity.getWriter().getId(), equalTo(user.getId()));
        assertThat(entity.getWriter().getRole(), equalTo(user.getRole()));
    }

    @Test
    public void mapperDelete() {
        boardMapper.save(targetBoard);

        long targetId = targetBoard.getId();
        assertThat(targetId, not(equalTo(0L)));

        Optional<BoardEntity> saved = boardMapper.findById(targetId);
        assertThat(saved, OptionalMatchers.isPresent());

        boardMapper.deleteById(targetId);
        Optional<BoardEntity> deleted = boardMapper.findById(targetId);
        assertThat(deleted, OptionalMatchers.isEmpty());
    }

    @Test
    public void mapperUpdate() {
        boardMapper.save(targetBoard);

        BoardForm form = BoardForm.builder().contents(randomString()).title(randomString()).build();
        BoardEntity entity = form.convertBoardEntity(targetBoard.getWriter());
        entity.setId(targetBoard.getId());
        boardMapper.updateById(entity);

        BoardEntity updatedEntity = boardMapper.findById(targetBoard.getId()).get();
        assertThat(updatedEntity.getContents(), equalTo(form.getContents()));
        assertThat(updatedEntity.getTitle(), equalTo(form.getTitle()));
    }

    @Test
    @DisplayName("특정 유저들이 작성한 글만 가져올 수 있다.")
    public void mapperFindAllByWriterIn1() {
        final int INSERT_SIZE = (int) (Math.random() * 9 + 1);
        List<UserEntity> writers = getUserEntities();
        insertTestDataBySomeWriters(writers, INSERT_SIZE);

        List<BoardEntity> findEntities = boardMapper.findAllByWriterIn(writers, PAGE, SIZE);

        List<Long> writerIds = writers.stream().map(writer -> writer.getId()).collect(Collectors.toList());
        List<Long> findWriterIds = findEntities.stream().map(boardEntity -> boardEntity.getWriter().getId()).distinct().collect(Collectors.toList());

        assertThat(findEntities.size(), equalTo(INSERT_SIZE));
        assertThat(writerIds, hasItems(findWriterIds.toArray(new Long[findWriterIds.size()])));
    }

    @Test
    @DisplayName("특정 유저들이 작성한 글만 가져와 페이징 처리할 수 있다.")
    public void mapperFindAllByWriterIn2() {
        final int INSERT_SIZE = 10 + (int) (Math.random() * (30 - 10 + 1));
        List<UserEntity> writers = getUserEntities();
        insertTestDataBySomeWriters(writers, INSERT_SIZE);

        List<BoardEntity> findEntities = boardMapper.findAllByWriterIn(writers, PAGE, SIZE);

        assertThat(findEntities.size(), equalTo(SIZE));
    }

    private List<UserEntity> getUserEntities() {
        List<UserEntity> userEntities = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            String accountId = randomString();
            insertNewTestUser(accountId);
            userEntities.add(userMapper.findByAccountId(accountId));
        }
        return userEntities;
    }

    private void insertTestDataBySomeWriters(List<UserEntity> writers, int INSERT_SIZE) {
        for (int i = 0; i < INSERT_SIZE; ++i) {
            // 글 작성자를 번갈아가며 사용하여 새로운 글을 저장한다.
            BoardEntity boardEntity = BoardEntity.builder().writer(writers.get(i % writers.size())).contents("contents").title("title").build();
            boardMapper.save(boardEntity);
        }
    }

    @Test
    @DisplayName("특정 유저가 작성한 글만 가져올 수 있다.")
    public void mapperFindAllByWriterId() {
        final String NEW_USER_ACCOUNT_ID = randomString();
        final int INSERT_SIZE = 10 + (int) (Math.random() * (30 - 10 + 1));

        insertNewTestUser(NEW_USER_ACCOUNT_ID);
        insertTestDataByNewWriter(NEW_USER_ACCOUNT_ID, INSERT_SIZE);

        List<BoardEntity> findEntities = boardMapper.findAllByWriterId(userMapper.findByAccountId(NEW_USER_ACCOUNT_ID).getId());
        List<String> findEntityWriterAccountId = findEntities.stream().map(boardEntity -> boardEntity.getWriter().getAccountId()).distinct().collect(Collectors.toList());

        assertThat(findEntities.size(), equalTo(INSERT_SIZE));
        assertThat(findEntityWriterAccountId.size(), equalTo(1));
        assertThat(NEW_USER_ACCOUNT_ID, equalTo(findEntityWriterAccountId.get(0)));
    }

    private void insertNewTestUser(final String NEW_USER_ACCOUNT_ID) {
        final UserEntity NEW_USER = UserEntity.builder()
                .accountId(NEW_USER_ACCOUNT_ID)
                .password(randomString())
                .role(UserRole.NORMAL)
                .build();
        // 새로운 유저를 추가한다.
        userMapper.save(NEW_USER);
    }

    private void insertTestDataByNewWriter(final String NEW_USER_ACCOUNT_ID, final int INSERT_SIZE) {
        final UserEntity NEW_USER = userMapper.findByAccountId(NEW_USER_ACCOUNT_ID);

        for (int i = 0; i < INSERT_SIZE; ++i) {
            // 새로 추가한 유저의 이름으로 새로운 글을 작성한다.
            BoardEntity boardEntity = BoardEntity.builder().writer(NEW_USER).contents("contents").title("title").build();
            boardMapper.save(boardEntity);
        }
    }

    @Test
    @DisplayName("ALL 타입으로 검색할 경우, 해당 키워드를 제목 혹은 내용에 포함하고 있는 레코드만 검색해 올 수 있다.")
    public void mapperSearch() {
        final int INSERT_SIZE = (int) (Math.random() * 9 + 1);
        final String SEARCH_KEYWORD = LocalDateTime.now().toString();
        insertTestDataForSearch(INSERT_SIZE, SEARCH_KEYWORD, SearchType.ALL);

        List<BoardEntity> findEntities = boardMapper.findAll(SearchType.ALL.name(), SEARCH_KEYWORD, PAGE, SIZE);

        assertThat(findEntities.size(), equalTo(INSERT_SIZE));
    }

    @Test
    @DisplayName("TITLE 타입으로 검색할 경우, 해당 키워드를 제목에 포함하고 있는 레코드만 검색해 올 수 있다.")
    public void mapperSearch2() {
        final int INSERT_SIZE = (int) (Math.random() * 9 + 1);
        final String SEARCH_KEYWORD = LocalDateTime.now().toString();
        insertTestDataForSearch(INSERT_SIZE, SEARCH_KEYWORD, SearchType.TITLE);

        List<BoardEntity> findEntities = boardMapper.findAll(SearchType.TITLE.name(), SEARCH_KEYWORD, PAGE, SIZE);

        assertThat(findEntities.size(), equalTo(INSERT_SIZE));
    }

    @Test
    @DisplayName("CONTENTS 타입으로 검색할 경우, 해당 키워드를 내용에 포함하고 있는 레코드만 검색해 올 수 있다.")
    public void mapperSearch3() {
        final int INSERT_SIZE = (int) (Math.random() * 9 + 1);
        final String SEARCH_KEYWORD = LocalDateTime.now().toString();
        insertTestDataForSearch(INSERT_SIZE, SEARCH_KEYWORD, SearchType.CONTENTS);

        List<BoardEntity> findEntities = boardMapper.findAll(SearchType.CONTENTS.name(), SEARCH_KEYWORD, PAGE, SIZE);

        assertThat(findEntities.size(), equalTo(INSERT_SIZE));
    }

    private void insertTestDataForSearch(final int INSERT_SIZE, final String SEARCH_KEYWORD, final SearchType searchType) {
        int loopSize = searchType.equals(SearchType.ALL) ? INSERT_SIZE : 10 + (int) (Math.random() * (30 - 10 + 1));
        int insertSizeForKeyword = 0;

        if (searchType.equals(SearchType.ALL)) {
            insertSizeForKeyword = INSERT_SIZE / 2;
        } else if (searchType.equals(SearchType.TITLE)) {
            insertSizeForKeyword = loopSize - INSERT_SIZE;
        } else if (searchType.equals(SearchType.CONTENTS)) {
            insertSizeForKeyword = INSERT_SIZE;
        }

        for (int i = 0; i < loopSize; ++i) {
            BoardEntity boardEntity = null;
            if (i < insertSizeForKeyword) {
                boardEntity = BoardEntity.builder().writer(user).contents(SEARCH_KEYWORD).title("title").build();
            } else {
                boardEntity = BoardEntity.builder().writer(user).contents("contents").title(SEARCH_KEYWORD).build();
            }
            boardMapper.save(boardEntity);
        }
    }

    @Test
    @DisplayName("검색할 경우 페이징 처리가 잘 되는지 확인한다.")
    public void mapperSearch5() {
        final String SEARCH_KEYWORD = LocalDateTime.now().toString();
        insertTestDataForPaging(SEARCH_KEYWORD);

        List<BoardEntity> findEntities = boardMapper.findAll(SearchType.CONTENTS.name(), SEARCH_KEYWORD, PAGE, SIZE);

        assertThat(findEntities.size(), equalTo(SIZE));
    }

    private void insertTestDataForPaging(final String SEARCH_KEYWORD) {
        final int INSERT_SIZE = 10 + (int) (Math.random() * (30 - 10 + 1));

        for (int i = 0; i < INSERT_SIZE; ++i) {
            boardMapper.save(BoardEntity.builder().writer(user).contents(SEARCH_KEYWORD).title("title").build());
        }
    }

    @Test
    @DisplayName("ALL 타입으로 검색할 경우, count 함수를 이용하여 제목이나 내용에 해당 키워드를 가진 레코드의 총 개수를 가져올 수 있다.")
    public void mapperSearch4() {
        final int INSERT_SIZE = (int) (Math.random() * 9 + 1);
        ;
        final String SEARCH_KEYWORD = LocalDateTime.now().toString();
        insertSearchCountingTestData(INSERT_SIZE, SEARCH_KEYWORD);

        int totalCount = boardMapper.findTotalSizeForSearch(SearchType.ALL.name(), SEARCH_KEYWORD);

        assertThat(totalCount, equalTo(INSERT_SIZE));
    }

    private void insertSearchCountingTestData(final int INSERT_SIZE, final String SEARCH_KEYWORD) {
        List<Long> savedEntityIds = new ArrayList<>();
        for (int i = 0; i < INSERT_SIZE; ++i) {
            BoardEntity boardEntity = null;
            if (i % 2 == 0) {
                boardEntity = BoardEntity.builder().writer(user).contents(SEARCH_KEYWORD).title("title").build();
            } else {
                boardEntity = BoardEntity.builder().writer(user).contents("contents").title(SEARCH_KEYWORD).build();
            }
            boardMapper.save(boardEntity);
            savedEntityIds.add(boardEntity.getId());
        }
    }

    private String randomString() {
        String id = "";
        for (int i = 0; i < 20; i++) {
            double dValue = Math.random();
            if (i % 2 == 0) {
                id += (char) ((dValue * 26) + 65);   // 대문자
                continue;
            }
            id += (char) ((dValue * 26) + 97); // 소문자
        }
        return id;
    }
}
