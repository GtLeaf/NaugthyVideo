package com.example.pc_0775.naugthyvideo.model.remote;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by PC-0775 on 2018/12/29.
 */

public interface BookServiceJ {
    @GET("book/{id}")
    Call<ResponseBody> getBook(@Path("id") int id);
}
