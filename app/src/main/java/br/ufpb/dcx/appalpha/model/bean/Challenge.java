package br.ufpb.dcx.appalpha.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity Challenge
 */
public class Challenge {
    private Long id;
    private String word;
    private String soundUrl;
    private String videoUrl;
    private String imageUrl;
    private List<Theme> contexts = new ArrayList<Theme>();

    /**
     * Alloc instance specifying id, word, sound url, video url, image url, list os contexts
     * @param id
     * @param word
     * @param soundUrl
     * @param videoUrl
     * @param imageUrl
     * @param contexts
     */
    public Challenge(Long id, String word, String soundUrl, String videoUrl, String imageUrl, List<Theme> contexts) {
        this.id = id;
        this.word = word;
        this.soundUrl = soundUrl;
        this.videoUrl = videoUrl;
        this.imageUrl = imageUrl;
        this.contexts = contexts;
    }

    /**
     * Alloc instance specifying id, word, sound url, video url, image url
     * @param id
     * @param word
     * @param soundUrl
     * @param videoUrl
     * @param imageUrl
     */
    public Challenge(Long id, String word, String soundUrl, String videoUrl, String imageUrl) {
        this.id = id;
        this.word = word;
        this.soundUrl = soundUrl;
        this.videoUrl = videoUrl;
        this.imageUrl = imageUrl;
    }

    /**
     * Alloc instance specifying word, sound url, video url, image url
     * @param word
     * @param soundUrl
     * @param videoUrl
     * @param imageUrl
     */
    public Challenge(String word, String soundUrl, String videoUrl, String imageUrl) {
        this.word = word;
        this.soundUrl = soundUrl;
        this.videoUrl = videoUrl;
        this.imageUrl = imageUrl;
    }

    /**
     * Get id from Challenge stored in local database
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * Set id of Challenge stored in local database
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get word of Challenge
     * @return
     */
    public String getWord() {
        return word;
    }

    /**
     * Set word of Challenge
     * @param word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Get the sound url of challenge
     * @return
     */
    public String getSoundUrl() {
        return soundUrl;
    }

    /**
     * Set the sound url of challenge
     * @param soundUrl
     */
    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }

    /**
     * Get the video url of challenge
     * @return
     */
    public String getVideoUrl() {
        return videoUrl;
    }

    /**
     * Set the video url of challenge
     * @param videoUrl
     */
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    /**
     * Get the image url of challenge
     * @return
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Set the image url of challenge
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Get list of related context of challenges
     * @return
     */
    public List<Theme> getContexts() {
        return contexts;
    }

    /**
     * Set list of related context of challenges
     * @param contexts
     */
    public void setContexts(List<Theme> contexts) {
        this.contexts = contexts;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Challenge challenge = (Challenge) o;
//        return id.equals(challenge.id);
//    }

    /**
     * Show description of Challenge
     * @return
     */
    @Override
    public String toString() {
        return "Challenge{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", soundUrl='" + soundUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", contexts=" + contexts +
                '}';
    }
}
