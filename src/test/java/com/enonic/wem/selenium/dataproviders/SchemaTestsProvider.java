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

public class SchemaTestsProvider
{

	private static final String CONTENT_TYPE_TEST_DATA = "create-content-type.xml";
	private static final String RELATIONSHIP_TYPE_TEST_DATA = "create-relationship-type.xml";
	private static final String MIXIN_TEST_DATA = "mixin-test.xml";
	private static final String DELETE_MIXIN_TEST_DATA = "delete-mixin-test.xml";
	private static final String RELATIONSHIP_TEST_DATA = "create-relationship.xml";
	private static final String DELETETEST_TEST_DATA = "delete-test.xml";
	private static final String EDITTEST_TEST_DATA = "edit-test.xml";
	private static final String CHANGE_NAME_TEST_DATA = "change-ctname-test.xml";
	
	
	
	@DataProvider(name = "addRelationshipType")
	public static Object[][] addRelationshipType() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentTypeTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream in = TestDataConvertor.class.getClassLoader().getResourceAsStream("test-data/schemamanager/" + RELATIONSHIP_TYPE_TEST_DATA);
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
	
	
	
	@DataProvider(name = "deleteMixin")
	public static Object[][] deleteMixin() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentTypeTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream in = TestDataConvertor.class.getClassLoader().getResourceAsStream("test-data/schemamanager/" + DELETE_MIXIN_TEST_DATA);
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
	
	
	
	@DataProvider(name = "changeDisplayName")
	public static Object[][] changeDisplayName() throws JAXBException {

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
	
	@DataProvider(name = "changeName")
	public static Object[][] changeName() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentTypeTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream in = TestDataConvertor.class.getClassLoader().getResourceAsStream("test-data/schemamanager/" + CHANGE_NAME_TEST_DATA);
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
