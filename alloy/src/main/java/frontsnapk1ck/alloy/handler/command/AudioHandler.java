package frontsnapk1ck.alloy.handler.command;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import frontsnapk1ck.alloy.handler.security.Auth;
import frontsnapk1ck.alloy.main.intefs.Audible;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.io.FileReader;
import frontsnapk1ck.utility.StringUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class AudioHandler {

    public static final long        NUMBER_OF_VIDEOS_RETURNED   = 10;
    
    public static final String      YOUTUBE_LINK_BASE           = "https://www.youtube.com/watch?v=";

    public static final Long MAX_TIME = 7200000L;

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;

    private static Audible audible;

    public static boolean join(VoiceChannel channel)
    {
        AudioManager manager = channel.getGuild().getAudioManager();
        try {
            manager.openAudioConnection(channel);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean memberIn(VoiceChannel vc, Member m)
    {
        List<Member> members = vc.getMembers();
        for (Member member : members) 
        {
            if (member.getIdLong() == m.getIdLong())
                return true;
        }
        return false;
    }

    public static boolean isConnected(Guild g) 
    {
        AudioManager manager = g.getAudioManager();
        return manager.isConnected();
    }

    public static String getUrl(String[] args) 
    {
        if (isLink(args))
            return args[0];
        else
        {
            try 
            {
                List<SearchResult> list = searchYouTube(args);
                String id = list.get(0).getId().getVideoId();
                String out = YOUTUBE_LINK_BASE + id;
                return out;
            } catch (IOException | IndexOutOfBoundsException e) 
            {
                return null;
            }

        }
    }

    public static boolean isLink(String[] args) 
    {
        URL url;
        try {
            url = new URL(args[0]);
            return  url.getProtocol().equalsIgnoreCase("http") || 
                    url.getProtocol().equalsIgnoreCase("https");
        } catch (MalformedURLException e) 
        {
            return false;
        }
    }

    private static List<SearchResult> searchYouTube(String[] args) throws IOException
    {
        // This object is used to make YouTube Data API requests. The last
        // argument is required, but since we don't need anything
        // initialized when the HttpRequest is initialized, we override
        // the interface and provide a no-op function.
        HttpRequestInitializer req = new HttpRequestInitializer()
        {
            public void initialize(HttpRequest request) throws IOException 
            {
            }
        };
        youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, req)
                                        .setApplicationName("alloy-search-youtube")
                                        .build();

        // Prompt the user to enter a query term.
        String queryTerm = StringUtil.joinStrings(args);

        // Define the API request for retrieving search results.
        YouTube.Search.List search = youtube.search().list(Arrays.asList("id","snippet"));

        // Set your developer key from the {{ Google Cloud Console }} for
        // non-authenticated requests. See:
        // {{ https://cloud.google.com/console }}
        String apiKey = FileReader.read(AlloyUtil.YOUTUBE_FILE)[0];
        search.setKey(apiKey);
        search.setQ(queryTerm);

        // Restrict the search results to only include videos. See:
        // https://developers.google.com/youtube/v3/docs/search/list#type
        search.setType(Arrays.asList("video"));

        // To increase efficiency, only retrieve the fields that the
        // application uses.
        search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
        search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

        // Call the API and print results.
        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        return searchResultList;
    }

    public static void clearQueue(Guild guild) 
    {
        audible.getGuildAudioPlayer(guild).getPlayer().stopTrack();
        audible.getGuildAudioPlayer(guild).getScheduler().clear();
    }

    public static void setAudible(Audible a) 
    {
        AudioHandler.audible = a;
    }
    
}