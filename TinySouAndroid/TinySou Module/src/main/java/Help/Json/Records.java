package Help.Json;

/**
 * Created by freestorm on 14-10-13.
 */
public class Records {
    protected String collection = null;
    protected String score = null;
    protected Highlight highlight = null;
    protected Document document = null;

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getCollection() {
        return collection;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public void setHighlight(Highlight highlight) {
        this.highlight = highlight;
    }

    public Highlight getHighlight() {
        return highlight;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

}
