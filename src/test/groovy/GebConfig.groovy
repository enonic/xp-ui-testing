import java.util.logging.*

import org.openqa.selenium.firefox.FirefoxDriver
// Use firefox by default
driver = {
	def driver = new FirefoxDriver()
	driver.setLogLevel(Level.INFO)
	driver.manage().window().maximize()
	return driver
}
// Set reports directory
reportsDir = 'target/geb-reports'

reportOnTestFailureOnly = false


environments {

	// run as “ -Dgeb.env=chrome test-app”

	// run as “ -Dgeb.env=firefox test-app”
	firefox {
		driver = {
			def d = new FirefoxDriver()
			d.setLogLevel(Level.INFO)
			d.manage().window().maximize()
			return d
		}
	}

}