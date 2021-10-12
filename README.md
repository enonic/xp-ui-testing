
# Enonic XP UI Testing

 #Local mode:
1. Start the XP-server on your machine
2. Run the task: 'xp-ui-testing>gradlew deploy' ( all required applications will be put into the
   xp-deploy-folder
3. Download required chromedriver from https://chromedriver.chromium.org/downloads
4. Open the "xp-ui-testing/selenium-tests/tests.properties" and specify a path to the your local "chromedriver" 
5. Run a task with tests: 'xp-ui-testing>gradlew testInputTypesLocal'
   It also possible to run a task with tests from an Idea. Just need to open Gradle window in Idea and do double click on a task whit tests,
   testInputTypesLocal for example
   

 #Mode, when XP distro will be Downloaded from the repo and  server starts after it

 1. Run the task: 'xp-ui-testing>gradlew deploy'
 2. Clear the repo and work directories in XP_HOME
 4. Run the task: 'xp-ui-testing>gradlew testInputTypes' (distro will be downloaded from enonic repo )
  


