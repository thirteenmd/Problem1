package com.doxbit.training;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class Finder extends SimpleFileVisitor<Path> {

  public ArrayList<Path> xmlFiles = new ArrayList<Path>();

  private final PathMatcher matcher;
  private int numMatches = 0;

  public Finder(String pattern) {
    matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
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
  public void done() {
    System.out.println("Matched: " + numMatches);
  }

  public ArrayList<Path> returnPaths() {
    return xmlFiles;
  }

  // Invoke the pattern matching
  // method on each file.
  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
    find(file);
    return CONTINUE;
  }

  // Invoke the pattern matching
  // method on each directory.
  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
    find(dir);
    return CONTINUE;
  }

  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) {
    System.err.println(exc);
    return CONTINUE;
  }
}