package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.exceptions.TestFrameworkException
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.FormViewPanel
import com.enonic.autotests.pages.form.TagFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared

class TagsInputType_2_5_Spec
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


    def "GIVEN wizard for adding a Tag-content (2:5) opened WHEN no one tag added and  'Save' and 'Publish' buttons pressed THEN new content with status 'online' appears "()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 2 );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( tagContent.getContentTypeName() );

        when: "type a data and 'save' and 'publish'"
        contentWizardPanel.typeData( tagContent ).save().clickOnPublishButton().close( tagContent.getDisplayName() );
        filterPanel.typeSearchText( tagContent.getName() );

        then: "content has a 'online' status"
        contentBrowsePanel.getContentStatus( tagContent.getPath() ).equals( ContentStatus.ONLINE.getValue() )
    }

    def "GIVEN creating new Tag-content 2:5 on root WHEN only one tag added and button 'Publish' pressed THEN validation message appears"()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 1 );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( tagContent.getContentTypeName() );

        when:
        contentWizardPanel.clickOnPublishButton();
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );

        then: "new content listed in the grid and can be opened for edit"
        formViewPanel.isValidationMessagePresent();
        and:
        formViewPanel.getValidationMessage() == String.format( FormViewPanel.VALIDATION_MESSAGE, 2 );
    }

    def "GIVEN wizard for adding a Tag-content (2:5) opened WHEN five tags added  THEN input text becomes disabled and impossible to add one more tag"()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 5 );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( tagContent.getContentTypeName() );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );
        boolean isDisplayedBefore = formViewPanel.isTagsInputDisplayed()

        when: "five tags added, input text becomes disabled(display: none)"
        contentWizardPanel.typeData( tagContent )

        then: "one tag with correct text present on the page"
        !formViewPanel.isTagsInputDisplayed() && isDisplayedBefore;
    }

    def "GIVEN five tags added in input is disabled WHEN one of the fives tags removed  THEN input text becomes enabled again"()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 5 );
        selectSiteOpenWizard( tagContent.getContentTypeName() ).typeData( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );
        boolean isDisplayedBefore = formViewPanel.isTagsInputDisplayed()

        when: "five tags added, input text becomes disabled(display: none)"
        formViewPanel.removeLastTag();

        then: "one tag with correct text present on the page"
        formViewPanel.isTagsInputDisplayed() && !isDisplayedBefore;
    }


    def "GIVEN creating new Tag-content 2:5 on root WHEN 2  tags added and button 'Save' and 'Publish' pressed  and just created content opened THEN two tags with correct name are present"()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 2 );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( tagContent.getContentTypeName() );

        when: "type a data and 'save' and open for edit new created content"
        contentWizardPanel.typeData( tagContent ).save().close( tagContent.getDisplayName() );
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );

        then: "one tag with correct text present on the page"
        formViewPanel.getNumberOfTags() == 2;
        and:
        String[] tags = [TAG_1, TAG_2];
        List<String> ttt = formViewPanel.getTagsText();
        ttt.containsAll( tags.toList() );

    }

    def "GIVEN wizard for adding a Tag-content (2:5) opened WHEN five tags added and 'Save' button pressed and just created content opened THEN five Tags with correct name are present in the wizard page "()
    {
        given: "start to add a content with type 'Tag 2:5'"
        Content tagContent = buildTag_2_5_Content( 5 );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( tagContent.getContentTypeName() );

        when: "type a data and 'save' and open for edit new created content"
        contentWizardPanel.typeData( tagContent ).save().close( tagContent.getDisplayName() );
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tagContent );
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );

        then: "one tag with correct text present on the page"
        formViewPanel.getNumberOfTags() == 5;
        and:
        String[] tags = [TAG_1, TAG_2, TAG_3, TAG_4, TAG_5];
        formViewPanel.getTagsText().containsAll( tags.toList() );
    }


    private PropertyTree buildData( int numberOfTags )
    {
        PropertyTree data = new PropertyTree();
        data.setLong( "min", 2 );
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

    private Content buildTag_2_5_Content( int numberOfTags )
    {
        PropertyTree data = buildData( numberOfTags );
        String name = "tag2_5";
        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "tag2_5 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":tag2_5" ).data( data ).
            build();
        return textLineContent;
    }
}