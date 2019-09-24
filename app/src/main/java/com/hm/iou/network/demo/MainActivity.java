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
import com.hm.iou.network.demo.bean.BaseResponse;
import com.hm.iou.network.demo.bean.CheckVersionResBean;
import com.hm.iou.network.demo.bean.MobileLoginReqBean;
import com.hm.iou.network.demo.bean.TestEncryptReqBean;
import com.hm.iou.network.demo.bean.UserInfo;
import com.hm.iou.network.interceptor.file.ProgressListener;
import com.hm.iou.tools.AesUtil;
import com.hm.iou.tools.RsaUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQpKdfrwBtR4KnBIQSJkIFh3jlF+J3VsOTDAua" +
            "mqDXmVzacsvSpYQeSVh3gj/t8dti0r5Cs2J13SPNJKr5N9Y93GrEV25hl7tBw6xXf0zc7Sn1WcZh" +
            "y3IP6PG/U+eAI9u2gwYoAHIMN+AdSbztgAYbzC08GOFJcwWUDcesoJf2ywIDAQAB";

    String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJCkp1+vAG1HgqcEhBImQgWHeOUX" +
            "4ndWw5MMC5qaoNeZXNpyy9KlhB5JWHeCP+3x22LSvkKzYnXdI80kqvk31j3casRXbmGXu0HDrFd/" +
            "TNztKfVZxmHLcg/o8b9T54Aj27aDBigAcgw34B1JvO2ABhvMLTwY4UlzBZQNx6ygl/bLAgMBAAEC" +
            "gYB0j4ulyvQwHDmIeFYp/hSkOgjNrVK90vimLkXHoCZPt/Igw/PG28U9VuPo4ti72nRuDeBvBfop" +
            "ezzXm14AmeOWHD8k3DpUTPAqfLcqACWNaE4+p6qjlbpESTec3q55fNrmxEZ45m3Gg6+bV/i0EFKq" +
            "D0K95NagQDwE9eHwRAbnMQJBANeNMMtjN24FioLL03mnee9bMAiSDIdT3EeoCzDbCcWl2E/uI1ht" +
            "ep+nS9+EuUqa0XXTUlbx5L2WJV+ctVokM3kCQQCryR7J3XPCEBJ3KdyINwBg+ukPwn0FlDV79F3e" +
            "RM+yeBB9R8wOEwTOwhuTX3FNxfFMOyWIE0mC4a6bXZ1S/cdjAkA2YIr/tC7mlYVEy9C0rvhG1tU+" +
            "REHe3dwDQ7RXBdroPpdcSrIl+7BkmFq+r4fi9z2CIOZzwqA256rT/P+PFvTZAkBAUatfdbUT4iSn" +
            "zVqu/bj8lrWQmwdogVeNM6+5uxXxApdZ8PnsAx73syHUChZIQmbYXgTtcBtED+/qaCCxpCd1AkEA" +
            "pfUtQ8LcSOWuZUKwXiFqxxsAPouo0sQfUjujq5DpfwXKDTyRDnEGDJnSJdNReqsqwbMSjk4zIZa/" +
            "FbjBexh5+g==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_encrypt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestEncryptReqBean reqBean = new TestEncryptReqBean(29, 1000, "hjy");
                HttpReqManager.getInstance().getService(TestService.class)
                        .testEncrypt(reqBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BaseResponse<String>>() {
                            @Override
                            public void accept(BaseResponse<String> str) throws Exception {

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        });
            }
        });

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
                MobileLoginReqBean reqBean = new MobileLoginReqBean();
                reqBean.setMobile("15967132742");
                reqBean.setQueryPswd("kaka123ac11");
                HttpReqManager.getInstance().getService(TestService.class)
                        .mobileLogin(reqBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BaseResponse<UserInfo>>() {
                            @Override
                            public void accept(BaseResponse<UserInfo> response) throws Exception {
                                System.out.println(response);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable t) throws Exception {
                                t.printStackTrace();
                            }
                        });
            }
        });

        findViewById(R.id.btn_checkVersion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                String key = AesUtil.generateRandomKey();
                map.put("originalKey", key);
                map.put("random", RsaUtil.encryptByPublicKey(key, publicKey));
                map.put("pubVersion", "V1.0.0");
                HttpReqManager.getInstance()
                        .getService(TestService.class)
                        .checkVersion(map)
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
                                throwable.printStackTrace();
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