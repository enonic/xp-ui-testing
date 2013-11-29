package com.enonic.autotests.services;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.schemamanger.ContentType;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.schemamanager.AddNewContentTypeWizard;
import com.enonic.autotests.pages.schemamanager.SchemasPage;

public class ContentTypeService
{

	/**
	 * Navigates to SchemaManager application and create a new content type.
	 * 
	 * @param testSession
	 * @param contentType
	 * @param isCloseWizard
	 * @return {@link SchemasPage} instance if wizard was closed.
	 */
	public Page createNewContentType(TestSession testSession, ContentType contentType, boolean isCloseWizard)
	{
		SchemasPage schemaManagerPage = NavigatorHelper.openSchemaManager(testSession);
		schemaManagerPage.doAddContentType(contentType,isCloseWizard);

		if (isCloseWizard)
		{
			return schemaManagerPage;
			
		} else
		{
			return new AddNewContentTypeWizard(testSession);
		}

	}
	
	
	/**
	 * Navigates to SchemaManager application select a content type in the table and delete it.
	 * 
	 * @param testSession
	 * @param contentTypeToDelete
	 * @return {@link SchemasPage} instance.
	 */
	public SchemasPage deleteContentType(TestSession testSession, ContentType contentTypeToDelete)
	{
		SchemasPage spacesPage = NavigatorHelper.openSchemaManager(testSession);

		spacesPage.doDeleteContentType(contentTypeToDelete);
		return new SchemasPage(testSession);
	}
}
