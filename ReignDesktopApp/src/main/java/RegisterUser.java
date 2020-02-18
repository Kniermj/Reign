import Utils.SystemIdentity;
import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.Firestore;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class RegisterUser {

    private SystemIdentity identity;
    private Firestore db;

    public RegisterUser(Firestore db, SystemIdentity identity){
        this.db = db;
        this.identity = identity;
        listenForNewUsers();

    }

    private String getAccessToken(){
        try{
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader("./system_settings.json");
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            String out = (String) jsonObject.get("accessToken");
            System.out.println(out);
            return out;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void addUserRefrence(AccessRequest req){
        identity.owner = req.userID;
        db.collection("systems").document(identity.documentId).set(identity);
        System.out.println("a user reference has been added");
    }

    private void removeAccessRequest(AccessRequest req){
        db.collection("access_requests").document(req.documentId).delete();
        System.out.println("a request has been removed");
    }


    private void listenForNewUsers(){
        db.collection("access_requests").whereEqualTo("systemID", identity.documentId).addSnapshotListener((snapshots, e) -> {
            System.out.println("access_request listener activated");
            for (DocumentChange item: snapshots.getDocumentChanges()) {
                if(item.getType() == DocumentChange.Type.ADDED || item.getType() == DocumentChange.Type.MODIFIED){
                    AccessRequest req = item.getDocument().toObject(AccessRequest.class);
                    if(req.systemToken.equals(getAccessToken())){
                        addUserRefrence(req);
                    }
                    removeAccessRequest(req);
                }
            }
        });
    }
}
