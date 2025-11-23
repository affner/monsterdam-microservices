package com.fanflip.multimedia.domain.enumeration;

/**
 * The MultimediaKind enumeration.
 */
public enum MultimediaKind {

    //  STORY("multimedia.kind.story"),
    VIDEO("multimedia.kind.video"),
    PHOTO("multimedia.kind.photo"),

    LIVESTREAM("multimedia.kind.livestream"),

    AUDIO("multimedia.kind.audio");

    private final String value;

    MultimediaKind(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    //   public static final String STORY_ENTITY = "STORY";
    public static final String VIDEO_ENTITY = "VIDEO";
    public static final String PHOTO_ENTITY = "PHOTO";
    public static final String LIVESTREAM_ENTITY = "LIVESTREAM";
    public static final String AUDIO_ENTITY = "AUDIO";
}
