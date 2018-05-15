package com.enonic.autotests.pages.contentmanager.browsepanel;



public enum SortOrder {
    ASCENDING("ascending"), DESCENDING("descending ");

    private String value;

    public String getValue() {
        return value;
    }

    private SortOrder(String value) {
        this.value = value;
    }
}
