package com.enonic.wem.modules.xeon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.wem.api.content.Content;
import com.enonic.wem.api.content.ContentPath;
import com.enonic.wem.api.content.ContentService;
import com.enonic.wem.api.content.page.CreatePageTemplateParams;
import com.enonic.wem.api.content.page.DescriptorKey;
import com.enonic.wem.api.content.page.PageRegions;
import com.enonic.wem.api.content.page.PageTemplateService;
import com.enonic.wem.api.content.page.region.ImageComponent;
import com.enonic.wem.api.content.page.region.LayoutComponent;
import com.enonic.wem.api.content.page.region.LayoutRegions;
import com.enonic.wem.api.content.page.region.PartComponent;
import com.enonic.wem.api.content.page.region.Region;
import com.enonic.wem.api.content.site.CreateSiteParams;
import com.enonic.wem.api.content.site.ModuleConfig;
import com.enonic.wem.api.content.site.ModuleConfigs;
import com.enonic.wem.api.content.site.Site;
import com.enonic.wem.api.data.PropertyTree;
import com.enonic.wem.api.initializer.DataInitializer;
import com.enonic.wem.api.module.ModuleKey;
import com.enonic.wem.api.schema.content.ContentTypeName;
import com.enonic.wem.api.schema.content.ContentTypeNames;
import com.enonic.wem.api.schema.content.ContentTypeService;

@SuppressWarnings("UnusedDeclaration")
public final class Initializer
    implements DataInitializer
{
    private final static Logger LOG = LoggerFactory.getLogger( Initializer.class );

    public static final ModuleKey THIS_MODULE = ModuleKey.from( Initializer.class );

    private ContentPath xeonFolder = ContentPath.from( "/xeon" );

    private ContentService contentService;

    private PageTemplateService pageTemplateService;

    private ContentTypeService contentTypeService;

    @Override
    public void initialize()
        throws Exception
    {
        LOG.info( "initialize...." );

        if ( !this.hasContent( xeonFolder ) )
        {
            final ModuleConfig moduleConfig = ModuleConfig.newModuleConfig().
                module( THIS_MODULE ).
                config( new PropertyTree() ).
                build();
            final ModuleConfigs moduleConfigs = ModuleConfigs.from( moduleConfig );

            final Site site = contentService.create( createSiteContent( "Xeon", "Xeon demo site.", moduleConfigs ) );

            createPageTemplateHomePage( site.getPath() );
            createPageTemplateBannerPage( site.getPath() );
            createPageTemplatePresonPage( site.getPath() );
        }
    }

    private CreateSiteParams createSiteContent( final String displayName, final String description, final ModuleConfigs moduleConfigs )
    {
        return new CreateSiteParams().
            moduleConfigs( moduleConfigs ).
            description( description ).
            displayName( displayName ).
            parent( ContentPath.ROOT );
    }

    private Content createPageTemplateHomePage( final ContentPath sitePath )
    {
        final ContentTypeNames supports = ContentTypeNames.from( ContentTypeName.site() );

        return pageTemplateService.create( new CreatePageTemplateParams().
            site( sitePath ).
            name( "home-page" ).
            displayName( "Home page" ).
            controller( DescriptorKey.from( THIS_MODULE, "apage" ) ).
            supports( supports ).
            pageConfig( new PropertyTree() ).
            pageRegions( PageRegions.newPageRegions().
                add( Region.newRegion().
                    name( "main" ).
                    add( PartComponent.newPartComponent().name( "Empty-part" ).build() ).
                    build() ).
                build() ) );
    }

    private Content createPageTemplateBannerPage( final ContentPath sitePath )
    {
        final ContentTypeNames supports = ContentTypeNames.from( ContentTypeName.site() );

        return pageTemplateService.create( new CreatePageTemplateParams().
            site( sitePath ).
            name( "banner-page" ).
            displayName( "Banner" ).
            controller( DescriptorKey.from( THIS_MODULE, "banner-page" ) ).
            supports( supports ).
            pageConfig( new PropertyTree() ).
            pageRegions( PageRegions.newPageRegions().
                add( Region.newRegion().
                    name( "main" ).
                    add( LayoutComponent.newLayoutComponent().name( "Layout-3-col" ).
                        descriptor( DescriptorKey.from( THIS_MODULE, "layout-3-col" ) ).
                        regions( LayoutRegions.newLayoutRegions().
                            add( Region.newRegion().name( "left" ).
                                add( ImageComponent.newImageComponent().name( "Image" ).build() ).
                                build() ).
                            add( Region.newRegion().name( "center" ).
                                add( ImageComponent.newImageComponent().name( "Image" ).build() ).
                                build() ).
                            add( Region.newRegion().name( "right" ).
                                add( ImageComponent.newImageComponent().name( "Image" ).build() ).
                                build() ).
                            build() ).
                        build() ).
                    add( PartComponent.newPartComponent().name( "mypart" ).build() ).
                    build() ).
                build() ) );
    }

    private Content createPageTemplatePresonPage( final ContentPath sitePath )
    {
        final ContentTypeNames supports = ContentTypeNames.from( ContentTypeName.from( THIS_MODULE, "person" ) );

        return pageTemplateService.create( new CreatePageTemplateParams().
            site( sitePath ).
            name( "person-page" ).
            displayName( "Person" ).
            controller( DescriptorKey.from( THIS_MODULE, "person" ) ).
            supports( supports ).
            pageConfig( new PropertyTree() ).
            pageRegions( PageRegions.newPageRegions().
                add( Region.newRegion().
                    name( "main" ).
                    add( PartComponent.newPartComponent().name( "Person" ).descriptor(
                        DescriptorKey.from( THIS_MODULE, "person" ) ).build() ).
                    build() ).
                build() ) );
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

    public void setContentService( final ContentService contentService )
    {
        this.contentService = contentService;
    }

    public void setPageTemplateService( final PageTemplateService pageTemplateService )
    {
        this.pageTemplateService = pageTemplateService;
    }

    public void setContentTypeService( final ContentTypeService contentTypeService )
    {
        this.contentTypeService = contentTypeService;
    }
}
