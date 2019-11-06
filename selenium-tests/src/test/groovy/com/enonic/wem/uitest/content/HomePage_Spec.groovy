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

    def "WHEN home page is opened THEN dashboard toolbar is present"()
    {
        when: "home page opened"
        HomePage homePage = new HomePage( getSession() );
        saveScreenshot( "home-page-dashboard" );

        then:
        homePage.isDashboardToolbarDisplayed();

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
    def "GIVEN home page is opened WHEN 'XP Tour' has been pressed THEN modal dialog appears"()
    {
        given: "home page is opened"
        HomePage homePage = new HomePage( getSession() );

        when: "'XP Tour' pressed"
        XpTourDialog dialog = homePage.clickOnXpTourLink();
        saveScreenshot( "xp_tour_dialog" );

        then: "xp tour dialog displayed on the page"
        dialog.isOpened();

        and: "cancel button displayed"
        dialog.isCancelButtonDisplayed();

        and: "skip button should be displayed"
        dialog.isSkipButtonDisplayed();

        and: "Next button should be displayed"
        dialog.isNextButtonDisplayed();
    }

    @Ignore
    def "GIVEN Xp Tour dialog is opened WHEN cancel button clicked THEN modal dialog has been closed"()
    {
        given: " Xp Tour dialog is opened"
        HomePage homePage = new HomePage( getSession() );
        XpTourDialog dialog = homePage.clickOnXpTourLink();

        when:
        dialog.clickOnCancelButton();

        then: "modal dialog has been closed"
        !dialog.isOpened()
    }

    @Ignore
    def "GIVEN Xp Tour dialog is opened WHEN Skip button has been clicked THEN modal should be closed"()
    {
        given: "Xp Tour dialog is opened"
        HomePage homePage = new HomePage( getSession() );
        XpTourDialog dialog = homePage.clickOnXpTourLink();

        when:
        dialog.clickOnSkipTourButton();

        then: "modal dialog should be closed"
        !dialog.isOpened()
    }

    def "GIVEN home page is opened WHEN 'About' button has been pressed THEN modal dialog should appear"()
    {
        given: "home page is opened"
        HomePage homePage = new HomePage( getSession() );

        when:
        AboutDialog dialog = homePage.clickOnAboutLink();
        saveScreenshot( "about_dialog" );

        then: "About dialog should appear"
        dialog.isOpened();

        and: "cancel button should be displayed"
        dialog.isCancelButtonDisplayed();

        and: "Licensing button should be displayed"
        dialog.isLicensingButtonDisplayed();
    }

    def "GIVEN About dialog is opened WHEN 'cancel' button has been clicked THEN modal dialog should be closed"()
    {
        given: "About dialog is opened"
        HomePage homePage = new HomePage( getSession() );
        AboutDialog dialog = homePage.clickOnAboutLink();

        when: "cancel button pressed"
        dialog.clickOnCancelButton();

        then: "'About' dialog should be closed"
        !dialog.isOpened()
    }
}
