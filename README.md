# Playtrans

Command line application that takes the playlist from a radio station and creates a playlist in your Spotify account.

# Configure

Create an 'App' on https://developers.spotify.com. Call it playtrans. Redirect uri must be http://localhost:1234.
In the directory `app/src/main/resources` create a file called `application.properties`.
It should contain the following information:
```
userId=
auth.clientId=
auth.clientSecret=
auth.redirectUri=http://localhost:1234
```
Userid should be your own. Technically it also works with other users (but they need to grant access).
ClientId is the id in the app you created on spotify.
ClientSecret is the secret in the app you created on spotify.

# Build

  mvn clean install

# Run the application

  java -jar app/target/playtrans-app-1.0.0-SNAPSHOT.jar

The application will show a list of radio stations you can choose.
When you typed in the number of the radio station you are prompted to select a date.
Enter a valid date (in yyyy-mm-dd format) that is not older than 30 days.
You can select todays date but your list will be short unless you run it at midnight.
The application will pop-up your browser to authenticate against Spotify.
It will load some time, to speed it up you can press enter in the URL bar of the browser.
The application will receive a code from Spotify and connect.
Then it will search all songs and create the playlist.
Open spotify to see the new playlist in your account.