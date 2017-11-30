package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TagFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content

class TagsInputType_2_5_Spec
    extends Base_InputFields_Occurrences

{

    def "GIVEN wizard for adding a Tag-content (2:5) opened WHEN no one tag added and  'Save' and 'Publish' buttons pressed THEN new content with status 'online' appears "()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 2 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );

        when: "type a data and 'save' and 'publish'"
        contentWizardPanel.typeData( tagContent ).save().clickOnWizardPublishButton().clickOnPublishNowButton();
        contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        contentWizardPanel.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( tagContent.getName() );

        then: "content has a 'Published' status"
        contentBrowsePanel.getContentStatus( tagContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() )
    }

    def "GIVEN creating new Tag-content 2:5 on root WHEN only one tag added and button 'Publish' pressed THEN 'Publish button is disabled and content is invalid'"()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 1 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );

        when: "data typed and number of tags less, than required"
        contentWizardPanel.typeData( tagContent );

        then: "'Publish' button disabled"
        !contentWizardPanel.isPublishButtonEnabled();

        //TODO add test check for validation in the wizard( when the feature will be implemented)
        //and: "content is invalid"
        //contentWizardPanel.isContentInvalid( tagContent.getDisplayName() );
    }

    def "GIVEN wizard for adding a Tag-content (2:5) opened WHEN five tags added  THEN input text becomes disabled and impossible to add one more tag"()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 5 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );
        boolean isDisplayedBefore = formViewPanel.isTagsInputDisplayed()

        when: "five tags added, input text becomes disabled(display: none)"
        contentWizardPanel.typeData( tagContent )

        then: "input text becomes disabled and impossible to add one more tag"
        !formViewPanel.isTagsInputDisplayed() && isDisplayedBefore;
    }

    def "GIVEN five tags added in input is disabled WHEN one of the fives tags removed THEN input text becomes enabled again"()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 5 );
        selectSitePressNew( tagContent.getContentTypeName() ).typeData( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );
        boolean isDisplayedBefore = formViewPanel.isTagsInputDisplayed()

        when: "one of the fives tags removed"
        formViewPanel.removeLastTag();

        then: "input text becomes enabled again"
        formViewPanel.isTagsInputDisplayed() && !isDisplayedBefore;
    }


    def "GIVEN creating new Tag-content 2:5 on root WHEN two tags added and button 'Save' and 'Publish' pressed  and just created content opened THEN two tags with correct name are present"()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 2 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );

        when: "type a data and 'save' and open for edit new created content"
        contentWizardPanel.typeData( tagContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );

        then: "two tags with correct name are present"
        formViewPanel.getNumberOfTags() == 2;
        and:
        String[] tags = [TAG_1, TAG_2];
        List<String> fromUI = formViewPanel.getTagsText();
        fromUI.containsAll( tags.toList() );

    }

    def "GIVEN wizard for adding a Tag-content (2:5) opened WHEN five tags added and 'Save' button pressed and just created content opened THEN five Tags with correct name are present in the wizard page"()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 5 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );

        when: "type a data and 'save' and open for edit new created content"
        contentWizardPanel.typeData( tagContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );

        then: "five Tags with correct name are present on the wizard page"
        formViewPanel.getNumberOfTags() == 5;

        and:
        String[] tags = [TAG_1, TAG_2, TAG_3, TAG_4, TAG_5];
        formViewPanel.getTagsText().containsAll( tags.toList() );
    }

}