package com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;


public class PropertiesWidgetItemView
    extends Application

{
    private final String DIV_CONTAINER = "//div[contains(@id,'PropertiesWidgetItemView')]";

    private final String TYPE_PROPERTY = DIV_CONTAINER + "//dd[contains(.,'Type:')]/following-sibling::dt[1]";

    private final String OWNER_PROPERTY = DIV_CONTAINER + "//dd[contains(.,'Owner:')]/following-sibling::dt[1]";

    private final String APPLICATION_NAME_PROPERTY = DIV_CONTAINER + "//dd[contains(.,'Application:')]/following-sibling::dt[1]";

    private final String ID_PROPERTY = DIV_CONTAINER + "//dd[contains(.,'Id:')]/following-sibling::dt[1]";

    private final String MODIFIED_PROPERTY = DIV_CONTAINER + "//dd[contains(.,'Modified:')]/following-sibling::dt[1]";

    private final String CREATED_PROPERTY = DIV_CONTAINER + "//dd[contains(.,'Created:')]/following-sibling::dt[1]";

    private final String LANGUAGE_PROPERTY = DIV_CONTAINER + "//dd[contains(.,'Language:')]/following-sibling::dt[1]";

    public PropertiesWidgetItemView( final TestSession session )
    {
        super( session );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( DIV_CONTAINER );
    }

    public boolean isTypePresent()
    {
        return isElementDisplayed( TYPE_PROPERTY );
    }

    public boolean isLanguagePresent()
    {
        return isElementDisplayed( LANGUAGE_PROPERTY );
    }

    public boolean isIdPresent()
    {
        return isElementDisplayed( ID_PROPERTY );
    }

    public boolean isCreatedPresent()
    {
        return isElementDisplayed( CREATED_PROPERTY );
    }

    public boolean isIdModifiedPresent()
    {
        return isElementDisplayed( MODIFIED_PROPERTY );
    }

    public boolean isApplicationNamePresent()
    {
        return isElementDisplayed( APPLICATION_NAME_PROPERTY );
    }

    public boolean isOwnerPresent()
    {
        return isElementDisplayed( OWNER_PROPERTY );
    }

    public String getType()
    {
        return getDisplayedString( TYPE_PROPERTY );
    }

    public String getOwner()
    {
        if ( !isElementDisplayed( OWNER_PROPERTY ) )
        {
            saveScreenshot( "err_det_panel_owner" );
            throw new TestFrameworkException( "owner was not found on the details panel" );
        }
        return getDisplayedString( OWNER_PROPERTY );
    }

    public String getModified()
    {
        return getDisplayedString( MODIFIED_PROPERTY );
    }

    public String getCreated()
    {
        return getDisplayedString( CREATED_PROPERTY );
    }

    public String getId()
    {
        return getDisplayedString( ID_PROPERTY );
    }

    public String getApplicationName()
    {
        return getDisplayedString( APPLICATION_NAME_PROPERTY );
    }

    public String getLanguage()
    {
        return getDisplayedString( LANGUAGE_PROPERTY );
    }
}

