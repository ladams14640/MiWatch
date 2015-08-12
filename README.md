# MiWatch
A Functional Android WatchFace that allows interactivity for the user. 

As it stands Android Watch Faces do not permit touches from the user. The touches that it handles go straight to the OS.
What I have done here is hijacked those touches with a sort of Hud View. 

To deactivate the hud, just click on the real estate above the Events Mod (i.e. the top of the watch face), it will pop back up when the user comes back to the watch face or when a peek card is dismissed.

I have created "Mods" that, as they stand do two things. 1. grabs your Google Calendar Events and displays to the screen, while providing the user a swipe gesture listener that will go to the next Event in your calendar. 2. It allows the user to access their fitness app and their timer app. ATM I find that I use those the most. I will allow customization, and provide the user the ability to swap out the Mods, change colors, positions, and sizes from their phone and have the changes take effect on their watch. 

ATM, if you try to use the Alarm Timer Mod or the Fitness Mod they probably wont work for you. 1. because the Timer mod is hooked into an app I made and havent released yet, but I will put it on the repository. and 2. The Fitness Mod ties to the Asus Zen Watch Fitness, so if you don't have that watch you will get a crash. Probably will tie it to Google's fitness app so it becomes universal, or better yet, provide the user the ability to choose what fitness app they want to point to for the Mod, from their phone.

It is still being heavily worked on and is not a final product.
It works fine if you have MiTimer downloaded and you are using an ASUS Zen Watch.
Customizeable features are not working atm.




