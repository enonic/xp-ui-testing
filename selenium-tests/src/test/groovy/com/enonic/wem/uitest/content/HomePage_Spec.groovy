package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.AboutDialog
import com.enonic.autotests.pages.HomePage
import com.enonic.autotests.pages.XpTourDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 13.09.2016.*/
@Stepwise
class HomePage_Spec
    extends BaseGebSpec
{
    @Shared
    ContentBrowsePanel contentBrowsePanel;

    def setup()
    {
        go "admin"
        NavigatorHelper.loginAndOpenHomePage( getTestSession() );
        NavigatorHelper.closeXpTourDialogIfPresent( getSession() );
    }

    def "WHEN home page opened THEN dashboard toolbar is present"()
    {
        when: "home page opened"
        HomePage homePage = new HomePage( getSession() );
        saveScreenshot( "home-page-dashboard" );

        then:
        homePage.isDashboardToolbarDisplayed();

        //and: "'Docs' link present"
        //homePage.isDocs_Displayed();

        and: "'Discuss' link present"
        homePage.isDiscus_Displayed();

        and: "'Market' link present"
        homePage.isMarket_Displayed()

        and: "'About' link present"
        homePage.isAbout_Displayed();
        and: "'XP Tour' link should not be present, because 'tourDisabled = true' is configured"
        !homePage.isXP_Tour_Displayed();
    }

    @Ignore
    def "GIVEN home page opened WHEN 'XP Tour' pressed THEN modal dialog appears"()
    {
        given: "home page opened"
        HomePage homePage = new HomePage( getSession() );

        when: "'XP Tour' pressed"
        XpTourDialog dialog = homePage.clickOnXpTourLink();
        saveScreenshot( "xp_tour_dialog" );

        then: "xp tour dialog displayed on the page"
        dialog.isOpened();

        and: "cancel button displayed"
        dialog.isCancelButtonDisplayed();

        and: "skip button displayed"
        dialog.isSkipButtonDisplayed();

        and: "Next button displayed"
        dialog.isNextButtonDisplayed();
    }

    @Ignore
    def "GIVEN Xp Tour dialog opened WHEN cancel button clicked THEN modal dialog has been closed"()
    {
        given: " Xp Tour dialog opened"
        HomePage homePage = new HomePage( getSession() );
        XpTourDialog dialog = homePage.clickOnXpTourLink();

        when:
        dialog.clickOnCancelButton();

        then: "modal dialog has been closed"
        !dialog.isOpened()
    }

    @Ignore
    def "GIVEN Xp Tour dialog opened WHEN Skip button clicked THEN modal dialog has been closed"()
    {
        given: "Xp Tour dialog opened"
        HomePage homePage = new HomePage( getSession() );
        XpTourDialog dialog = homePage.clickOnXpTourLink();

        when:
        dialog.clickOnSkipTourButton();

        then: "modal dialog has been closed"
        !dialog.isOpened()
    }

    def "GIVEN home page opened WHEN 'About' pressed THEN modal dialog appears"()
    {
        given: "home page opened"
        HomePage homePage = new HomePage( getSession() );

        when:
        AboutDialog dialog = homePage.clickOnAboutLink();
        saveScreenshot( "about_dialog" );

        then: "About dialog displayed on the page"
        dialog.isOpened();

        and: "cancel button displayed"
        dialog.isCancelButtonDisplayed();

        and: "Downloads link displayed"
        dialog.isDownloadsLinkDisplayed();

        and: "Source Code link displayed"
        dialog.isSourceCodeLinkDisplayed();

        and: "Licensing link displayed"
        dialog.isLicensingLinkDisplayed();
    }

    def "GIVEN About dialog opened WHEN cancel button clicked THEN modal dialog has been closed"()
    {
        given: "About dialog opened"
        HomePage homePage = new HomePage( getSession() );
        AboutDialog dialog = homePage.clickOnAboutLink();

        when: "cancel button pressed"
        dialog.clickOnCancelButton();

        then: "'About' dialog has been closed"
        !dialog.isOpened()
    }
}
