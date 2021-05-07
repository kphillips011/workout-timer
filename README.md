## **Workout Timer**



## **Learning Objectives**
- Familiarize with Android App Development
- Git Source Code Management



## **System Requirements**
- Android Studio
- Java SDK 8 or later
- Gradle with Kotlin



## **Description**
- This workout application is tailored towards the user’s needs such as creating custom workouts
- Users are able to upload images associated with those routines
- Users pick from a default list of workout to add/delete
- Add/delete custom workouts independent of a default list
- Tailor the time interval for each specific pose



## **Running the Application**
- Clone project repo and save in the project root directory, e.g., /home/youruserid/StudioProjects/workout-timer
- Create Android Virtual Device with API level 26 or later
- Edit configuration and make sure app is selected
- Run app 



## **Testing Procedure**
- We plan to implement Unit and Instrumented tests, but our primary testing method as of right now is manual testing.
- If the user presses the Extended Floating Action Button, the button should expand into two Floating Action Buttons giving the option to either add a preset or custom workout/element depending on the View they are on.
- If the user presses the "play" button on any workout's page, the workout should be played in order of the elements listed.
- If the user uploads an image for an element in particular workout, the image should stay attached to that element for the duration of the app session.



## **License**
- MIT
