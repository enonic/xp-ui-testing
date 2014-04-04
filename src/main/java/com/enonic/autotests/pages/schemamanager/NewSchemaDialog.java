package com.enonic.autotests.pages.schemamanager;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.BaseModalDialog;
import com.enonic.autotests.pages.schemamanager.wizardpanel.ContentTypeWizardPanel;
import com.enonic.autotests.pages.schemamanager.wizardpanel.MixinWizardPanel;
import com.enonic.autotests.pages.schemamanager.wizardpanel.RelationshipWizardPanel;
import com.enonic.autotests.pages.schemamanager.wizardpanel.SchemaWizardPanel;

/**
 *
 *
 */
public class NewSchemaDialog
    extends BaseModalDialog
{

    private String KIND = "//div[@class='modal-dialog new-schema-dialog']//h6[text()='%s']";

    /**
     * The Constructor.
     *
     * @param session
     */
    public NewSchemaDialog( TestSession session )
    {
        super( session );
    }

    /**
     * @param kind
     * @return
     */
    public SchemaWizardPanel<?> selectKind( String kind )
    {
        SchemaWizardPanel<?> wizard = null;
        String kindXpath = String.format( KIND, kind );
        boolean isPpresent = waitAndFind( By.xpath( kindXpath ) );

        if ( !isPpresent )
        {
            throw new TestFrameworkException( "The kind of content type" + kind + " was not found!!!" );
        }
        getDriver().findElement( By.xpath( kindXpath ) ).click();
        if ( kind.equals( SchemaKindUI.CONTENT_TYPE.getValue() ) )
        {
            wizard = new ContentTypeWizardPanel( getSession() );
        }
        else if ( kind.equals( SchemaKindUI.RELATIONSHIP_TYPE.getValue() ) )
        {
            wizard = new RelationshipWizardPanel( getSession() );
        }
        else if ( kind.equals( SchemaKindUI.MIXIN.getValue() ) )
        {
            wizard = new MixinWizardPanel( getSession() );
        }
        else
        {
            throw new TestFrameworkException( "NewContentDialog: wrong schema type" );
        }

        wizard.waitUntilWizardOpened();
        return wizard;
    }

    /**
     * @return
     */
    public boolean verifyIsOpened()
    {
        String title = "//div[@class='modal-dialog new-schema-dialog']/div[contains(.,'Select Kind')]";
        return waitUntilVisibleNoException( By.xpath( title ), 2 );
    }

}
