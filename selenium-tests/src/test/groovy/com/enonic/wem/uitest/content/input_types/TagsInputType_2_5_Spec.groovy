package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TagFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content

class TagsInputType_2_5_Spec
    extends Base_InputFields_Occurrences

{
    def "GIVEN new wizard for Tag-content 2:5 is opened WHEN two tags has been added AND the content has been published THEN that content should be 'PUBLISHED'"()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 2 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );

        when: "type a data and 'save' and 'publish'"
        contentWizardPanel.typeData( tagContent ).clickOnMarkAsReadyAndDoPublish();
        contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        contentWizardPanel.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( tagContent.getName() );

        then: "content has a 'Published' status"
        contentBrowsePanel.getContentStatus( tagContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() )
    }

    def "GIVEN new wizard for Tag-content 2:5 is opened WHEN one tag has been added AND 'Publish-menu' has been opened THEN 'Publish menu item should be disabled AND content is not valid'"()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 1 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );

        when: "data typed and number of tags less, than required"
        contentWizardPanel.typeData( tagContent );

        then: "'Publish' menu item should be disabled, because 2 tags are required!"
        !contentWizardPanel.showPublishMenu().isPublishMenuItemEnabled();

        and: "content should be invalid, because one tag is added"
        contentWizardPanel.isContentInvalid();
    }

    def "GIVEN new wizard for Tag-content 2:5 is opened WHEN five tags has been added THEN tags-input becomes hidden and impossible to add one more tag"()
    {
        given: "new wizard is opened:"
        Content tagContent = buildTag_2_5_Content( 5 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );
        boolean isDisplayedBefore = formViewPanel.isTagsInputDisplayed()

        when: "five tags has been added:"
        contentWizardPanel.typeData( tagContent )

        then: "input text becomes disabled and impossible to add one more tag"
        !formViewPanel.isTagsInputDisplayed() && isDisplayedBefore;
    }

    def "GIVEN five tags has been added WHEN one of the tags has been removed THEN input text gets enabled again"()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 5 );
        selectSitePressNew( tagContent.getContentTypeName() ).typeData( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );
        boolean isDisplayedBefore = formViewPanel.isTagsInputDisplayed()

        when: "one of the fives tags has been removed"
        formViewPanel.removeLastTag();

        then: "input text becomes enabled again"
        formViewPanel.isTagsInputDisplayed() && !isDisplayedBefore;
    }

    def "GIVEN new wizard for Tag-content 2:5 is opened WHEN two tags has been added  'Saved' AND 'Published'  THEN two tags with expected text should be present"()
    {
        given: "new wizard for Tag-content 2:5 is opened"
        Content tagContent = buildTag_2_5_Content( 2 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );

        when: "type a data and 'save' and open for edit new created content"
        contentWizardPanel.typeData( tagContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );

        then: "two tags with expected text should be present"
        formViewPanel.getNumberOfTags() == 2;
        and:
        String[] tags = [TAG_1, TAG_2];
        List<String> fromUI = formViewPanel.getTagsText();
        fromUI.containsAll( tags.toList() );
    }

    def "GIVEN new wizard for Tag-content 2:5 is opened WHEN five tags has been added and 'Save' button pressed THEN five Tags with expected text should be present"()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 5 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );

        when: "type a data and 'save' and open for edit new created content"
        contentWizardPanel.typeData( tagContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );

        then: "five Tags with expected name are present on the wizard page"
        formViewPanel.getNumberOfTags() == 5;

        and:
        String[] tags = [TAG_1, TAG_2, TAG_3, TAG_4, TAG_5];
        formViewPanel.getTagsText().containsAll( tags.toList() );
    }
}