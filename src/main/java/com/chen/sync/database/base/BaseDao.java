package com.chen.sync.database.base;

import android.content.Context;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 13-7-2
 * Time: 下午3:06
 * To change this template use File | Settings | File Templates.
 */
public class BaseDao {

    static ConnectionSource connectionSource = null;

    public static Dao getDao(Context context, Class clazz) {
        if (connectionSource == null)
            connectionSource = new AndroidConnectionSource(new DatabaseHelper(context));
        try {
            return DaoManager.createDao(connectionSource, clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
