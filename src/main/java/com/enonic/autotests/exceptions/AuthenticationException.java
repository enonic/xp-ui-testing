package com.enonic.autotests.exceptions;

/**
 * <p>
 * A runtime exception representing a failure to provide correct authentication
 * credentials.
 * </p>
 */
public class AuthenticationException
    extends RuntimeException
{

    private static final long serialVersionUID = -646053826968947970L;

    public AuthenticationException( String message )
    {
        super( message );

    }

    public AuthenticationException()
    {

    }

}
