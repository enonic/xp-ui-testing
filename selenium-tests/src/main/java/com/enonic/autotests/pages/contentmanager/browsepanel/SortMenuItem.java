package com.enonic.autotests.pages.contentmanager.browsepanel;


public enum SortMenuItem {
    DISPLAY_NAME("Display name"), MODIFIED_DATE("Modified date"), PUBLISHED_DATE("Published date"), MANUALLY_SORTED("Manually sorted");

    private String value;

    public String getValue() {
        return value;
    }

    private SortMenuItem(String value) {
        this.value = value;
    }
}
