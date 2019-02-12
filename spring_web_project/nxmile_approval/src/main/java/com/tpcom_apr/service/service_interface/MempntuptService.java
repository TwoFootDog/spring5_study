package com.tpcom_apr.service.service_interface;

import com.tpcom_apr.domain.service.MempntuptInputVO;
import com.tpcom_apr.domain.service.MempntuptOutputVO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface MempntuptService {
    public ResponseEntity<MempntuptOutputVO> syncCall(HttpHeaders requestHeaders, MempntuptInputVO inputVO);
}