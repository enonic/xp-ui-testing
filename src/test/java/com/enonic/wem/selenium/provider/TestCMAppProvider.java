package com.enonic.wem.selenium.provider;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.testng.annotations.DataProvider;

import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.testdata.TestDataConvertor;
import com.enonic.autotests.testdata.accounts.SystemUserXml;
import com.enonic.autotests.testdata.accounts.SystemUsersTestData;
import com.enonic.autotests.testdata.cm.AbstractContentXml;
import com.enonic.autotests.testdata.cm.ContentTestData;

public class TestCMAppProvider
{

	private static final String CM_TEST_DATA = "cm-test-data.xml";
	private static final String CM_OPEN_TEST_DATA = "cm-opencontent-test.xml";
	private static final String CM_TEST_DATA_NEG = "cm-test-data-negative.xml";
	
	private static final String FILTERING_TEST_DATA = "filtering-test-data.xml";
	
	@DataProvider(name = "filteringAddContent")
	public static Object[][] filteringAddContent() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream in = TestDataConvertor.class.getClassLoader().getResourceAsStream("test-data/cm/" + FILTERING_TEST_DATA);
		if (in == null) {
			throw new TestFrameworkException("test data was not found!");
		}
		ContentTestData testdata = (ContentTestData) unmarshaller.unmarshal(in);
		List<AbstractContentXml> cases = testdata.getContentList();
		for (AbstractContentXml ctype : cases) {
			casesParameters.add(new Object[] { ctype });
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}

	/**
	 * @return
	 * @throws JAXBException
	 */
	@DataProvider(name = "addNewContent")
	public static Object[][] addNewContent() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream in = TestDataConvertor.class.getClassLoader().getResourceAsStream("test-data/cm/" + CM_TEST_DATA);
		if (in == null) {
			throw new TestFrameworkException("test data was not found!");
		}
		ContentTestData testdata = (ContentTestData) unmarshaller.unmarshal(in);
		List<AbstractContentXml> cases = testdata.getContentList();
		for (AbstractContentXml ctype : cases) {
			casesParameters.add(new Object[] { ctype });
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}
	@DataProvider(name = "openAndVerify")
	public static Object[][] openAndVerify() throws JAXBException {

		List<Object[]> casesParameters = new ArrayList<Object[]>();
		JAXBContext context = JAXBContext.newInstance(ContentTestData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream in = TestDataConvertor.class.getClassLoader().getResourceAsStream("test-data/cm/" + CM_OPEN_TEST_DATA);
		if (in == null) {
			throw new TestFrameworkException("test data was not found!");
		}
		ContentTestData testdata = (ContentTestData) unmarshaller.unmarshal(in);
		List<AbstractContentXml> cases = testdata.getContentList();
		for (AbstractContentXml ctype : cases) {
			casesParameters.add(new Object[] { ctype });
		}
		return casesParameters.toArray(new Object[casesParameters.size()][]);
	}
}
