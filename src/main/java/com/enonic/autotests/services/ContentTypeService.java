package com.enonic.autotests.services;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.schemamanger.ContentType;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.schemamanager.AddNewContentTypeWizard;
import com.enonic.autotests.pages.schemamanager.SchemaTablePage;

public class ContentTypeService
{

	/**
	 * Navigates to SchemaManager application and create a new content type.
	 * 
	 * @param testSession
	 * @param contentType
	 * @param isCloseWizard
	 * @return {@link SchemaTablePage} instance if wizard was closed.
	 */
	public Page createContentType(TestSession testSession, ContentType contentType, boolean isCloseWizard)
	{
		SchemaTablePage schemaManagerPage = NavigatorHelper.openSchemaManager(testSession);
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
	 * @return {@link SchemaTablePage} instance.
	 */
	public SchemaTablePage deleteContentType(TestSession testSession, ContentType contentTypeToDelete)
	{
		SchemaTablePage spacesPage = NavigatorHelper.openSchemaManager(testSession);

		spacesPage.doDeleteContentType(contentTypeToDelete);
		return spacesPage;
	}
	

	/**
	 * Navigates to SchemaManager application select a content type in the table and delete it.
	 * 
	 * @param testSession
	 * @param contentTypeToDelete
	 * @return {@link SchemaTablePage} instance.
	 */
	public SchemaTablePage editContentType(TestSession testSession, ContentType contentTypeToEdit, ContentType newContentType)
	{
		SchemaTablePage spacesPage = NavigatorHelper.openSchemaManager(testSession);

		spacesPage.doEditContentType(contentTypeToEdit, newContentType);
		return spacesPage;
	}
}
