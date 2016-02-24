package com.enonic.autotests.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class TextTransfer
    implements ClipboardOwner
{
    public void typePathToFileToSystemDialog( String absolutePath )
    {
        try
        {
            setClipboardContents( absolutePath );
            sleep( 1500 );
            Robot robot = new Robot();
            robot.waitForIdle();
            robot.keyPress( KeyEvent.VK_CONTROL );
            robot.keyPress( KeyEvent.VK_V );
            sleep( 500 );
            robot.keyRelease( KeyEvent.VK_V );
            robot.keyRelease( KeyEvent.VK_CONTROL );
            sleep( 2000 );
            robot.keyPress( KeyEvent.VK_ENTER );
            robot.keyRelease( KeyEvent.VK_ENTER );
            sleep( 1000 );
        }
        catch ( AWTException e )
        {
            System.out.println( e );

        }
    }

    /**
     * Place a String on the clipboard, and make this class the owner of the
     * Clipboard's contents.
     */
    public void setClipboardContents( String aString )
    {
        StringSelection stringSelection = new StringSelection( aString );
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents( stringSelection, this );
    }

    public String getClipboardContents()
    {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents( null );
        boolean hasTransferableText = ( contents != null ) && contents.isDataFlavorSupported( DataFlavor.stringFlavor );
        if ( hasTransferableText )
        {
            try
            {
                result = (String) contents.getTransferData( DataFlavor.stringFlavor );
            }
            catch ( UnsupportedFlavorException ex )
            {
                // highly unlikely since we are using a standard DataFlavor
                System.out.println( ex );
                ex.printStackTrace();
            }
            catch ( IOException ex )
            {
                System.out.println( ex );
                ex.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void lostOwnership( Clipboard clipboard, Transferable contents )
    {
        // TODO Auto-generated method stub
    }
}