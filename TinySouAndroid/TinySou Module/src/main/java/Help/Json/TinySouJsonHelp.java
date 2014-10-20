package Help.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tinysou on 14-9-22.
 * Author:Yeming Wang
 * Data: 2014.10.11
 * 简介：微搜索Json类，用于Json处理
 */
public class TinySouJsonHelp {
    protected Info info;
    protected List<Records> records = new ArrayList<Records>();
    protected Errors errors;

    public void TinySouJsonHelp() {
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Info getInfo() {
        return info;
    }

    public void setRecords(List<Records> records) {
        this.records = records;
    }

    public List<Records> getRecords() {
        return records;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
