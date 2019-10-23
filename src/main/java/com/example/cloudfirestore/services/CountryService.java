package com.example.cloudfirestore.services;

import com.example.cloudfirestore.exceptions.DocumentAlreadyExistException;
import com.example.cloudfirestore.exceptions.UnknownDocumentException;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firestore.v1.Document;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CountryService {

  private Firestore client;
  private final String countryCollectionName = "countries";
  private final String firebaseConfigFilePath =
      "src/main/resources/ofood-cloud-ar-firebase-adminsdk.json";

  public CountryService() {
    this.initializeFirebase();
  }

  private void initializeFirebase() {
    try {
      InputStream serviceAccount = new FileInputStream(this.firebaseConfigFilePath);
      var credentials = GoogleCredentials.fromStream(serviceAccount);
      var options = new FirebaseOptions.Builder().setCredentials(credentials).build();
      FirebaseApp.initializeApp(options);
      this.client = FirestoreClient.getFirestore();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private DocumentReference getDocumentReference(String document){
    return this.client.collection(this.countryCollectionName).document(document);
  }

  private boolean documentExist(String document) throws ExecutionException, InterruptedException {
    DocumentReference docRef = this.getDocumentReference(document);
    ApiFuture<DocumentSnapshot> future = docRef.get();
    DocumentSnapshot doc = future.get();
    if(doc.exists()){
      return true;
    }
    return false;
  }

  public void addCountry(String document, HashMap<String, Object> data) throws ExecutionException, InterruptedException, DocumentAlreadyExistException {
    var docRef = this.getDocumentReference(document);
    if(!this.documentExist(document)){
      ApiFuture<WriteResult> result = docRef.set(data);
      System.out.println("Update time : " + result.get().getUpdateTime());
    }else{
      throw new DocumentAlreadyExistException();
    }
  }

  public List<HashMap<String, Object>> getCountries()
      throws ExecutionException, InterruptedException {
    List<HashMap<String, Object>> countries = new ArrayList<>();
    ApiFuture<QuerySnapshot> query = this.client.collection(this.countryCollectionName).get();
    QuerySnapshot querySnapshot = query.get();
    List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
    for (QueryDocumentSnapshot document : documents) {
      countries.add((HashMap<String, Object>) document.getData());
    }
    return countries;
  }

  public void updateCoutry(String document, HashMap<String, Object> updatedFields)
      throws UnknownDocumentException,ExecutionException, InterruptedException{
    var docRef = this.getDocumentReference(document);
    if(this.documentExist(document)){
      ApiFuture<WriteResult> result = docRef.update(updatedFields);
      System.out.println("Update time : " + result.get().getUpdateTime());
    }else{
      throw new UnknownDocumentException();
    }
  }
}
