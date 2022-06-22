package io.egg.anticrypt.json;

import java.util.List;

public class LauncherList {
    public List<GenericDownloadable> versions;
    public GenericDownloadable findVersion(String id) {
        for (GenericDownloadable v : versions) {
            if (v.id.equals(id)) return v;
        }
        return null;
    }
}
