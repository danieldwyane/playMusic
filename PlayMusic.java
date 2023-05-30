import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayMusic {

    private static int count = 0;
    private static List<Music> musicList = new ArrayList<Music>();
    private static Random rand = new Random(); // instance of random class

    public static void main(String[] args) throws IOException {
        System.out.println("Inicio: " + new Date());
        List<String> fileExtensions = new ArrayList<String>();
        fileExtensions.add("mp3");
        fileExtensions.add("wma");
        fileExtensions.add("m4a");
        findFile(fileExtensions, new File("/media/daniel/D804B62904B60B10/Music/"));
        deletePlayedMusic();
        System.out.println(count);
        chooseRandomMusic();
        deleteFolder();
        copyMusic();
        saveMusicToFile();
        System.out.println("Fin: " + new Date());
    }

    public static void findFile(List<String> names, File file) {
        File[] list = file.listFiles();
        if (list != null)
            for (File fil : list) {
                if (fil.isDirectory()) {
                    findFile(names, fil);
                } else {
                    for (String extension : names) {
                        if (fil.getName().toUpperCase().endsWith(extension.toUpperCase())) {
                            count++;
                            Music music = new Music();
                            music.setPath(fil.getPath());
                            music.setTitle(fil.getName());
                            musicList.add(music);
                        }
                    }
                }
            }
    }

    public static void deletePlayedMusic() {
        List<Music> resList = readMusicFromFile();
        Set<String> ids = resList.stream().map(obj -> obj.getPath()).collect(Collectors.toSet());
        musicList = musicList.stream().filter(obj -> !ids.contains(obj.getPath())).collect(Collectors.toList());
    }

    public static void chooseRandomMusic() {
        int upperbound = musicList.size();
        int musicNumber = 30;
        if (musicList.size() < 30) {
            musicNumber = musicList.size();
        }
        List<Music> musicAuxList = new ArrayList<Music>();
        for (int x = 0; x < musicNumber; x++) {
            // generate random values from 0-musicList.size()
            int int_random = rand.nextInt(upperbound);
            musicAuxList.add(musicList.get(int_random));
            System.out.println(musicList.get(int_random));
        }
        musicList.clear();
        musicList.addAll(musicAuxList);
    }

    public static void deleteFolder() {
        File folder = new File("/home/daniel/Documentos/musicPlay");
        File[] files = folder.listFiles();
        if (files != null) { // some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder();
                } else {
                    f.delete();
                }
            }
        }
    }

    public static void copyMusic() {
        try {
            for (Music mus : musicList) {
                Path copied = Paths.get(mus.getPath());
                Path targetFilePath = Paths.get("/home/daniel/Documentos/musicPlay", mus.getTitle());
                Files.copy(copied, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            String i;
        }
    }

    public static List<Music> readMusicFromFile() {
        List<Music> playedMusList = new ArrayList<Music>();
        try {
            File myObj = new File("/home/daniel/Documentos/out.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                Music mus = new Music();
                mus.setPath(data);
                playedMusList.add(mus);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return playedMusList;
    }

    public static void saveMusicToFile() throws IOException {
        File fout = new File("/home/daniel/Documentos/out.txt");
        FileOutputStream fos = new FileOutputStream(fout, true);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for (Music mus : musicList) {
            bw.write(mus.getPath());
            bw.newLine();
        }

        bw.close();
    }

    public static class Music {
        String path;
        String title;

        public Music() {
            this.path = "";
            this.title = "";
        }

        public Music(String path, String title) {
            this.path = path;
            this.title = title;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String newPath) {
            this.path = newPath;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String newTitle) {
            this.title = newTitle;
        }
    }
}