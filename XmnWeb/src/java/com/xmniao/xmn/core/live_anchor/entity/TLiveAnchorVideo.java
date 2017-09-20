package com.xmniao.xmn.core.live_anchor.entity;

import java.util.Date;

import com.xmniao.xmn.core.base.BaseEntity;

public class TLiveAnchorVideo extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7277891696319003907L;

	private Integer id;//主键

    private String title;//标题

    private Integer anchorId;//主播用户id
    
    private String anchorName;//主播昵称

    private String coverUrl;//封面地址

    private String videoUrl;//视频地址

    private Integer sort;//排序值，默认1。降序排序

    private String status;//默认 001上线   002下线

    private Date createTime;//创建时间

    private Date updateTime;//更新时间
    
    private Integer recommended;//首页推荐，0否，1是。默认0
    
    private String ids;
    
	private Integer rtype;
	
	private Integer viewCount;

    public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public String getIds() {
    	return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(Integer anchorId) {
        this.anchorId = anchorId;
    }
    
    

    /**
	 * @return the anchorName
	 */
	public String getAnchorName() {
		return anchorName;
	}

	/**
	 * @param anchorName the anchorName to set
	 */
	public void setAnchorName(String anchorName) {
		this.anchorName = anchorName;
	}

	public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl == null ? null : coverUrl.trim();
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl == null ? null : videoUrl.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    
    /**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    

	/**
	 * @return the recommended
	 */
	public Integer getRecommended() {
		return recommended;
	}

	/**
	 * @param recommended the recommended to set
	 */
	public void setRecommended(Integer recommended) {
		this.recommended = recommended;
	}

	public Integer getRtype() {
		return rtype;
	}

	public void setRtype(Integer rtype) {
		this.rtype = rtype;
	}

	@Override
	public String toString() {
		return "TLiveAnchorVideo [id=" + id + ", title=" + title
				+ ", anchorId=" + anchorId + ", anchorName=" + anchorName
				+ ", coverUrl=" + coverUrl + ", videoUrl=" + videoUrl
				+ ", sort=" + sort + ", status=" + status + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", recommended="
				+ recommended + ", ids=" + ids + ", rtype=" + rtype + "]";
	}


    
    
}