package com.playsiteapp.data.dbservices.firebase;


import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.playsiteapp.domain.models.Business;
import com.playsiteapp.domain.models.PlaySitePOI;
import com.playsiteapp.domain.models.Playground;

import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.operators.observable.ObservableFromCallable;
import io.reactivex.subjects.CompletableSubject;
import rxfirebase2.database.RxFirebaseDatabase;

/**
 * Created by Coco on 11/07/2017.
 */

public class FirebaseDataSource extends BaseFirebaseConstants {

    public static void setValue(String key, PlaySitePOI playSitePOI) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = null;

        if (playSitePOI instanceof Playground) {

            dbRef = firebaseDatabase.getReferenceFromUrl(firebaseDatabase.getReference() + FIREBASE_CHILD_PLAYGROUND_INFO_KEY + key);
        } else if (playSitePOI instanceof Business) {

            dbRef = firebaseDatabase.getReferenceFromUrl(firebaseDatabase.getReference() + FIREBASE_CHILD_BUSINESS_INFO_KEY + key);
        }

        RxFirebaseDatabase.setValue(dbRef, playSitePOI).subscribe();
    }

}