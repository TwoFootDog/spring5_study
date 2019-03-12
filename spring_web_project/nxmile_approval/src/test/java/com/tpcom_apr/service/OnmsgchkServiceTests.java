package com.tpcom_apr.service;

import com.commons.domain.CustomizeHeaderVO;
import com.commons.exception.ValidException;
import com.tpcom_apr.domain.service.OnmsgchkInputVO;
import com.tpcom_apr.domain.service.wrapper.OnmsgchkInputWrapperVO;
import com.tpcom_apr.domain.service.wrapper.OnmsgchkOutputWrapperVO;
import com.tpcom_apr.domain.sql.Rul_svcavl_con_tpcom_vs2001OutputVO;
import com.tpcom_apr.mapper.Rul_svcavl_conMapper;
import com.tpcom_apr.service.serviceInterface.OnmsgchkService;
import lombok.extern.log4j.Log4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring-config/applicationContext.xml", "file:src/main/webapp/WEB-INF/spring-config/dispatcher-servlet.xml"})
@WebAppConfiguration
@Log4j
public class OnmsgchkServiceTests {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test   // 서비스 synCall 정상 호출여부 검증
    public void testOnmsgchkServiceOkSynccallOK() {
        OnmsgchkService service = mock(OnmsgchkService.class);
        HttpHeaders headers = mock(HttpHeaders.class);
        OnmsgchkOutputWrapperVO output = service.syncCall(new OnmsgchkInputWrapperVO());
        verify(service).syncCall(any());
    }

    @Test   // 서비스 내 commonInputValidChk 및 rul_svcavl_con_tpcom_vs2001 정상 호출여부 검증
    public void testOnmsgchkServiceInnerMethodCallOK() {
        /* Mock, Spy Object 선언*/
        HttpHeaders headers = mock(HttpHeaders.class);
        OnmsgchkService service = spy(OnmsgchkServiceImpl.class);
        OnmsgchkInputWrapperVO inputWrapperVO = mock(OnmsgchkInputWrapperVO.class);
        CustomizeHeaderVO header = mock(CustomizeHeaderVO.class);
        OnmsgchkInputVO input = mock(OnmsgchkInputVO.class);
        Rul_svcavl_conMapper mapper = mock(Rul_svcavl_conMapper.class);
        ((OnmsgchkServiceImpl) service).setRul_svcavl_conMapper(mapper);

        /* Stub 선언*/
        when(inputWrapperVO.getHeader()).thenReturn(header);
        when(inputWrapperVO.getBody()).thenReturn(input);
        when(header.getTelgrm_no()).thenReturn("K410");
        when(header.getOrgan_cd()).thenReturn("5004");
        when(header.getTrc_no()).thenReturn("1111111111");
        when(header.getTelgrm_fg()).thenReturn("ON");
        when(input.getTrc_no()).thenReturn("1111111111");
        when(input.getMcht_no()).thenReturn("11111111");
        when(input.getDeal_dy()).thenReturn("20190128");
        when(input.getCrd_no()).thenReturn("1111111111111111");
        when(input.getMcht_biz_no()).thenReturn("1111111111");
        when(mapper.rul_svcavl_con_tpcom_vs2001(any())).
                thenReturn(new Rul_svcavl_con_tpcom_vs2001OutputVO(
                        "A","ON","N","1","N"));

        /* 서비스 호출 및 검증*/
        OnmsgchkOutputWrapperVO output = service.syncCall(inputWrapperVO);
        verify(service).syncCall(any());
        verify(service).commonInputDataValidChk(any());
        verify(mapper).rul_svcavl_con_tpcom_vs2001(any());
    }


    @Test   // 추적번호 미 유입시 7777 에러
    public void testOnmsgchkServiceErrorTrcNoNotInput() {
        /* Mock, Spy Object 선언*/
        HttpHeaders headers = mock(HttpHeaders.class);
        OnmsgchkService service = spy(OnmsgchkServiceImpl.class);
        OnmsgchkInputWrapperVO inputWrapperVO = mock(OnmsgchkInputWrapperVO.class);
        OnmsgchkInputVO input = mock(OnmsgchkInputVO.class);
        Rul_svcavl_conMapper mapper = mock(Rul_svcavl_conMapper.class);
        ((OnmsgchkServiceImpl) service).setRul_svcavl_conMapper(mapper);

        /* Stub 선언*/
        when(inputWrapperVO.getBody()).thenReturn(input);
//        when(inputVO.getTrc_no()).thenReturn("1111111111");
        when(input.getMcht_no()).thenReturn("11111111");
        when(input.getDeal_dy()).thenReturn("20190128");
        when(input.getCrd_no()).thenReturn("1111111111111111");
        when(input.getMcht_biz_no()).thenReturn("1111111111");
        when(mapper.rul_svcavl_con_tpcom_vs2001(any())).
                thenReturn(new Rul_svcavl_con_tpcom_vs2001OutputVO(
                        "A","ON","N","1","N"));

        /* 서비스 호출 및 검증*/
        try{
            OnmsgchkOutputWrapperVO output = service.syncCall(inputWrapperVO);
        } catch (ValidException e) {
            if(e.getAns_cd().equals("7777")) {
                log.info("에러코드 :  " + e.getAns_cd() + ". 에러 검증 완료");
            } else {
                throw new ValidException(e.getAns_cd(), e.getAns_msg());
            }
        }
    }

