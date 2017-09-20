package com.xmniao.xmn.core.manor.entity;

public class ManorFlowerBranch {
    private String id;

    private String parentId;

    private Integer uid;

    private Integer location;

    private Integer level;

    private String zid;
    private String uname;
    private String nname;
    private Integer childSum;

    public Integer getChildSum() {
		return childSum;
	}

	public void setChildSum(Integer childSum) {
		this.childSum = childSum;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getZid() {
        return zid;
    }

    public void setZid(String zid) {
        this.zid = zid == null ? null : zid.trim();
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUname() {
        return uname;
    }

    public void setNname(String nname) {
        this.nname = nname;
    }

    public String getNname() {
        return nname;
    }
}