package com.chen.sync.http.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 13-8-12
 * Time: 下午2:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseAccess {

    final static String URL_PREFIX = "http://";
    final static String DEFAULT_SERVER_IP = "192.168.1.201";
    final static String PORT = "8080";

    public static String urlCombine(String url) {
        return URL_PREFIX + DEFAULT_SERVER_IP + ":" + PORT + "/" + url;
    }

    private static String getParams(Map<String, Object> map) {
        Set<String> strings = map.keySet();
        String res = "";
        for (String string : strings) {
            Object obj = map.get(string);
            if (res.equals("")) {
                res = "?";
            } else {
                res = res + "&";
            }
            res = res + string + "=" + obj;
        }
        return res;
    }

    public static String accessServerByGet(String url, Map<String, Object> map) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(urlCombine(url)).append(getParams(map));
        String response = null;
        HttpResponse httpResponse = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(new URI(stringBuilder.toString()));

            System.out.println("I go to http url:" + stringBuilder.toString());
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                //throw new Exception("Http response bad!" + httpResponse.getStatusLine().getStatusCode());
            } else {
                response = EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    public static String accessServerByPost(String url, Map<String, Object> map) {

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
        for (String s : map.keySet()) {
            formData.add(s, map.get(s));
        }

        try {
            final String f_url = urlCombine(url);
            System.out.println("=====accessServerByPost2 " + f_url);
            HttpHeaders requestHeaders = new HttpHeaders();
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, requestHeaders);
            RestTemplate restTemplate = new RestTemplate(true);
            SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
            rf.setReadTimeout(60 * 1000);
            ResponseEntity<String> response = restTemplate.exchange(f_url, HttpMethod.POST, requestEntity, String.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static Bitmap getPicFromServer(String url, Map<String, Object> map) {
        Bitmap bitmap = null;
        try {
            String totalUrl = urlCombine(url);

            Set<String> strings = map.keySet();
            for (String string : strings) {
                totalUrl = totalUrl + "?" + string + "=" + map.get(string);
            }
            System.out.println("==========I get pic " + totalUrl);
            URL picurl = new URL(totalUrl);
            HttpURLConnection conn = (HttpURLConnection) picurl.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5 * 1000);  //设置5秒超时
            conn.connect();
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            byte[] dataImage = bos.toByteArray();
            bos.close();
            in.close();
            bitmap = BitmapFactory.decodeByteArray(dataImage, 0, dataImage.length);
        } catch (Exception e) {

        }
        return bitmap;
    }


    public static String sendPicBySpring(String url, String path, Resource resource) {

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
        formData.add("picPath", path);
        formData.add("file", resource);

        try {
            final String f_url = urlCombine(url);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
                    formData, requestHeaders);

            RestTemplate restTemplate = new RestTemplate(true);
            SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
            rf.setReadTimeout(60 * 1000);

            ResponseEntity<String> response = restTemplate.exchange(f_url, HttpMethod.POST, requestEntity, String.class);

            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String smallpicpath(String path) {
        String[] smalls = path.split("/");
        return smalls[smalls.length - 1];
    }


    public static <T> T stringToObject(String josnStr, Class<T> type) {
        T obj = null;
        try {
            obj = getGson().fromJson(josnStr, type);
        } catch (JsonSyntaxException e) {
        }
        return obj;
    }

    public static Object stringToObject(String josnStr, Type type) {
        Object obj = null;
        try {
            obj = getGson().fromJson(josnStr, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static <T> T stringToObject(String josnStr, Type type, TypeAdapter typeAdapter) {
        T obj = null;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, typeAdapter).create();
            obj = gson.fromJson(josnStr, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return obj;

    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }
}
