package com.helper;

import java.io.Serializable;

public class CSSModel implements Serializable {
	private static final long SerialVersionUID = 102;
	String cssName = "";
	String tagSelector = "";
	String classSelector = "";
	String idSelector = "";
	String otherSelector = "";
	int tagSelectorCount = 0;
	int classSelectorCount = 0;
	int idSelectorCount = 0;
	int otherSelectorCount = 0;
	String cssUrl = "";

	public String getCssUrl() {
		return cssUrl;
	}

	public void setCssUrl(String cssUrl) {
		this.cssUrl = cssUrl;
	}

	public String getCssName() {
		return cssName;
	}

	public void setCssName(String cssName) {
		this.cssName = cssName;
	}

	public String getTagSelector() {
		return tagSelector;
	}

	public void setTagSelector(String tagSelector) {
		this.tagSelector = tagSelector;
	}

	public String getClassSelector() {
		return classSelector;
	}

	public void setClassSelector(String classSelector) {
		this.classSelector = classSelector;
	}

	public String getIdSelector() {
		return idSelector;
	}

	public void setIdSelector(String idSelector) {
		this.idSelector = idSelector;
	}

	public String getOtherSelector() {
		return otherSelector;
	}

	public void setOtherSelector(String otherSelector) {
		this.otherSelector = otherSelector;
	}

	public int getTagSelectorCount() {
		return tagSelectorCount;
	}

	public void setTagSelectorCount(int tagSelectorCount) {
		this.tagSelectorCount = tagSelectorCount;
	}

	public int getClassSelectorCount() {
		return classSelectorCount;
	}

	public void setClassSelectorCount(int classSelectorCount) {
		this.classSelectorCount = classSelectorCount;
	}

	public int getIdSelectorCount() {
		return idSelectorCount;
	}

	public void setIdSelectorCount(int idSelectorCount) {
		this.idSelectorCount = idSelectorCount;
	}

	public int getOtherSelectorCount() {
		return otherSelectorCount;
	}

	public void setOtherSelectorCount(int otherSelectorCount) {
		this.otherSelectorCount = otherSelectorCount;
	}

}
