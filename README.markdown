# Cloudsdale Mobile for Android

## Projects

The pulled folder will contain the following folders:

>- Cloudsdale Mobile (CDM)
>- Assets -- All the raw assets for the projects
>	- Gson -- JSON de/serialization module
>	- Twitter4J -- The Twitter library for Android apps
>	- jBCrypt -- Java implementaiton of the Blowfish encryption protocol

You will need to pull the following using Git submodules:

>- Holo Everywhere (HE) -- Creates the Holo UI on all Android platforms
>- ActionBar Sherlock (ABS) -- Allows use of the ActionBar on all Android platforms
>- Facebook SDK -- Can be updated by opening it via Git
>- Faye Android library  -- Android Faye client


## Importing into Eclipse

DOING THIS LATER

### Setting up your device for debug

On your device, go to ```System settings -> Apps```
Check the box for ```Unknown Sources``` and enable ```ADB debugging```

### Setting up an Intel Accelerated AVD (Only for ICS/JB images)

Open your SDK manager, and under the "Extras" header, make sure that the "Intel Harware Accelerated Execution Manager" is either installed or maked to be installed. If it is marked to be installed, install it.

After the execution manager is installed, navigate to [SDK_DIR]/extras/intel/Hardware_Accelerated_Execution_Manager/ and run the executable. Default settings should work just fine.

Follow all the regular steps to create an AVD, making sure to select API level 15 (ICS) or 16 (JB), and selecting the Intel Atom option for your CPU.

### Creating an AVD (Emulated Android device)

Open up the AVD Manager in your SDK directory. Click "new", select your platform level, select your CPU if applicable, fill in a value for the SD card (I generally do 2048MB). From here, you can either create the AVD after naming it, or select a built-in pre-defined device from the built-in skin dropdown. Save your device, then click it's entry in the list and click start. The AVD will boot. Initial boot can take some time, so please be patient.