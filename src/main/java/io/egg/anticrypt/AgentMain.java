package io.egg.anticrypt;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import io.egg.anticrypt.injector.InjectorThread;
import io.egg.anticrypt.json.FullVersionManifest;
import io.egg.anticrypt.json.LauncherList;
import io.egg.anticrypt.json.GenericDownloadable;
import io.egg.anticrypt.json.VersionManifestFile;
import net.minecraft.bundler.Main;
import proguard.obfuscate.MappingReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class AgentMain {
    public static Gson g;
    public static Instrumentation cheats;
    public static void premain(String args, Instrumentation inst) throws IOException {
        cheats = inst;
        g = new Gson();
        URL version = Main.class.getResource("/version.json");
        var v = Resources.toString(version, StandardCharsets.UTF_8);
        VersionManifestFile vv = g.fromJson(v, VersionManifestFile.class);
        System.out.println("[AntiCrypt] Server version is " + vv.name + " (" + vv.id + ")");
        File s = findMappings(vv);
        MappingReader r = new MappingReader(s);
        r.pump(new ShittyMappingProcessor());
        new InjectorThread().start();

    }
    public static File findMappings(VersionManifestFile vv) throws IOException {
        var folder = new File("mappings");
        if (!folder.exists()) {
            folder.mkdir();
            return downloadMappings(vv);
        } else {
            var txt = Path.of("mappings", vv.id + ".txt").toFile();
            if (!txt.exists()) {
                return downloadMappings(vv);
            }
            return txt;
        }
    }
    public static File downloadMappings(VersionManifestFile vv) throws IOException {
        System.out.println("[AntiCrypt] Downloading mappings");
        String things = readStringFromURL("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        LauncherList l = g.fromJson(things, LauncherList.class);
        GenericDownloadable theVersion = l.findVersion(vv.id);
        System.out.println("[AntiCrypt] Need to get manifest from " + theVersion.url);
        String manifest = readStringFromURL(theVersion.url);
        FullVersionManifest f = g.fromJson(manifest, FullVersionManifest.class);
        var url = f.downloads.server_mappings.url;
        var path = Path.of("mappings", vv.id + ".txt");
        download(url, path);
        return path.toFile();
    }
    static long download(String url, Path filename) throws IOException {
        try (InputStream in = URI.create(url).toURL().openStream()) {
            return Files.copy(in, filename);
        }
    }
    public static String readStringFromURL(String requestURL) throws IOException
    {
        try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }
}
