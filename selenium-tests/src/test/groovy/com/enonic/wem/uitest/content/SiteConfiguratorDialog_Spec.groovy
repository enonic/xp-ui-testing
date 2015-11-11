package com.enonic.wem.uitest.content

import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class SiteConfiguratorDialog_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    def "GIVEN creating new Site based on 'Simple site'  WHEN saved and wizard closed THEN new site should be present"()
    {
        given:
        SITE = buildSimpleSiteApp();
        when: "data typed and saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData( SITE ).save().close(
            SITE.getDisplayName() );

        then: "new site should be present"
        contentBrowsePanel.exists( SITE.getName() );
    }
}
