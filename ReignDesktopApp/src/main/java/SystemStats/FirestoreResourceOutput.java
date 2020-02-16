package SystemStats;

import Utils.SystemIdentity;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;

public class FirestoreResourceOutput implements SystemResourceOut {

    private SystemIdentity identity;
    private Firestore db;
    private CollectionReference statRef;

    public FirestoreResourceOutput(SystemIdentity identity, Firestore db){
        this.identity = identity;
        this.db = db;
        this.statRef = this.db.collection("systems")
                .document(identity.documentId)
                .collection("Status");
    }

    @Override
    public void output(SystemStatus status) {
            statRef.add(status);
    }
}
