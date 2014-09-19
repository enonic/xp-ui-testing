import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver

import java.util.logging.Level

// Use firefox by default
driver = {

    Properties props = new Properties()
    File propsFile = new File( 'tests.properties' )
    props.load( propsFile.newDataInputStream() )

    def pathToDriver = props.getProperty( 'chromedriver.path' )
    System.setProperty( "webdriver.chrome.driver", pathToDriver )

    def driver = new ChromeDriver()
    //driver.setLogLevel(Level.INFO)
    driver.manage().window().maximize()
    println "default configuration"
    return driver
}
// Set reports directory
reportsDir = 'target/geb-reports'
reportOnTestFailureOnly = false

environments {

    remote {
        driver = {
            def driver = new RemoteWebDriver( new URL( "http://10.10.9.152:4444/wd/hub" ), DesiredCapabilities.firefox() )
            driver.setLogLevel( Level.INFO );
            driver.manage().window().maximize();
            return driver;
        }
    }


}