package com.enonic.wem.uitest.content.liveedit

import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class BasePageEditorFeaturesSpec
    extends BaseContentSpec
{
    @Shared
    String COUNTRY_SITE_HTML_HEADER = "<title>Country Region</title>";

    @Shared
    String COUNTRY_REGION_PAGE_CONTROLLER = "Country Region";

    @Shared
    String COUNTRY_REGION = "Country Region";
}
