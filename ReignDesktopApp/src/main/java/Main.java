import CommandLine.*;
import ProcessStats.FirestoreProcessOutput;
import ProcessStats.ProcessFinderTrigger;
import ProcessStats.ProcessRefresh;
import ProcessStats.ProcesssFinder;
import SystemStats.FirestoreResourceOutput;
import SystemStats.SystemResourceFinder;
import SystemStats.SystemResourceOut;
import Utils.SystemCheckIn;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args){
        Firestore db = null;
        try {
            InputStream serviceAccount = ClassLoader.getSystemResourceAsStream("reign-76b44-firebase-adminsdk-npgy4-02aae91faa.json");
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
        check.getSystemIdentity();

        RegisterUser register = new RegisterUser(db, check.identity);

        CommandFeeder feeder = new CommandFeederQueue(check.identity, db);
        CommandOutput commandOut = new FirestoreCommandOutput(check.identity, db);

        CommandLineExecution command  = new CommandLineExecution(feeder, commandOut);

        FirestoreProcessOutput processoutput = new FirestoreProcessOutput(check.identity, db);
        ProcessFinderTrigger trigger = new ProcessRefresh(check, db);
        ProcesssFinder res = new ProcesssFinder(processoutput, trigger);

        SystemResourceOut systemOut = new FirestoreResourceOutput(check.identity, db);
        SystemResourceFinder systemRes = new SystemResourceFinder(systemOut);


        command.start();
        res.start();
        systemRes.start();;

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                System.out.println("stopping");
                systemRes.stop();
                res.stop();
                command.stop();
                check.shutdown();
            }
        });

        System.out.println("setup");

    }
}
