package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentPublish_Child_Spec
    extends BaseContentSpec
{

    @Shared
    Content parentContent;

    @Shared
    Content childContent1;

    @Shared
    Content childContent2;

    def "GIVEN existing parent folder with child WHEN parent content selected but 'Include child' checkbox unchecked and 'Publish' button on toolbar pressed THEN parent content has 'Online' status but child not published"()
    {
        setup: "parent folder added"
        parentContent = buildFolderContent( "publish", "parent-folder" );
        addContent( parentContent );

        and: "new child content added"
        findAndSelectContent( parentContent.getName() );
        childContent1 = buildFolderContentWithParent( "publish", "child-folder1", parentContent.getName() );
        addContent( childContent1 );

        when: " 'publish' dialog opened and parent content have been published without the child "
        contentBrowsePanel.clickOnClearSelection();
        filterPanel.typeSearchText( parentContent.getName() );
        ContentPublishDialog dialog = contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() ).clickToolbarPublish();
        dialog.setIncludeChildCheckbox( false ).clickOnPublishNowButton();

        then: "child content has 'offline' status"
        filterPanel.typeSearchText( childContent1.getName() );
        contentBrowsePanel.getContentStatus( childContent1.getName() ).equalsIgnoreCase( ContentStatus.OFFLINE.getValue() );
    }

    def "GIVEN parent 'online' folder with not published child WHEN the parent folder is selected THEN 'Publish' button on toolbar is disabled"()
    {
        given: "parent folder with a not published child"
        filterPanel.typeSearchText( parentContent.getName() );

        when: "the parent folder is selected"
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() )

        then: "'Publish' button on toolbar is disabled"
        !contentBrowsePanel.isPublishButtonEnabled();
    }

    def "GIVEN parent 'online' folder with not published child WHEN 'publish' menu expanded AND 'Publish Tree' selected THEN 'Publish Dialog' appears and correct name of child is displayed"()
    {
        given: "parent folder with a not published child"
        findAndSelectContent( parentContent.getName() );

        when: "modal dialog has been opened"
        ContentPublishDialog modalDialog = contentBrowsePanel.selectPublishTreeMenuItem();
        List<String> dependantList = modalDialog.getDependantList();

        then: "checkbox 'Include child 'is checked"
        modalDialog.isIncludeChildCheckboxSelected();

        and: "one content present in the dependant list"
        dependantList.size() == 1;

        and: "correct name of content displayed in the dependant list"
        dependantList.get( 0 ).contains( childContent1.getName() );
    }

    def "GIVEN parent 'online' folder with not published child WHEN 'Publish Tree' in the context menu selected THEN child content is 'online' now"()
    {
        given: "parent folder with a not published child"
        findAndSelectContent( parentContent.getName() );

        when: "'publish tree' menu item selected"
        ContentPublishDialog modalDialog = contentBrowsePanel.selectPublishTreeMenuItem();

        and: "'publish' button on modal dialog clicked"
        modalDialog.clickOnPublishNowButton();
        TestUtils.saveScreenshot( getSession(), "test_child_published" );

        then: "child content is 'online' now"
        filterPanel.typeSearchText( childContent1.getName() );
    }

    def "GIVEN existing published parent folder with child WHEN one more child content added into a folder  THEN just added child content has a 'Offline' status"()
    {
        setup: "add one more content into the published folder"
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() )
        childContent2 = buildFolderContentWithParent( "publish", "child-folder2", parentContent.getName() );
        addContent( childContent2 );

        when:
        contentBrowsePanel.clickOnClearSelection();
        filterPanel.typeSearchText( childContent2.getName() );

        then: "just new added content has a 'Offline' status"
        contentBrowsePanel.getContentStatus( childContent2.getName() ).equalsIgnoreCase( ContentStatus.OFFLINE.getValue() );
    }

    def "GIVEN existing parent has 'online' and child contents is 'offline' WHEN parent folder selected and 'Delete' button pressed  THEN 'offline' child content removed, but parent folder and 'online' child content are 'Pending delete' "()
    {
        when: "parent folder selected and 'Delete' button pressed"
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() ).clickToolbarDelete().doDelete();
        contentBrowsePanel.expandItem( parentContent.getPath().toString() );

        then: "online-parent becomes 'pending-delete'"
        contentBrowsePanel.getContentStatus( parentContent.getName() ).equalsIgnoreCase( ContentStatus.PENDING_DELETE.getValue() );

        and: "online-child becomes 'pending-delete'"
        contentBrowsePanel.getContentStatus( childContent1.getName() ).equalsIgnoreCase( ContentStatus.PENDING_DELETE.getValue() );

        and: "offline-child has been removed"
        !contentBrowsePanel.exists( childContent2.getName() );
    }

    def "GIVEN existing  parent folder with one child and status of both contents are 'PENDING_DELETE'  WHEN  parent folder selected and 'Publish' button pressed  THEN parent folder not listed"()
    {
        when:
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() ).clickToolbarPublish().clickOnPublishNowButton(); ;

        then: "parent folder not listed"
        !contentBrowsePanel.exists( parentContent.getName() );
    }

}
