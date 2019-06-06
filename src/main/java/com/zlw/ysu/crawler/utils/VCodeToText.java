package com.zlw.ysu.crawler.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 处理验证码工具类2
 */
public class VCodeToText {

    /**
     * 使用httpClient去下载图片并保存到savePath
     *
     * @param savePath
     * @param httpClient
     */
    public static void save(String savePath, CloseableHttpClient httpClient){
        try {
            HttpGet httpGet = new HttpGet();
            httpGet.setURI(new URI("http://202.206.243.62/CheckCode.aspx"));
            httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");

            CloseableHttpResponse response = httpClient.execute(httpGet);

            String codeImagefilePath = savePath;
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                File f = new File(codeImagefilePath);
                if (!f.exists()) f.createNewFile();
                OutputStream os = new FileOutputStream(f);
                int length = -1;
                byte[] bytes = new byte[1024];
                while ((length = is.read(bytes)) != -1) {
                    os.write(bytes, 0, length);
                }
                os.close();
                //关闭response
                response.close();
                EntityUtils.consume(entity);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求验证码保存到本地，并进行识别
     * @param savePath
     * @param httpClient
     * @return 验证码识别结果
     */
    public static String getCodeText(String savePath, CloseableHttpClient httpClient){
        String vcode = "";
        try {
            //先使用httpClient将验证码下载到savePath
            save(savePath,httpClient);
            //下载的验证码与字库进行对比，得出字符
            vcode = VCodeOCR.ocr(savePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vcode;
    }

}
