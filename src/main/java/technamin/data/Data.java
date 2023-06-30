package technamin.data;

import org.bson.codecs.pojo.annotations.BsonId;


public class Data {

    @BsonId
    private Long doc_id;
    private Long seq;
    private String data;
    private Long time;

    public Long getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(Long doc_id) {
        this.doc_id = doc_id;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
