package com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel;


import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;

/**
 * 'Details Info Widget' contains  Status view, User Access View,Properties View, Attachments View
 */
public class ContentInfoWidget
    extends Application
{
    private final String CONTAINER = "//div[contains(@id,WidgetView) and contains(@class,'properties-widget')]";

    private final String STATUS_TEXT = CONTAINER + "//div[contains(@id,'StatusWidgetItemView')]/span";

    private final String PROPERTIES_DL = "//div[contains(@id,'PropertiesWidgetItemView')]/dl";

    private final String PROPERTIES_DT = CONTAINER + PROPERTIES_DL + "//dt";

    private final String PROPERTIES_DD = CONTAINER + PROPERTIES_DL + "//dd";

    public ContentInfoWidget( final TestSession session )
    {
        super( session );
    }

    public boolean isDisplayed()
    {
        return waitUntilVisibleNoException( By.xpath( CONTAINER ), 2 );
    }

    public String getContentStatus()
    {
        if ( !isElementDisplayed( STATUS_TEXT ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_status" ) );
            throw new TestFrameworkException( "content info widget: status was not found!" );
        }
        return getDisplayedString( STATUS_TEXT );
    }

    public HashMap<String, String> getContentProperties()
    {
        HashMap<String, String> props = new HashMap<>();
        List<String> terms = findElements( By.xpath( PROPERTIES_DT ) ).stream().map( e -> e.getText() ).filter( e -> !e.isEmpty() ).collect(
            Collectors.toList() );
        List<String> descriptions =
            findElements( By.xpath( PROPERTIES_DD ) ).stream().map( e -> e.getText() ).collect( Collectors.toList() );
        for ( int i = 0; i < terms.size(); i++ )
        {
            props.put( descriptions.get( i ).substring( 0, descriptions.get( i ).indexOf( ":" ) ), terms.get( i ) );
        }
        return props;
    }
}
