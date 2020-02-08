import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args){
        Firestore db = null;
        try {
            InputStream serviceAccount = ClassLoader.getSystemResourceAsStream("reign-76b44-firebase-adminsdk-npgy4-141c9e4d7b.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SystemCheckIn check = new SystemCheckIn(db);
        SystemIdentity identity = check.getSystemIdentity();

        RegisterUser register = new RegisterUser(db, identity);

        CommandFeeder feeder = new CommandFeederQueue(identity, db);
        CommandOutput out = new FirestoreCommandOutput(identity, db);
        CommandLineExecution command  = new CommandLineExecution(feeder, out);


        command.start();

    }
}
