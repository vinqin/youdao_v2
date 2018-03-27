package edu.stu.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class YouDaoTemplate {
    /**
     * 生成32位MD5摘要
     *
     * @param string appKey+q+salt+密钥
     * @return md5(appKey + q + salt + 密钥)
     */
    private String md5(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = string.getBytes("utf-8");
            /* 获得MD5摘要算法的 MessageDigest 对象 */
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            /* 使用指定的字节更新摘要 */
            mdInst.update(btInput);
            /* 获得密文 */
            byte[] md = mdInst.digest();
            /* 把密文转换成十六进制的字符串形式 */
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 准备发送到有道API的HTTP POST参数
     *
     * @param words 从shell脚本中传入的参数
     * @return HTTP POST表单所需要填入的参数
     */
    public Map<String, String> prepareArg(Map<String, String> words) {
        String appKey = "1a11210f12961f21";//appKey 应用ID
        String salt = String.valueOf(System.currentTimeMillis());//随机数
        String psw = "Nq9Yl6ZQtzxk6mRVov9fxNrUvPVE1fwB";//账户密钥
        String sign = md5(appKey + words.get("content") + salt + psw);//有道智云需要的凭证
        Map<String, String> params = new HashMap<>();
        params.put("q", words.get("content"));
        params.put("from", words.get("from"));
        params.put("to", words.get("to"));
        params.put("sign", sign);
        params.put("salt", salt);
        params.put("appKey", appKey);
        return params;
    }


    public String requestForHttp(String url, Map<String, String> requestParams) throws Exception {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        /*HttpPost*/
        HttpPost httpPost = new HttpPost(url);
        //System.out.println("请求的参数：" + new JSONObject(requestParams).toString());
        List<BasicNameValuePair> params = new ArrayList<>();
        Iterator<Map.Entry<String, String>> it = requestParams.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            String key = en.getKey();
            String value = en.getValue();
            if (value != null) {
                params.add(new BasicNameValuePair(key, value));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        /*HttpResponse*/
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "utf-8");
            EntityUtils.consume(httpEntity);//释放资源
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("返回的参数：" + result);
        return result;
    }
}