    @Test   // 가맹점번호 미 유입시 7730 에러
    public void testOnmsgchkServiceErrorMchtNoNotInput() {
        /* Mock, Spy Object 선언*/
        HttpHeaders headers = mock(HttpHeaders.class);
        OnmsgchkService service = spy(OnmsgchkServiceImpl.class);
        OnmsgchkInputWrapperVO inputWrapperVO = mock(OnmsgchkInputWrapperVO.class);
        OnmsgchkInputVO input = mock(OnmsgchkInputVO.class);
        Rul_svcavl_conMapper mapper = mock(Rul_svcavl_conMapper.class);
        ((OnmsgchkServiceImpl) service).setRul_svcavl_conMapper(mapper);

        /* Stub 선언*/
        when(inputWrapperVO.getBody()).thenReturn(input);
        when(input.getTrc_no()).thenReturn("1111111111");
//        when(inputVO.getMcht_no()).thenReturn("11111111");
        when(input.getDeal_dy()).thenReturn("20190128");
        when(input.getCrd_no()).thenReturn("1111111111111111");
        when(input.getMcht_biz_no()).thenReturn("1111111111");
        when(mapper.rul_svcavl_con_tpcom_vs2001(any())).
                thenReturn(new Rul_svcavl_con_tpcom_vs2001OutputVO(
                        "A","ON","N","1","N"));

        /* 서비스 호출 및 검증*/
        try{
            OnmsgchkOutputWrapperVO output = service.syncCall(inputWrapperVO);
        } catch (ValidException e) {
            if (e.getAns_cd().equals("7730")) {
                log.info("에러코드 :  " + e.getAns_cd() + ". 에러 검증 완료");
            } else {
                throw new ValidException(e.getAns_cd(), e.getAns_msg());
            }
        }
    }

    @Test   // 월거래일자 미 유입시 7745 에러
    public void testOnmsgchkServiceErrorOrgnDealDyNotInput() {
        /* Mock, Spy Object 선언*/
        HttpHeaders headers = mock(HttpHeaders.class);
        OnmsgchkService service = spy(OnmsgchkServiceImpl.class);
        OnmsgchkInputWrapperVO inputWrapperVO = mock(OnmsgchkInputWrapperVO.class);
        CustomizeHeaderVO header = mock(CustomizeHeaderVO.class);
        OnmsgchkInputVO input = mock(OnmsgchkInputVO.class);
        Rul_svcavl_conMapper mapper = mock(Rul_svcavl_conMapper.class);
        ((OnmsgchkServiceImpl) service).setRul_svcavl_conMapper(mapper);

        /* Stub 선언*/
        when(inputWrapperVO.getHeader()).thenReturn(header);
        when(inputWrapperVO.getBody()).thenReturn(input);
        when(header.getTelgrm_no()).thenReturn("K410");
        when(header.getOrgan_cd()).thenReturn("5004");
        when(header.getTrc_no()).thenReturn("1111111111");
        when(header.getTelgrm_fg()).thenReturn("ON");
        when(input.getTrc_no()).thenReturn("1111111111");
        when(input.getMcht_no()).thenReturn("11111111");
        when(input.getDeal_dy()).thenReturn("20190128");
        when(input.getCrd_no()).thenReturn("1111111111111111");
        when(input.getMcht_biz_no()).thenReturn("1111111111");
        when(mapper.rul_svcavl_con_tpcom_vs2001(any())).
                thenReturn(new Rul_svcavl_con_tpcom_vs2001OutputVO(
                        "A","ON","Y","1","N"));

        /* 서비스 호출 및 검증*/
        try{
            OnmsgchkOutputWrapperVO output = service.syncCall(inputWrapperVO);
        } catch (ValidException e) {
            if (e.getAns_cd().equals("7745")) {
                log.info("에러코드 :  " + e.getAns_cd() + ". 에러 검증 완료");
            } else {
                throw new ValidException(e.getAns_cd(), e.getAns_msg());
            }
        }
    }
}
