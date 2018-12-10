package com.vily.audiodemo2;


import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;


//import io.reactivex.Observable;
//import retrofit2.http.POST;
//import retrofit2.http.QueryMap;

/**
 * description :
 * Author : Vily
 * Date : 2018/04/24
 * Time : 15:12
 */
public interface MyAPI {


    @Multipart
    @POST("getUpload.do")
    Observable<Object> test(@Part() MultipartBody.Part parts);



}
