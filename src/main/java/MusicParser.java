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
        lastURL = url;
        try {
            Document doc = Jsoup.connect(url).get();
            var jsonResponse = doc.select("#SIGI_STATE").html();
            logger.info(jsonResponse);

            var obj = new JSONObject(jsonResponse);

            var music = obj.getJSONObject("MusicModule").getJSONObject("musicInfo").getJSONObject("music");
            String musicURL = music.getString("playUrl");
            System.out.println(musicURL);
            return "";
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    // TODO: refactor this shit
    private static String parseVideo(String url) {
        lastURL = url;
        try {
            Document doc = Jsoup.connect(url).get();
            var jsonResponse = doc.select("#SIGI_STATE").html();
//            logger.info(jsonResponse);

            var obj = new JSONObject(jsonResponse);

//            var music = obj.getJSONObject("ItemModule").getJSONObject(0).getJSONObject("music");
            var module = obj.getJSONObject("ItemModule");
            var music = module.getJSONObject(module.keys().next()).getJSONObject("music");

            String musicURL = music.getString("playUrl");
            System.out.println(musicURL);
//            logger.info(music.toString());
            return "";
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception
    {
        parseVideo("https://www.tiktok.com/@genboy3/video/7075486642357718298?_r=1&_t=8X0xGtbsjXF&is_from_webapp=v1" +
                   "&item_id=7075486642357718298");
//        System.out.println(MusicParser.parse("https://vt.tiktok.com/ZSR7kQNUr/"));
//        System.out.println(lastURL);
    }

}
