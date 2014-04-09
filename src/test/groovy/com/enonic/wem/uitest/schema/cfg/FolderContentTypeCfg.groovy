package com.enonic.wem.uitest.schema.cfg

class FolderContentTypeCfg
{
    static def FOLDER_CFG = '''
<type>
	<module>system</module>
    <display-name>ctFolder</display-name>
    <content-display-name-script />
	<super-type>folder</super-type>
	<is-abstract>false</is-abstract>
	<is-built-in>true</is-built-in>
	<is-final>false</is-final>
	<allow-children>true</allow-children>
	<form />
</type>	'''
}
