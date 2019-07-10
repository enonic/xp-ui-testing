package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.exceptions.TestFrameworkException
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TagFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree

class TagsInputType_0_5_Spec
    extends Base_InputFields_Occurrences

{

    def "GIVEN wizard for adding a Tag-content (0:5) opened WHEN no one tag added and  'Save' and 'Publish' buttons pressed THEN new content with status 'online' appears "()
    {
        given: "start to add a content with type 'Tag 0:5'"
        Content tagContent = buildTag_0_5_Content( 0 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );

        when: "type a data and 'save' and 'publish'"
        contentWizardPanel.typeData( tagContent ).save().clickOnWizardPublishButton().clickOnPublishButton();
        contentWizardPanel.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( tagContent.getName() );

        then: "content has a 'Published' status"
        contentBrowsePanel.getContentStatus( tagContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() )
    }


    def "GIVEN wizard for adding a Tag-content (0:5) opened WHEN one tag added and 'Save' button pressed and just created content opened THEN only one Tag with correct name present on wizard "()
    {
        given: "start to add a content with type 'Tag 0:5'"
        Content tagContent = buildTag_0_5_Content( 1 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );


        when: "type a data and 'save' and open for edit new created content"
        contentWizardPanel.typeData( tagContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );

        then: "one tag with correct text present on the page"
        formViewPanel.getNumberOfTags() == 1;
        and:
        formViewPanel.getTagsText().contains( TAG_1 );
    }

    def "GIVEN wizard for adding a Tag-content (0:5) opened WHEN five tags added  THEN input text becomes disabled and impossible to add one more tag"()
    {
        given: "start to add a content with type 'Tag 0:5'"
        Content tagContent = buildTag_0_5_Content( 5 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );
        boolean isDisplayedBefore = formViewPanel.isTagsInputDisplayed()

        when: "five tags added, input text becomes disabled(display: none)"
        contentWizardPanel.typeData( tagContent );

        then: "input text becomes disabled and impossible to add one more tag"
        !formViewPanel.isTagsInputDisplayed() && isDisplayedBefore;
    }

    def "GIVEN five tags added in input is disabled WHEN one of the fives tags removed  THEN input text becomes enabled again"()
    {
        given: "start to add a content with type 'Tag 0:5'"
        Content tagContent = buildTag_0_5_Content( 5 );
        selectSitePressNew( tagContent.getContentTypeName() ).typeData( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );
        boolean isDisplayedBefore = formViewPanel.isTagsInputDisplayed()

        when: "one of the fives tags has been removed"
        formViewPanel.removeLastTag();

        then: "input text becomes enabled again"
        formViewPanel.isTagsInputDisplayed() && !isDisplayedBefore;
    }

    def "GIVEN wizard for adding a Tag-content (0:5) opened WHEN five tags added and 'Save' button pressed and just created content opened THEN five Tags with correct name are present in the wizard page"()
    {
        given: "start to add a content with type 'Tag 0:5'"
        Content tagContent = buildTag_0_5_Content( 5 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );


        when: "type a data and 'save' and open for edit new created content"
        contentWizardPanel.typeData( tagContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );

        then: "five Tags with correct name are present in the wizard page"
        formViewPanel.getNumberOfTags() == 5;
        and:
        String[] tags = [TAG_1, TAG_2, TAG_3, TAG_4, TAG_5];
        formViewPanel.getTagsText().containsAll( tags.toList() );
    }


    private PropertyTree buildData( int numberOfTags )
    {
        PropertyTree data = new PropertyTree();
        data.setLong( "min", 0 );
        data.setLong( "max", 5 );
        switch ( numberOfTags )
        {
            case 0:
                break;
            case 1:
                data.addString( "tags", TAG_1 );
                break;
            case 2:
                data.addString( "tags", TAG_1 );
                data.addString( "tags", TAG_2 );
                break;
            case 5:
                data.addString( "tags", TAG_1 );
                data.addString( "tags", TAG_2 );
                data.addString( "tags", TAG_3 );
                data.addString( "tags", TAG_4 );
                data.addString( "tags", TAG_5 );
                break;
            default:
                throw new TestFrameworkException( "data not implemented" );


        }
        return data;
    }

    private Content buildTag_0_5_Content( int numberOfTags )
    {
        PropertyTree data = buildData( numberOfTags );
        String name = "tag0_5";
        Content tagContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "tag0_5 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "tag0_5" ).data( data ).
            build();
        return tagContent;
    }
}