package com.enonic.wem.uitest.schema.browsepanel

import java.util.Random;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.enonic.autotests.pages.schemamanager.SchemaGridPage;
import com.enonic.autotests.testdata.TestDataConvertor;

import com.enonic.autotests.vo.schemamanger.ContentType;

import com.enonic.autotests.pages.schemamanager.SchemaGridPage;
import com.enonic.autotests.vo.schemamanger.ContentType;
import spock.lang.Shared;
import spock.lang.Stepwise;
import com.enonic.autotests.pages.schemamanager.KindOfContentTypes;
import com.enonic.autotests.pages.schemamanager.SchemaGridPage;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.vo.schemamanger.ContentType;
import com.enonic.wem.uitest.BaseGebSpec;
import com.enonic.wem.uitest.schema.cfg.MixinAddress;

@Stepwise
class MixinSpec extends BaseGebSpec
{
	@Shared ContentTypeService contentTypeService = new ContentTypeService();
	@Shared String MIXIN_KEY = "mixin"

	def "Given BrowsePanel When adding Mixin-adress  Then the new mixin should be listed in the table"()
	{
		given:
		go "admin"
		String mixinCFG = MixinAddress.CFG
		ContentType mixin = ContentType.with().name("adressmixin").kind(KindOfContentTypes.MIXIN).configuration(mixinCFG).build();
		getTestSession().put(MIXIN_KEY,mixin);
		
		when:
		contentTypeService.createContentType(getTestSession(), mixin, true)
				
		then:
		SchemaGridPage grid = new SchemaGridPage(getTestSession())
		grid.isContentTypePresentInTable(mixin)

	}
	

	def "Given BrowsePanel and exist Mixin  When Mixin editet, name changed  Then the Mixin whith new name should be listed in the table"()
	{   
		given:
		go "admin"
		
		ContentType ct = (ContentType)getTestSession().get(MIXIN_KEY);
		ContentType newMixin = ct.cloneContentType();
		String newName = "mixinrenamed"+ Math.abs(new Random().nextInt());
		newMixin.setName(newName);
		
		when:
		SchemaGridPage schemasPage = contentTypeService.editContentType(getTestSession(), ct, newMixin);
		ct.setName(newName);
		
		then:
		schemasPage.isContentTypePresentInTable(newMixin);
		
	}
	
	
	def "Given BrowsePanel and exist Mixin  When Mixin editet, display-name changed  Then the Mixin whith new display-name should be listed in the table"()
	{
		given:
		go "admin"
		
		ContentType ct = (ContentType)getTestSession().get(MIXIN_KEY);
		ContentType newMixin = ct.cloneContentType();	
		String newDisplayName = "change display name test";
		// set a new display name:
		newMixin.setDisplayNameInConfig(newDisplayName );
		
		when:
		SchemaGridPage schemasPage = contentTypeService.editContentType(getTestSession(), ct, newMixin)
		ct.setDisplayNameInConfig(newDisplayName );
		then:
		schemasPage.isContentTypePresentInTable(newMixin)
	}
			
	def "Given BrowsePanel When existing selected mixin and clicking Delete Then Mixin is removed from list"()
	
	{
		given:
		go "admin"	
		ContentType mixinToDelete = (ContentType)getTestSession().get(MIXIN_KEY)
		
		when:
		SchemaGridPage schemasPage = contentTypeService.deleteContentType(getTestSession(), mixinToDelete);
		
		then:
		!schemasPage.isContentTypePresentInTable(mixinToDelete);
	}
}
