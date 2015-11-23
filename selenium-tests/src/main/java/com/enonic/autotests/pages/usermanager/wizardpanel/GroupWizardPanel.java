package com.enonic.autotests.pages.usermanager.wizardpanel;


import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.usermanager.Group;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class GroupWizardPanel
    extends WizardPanel<Group>
{

    public static final String DIV_ROLE_WIZARD_PANEL =
        "//div[contains(@id,'app.wizard.GroupWizardPanel') and not(contains(@style,'display: none'))]";

    public static final String TOOLBAR_SAVE_BUTTON =
        "//div[contains(@id,'app.wizard.GroupWizardPanel')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Save']]";

    public static final String TOOLBAR_CLOSE_WIZARD_BUTTON =
        "//div[contains(@id,'app.wizard.GroupWizardPanel')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Close']]";

    private static final String TOOLBAR_DUPLICATE_BUTTON =
        "//div[@id='app.wizard.GroupWizardPanel']/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Duplicate']]";

    private static final String TOOLBAR_DELETE_BUTTON =
        "//div[contains(@id,'app.wizard.GroupWizardPanel')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Delete']]";

    @FindBy(xpath = TOOLBAR_DELETE_BUTTON)
    private WebElement toolbarDeleteButton;

    /**
     * The constructor.
     *
     * @param session
     */
    public GroupWizardPanel( TestSession session )
    {
        super( session );
    }

    @Override
    public String getWizardDivXpath()
    {
        return DIV_ROLE_WIZARD_PANEL;
    }

    @Override
    public WizardPanel<Group> save()
    {
        return null;
    }

    @Override
    public boolean isOpened()
    {
        return false;
    }

    @Override
    public boolean isSaveButtonEnabled()
    {
        return false;
    }

    @Override
    public WizardPanel<Group> typeData( final Group group )
    {
        // 1. type a data: 'name' and 'Display Name'.
        waitElementClickable( By.name( "displayName" ), 2 );
        getLogger().info( "types displayName: " + group.getDisplayName() );
        clearAndType( displayNameInput, group.getDisplayName() );
        sleep( 500 );
        if ( StringUtils.isNotEmpty( group.getDisplayName() ) )
        {
            waitElementClickable( By.name( "name" ), 2 );
            getLogger().info( "types name: " + group.getDisplayName() );
            //  clearAndType( nameInput, user.getName() );
        }
        TestUtils.saveScreenshot( getSession(), group.getDisplayName() );
        // 2. populate main tab
        return this;
    }

    @Override
    public WizardPanel<Group> waitUntilWizardOpened()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( DIV_ROLE_WIZARD_PANEL ), Application.EXPLICIT_NORMAL );
        findElements( By.xpath( DIV_ROLE_WIZARD_PANEL ) );
        if ( !result )
        {
            throw new TestFrameworkException( "UserWizard was not showed!" );
        }
        return this;
    }

    @Override
    public boolean isDeleteButtonEnabled()
    {
        return toolbarDeleteButton.isEnabled();
    }
}