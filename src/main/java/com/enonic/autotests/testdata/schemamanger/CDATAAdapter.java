package com.enonic.autotests.testdata.schemamanger;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CDATAAdapter extends XmlAdapter<String, String> {
	 
    @Override
    public String marshal(String configData) throws Exception {
        return "<![CDATA[" + configData + "]]>";
    }
 
    @Override
    public String unmarshal(String configData) throws Exception {
        return configData;
    }

}
