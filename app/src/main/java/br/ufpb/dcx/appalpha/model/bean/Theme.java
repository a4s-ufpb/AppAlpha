package br.ufpb.dcx.appalpha.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity Theme
 */
public class Theme {
    private Long id;
    private String name;
    private String imageUrl;
    private String soundUrl;
    private String videoUrl;
    private List<Challenge> challenges = new ArrayList<>();
    private Long apiId;
    private Boolean deletavel;

    /**
     * Alloc instance specifying id, name, image url, sound url, video url, list of challenges, api Id
     *
     * @param id
     * @param name
     * @param imageUrl
     * @param soundUrl
     * @param videoUrl
     * @param challenges
     * @param apiId
     */
    public Theme(Long id, String name, String imageUrl, String soundUrl, String videoUrl, List<Challenge> challenges, Long apiId) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.soundUrl = soundUrl;
        this.videoUrl = videoUrl;
        this.challenges = challenges;
        this.apiId = apiId;
    }

    /**
     * Alloc instance specifying name, image url, sound url, video url
     *
     * @param name
     * @param imageUrl
     * @param soundUrl
     * @param videoUrl
     */
    public Theme(String name, String imageUrl, String soundUrl, String videoUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.soundUrl = soundUrl;
        this.videoUrl = videoUrl;
    }

    /**
     * Get id from Theme stored in local database
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * Set id of Theme stored in local database
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get name of Theme
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of Theme
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get image url of Theme
     *
     * @return
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Set image url of Theme
     *
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Get sound url of Theme
     *
     * @return
     */
    public String getSoundUrl() {
        return soundUrl;
    }

    /**
     * Set sound url of Theme
     *
     * @param soundUrl
     */
    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }

    /**
     * Get video url of Theme
     *
     * @return
     */
    public String getVideoUrl() {
        return videoUrl;
    }

    /**
     * Set video url of Theme
     *
     * @param videoUrl
     */
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    /**
     * Get list of related Challenge of Theme
     *
     * @return
     */
    public List<Challenge> getChallenges() {
        return challenges;
    }

    /**
     * Set list of related Challenge of Theme
     *
     * @param challenges
     */
    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }

    /**
     * Get Id of Theme in the API
     *
     * @return
     */
    public Long getApiId() {
        return apiId;
    }

    /**
     * Set Id of Theme in the API
     *
     * @param apiId
     */
    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    /**
     * Check if Theme is deletable
     *
     * @return
     */
    public Boolean getDeletavel() {
        return deletavel;
    }

    /**
     * Set if Theme is deletable
     *
     * @param deletavel
     */
    public void setDeletavel(Boolean deletavel) {
        this.deletavel = deletavel;
    }

    /**
     * Check if is equal between Theme object
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return id.equals(theme.id);
    }

    /**
     * Show description of Theme
     *
     * @return
     */
    @Override
    public String toString() {
        return "Theme{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", soundUrl='" + soundUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", deletavel='" + deletavel + '\'' +
                ", apiId='" + apiId + '\'' +
                '}';
    }
}
