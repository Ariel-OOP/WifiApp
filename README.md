# WifiApp
A wifi app that can calculate many different calculations

# General Description <br />
This project is written in java. The program is divided into two parts:<br />
<br />
**The First part,** receives a CSV type file that was exported from the Wigle app :iphone:. The file contains WIFI points that were sampled and identify each point (For example: time, location, MAC address, signal strength and ect.) and sorts each time period and location its points by its strength :signal_strength: and takes the 10 strongest. At the end of the program it exports a new CSV file which each a row shows at the most 10 WIFI points with the following parameters SSID,BSSID, Frequncy, Signal. Each row is the same time and location.

**The Second part,** recevies the file that was exported from the first part (or similiar file) and filters it using three criteria: time,location and ID. <br />

1.  Time, the program asks the user to enter two dates, a start date/time and a end date/time. The program filters
the WIFI points in that period of time from the CSV file.
2.  Location, the program asks the user to enter coordinates (Lat,Lon) and radius. The program filters the WIFI points in
the given radius of the coordinates from the CSV file.
3. ID, the program asks the user to enter the manufacturer model and will give WIFI points from the corresponding deice.
<br />
After the filteration is complete the program will export a KML file (it is similar to an XML file) with all the related WIFI points. The KML file can be opened using Google Earth.

### The program uses following api/libraries: ###
  1. Commons CSV 1.5 api
  2. Jak kml 2.2 api
  3. Junit 4 

# Instructions <br />

1. Clone repository and add the jars in the resources folder to libary path.
2. Run the Main.java in the Application/src folder.
3. You can add your own CSV file from the wigle app to the FileResources folder:open_file_folder::.
4. Enjoy!!!

# The name web-flow is GeekCSA

# ![Alt text](https://github.com/Ariel-OOP/WifiApp/blob/master/Diagram_of_classes_after_teacher.png?raw=true "Title")
