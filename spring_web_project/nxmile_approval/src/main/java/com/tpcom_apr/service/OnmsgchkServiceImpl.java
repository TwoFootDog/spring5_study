package com.tpcom_apr.service;

import com.commons.domain.CustomizeHeaderVO;
import com.commons.exception.ValidException;
import com.tpcom_apr.domain.service.OnmsgchkInputVO;
import com.tpcom_apr.domain.service.OnmsgchkOutputVO;
import com.tpcom_apr.domain.service.wrapper.OnmsgchkInputWrapperVO;
import com.tpcom_apr.domain.service.wrapper.OnmsgchkOutputWrapperVO;
import com.tpcom_apr.domain.sql.Rul_svcavl_con_tpcom_vs2001InputVO;
import com.tpcom_apr.domain.sql.Rul_svcavl_con_tpcom_vs2001OutputVO;
import com.tpcom_apr.mapper.Rul_svcavl_conMapper;
import com.tpcom_apr.service.service_interface.OnmsgchkService;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
@Log4j
public class OnmsgchkServiceImpl implements OnmsgchkService {

    @Setter(onMethod_ = {@Autowired})
    private Rul_svcavl_conMapper rul_svcavl_conMapper;  // SQL 호출 서비스
    private Rul_svcavl_con_tpcom_vs2001OutputVO rul_svcavl_con_tpcom_vs2001OutputVO;    // SQL 호출 결과값

    private CustomizeHeaderVO header;   // 요청 header
    private OnmsgchkInputVO inputVO;    // 요청 body
    private OnmsgchkOutputVO outputVO;  // 응답 body
    private OnmsgchkOutputWrapperVO outputWrapperVO;    // 응답 header + body




    /* 온라인 전문 유효성 체크(BM_COM_ONMSGCHK) */
    public OnmsgchkOutputWrapperVO syncCall(OnmsgchkInputWrapperVO inputWrapperVO) {

        header = inputWrapperVO.getHeader();
        inputVO = inputWrapperVO.getBody();
        commonInputDataValidChk(inputVO);

        /* rul_svcavl_con_tpcom_vs2001(서비스모듈 유효성 확인조회 SQL 호출하여 결과값 셋팅 */
        rul_svcavl_con_tpcom_vs2001OutputVO =
                rul_svcavl_conMapper.rul_svcavl_con_tpcom_vs2001(
                        new Rul_svcavl_con_tpcom_vs2001InputVO(
                                inputVO.getOrgan_cd(),
                                inputVO.getTelgrm_no(),
                                inputVO.getSvc_modu_id(),
                                inputVO.getTelgrm_fg()));


        /* rul_svcavl_con_tpcom_vs2001 결과값 존재 시 OnmsgchkOutputVO(온라인 전문 유효성 체크) output 값 셋팅. 미 존재 시 7777 에러 발생 */
        if (!StringUtils.isEmpty(rul_svcavl_con_tpcom_vs2001OutputVO)) {
            outputWrapperVO = new OnmsgchkOutputWrapperVO();
            outputWrapperVO.setHeader(
                    new CustomizeHeaderVO(
                            inputWrapperVO.getHeader().getTelgrm_no().substring(0, 3).concat("1"),
                            inputWrapperVO.getHeader().getOrgan_cd(),
                            new SimpleDateFormat("yyyyMMdd").format(new Date()),
                            new SimpleDateFormat("HHmmss").format(new Date()),
                            inputWrapperVO.getHeader().getTrc_no(),
                            inputWrapperVO.getHeader().getTelgrm_fg(),
                            "0000",
                            ""));
            outputVO = new OnmsgchkOutputVO(
                    rul_svcavl_con_tpcom_vs2001OutputVO.getMbrsh_pgm_id(),
                    rul_svcavl_con_tpcom_vs2001OutputVO.getTelgrm_typ(),
                    "00",
                    rul_svcavl_con_tpcom_vs2001OutputVO.getMsg_fg()
            );
            outputWrapperVO.setBody(outputVO);
        } else if (StringUtils.isEmpty(rul_svcavl_con_tpcom_vs2001OutputVO)) {   // 쿼리 처리 결과 미 존재 시
            throw new ValidException("7777", "전문 유효성 데이터 미 존재");
        } else {
            throw new ValidException("9080", "시스템실 연락바람");
        }


        /* 취소전문이 아닌경우와 취소전문인 경우를 구분하여 필수값 체크 */
        if (!StringUtils.isEmpty(rul_svcavl_con_tpcom_vs2001OutputVO.getCncl_yn()) &&
                rul_svcavl_con_tpcom_vs2001OutputVO.getCncl_yn().equals("N")) {     // 취소 전문이 아닌 경우
            if (inputVO.getMcht_biz_no().length() != 10) {
                throw new ValidException("3311", "사업자번호 미입력 또는 길이 오류 [" + inputVO.getMcht_biz_no() + "]");
            }
        } else {    // 취소 전문인 경우
            if (StringUtils.isEmpty(inputVO.getOrgn_deal_dy())) {
                throw new ValidException("7745", "원거래일자 미입력");
            }
            if ((StringUtils.isEmpty(inputVO.getOrgn_deal_aprv_no()) && StringUtils.isEmpty(inputVO.getOrgn_deal_coopco_aprv_no())) &&
                    ((StringUtils.isEmpty(inputVO.getAns_cd1()) || (!StringUtils.isEmpty(inputVO.getAns_cd1())) &&
                            (!inputVO.getAns_cd1().equals("60") &&
                                    !inputVO.getAns_cd1().equals("52"))))) {
                throw new ValidException("7746", "원거래승인번호/원거래제휴사승인번호 미입력");
            }
            if (StringUtils.isEmpty(inputVO.getCncl_req_fg())) {
                throw new ValidException("3313", "요청 서비스구분 미입력");
            }
        }
        return outputWrapperVO;
    }


    /* 공통 입력값 유효성 체크*/
    public void commonInputDataValidChk(OnmsgchkInputVO inputVO) {
        if (StringUtils.isEmpty(inputVO.getTrc_no())) {
            throw new ValidException("7777", "추적번호 미 유입");
        }
        if (StringUtils.isEmpty(inputVO.getMcht_no())) {
            throw new ValidException("7730", "가맹점번호 미 유입");
        }
        if (StringUtils.isEmpty(inputVO.getDeal_dy())) {
            throw new ValidException("6940", "거래일자 미 유입");
        }
        if (StringUtils.isEmpty(inputVO.getCrd_no()) &&
                StringUtils.isEmpty(inputVO.getResd_no())) {
            throw new ValidException("7720", "식별자 미 유입");
        }
    }
}
