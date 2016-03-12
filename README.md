# BarT-Android
The Android code for BarT, the automated bartender

The BarT is a system built by Mithun Manivannan, Andrew Doskochynskyy, and Matthey Koopman in the Spring of 2015 for our Senior Design at UIUC. It consists of a wooden frame containing the arduino+BLE shield, CO2 canister, bottles with liquid, actuators, the dispensing section, and the infinity mirror.

This app allows an Android device running Lollipop+ to interface through BLE with the Arduino that controls the BarT. This app sends data about the drink that the user chose (this data is mirrored on the Arduino) and the user's name that they entered. The Arduino then proceeds to send signals to the actuators to open valves to the proper liquids to pour the drinks out.

App Flow:
 1. Choose from a list of drinks
 2. Order and enter your name
 3. Wait for BarT to pour your drink!

Pictures: http://imgur.com/a/kO7On

Hackaday: http://hackaday.com/2015/04/26/senior-design-project-serves-infinite-drinks/#comment-2543066

Paper: https://drive.google.com/file/d/0B_xuNIeTZd1UV0hfSTE4ZzNtc1U/view?usp=sharing

Presentation: https://drive.google.com/file/d/0B_xuNIeTZd1UaWNMUWdtd2dBcnM/view?usp=sharing
