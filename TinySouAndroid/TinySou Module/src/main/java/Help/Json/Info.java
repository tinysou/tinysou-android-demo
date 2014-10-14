package Help.Json;

/**
 * Created by freestorm on 14-10-13.
 */
public class Info {
    protected String query = null;
    protected String page = null;
    protected String per_page = null;
    protected String total = null;
    protected String max_score = null;

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public String getPer_page() {
        return per_page;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setMax_score(String max_score) {
        this.max_score = max_score;
    }

    public String getMax_score() {
        return max_score;
    }
}
