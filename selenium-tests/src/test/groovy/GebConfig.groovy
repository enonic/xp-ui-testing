import org.openqa.selenium.Platform
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver

import java.util.logging.Level

// chrome driver by default
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

    def driver = new ChromeDriver();
    println "screen height is " + driver.manage().window().getSize().height;
    println "screen width is " + driver.manage().window().getSize().width;
    println "default configuration";
    return driver;
}
// Set reports directory
reportsDir = 'build/reports'
reportOnTestFailureOnly = false

environments {
    // TODO read capabilities from a property-file
    browserstack {
        //TODO change the baseUrl to actual
        baseUrl = 'http://google.com'
        driver = {
            baseUrl = 'http://google.com'
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability( "browser", "Firefox" );
            caps.setCapability( "browser_version", "44.0" );
            caps.setCapability( "os", "Windows" );
            caps.setCapability( "os_version", "7" );
            caps.setCapability( "resolution", "1024x768" );
            URL url = new URL( "http://alexrodriguez2:ZxWiqCXgYq5NJfmyy1ms@hub.browserstack.com/wd/hub" )
            WebDriver driver = new RemoteWebDriver( url, caps );
            return driver;
        }
    }
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