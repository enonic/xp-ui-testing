import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver

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

    firefox {
        driver = {
            def driver = new FirefoxDriver()
            driver.setLogLevel( Level.INFO )
            driver.manage().window().maximize()
            println "default configuration"
            return driver
        }
    }


}