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
 * */
@Stepwise
class SiteWizard_Mixin_ImageSelector_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    def "GIVEN site wizard is opened WHEN application with 'selected-image'-mixin has been selected THEN image-selector should appear appears in the wizard"()
    {
        given: "site wizard is opened "
        SITE = buildSiteWithApps( FIRST_TEST_APP_NAME );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType(
            SITE.getContentTypeName() ).waitUntilWizardOpened();
        ImageSelectorFormViewPanel imageSelectorFormViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "application with 'select-image mixin' has been selected"
        wizard.typeData( SITE ).save();

        then: "option filter input for a selecting an image should appear in the wizard's page"
        imageSelectorFormViewPanel.isOptionFilterIsDisplayed();

        and: "'Selected Image' wizard step should be added on the toolbar"
        wizard.isWizardStepPresent( "Selected image" );
    }

    def "GIVEN existing site with mixin(image-selector) WHEN site has been opened THEN spinner disappears after a few seconds"()
    {
        given: "existing site with mixin(image-selector)"
        findAndSelectContent( SITE.getName() );

        when: "site has been opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        saveScreenshot( "site_mixin_imageselector_opened" );

        then: "wizard opened and spinner disappears after a few seconds"
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
    }

    def "GIVEN existing site with mixin(image-selector) WHEN site has been opened AND image selected AND site saved THEN image shoukld be displayed in hte wizard"()
    {
        given: "existing site with mixin(image-selector)"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        ImageSelectorFormViewPanel imageSelectorFormViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "image has been selected(in mixin)"
        PropertyTree data = new PropertyTree();
        data.addStrings( ImageSelectorFormViewPanel.IMAGES_PROPERTY, IMPORTED_IMAGE_BOOK_DISPLAY_NAME );
        imageSelectorFormViewPanel.type( data );

        and: "site has been saved"
        wizard.save();
        saveScreenshot( "site_mixin_image_selected" );

        then: "image should be present in the form"
        imageSelectorFormViewPanel.isOptionSelected();

        and: "option filter input for image-selector should not be displayed"
        !imageSelectorFormViewPanel.isOptionFilterIsDisplayed();
    }
}
