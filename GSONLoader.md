# How to use GSON 2.3.1

## Why is this necessary?

GSON is a library developed by Google that allows users to use JSON files within their java projects. 
GSON can serialize a java object to a JSON file, and it can deserialize a JSON file to a java object. 
We also utilize the Stripe library, which has a dependency on GSON. 
This is all fine and dandy, but there is a bug that was introduced sometime between GSON version 2.3.1 and 2.9.0 that breaks Stripe. 
So, we should simply roll back the GSON version to 2.3.1, right? 
Well, it is not that simple. 
The details are not important, but suffice it to say, IntelliJ/Maven doesn’t like it when you try to roll back libraries in this way. 
So we have had to make a workaround to load the correct version of GSON into the project structure. 
Follow the directions below to allow Stripe to function correctly. 

## Before you begin

Before taking these steps: make sure to pull from master to get GSON version 2.3.1. 

## Add the library

1. Navigate to File > Project Structure > Project Settings > Libraries.
2. Identify the library “Maven: com.google.code.gson:gson:2.9.0”. If you do not have this, go no further.
3. Press the plus (+) button and select Java. A file selection screen will appear.
4. Navigate to libraries/gson.
5. Select the files:
   * gson-2.3.1.jar (SELECT THIS ONE FIRST)
   * gson-2.3.1-javadoc.jar
   * gson-2.3.1-sources.jar
6. Press Open
7. You will be prompted with a screen that says “Library ‘gson-2.3.1’ will be added to the selected modules”. Press OK.
8. Within Libraries, there should now be a library with the name “gson-2.3.1”. Rename this to “Maven: com.google.code.gson:gson:2.3.1”.
9. Delete the existing library named “Maven: com.google.code.gson:gson:2.9.0”. Select OK. 

## Add the module

1. Navigate to File > Project Structure > Project Settings > Modules.
2. Locate the module named “gson-2.3.1”. Right click on it and select Edit.
3. Rename the module to “Maven: com.google.code.gson:gson:2.3.1”. 

If you have any issues following these steps, contact Matt Knox (mjk63@duke.edu). 