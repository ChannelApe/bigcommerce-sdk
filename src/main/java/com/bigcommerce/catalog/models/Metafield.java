package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class Metafield {

	@XmlElement(name = "resource_id")
	private String resourceId;

	private Integer id;
	private String namespace;
	private String key;
	private String value;

	@XmlElement(name = "permission_set")
	private String permissionSet;

	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(final String namespace) {
		this.namespace = namespace;
	}

	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String getPermissionSet() {
		return permissionSet;
	}

	public void setPermissionSet(final String permissionSet) {
		this.permissionSet = permissionSet;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(final String resourceId) {
		this.resourceId = resourceId;
	}

}
