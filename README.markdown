# Cloudsdale Mobile for Android

## Projects

The pulled folder will contain the following folders:

>- Cloudsdale Mobile (CDM)
>- Ignition Core (IC) -- Android speedy development framework
>- Holo Everywhere (HE) -- Creates the Holo UI on all Android platforms
>- ActionBar Sherlock (ABS) -- Allows use of the ActionBar on all Android platforms
>- Pull to Refresh (PTR) -- Creates easy views with pull to refresh logic
>- ViewPager Indicator (VPI) -- Easy ViewPager on all platforms
>- Facebook SDK -- Can be updated by opening it via Git
>- Faye Android library  -- Can be updated by opening it via Mercurial
>- Assets -- All the raw assets for the projects
>>- Gson -- JSON de/serialization module
>>- Guava -- A collection of Google APIs from GWT in Java
>>- AndroidAnnotations -- An annotation library for Android functions

## Importing into Eclipse

Go to Eclipse and click
> File -> Import

Select
> Existing Projects into Workspace

Select the directory That you pulled the repo to, this will add all the Eclipse projects into your IDE.

## Running the application

### Setting manual device selection

Click the dropdown on the run or debug button, select ```Run Configurations```, click Target tab, then select "Manual".
This will spawn a dialogue every time you launch asking which device you'd like to deploy to

### Setting up your device for debug

On your device, go to ```System settings -> Apps```
Check the box for ```Unknown Sources``` and enable ```ADB debugging```

### Debug mode

Open one of the .java files in Eclipse, click one of the launch buttons

### Deploy mode

1. Change the appropriate strings to the deploy version (To be documented later)
2. ```File -> Export```
3. ```Export Android Application```
4. Select the project (Either CDM or Holo, their dependencies will automatically be taken care of)
5. Select ```Use an existing keystore```
6. Browse to the keystore ```[PROJECT DIR]/Assets/deploy.keystore```
7. Enter the password found in ```[PROJECT DIR]/Assets/deploy_info.txt```

[[FINISH THIS LATER]]

## Commit policy

If you finish something or finish a session and your code compiles and DOESN'T ANR, push it. Else, simply commit it and push when one of the above is true.

## On the matter of TODOs

TODOs are your friend, litter them in your code files if you feel like it