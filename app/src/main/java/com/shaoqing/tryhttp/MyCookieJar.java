package com.shaoqing.tryhttp;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
/**
 * Created by Shaoqing on 2017/10/18.
 */

public class MyCookieJar implements CookieJar {

    private static List<Cookie> cookies;

    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
        this.cookies =  cookies;
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        if (null != cookies) {
            return cookies;
        } else {
            return new ArrayList<Cookie>();
        }
    }

    public static void resetCookies() {
        cookies = null;
    }
}
