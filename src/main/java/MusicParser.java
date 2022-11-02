import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.*;

import org.json.*;

import java.io.IOException;

/**
 * This huita is working a lil bit
 * @since v0.0.1
 */
public class MusicParser
{
    private static final Logger logger = LoggerFactory.getLogger(MusicParser.class);

    public static String lastURL = "";

    public static String parse(String url) throws Exception
    {
        url = url.strip();
        Connection.Response res = Jsoup.connect(url).followRedirects(true).execute();
        url = res.url().toString();
        lastURL = url;

        if (url.matches("(?:http[s]?:\\/\\/)?(?:www.)?(?:[a-z]*\\.)*tiktok\\.com\\/.*music\\/.+")) {
            return parseMusic(url);
        }
        else if (url.matches("(?:http[s]?:\\/\\/)?(?:www.)?(?:[a-z]*\\.)*tiktok\\.com\\/.*video\\/.+"))
        {
            logger.info("" + url);
            return parseVideo(url);
        }
        else
        {
            logger.info(url + " is not supported");
            throw new Exception("Given URL is not supported");
        }
    }

    private static String parseMusic(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            var jsonResponse = doc.select("#SIGI_STATE").html();

//            System.out.println(jsonResponse);

            var obj = new JSONObject(jsonResponse);
            System.out.println(obj.length());
            var music = obj.getJSONObject("MusicModule").getJSONObject("musicInfo").getJSONObject("music");
            String musicURL = music.getString("playUrl");
            System.out.println(musicURL);
//            var arr = new JSONArray(obj.toString());
//            arr.getJSONArray();
//            System.out.println(arr.toString());
//            for (int i = 0; i < arr.length(); i++) { // Walk through the Array.
//                JSONObject obj = arr.getJSONObject(i);
//                System.out.println(obj.toString());
//            }



//            System.out.println(music.toString());

//            System.out.println(doc.html());
            return "";
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static String parseVideo(String url) {
        return null;
    }

    public static void main(String[] args) throws Exception
    {
        System.out.println(MusicParser.parse("https://vt.tiktok.com/ZSR7kQNUr/"));
        System.out.println(lastURL);
    }

}
