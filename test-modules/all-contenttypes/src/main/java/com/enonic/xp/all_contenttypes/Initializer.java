package com.enonic.xp.all_contenttypes;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;



import com.enonic.xp.content.ContentPath;
import com.enonic.xp.content.ContentService;
import com.enonic.xp.content.CreateContentParams;
import com.enonic.xp.content.CreateMediaParams;
import com.enonic.xp.data.PropertyTree;
import com.enonic.xp.schema.content.ContentTypeName;
import com.enonic.xp.security.PrincipalKey;

@Component(immediate = true)
public class Initializer
{
    private final static Logger LOG = LoggerFactory.getLogger( Initializer.class );

    private static final String[] FOLDER_IMAGES =
        {"book.jpg", "man.jpg", "man2.jpg", "fl.jpg", "nord.jpg", "whale.jpg", "elephant.png", "dollar.png"};


    private static final String FOLDER_NAME = "All Content types images";

    private ContentService contentService;

    @Activate
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
        final ContentPath imageArchivePath = ContentPath.from( ContentPath.ROOT, FOLDER_NAME );
        if ( hasContent( imageArchivePath ) )
        {
            LOG.info( "Already initialized with data. Skipping." );
            return;
        }

        LOG.info( "Initializing content for 'All content types selenium tests' " );
        final long tm = System.currentTimeMillis();

        try
        {
            doCreateImages();
        }
        finally
        {
            LOG.info( "Initialized content for 'All content types' in " + ( System.currentTimeMillis() - tm ) + " ms" );
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
        final String filePath = "/cms/images/" + fileName;

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

            type( ContentTypeName.templateFolder() ).parent( ContentPath.ROOT ).displayName( FOLDER_NAME ).
            requireValid( true ).owner( PrincipalKey.ofAnonymous() ).
            contentData( new PropertyTree() ).type( ContentTypeName.folder() ).
            build();

    }


    @Reference
    public void setContentService( final ContentService contentService )
    {
        this.contentService = contentService;
    }

}