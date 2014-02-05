package com.enonic.wem.uitest.schema.browsepanel

import com.enonic.autotests.pages.schemamanager.KindOfContentTypes
import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel
import com.enonic.autotests.services.ContentTypeService
import com.enonic.autotests.vo.schemamanger.ContentType
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.wem.uitest.schema.cfg.LinkRelationship
import spock.lang.Shared

class RelationshipSpec extends BaseGebSpec {

	@Shared ContentTypeService contentTypeService = new ContentTypeService();

	def "Given BrowsePanel When adding relationship  Then the new relationship should be listed in the table"() 
	{
		given:
		go "admin"
		String relCFG = LinkRelationship.CFG
		String relationshipName = "relationship" + Math.abs(new Random().nextInt());
		ContentType relationship = ContentType.with().name(relationshipName).kind(KindOfContentTypes.RELATIONSHIP_TYPE).configuration(relCFG).build();
		relationship.setDisplayNameInConfig("testrelationship");

		when:
		SchemaBrowsePanel grid = (SchemaBrowsePanel) contentTypeService.createContentType(getTestSession(), relationship, true);

		then:
		grid.isContentTypePresentInTable(relationship);
	}
	
	def "Given BrowsePanel and existing relationship  When relationship deleted Then the this relationship should not be listed in the table"() 
	{
		given:
		go "admin"
		String relCFG = LinkRelationship.CFG
		String relationshipName = "relationship" + Math.abs(new Random().nextInt());
		ContentType relToDelete = ContentType.with().name(relationshipName).kind(KindOfContentTypes.RELATIONSHIP_TYPE).configuration(relCFG).build();
		relToDelete.setDisplayNameInConfig("relationshiptodelete");	
		SchemaBrowsePanel schemasPage = (SchemaBrowsePanel) contentTypeService.createContentType(getTestSession(), relToDelete, true);

		when:
		contentTypeService.deleteContentType(getTestSession(), relToDelete);
		
		then:
        !schemasPage.isContentTypePresentInTable(relToDelete);
	}
}

