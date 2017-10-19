package com.shaoqing.tryhttp;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.shaoqing.tryhttp.MyCookieJar;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by DE on 2017/9/18.
 */
public class OkHttpUtils {

    static {

        mInstance = new OkHttpUtils();
    }

    public static final String TAG = "OkHttpUtils";

    private static final String ACTION = "/detect";
    private static final String PORT1 = "10624";
    private static final String PORT2 = "5000";
    private static final String PORT3 = "9002";
    private static final String PORT4 = "24380";

    public static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/jpeg");

    private static OkHttpUtils mInstance;
    private OkHttpClient mHttpClient;

    private OkHttpUtils() {

        mHttpClient =  new OkHttpClient.Builder().build();

    };

    public static OkHttpUtils getInstance(){

        return  mInstance;

    }

    public String uploadImage(String ip, File imgFile) throws Exception{

        String name = imgFile.getName();
        RequestBody req_body = new MultipartBody.Builder()

                .setType(MultipartBody.FORM)
                //.addFormDataPart()
                .addFormDataPart("photo", imgFile.getName(), RequestBody.create(MEDIA_TYPE_IMAGE, imgFile))
                .build();

        String url = "http://" + ip + ":" + PORT1;
        Request request = new Request.Builder()
                //.addHeader("Connection","close")
                .url(url)
                .post(req_body)
                .build();

        Response response = mHttpClient.newCall(request).execute();


        Buffer buff_request = new Buffer();
        //response.body().toString();
        //response.body().writeTo(buff_request);
        //Log.i(TAG, buff_request.readUtf8());
        Log.i(TAG, response.body().toString());
        return response.toString();
    }

    public String uploadImage2(String ip, File imgFile) throws Exception {
        String url = "http://" + ip + ":" + PORT4;

        OkHttpClient.Builder client_builder = new OkHttpClient.Builder();
        client_builder.cookieJar(new MyCookieJar());
        OkHttpClient client_a = client_builder.build();
        //OkHttpClient client_a = new OkHttpClient();
        Request get_a = new Request.Builder()
                .get()
                .url(url)
                .build();

        Response response_a = client_a.newCall(get_a).execute();
        Document parse_a = Jsoup.parse(response_a.body().string());
        Elements table_a = parse_a.select("input[type=hidden]");
        Element ele_a = table_a.get(0);
        String token_a = ele_a.attr("value");
        Log.i(TAG,token_a);


        RequestBody req_b_body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("csrf_token",token_a)
                .addFormDataPart("photo", imgFile.getName(), RequestBody.create(MEDIA_TYPE_IMAGE, imgFile))
                .build();

        //Buffer req_b_buffer = new Buffer();
        //req_b_body.writeTo(req_b_buffer);
        //Log.i("!!!!!",req_b_buffer.readUtf8());



        Request req_b = new Request.Builder()
                //.addHeader("Connection","close")
                .url(url)
                .post(req_b_body)
                .build();

        Log.i("111111","00");
        Log.i("???",req_b.headers().toString());

        client_a.newCall(req_b).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("infooooo",response.toString());
            }
        });

        return req_b.toString();
    }
    public String detectImage(String ip, File imgFile, String dtcType) throws IOException{
        String url = String.format("http://%s:%s%s?nm=%s&dtcType=%s",ip, PORT2, ACTION, imgFile.getName(), dtcType);
        Log.i(TAG, url);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = mHttpClient.newCall(request).execute();

        return response.body().string();
    }


    public void writeFile(File f, String s){
        try {
            FileOutputStream fos = new FileOutputStream(f);
            byte[] bytes = s.getBytes();
            fos.write(bytes, 0, bytes.length);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

}
