package com.hm.iou.network.demo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hm.iou.network.HttpReqManager;
import com.hm.iou.network.demo.bean.CheckVersionResBean;
import com.hm.iou.network.demo.bean.UserInfo;
import com.hm.iou.network.interceptor.file.ProgressListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                System.out.println("brand = " + Build.BRAND);
                System.out.println("manfacturer = " + Build.MANUFACTURER);
                System.out.println("release = " + Build.VERSION.RELEASE);
                System.out.println("model = " + Build.MODEL);

                HttpReqManager.getInstance().getService(TestService.class)
                        .appInit("android", "19", "1.0.2")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseResult<APPInitDataBean>>() {
                            @Override
                            public void accept(ResponseResult<APPInitDataBean> stringResponseResult) throws Exception {

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        });
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = Md5.getMd5ByString("kaka123ac11");
                HttpReqManager.getInstance().getService(TestService.class)
                        .mobileLogin("15967132742", pwd)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseResult<UserInfo>>() {
                            @Override
                            public void accept(ResponseResult<UserInfo> userInfoResponseResult) throws Exception {

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        });
            }
        });

        findViewById(R.id.btn_checkVersion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpReqManager.getInstance()
                        .getService(TestService.class)
                        .checkVersion()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseResult<CheckVersionResBean>>() {
                            @Override
                            public void accept(ResponseResult<CheckVersionResBean> checkVersionResBeanResponseResult) throws Exception {
                                Log.d(TAG, "检查更新：" + checkVersionResBeanResponseResult.getData().toString());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        });
            }
        });
        findViewById(R.id.btn_downFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fileUrl = "http://218.75.75.34:9099/update/android/hm.jietiao_V1.0.10.apk";
                HttpReqManager
                        .getInstance()
                        .getService(TestService.class, new ProgressListener() {
                            @Override
                            public void onProgress(long addedBytes, long contentLength, boolean done) {

                            }
                        })
                        .downFile(fileUrl)
                        .subscribeOn(Schedulers.io())//subscribeOn和ObserOn必须在io线程，如果在主线程会出错
                        .observeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())//需要
                        .map(new Function<ResponseBody, File>() {
                            @Override
                            public File apply(ResponseBody responseBody) throws Exception {
                                return writeResponseBodyToDisk(responseBody);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<File>() {
                            @Override
                            public void accept(File file) throws Exception {
                                Toast.makeText(MainActivity.this, "" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                            }
                        });


//                Call<ResponseBody> responseCall = HttpReqManager.getInstance().getService(TestService.class)
//                        .downloadFileWithDynamicUrlSync(fileUrl);
//                responseCall.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful()) {
//                            Log.d(TAG, "server contacted and has file");
//
//                            boolean writtenToDisk = writeResponseBodyToDisk(response.body());
//
//                            Log.d(TAG, "file download was a success? " + writtenToDisk);
//                        } else {
//                            Log.d(TAG, "server contact failed");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Log.e(TAG, "error");
//                    }
//                });
            }
        });

    }

    private File writeResponseBodyToDisk(ResponseBody body) {
        Log.d(TAG, "file contentLength: " + body.contentLength());
        try {
            // todo change the file location/name according to your needs

            File futureStudioIconFile = new File(Environment.getExternalStorageDirectory() + File.separator + "借条管家.apk");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return futureStudioIconFile;
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

}