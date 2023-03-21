package com.inf5190.chat.messages.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import com.inf5190.chat.messages.model.Message;
import com.inf5190.chat.messages.model.MessageRequest;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Classe qui gère la persistence des messages.
 * En mémoire pour le moment.
 */
@Repository
public class MessageRepository {
    //private final List<Message> messages = new ArrayList<Message>();
    //private final AtomicLong idGenerator = new AtomicLong(0);

    private final Firestore firestore ;
    private final StorageClient storageClient;
    private static final String COLLECTION_NAME = "messages";
    @Autowired
    @Qualifier("storageBucketName")
    private String storageBucketName;


    public MessageRepository(StorageClient storageClient,Firestore firestore) {
        this.firestore = firestore;
        this.storageClient = storageClient;
    }

    public List<Message> getMessages(Optional<String> fromId) throws ExecutionException, InterruptedException {
        final CollectionReference collectionRef = firestore.collection(COLLECTION_NAME);
        final Query docRef;
        if(fromId.isPresent()) {
            ApiFuture<DocumentSnapshot> future = collectionRef.document(fromId.get()).get();
            DocumentSnapshot snapshot = future.get();
            if(!snapshot.exists())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            docRef = collectionRef.orderBy("timestamp", Query.Direction.ASCENDING).startAfter(snapshot);
        }
        else{
            docRef = collectionRef.orderBy("timestamp", Query.Direction.ASCENDING).limitToLast(20);
        }
        final ApiFuture<QuerySnapshot> apiFuture1 = docRef.get();
        QuerySnapshot queryDocumentSnapshots = apiFuture1.get();
        List<Message> list = new ArrayList<>();
        for (var snap: queryDocumentSnapshots) {
            String urlimage = null;
            if(snap.get("imageUrl") != null)
                urlimage = Objects.requireNonNull(snap.get("imageUrl")).toString();
            list.add(new Message(snap.getId(), Objects.requireNonNull(snap.get("username")).toString(),
                    ((Timestamp) Objects.requireNonNull(snap.get("timestamp"))).toDate().getTime(),
                    Objects.requireNonNull(snap.get("text")).toString(),
                    urlimage));
        }
        return list;


    }

    public Message createMessage(MessageRequest message) throws ExecutionException, InterruptedException {
        String imageUrl = null;
        final CollectionReference collectionRef = firestore.collection(COLLECTION_NAME);
        final DocumentReference docRef = collectionRef.document();

        if(message.imageData() != null) {
            Bucket b = storageClient.bucket(storageBucketName);
            String path = String.format("images/%s.%s", docRef.getId(),
                    message.imageData().type());
            b.create(path, Decoders.BASE64.decode(message.imageData().data()),
                    Bucket.BlobTargetOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ));
            imageUrl = String.format("https://storage.googleapis.com/%s/%s", storageBucketName, path);
        }

        FirestoreMessage mess = new FirestoreMessage(message.username(), Timestamp.now(), message.text(),imageUrl);

        final ApiFuture<WriteResult> apiFuture1 = docRef.create(mess);
        WriteResult writeResult = apiFuture1.get();

        return new Message(docRef.getId(),message.username(),writeResult.getUpdateTime().toDate().getTime(),message.text(),imageUrl);
    }

}
