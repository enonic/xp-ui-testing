package com.enonic.autotests.exceptions;

public class WizardPanelNotClosingException
    extends RuntimeException
{

    private static final long serialVersionUID = -646053826968947970L;

    public WizardPanelNotClosingException( String message )
    {
        super( message );

    }

    public WizardPanelNotClosingException()
    {

    }

}
