package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.context_window.ImageInspectionPanel
import com.enonic.autotests.pages.form.liveedit.ImageComponentView
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 8/16/2017.
 *
 * Tasks: xp-ui-testing#81  Add Selenium tests for ImageInspectionPanel
 * */
@Stepwise
class Image_Change_In_InspectionPanel_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE1;

    @Shared
    Content SITE2;

    @Shared
    String IMAGE_DISPLAY_NAME = "seng";


    def "GIVEN site wizard is opened WHEN new image component has been inserted THEN the image should be present in the 'selected option' on the Inspection Panel"()
    {
        given: "site wizard  is opened and the controller has been selected"
        SITE1 = buildSiteWithAllTypes( "inspect" );
        ContentWizardPanel siteWizard = contentBrowsePanel.clickToolbarNew().selectContentType( SITE1.getContentTypeName() ).typeData(
            SITE1 ).selectPageDescriptor( "main region" );
        and: "'Page Component View' has been opened"
        PageComponentsViewDialog pageComponentsView = siteWizard.showComponentView();

        when: "new image-component has been inserted"
        insertImage( pageComponentsView, siteWizard, IMAGE_DISPLAY_NAME );
        ImageInspectionPanel imageInspectionPanel = new ImageInspectionPanel( getSession() );
        saveScreenshot( "img_inspect_panel" );

        then: "the image should be present in the 'selected option' on the Inspection Panel"
        imageInspectionPanel.getSelectedImageDisplayName() == IMAGE_DISPLAY_NAME;
    }

    def "GIVEN image- component has been clicked and InspectionPanel opened WHEN remove-selected option has been clicked THEN OptionFilter input should appear on the panel"()
    {
        given: "site wizard  is opened and the controller has been selected"
        ContentWizardPanel siteWizard = findAndSelectContent( SITE1.getName() ).clickToolbarEdit();

        and: "'Page Component View' has been opened"
        PageComponentsViewDialog pageComponentsView = siteWizard.showComponentView();
        pageComponentsView.openMenu( IMAGE_DISPLAY_NAME ).selectMenuItem( "Inspect" );
        ImageInspectionPanel imageInspectionPanel = new ImageInspectionPanel( getSession() );


        when: "'remove option' button has been clicked"
        imageInspectionPanel.clickOnRemoveSelectedOptionButton();
        saveScreenshot( "img_component_removed_panel" );

        then: "OptionFilter input should appear on the panel"
        imageInspectionPanel.isOptionFilterInputDisplayed();

        and: "the removed image should not be present on the LiveEdit-page"
        siteWizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        !liveFormPanel.isImagePresent( IMAGE_DISPLAY_NAME );
    }


    private void insertImage( PageComponentsViewDialog pageComponentsView, ContentWizardPanel siteWizard, String imageDisplayName )
    {
        pageComponentsView.openMenu( "main" ).selectMenuItem( "Insert", "Image" );
        pageComponentsView.doCloseDialog();
        siteWizard.switchToLiveEditFrame();
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageFromOptions( imageDisplayName );
        siteWizard.switchToDefaultWindow();
    }
}
