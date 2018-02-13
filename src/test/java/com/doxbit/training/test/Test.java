package com.doxbit.training.test;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.doxbit.training.Finder;

public class Test {
  public static void main(String[] args) throws IOException, XmlPullParserException {
    Path startingDir = Paths.get("test");
    String pattern = "*.{xml,XML}";

    Finder finder = new Finder(pattern);
    Files.walkFileTree(startingDir, finder);
    finder.done();

    ArrayList<Path> paths = finder.returnPaths();

    try {
      MavenXpp3Reader reader = new MavenXpp3Reader();
      for (Path path : paths) {
        System.out.println("Information from " + path.toString());
        Model model = reader.read(new FileReader(path.toString()));
        System.out.println(model.getId());
        System.out.println(model.getGroupId());
        System.out.println(model.getArtifactId());
        System.out.println(model.getVersion());
        System.out.println(" ");
        System.out.println(" ");
      }
    } catch (XmlPullParserException e) {
      System.err.print(e.toString());
    }
  }
}
