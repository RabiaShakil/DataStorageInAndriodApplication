#Assignment: Data Storage in Android Application
Scenario: You are working on a mobile application project for a client who wants a
productivity app that can help users manage their contacts effectively. The app should allow
users to import contact information from the device&#39;s contacts app, store this data in a local
database, and provide the ability to view, update, delete contacts, initiate calls, send
messages, and upload images from the gallery for a particular contact from within the app.
Your task is to develop this Android application.
Requirements:
1. Request Data from Another App (Contacts):
   *Implement functionality to request data (contact information) from the
   device(built-in contacts app using a Content Resolver).
   *Users should be able to select specific contacts or import all contacts into your app.
2. Store Data in a Local Database:
   * Use a suitable library (e.g., SQLite, Room, or any other library of your choice)
   to create a local database within your application.
   * Store the imported contact data in the database for further manipulation.
3. Display Data and Perform Operations:
    * Create a user-friendly interface that displays the imported contact data in a list
    view or a similar UI component.
    * Implement the following operations for each contact:
    * Update: Users should be able to edit and update contact information.
    * Delete: Users should be able to delete contacts from the app.
    * Call: Add an option that, when clicked, opens the device&#39;s dialer with
    the selected contact&#39;s phone number pre-populated for a call.
    * Message: Add an option that, when clicked, opens the device&#39;s
    messaging app with the selected contact pre-populated as the recipient
    for a message.
