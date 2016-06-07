package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.*
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Restore_Version_All_Inputs_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content ALL_INPUTS_CONTENT;

    @Shared
    String INITIAL_DISPLAY_NAME = "restore-version-types";

    @Shared
    String NEW_DISPLAY_NAME = NameHelper.uniqueName( "restore-all" );

    @Shared
    String DATA_FOR_RELATIONSHIP = "server.bat";

    @Shared
    String TEXT_LINE_V1 = "version1";

    @Shared
    String TEXT_LINE_V2 = "version2";

    @Shared
    String GEO_POINT_V1 = "1,1";

    @Shared
    String GEO_POINT_V2 = "2,2";

    @Shared
    String LONG_V1 = "1";

    @Shared
    String LONG_V2 = "2";

    @Shared
    String DOUBLE_V1 = "1";

    @Shared
    String DOUBLE_V2 = "2";

    @Shared
    String RADIO_OPTION_V1 = "myOption 1";

    @Shared
    String RADIO_OPTION_V2 = "myOption 2";

    @Shared
    String COMBOBOX_OPTION_V1 = "myOption 1";

    @Shared
    String COMBOBOX_OPTION_V2 = "myOption 2";

    def "GIVEN adding of content with all input types WHEN content saved and version history opened THEN two versions are present in panel"()
    {
        given: "adding of content with input types"
        ALL_INPUTS_CONTENT = buildContent( "types", INITIAL_DISPLAY_NAME );
        ContentWizardPanel wizard = selectSiteOpenWizard( ALL_INPUTS_CONTENT.getContentTypeName() );
        wizard.typeData( ALL_INPUTS_CONTENT );
        TestUtils.saveScreenshot( getSession(), "all_inputs_typed" );
        wizard.save().close( INITIAL_DISPLAY_NAME );


        when: "display name of the folder changed"
        contentBrowsePanel.clickOnClearSelection();
        findAndSelectContent( ALL_INPUTS_CONTENT.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        then: "new 'version history item' appeared in the version-view"
        allContentVersionsView.getAllVersions().size() == 2;
    }

    def "GIVEN content with input types added WHEN all values changed THEN new version appears in the version history"()
    {
        given:
        ContentWizardPanel wizard = findAndSelectContent( ALL_INPUTS_CONTENT.getName() ).clickToolbarEdit();
        InputsFormViewPanel formView = new InputsFormViewPanel( getSession() );

        when:
        formView.getComboBoxFormViewPanel().clickOnLastRemoveButton().typeNameOfOptionAndSelectOption( COMBOBOX_OPTION_V2 );
        formView.getCheckBoxFormViewPanel().setChecked( false );
        formView.getLongFormViewPanel().typeLongValue( LONG_V2 );
        formView.getDoubleFormViewPanel().typeDoubleValue( DOUBLE_V2 );
        formView.getGeoPointFormViewPanel().typeGeoPoint( GEO_POINT_V2 );
        formView.getSingleSelectorRadioFormView().selectOption( RADIO_OPTION_V2 );
        formView.getTextLineFormViewPanel().typeText( TEXT_LINE_V2 );
        wizard.save().close( ALL_INPUTS_CONTENT.getDisplayName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        then:
        allContentVersionsView.getAllVersions().size() == 3;
    }

    def "GIVEN values in all inputs were updated WHEN previous version of content has been restored THEN correct values are present in the wizard"()
    {
        given: "content with updated values"
        findAndSelectContent( ALL_INPUTS_CONTENT.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "the previous version is restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        TestUtils.saveScreenshot( getSession(), "all_inputs_restored" );

        and: "content opened"
        contentBrowsePanel.clickToolbarEdit();

        then: "correct values are present on the wizard"
        InputsFormViewPanel formView = new InputsFormViewPanel( getSession() );
        formView.geoPointFormViewPanel.getGeoPointValue() == GEO_POINT_V1;

        and: "checkbox is checked"
        formView.getCheckBoxFormViewPanel().isChecked();

        and: "data for 'radio buttons' restored correctly"
        formView.getSingleSelectorRadioFormView().getSelectedOption() == COMBOBOX_OPTION_V1;

        and: "data for 'double input' restored correctly"
        formView.getDoubleFormViewPanel().getFirstDoubleInputValue() == DOUBLE_V1;

        and: "data for 'long input' restored correctly"
        formView.getLongFormViewPanel().getLongValue() == LONG_V1;

        and: "data for 'text line input' restored correctly"
        formView.getTextLineFormViewPanel().getTextLineValue() == TEXT_LINE_V1;
    }

    private Content buildContent( String name, String displayName )
    {
        PropertyTree data = new PropertyTree();
        addTextLineData( data, TEXT_LINE_V1 );
        addGeoPointData( data, GEO_POINT_V1 );
        addDoubleData( data, DOUBLE_V1 );
        addLongData( data, LONG_V1 );
        addComboboxData( data, COMBOBOX_OPTION_V1 );
        addRadioData( data, RADIO_OPTION_V1 );
        addCheckboxData( data, true );
        addImageSelectorData( data, BOOK_IMAGE_NAME )
        addRelationshipData( data, DATA_FOR_RELATIONSHIP )
        Content content = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( displayName ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":all-inputs" ).data( data ).
            build();
        return content;
    }

    private addDoubleData( PropertyTree data, String doubleValue )
    {
        data.addString( DoubleFormViewPanel.DOUBLE_VALUES, doubleValue );
    }

    private addRelationshipData( PropertyTree data, String... names )
    {
        data.addStrings( RelationshipFormView.RELATIONSHIPS_PROPERTY, names );
    }


    private addImageSelectorData( PropertyTree data, String... images )
    {
        data.addStrings( ImageSelectorFormViewPanel.IMAGES_PROPERTY, images );
    }

    private addCheckboxData( PropertyTree data, boolean booleanValue )
    {
        data.addBoolean( CheckBoxFormViewPanel.CHECKBOX_PROPERTY, booleanValue );
    }

    private addRadioData( PropertyTree data, String option )
    {
        data.addString( SingleSelectorRadioFormView.RADIO_OPTION, option );
    }

    private addComboboxData( PropertyTree data, String option )
    {
        data.addString( "options", option );
    }

    private addLongData( PropertyTree data, String longValue )
    {
        data.addString( LongFormViewPanel.LONG_VALUE, longValue );
    }

    private addTextLineData( PropertyTree data, String text )
    {
        data.addString( TextLine0_1_FormViewPanel.TEXT_INPUT_PROPERTY, text );
    }

    private addGeoPointData( PropertyTree data, String geopoint )
    {
        data.addString( GeoPointFormViewPanel.GEO_POINT_PROPERTY, geopoint );
    }
}