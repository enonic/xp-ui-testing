package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TextLine0_1_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Occurrences_TextLine_0_1_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    String TEST_TEXT = "test text 0:1";


    def "WHEN wizard for adding a TextLine-content (0:1) is opened THEN one 'text input' should be present"()
    {
        when: "wizard for adding a TextLine-content (0:1) is opened"
        Content textLineContent = buildTextLine0_1_Content( TEST_TEXT );
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine0_1_FormViewPanel formViewPanel = new TextLine0_1_FormViewPanel( getSession() );

        and: ""
        wizard.typeDisplayName( textLineContent.getDisplayName() );

        then: "one text input should be displayed on the form view"
        formViewPanel.getNumberOfTextInputs() == 1;

        and: "button 'Add' should not be present on the page"
        !formViewPanel.isAddButtonPresent();

        and: "red icon should not be displayed, because the text input is not required"
        !wizard.isContentInvalid();

        and: "button 'Remove' should not present on the page "
        formViewPanel.getNumberOfDisplayedRemoveButtons() == 0;
    }

    def "GIVEN wizard for adding a TextLine-content (0:1) is opened WHEN data was saved and wizard closed THEN content should be listed AND saved text showed when the content is opened"()
    {
        given: "wizard for adding a TextLine-content (0:1) is opened"
        Content textLineContent = buildTextLine0_1_Content( TEST_TEXT );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine0_1_FormViewPanel formViewPanel = new TextLine0_1_FormViewPanel( getSession() );

        when: "all data has been typed and saved"
        contentWizardPanel.typeData( textLineContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( textLineContent );

        then: "actual text in the text line should be equals with the expected"
        String valueFromUI = formViewPanel.getTextLineValue();
        valueFromUI.equals( TEST_TEXT );
    }

    def "GIVEN creating new TextLine0:1 on root WHEN data typed and 'Save' and  'Publish' are pressed THEN new content with status equals 'Online' listed"()
    {
        given: "wizard for adding a TextLine-content (0:1) is opened"
        Content textLineContent = buildTextLine0_1_Content( TEST_TEXT );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( textLineContent.getContentTypeName() );

        when: "all data has been typed and the content published"
        contentWizardPanel.typeData( textLineContent ).save().clickOnWizardPublishButton().clickOnPublishNowButton();
        contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        contentWizardPanel.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( textLineContent.getName() );

        then: "the content of the status should be 'Online'"
        contentBrowsePanel.getContentStatus( textLineContent.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() )
    }
}