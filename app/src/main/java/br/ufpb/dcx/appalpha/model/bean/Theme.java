package br.ufpb.dcx.appalpha.model.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Theme {
    private Long id;
    private String name;
    private String imageUrl;
    private String soundUrl;
    private String videoUrl;
    private List<Challenge>  challenges = new ArrayList<>();
    private Long apiId;
    private Boolean deletavel;

    public Theme(Long id, String name, String imageUrl, String soundUrl, String videoUrl, List<Challenge>  challenges, Long apiId) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.soundUrl = soundUrl;
        this.videoUrl = videoUrl;
        this.challenges = challenges;
        this.apiId = apiId;
    }

    public Theme(String name, String imageUrl, String soundUrl, String videoUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.soundUrl = soundUrl;
        this.videoUrl = videoUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Boolean getDeletavel() {
        return deletavel;
    }

    public void setDeletavel(Boolean deletavel) {
        this.deletavel = deletavel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return id.equals(theme.id);
    }

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
