package Help.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freestorm on 14-10-13.
 */
public class Errors {
    protected List<String> search_fields = new ArrayList<String>();

    public void setSearchFields(List<String> search_fields) {
        this.search_fields = search_fields;
    }

    public List<String> getSearchFields() {
        return search_fields;
    }
}
