import org.openqa.selenium.firefox.FirefoxDriver
import geb.report.ReportState
import geb.report.Reporter
import geb.report.ReportingListener

import java.util.logging.Level

// Use firefox by default
driver = {
    def driver = new FirefoxDriver()
    driver.setLogLevel( Level.INFO )
    driver.manage().window().maximize()
    return driver
}
// Set reports directory
reportsDir = 'target/geb-reports'

reportOnTestFailureOnly = false


environments {

    firefox {
        driver = {
            def d = new FirefoxDriver()
            d.setLogLevel( Level.INFO )
            d.manage().window().maximize()
            return d
        }
    }

}