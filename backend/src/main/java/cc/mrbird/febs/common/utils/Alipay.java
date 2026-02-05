package cc.mrbird.febs.common.utils;

import cc.mrbird.febs.common.config.AlipayConfig;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Alipay {

    @Autowired
    private AlipayConfig alipayConfig;
}