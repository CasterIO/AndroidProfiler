package bootstrap.casterio.com.myapplication.api;


import bootstrap.casterio.com.myapplication.model.PopularMemes;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MemeInterface {

    @GET("get_memes")
    Call<PopularMemes> loadMemes();
}
