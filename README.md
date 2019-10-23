# Passenger Security - Online FIR Filing System
This is project of Smart India Hackathon 2018 - Software Edition and our Third Year DBMS Mini Project.

## Introduction
Source [Smart India Hackathon 2018](https://innovate.mygov.in/sih_ps/passenger-security/)

Security of passengers and their belongings is of paramount importance for Indian Railways. In case of any untoward incident or incident of crime against passengers and their belongings matter can be reported to Government Railway Police at Railway stations and on trains and also through given security helpline telephone number 182. A software solution needs to be provided for online registration of FIR over entire network of Indian Railway for speedy initiation of action by GRP.

## Modules
- ***Passenger :*** Native android app is built for passenger. Passenger needs to authenticate first and then he/she can File FIR, See the status of FIR or Turn on Panic mode to report danger situation to Government Railway Police.

- ***GRP (Government Railway Police) :*** This is dashboard for GRP. GRP can see list of registered FIRs, can take action on FIRs, can see Alert details.

## Technologies Used
- Front End: Android-Java, HTML, CSS, Bootstrap, JavaScript.
- Back End: PHP-XAMPP.
- Database: MySQL, Firebase Realtime database.

## Directories
- [`Android`](Android/) : Android app for passenger. Built using Android Studio 3.5.1
- [`Server`](Server/) : HTML and PHP files for GRP Dashboard.
- [`Database`](Database/): Backup `.sql` file.


## How to Setup Project on your system?

### Backend Setup:
- Install XAMPP Server.
- Copy `Server` Files in `htdocs` folder of XAMPP.
- Import `railway.sql` MySQL backup file in your database.
- Edit `Config.php` as your database credentials.
- Go to [Firebase Console](https://console.firebase.google.com) and setup android project (Don't forgot to add `SHA` key). Download `google-services.json` file.
- Edit `AlertDetails.php` file, Modify Firebase database path URL on 6th Line.

### Android Setup:
- Import android project files in Android Studio.
- Copy `google-services.json` file which downloaded in previous step in `/app` directory.
- Update End point URLs (Only IP Address) in `strings.xml` of Android app.

After this, you're done with setup. Just run the app!


