package top.omooo.zhihudaily.utils;

import android.os.Handler;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Omooo on 2018/1/19.
 */

public class OkHttpUtils {
    private static OkHttpUtils sOkHttpUtils = null;
    private Handler mHandler=new Handler();
    private OkHttpClient mClient;
    private Request mRequest;

    private OkHttpUtils() {

    }

    public static OkHttpUtils getInstance() {
        if (sOkHttpUtils == null) {
            synchronized (OkHttpUtils.class) {
                if (sOkHttpUtils == null) {
                    sOkHttpUtils = new OkHttpUtils();
                }
            }
        }
        return sOkHttpUtils;
    }

    public void startGet(String url, final OnNetResultListener listener) {
        mClient = new OkHttpClient();
        mRequest = new Request.Builder().url(url).build();
        mClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String errMsg = e.getMessage();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailureListener(errMsg);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSuccessListener(result);
                    }
                });
            }
        });
    }
}
