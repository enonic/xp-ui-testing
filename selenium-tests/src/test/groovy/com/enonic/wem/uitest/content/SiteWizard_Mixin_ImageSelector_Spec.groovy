package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ImageSelectorFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 18.10.2016.
 * Task: XP-4295 Add selenium tests to verify XP-4278
 * Verifies 'XP-4278 A mixin with image selector crashes the Content Wizard"
 *
 * */
@Stepwise
class SiteWizard_Mixin_ImageSelector_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    def "GIVEN creating a site WHEN application with 'selected-image'-mixin selected THEN image selector appears on the page"()
    {
        given: "creating a site"
        SITE = buildSiteWithApps( FIRST_TEST_APP_NAME );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType(
            SITE.getContentTypeName() ).waitUntilWizardOpened();
        ImageSelectorFormViewPanel imageSelectorFormViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "application with 'select-image mixin' selected"
        wizard.typeData( SITE ).save();

        then: "option filter input for a selecting an image appears on the wizard's page"
        imageSelectorFormViewPanel.isOptionFilterIsDisplayed();

        and: "'Selected Image' wizard step is present"
        wizard.isWizardStepPresent( "Selected image" );
    }
    //verifies: XP-4278 A mixin with image selector crashes the Content Wizard
    def "GIVEN existing site with mixin(image-selector) WHEN site opened THEN spinner disappears after a few seconds "()
    {
        given: "existing site with mixin(image-selector)"
        findAndSelectContent( SITE.getName() );

        when: "site opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        saveScreenshot( "site_mixin_imageselector_opened" );

        then: "wizard opened and spinner disappears after a few seconds"
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
    }

    def "GIVEN existing site with mixin(image-selector) WHEN site opened AND image selected AND site saved THEN image is displayed on te page"()
    {
        given: "existing site with mixin(image-selector)"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        ImageSelectorFormViewPanel imageSelectorFormViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "image selected"
        PropertyTree data = new PropertyTree();
        data.addStrings( ImageSelectorFormViewPanel.IMAGES_PROPERTY, IMPORTED_BOOK_IMAGE );
        imageSelectorFormViewPanel.type( data );

        and: "site saved"
        wizard.save();
        saveScreenshot( "site_mixin_image_selected" );

        then: "image is displayed on the page"
        imageSelectorFormViewPanel.isOptionSelected();

        and: "option filter input for image-selector not displayed"
        !imageSelectorFormViewPanel.isOptionFilterIsDisplayed();
    }
}
