import com.google.cloud.firestore.annotation.DocumentId;

public class AccessRequest {

    @DocumentId
    public String documentId;

    public String systemID;

    public String userID;

    public String systemToken;

    public AccessRequest(){

    }
}
