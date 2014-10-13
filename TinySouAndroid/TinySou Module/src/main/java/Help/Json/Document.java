package Help.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freestorm on 14-10-13.
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
}
