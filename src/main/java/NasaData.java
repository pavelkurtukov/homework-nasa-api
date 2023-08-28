import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NasaData {
    private final String date;
    private final String url;
    private final String hdUrl;
    private final String mediaType;
    private final String title;

    public NasaData(
        @JsonProperty("date")
        String date,
        @JsonProperty("url")
        String url,
        @JsonProperty("hdurl")
        String hdUrl,
        @JsonProperty("media_type")
        String mediaType,
        @JsonProperty("title")
        String title
    ) {
        this.date = date;
        this.url = url;
        this.hdUrl = hdUrl;
        this.mediaType = mediaType;
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getHdUrl() {
        return hdUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "NasaData{" +
                "date='" + date + '\'' +
                ", url='" + url + '\'' +
                ", hdUrl='" + hdUrl + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
