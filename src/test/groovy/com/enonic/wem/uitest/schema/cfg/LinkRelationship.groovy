package com.enonic.wem.uitest.schema.cfg

class LinkRelationship
{
    static def CFG = '''
	<relationship-type>
		<display-name>link test</display-name>
		<from-semantic>links to</from-semantic>
		<to-semantic>linked by</to-semantic>
		<allowed-from-types />
		<allowed-to-types />
	</relationship-type>
'''
}
