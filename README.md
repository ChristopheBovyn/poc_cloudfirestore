# poc_cloudfirestore

This project is a POC made to integrate a Firebase Cloud Firestore to Rest API in JAVA.

## Getting started

### Firebase Cloud Firestore 

Follow step 1 to 5 from [Firebase Firestore documentation](https://firebase.google.com/docs/firestore/quickstart)

Then go to the firebase console > Project Settings > Service accounts and click on "GENERATE NEW PRIVATE KEY". This will download a JSON file, which will allow your API to access Firestore. If you loose it, you can redo this step.

#### Firestore Database Rules

Go to Firebase Console > Database > Rules.
My database have a read public access and require authentification for writting.
Below my rules but you can use your own.

```javascript
rules_version = '2'; 
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=\*\*} {
      allow read, write: if request.auth.uid != null;
    }
  }
}
```



### REST API

The REST API is hosted on a Apache Tomcat Server embbed in a Spring Starter with Spring Web dependency, using Maven and Java 11.

The API default path is : **locahost:8080**

Open the project on your IDE (IntelliJ for example) and put your JSON file generated before in **src/main/resources**, you can also put this file as you want, you'll just need to update the field **"firebaseConfiFilePath"** .
For example, if you put it on src folder and your json file name is "firebase_conf.json", set as following :
```java
    private final String firebaseConfigFilePath = "src/firebase_conf.json"
```

in **CoutryService.java** (src/main/com/example/cloudfirestore/services).

In my example, I use a collection named "countries", create a collection with this name or change the field **countryCollectionName** in **CountryService.java**

You can now launch the app and test it using a client (Postman for example).

The API provides 3 endpoints :
 - GET localhost:8080/country which retrieve all documents in my collection
 - POST localhost:8080/country to add a new document in the collection
- PUT localhost:8080/country to update a document in the collection

You can change the path in **CountryController**(src/main/com/example/cloudfirestore/rest) by using Spring annotations.
