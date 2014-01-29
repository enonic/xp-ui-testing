package com.enonic.wem.uitest.content

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentGridPage;
import com.enonic.autotests.pages.contentmanager.browsepanel.SelectContentTypeDialog.ContentTypeName;
import com.enonic.autotests.vo.contentmanager.StructuredContent;
import spock.lang.Shared;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentGridPage;
import com.enonic.autotests.pages.contentmanager.browsepanel.SelectContentTypeDialog.ContentTypeName;
import com.enonic.autotests.services.ContentManagerService;
import com.enonic.autotests.vo.contentmanager.ArchiveContent;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;
import com.enonic.autotests.vo.contentmanager.CitationContent;
import com.enonic.autotests.vo.contentmanager.DataContent;
import com.enonic.autotests.vo.contentmanager.FolderContent;
import com.enonic.autotests.vo.contentmanager.MediaContent;
import com.enonic.autotests.vo.contentmanager.MixinContent;
import com.enonic.autotests.vo.contentmanager.PageContent;
import com.enonic.autotests.vo.contentmanager.ShortcutContent;
import com.enonic.autotests.vo.contentmanager.StructuredContent;
import com.enonic.autotests.vo.contentmanager.TextContent;
import com.enonic.autotests.vo.contentmanager.UnstructuredContent;
import com.enonic.wem.uitest.BaseGebSpec;

class BrowsePanelSpec extends BaseGebSpec
{
	   @Shared String REPONAME = "/bluman-trampoliner";
	   
	   @Shared ContentManagerService cManagerService = new ContentManagerService();
	   
	  def "Given BrowsePanel and exist content  When content editet, name changed  Then the content whit new name should be listed in the table"()
	   {
		   given:
		   go "admin"
		   
		   String name = "editname" 
		   StructuredContent contentToEdit = StructuredContent.builder().withName(name).withDisplayName("edittest").build();
		   String[] parent = [REPONAME]
		   contentToEdit.setParentNames(parent);
		   
		   cManagerService.addContent(getTestSession(), contentToEdit, true)
		   
		   
		   String newName = "edited" + Math.abs(new Random().nextInt());
		   StructuredContent newcontent = StructuredContent.builder().withName(newName).withDisplayName("edited").build();
		   newcontent.setParentNames(parent);
		   
		   when:
		   cManagerService.doOpenContentAndEdit(getTestSession(), contentToEdit, newcontent);
		   
		   then:
		   ContentGridPage grid = new ContentGridPage(getTestSession())
		   grid.findContentInTable(newcontent, 2l)
   
	   }
   
	   def "Given BrowsePanel and exist content  When content editet, display name changed  Then the content whit new display-name should be listed in the table"()
	   {
		   given:
		   go "admin"
		   
		   String name = "editdisplayname"
		   StructuredContent contentToEdit = StructuredContent.builder().withName(name).withDisplayName("edittest").build();
		   String[] parent = [REPONAME]
		   contentToEdit.setParentNames(parent);
		   
		   cManagerService.addContent(getTestSession(), contentToEdit, true)
		   
		   
		   String newDisplayName = "edited" + Math.abs(new Random().nextInt());
		   StructuredContent newcontent = StructuredContent.builder().withName(name).withDisplayName(newDisplayName).build();
		   newcontent.setParentNames(parent);
		   
		   when:
		   cManagerService.doOpenContentAndEdit(getTestSession(), contentToEdit, newcontent);
		   
		   then:
		   ContentGridPage grid = new ContentGridPage(getTestSession())
		   grid.findContentInTable(newcontent, 2l)
   
	   }
   
	   
		def "Given BrowsePanel When adding Folder content Then the content should be listed in the table"()
		{
			given:
			go "admin"
			
			BaseAbstractContent content = FolderContent.builder().withName("foldercontent").withDisplayName("folder").build();
			String[] parent = [REPONAME]
			content.setParentNames(parent)
			
			when:
			cManagerService.addContent(getTestSession(), content, true)
			report "ContentGridPage opened, try to find a new content with name: "+ content.getName()
			then:
			ContentGridPage grid = new ContentGridPage(getTestSession())
			grid.findContentInTable(content, 2l)
	
		}
		
