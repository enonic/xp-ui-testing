package com.enonic.wem.uitest.user

import com.enonic.autotests.vo.usermanager.UserStore

class UserTestUtils
{
    public static UserStore buildUserStoreWithDisplayName( String displayName )
    {
        return UserStore.builder().displayName( displayName ).build();

    }
}
