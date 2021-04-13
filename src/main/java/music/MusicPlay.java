//package music;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//
//import fileOperate.FilePath;
//import sun.audio.AudioPlayer;
//
///**
// * 此类只有静态函数 用于翻页提示音的播放
// */
//public class MusicPlay {
//
//    private static String song_path1 = new FilePath().filePath("wav/下一页.wav");
//
//    private static String song_path2 = new FilePath().filePath("wav/上一页.wav");
//
//    private static String song_path3 = new FilePath().filePath("wav/关键.wav");
//
//    public static void tipNext()
//    {
//        try {
//            AudioPlayer.player.start(new FileInputStream(song_path1));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void tipLast()
//    {
//        try {
//            AudioPlayer.player.start(new FileInputStream(song_path2));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void tipKey()
//    {
//        try {
//            AudioPlayer.player.start(new FileInputStream(song_path3));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//}
