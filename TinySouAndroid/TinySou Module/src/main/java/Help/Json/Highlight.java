package Help.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freestorm on 14-10-13.
 */
public class Highlight {
    protected List<String> body = new ArrayList<String>();
    protected List<String> title = new ArrayList<String>();
    protected List<String> sections = new ArrayList<String>();

    public void setBody(List<String> body){
        this.body = body;
    }

    public List<String> getBody(){
        return body;
    }

    public void setTitle(List<String> title){
        this.title = title;
    }

    public List<String> getTitle(){
        return title;
    }

    public void setSections(List<String> sections){
        this.sections = sections;
    }

    public List<String> getSections(){
        return sections;
    }
}
