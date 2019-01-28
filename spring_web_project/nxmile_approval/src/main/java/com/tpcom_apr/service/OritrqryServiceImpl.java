package com.tpcom_apr.service;

import com.commons.exception.ValidException;
import com.tpcom_apr.domain.*;
import com.tpcom_apr.mapper.Apr_dealtr_trnMapper;
import com.tpcom_apr.service.service_interface.OritrqryService;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j
public class OritrqryServiceImpl implements OritrqryService {

    @Setter(onMethod_ = {@Autowired})
    private Apr_dealtr_trnMapper apr_dealtr_trnMapper;

    private Apr_dealtr_trn_tpcom_vs2001OutputVO apr_dealtr_trn_tpcom_vs2001OutputVO;
    private Apr_dealtr_trn_tpcom_vs2002OutputVO apr_dealtr_trn_tpcom_vs2002OutputVO;
    private Apr_dealtr_trn_tpcom_vs2003OutputVO apr_dealtr_trn_tpcom_vs2003OutputVO;
    private Apr_dealtr_trn_tpcom_vs2004OutputVO apr_dealtr_trn_tpcom_vs2004OutputVO;
    private Apr_dealtr_trn_tpcom_vs2035OutputVO apr_dealtr_trn_tpcom_vs2035OutputVO;

    private HttpHeaders responseHeader;
    private OritrqryOutputVO outputVO;


    @Override
    public ResponseEntity<OritrqryOutputVO> syncCall(HttpServletRequest request, OritrqryInputVO inputVO) {

        String orgn_deal_aprv_no;
        String orgn_deal_coopco_aprv_no;

        int sql_type = sqlTypeSetting(inputVO);


        Map<String, String> orgnAprvNo = changeOrgnAprvNo(inputVO.getOrgn_deal_aprv_no(), inputVO.getOrgn_deal_coopco_aprv_no());
        orgn_deal_aprv_no = orgnAprvNo.get("orgn_deal_aprv_no");
        orgn_deal_coopco_aprv_no = orgnAprvNo.get("orgn_deal_coopco_aprv_no");
        log.info("orgnAprvNo : " + orgn_deal_aprv_no + ", orgn_deal_coopco_aprv_no : " + orgn_deal_coopco_aprv_no);


        switch (sql_type) {
            case 11 :
                // 일반적립취소
                log.info("적립 원거래 조회(일반)");
                apr_dealtr_trn_tpcom_vs2001OutputVO =
                        apr_dealtr_trnMapper.apr_dealtr_trn_tpcom_vs2001(
                                new Apr_dealtr_trn_tpcom_vs2001InputVO(
                                        inputVO.getMbrsh_pgm_id(),
                                        inputVO.getOrgn_Deal_dy(),
                                        inputVO.getCrd_no(),
                                        inputVO.getMcht_no(),
                                        inputVO.getMix_sttl_yn(),
                                        inputVO.getSvc_modu_id(),
                                        inputVO.getOrgn_deal_amt(),
                                        orgn_deal_aprv_no,
                                        orgn_deal_coopco_aprv_no));
                if (!StringUtils.isEmpty(apr_dealtr_trn_tpcom_vs2001OutputVO)) {
                    responseHeader = new HttpHeaders();
                    responseHeader.add("ans_cd", "0000");
                    outputVO = new OritrqryOutputVO(apr_dealtr_trn_tpcom_vs2001OutputVO);
                } else if (StringUtils.isEmpty(apr_dealtr_trn_tpcom_vs2001OutputVO)) {

                    throw new ValidException("7777", "데이터 미존재");
                } else {
                    throw new ValidException("9080", "시스템실 연락바람");
                }
                break;
            case 21 :
                // 일반사용취소
                log.info("사용 원거래 조회(일반)");
                apr_dealtr_trn_tpcom_vs2002OutputVO =
                        apr_dealtr_trnMapper.apr_dealtr_trn_tpcom_vs2002(
                                new Apr_dealtr_trn_tpcom_vs2002InputVO(
                                        inputVO.getMbrsh_pgm_id(),
                                        inputVO.getOrgn_Deal_dy(),
                                        inputVO.getCrd_no(),
                                        inputVO.getMcht_no(),
                                        orgn_deal_coopco_aprv_no,
                                        orgn_deal_aprv_no,
                                        inputVO.getOrgn_deal_amt(),
                                        request.getHeader("organ_cd"), inputVO.getSvc_modu_id()));
                if (!StringUtils.isEmpty(apr_dealtr_trn_tpcom_vs2002OutputVO)) {
                    responseHeader = new HttpHeaders();
                    responseHeader.add("ans_cd", "0000");
                    outputVO = new OritrqryOutputVO(apr_dealtr_trn_tpcom_vs2002OutputVO);
                } else if (StringUtils.isEmpty(apr_dealtr_trn_tpcom_vs2002OutputVO)){
                    throw new ValidException("7777", "데이터 미존재");
                } else {
                    throw new ValidException("9080", "시스템실 연락바람");
                }
                break;
            case 10 :
                // 망상적립취소
                log.info("적립 원거래 조회(망상)");
                break;
            case 20 :
                // 망상사용취소
                log.info("사용 원거래 조회(망상)");
                break;
            case 22 :
                // 망상재사용
                log.info("사용 원거래 조회(망상재사용)");
                break;
            default :
                throw new ValidException("9080", "원거래조회 처리 유형 에러");
        }
        return new ResponseEntity<OritrqryOutputVO>(outputVO, responseHeader, HttpStatus.OK);
    }


