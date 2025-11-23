package com.fanflip.multimedia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ContentPackage.
 */
@Entity
@Table(name = "content_package")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContentPackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "video_count")
    private Integer videoCount;

    @Column(name = "image_count")
    private Integer imageCount;

    @NotNull
    @Column(name = "is_paid_content", nullable = false)
    private Boolean isPaidContent;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "post_id")
    private Long postId;

    @JsonIgnoreProperties(value = { "contentPackage" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private SingleAudio audio;

    @JsonIgnoreProperties(value = { "contentPackage" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private SingleLiveStream liveStream;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "belongPackage")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "belongPackage" }, allowSetters = true)
    private Set<SingleVideo> videos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "belongPackage")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "belongPackage" }, allowSetters = true)
    private Set<SinglePhoto> photos = new HashSet<>();


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contentPackage")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contentPackage" }, allowSetters = true)
    private Set<UserTagRelation> tags = new HashSet<>();

    @JsonIgnoreProperties(value = { "contentPackage" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "contentPackage")
    private SpecialReward specialReward;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContentPackage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public ContentPackage amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getVideoCount() {
        return this.videoCount;
    }

    public ContentPackage videoCount(Integer videoCount) {
        this.setVideoCount(videoCount);
        return this;
    }

    public void setVideoCount(Integer videoCount) {
        this.videoCount = videoCount;
    }

    public Integer getImageCount() {
        return this.imageCount;
    }

    public ContentPackage imageCount(Integer imageCount) {
        this.setImageCount(imageCount);
        return this;
    }

    public void setImageCount(Integer imageCount) {
        this.imageCount = imageCount;
    }

    public Boolean getIsPaidContent() {
        return this.isPaidContent;
    }

    public ContentPackage isPaidContent(Boolean isPaidContent) {
        this.setIsPaidContent(isPaidContent);
        return this;
    }

    public void setIsPaidContent(Boolean isPaidContent) {
        this.isPaidContent = isPaidContent;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ContentPackage createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public ContentPackage lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ContentPackage createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public ContentPackage lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public ContentPackage isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getMessageId() {
        return this.messageId;
    }

    public ContentPackage messageId(Long messageId) {
        this.setMessageId(messageId);
        return this;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getPostId() {
        return this.postId;
    }

    public ContentPackage postId(Long postId) {
        this.setPostId(postId);
        return this;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public SingleAudio getAudio() {
        return this.audio;
    }

    public void setAudio(SingleAudio singleAudio) {
        this.audio = singleAudio;
    }

    public ContentPackage audio(SingleAudio singleAudio) {
        this.setAudio(singleAudio);
        return this;
    }
    public SingleLiveStream getLiveStream() {
        return liveStream;
    }

    public void setLiveStream(SingleLiveStream liveStream) {
        this.liveStream = liveStream;
    }
    public ContentPackage liveStream(SingleLiveStream liveStream) {
        this.setLiveStream(liveStream);
        return this;
    }

    public Set<SingleVideo> getVideos() {
        return this.videos;
    }

    public void setVideos(Set<SingleVideo> singleVideos) {
        if (this.videos != null) {
            this.videos.forEach(i -> i.setBelongPackage(null));
        }
        if (singleVideos != null) {
            singleVideos.forEach(i -> i.setBelongPackage(this));
        }
        this.videos = singleVideos;
    }

    public ContentPackage videos(Set<SingleVideo> singleVideos) {
        this.setVideos(singleVideos);
        return this;
    }

    public ContentPackage addVideos(SingleVideo singleVideo) {
        this.videos.add(singleVideo);
        singleVideo.setBelongPackage(this);
        return this;
    }

    public ContentPackage removeVideos(SingleVideo singleVideo) {
        this.videos.remove(singleVideo);
        singleVideo.setBelongPackage(null);
        return this;
    }

    public Set<SinglePhoto> getPhotos() {
        return this.photos;
    }

    public void setPhotos(Set<SinglePhoto> singlePhotos) {
        if (this.photos != null) {
            this.photos.forEach(i -> i.setBelongPackage(null));
        }
        if (singlePhotos != null) {
            singlePhotos.forEach(i -> i.setBelongPackage(this));
        }
        this.photos = singlePhotos;
    }

    public ContentPackage photos(Set<SinglePhoto> singlePhotos) {
        this.setPhotos(singlePhotos);
        return this;
    }

    public ContentPackage addPhotos(SinglePhoto singlePhoto) {
        this.photos.add(singlePhoto);
        singlePhoto.setBelongPackage(this);
        return this;
    }

    public ContentPackage removePhotos(SinglePhoto singlePhoto) {
        this.photos.remove(singlePhoto);
        singlePhoto.setBelongPackage(null);
        return this;
    }

    public Set<UserTagRelation> getTags() {
        return this.tags;
    }

    public void setTags(Set<UserTagRelation> userTagRelations) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setContentPackage(null));
        }
        if (userTagRelations != null) {
            userTagRelations.forEach(i -> i.setContentPackage(this));
        }
        this.tags = userTagRelations;
    }

    public ContentPackage tags(Set<UserTagRelation> userTagRelations) {
        this.setTags(userTagRelations);
        return this;
    }

    public ContentPackage addTags(UserTagRelation userTagRelation) {
        this.tags.add(userTagRelation);
        userTagRelation.setContentPackage(this);
        return this;
    }

    public ContentPackage removeTags(UserTagRelation userTagRelation) {
        this.tags.remove(userTagRelation);
        userTagRelation.setContentPackage(null);
        return this;
    }

    public SpecialReward getSpecialReward() {
        return this.specialReward;
    }

    public void setSpecialReward(SpecialReward specialReward) {
        if (this.specialReward != null) {
            this.specialReward.setContentPackage(null);
        }
        if (specialReward != null) {
            specialReward.setContentPackage(this);
        }
        this.specialReward = specialReward;
    }

    public ContentPackage specialReward(SpecialReward specialReward) {
        this.setSpecialReward(specialReward);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContentPackage)) {
            return false;
        }
        return getId() != null && getId().equals(((ContentPackage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContentPackage{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", videoCount=" + getVideoCount() +
            ", imageCount=" + getImageCount() +
            ", isPaidContent='" + getIsPaidContent() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", messageId=" + getMessageId() +
            ", postId=" + getPostId() +
            "}";
    }


}
