package com.enonic.wem.uitest.schema.cfg

class TwoTextLineContentTypeCfg {

	static def CFG = '''
	<type>
	<display-name>Text Line</display-name>
	 <content-display-name-script />
	 <super-type>structured</super-type>
	 <is-abstract>false</is-abstract>
	 <is-final>true</is-final>
	 <is-built-in>false</is-built-in>
	 <allow-child-content>true</allow-child-content>
	 <form>
		   <input type="TextLine" name="requiredTextLine">
		   	 <label>Text Line</label>
			 <immutable>false</immutable>
			 <indexed>true</indexed>
			 <custom-text />
			 <help-text />
			 <occurrences minimum="1" maximum="0" />
		   </input>

          <input type="TextLine" name="unrequiredTextLine">
			 <label>Text Line</label>
			 <immutable>false</immutable>
			 <indexed>true</indexed>
			 <custom-text />
			 <help-text />
			 <occurrences minimum="0" maximum="0" />
		   </input>
	 </form>
</type>
	'''
}
