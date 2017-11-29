package com.enonic.autotests.pages.usermanager.wizardpanel;


import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.LoaderComboBox;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.vo.contentmanager.security.UserStoreAccess;
import com.enonic.autotests.vo.contentmanager.security.UserStoreAclEntry;
import com.enonic.autotests.vo.usermanager.UserStore;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class UserStoreWizardPanel
    extends WizardPanel<UserStore>
{
    public final String WIZARD_PANEL = "//div[contains(@id,'UserStoreWizardPanel')]";

    private final String TOOLBAR = "//div[contains(@id,'Toolbar')]";

    public final String TOOLBAR_SAVE_BUTTON = WIZARD_PANEL + TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Save']]";

    private final String PRINCIPAL_SELECTOR = WIZARD_PANEL + "//div[@name='principalSelector']";

    private final String PRINCIPALS_OPTIONS_FILTER_INPUT = PRINCIPAL_SELECTOR + COMBOBOX_OPTION_FILTER_INPUT;

    private final String ID_PROVIDER_COMBOBOX = "//div[contains(@id,'AuthApplicationComboBox')]";

    private final String ID_PROVIDER_COMBOBOX_DROPDOWN_HANDLER = ID_PROVIDER_COMBOBOX + "//button[contains(@id,'DropdownHandle')]";

    private final String PRINCIPAL_COMBOBOX_DROPDOWN_HANDLE = PRINCIPAL_SELECTOR + "//button[contains(@id,'DropdownHandle')]";

    private final String ID_PROVIDER_OPTIONS_FILTER_INPUT = ID_PROVIDER_COMBOBOX + COMBOBOX_OPTION_FILTER_INPUT;

    private final String TOOLBAR_DELETE_BUTTON =
        WIZARD_PANEL + TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Delete']]";

    private final String DESCRIPTION_INPUT = WIZARD_PANEL + "//div[@class='form-view']//input[contains(@id,'TextInput')]";

    private final String SELECTED_ID_PROVIDER_VIEW = WIZARD_PANEL + "//div[contains(@id,'AuthApplicationSelectedOptionView')]";

    private final String SELECTED_ID_PROVIDER_DISPLAY_NAME = SELECTED_ID_PROVIDER_VIEW + H6_DISPLAY_NAME;

    private String REMOVE_SELECTED_ID_PROVIDER_BUTTON =
        "//div[contains(@id,'AuthApplicationSelectedOptionView') and descendant::h6[contains(@class,'main-name') and contains(.,'%s')]]//a[contains(@class,'remove')]";

    private final String PERMISSIONS_LIST = "//ul[contains(@id,'UserStoreACESelectedOptionsView')]";

    private final String SELECTED_ACE_ITEMS = PERMISSIONS_LIST + "//div[contains(@id,'UserStoreACESelectedOptionView')]";

    private final String SELECTED_ACE_ITEM_BY_DISPLAY_NAME = PERMISSIONS_LIST +
        "//div[contains(@id,'UserStoreACESelectedOptionView') and descendant::h6[contains(@class,'main-name') and contains(.,'%s')]]";

    @FindBy(xpath = TOOLBAR_SAVE_BUTTON)
    protected WebElement toolbarSaveButton;

    @FindBy(xpath = TOOLBAR_DELETE_BUTTON)
    private WebElement toolbarDeleteButton;

    @FindBy(xpath = DESCRIPTION_INPUT)
    private WebElement descriptionInput;

    @FindBy(xpath = ID_PROVIDER_OPTIONS_FILTER_INPUT)
    protected WebElement idProviderOptiosnFilterInput;

    @FindBy(xpath = PRINCIPALS_OPTIONS_FILTER_INPUT)
    protected WebElement principalsOptionsFilterInput;

    @FindBy(xpath = PRINCIPAL_COMBOBOX_DROPDOWN_HANDLE)
    protected WebElement principalComboBoxDropDownHandle;

    /**
     * The constructor.
     *
     * @param session
     */
    public UserStoreWizardPanel( TestSession session )
    {
        super( session );
    }

    public UserStoreWizardPanel selectIdProvider( String providerName )
    {
        getDisplayedElement( By.xpath( ID_PROVIDER_COMBOBOX_DROPDOWN_HANDLER ) ).click();
        clearAndType( idProviderOptiosnFilterInput, providerName );
        sleep( 2000 );
        LoaderComboBox loaderComboBox = new LoaderComboBox( getSession() );
        loaderComboBox.selectOption( providerName );
        return this;
    }

    public UserStoreWizardPanel removeIdProvider( String idProviderDisplayName )
    {
        if ( !isRemoveSelectedIdProviderButtonDisplayed( idProviderDisplayName ) )
        {
            saveScreenshot( "err_remove_provider_button" );
            throw new TestFrameworkException( "remove button for " + idProviderDisplayName + "was not found!" );
        }
        String removeButtonXpath = String.format( REMOVE_SELECTED_ID_PROVIDER_BUTTON, idProviderDisplayName );
        getDisplayedElement( By.xpath( removeButtonXpath ) ).click();
        sleep( 400 );
        return this;
    }

    public UserStoreWizardPanel removePermission( String principalDisplayName )
    {
        String removeEntryButton = String.format( SELECTED_ACE_ITEM_BY_DISPLAY_NAME + ICON_REMOVE, principalDisplayName );
        if ( !isElementDisplayed( removeEntryButton ) )
        {
            saveScreenshot( "err_remove_acl_entry" );
            throw new TestFrameworkException( "button 'remove acl-entry' was not found!" );
        }
        getDisplayedElement( By.xpath( removeEntryButton ) ).click();
        sleep( 500 );
        return this;
    }

    public boolean isRemoveSelectedIdProviderButtonDisplayed( String idProviderDisplayName )
    {
        String removeButtonXpath = String.format( REMOVE_SELECTED_ID_PROVIDER_BUTTON, idProviderDisplayName );
        return isElementDisplayed( removeButtonXpath );
    }

    /**
     * types a display name of role or user and select a principal from the drop down menu
     */
    public UserStoreWizardPanel addPrincipal( String principalDisplayName )
    {
        clearAndType( principalsOptionsFilterInput, principalDisplayName );
        sleep( 400 );
        LoaderComboBox loaderComboBox = new LoaderComboBox( getSession() );
        loaderComboBox.selectOption( principalDisplayName );
        sleep( 500 );
        return this;
    }

    public UserStoreWizardPanel clearPrincipalOptionsFilterInput()
    {
        principalsOptionsFilterInput.clear();
        return this;
    }

    public UserStoreWizardPanel clickOnPrincipalComboBoxDropDownHandle()
    {
        principalComboBoxDropDownHandle.click();
        sleep( 200 );
        return this;
    }

    public boolean isAclEntryReadOnly( String principalDisplayName )
    {
        String selectedAclEntry = String.format( SELECTED_ACE_ITEM_BY_DISPLAY_NAME, principalDisplayName );
        WebElement entry = getDisplayedElement( By.xpath( selectedAclEntry ) );
        return entry.getAttribute( "class" ).contains( "readonly" );
    }

    /**
     * gets list of acl-entries from the UI
     */
    public List<UserStoreAclEntry> getPermissions()
    {
        return findElements( By.xpath( SELECTED_ACE_ITEMS ) ).stream().map( e -> buildAclEntry( e ) ).collect( Collectors.toList() );
    }

    public boolean isPermissionDisplayed( String principalDisplayName )
    {
        List<UserStoreAclEntry> permissions = getPermissions();
        return permissions.stream().anyMatch( entry -> entry.getPrincipalDisplayName().equals( principalDisplayName ) );
    }

    private UserStoreAclEntry buildAclEntry( WebElement itemView )
    {
        String principalName = itemView.findElement( By.xpath( "." + H6_DISPLAY_NAME ) ).getText();
        String userStoreAccess =
            itemView.findElement( By.xpath( "." + "//div[contains(@id,'UserStoreAccessSelector')]//a[@class='label']" ) ).getText();
        UserStoreAccess access = UserStoreAccess.findByValue( userStoreAccess );
        return UserStoreAclEntry.builder().principalName( principalName ).access( access ).build();
    }

    @Override
    protected String getWizardDivXpath()
    {
        return WIZARD_PANEL;
    }

    @Override
    public WizardPanel<UserStore> save()
    {
        toolbarSaveButton.click();
        sleep( 1500 );
        return this;
    }

    @Override
    public WizardPanel<UserStore> typeData( final UserStore userStore )
    {
        waitElementClickable( By.name( "displayName" ), 2 );
        clearAndType( displayNameInput, userStore.getDisplayName() );
        sleep( 500 );
        if ( StringUtils.isNotEmpty( userStore.getName() ) )
        {
            waitElementClickable( By.name( "name" ), 2 );
            clearAndType( nameInput, userStore.getName() );
        }
        if ( StringUtils.isNotEmpty( userStore.getDescription() ) )
        {
            getLogger().info( "types the description: " + userStore.getDescription() );
            clearAndType( descriptionInput, userStore.getDescription() );
        }
        if ( StringUtils.isNotEmpty( userStore.getIdProviderDisplayName() ) )
        {
            selectIdProvider( userStore.getIdProviderDisplayName() );
        }
        saveScreenshot( userStore.getDisplayName() );

        if ( userStore.getAclEntries() != null )
        {
            userStore.getAclEntries().stream().forEach( entry -> addPrincipal( entry.getPrincipalDisplayName() ) );
        }
        sleep( 500 );
        return this;
    }


    public String getStoreNameInputValue()
    {
        return nameInput.getAttribute( "value" );
    }

    public String getDescriptionValue()
    {
        return descriptionInput.getAttribute( "value" );
    }

    public boolean isIdProviderSelected()
    {
        return isElementDisplayed( SELECTED_ID_PROVIDER_VIEW );
    }

    public String getIdProviderDisplayName()
    {
        return getDisplayedString( SELECTED_ID_PROVIDER_DISPLAY_NAME );
    }

    public boolean isSelectorForIdProviderDisplayed()
    {
        return idProviderOptiosnFilterInput.isDisplayed();
    }

    public boolean isDescriptionInputDisplayed()
    {
        return descriptionInput.isDisplayed();
    }

    public boolean isPermissionsSelectorDisplayed()
    {
        return principalsOptionsFilterInput.isDisplayed();
    }

    public UserStoreWizardPanel typeDisplayName( String displayName )
    {
        clearAndType( displayNameInput, displayName );
        return this;
    }

    public UserStoreWizardPanel typeName( String name )
    {
        clearAndType( nameInput, name );
        return this;
    }

    @Override
    public boolean isSaveButtonEnabled()
    {
        return waitUntilElementEnabledNoException( By.xpath( TOOLBAR_SAVE_BUTTON ), Application.EXPLICIT_NORMAL );
    }

    @Override
    public boolean isOpened()
    {
        return toolbarSaveButton.isDisplayed();
    }

    @Override
    public UserStoreWizardPanel waitUntilWizardOpened()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( WIZARD_PANEL ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            saveScreenshot( NameHelper.uniqueName( "err_us_wizard" ) );
            throw new TestFrameworkException( "UserStoreWizard was not shown!" );
        }
        return this;
    }

    @Override
    public boolean isDeleteButtonEnabled()
    {
        return toolbarDeleteButton.isEnabled();
    }

    @Override
    public ConfirmationDialog clickToolbarDelete()
    {
        toolbarDeleteButton.click();
        sleep( 500 );
        ConfirmationDialog confirmationDialog = new ConfirmationDialog( getSession() );
        return confirmationDialog;
    }
}

