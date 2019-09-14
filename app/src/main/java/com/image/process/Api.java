package com.image.process;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    @GET("fetch_images.php")
    Call<DefaultResponse> getImagesList();
}
