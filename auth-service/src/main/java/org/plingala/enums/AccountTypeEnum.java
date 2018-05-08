package org.plingala.enums;

public enum AccountTypeEnum {
	CHECKING("C", "Checking Account"), SAVINGS("S", "Savings Account");
	private final String code;
	private final String value;

	AccountTypeEnum(String code, String val) {
		this.code = code;
		this.value = val;
	}

	public String getValue() {
		return value;
	}

	public String getCode() {
		return code;
	}
}