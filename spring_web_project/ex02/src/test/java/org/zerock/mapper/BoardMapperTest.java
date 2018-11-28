package org.zerock.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.BoardVO;
import org.zerock.mapper.BoardMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardMapperTest {

	@Setter(onMethod_ = @Autowired)
	private BoardMapper boardMapper;

/*	@Test
	public void testGetList() {
		boardMapper.getList().forEach(board -> log.info(board));
	}

	@Test
	public void testInsert() {
		BoardVO board = new BoardVO();
		board.setTitle("���� �ۼ��ϴ� ��");
		board.setContent("���� �ۼ��� ����");
		board.setWriter("newbie");
		boardMapper.insert(board);
		log.info(board);
	}

	@Test
	public void testInsertSelectKey() {
		BoardVO board = new BoardVO();
		board.setTitle("���� �ۼ��� �� select key");
		board.setContent("���� �ۼ��ϴ� ���� select key");
		board.setWriter("newbie");
		boardMapper.insert(board);
		log.info(board);
	}

	@Test
	public void testRead() {
		BoardVO board = boardMapper.read(5L);
		log.info(board);
	}

	@Test
	public void testDelete() {
		log.info("Delete count : " + boardMapper.delete(5L));
	}*/
	
	@Test
	public void testUpdate() { 
		BoardVO board = new BoardVO();
		board.setBno(6L);
		board.setTitle("��������1");
		board.setContent("���� ����");
		board.setWriter("������1");
		
		int count = boardMapper.update(board);
		log.info("update count : " + count);
	}
}
