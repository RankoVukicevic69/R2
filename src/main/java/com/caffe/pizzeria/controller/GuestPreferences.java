package com.caffe.pizzeria.controller;

import java.io.Serializable;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;

@ManagedBean
@SessionScoped
public class GuestPreferences implements Serializable {
	private static final long serialVersionUID = 1L;

	private String layout = "moody";
        
    private String theme = "serenity-bluegray";
    
    private boolean darkMenu;

    private boolean horizontal = false;
    
    private boolean orientationRTL;
                            
	public String getTheme() {		
		return theme;
	}
    
	public void setTheme(String theme) {
		this.theme = theme;
	}
    
    public String getLayout() {		
        return layout;
    }
    
    public void setLayout(String layout) {
        this.layout = layout;
    }
    
    public boolean isDarkMenu() {		
        return darkMenu;
    }
    
    public void setDarkMenu(boolean darkMenu) {
        this.darkMenu = darkMenu;
    }

    public boolean isOrientationRTL() {
        return orientationRTL;
    }

    public void setOrientationRTL(boolean orientationRTL) {
        this.orientationRTL = orientationRTL;
    }

    public boolean isHorizontal() {
        return this.horizontal;
    }

    public void setHorizontal(boolean value) {
        this.horizontal = value;
    }
}
