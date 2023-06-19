package technamin.data;

import java.sql.Timestamp;

public class Data {
    private String docId;
    private Integer seqIs;
    private String data;
    private Timestamp timeIs;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public Integer getSeqIs() {
        return seqIs;
    }

    public void setSeqIs(Integer seqIs) {
        this.seqIs = seqIs;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Timestamp getTimeIs() {
        return timeIs;
    }

    public void setTimeIs(Timestamp timeIs) {
        this.timeIs = timeIs;
    }
}
