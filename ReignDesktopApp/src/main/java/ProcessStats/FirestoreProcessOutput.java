package ProcessStats;

import Utils.SystemIdentity;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

import java.util.List;

public class FirestoreProcessOutput implements ProcessOutput {

    private SystemIdentity identity;
    private Firestore db;
    private CollectionReference processRef;
    private DocumentReference systemRef;

    public FirestoreProcessOutput(SystemIdentity identity, Firestore db){
        this.identity = identity;
        this.db = db;
        this.processRef = this.db.collection("systems")
                .document(identity.documentId)
                .collection("Processes");
        this.systemRef = this.db.collection("systems")
                .document(identity.documentId);
    }

    @Override
    public void output(List<ProcessStat> status) {
        resetProcessRefresh();
        deletePreviousProcesses();
        addCurrentProcesses(status);
    }

    private void resetProcessRefresh() {
        identity.refreshProcesses = false;
        systemRef.set(identity);
    }

    private void deletePreviousProcesses(){
        Iterable<DocumentReference> processes = processRef.listDocuments();
        for (DocumentReference doc: processes) {
            doc.delete();
        }
        System.out.println("items deleted");

    }

    private void addCurrentProcesses(List<ProcessStat> status){
        for (ProcessStat proc: status) {
            processRef.add(proc);
        }
        System.out.println("items added");
    }
}
