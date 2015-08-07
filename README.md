A Functional Android WatchFace that allows interactivity for the user. 

As it stands Android Watch Faces do not permit touches from the user. The touches that it handles go straight to the OS. What I have done here is hijacked those touches with a sort of Hud View. 

To deactivate the hud, just click on the real estate above the Events Mod (i.e. the top of the watch face) or induce a peak card display. Hud will pop back up when the user comes back to the watch face or when a peek card is dismissed.

I have created "Mods" that, as they stand do three things. 1. grabs your Google Calendar Events and displays them to the screen, while providing the user a swipe gesture listener that will go to the next or previous Event in your calendar. 2. It allows the user to access their fitness app and their timer app. 3. It allows them to get the current weather in Biddeford, ME (will add zipcode prompt on CompanionApp to change location of weather update - not everyone lives in Biddo Maine); if a) the Companion App is active (on the phone), b) user has bluetooth connect between phone and wearable, 3) user has wifi or data mobile available, 4) click on the degree display on the watchface, it will run a thread and grab weather from OpenWeather. I will allow customization, and provide the user the ability to swap out the Mods, change colors, positions, and sizes from their phone and have the changes take effect on their watch - debating spliting some of the customization settings on the watchface config app instead of all on the Companion app (the phone sister app). 

Fitness Mod is tied to Google Fitness, and the Timer Mod is tied to MiTimer (self made app). So make sure you have Google Fitness and my MiTimer application from this git repository.

It is still being heavily worked on and is not a final product. It works fine if you have MiTimer downloaded and you are using an ASUS Zen Watch. Customizeable features are not working atm on the Companion Application.
