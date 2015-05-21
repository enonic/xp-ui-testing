package com.enonic.wem.uitest.user

import com.enonic.autotests.vo.usermanager.User
import com.enonic.autotests.vo.usermanager.UserStore

class UserTestUtils
{
    public static UserStore buildUserStoreWithDisplayName( String displayName )
    {
        return UserStore.builder().displayName( displayName ).build();

    }

    public static User buildUser( String userName, String password )
    {
        return User.builder().displayName( userName ).email( userName + "@gmail.com" ).password( password ).build();

    }
}
