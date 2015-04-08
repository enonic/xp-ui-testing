package com.enonic.autotests.pages.contentmanager;


import java.util.List;

import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.xp.data.PropertyTree;

public class ContentUtils
{
    public static PropertyTree buildComboBoxData( int numberOfOptions )
    {
        PropertyTree data = new PropertyTree();

        switch ( numberOfOptions )
        {
            case 0:
                break;
            case 1:
                data.addString( "options", "option A" );
                break;
            case 2:
                data.addString( "options", "option A" );
                data.addString( "options", "option B" );
                break;
            case 3:
                data.addString( "options", "option A" );
                data.addString( "options", "option B" );
                data.addString( "options", "option C" );
                break;
            case 4:
                data.addString( "options", "option A" );
                data.addString( "options", "option B" );
                data.addString( "options", "option C" );
                data.addString( "options", "option D" );
                break;
            default:
                throw new TestFrameworkException( "data not implemented" );


        }
        return data;
    }

    public static PropertyTree buildSingleSelectionData( List<String> options )
    {
        PropertyTree data = new PropertyTree();
        if ( options != null )
        {
            data.addStrings( "options", options );
        }
        return data;
    }
}
