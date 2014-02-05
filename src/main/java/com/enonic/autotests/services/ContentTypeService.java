package com.enonic.autotests.services;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.schemamanager.ContentTypeWizardPanel;
import com.enonic.autotests.pages.schemamanager.KindOfContentTypes;
import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel;
import com.enonic.autotests.vo.schemamanger.ContentType;

public class ContentTypeService
{

	/**
	 * Navigates to SchemaManager application and create a new content type.
	 * 
	 * @param testSession
	 * @param contentType
	 * @param isCloseWizard
	 * @return {@link com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel} instance if wizard was closed.
	 */
	public Page createContentType(TestSession testSession, ContentType contentType, boolean isCloseWizard)
	{
		SchemaBrowsePanel schemaManagerPage = NavigatorHelper.openSchemaManager(testSession);
		schemaManagerPage.doAddContentType(contentType,isCloseWizard);

		if (isCloseWizard)
		{
			return schemaManagerPage;
			
		} else
		{
			return new ContentTypeWizardPanel(testSession);
		}

	}
	public ContentTypeWizardPanel openAddContentTypeWizard(TestSession testSession,KindOfContentTypes kind)
	{
		SchemaBrowsePanel schemaManagerPage = NavigatorHelper.openSchemaManager(testSession);
		schemaManagerPage.doOpenAddNewTypeWizard(kind.getValue());
		return new ContentTypeWizardPanel(testSession);
	
	}
	
	/**
	 * Navigates to SchemaManager application select a content type in the table and delete it.
	 * 
	 * @param testSession
	 * @param contentTypeToDelete
	 * @return {@link com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel} instance.
	 */
	public SchemaBrowsePanel deleteContentType(TestSession testSession, ContentType contentTypeToDelete)
	{
		SchemaBrowsePanel spacesPage = NavigatorHelper.openSchemaManager(testSession);

		spacesPage.doDeleteContentType(contentTypeToDelete);
		return spacesPage;
	}
	

	/**
	 * Navigates to SchemaManager application select a content type in the table and delete it.
	 * 
	 * @param testSession
	 * @param contentTypeToDelete
	 * @return {@link com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel} instance.
	 */
	public SchemaBrowsePanel editContentType(TestSession testSession, ContentType contentTypeToEdit, ContentType newContentType)
	{
		SchemaBrowsePanel spacesPage = NavigatorHelper.openSchemaManager(testSession);

		spacesPage.doEditContentType(contentTypeToEdit, newContentType);
		return spacesPage;
	}
}
