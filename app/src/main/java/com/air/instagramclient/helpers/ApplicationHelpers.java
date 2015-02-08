package com.air.instagramclient.helpers;

import com.air.instagramclient.models.InstagramPhotoModel;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by hetashah on 2/6/15.
 */
public class ApplicationHelpers {
    public static boolean isEmpty(List photos) {
        return !(photos != null && photos.isEmpty());
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equalsIgnoreCase(str);
    }

    public enum SUPPORTED_TYPE {
        IMAGE
    }

    public static boolean isNotEmpty(JSONArray obj) {
        return (obj != null && obj.length() > 0);
    }

    public static boolean isEmpty(JSONArray obj) {
        return !(obj != null && obj.length() > 0);
    }

    public static boolean isSupportType(String type) {
        if (type != null) {
            for (SUPPORTED_TYPE sptType : SUPPORTED_TYPE.values()) {
                if (type.equalsIgnoreCase(sptType.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
}