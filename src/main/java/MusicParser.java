import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.slf4j.*;

import org.json.*;

import java.io.IOException;

/**
 * Gets the URL of music source from given TikTok video or music URL.
 *
 * @since v0.0.1
 */
public class MusicParser
{
    private static final Logger logger = LoggerFactory.getLogger(MusicParser.class);

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

    public static void main(String[] args) throws Exception
    {
        String url = "https://www.tiktok.com/music/Cool-for-the-Summer-Sped-Up-Nightcore-7067574132983991045?_d" +
                     "=secCgYIASAHKAESPgo8iTlnQLKqD5%2BYzJDtDmT%2FymdUjZvvV7ymamczyiQdIVsMecyTVMLRG68RM%2BD" +
                     "%2FdomrkMCiWb6Oi1MSq1PaGgA%3D&_r=1&language=en&sec_user_id" +
                     "=MS4wLjABAAAA7__kURiSKYxLyDuB4cnzZ_sIABe8XIxjw3K9R0c3KLwnEwLdk06ZKbNtG2Jet9Cs&share_app_id=1233" +
                     "&share_link_id=b2476394-207f-4547-aa4d-2e05b9cb776b&share_music_id=7067574132983991045&source" +
                     "=h5_m" +
                     "&timestamp=1667374361&u_code=dmabmlfkcef9h9&ugbiz_name=Unknown&user_id=7044619555884696578" +
                     "&utm_campaign=client_share&utm_medium=android&utm_source=telegram";
        System.out.println(get(url));
    }
}
