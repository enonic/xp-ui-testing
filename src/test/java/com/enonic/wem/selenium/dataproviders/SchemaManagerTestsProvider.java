package com.enonic.wem.selenium.dataproviders;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.testng.annotations.DataProvider;

import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.testdata.TestDataConvertor;
import com.enonic.autotests.testdata.schemamanger.ContentTypeTestData;
import com.enonic.autotests.testdata.schemamanger.ContentTypeXml;

public class SchemaManagerTestsProvider
{

	private static final String CONTENT_TYPE_TEST_DATA = "create-content-type.xml";
	private static final String MIXIN_TEST_DATA = "create-mixin.xml";
	private static final String RELATIONSHIP_TEST_DATA = "create-relationship.xml";
	private static final String DELETETEST_TEST_DATA = "delete-test.xml";
	private static final String EDITTEST_TEST_DATA = "edit-test.xml";
	
	
	
	
	@DataProvider(name = "addContentType")
	public static Object[][] addContentType() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentTypeTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream in = TestDataConvertor.class.getClassLoader().getResourceAsStream("test-data/schemamanager/" + CONTENT_TYPE_TEST_DATA);
		if (in == null) {
			throw new TestFrameworkException("test data was not found!");
		}
		ContentTypeTestData testdata = (ContentTypeTestData) unmarshaller.unmarshal(in);
		List<ContentTypeXml> cases = testdata.getContentTypes();
		for (ContentTypeXml ctype : cases) {
			casesParameters.add(new Object[] { ctype });
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}
	
	@DataProvider(name = "addMixin")
	public static Object[][] addMixin() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentTypeTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream in = TestDataConvertor.class.getClassLoader().getResourceAsStream("test-data/schemamanager/" + MIXIN_TEST_DATA);
		if (in == null) {
			throw new TestFrameworkException("test data was not found!");
		}
		ContentTypeTestData testdata = (ContentTypeTestData) unmarshaller.unmarshal(in);
		List<ContentTypeXml> cases = testdata.getContentTypes();
		for (ContentTypeXml ctype : cases) {
			casesParameters.add(new Object[] { ctype });
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}
	
	@DataProvider(name = "addRelationship")
	public static Object[][] addRelationship() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentTypeTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream in = TestDataConvertor.class.getClassLoader().getResourceAsStream("test-data/schemamanager/" + RELATIONSHIP_TEST_DATA);
		if (in == null) {
			throw new TestFrameworkException("test data was not found!");
		}
		ContentTypeTestData testdata = (ContentTypeTestData) unmarshaller.unmarshal(in);
		List<ContentTypeXml> cases = testdata.getContentTypes();
		for (ContentTypeXml ctype : cases) {
			casesParameters.add(new Object[] { ctype });
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}
	
	
	
	@DataProvider(name = "editContentType")
	public static Object[][] editContentType() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentTypeTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream in = TestDataConvertor.class.getClassLoader().getResourceAsStream("test-data/schemamanager/" + EDITTEST_TEST_DATA);
		if (in == null) {
			throw new TestFrameworkException("test data was not found!");
		}
		ContentTypeTestData testdata = (ContentTypeTestData) unmarshaller.unmarshal(in);
		List<ContentTypeXml> cases = testdata.getContentTypes();
		for (ContentTypeXml ctype : cases) {
			casesParameters.add(new Object[] { ctype });
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}
	
	@DataProvider(name = "deleteContentType")
	public static Object[][] deleteContentType() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentTypeTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream in = TestDataConvertor.class.getClassLoader().getResourceAsStream("test-data/schemamanager/" + DELETETEST_TEST_DATA);
		if (in == null) {
			throw new TestFrameworkException("test data was not found!");
		}
		ContentTypeTestData testdata = (ContentTypeTestData) unmarshaller.unmarshal(in);
		List<ContentTypeXml> cases = testdata.getContentTypes();
		for (ContentTypeXml ctype : cases) {
			casesParameters.add(new Object[] { ctype });
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}
	
	
}
