import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SystemCheckIn {
    private Firestore db;
    private String systemID;

    public SystemCheckIn(Firestore db){
        this.db = db;
        this.systemID = loadSavedID();
        try {
            if(!checkIfRegistred()){
                registerWithFirestore();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public SystemIdentity getSystemIdentity(){
        try {
            DocumentReference docRef = db.collection("systems").document(systemID);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if(document.exists()){
                return  document.toObject(SystemIdentity.class);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean checkIfRegistred() throws ExecutionException, InterruptedException {
        if (systemID.equals("")){
            return false;
        }
        DocumentReference docRef = db.collection("systems").document(systemID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        return document.exists();
    }

    private boolean registerWithFirestore() throws ExecutionException, InterruptedException {
        System.out.println("making new system in database");
        SystemIdentity identity = new SystemIdentity("from java", 3, true);
        CollectionReference docRef = db.collection("systems");
        ApiFuture<DocumentReference> future = docRef.add(identity);
        String systemID = future.get().getId();
        saveSystemID(systemID);
        return true;
    }

    private void saveSystemID(String id){
        System.out.println("writing system id");
        JSONObject obj = new JSONObject();
        obj.put("systemID", id);
        try{
            FileWriter file = new FileWriter("system_settings.json");
            file.write(obj.toJSONString());
            file.flush();
            file.close();
            System.out.println("updated system id");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadSavedID(){
        try{
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader("./system_settings.json");
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            String out = (String) jsonObject.get("systemID");
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
}
