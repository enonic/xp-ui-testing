package com.enonic.xp.all_contenttypes;

import java.util.concurrent.Callable;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

import com.enonic.xp.content.ApplyContentPermissionsParams;
import com.enonic.xp.content.Content;
import com.enonic.xp.content.ContentPath;
import com.enonic.xp.content.ContentService;
import com.enonic.xp.content.CreateContentParams;
import com.enonic.xp.content.CreateMediaParams;
import com.enonic.xp.content.UpdateContentParams;
import com.enonic.xp.context.ContextAccessor;
import com.enonic.xp.context.ContextBuilder;
import com.enonic.xp.data.PropertyTree;
import com.enonic.xp.schema.content.ContentTypeName;
import com.enonic.xp.security.PrincipalKey;
import com.enonic.xp.security.RoleKeys;
import com.enonic.xp.security.User;
import com.enonic.xp.security.acl.AccessControlEntry;
import com.enonic.xp.security.acl.AccessControlList;
import com.enonic.xp.security.acl.Permission;
import com.enonic.xp.security.auth.AuthenticationInfo;

@Component(immediate = true)
public class Initializer
{
    private static final String[] FOLDER_IMAGES = {"book.jpg", "man.jpg", "man2.jpg", "fl.jpg", "nord.jpg", "whale.jpg", "hand.jpg"};
    //, "elephant.png", "dollar.png"

    private static final String FOLDER_NAME = "all-content-types-images";

    private static final String FOLDER_DISPLAY_NAME = "All Content types images";

    private static final AccessControlList PERMISSIONS =
        AccessControlList.of( AccessControlEntry.create().principal( PrincipalKey.ofAnonymous() ).allow( Permission.READ ).build(),
                              AccessControlEntry.create().principal( RoleKeys.EVERYONE ).allow( Permission.READ ).build(),
                              AccessControlEntry.create().principal( RoleKeys.AUTHENTICATED ).allowAll().build(),
                              AccessControlEntry.create().principal( RoleKeys.CONTENT_MANAGER_ADMIN ).allowAll().build() );

    private ContentService contentService;


    private final Logger LOG = LoggerFactory.getLogger( Initializer.class );

    @Activate
    public void initialize()
        throws Exception
    {
        runAs( RoleKeys.CONTENT_MANAGER_ADMIN, () -> {
            doInitialize();
            return null;
        } );
    }

    private void doInitialize()
        throws Exception
    {
        final ContentPath imagesPath = ContentPath.from( "/" + FOLDER_NAME );
        if ( hasContent( imagesPath ) )
        {
            return;
        }

        folderWithImage();

        // set permissions
        final Content moduleContent = contentService.getByPath( imagesPath );
        if ( moduleContent != null )
        {
            final UpdateContentParams setFeaturesPermissions = new UpdateContentParams().
                contentId( moduleContent.getId() ).
                editor( ( content ) -> {
                    content.permissions = PERMISSIONS;
                    content.inheritPermissions = false;
                } );
            contentService.update( setFeaturesPermissions );

            contentService.applyPermissions( ApplyContentPermissionsParams.create().
                contentId( moduleContent.getId() ).
                modifier( PrincipalKey.ofAnonymous() ).
                build() );
        }
    }

