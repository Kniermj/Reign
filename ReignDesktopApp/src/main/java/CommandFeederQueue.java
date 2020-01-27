import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

public class CommandFeederQueue implements CommandFeeder {

    private SystemIdentity identity;
    private Firestore db;
    private List<CommandObject> commandQueue;

    public CommandFeederQueue(SystemIdentity identity, Firestore db){
        this.identity = identity;
        this.db = db;
        commandQueue = new ArrayList<>();
        db.collection("systems")
                .document(identity.documentId)
                .collection("CommandQueue")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        System.out.println(e);
                        return;
                    }
                    List<CommandObject> recentCommands = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots) {
                        CommandObject loadedObj = doc.toObject(CommandObject.class);
                        recentCommands.add(loadedObj);
                    }
                    commandQueue = recentCommands;
                    System.out.println(commandQueue.size());
                });


    }

    @Override
    public CommandObject getNextCommand() {
        if(this.commandQueue.size() > 0){
            this.identity.processing = this.commandQueue.remove(0);
            return this.identity.processing;
        }
        return null;

    }

    @Override
    public boolean hasCommand() {
        System.out.println(!this.commandQueue.isEmpty());
        return !this.commandQueue.isEmpty();
    }


}
