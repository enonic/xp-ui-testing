package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

/**
 * Add new content wizard, Mixin Tab.
 */
public class MixinWizardTab
    extends Application
{

    String adressBlock =
        "//div[contains(@class,admin-formitemset-container) and descendant::input[@name='street'] and descendant::input[@name='postalCode']]";

    @FindBy(xpath = "//button/span[contains(., 'Add Address')]")
    private WebElement addAddressButton;

    public MixinWizardTab( TestSession session )
    {
        super( session );

    }
}
