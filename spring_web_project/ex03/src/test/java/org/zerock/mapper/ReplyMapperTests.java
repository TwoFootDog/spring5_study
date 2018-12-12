package org.zerock.mapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.ReplyVO;

import java.util.stream.IntStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ReplyMapperTests {

    @Setter(onMethod_ = @Autowired)
    private ReplyMapper replyMapper;

    private Long[] bnoArr = {3L, 4L, 6L, 7L, 8L};

    @Test
    public void testCreate() {
        IntStream.rangeClosed(1,10).forEach(i->{
            ReplyVO replyVO = new ReplyVO();
            replyVO.setBno(bnoArr[i%5]);
            replyVO.setReply("����׽�Ʈ" + i);
            replyVO.setReplyer("�׽���" + i);
            replyMapper.insert(replyVO);
        });
    }

/*    @Test
    public void testMapper() {
        log.info(replyMapper);
    }*/

}
