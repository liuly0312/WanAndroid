package com.lhg.wanandroid.http;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lhg.wanandroid.bean.CollectArticleBean;
import com.lhg.wanandroid.bean.CollectWebsiteBean;
import com.lhg.wanandroid.bean.IndexBean;
import com.lhg.wanandroid.bean.UserResultBean;
import com.lhg.wanandroid.bean.ProjectCastBean;
import com.lhg.wanandroid.bean.ProjectListBean;
import com.lhg.wanandroid.bean.SearchWordsBean;
import com.lhg.wanandroid.bean.SystemCastBean;
import com.lhg.wanandroid.bean.SystemListBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private static final long TIMEOUT = 5;
    private static Gson gson;
    private final OkHttpClient.Builder builder;
    private OkHttpClient httpClient;
    private EnjoyApi enjoyApi;
    public static HashMap<String, List<Cookie>> cookieStore;

    private static class sinalInstance {
        public static final RetrofitFactory instance = new RetrofitFactory();
    }

    public static RetrofitFactory getInstance() {
        return sinalInstance.instance;
    }

    private RetrofitFactory() {
        builder = new OkHttpClient.Builder();
        cookieStore = new HashMap<>();
        builder.cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        });
        httpClient = builder
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder builder = chain.request().newBuilder();
                        return chain.proceed(builder.build());
                    }
                })
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
        enjoyApi = new Retrofit.Builder()
                .baseUrl(EnjoyApi.baseUrl)
                // 添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                // 添加Retrofit到RxJava的转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()
                .create(EnjoyApi.class);
    }

    private static Gson buildGson() {
        gson = new GsonBuilder()
                .setLenient()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                // 此处可以添加Gson 自定义TypeAdapter
                // .registerTypeAdapter(UserInfo.class, new UserInfoTypeAdapter())
                .create();
        return gson;
    }

    public void getIndexBean(int id, Observer<IndexBean> observer) {
        enjoyApi.getIndexBean(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getSystemCastBean(Observer<SystemCastBean> observer) {
        enjoyApi.getSystemCastBean()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getSystemListBean(int id, int cid, Observer<SystemListBean> observer) {
        enjoyApi.getSystemListBean(id, cid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getProjectCastBean(Observer<ProjectCastBean> observer) {
        enjoyApi.getProjectCastBean()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getProjectListBean(int id, int cid, Observer<ProjectListBean> observer) {
        enjoyApi.getProjectListBean(id, cid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getSearchWordsBean(Observer<SearchWordsBean> observer) {
        enjoyApi.getSearchWordsBean()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getSearchResultBean(int id, String words, Observer<IndexBean> observer) {
        enjoyApi.getSearchResultBean(id, words)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getLoginResult(String name, String password, Observer<UserResultBean> observer) {
        enjoyApi.getLoginResult(name, password)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getRegisterResult(String name, String password, String repassword, Observer<UserResultBean> observer) {
        enjoyApi.getRegisterResult(name, password, repassword)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getCollectArticle(int id, Observer<CollectArticleBean> observer) {
        enjoyApi.getCollectArticle(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getCollectWebsite(Observer<CollectWebsiteBean> observer) {
        enjoyApi.getCollectWebsite()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
