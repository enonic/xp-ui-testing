package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.exceptions.TestFrameworkException
import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TagFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared

class TagsInputType_Unlim_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    String TAG_1 = "tag1"

    @Shared
    String TAG_2 = "tag2"

    @Shared
    String TAG_3 = "tag3"

    @Shared
    String TAG_4 = "tag4"

    @Shared
    String TAG_5 = "tag5"

    @Shared
    String TAG_6 = "tag6"


    def "GIVEN wizard for adding a Tag-content (unlimited) opened WHEN no one tag added and 'Save' and 'Publish' buttons pressed and wizard closed THEN new content with status 'online' appears "()
    {
        given: "start to add a content with type 'Tag unlimited'"
        Content tagContent = buildTag_Unlim_Content( 0 );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( tagContent.getContentTypeName() );

        when: "type a data and 'save' and 'publish'"
        contentWizardPanel.typeData(
            tagContent ).save().clickOnWizardPublishButton().clickOnPublishNowButton().waitPublishNotificationMessage(
            Application.EXPLICIT_NORMAL );
        contentWizardPanel.close( tagContent.getDisplayName() );
        filterPanel.typeSearchText( tagContent.getName() );

        then: "content has a 'online' status"
        contentBrowsePanel.getContentStatus( tagContent.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() )
    }


    def "GIVEN wizard for adding a Tag-content (unlimited) opened WHEN one tag added and 'Save' button pressed and just created content opened THEN only one Tag with correct name present on wizard "()
    {
        given: "start to add a content with type 'Tag unlimited'"
        Content tagContent = buildTag_Unlim_Content( 1 );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( tagContent.getContentTypeName() );

        when: "type a data and 'save' and open for edit the new created content"
        contentWizardPanel.typeData( tagContent ).save().close( tagContent.getDisplayName() );
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );

        then: "one tag with correct text present on the page"
        formViewPanel.getNumberOfTags() == 1;
        and:
        formViewPanel.getTagsText().contains( TAG_1 );
    }


    def "GIVEN wizard for adding a tag opened and 6 tags have been added WHEN one tags removed THEN number of tags reduced "()
    {
        given: "start to add a content with type 'Tag unlimited'"
        Content tagContent = buildTag_Unlim_Content( 6 );
        selectSiteOpenWizard( tagContent.getContentTypeName() ).typeData( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );
        int numberOfTagBeforeRemoving = formViewPanel.getNumberOfTags();

        when: "one tag removed"
        formViewPanel.removeLastTag();

        then: "number of tags reduced"
        ( numberOfTagBeforeRemoving - formViewPanel.getNumberOfTags() ) == 1;
    }

    def "WHEN wizard for adding a tag opened and 6 tags have been added THEN tags input enabled"()
    {
        when: "start to add a content with type 'Tag unlimited'"
        Content tagContent = buildTag_Unlim_Content( 6 );
        selectSiteOpenWizard( tagContent.getContentTypeName() ).typeData( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );

        then: "number of tags reduced"
        formViewPanel.getNumberOfTags() == 6;
        and: "tags input is enabled and there ia ability to add new tags"
        formViewPanel.isTagsInputDisplayed();
    }


    def "GIVEN wizard for adding a Tag-content (unlimited) opened WHEN six tags added and 'Save' button pressed and just created content opened THEN six Tags with correct name are present in the wizard page "()
    {
        given: "start to add a content with type 'Tag unlimited'"
        Content tagContent = buildTag_Unlim_Content( 6 );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( tagContent.getContentTypeName() );

        when: "type a data and 'save' and open for edit new created content"
        contentWizardPanel.typeData( tagContent ).save().close( tagContent.getDisplayName() );

        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );
        TestUtils.saveScreenshot( getSession(), "tags-unlim_bug" )

        then: "one tag with correct text present on the page"
        formViewPanel.getNumberOfTags() == 6;
        and:
        String[] tags = [TAG_1, TAG_2, TAG_3, TAG_4, TAG_5, TAG_6];
        formViewPanel.getTagsText().containsAll( tags.toList() );
    }


    private PropertyTree buildData( int numberOfTags )
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
        PropertyTree data = buildData( numberOfTags );
        String name = "tag_unlim";
        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "tag_unlim content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":tag_unlim" ).data( data ).
            build();
        return textLineContent;
    }
}