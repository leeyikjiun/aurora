# aurora
A collection of high performance algorithms &amp; data structures.

Setup:
* Update your Java to at least Version 1.7 (Check via ```java -version```)
* Install Maven (For Macs with Homebrew, use ```brew install maven```)
* Add ```export JAVA_HOME=$(/usr/libexec/java_home -v 1.7)``` to the end of your ```.bash_profile```
* Run ```. .bash_profile``` to refresh your settings, or simply restart your terminal
* Change directory to ```/aurora```
* ```mvn clean install```

Standard workflow:
* Implement some stuff
* Write your own tests or use existing ones, if they are relevant
* Make sure all tests pass via ```mvn test```
* Commit and push!
