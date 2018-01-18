import org.openqa.selenium.Platform
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxBinary
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver

import java.util.logging.Level

// chrome driver by default
driver = {
    def path = System.getProperty( "webdriver.chrome.driver" )

    if ( path == null )
    {
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

    ChromeOptions options = new ChromeOptions();
    options.addArguments( "--lang=en" );

    def headless = System.getProperty( "chrome.headless" )

    if ( headless != null && headless.equals( "true" ) )
    {
        options.addArguments( "--headless", "--disable-gpu", "--no-sandbox", "window-size=1920,1100" );
    }

    ChromeDriver driver = new ChromeDriver( options );
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
    browserstack_chrome {
        //TODO change the baseUrl to the actual URL,
        baseUrl = 'http://google.com/'
        driver = {
            baseUrl = 'http://google.com'
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability( "browser", "Firefox" );
            caps.setCapability( "browser_version", "48.0.2" );
            caps.setCapability( "os", "Windows" );
            caps.setCapability( "os_version", "7" );
            caps.setCapability( "resolution", "1024x768" );
            URL url = new URL( "http://alexrodriguez2:ZxWiqCXgYq5NJfmyy1ms@hub.browserstack.com/wd/hub" )
            WebDriver driver = new RemoteWebDriver( url, caps );
            return driver;
        }
    }
    browserstack_ff {
        //TODO change the baseUrl to the actual URL,
        baseUrl = 'http://google.com/'
        driver = {
            baseUrl = 'http://google.com'
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability( "browser", "Chrome" );
            caps.setCapability( "browser_version", "49.0" );
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
            println "Firefox Configuration!"
            Properties props = new Properties()
            File propsFile = new File( 'tests.properties' )
            props.load( propsFile.newDataInputStream() )
            def pathToDriver;
            def headless = System.getProperty( "firefox.headless" )
            FirefoxOptions options = new FirefoxOptions();


            if ( Platform.current.is( Platform.WINDOWS ) )
            {
                pathToDriver = props.getProperty( 'windows.gecko.path' )
            }

            else if ( Platform.current.is( Platform.LINUX ) )
            {
                pathToDriver = props.getProperty( 'linux.gecko.path' )
            }
            else if ( Platform.current.is( Platform.MAC ) )
            {
                pathToDriver = props.getProperty( 'mac.gecko.path' )
            }
            else
            {
                throw new RuntimeException( "Unsupported operating system [${Platform.current}]" )
            }
            System.setProperty( "webdriver.gecko.driver", pathToDriver );
//            FirefoxProfile profile = new FirefoxProfile();
//            DesiredCapabilities capabilities = new DesiredCapabilities();
//            capabilities.setCapability( FirefoxDriver.PROFILE, profile );
//            capabilities.setCapability( "marionette", true );
//            FirefoxDriver driver = new FirefoxDriver( capabilities );

            FirefoxBinary firefoxBinary = new FirefoxBinary();
            if ( headless != null && headless.equals( "true" ) )
            {
                firefoxBinary.addCommandLineOptions( "--headless" );
                firefoxBinary.addCommandLineOptions( "--disable-gpu" );
                firefoxBinary.addCommandLineOptions( "window-size=1920,1100" );

            }

            System.setProperty( "webdriver.gecko.driver", pathToDriver );
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setBinary( firefoxBinary );
            FirefoxDriver driver = new FirefoxDriver( firefoxOptions );

            driver.setLogLevel( Level.SEVERE )
            driver.manage().window().maximize()
            println "The end of firefox configuration";
            return driver
        }
    }
}