    public int sqlTypeSetting(OritrqryInputVO inputVO) {
        int caller_type;
        int cancel_type;
        int sql_type;

        if ((!StringUtils.isEmpty(inputVO.getSvc_modu_id())) &&
                (inputVO.getSvc_modu_id().equals("ZPTOTXPTC0001") ||
                inputVO.getSvc_modu_id().equals("ZRCPTXPTC0001") ||
                inputVO.getSvc_modu_id().equals("ZSPOTXPTC0001") ||
                inputVO.getSvc_modu_id().equals("ZEBCTNPTOC001") ||
                inputVO.getSvc_modu_id().equals("ZCPNTXDSC0001"))) {
            caller_type = 1;    // 적립성 서비스 취소
            log.info("input svc_modu_id : [" + inputVO.getSvc_modu_id() + "]");
        } else if ((!StringUtils.isEmpty(inputVO.getSvc_modu_id())) &&
                (inputVO.getSvc_modu_id().equals("ZPTUTXPTC0001") ||
                inputVO.getSvc_modu_id().equals("ZGFTTXPTC0001") ||
                inputVO.getSvc_modu_id().equals("ZDSCTXALC0001") ||
                inputVO.getSvc_modu_id().equals("ZDSCTXPTC0001") ||
                inputVO.getSvc_modu_id().equals("ZEBCTNPTU0001") ||
                inputVO.getSvc_modu_id().equals("ZJONTNPTU0001"))) {
            caller_type = 2;    // 사용성 서비스 취소
            log.info("input svc_modu_id : [" + inputVO.getSvc_modu_id() + "]");
        } else {
            throw new ValidException("9080", "요청 서비스ID로는 처리할 수 없습니다.");
        }

        if ((!StringUtils.isEmpty(inputVO.getAns_cd())) &&
                (inputVO.getAns_cd().equals("60") ||
                inputVO.getAns_cd().equals("52"))) {
            cancel_type = 0;    // 망상취소
            log.info("input ans_cd : [" + inputVO.getAns_cd() + "]");
        } else if (!StringUtils.isEmpty(inputVO.getAns_cd()) &&
                inputVO.getAns_cd().equals("70")){
            cancel_type = 2;    // 망상재사용
            log.info("input ans_cd : [" + inputVO.getAns_cd() + "]");
        } else {
            cancel_type = 1;    // 일반취소
            log.info("input ans_cd : [" + inputVO.getAns_cd() + "]");
        }

        sql_type = caller_type * 10 + cancel_type;

        return sql_type;
    }


    public Map<String, String> changeOrgnAprvNo(String orgn_deal_aprv_no, String orgn_deal_coopco_aprv_no) {

        Map<String, String> orgnAprvNo = new HashMap<>();
        orgnAprvNo.put("orgn_deal_aprv_no", orgn_deal_aprv_no);
        orgnAprvNo.put("orgn_deal_coopco_aprv_no", orgn_deal_coopco_aprv_no);

        if (!StringUtils.isEmpty(orgn_deal_coopco_aprv_no)) {
            if (orgn_deal_coopco_aprv_no.substring(0, 2).equals("51") ||
                    orgn_deal_coopco_aprv_no.substring(0, 2).equals("52") ||
                    orgn_deal_coopco_aprv_no.substring(0, 2).equals("06") ||
                    orgn_deal_coopco_aprv_no.substring(0, 2).equals("20") ||
                    orgn_deal_coopco_aprv_no.substring(0, 2).equals("21") ||
                    orgn_deal_coopco_aprv_no.substring(0, 2).equals("22") ||
                    orgn_deal_coopco_aprv_no.substring(0, 2).equals("23") ||
                    orgn_deal_coopco_aprv_no.substring(0, 2).equals("G1") ||
                    orgn_deal_coopco_aprv_no.substring(0, 2).equals("G2") ||
                    orgn_deal_coopco_aprv_no.substring(0, 2).equals("F8") ||
                    orgn_deal_coopco_aprv_no.substring(0, 2).equals("69") ||
                    orgn_deal_coopco_aprv_no.substring(0, 2).equals("25")) {
                if (orgn_deal_coopco_aprv_no.length() == 9) {
                    orgnAprvNo.put("orgn_deal_aprv_no", orgn_deal_coopco_aprv_no);
                } else {
                    orgnAprvNo.put("orgn_deal_coopco_aprv_no", orgn_deal_coopco_aprv_no);
                }
            } else {
                orgnAprvNo.put("orgn_deal_coopco_aprv_no", orgn_deal_coopco_aprv_no);
            }
        } else if ((!StringUtils.isEmpty(orgn_deal_aprv_no) && orgn_deal_aprv_no.length() == 9) &&
                    (orgn_deal_aprv_no.substring(0, 2).equals("51") ||
                    orgn_deal_aprv_no.substring(0, 2).equals("52") ||
                    orgn_deal_aprv_no.substring(0, 2).equals("06") ||
                    orgn_deal_aprv_no.substring(0, 2).equals("20") ||
                    orgn_deal_aprv_no.substring(0, 2).equals("21") ||
                    orgn_deal_aprv_no.substring(0, 2).equals("22") ||
                    orgn_deal_aprv_no.substring(0, 2).equals("23") ||
                    orgn_deal_aprv_no.substring(0, 2).equals("G1") ||
                    orgn_deal_aprv_no.substring(0, 2).equals("G2") ||
                    orgn_deal_aprv_no.substring(0, 2).equals("F8") ||
                    orgn_deal_aprv_no.substring(0, 2).equals("69") ||
                    orgn_deal_aprv_no.substring(0, 2).equals("25"))){
            orgnAprvNo.put("orgn_deal_aprv_no", orgn_deal_aprv_no);
        } else {
            orgnAprvNo.put("orgn_deal_coopco_aprv_no", orgn_deal_aprv_no);
        }
        return orgnAprvNo;
    }

}
