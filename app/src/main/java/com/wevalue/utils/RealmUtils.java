package com.wevalue.utils;

import com.wevalue.model.Channels;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 作者：邹永奎
 * 创建时间：2016/11/14
 * 类说明：
 */

public class RealmUtils {
    public List<Channels> loadChanel(Realm realm) {
        return realm.where(Channels.class).findAll();
    }

    public Channels loadChanelByName(Realm realm, String classname) {
        Channels channels = realm.where(Channels.class).equalTo("classname", classname).findFirst();
        return channels;
    }

    public boolean updateChanelsById(Realm realm, String newName) {
        Channels channels = realm.where(Channels.class).equalTo("id", "3").findFirst();
        if (channels != null) {
            realm.beginTransaction();
            channels.setClassname(newName);
            realm.commitTransaction();
            return true;
        }
        return false;
    }

    public RealmResults<Channels> getUserLikeType(Realm realm) {
        RealmResults<Channels> channels = realm.where(Channels.class).equalTo("isLike", "true").findAll();
        return channels;
    }

    public boolean setUserLikeType(Realm realm, String id) {
        RealmResults<Channels> channels = realm.where(Channels.class).equalTo("isLike", "true").findAll();

        return false;
    }
}
