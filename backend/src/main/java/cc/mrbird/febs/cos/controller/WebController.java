package cc.mrbird.febs.cos.controller;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.GpsCoordinateUtils;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebController {

    private final IBulletinInfoService bulletinInfoService;


    /**
     * File 转MultipartFile
     *
     * @param file
     * @return
     */
    public MultipartFile getMultipartFile(File file) {
        FileInputStream fileInputStream = null;
        MultipartFile multipartFile = null;
        try {
            fileInputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartFile;
    }

//    @PostMapping("/userAdd")
//    public R userAdd(@RequestBody UserInfo user) throws Exception {
//        String url = "https://api.weixin.qq.com/sns/jscode2session";
//        url += "?appid=wx76a6577665633a86";//自己的appid
//        url += "&secret=78070ccedf3f17b272b84bdeeca28a2e";//自己的appSecret
//        url += "&js_code=" + user.getOpenId();
//        url += "&grant_type=authorization_code";
//        url += "&connect_redirect=1";
//        String res = null;
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//        // DefaultHttpClient();
//        HttpGet httpget = new HttpGet(url);
//        CloseableHttpResponse response = null;
//        // 配置信息
//        RequestConfig requestConfig = RequestConfig.custom()
//                .setConnectTimeout(5000)
//                .setConnectionRequestTimeout(5000)
//                .setSocketTimeout(5000)
//                .setRedirectsEnabled(false).build();
//        httpget.setConfig(requestConfig);
//        response = httpClient.execute(httpget);
//        HttpEntity responseEntity = response.getEntity();
//        System.out.println("响应状态为:" + response.getStatusLine());
//        if (responseEntity != null) {
//            res = EntityUtils.toString(responseEntity);
//            System.out.println("响应内容长度为:" + responseEntity.getContentLength());
//            System.out.println("响应内容为:" + res);
//        }
//        // 释放资源
//        httpClient.close();
//        response.close();
//        String openid = JSON.parseObject(res).get("openid").toString();
//
////        String openid = "oeDfR58jT62s8t7l5nQbPYdss5-I";
//        System.out.println("openid" + openid);
//        int count = userInfoService.count(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getOpenId, openid));
//        if (count > 0) {
//            return R.ok(userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getOpenId, openid)));
//        } else {
//            user.setOpenId(openid);
//            user.setCreateDate(DateUtil.formatDateTime(new Date()));
//            user.setName(user.getUserName());
//            user.setCode("UR-" + System.currentTimeMillis());
//            // 图片上传
//            byte[] bytes = HttpUtil.downloadBytes(user.getAvatar());
//            MultipartFile multipartFile = new MockMultipartFile("xxx.jpg", "xxx.jpg", ".jpg", bytes);
////            MultipartFile cMultiFile = new MockMultipartFile("file", file.getName(), null, new FileInputStream(file));
////
//            // 1定义要上传文件 的存放路径
//            String localPath = "G:/Project/20250227校园快递配送系统/db";
//            // 2获得文件名字
//            String fileName = multipartFile.getName();
//            // 2上传失败提示
//            String warning = "";
//            String newFileName = cc.mrbird.febs.common.utils.FileUtil.upload(multipartFile, localPath, fileName);
//            if (newFileName != null) {
//                //上传成功
//                warning = newFileName;
//            } else {
//                warning = "上传失败";
//            }
//            System.out.println(warning);
//            user.setImages(warning);
//            user.setType("1");
//            userInfoService.save(user);
//            return R.ok(user);
//        }
//    }

    @RequestMapping("/openid")
    public R getUserInfo(@RequestParam(name = "code") String code) throws Exception {
        System.out.println("code" + code);
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        url += "?appid=wx9fffb151ded22005";//自己的appid
        url += "&secret=9666e94c91361e7de4d3a6d09a23402f";//自己的appSecret
        url += "&js_code=" + code;
        url += "&grant_type=authorization_code";
        url += "&connect_redirect=1";
        String res = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);    //GET方式
        CloseableHttpResponse response = null;
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000)
                .setRedirectsEnabled(false).build();
        httpget.setConfig(requestConfig);
        response = httpClient.execute(httpget);
        HttpEntity responseEntity = response.getEntity();
        System.out.println("响应状态为:" + response.getStatusLine());
        if (responseEntity != null) {
            res = EntityUtils.toString(responseEntity);
            System.out.println("响应内容长度为:" + responseEntity.getContentLength());
            System.out.println("响应内容为:" + res);
        }
        // 释放资源
        httpClient.close();
        response.close();
        String openid = JSON.parseObject(res).get("openid").toString();
        System.out.println("openid" + openid);
        return R.ok(openid);
    }

    @GetMapping("/subscription")
    public R subscription(@RequestParam("taskCode") String taskCode) throws Exception {
        LinkedHashMap<String, Object> tokenParam = new LinkedHashMap<String, Object>() {
            {
                put("grant_type", "client_credential");
                put("appid", "wx76a6577665633a86");
                put("secret", "78070ccedf3f17b272b84bdeeca28a2e");
            }
        };
        String tokenResult = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token", tokenParam);
        String token = JSON.parseObject(tokenResult).get("access_token").toString();
        LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>() {
            {
                put("thing1", new HashMap<String, Object>() {
                    {
                        put("value", "张三");
                    }
                });
                put("character_string3", new HashMap<String, Object>() {
                    {
                        put("value", taskCode);
                    }
                });
                put("time4", new HashMap<String, Object>() {
                    {
                        put("value", DateUtil.formatDateTime(new Date()));
                    }
                });
                put("thing5", new HashMap<String, Object>() {
                    {
                        put("value", "若查看详细内容，请登录小程序");
                    }
                });
            }
        };
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + token;
        LinkedHashMap<String, Object> subscription = new LinkedHashMap<String, Object>() {
            {
                put("touser", "oeDfR5zqxQD3EEA3uPT836qnauSc");
                put("template_id", "Z27pBK1n9WnKNtQ_fo7TC-nUJUlOQ9KVJk6LIgp0nH8");
                put("data", data);
            }
        };
        String result = HttpUtil.post(url, JSONUtil.toJsonStr(subscription));
        return R.ok(result);
    }

}
