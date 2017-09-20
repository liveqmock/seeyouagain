package com.xmniao.xmn.core.manor.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.xmniao.xmn.core.base.BaseEntity;
import com.xmniao.xmn.core.xmnburs.entity.Burs;

import java.util.Date;

public class TManorFlowerRelation extends BaseEntity{
    /**
	 * 原花蜜收益管理
	 */
	private static final long serialVersionUID = -456878261981937667L;

	private Integer id;

    private Integer type;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Integer location;

    private Integer pid;

    private Integer leftNode;

    private Integer rightNode;

    private Integer zid;

    private Integer level;

    private Integer uid;

    private Integer fromUid;

    private Integer state;
    
    /*自定义字段区域*/
    private Integer currentFlowerRelationCount;  //当前贡献花朵
    
    private Integer totalFlowerRelationCount;  //累计贡献花朵
    
    private String fromUname;  //名称


    private Integer livedFlowers;       // 当前花朵数
    private Integer perishedFlowers;    // 枯萎花朵数
    private Burs userInfo;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Burs getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Burs userInfo) {
        this.userInfo = userInfo;
    }

    public Integer getLivedFlowers() {
        return livedFlowers;
    }

    public void setLivedFlowers(Integer livedFlowers) {
        this.livedFlowers = livedFlowers;
    }

    public Integer getPerishedFlowers() {
        return perishedFlowers;
    }

    public void setPerishedFlowers(Integer perishedFlowers) {
        this.perishedFlowers = perishedFlowers;
    }

    public Integer getCurrentFlowerRelationCount() {
		return currentFlowerRelationCount;
	}

	public void setCurrentFlowerRelationCount(Integer currentFlowerRelationCount) {
		this.currentFlowerRelationCount = currentFlowerRelationCount;
	}

	public Integer getTotalFlowerRelationCount() {
		return totalFlowerRelationCount;
	}

	public void setTotalFlowerRelationCount(Integer totalFlowerRelationCount) {
		this.totalFlowerRelationCount = totalFlowerRelationCount;
	}

	public String getFromUname() {
		return fromUname;
	}

	public void setFromUname(String fromUname) {
		this.fromUname = fromUname;
	}
	
	
    /*自定义字段区域*/
    


	public Integer getId() {
        return id;
    }

	public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Integer leftNode) {
        this.leftNode = leftNode;
    }

    public Integer getRightNode() {
        return rightNode;
    }

    public void setRightNode(Integer rightNode) {
        this.rightNode = rightNode;
    }

    public Integer getZid() {
        return zid;
    }

    public void setZid(Integer zid) {
        this.zid = zid;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getFromUid() {
        return fromUid;
    }

    public void setFromUid(Integer fromUid) {
        this.fromUid = fromUid;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

	@Override
	public String toString() {
		return "TManorFlowerRelation [id=" + id + ", type=" + type
				+ ", endTime=" + endTime + ", location=" + location + ", pid="
				+ pid + ", leftNode=" + leftNode + ", rightNode=" + rightNode
				+ ", zid=" + zid + ", level=" + level + ", uid=" + uid
				+ ", fromUid=" + fromUid + ", state=" + state
				+ ", currentFlowerRelationCount=" + currentFlowerRelationCount
				+ ", totalFlowerRelationCount=" + totalFlowerRelationCount
				+ ", fromUname=" + fromUname + "]";
	}

    
}