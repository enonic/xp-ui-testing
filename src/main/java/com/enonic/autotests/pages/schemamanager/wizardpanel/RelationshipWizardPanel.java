package com.enonic.autotests.pages.schemamanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.vo.schemamanger.RelationshipType;

public class RelationshipWizardPanel
    extends SchemaWizardPanel<RelationshipType>
{
    public final String DIV_RELATIONSHIP_WIZARD = "//div[contains(@id,'app.wizard.RelationshipTypeWizardPanel')]";

    public final String TOOLBAR_SAVE_BUTTON_XPATH =
        "//div[contains(@id,'app.wizard.RelationshipTypeWizardToolbar')]/*[contains(@id, 'api.ui.ActionButton') and child::span[text()='Save']]";

    public final String TOOLBAR_CLOSE_WIZARD_BUTTON_XPATH =
        "//div[contains(@id,'app.wizard.RelationshipTypeWizardToolbar')]/*[contains(@id, 'api.ui.ActionButton') and child::span[text()='Close']]";

    @FindBy(xpath = TOOLBAR_SAVE_BUTTON_XPATH)
    protected WebElement toolbarSaveButton;

    @FindBy(xpath = TOOLBAR_CLOSE_WIZARD_BUTTON_XPATH)
    protected WebElement closeButton;

    public RelationshipWizardPanel( TestSession session )
    {
        super( session );
    }


    public RelationshipWizardPanel typeData( RelationshipType relationship )
    {
        // 1. type a data: 'name' and 'Display Name'.
        clearAndType( nameInput, relationship.getName() );
        //2. type the XML-config data:
        getLogger().info( "set contenttype configuration " );
        setConfiguration( relationship.getConfigData().trim() );
        return this;
    }

    @Override
    public RelationshipWizardPanel save()
    {
        boolean isSaveButtonEnabled = waitUntilElementEnabledNoException( By.xpath( TOOLBAR_SAVE_BUTTON_XPATH ), 2l );
        //getDriver().findElements(By.xpath( TOOLBAR_SAVE_BUTTON_XPATH ));
        if ( !isSaveButtonEnabled )
        {
            throw new SaveOrUpdateException( "Impossible to save, button 'Save' is disabled!" );
        }
        toolbarSaveButton.click();
        boolean isSaveEnabled = isEnabledSaveButton();
        if ( !isSaveEnabled )
        {
            throw new SaveOrUpdateException( "the content with  was not correctly saved, button 'Save' still disabled!" );
        }
        return this;
    }

    public boolean isEnabledSaveButton()
    {
        return waitUntilElementEnabledNoException( By.xpath( TOOLBAR_SAVE_BUTTON_XPATH ), Application.IMPLICITLY_WAIT );
    }

    @Override
    public boolean isOpened()
    {
        return toolbarSaveButton.isDisplayed();

    }

    @Override
    public WizardPanel<RelationshipType> waitUntilWizardOpened()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( DIV_RELATIONSHIP_WIZARD ), Application.DEFAULT_IMPLICITLY_WAIT );
        if ( !result )
        {
            throw new TestFrameworkException( "RelationshipWizardPanel was not showed!" );
        }
        return this;
    }

    @Override
    public String getWizardDivXpath()
    {

        return DIV_RELATIONSHIP_WIZARD;
    }


    @Override
    public WebElement getCloseButton()
    {
        return closeButton;
    }

}
