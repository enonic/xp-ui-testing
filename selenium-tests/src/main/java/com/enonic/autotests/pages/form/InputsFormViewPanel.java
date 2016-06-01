package com.enonic.autotests.pages.form;


import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

public class InputsFormViewPanel
    extends FormViewPanel

{
    private ComboBoxFormViewPanel comboBoxFormViewPanel;

    private DoubleFormViewPanel doubleFormViewPanel;

    private GeoPointFormViewPanel geoPointFormViewPanel;

    private ImageSelectorFormViewPanel imageSelectorFormViewPanel;

    private SingleSelectorRadioFormView singleSelectorRadioFormView;

    private TextLine0_1_FormViewPanel textLineFormViewPanel;

    private LongFormViewPanel longFormViewPanel;

    private CheckBoxFormViewPanel checkBoxFormViewPanel;

    private RelationshipFormView relationshipFormView;

    public InputsFormViewPanel( TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        getComboBoxFormViewPanel().type( data );
        getDoubleFormViewPanel().type( data );
        getSingleSelectorRadioFormView().type( data );
        getGeoPointFormViewPanel().type( data );
        getTextLineFormViewPanel().type( data );
        getLongFormViewPanel().type( data );
        getCheckBoxFormViewPanel().type( data );
        getImageSelectorFormViewPanel().type( data );
        getRelationshipFormView().type( data );
        return this;
    }

    public RelationshipFormView getRelationshipFormView()
    {
        if ( relationshipFormView == null )
        {
            relationshipFormView = new RelationshipFormView( getSession() );
        }
        return relationshipFormView;
    }

    public ImageSelectorFormViewPanel getImageSelectorFormViewPanel()
    {
        if ( imageSelectorFormViewPanel == null )
        {
            imageSelectorFormViewPanel = new ImageSelectorFormViewPanel( getSession() );
        }
        return imageSelectorFormViewPanel;
    }

    public CheckBoxFormViewPanel getCheckBoxFormViewPanel()
    {
        if ( checkBoxFormViewPanel == null )
        {
            checkBoxFormViewPanel = new CheckBoxFormViewPanel( getSession() );
        }
        return checkBoxFormViewPanel;
    }

    public DoubleFormViewPanel getDoubleFormViewPanel()
    {
        if ( doubleFormViewPanel == null )
        {
            doubleFormViewPanel = new DoubleFormViewPanel( getSession() );
        }
        return doubleFormViewPanel;
    }

    public TextLine0_1_FormViewPanel getTextLineFormViewPanel()
    {
        if ( textLineFormViewPanel == null )
        {
            textLineFormViewPanel = new TextLine0_1_FormViewPanel( getSession() );
        }
        return textLineFormViewPanel;
    }

    public ComboBoxFormViewPanel getComboBoxFormViewPanel()
    {
        if ( comboBoxFormViewPanel == null )
        {
            comboBoxFormViewPanel = new ComboBoxFormViewPanel( getSession() );
        }
        return comboBoxFormViewPanel;
    }

    public LongFormViewPanel getLongFormViewPanel()
    {
        if ( longFormViewPanel == null )
        {
            longFormViewPanel = new LongFormViewPanel( getSession() );
        }
        return longFormViewPanel;
    }

    public SingleSelectorRadioFormView getSingleSelectorRadioFormView()
    {
        if ( singleSelectorRadioFormView == null )
        {
            singleSelectorRadioFormView = new SingleSelectorRadioFormView( getSession() );
        }
        return singleSelectorRadioFormView;
    }

    public GeoPointFormViewPanel getGeoPointFormViewPanel()
    {
        if ( geoPointFormViewPanel == null )
        {
            geoPointFormViewPanel = new GeoPointFormViewPanel( getSession() );
        }
        return geoPointFormViewPanel;
    }
}
