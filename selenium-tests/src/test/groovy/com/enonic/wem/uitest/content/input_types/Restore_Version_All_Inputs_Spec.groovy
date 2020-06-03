package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.*
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
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

    def "GIVEN content with input types has been added WHEN version paqnel has been opened THEN two versions should be present in versions-panel"()
    {
        given: "content with input types has been added"
        ALL_INPUTS_CONTENT = buildContent( "types", INITIAL_DISPLAY_NAME );
        ContentWizardPanel wizard = selectSitePressNew( ALL_INPUTS_CONTENT.getContentTypeName() );
        wizard.typeData( ALL_INPUTS_CONTENT );
        saveScreenshot( "all_inputs_typed" );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content has been selected and version history opened"
        contentBrowsePanel.doClearSelection();
        findAndSelectContent( ALL_INPUTS_CONTENT.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        then: "2 versions should be present on the widget by default(the content is just created)"
        allContentVersionsView.getAllVersions().size() == 2;
    }

    def "GIVEN existing content is opened WHEN values in inputs have been changed AND the content has been saved THEN number of versions should be increased by 1"()
    {
        given:
        ContentWizardPanel wizard = findAndSelectContent( ALL_INPUTS_CONTENT.getName() ).clickToolbarEdit();
        InputsFormViewPanel formView = new InputsFormViewPanel( getSession() );

        when: "all data in inputs are changed"
        formView.getComboBoxFormViewPanel().clickOnLastRemoveButton().typeNameOfOptionAndSelectOption( COMBOBOX_OPTION_V2 );
        formView.getCheckBoxFormViewPanel().clickOnCheckBox(  );
        formView.getLongFormViewPanel().typeLongValue( LONG_V2 );
        formView.getDoubleFormViewPanel().typeDoubleValue( DOUBLE_V2 );
        formView.getGeoPointFormViewPanel().typeGeoPoint( GEO_POINT_V2 );
        wizard.doScrollPanel( 250 );
        formView.getSingleSelectorRadioFormView().selectOption( RADIO_OPTION_V2 );
        formView.getTextLineFormViewPanel().typeText( TEXT_LINE_V2 );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        then: "total amount of versions is increased"
        allContentVersionsView.getAllVersions().size() == 3;
    }

    def "GIVEN existing content WHEN previous version has been restored THEN expected values should be present in all inputs"()
    {
        given: "existing content with 3 versions"
        findAndSelectContent( ALL_INPUTS_CONTENT.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "the previous version has been restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion(  );
        saveScreenshot( "all_inputs_restored" );

        and: "content is reopened"
        contentBrowsePanel.clickToolbarEdit();

        then: "expected values should be present in all inputs"
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
        addImageSelectorData( data, BOOK_IMAGE_DISPLAY_NAME )
        addRelationshipData( data, "Templates" )
        Content content = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( displayName ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "all-inputs" ).data( data ).
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