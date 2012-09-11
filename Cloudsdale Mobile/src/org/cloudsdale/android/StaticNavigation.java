package org.cloudsdale.android;

public enum StaticNavigation {

    HOME("Home", R.drawable.color_icon, "Home"), SETTINGS("Settings",
            R.drawable.color_icon, "Settings"), EXPLORE("Explore",
            R.drawable.color_icon, "Explore"), LOGOUT("Log out",
            R.drawable.color_icon, "Logout");

    private String displayName;
    private int    resId;
    private String textId;

    private StaticNavigation(String display, int resid, String textid) {
        this.displayName = display;
        this.resId = resid;
        this.textId = textid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getResId() {
        return resId;
    }

    public String getTextId() {
        return textId;
    }

}