		def "Given BrowsePanel When adding Structured content Then the content should be listed in the table"()
		{
			given:
			go "admin"
			
			BaseAbstractContent content = StructuredContent.builder().withName("structuredcontent").withDisplayName("sturcured").build();
			String[] parent = [REPONAME]
			content.setParentNames(parent)
			
			when:
			cManagerService.addContent(getTestSession(), content, true)
			report "ContentGridPage opened, try to find a new content with name: "+ content.getName()
			then:
			ContentGridPage grid = new ContentGridPage(getTestSession())
			grid.findContentInTable(content, 2l)
	
		}
		
		def "Given BrowsePanel When adding Unstructured content Then the content should be listed in the table"()
		{
			given:
			go "admin"
			
			BaseAbstractContent content = UnstructuredContent.builder().withName("unstructuredcontent").withDisplayName("unsturcured").build();
			String[] parent = [REPONAME]
			content.setParentNames(parent)
			
			when:
			cManagerService.addContent(getTestSession(), content, true)
			report "ContentGridPage opened, try to find a new content with name: "+ content.getName()
			then:
			ContentGridPage grid = new ContentGridPage(getTestSession())
			grid.findContentInTable(content, 2l)
	
		}
		
		def "Given BrowsePanel When adding Archive content Then the content should be listed in the table"()
		{
			given:
			go "admin"
			
			BaseAbstractContent content = ArchiveContent.builder().withName("archivecontent").withDisplayName("archive").build();
			String[] parent = [REPONAME]
			content.setParentNames(parent)
			
			when:
			cManagerService.addContent(getTestSession(), content, true)
			report "ContentGridPage opened, try to find a new content with name: "+ content.getName()
			then:
			ContentGridPage grid = new ContentGridPage(getTestSession())
			grid.findContentInTable(content, 2l)
	
		}
		
		def "Given BrowsePanel When adding Media content Then the content should be listed in the table"()
		{
			given:
			go "admin"
			
			BaseAbstractContent content = MediaContent.builder().withName("mediacontent").withDisplayName("media").build();
			String[] parent = [REPONAME]
			content.setParentNames(parent)
			
			when:
			cManagerService.addContent(getTestSession(), content, true)
			report "ContentGridPage opened, try to find a new content with name: "+ content.getName()
			then:
			ContentGridPage grid = new ContentGridPage(getTestSession())
			grid.findContentInTable(content, 2l)
	
		}
		
		def "Given BrowsePanel When adding Data content Then the content should be listed in the table"()
		{
			given:
			go "admin"
			
			BaseAbstractContent content = DataContent.builder().withName("datacontent").withDisplayName("data").build();
			String[] parent = [REPONAME]
			content.setParentNames(parent)
			
			when:
			cManagerService.addContent(getTestSession(), content, true)
			report "ContentGridPage opened, try to find a new content with name: "+ content.getName()
			then:
			ContentGridPage grid = new ContentGridPage(getTestSession())
			grid.findContentInTable(content, 2l)
	
		}
		
		
		def "Given BrowsePanel When adding Text content Then the content should be listed in the table"()
		{
			given:
			go "admin"
			
			BaseAbstractContent content = TextContent.builder().withName("textcontent").withDisplayName("text").build();
			String[] parent = [REPONAME]
			content.setParentNames(parent)
			
			when:
			cManagerService.addContent(getTestSession(), content, true)
			report "ContentGridPage opened, try to find a new content with name: "+ content.getName()
			then:
			ContentGridPage grid = new ContentGridPage(getTestSession())
			grid.findContentInTable(content, 2l)
	
		}
		def "Given BrowsePanel When adding Page content Then the content should be listed in the table"()
		{
			given:
			go "admin"
			
			BaseAbstractContent content = PageContent.builder().withName("pagecontent").withDisplayName("page").build();
			String[] parent = [REPONAME]
			content.setParentNames(parent)
			
			when:
			cManagerService.addContent(getTestSession(), content, true)
			report "ContentGridPage opened, try to find a new content with name: "+ content.getName()
			then:
			ContentGridPage grid = new ContentGridPage(getTestSession())
			grid.findContentInTable(content, 2l)
	
		}
		
		def "Given BrowsePanel When adding Shortcut content Then the content should be listed in the table"()
		{
			given:
			go "admin"
			
			BaseAbstractContent content = ShortcutContent.builder().withName("shortcutcontent").withDisplayName("shortcut").build();
			String[] parent = [REPONAME]
			content.setParentNames(parent)
			
			when:
			cManagerService.addContent(getTestSession(), content, true)
			report "ContentGridPage opened, try to find a new content with name: "+ content.getName()
			then:
			ContentGridPage grid = new ContentGridPage(getTestSession())
			grid.findContentInTable(content, 2l)
	
		}
		
}
