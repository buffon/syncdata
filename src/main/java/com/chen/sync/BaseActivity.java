package com.chen.sync;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import com.chen.sync.database.DaoImpl;
import com.chen.sync.http.HttpAccessImpl;
import roboguice.activity.RoboActivity;

import java.io.File;


/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 13-5-28
 * Time: 上午11:30
 * To change this template use File | Settings | File Templates.
 */
public class BaseActivity extends RoboActivity {

    public static String rootPath;
    public Uri photoUri;
    public static SQLiteDatabase database;

    private static HttpAccessImpl httpAccess;
    private static DaoImpl daoImpl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (rootPath == null)
            rootPath = getApplicationContext().getFilesDir().getAbsolutePath();
    }

    public static HttpAccessImpl getHttpAccess() {
        if (httpAccess == null)
            httpAccess = new HttpAccessImpl();
        return httpAccess;
    }

    public static DaoImpl getDaoImpl() {
        if (daoImpl == null)
            daoImpl = new DaoImpl();
        return daoImpl;
    }

    /*
         * for processDialog
         */
    private ProgressDialog progressDialog;

    public void showLoadingProgressDialog(Context context, String dialogText) {
        this.showProgressDialog(context, dialogText);
    }

    private void showProgressDialog(Context context, CharSequence message) {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setIndeterminate(true);
        }

        this.progressDialog.setMessage(message);
        this.progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (this.progressDialog != null && this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void select_back(View v) {
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 判断网络状态
     */
    public boolean isNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getVersion() {
        String version = null;
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

}
