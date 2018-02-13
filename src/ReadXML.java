import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import static java.nio.file.FileVisitResult.*;
import static java.nio.file.FileVisitOption.*;
import java.util.*;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ReadXML {

    public static class Finder
            extends SimpleFileVisitor<Path> {

        public List<Path> xmlFiles = new ArrayList<Path>();

        private final PathMatcher matcher;
        private int numMatches = 0;

        Finder(String pattern) {
            matcher = FileSystems.getDefault()
                    .getPathMatcher("glob:" + pattern);
        }

        // Compares the glob pattern against
        // the file or directory name.
        void find(Path file) {
            Path name = file.getFileName();
            if (name != null && matcher.matches(name)) {
                numMatches++;
                System.out.println(file);
                xmlFiles.add(file);
            }
        }

        // Prints the total number of
        // matches to standard out.
        void done() {
            System.out.println("Matched: "
                    + numMatches);
        }

        List returnPaths(){
            return xmlFiles;
        }

        // Invoke the pattern matching
        // method on each file.
        @Override
        public FileVisitResult visitFile(Path file,
                                         BasicFileAttributes attrs) {
            find(file);
            return CONTINUE;
        }

//        // Invoke the pattern matching
//        // method on each directory.
//        @Override
//        public FileVisitResult preVisitDirectory(Path dir,
//                                                 BasicFileAttributes attrs) {
//            find(dir);
//            return CONTINUE;
//        }

        @Override
        public FileVisitResult visitFileFailed(Path file,
                                               IOException exc) {
            System.err.println(exc);
            return CONTINUE;
        }
    }

    static void usage() {
        System.err.println("java Find <path>" +
                " -name \"<glob_pattern>\"");
        System.exit(-1);
    }

    public static void main(String[] args)
            throws IOException, XmlPullParserException {

        Path startingDir = Paths.get("C:\\Users\\admin\\Documents\\maven");
        String pattern = "pom.{xml,XML}";

        Finder finder = new Finder(pattern);
        Files.walkFileTree(startingDir, finder);
        finder.done();

        List<Path> paths = finder.returnPaths();

        try{
            MavenXpp3Reader reader = new MavenXpp3Reader();
            for(Path path: paths){
                System.out.println("Information from " + path.toString());
                Model model = reader.read(new FileReader(path.toString()));
                System.out.println(model.getId());
                System.out.println(model.getGroupId());
                System.out.println(model.getArtifactId());
                System.out.println(model.getVersion());
                System.out.println(" ");
                System.out.println(" ");
            }
        }catch (XmlPullParserException e){
            System.err.print(e.toString());
        }
    }
}