import org.openqa.selenium.Platform
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxProfile

import java.util.logging.Level

// Use firefox by default
driver = {

    def path = System.getProperty( "webdriver.chrome.driver" )

    if ( path == null )
    {
        println "specify a path to chrome webdriver:"
        Properties props = new Properties()
        File propsFile = new File( 'tests.properties' )
        props.load( propsFile.newDataInputStream() )


        def pathToDriver;
        if ( Platform.current.is( Platform.WINDOWS ) )
        {
            pathToDriver = props.getProperty( 'windows.chromedriver.path' )
        }

        else if ( Platform.current.is( Platform.LINUX ) )
        {
            pathToDriver = props.getProperty( 'linux.chromedriver.path' )
        }
        else if ( Platform.current.is( Platform.MAC ) )
        {
            pathToDriver = props.getProperty( 'mac.chromedriver.path' )
        }
        else
        {
            throw new RuntimeException( "Unsupported operating system [${Platform.current}]" )
        }
        System.setProperty( "webdriver.chrome.driver", pathToDriver )

    }

    //def driver = new ChromeDriver();
    ChromeOptions options = new ChromeOptions();
    options.addArguments( "start-maximized" );
    def driver = new ChromeDriver( options )

    //FirefoxProfile profile = new FirefoxProfile();
    //profile.setEnableNativeEvents( true );
    // def driver = new FirefoxDriver( profile );
    // def driver = new FirefoxDriver();
    driver.manage().window().maximize()
    println "default configuration"
    return driver
}
// Set reports directory
reportsDir = 'build/reports'
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
            if ( path == null )
            {
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