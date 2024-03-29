package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.exceptions.TestFrameworkException
import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TagFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore

@Ignore
class TagsInputType_Unlim_Spec
    extends Base_InputFields_Occurrences

{

    def "GIVEN new Tag-content-wizard (unlimited) is opened WHEN no one tag is added and 'Save' and 'Publish' buttons pressed THEN new content with 'PUBLISHED' status appears"()
    {
        given: "new wizard (tag-unlimited) is opened"
        Content tagContent = buildTag_Unlim_Content( 0 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );

        when: "data has been typed then 'save' and 'publish'"
        contentWizardPanel.typeData( tagContent ).clickOnMarkAsReadyAndDoPublish();
        contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        contentWizardPanel.close( tagContent.getDisplayName() );
        filterPanel.typeSearchText( tagContent.getName() );

        then: "content has a 'Published' status"
        contentBrowsePanel.getContentStatus( tagContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() )
    }

    def "GIVEN new Tag-content-wizard (unlimited) is opened AND one tag is added WHEN the content has been reopened THEN expected Tag should be present in wizard "()
    {
        given: "start to add a content with type 'Tag unlimited'"
        Content tagContent = buildTag_Unlim_Content( 1 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );

        when: "type a data and 'save' and open for edit the new created content"
        contentWizardPanel.typeData( tagContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );

        then: "one expected tag should be present in the page"
        formViewPanel.getNumberOfTags() == 1;
        and:
        formViewPanel.getTagsText().contains( TAG_1 );
    }

    def "GIVEN wizard for adding a tag opened and 6 tags have been added WHEN one tags removed THEN number of tags reduced "()
    {
        given: "start to add a content with type 'Tag unlimited'"
        Content tagContent = buildTag_Unlim_Content( 6 );
        selectSitePressNew( tagContent.getContentTypeName() ).typeData( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );
        int numberOfTagBeforeRemoving = formViewPanel.getNumberOfTags();

        when: "one tag removed"
        formViewPanel.removeLastTag();

        then: "number of tags reduced"
        ( numberOfTagBeforeRemoving - formViewPanel.getNumberOfTags() ) == 1;
    }

    def "WHEN wizard for adding a tag opened and 6 tags have been added THEN tags input enabled, because this is unlimited type"()
    {
        when: "start to add a content with type 'Tag unlimited'"
        Content tagContent = buildTag_Unlim_Content( 6 );
        selectSitePressNew( tagContent.getContentTypeName() ).typeData( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );

        then: "tags input enabled, because this is unlimited type"
        formViewPanel.getNumberOfTags() == 6;

        and: "tags input is enabled and there ia ability to add new tags"
        formViewPanel.isTagsInputDisplayed();
    }

    def "GIVEN wizard for adding a Tag-content (unlimited) opened WHEN six tags added and 'Save' button pressed and just created content opened THEN six Tags with correct name are present in the wizard page"()
    {
        given: "start to add a content with type 'Tag unlimited'"
        Content tagContent = buildTag_Unlim_Content( 6 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( tagContent.getContentTypeName() );

        when: "type a data and 'save' and open for edit new created content"
        contentWizardPanel.typeData( tagContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );
        saveScreenshot( "tags-unlim_bug" )

        then: "six Tags with correct name are present in the wizard page"
        formViewPanel.getNumberOfTags() == 6;
        and:
        String[] tags = [TAG_1, TAG_2, TAG_3, TAG_4, TAG_5, TAG_6];
        formViewPanel.getTagsText().containsAll( tags.toList() );
    }

    private PropertyTree buildUnlimTagData( int numberOfTags )
    {
        PropertyTree data = new PropertyTree();
        data.setLong( "min", 0 );
        data.setLong( "max", 6 );
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
            case 6:
                data.addString( "tags", TAG_1 );
                data.addString( "tags", TAG_2 );
                data.addString( "tags", TAG_3 );
                data.addString( "tags", TAG_4 );
                data.addString( "tags", TAG_5 );
                data.addString( "tags", TAG_6 );
                break;
            default:
                throw new TestFrameworkException( "data not implemented" );
        }
        return data;
    }

    private Content buildTag_Unlim_Content( int numberOfTags )
    {
        PropertyTree data = buildUnlimTagData( numberOfTags );
        String name = "tag_unlim";
        Content tagContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "tag_unlim content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "tag_unlim" ).data( data ).
            build();
        return tagContent;
    }
}
