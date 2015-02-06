package com.enonic.xp.simplesite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

import com.enonic.wem.api.content.ContentPath;
import com.enonic.wem.api.content.ContentService;
import com.enonic.wem.api.content.CreateContentParams;
import com.enonic.wem.api.content.CreateMediaParams;
import com.enonic.wem.api.data.PropertyTree;
import com.enonic.wem.api.form.Form;
import com.enonic.wem.api.form.Input;
import com.enonic.wem.api.form.inputtype.InputTypes;
import com.enonic.wem.api.initializer.DataInitializer;
import com.enonic.wem.api.schema.content.ContentTypeName;
import com.enonic.wem.api.schema.content.ContentTypeService;
import com.enonic.wem.api.security.PrincipalKey;
import com.enonic.wem.api.security.SecurityService;


public class Initializer
    implements DataInitializer
{
    private final static Logger LOG = LoggerFactory.getLogger( Initializer.class );

    private static final String[] FOLDER_IMAGES =
        {"bro.jpg", "Pop_02.jpg", "Pop_03.jpg", "Pop_04.jpg", "seng.jpg", "foss.jpg", "telk.png", "geek.png", "enterprise.png", "item1.png",
            "item2.png", "team1.png", "team2.png"};


    private static final Form MEDIA_IMAGE_FORM = createMediaImageForm();

    private static final String IMAGE_ARCHIVE_PATH_ELEMENT = "imagearchive";


    private ContentService contentService;

    private ContentTypeService contentTypeService;

    private SecurityService securityService;

    @Override
    public void initialize()
        throws Exception
    {
        createImages();
    }

    private boolean hasContent( final ContentPath path )
    {
        try
        {
            return this.contentService.getByPath( path ) != null;
        }
        catch ( final Exception e )
        {
            return false;
        }
    }

    private void createImages()
        throws Exception
    {
        final ContentPath imageArchivePath = ContentPath.from( ContentPath.ROOT, IMAGE_ARCHIVE_PATH_ELEMENT );
        if ( hasContent( imageArchivePath ) )
        {
            LOG.info( "Already initialized with data. Skipping." );
            return;
        }

        LOG.info( "Initializing demo content..." );
        final long tm = System.currentTimeMillis();

        try
        {
            doCreateImages();
        }
        finally
        {
            LOG.info( "Initialized demo content in " + ( System.currentTimeMillis() - tm ) + " ms" );
        }

    }

    private void doCreateImages()
        throws Exception
    {

        final ContentPath imageArchivePath = contentService.create( createFolder() ).getPath();

        for ( final String fileName : FOLDER_IMAGES )
        {
            createImageContent( imageArchivePath, fileName );
        }

    }

    private void createImageContent( final ContentPath parent, final String fileName )
        throws Exception
    {

        final byte[] bytes = loadImageFileAsBytes( fileName );
        if ( bytes == null )
        {
            return;
        }

        final CreateMediaParams params = new CreateMediaParams().
            mimeType( "image/jpeg" ).
            name( fileName ).
            parent( parent ).byteSource( ByteSource.wrap( bytes ) );
        contentService.create( params ).getId();
    }

    private byte[] loadImageFileAsBytes( final String fileName )
    {
        final String filePath = "/images/" + fileName;

        try
        {
            return Resources.toByteArray( getClass().getResource( filePath ) );
        }
        catch ( Exception e )
        {
            return null;
        }
    }

    private CreateContentParams createFolder()
    {

        return CreateContentParams.create().

            type( ContentTypeName.templateFolder() ).parent( ContentPath.ROOT ).displayName( "Simple Page Images" ).
            requireValid( true ).owner( PrincipalKey.ofAnonymous() ).
            contentData( new PropertyTree() ).type( ContentTypeName.folder() ).
            build();

    }

    private static Form createMediaImageForm()

    {
        return Form.newForm().
            addFormItem( Input.newInput().name( "image" ).
                inputType( InputTypes.IMAGE_UPLOADER ).build() ).
            addFormItem( Input.newInput().name( "mimeType" ).
                inputType( InputTypes.TEXT_LINE ).
                label( "Mime type" ).
                occurrences( 1, 1 ).
                build() ).

            build();
    }

    public void setContentService( final ContentService contentService )
    {
        this.contentService = contentService;
    }

    public void setContentTypeService( final ContentTypeService contentTypeService )
    {
        this.contentTypeService = contentTypeService;
    }

    public void setSecurityService( final SecurityService securityService )
    {
        this.securityService = securityService;
    }

}
