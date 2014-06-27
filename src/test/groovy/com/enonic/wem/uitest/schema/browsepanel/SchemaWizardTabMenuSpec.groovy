package com.enonic.wem.uitest.schema.browsepanel

import spock.lang.Ignore;
import spock.lang.Shared;

import com.enonic.autotests.pages.SaveBeforeCloseDialog;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;
import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel;
import com.enonic.autotests.pages.schemamanager.wizardpanel.ContentTypeWizardPanel;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.wem.uitest.BaseGebSpec;
import com.enonic.wem.uitest.schema.cfg.FolderContentTypeCfg;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.schemamanger.ContentType

class SchemaWizardTabMenuSpec extends BaseGebSpec
{

	@Shared
    SchemaBrowsePanel schemaBrowsePanel;

	def setup()
	{
		go "admin"
		schemaBrowsePanel = NavigatorHelper.openSchemaManager( getTestSession() );
	}
	
	def "GIVEN Content Type Wizard opened, data typed WHEN tab-menu button clicked THEN a list of items with content type's name present"()
	{
		given:
	    String folderCFG = FolderContentTypeCfg.FOLDER_CFG;
	    ContentType ctype = ContentType.newContentType().name( NameHelper.uniqueName("ctfolder" )).configData( folderCFG ).build();
	    ContentTypeWizardPanel wizard = schemaBrowsePanel.clickToolbarNew().selectKind( ctype.getSchemaKindUI().getValue() ).typeData( ctype );
				
        when:
	    wizard.showTabMenuItems();
		
		then:
		wizard.isTabMenuItemPresent(ctype.getName());
	}
	
	def "GIVEN ContentType wizard opened and data typed, tabmenuItem(close) pressed WHEN Yes is chosen THEN new ContentType is listed beneath a parent"()
	{
		given:
		String folderCFG = FolderContentTypeCfg.FOLDER_CFG;
	    ContentType ctype = ContentType.newContentType().name( NameHelper.uniqueName("ctfolder" )).configData( folderCFG ).build();
	    ContentTypeWizardPanel wizard = schemaBrowsePanel.clickToolbarNew().selectKind( ctype.getSchemaKindUI().getValue() ).typeData( ctype ).showTabMenuItems();

		when:
		SaveBeforeCloseDialog dialog = wizard.closeInTabMenuItem( ctype.getName() );
		dialog.clickYesButton();
		schemaBrowsePanel.waituntilPageLoaded(3);
		schemaBrowsePanel.expandSuperTypeFolder( ctype.getSuperTypeNameFromConfig() );
		TestUtils.saveScreenshot( getTestSession(),"schema_tabmenu1" );

		then:
		schemaBrowsePanel.exists( ctype );
	}
		
	def "GIVEN ContentType wizard opened and data typed, tabmenuItem(close) pressed WHEN No is chosen THEN new ContentType not created"()
	{
		given:
		String folderCFG = FolderContentTypeCfg.FOLDER_CFG;
		ContentType ctype = ContentType.newContentType().name( NameHelper.uniqueName("ctfolder" )).configData( folderCFG ).build();
		ContentTypeWizardPanel wizard = schemaBrowsePanel.clickToolbarNew().selectKind( ctype.getSchemaKindUI().getValue() ).typeData( ctype ).showTabMenuItems();

		when:
		SaveBeforeCloseDialog dialog = wizard.closeInTabMenuItem( ctype.getName() );
		dialog.clickNoButton();
		schemaBrowsePanel.waituntilPageLoaded(3);
		schemaBrowsePanel.expandSuperTypeFolder( ctype.getSuperTypeNameFromConfig() );
		TestUtils.saveScreenshot( getTestSession(),"schema_tabmenu2" );

		then:
		!schemaBrowsePanel.exists( ctype );
	}
	
	def "GIVEN ContentType wizard opened and data typed, tabmenuItem(close) pressed WHEN Cancel is chosen THEN Wizard showed again"()
	{
		given:
		String folderCFG = FolderContentTypeCfg.FOLDER_CFG;
		ContentType ctype = ContentType.newContentType().name( NameHelper.uniqueName("ctfolder" )).configData( folderCFG ).build();
		ContentTypeWizardPanel wizard = schemaBrowsePanel.clickToolbarNew().selectKind( ctype.getSchemaKindUI().getValue() ).typeData( ctype ).showTabMenuItems();

		when:
		SaveBeforeCloseDialog dialog = wizard.closeInTabMenuItem( ctype.getName() );
		dialog.clickCancelButton();		

		then:
		wizard.waitUntilWizardOpened();
	}
}
