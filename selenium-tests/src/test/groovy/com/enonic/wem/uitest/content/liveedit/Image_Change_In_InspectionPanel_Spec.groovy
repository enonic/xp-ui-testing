package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.context_window.ImageInspectionPanel
import com.enonic.autotests.pages.form.liveedit.ImageComponentView
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

/**
 * Created on 8/16/2017.*/
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
        SITE1 = buildSiteBasedOnFirstApp();
        ContentWizardPanel siteWizard = contentBrowsePanel.clickToolbarNew().selectContentType( SITE1.getContentTypeName() ).typeData(
            SITE1 ).selectPageDescriptor( "main region" ).save();
        and: "'Page Component View' has been opened"
        PageComponentsViewDialog pageComponentsView = siteWizard.showComponentView();

        when: "new image-component has been inserted"
        insertImage( pageComponentsView, siteWizard, IMAGE_DISPLAY_NAME );
        ImageInspectionPanel imageInspectionPanel = new ImageInspectionPanel( getSession() );

        then: "the image should be present in the 'selected option' on the Inspection Panel"
        imageInspectionPanel.getSelectedImageDisplayName() == IMAGE_DISPLAY_NAME;
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
