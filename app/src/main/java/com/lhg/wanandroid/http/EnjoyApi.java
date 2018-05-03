package com.lhg.wanandroid.http;

import com.lhg.wanandroid.bean.CollectArticleBean;
import com.lhg.wanandroid.bean.CollectWebsiteBean;
import com.lhg.wanandroid.bean.IndexBean;
import com.lhg.wanandroid.bean.UserResultBean;
import com.lhg.wanandroid.bean.ProjectCastBean;
import com.lhg.wanandroid.bean.ProjectListBean;
import com.lhg.wanandroid.bean.SearchWordsBean;
import com.lhg.wanandroid.bean.SystemCastBean;
import com.lhg.wanandroid.bean.SystemListBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EnjoyApi {

    String baseUrl = "http://www.wanandroid.com/";

    @GET("article/list/{id}/json")
    Observable<IndexBean> getIndexBean(@Path("id") int id);

    @GET("tree/json")
    Observable<SystemCastBean> getSystemCastBean();

    @GET("article/list/{id}/json")
    Observable<SystemListBean> getSystemListBean(@Path("id") int id, @Query("cid") int cid);

    @GET("project/tree/json")
    Observable<ProjectCastBean> getProjectCastBean();

    @GET("project/list/{id}/json")
    Observable<ProjectListBean> getProjectListBean(@Path("id") int id, @Query("cid") int cid);

    @GET("hotkey/json")
    Observable<SearchWordsBean> getSearchWordsBean();

    @FormUrlEncoded
    @POST("article/query/{id}/json")
    Observable<IndexBean> getSearchResultBean(@Path("id") int id, @Field("k") String words);

    @FormUrlEncoded
    @POST("user/login")
    Observable<UserResultBean> getLoginResult(@Field("username") String username, @Field("password") String passwords);

    @FormUrlEncoded
    @POST("user/register")
    Observable<UserResultBean> getRegisterResult(@Field("username") String username,
                                                 @Field("password") String passwords,
                                                 @Field("repassword") String repassword);

    @GET("lg/collect/list/{id}/json")
    Observable<CollectArticleBean> getCollectArticle(@Path("id") int id);

    //http://www.wanandroid.com/
    @GET("lg/collect/usertools/json")
    Observable<CollectWebsiteBean> getCollectWebsite();



}
