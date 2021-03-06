package com.tpcom_apr.domain.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnmsgchkInputVO {
    private String svc_modu_id;
    private String telgrm_no;
    private String organ_cd;
    private String trc_no;
    private String mcht_no;
    private String mcht_biz_no;
    private String deal_dy;
    private String crd_no;
    private String resd_no;
    private String orgn_deal_dy;
    private String orgn_deal_aprv_no;
    private String orgn_deal_coopco_aprv_no;
    private Long deal_amt_sum;
    private Long deal_amt1;
    private Long orgn_deal_amt;
    private String cncl_req_fg;
    private String aprv_no;
    private String ans_cd1;
    private String telgrm_fg;
    private String valid_chk_yn;
}
