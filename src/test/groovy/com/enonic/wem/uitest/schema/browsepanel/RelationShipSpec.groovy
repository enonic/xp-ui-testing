package com.enonic.wem.uitest.schema.browsepanel
import java.util.Random;

import org.testng.Assert;

import com.enonic.autotests.pages.schemamanager.KindOfContentTypes;
import com.enonic.autotests.pages.schemamanager.SchemaGridPage;
import com.enonic.autotests.testdata.TestDataConvertor;
import com.enonic.autotests.vo.schemamanger.ContentType;



import spock.lang.Shared;

import com.enonic.autotests.services.ContentTypeService;
import com.enonic.wem.uitest.BaseGebSpec;
import com.enonic.wem.uitest.schema.cfg.LinkRelationship;

class RelationshipSpec extends BaseGebSpec
{

	@Shared ContentTypeService contentTypeService = new ContentTypeService();
	
	def "Given BrowsePanel When adding relationship  Then the new relationship should be listed in the table"()
	{
		given:
		go "admin"
		String relCFG = LinkRelationship.CFG
		String relationshipName = "relationship" + Math.abs(new Random().nextInt());
		ContentType relationship = ContentType.with().name(relationshipName).kind(KindOfContentTypes.RELATIONSHIP_TYPE).configuration(relCFG).build();
		 
		when:
		relationship.setName(relationshipName);
		relationship.setDisplayNameInConfig("testrelationship");
		SchemaGridPage grid = (SchemaGridPage) contentTypeService.createContentType(getTestSession(), relationship, true);
		
		then:
		grid.isContentTypePresentInTable(relationship);
		
	}
}

