package com.enonic.autotests.pages.form.optionset;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.form.FormViewPanel;
import com.enonic.xp.data.PropertyTree;

/**
 * Created on 5/12/2017.
 */
public class OptionSetFormView
    extends FormViewPanel
{

    private SingleSelectionOptionSetView singleSelectionOptionSet;

    private MultiSelectionOptionSetView multiSelectionOptionSet;

    public OptionSetFormView( final TestSession session )
    {
        super( session );
    }

    public SingleSelectionOptionSetView getSingleSelectionOptionSet()
    {
        if ( singleSelectionOptionSet == null )
        {
            singleSelectionOptionSet = new SingleSelectionOptionSetView( getSession() );
        }
        return singleSelectionOptionSet;
    }

    public MultiSelectionOptionSetView getMultiSelectionOptionSet()
    {
        if ( multiSelectionOptionSet == null )
        {
            multiSelectionOptionSet = new MultiSelectionOptionSetView( getSession() );
        }
        return multiSelectionOptionSet;
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {

        return this;
    }
}
