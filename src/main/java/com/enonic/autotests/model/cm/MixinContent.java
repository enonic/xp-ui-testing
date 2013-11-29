package com.enonic.autotests.model.cm;

import java.util.List;

public class MixinContent extends BaseAbstractContent
{
	private List<Address> addressList;

	public List<Address> getAddressList()
	{
		return addressList;
	}

	public void setAddressList(List<Address> addressList)
	{
		this.addressList = addressList;
	}

	
}
