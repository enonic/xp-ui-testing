import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxProfile

import java.util.logging.Level

// Use firefox by default
driver = {

    def path = System.getProperty( "webdriver.chrome.driver" )
    if(path == null){
        println "specify a path to chrome webdriver:"
        Properties props = new Properties()
        File propsFile = new File( 'tests.properties' )
        props.load( propsFile.newDataInputStream() )

        def pathToDriver = props.getProperty( 'chromedriver.path' )
        System.setProperty( "webdriver.chrome.driver", pathToDriver )

    }

    def driver = new ChromeDriver()
    //def driver = new FirefoxDriver()

    //FirefoxProfile profile = new FirefoxProfile();
    //profile.setEnableNativeEvents( true );
    // def driver = new FirefoxDriver( profile );
    //driver.setLogLevel(Level.INFO)
    driver.manage().window().maximize()
    println "default configuration"
    return driver
}
// Set reports directory
reportsDir = 'build/geb-reports'
reportOnTestFailureOnly = false

environments {

    firefox {
        driver = {
            FirefoxProfile profile = new FirefoxProfile();
            profile.setEnableNativeEvents( true );
            def driver = new FirefoxDriver( profile );
            driver.setLogLevel( Level.INFO )
            driver.manage().window().maximize()
            println "firefox configuration"
            return driver
        }
    }

    chrome {
        driver = {
            def path = System.getProperty( "webdriver.chrome.driver" )
            if(path == null){
                println "specify a path to chrome webdriver"
                Properties props = new Properties()
                File propsFile = new File( 'tests.properties' )
                props.load( propsFile.newDataInputStream() )

                def pathToDriver = props.getProperty( 'chromedriver.path' )
                System.setProperty( "webdriver.chrome.driver", pathToDriver )

            }

            def driver = new ChromeDriver()
            driver.manage().window().maximize()
            println "chrome configuration"
            return driver
        }
    }


}