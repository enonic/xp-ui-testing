package com.enonic.autotests.pages.cm;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.cm.Address;
import com.enonic.autotests.model.cm.MixinContent;
import com.enonic.autotests.pages.Page;

/**
 * Add new content wizard, Mixin Tab.
 * 
 */
public class MixinWizardTab extends Page
{

	String adressBlock = "//div[contains(@class,admin-formitemset-container) and descendant::input[@name='street'] and descendant::input[@name='postalCode']]";

	@FindBy(xpath = "//button/span[contains(., 'Add Address')]")
	private WebElement addAddressButton;

	public MixinWizardTab( TestSession session )
	{
		super(session);

	}

//	public void populateAddresses(MixinContent content)
//	{
//		List<Address> adrList = content.getAddressList();
//
//		List<WebElement> adrBlocks = null;// adrBlocks.size()
//		// TODO implement more than one address
//		for (int i = 0; i < adrList.size(); i++)
//		{
//			adrBlocks = getSession().getDriver().findElements(By.xpath(adressBlock));
//			adrBlocks.get(i).findElement(By.xpath("//input[@name='street']")).sendKeys(adrList.get(i).getStreet());// adrBlocks.get(3).findElements(By.xpath("//input[@name='street']")).size()
//			adrBlocks.get(i).findElement(By.xpath("//input[@name='postalCode']")).sendKeys(adrList.get(i).getPostalCode());
//			adrBlocks.get(i).findElement(By.xpath("//input[@name='postalPlace']")).sendKeys(adrList.get(i).getPostalPlace());
//			break;
//
//		}
//
//	}

}
