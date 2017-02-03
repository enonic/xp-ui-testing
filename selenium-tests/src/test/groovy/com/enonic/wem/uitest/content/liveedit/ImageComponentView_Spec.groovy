package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ImageComponentView
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Ignore
import spock.lang.Shared

/**
 * Created on 2/2/2017.
 *
 * Tasks:
 * XP-4945 Add ui-test to verify the XP-4944
 * Verifies:
 * XP-4944 ImageComponentView - NullPointerException and Upload does not add new file
 * */
class ImageComponentView_Spec
    extends BaseContentSpec
{
    @Shared
    String SITE_NAME;

    def "GIVEN existing site is opened  WHEN  Image-component was inserted THEN Image Component View should be displayed on the page editor"()
    {
        given: "adding of new site "
        SITE_NAME = NameHelper.uniqueName( "site" );
        addSiteWithAllInputTypes( SITE_NAME );

        and: "the site is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE_NAME ).clickToolbarEdit();

        when: "image component has been inserted"
        PageComponentsViewDialog pageComponentsViewDialog = wizard.selectPageDescriptor( "Page" ).save().showComponentView();
        pageComponentsViewDialog.openMenu( "main" ).selectMenuItem( "Insert", "Image" );
        pageComponentsViewDialog.doCloseDialog();
        wizard.switchToLiveEditFrame();
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );

        then: "the component should be displayed on the page"
        imageComponentView.isDropDownHandlerDisplayed();

        and: "'Upload' button should be displayed"
        imageComponentView.isUploadButtonDisplayed();
    }
    //verifies XP-4944 ImageComponentView - NullPointerException and Upload does not add new file
    def "GIVEN existing site with a controller is opened AND image component has been inserted WHEN dropdown handler was clicked THEN list of available options should be displayed"()
    {
        given: "existing site is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE_NAME ).clickToolbarEdit();
        and: "image component has been inserted"
        PageComponentsViewDialog pageComponentsViewDialog = wizard.showComponentView();
        pageComponentsViewDialog.openMenu( "main" ).selectMenuItem( "Insert", "Image" );
        pageComponentsViewDialog.doCloseDialog();
        wizard.switchToLiveEditFrame();
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );

        when: "dropdown handler was clicked"
        imageComponentView.clickOnTheDropDownHandler();

        then: "list of available options should be displayed"
        imageComponentView.getDisplayedOptions().size() > 0

        and: "error messages should not be displayed in the component"
        !imageComponentView.isErrorMessageDisplayed();
    }

    //TODO remove it when XP-4944 will be fixed
    @Ignore
    def "GIVEN existing site with a controller is opened WHEN Image component was inserted AND an image has been selected from the list of options THEN "()
    {
        given: "existing site is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE_NAME ).clickToolbarEdit();
        and: "image component has been inserted"
        PageComponentsViewDialog pageComponentsViewDialog = wizard.showComponentView();
        pageComponentsViewDialog.openMenu( "main" ).selectMenuItem( "Insert", "Image" );
        pageComponentsViewDialog.doCloseDialog();
        wizard.switchToLiveEditFrame();
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );

        when: "dropdown handler was clicked"
        imageComponentView.clickOnTheDropDownHandler();

        and: "image has been selected from the list of options"
        imageComponentView.clickOnOption( IMPORTED_IMAGE_BOOK_DISPLAY_NAME );
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        wizard.switchToLiveEditFrame();

        then: "new image should be added on the page"
        LinkedList<String> images = liveFormPanel.getImageNames();
        images.contains( IMPORTED_IMAGE_BOOK_DISPLAY_NAME )
    }
}
