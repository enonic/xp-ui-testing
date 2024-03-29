package com.enonic.autotests.pages.usermanager.wizardpanel;


import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.vo.usermanager.Group;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class GroupWizardPanel
    extends WizardPanel<Group>
{
    private final String GROUP_WIZARD_PANEL = "//div[contains(@id,'GroupWizardPanel')]";

    private final String MEMBERS_FORM = GROUP_WIZARD_PANEL + "//div[contains(@id,'MembersWizardStepForm')]";

    private final String TOOLBAR = "//div[contains(@id,'Toolbar')]";

    public final String TOOLBAR_SAVE_BUTTON =
        GROUP_WIZARD_PANEL + TOOLBAR + "//*[contains(@id, 'ActionButton') and child::span[text()='Save']]";

    private final String TOOLBAR_DELETE_BUTTON =
        GROUP_WIZARD_PANEL + TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Delete']]";

    private final String DESCRIPTION_INPUT =
        GROUP_WIZARD_PANEL + "//div[contains(@id,'PrincipalDescriptionWizardStepForm')]//input[contains(@id,'TextInput')]";

    private final String MEMBERS_OPTIONS_FILTER_INPUT =
        GROUP_WIZARD_PANEL + "//div[contains(@id,'FormItem') and child::label[text()='Members']]" + COMBOBOX_OPTION_FILTER_INPUT;

    @FindBy(xpath = TOOLBAR_DELETE_BUTTON)
    private WebElement toolbarDeleteButton;

    @FindBy(xpath = TOOLBAR_SAVE_BUTTON)
    private WebElement toolbarSaveButton;

    @FindBy(xpath = DESCRIPTION_INPUT)
    private WebElement descriptionInput;

    @FindBy(xpath = MEMBERS_OPTIONS_FILTER_INPUT)
    protected WebElement principalOptionsFilter;

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
        return GROUP_WIZARD_PANEL;
    }

    @Override
    public WizardPanel<Group> save()
    {
        boolean isSaveButtonEnabled = waitUntilElementEnabledNoException( By.xpath( TOOLBAR_SAVE_BUTTON ), 2l );
        if ( !isSaveButtonEnabled )
        {
            saveScreenshot( NameHelper.uniqueName( "err_save_button" ) );
            throw new SaveOrUpdateException( "Impossible to save, button 'Save' is not available!!" );
        }
        toolbarSaveButton.click();
        sleep( 700 );
        return this;
    }

    public ConfirmationDialog clickToolbarDelete()
    {
        toolbarDeleteButton.click();
        sleep( 1000 );
        ConfirmationDialog confirmationDialog = new ConfirmationDialog( getSession() );
        return confirmationDialog;
    }

    @Override
    public boolean isOpened()
    {
        return isElementDisplayed( GROUP_WIZARD_PANEL );
    }

    @Override
    public boolean isSaveButtonEnabled()
    {
        return getDisplayedElement( By.xpath( TOOLBAR_SAVE_BUTTON ) ).isEnabled();
    }

    @Override
    public WizardPanel<Group> typeData( final Group group )
    {
        waitElementClickable( By.name( "displayName" ), 2 );
        getLogger().info( "types displayName: " + group.getDisplayName() );
        sleep( 500 );
        if ( StringUtils.isNotEmpty( group.getName() ) )
        {
            getLogger().info( "types name: " + group.getName() );
            clearAndType( nameInput, group.getName().trim() );
        }
        if ( StringUtils.isNotEmpty( group.getDisplayName() ) )
        {
            getLogger().info( "types display name: " + group.getDisplayName() );
            clearAndType( displayNameInput, group.getDisplayName() );
        }
        if ( StringUtils.isNotEmpty( group.getDescription() ) )
        {
            getLogger().info( "types the description: " + group.getDescription() );
            clearAndType( descriptionInput, group.getDescription() );
        }
        if ( group.getMemberDisplayNames() != null )
        {
            addMembers( group.getMemberDisplayNames() );
        }
        saveScreenshot( group.getDisplayName() );
        return this;
    }

    public GroupWizardPanel addMembers( List<String> displayNames )
    {
        displayNames.stream().forEach( memberDisplayName -> addMember( memberDisplayName ) );
        return this;
    }

    public void addMember( String memberDisplayName )
    {
        clearAndType( principalOptionsFilter, memberDisplayName );
        sleep( 1000 );
        String rowCheckboxXpath = String.format( SLICK_ROW_BY_DISPLAY_NAME + "//label[child::input[@type='checkbox']]", memberDisplayName );
        if ( !isElementDisplayed( By.xpath( rowCheckboxXpath ) ) )
        {
            saveScreenshot( "err_" + memberDisplayName );
            throw new TestFrameworkException( "principal was not found! " + memberDisplayName );
        }
        findElement( By.xpath( rowCheckboxXpath ) ).click();
        principalOptionsFilter.sendKeys( Keys.ENTER );
        sleep( 300 );
    }

    public GroupWizardPanel typeDisplayName( String displayName )
    {
        clearAndType( displayNameInput, displayName );
        return this;
    }

    public GroupWizardPanel typeName( String name )
    {
        clearAndType( nameInput, name );
        return this;
    }

    public List<String> getMembersDisplayNames()
    {
        return getDisplayedStrings( By.xpath( MEMBERS_FORM + H6_DISPLAY_NAME ) );
    }

    public GroupWizardPanel removeMember( String displayName )
    {
        String removeButton =
            MEMBERS_FORM + "//div[contains(@class,'principal-selected-options-view') and descendant::h6[contains(@class,'main-name')]]" +
                ICON_REMOVE;
        if ( !isElementDisplayed( removeButton ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_member" + displayName ) );
            throw new TestFrameworkException( "member was not found! " + displayName );
        }
        getDisplayedElement( By.xpath( removeButton ) ).click();
        sleep( 300 );
        return this;
    }

    @Override
    public WizardPanel<Group> waitUntilWizardOpened()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( GROUP_WIZARD_PANEL ), Application.EXPLICIT_NORMAL );
        findElements( By.xpath( GROUP_WIZARD_PANEL ) );
        if ( !result )
        {
            saveScreenshot( "err_group_wizard" );
            throw new TestFrameworkException( "UserWizard was not showed!" );
        }
        return this;
    }

    public boolean isDeleteButtonEnabled()
    {
        return toolbarDeleteButton.isEnabled();
    }

    public String getDescription()
    {
        return descriptionInput.getAttribute( "value" );
    }
}
