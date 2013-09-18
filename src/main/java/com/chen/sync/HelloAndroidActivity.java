package com.chen.sync;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.chen.sync.domain.Sync;
import org.springframework.util.MultiValueMap;
import roboguice.inject.InjectView;

import java.sql.Timestamp;
import java.util.List;

public class HelloAndroidActivity extends BaseActivity {

    private static String TAG = "SyncDirectionary";

    @InjectView(R.id.sync)
    Button sync;

    @InjectView(R.id.number)
    TextView number;

    SharedPreferences sharedPreferences;

    Timestamp timestamp;

    private static final String SHARED_NAME = "sync";

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return super.getSharedPreferences(name, mode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sharedPreferences = getSharedPreferences(SHARED_NAME, 0);
        String updatetime = sharedPreferences.getString("latesttime", "");
        if (!updatetime.equals("")) {
            timestamp = new Timestamp(Long.parseLong(updatetime));
        }

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PostMessageTask().execute();
            }
        });
    }

    private class PostMessageTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog(HelloAndroidActivity.this, "loading");
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                List<Sync> list = getHttpAccess().getSyncData(timestamp);
                for (Sync s : list) {
                    getDaoImpl().save(HelloAndroidActivity.this, s);
                    if (timestamp.before(s.getUpdatetime())) {
                        timestamp = s.getUpdatetime();
                    }
                }

                sharedPreferences.edit().putString("latesttime", String.valueOf(timestamp)).commit();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
        }

    }

}

