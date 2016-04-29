package com.enonic.autotests.pages.contentmanager.browsepanel;


import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

public class ContentInfoWidget
    extends Application
{
    private final String STATUS_TEXT = ContentDetailsPanel.DETAILS_PANEL + "//div[contains(@id,'StatusWidgetItemView')]/span";

    private final String PROPERTIES_DL = "//div[contains(@id,'PropertiesWidgetItemView')]/dl";

    private final String PROPERTIES_DT = PROPERTIES_DL + "//dt";

    private final String PROPERTIES_DD = PROPERTIES_DL + "//dd";

    public ContentInfoWidget( final TestSession session )
    {
        super( session );
    }

    public String getContentStatus()
    {
        if ( !isElementDisplayed( STATUS_TEXT ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_status" ) );
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
