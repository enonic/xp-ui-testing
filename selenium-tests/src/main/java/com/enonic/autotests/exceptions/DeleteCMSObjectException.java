package com.enonic.autotests.exceptions;

public class DeleteCMSObjectException
    extends RuntimeException
{

    private static final long serialVersionUID = -677053826968944470L;

    public DeleteCMSObjectException( String message )
    {
        super( message );

    }

    public DeleteCMSObjectException()
    {

    }

}
