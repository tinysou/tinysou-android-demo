package Help.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tinysou on 14-10-13.
 */
public class Document {
    public String id = null;
    public String title = null;
    public List<String> tags = new ArrayList<String>();
    public String author = null;
    public String updated_at = null;
    public String body = null;
    public List<String> sections = new ArrayList<String>();
    public String url = null;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setUpdatedAt(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setSections(List<String> sections) {
        this.sections = sections;
    }

    public List<String> getSections() {
        return sections;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
