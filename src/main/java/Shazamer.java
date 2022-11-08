import com.squareup.okhttp.*;
import org.jsoup.helper.HttpConnection;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @since v0.0.3
 */

public class Shazamer
{
    public static void main(String[] args) throws IOException
    {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody data = new MultipartBuilder().type(MultipartBuilder.FORM)
                    .addFormDataPart("api_token", "aa0e50f828a45858e93dbff745dd7386")
                    .addFormDataPart("url", "https://sf16-ies-music-va.tiktokcdn.com/obj/tos-useast2a-ve-2774/9fd45eb00fcc4d52a71f19b0d80ce3e1")
                    .addFormDataPart("return", "apple_music,spotify,deezer,napster").build();
            Request request = new Request.Builder().url("https://api.audd.io/")
                    .post(data).build();
            Response response = null;
            response = client.newCall(request).execute();
            String result = null;
            result = response.body().string();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
