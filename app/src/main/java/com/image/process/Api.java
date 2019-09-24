package com.image.process;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("fetch_images_new.php")
    Call<DefaultResponse> getImagesList(
            @Field("postdate") String postdate,
            @Field("clientcd") String clientcd
    );

    @FormUrlEncoded
    @POST("deletesamarthak.php")
    Call<DefaultResponse> getImagesListAferDelete(
            @Field("postdate") String postdate,
            @Field("clientcd") String clientcd,
            @Field("samarthak_id") String samarthak_id
    );

    @GET("client_list.php")
    Call<ClientResponse> getClientNames();
}
