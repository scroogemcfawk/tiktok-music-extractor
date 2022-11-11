import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.slf4j.*;

import org.json.*;

import java.io.IOException;

/**
 * Gets the URL of music source from given TikTok video or music URL.
 * @since v0.0.1
 */
public class MusicParser
{
    private static final Logger logger = LoggerFactory.getLogger(MusicParser.class);

    private MusicParser()
    {
        throw new IllegalStateException("Utility class");
    }

    public static String get(String url) throws Exception
    {
        String goal = url.strip();
        Connection.Response res = Jsoup.connect(goal).followRedirects(true).execute();
        goal = res.url().toString();

        if (goal.matches("(?:http[s]?://)?(?:www.)?(?:[a-z]*\\.)*tiktok\\.com/.*.{5}/.+"))
        {
            return parse(url);
        }
        else
        {
            logger.info(url + " is not supported");
            throw new Exception("Given URL is not supported");
        }
    }

    // Works for both music and video URLs, not sure if there is a faster way of getting the source URL
    private static String parse(String url)
    {
        try
        {
            Document doc = Jsoup.connect(url).get();
            // Get JSON obj in HTML doc
            var jsonElement = doc.select("#SIGI_STATE").html();
            var jsonObject = new JSONObject(jsonElement);
            var module = jsonObject.getJSONObject("ItemModule");
            var music = module.getJSONObject(module.keys().next()).getJSONObject("music");
            // Get music source URL
            return music.getString("playUrl");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
