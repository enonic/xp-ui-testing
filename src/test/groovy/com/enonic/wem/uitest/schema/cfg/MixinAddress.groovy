package com.enonic.wem.uitest.schema.cfg

class MixinAddress
{
    static def CFG = '''
<mixin>
  <display-name>Test: Address Mixin</display-name>
  <items>
    <form-item-set name="address">
      <label>Address</label>
      <immutable>false</immutable>
      <custom-text />
      <help-text />
      <occurrences minimum="0" maximum="0" />
      <items>
        <input type="TextLine" name="street">
          <label>Street</label>
          <immutable>false</immutable>
          <indexed>true</indexed>
          <custom-text />
          <help-text />
          <occurrences minimum="0" maximum="2" />
        </input>
        <input type="TextLine" name="postalCode">
          <label>Postal code</label>
          <immutable>false</immutable>
          <indexed>true</indexed>
          <custom-text />
          <help-text />
          <occurrences minimum="1" maximum="1" />
        </input>
        <input type="TextLine" name="postalPlace">
          <label>Postal place</label>
          <immutable>false</immutable>
          <indexed>true</indexed>
          <custom-text />
          <help-text />
          <occurrences minimum="0" maximum="1" />
        </input>
      </items>
    </form-item-set>
  </items>
</mixin>
'''
}
