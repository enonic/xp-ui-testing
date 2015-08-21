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


    def "WHEN wizard for adding a TextLine-content (0:1) opened THEN one text input present "()
    {
        when: "start to add a content with type 'TextLine 0:1'"
        Content textLineContent = buildTextLine0_1_Content( TEST_TEXT );
        selectSiteOpenWizard( textLineContent.getContentTypeName() );
        TextLine0_1_FormViewPanel formViewPanel = new TextLine0_1_FormViewPanel( getSession() );

        then: "one text input should be displayed in the form view"
        formViewPanel.getNumberOfTextInputs() == 1;
    }

    def "WHEN wizard for adding a TextLine-content (0:1) opened THEN button 'Remove' not present on page "()
    {
        when: "start to add a content with type 'TextLine 0:1'"
        Content textLineContent = buildTextLine0_1_Content( TEST_TEXT );
        selectSiteOpenWizard( textLineContent.getContentTypeName() );
        TextLine0_1_FormViewPanel formViewPanel = new TextLine0_1_FormViewPanel( getSession() );

        then: "one text input should be displayed in the form view"
        formViewPanel.getNumberOfDisplayedRemoveButtons() == 0;
    }

    def "WHEN wizard for adding a TextLine-content (0:1) opened THEN button 'Add' not present on page "()
    {
        when: "start to add a content with type 'TextLine 0:1'"
        Content textLineContent = buildTextLine0_1_Content( TEST_TEXT );
        selectSiteOpenWizard( textLineContent.getContentTypeName() );
        TextLine0_1_FormViewPanel formViewPanel = new TextLine0_1_FormViewPanel( getSession() );

        then: "one text input should be displayed in the form view"
        !formViewPanel.isAddButtonPresent();
    }

    def "GIVEN creating new Content on root WHEN saved and wizard closed THEN new text line Content should be listed AND saved text showed when content opened for edit"()
    {
        given: "start to add a content with type 'TextLine 0:1'"
        Content textLineContent = buildTextLine0_1_Content( TEST_TEXT );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( textLineContent.getContentTypeName() );
        TextLine0_1_FormViewPanel formViewPanel = new TextLine0_1_FormViewPanel( getSession() );

        when:
        contentWizardPanel.typeData( textLineContent ).save().close( textLineContent.getDisplayName() );
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( textLineContent );

        then: "actual text in the text line should be equals as expected"
        String valueFromUI = formViewPanel.getTextLineValue();
        valueFromUI.equals( TEST_TEXT );
    }

    def "GIVEN creating new TextLine0:1 on root WHEN data typed and 'Save' and  'Publish' are pressed THEN new content with status equals 'Online' listed"()
    {
        given: "start to add a content with type 'TextLine 0:1'"
        Content textLineContent = buildTextLine0_1_Content( TEST_TEXT );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( textLineContent.getContentTypeName() );

        when: "type a data and 'save' and 'publish'"
        contentWizardPanel.typeData(
            textLineContent ).save().clickOnWizardPublishButton().clickOnPublishNowButton().waitPublishNotificationMessage(
            Application.EXPLICIT_NORMAL );
        contentWizardPanel.close( textLineContent.getDisplayName() );
        filterPanel.typeSearchText( textLineContent.getName() );

        then: "content has a 'online' status"
        contentBrowsePanel.getContentStatus( textLineContent.getName() ).equals( ContentStatus.ONLINE.getValue() )
    }

}