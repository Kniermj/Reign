package CommandLine;

import Utils.SystemIdentity;
import com.google.cloud.firestore.*;

public class FirestoreCommandOutput implements CommandOutput {

    private SystemIdentity identity;
    private Firestore db;

    public FirestoreCommandOutput(SystemIdentity identity, Firestore db){
        this.identity = identity;
        this.db = db;
    }

    @Override
    public boolean output(CommandObject obj) {
        updateSystemState();
        updateSystemCommandQueue(obj);
        updateSystemCommandHistory(obj);
        return true;
    }

    private void updateSystemCommandHistory(CommandObject obj) {
        DocumentReference docRef = this.db.collection("systems")
                .document(identity.documentId)
                .collection("CommandHistory")
                .document();
        docRef.set(obj);
    }

    private void updateSystemCommandQueue(CommandObject obj) {
        DocumentReference docRef = this.db.collection("systems")
                .document(identity.documentId)
                .collection("CommandQueue")
                .document(obj.documentId);
        docRef.delete();
    }

    private void updateSystemState() {
        this.identity.processing = null;
        DocumentReference docRef = this.db.collection("systems").document(identity.documentId);
        docRef.set(this.identity);
    }
}
