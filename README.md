
Java Rest Assured Task 

Prerequisite:
    Java JDK 11+ 
    IDE (Intellij or Eclipse)
    Maven installed 

Clone the repo
    git clone 
    cd weather-api-auto
        
Open the project in Intellij or Eclipse IDE
    export the project to Intellij or Eclipse
        
Commands for running the test
    mvn test
    mvn clean test -DsuiteXmlFile=testng.xml 
    
Running a group 
    mvn clean test -DsuiteXmlFile=testng.xml -Dgroups=warmestCity_AU


This Repository contains RestAssured Java tesk to create automated API tests for a REST API that provides weather data
    Tests Included:
        1. Current weather data for a list of multiple major international cities
        2. Identify the warmest capital city in Australia 
        3. Coldest US state



    Project Contains 
        WeatherTest class - contains the tests for fetching the data 
        ConfigReader for reading property files (url, api key)
        WeatherService class - that actually contains the Method to get the weather data from the api
        ApiHandler - that contains basic request and response specification
        supportData folder that contains the test data files - json and csv files and config.properties
        Reports - Reports are generated using extentreports and reports are generated in the reports folder
        testng.xml for running the tests. 




        



    