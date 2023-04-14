# Marvel-Syntax-Final-project
This project was made as the final project for module 3 at Syntax institute, the core idea of the app was to make use of the Marvel-API and present the information that this API provides to the user.
The user has the ability to search for any marvel character and comic-serie, as well as saving favorite characters and series into a library connected to a firebase-firestore.


## How to setup & run the app:
1. Sign up and request an API-Key at: 
[Marvel](https://developer.marvel.com/)
2. Sign up and create a Firebase project at: 
[Firebase](https://firebase.google.com), 
for assistance on creating a firebase-project and setting it up with Android Studio please check: 
[Firebase docs](https://firebase.google.com/docs/android/setup)
3. Once a Firebase-project has been created download the google-services.json
4. Enable Email & Password authentification as well as Firestore
5. Open Marvel-Syntax-Final-project in Android Studio and replace the current google-service.json file with your own (it can be found in a folder at "Project -> App")
6. Navigate to the remote package and in MarvelApi.kt replace the API_KEY and PRIVATE_KEY with your keys provided by Marvel
7. Press play and you're good to go.

-------

This project taught me a lot about RESTful services, authentification, implementation of a databank / cloud-storage and challenged me to handle the errors that comes with using these features. 
<br> <br> Fun factor 10/10 😃
