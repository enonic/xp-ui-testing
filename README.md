
# Enonic XP UI Testing

 #Local mode:
1. Start the XP-server on your machine
2. Perform the command: "gradlew deploy" ( all required applications will be put into the
   xp-deploy-folder
3. Open the "/selenium-tests/tests.properties" and specify a path to the  "chromedriver" on your machine
4. Perform one of the tasks which ends with *Local (testUserLocal, testInputTypesLocal
 testContentStudioLocal, testPublishMoveSortDetailsPanelLocal, testApplicationLocal, testPageEditorLocal)

 #Mode, when distro will be Downloaded from the repo and  server starts after it
 -remove the "XP_HOME" variable
 -perform one of the next tasks: testPageEditor, testApplication, testUser, testPublishMoveSortDeatailsPanel, testInputTypes
   testContentStudio.  Example: "gradlew testApplication"

# You can run separate test-file from the idea
1. Open the "tests.properties"  in the root directory and specify a path to the  "chromedriver" on your machine
2. select one of the '**Spec' files from the 'selenium-tests'- folder and select the 'Run' menu item




