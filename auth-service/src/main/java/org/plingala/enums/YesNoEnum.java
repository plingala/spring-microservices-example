package org.plingala.enums;

import org.apache.commons.lang.StringUtils;

public enum YesNoEnum {
	YES("Y"), NO("N"),RETURNED("R");
	
	private final String value;
	
	YesNoEnum(String val) {
		this.value = val;
	}
	public String getValue() {
		return this.value;
	}
	public char getCharValue() {
		return this.value.charAt(0);
	}
	public boolean getBooleanValue() {
		return (StringUtils.equalsIgnoreCase(getValue(), "Y")) ? true : false;
	}
}
