package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ImageComponentView
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

/**
 * Created on 2/2/2017.
 *
 * Tasks:
 * XP-4945 Add ui-test to verify the XP-4944
 * xp-ui-testing#66 Add selenium tests for expanding of a tree in the ImageSelector drop-down list
 * Verifies:
 * XP-4944 ImageComponentView - NullPointerException and Upload does not add new file
 * (NullPointerException thrown when dropdown Handler was clicked  when dropdown Handler was clicked)
 * */
class ImageComponentView_Spec
    extends BaseContentSpec
{
    @Shared
    String SITE_NAME;

    def "GIVEN new site has been added and it opened WHEN  Image-component was inserted THEN Image Component View should be displayed on the page editor"()
    {
        given: "new site has been added"
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

        when: "drop-down handler was clicked"
        imageComponentView.clickOnTheDropDownHandler();

        then: "list of available options should be displayed"
        imageComponentView.getDisplayedOptions().size() > 0

        and: "error messages should not be displayed in the component"
        !imageComponentView.isErrorMessageDisplayed();
    }

    //verifies the XP-4944 -NullPointerException thrown when dropdownHandler was clicked
    //xp-ui-testing#66 Add selenium tests for expanding of a tree in the ImageSelector drop-down list
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

        when: "dropdown handler has been clicked and the folder with images has been expanded"
        imageComponentView.clickOnTheDropDownHandler().clickOnExpanderInDropDownList( "imagearchive" );
        saveScreenshot( "img_comp_view_dropdown_handler" );

        and: "image has been selected from the list of options"
        imageComponentView.clickOnOption( "geek" );
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        wizard.switchToLiveEditFrame();

        then: "new image should be added on the page"
        LinkedList<String> images = liveFormPanel.getImageNames();
        images.contains( "geek.png" )
    }
}
