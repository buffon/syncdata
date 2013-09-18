package com.chen.sync.http;

import android.graphics.Bitmap;
import com.chen.sync.domain.Sync;
import com.chen.sync.http.base.BaseAccess;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.justonetech.ipromis.domain.android.*;
import org.springframework.core.io.Resource;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 13-8-12
 * Time: 下午2:48
 * To change this template use File | Settings | File Templates.
 */
public class HttpAccessImpl extends BaseAccess {
    private static final String URL_SYNC = "syncs/syncd";

    public List<Sync> getSyncData(Timestamp timestamp) {
        Map map = new HashMap<String, String>();
        map.put("timestamp", timestamp);
        return (List<Sync>) stringToObject(accessServerByGet(URL_SYNC, map), new TypeToken<List<Sync>>() {
        }.getType());
    }



}

