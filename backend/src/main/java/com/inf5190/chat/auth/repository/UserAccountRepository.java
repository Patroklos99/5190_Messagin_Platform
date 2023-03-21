package com.inf5190.chat.auth.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;



@Repository
public class UserAccountRepository {
    private static final String COLLECTION_NAME = "userAccounts";
    private final Firestore firestore;

    public UserAccountRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public FirestoreUserAccount getUserAccount(String username) throws
            InterruptedException, ExecutionException {
        final CollectionReference collectionRef = firestore.collection(COLLECTION_NAME);
        final DocumentReference docRef = collectionRef.document(username);
        final ApiFuture<DocumentSnapshot> apiFuture1 = docRef.get();
        final DocumentSnapshot result1 = apiFuture1.get();
        if(result1.exists())
            return result1.toObject(FirestoreUserAccount.class);
        return null;

    }


    public void setUserAccount(FirestoreUserAccount userAccount) throws
            InterruptedException, ExecutionException {
        final CollectionReference collectionRef = firestore.collection(COLLECTION_NAME);
        final DocumentReference docRef = collectionRef.document(userAccount.getUsername());

        final ApiFuture<WriteResult> apiFuture1 = docRef.create(userAccount);
        apiFuture1.get();
        //throw new UnsupportedOperationException("A faire");
    }

}
