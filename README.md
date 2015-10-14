# Color Capture Android App

##Project Pitch:
This app changes the color of the Raspberry Pi LED strip. From the home screen the user can change the LED strip to red, green, or blue. The app also allows the user to "Capture" a color by taking a photo using the mobile device's camera and touching any color within the taken photo. You can also tweet the color you've captured to our Twitter page (@LEDColorCapture)!
List of key features:
- This application uses the built­in camera to take photos
- Pixel analysis on taken photos on pixels selected by touch
- LED color RGB values are sent as POST JSON to any IP address
- Tweet “captured” RGB values to the @LEDColorCapture Twitter page

##Basic instructions on usage:
Upon opening the app, user can set the LED color to red, green, or blue using the clearly labeled buttons. If those buttons do not work, user should check the url and change if necessary. User can also select the “Color Capture” button to open to a new page where the color capture function is located.
User should select the “Open Camera” button to take a picture. After taking the photo and confirming via the checkmark, user can touch anywhere on the displayed photo to load a color. Then user can send the “captured” color to the LED strip or tweet the color “captured” to the @LEDColorCapture Twitter page.

##Fun Tidbits
- I used lights to show the captured color that is sent through POST JSON.
- I used the camera to take pictures for pixel analysis.
- I used Twitter with a Java library for the Twitter API called Twitter4J. You may find our twitter page at https://twitter.com/LEDColorCapture.