    private CreateContentParams.Builder makeFolder()
    {
        return CreateContentParams.create().
            owner( PrincipalKey.ofAnonymous() ).
            contentData( new PropertyTree() ).
            type( ContentTypeName.folder() ).
            inheritPermissions( true );
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

    @Reference
    public void setContentService( final ContentService contentService )
    {
        this.contentService = contentService;
    }

    private void folderWithImage()
        throws Exception
    {
        final ContentPath testFolderPath = ContentPath.from( "/" + FOLDER_NAME );
        if ( !hasContent( testFolderPath ) )
        {
            contentService.create( makeFolder().
                name( FOLDER_NAME ).
                displayName( FOLDER_DISPLAY_NAME ).
                parent( ContentPath.ROOT ).
                permissions( PERMISSIONS ).
                inheritPermissions( false ).
                build() );

            for ( final String fileName : FOLDER_IMAGES )
            {
                try
                {
                    createImageContent( testFolderPath, fileName );
                }
                finally
                {
                    LOG.info( "Initialized content for 'All content types'" );
                }
            }
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
        System.out.println( "content created!!  " + fileName );

    }

    private byte[] loadImageFileAsBytes( final String fileName )
    {
        final String filePath = "/app/images/" + fileName;

        try
        {
            return Resources.toByteArray( getClass().getResource( filePath ) );
        }
        catch ( Exception e )
        {
            return null;
        }
    }

    private <T> T runAs( final PrincipalKey role, final Callable<T> runnable )
    {
        final AuthenticationInfo authInfo = AuthenticationInfo.create().principals( role ).user( User.ANONYMOUS ).build();
        return ContextBuilder.from( ContextAccessor.current() ).authInfo( authInfo ).build().callWith( runnable );
    }
}

//    private final static Logger LOG = LoggerFactory.getLogger( Initializer.class );
//
//    private static final String[] FOLDER_IMAGES =
//        {"book.jpg", "man.jpg", "man2.jpg", "fl.jpg", "nord.jpg", "whale.jpg", "elephant.png", "dollar.png"};
//
//
//    private static final String FOLDER_DISPLAY_NAME = "All Content types images";
//
//    private static final String FOLDER_NAME = "all-content-types-images";
//
//    private ContentService contentService;
//
//    @Activate
//    public void initialize()
//        throws Exception
//    {
//        createImages();
//    }
//
//    private boolean hasContent( final ContentPath path )
//    {
//        try
//        {
//            LOG.info( "path is  " + path );
//            return this.contentService.getByPath( path ) != null;
//        }
//        catch ( final Exception e )
//        {
//            return false;
//        }
//    }
//
//    private void createImages()
//        throws Exception
//    {
//        final ContentPath imageArchivePath = ContentPath.from( ContentPath.ROOT, FOLDER_NAME );
//        if ( hasContent( imageArchivePath ) )
//        {
//            LOG.info( "Already initialized with data. Skipping." );
//            return;
//        }
//
//        LOG.info( "Initializing content for 'All content types selenium tests' " );
//        final long tm = System.currentTimeMillis();
//
//        try
//        {
//            doCreateImages();
//        }
//        finally
//        {
//            LOG.info( "Initialized content for 'All content types' in " + ( System.currentTimeMillis() - tm ) + " ms" );
//        }
//
//    }
//
//    private void doCreateImages()
//        throws Exception
//    {
//
//        final ContentPath imageArchivePath = contentService.create( createFolder() ).getPath();
//
//        for ( final String fileName : FOLDER_IMAGES )
//        {
//            createImageContent( imageArchivePath, fileName );
//        }
//
//    }
//
//    private void createImageContent( final ContentPath parent, final String fileName )
//        throws Exception
//    {
//
//        final byte[] bytes = loadImageFileAsBytes( fileName );
//        if ( bytes == null )
//        {
//            return;
//        }
//
//        final CreateMediaParams params = new CreateMediaParams().
//            mimeType( "image/jpeg" ).
//            name( fileName ).
//            parent( parent ).byteSource( ByteSource.wrap( bytes ) );
//        contentService.create( params ).getId();
//    }
//
//    private byte[] loadImageFileAsBytes( final String fileName )
//    {
//        final String filePath = "/cms/images/" + fileName;
//
//        try
//        {
//            return Resources.toByteArray( getClass().getResource( filePath ) );
//        }
//        catch ( Exception e )
//        {
//            return null;
//        }
//    }
//
//    private CreateContentParams createFolder()
//    {
//
//        return CreateContentParams.create().
//
//            type( ContentTypeName.templateFolder() ).parent( ContentPath.ROOT ).displayName( FOLDER_DISPLAY_NAME ).
//            requireValid( true ).owner( PrincipalKey.ofAnonymous() ).
//            contentData( new PropertyTree() ).type( ContentTypeName.folder() ).
//            build();
//
//    }
//
//    private <T> T runAs( final PrincipalKey role, final Callable<T> runnable )
//    {
//        final AuthenticationInfo authInfo = AuthenticationInfo.create().principals( role ).user( User.ANONYMOUS ).build();
//        return ContextBuilder.from( ContextAccessor.current() ).authInfo( authInfo ).build().callWith( runnable );
//    }
//    @Reference
//    public void setContentService( final ContentService contentService )
//    {
//        this.contentService = contentService;
//    }
//
//}
