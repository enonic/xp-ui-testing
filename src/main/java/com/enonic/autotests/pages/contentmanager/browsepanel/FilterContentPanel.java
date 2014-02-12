package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import static com.enonic.autotests.utils.SleepHelper.sleep;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.ContentFilterException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

public class FilterContentPanel extends Application
{
	private final String CLEAR_FILTER_LINK = "Clear filter";
	
	//this xpath specifies a checkbox for Filtering by 'content type name'
	private String CONTENT_TYPE_FILTER_ITEM = "//div[@class='facet-group-view' and child::h2[text()='Content Type']]//div[@class='facet-entry-view' and child::label[contains(.,'%s')]]/label";
	
	//this xpath specifies a checkbox for Filtering by 'Space'
	private String SPACE_FILTER_ITEM = "//div[@class='admin-facet-group' and @name='space']//div[contains(@class,'admin-facet') and descendant::label[contains(.,'%s')]]";
	
	//this xpath specifies a checkbox for Filtering by 'Last Modified'
	private String DATE_FILTER_ITEM = "//div[@class='admin-facet-group' and @name='ranges']//div[contains(@class,'admin-facet') and descendant::label[contains(.,'%s')]]";

	@FindBy(name = "query")
	private WebElement queryInput;

	/**
	 * The constructor
	 * 
	 * @param session
	 */
	public FilterContentPanel( TestSession session )
	{
		super(session);

	}

	/**
	 * @param query
	 */
	public void executeQuery(String query)
	{
		getLogger().info("query will be applied : "+ query);
		queryInput.sendKeys(query);
		queryInput.sendKeys(Keys.ENTER);
		sleep(1000);
		getLogger().info("Filtered by : "+ query);
	}
	/**
	 * Clicks by range of date: '1 hour' or  '1 day' or '1 week'.
	 * 
	 * @param date
	 */
	public void doFilterByDate(FilterPanelLastModified date)
	{
		String rangeXpath = String.format(DATE_FILTER_ITEM, date.getValue());
		boolean isVisible = waitUntilVisibleNoException(By.xpath(rangeXpath), 1l);
		if (!isVisible)
		{ 
			getLogger().info("range was not found: "+ date.getValue());
			throw new TestFrameworkException("The link with name 'Clear Filter' was not found!");
		}
		getDriver().findElement(By.xpath(rangeXpath)).click();
		sleep(500);
		getLogger().info("Filtered by : "+ date.getValue());
	}

	/**
	 * Clicks by link 'Clear Filter', located on the search panel.
	 */
	public void doClearFilter()
	{
		boolean isVisible = waitUntilVisibleNoException(By.linkText(CLEAR_FILTER_LINK), 2l);
		if (!isVisible)
		{
			getLogger().info("The link with name 'Clear Filter' was not found!");
			throw new TestFrameworkException("The link with name 'Clear Filter' was not found!");
		}
		getDriver().findElement(By.linkText(CLEAR_FILTER_LINK)).click();
		sleep(1000);
	}

	

	/**
	 * Select a space on the search panel and filter contents.
	 * 
	 * @param spaceName
	 *            , a space name.
	 */
	public void doFilterBySpaceDisplayName(String spaceName)
	{
		String spaceXpath = String.format(SPACE_FILTER_ITEM, spaceName);
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(spaceXpath));
		if (elems.size() == 0)
		{
			logError("space  was not found in the search panel:" + spaceName);
			throw new ContentFilterException("space  was not found in the search panel:" + spaceName);
		} else
		{
			elems.get(0).click();
		}
	}

	/**
	 * Select a content type on the search panel and filter contents.
	 * 
	 * @param type
	 */
	public void doFilterByContentType(String contentTypeName)
	{
		String itemXpath = String.format(CONTENT_TYPE_FILTER_ITEM, contentTypeName);
		List<WebElement> elems = getDriver().findElements(By.xpath(itemXpath));
		if (elems.size() == 0)
		{
			logError("content type was not found in the search panel:" + contentTypeName);
			throw new ContentFilterException("content type was not found in the search panel:" + contentTypeName);
		} else
		{
			elems.get(0).click();
		}
	}

	public List<String> getSpaceNames()
	{
		return null;
	}

}
