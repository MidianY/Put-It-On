package edu.brown.cs.student.main;

import java.util.Arrays;


/** The Main class of our project. This is where execution begins. */
public final class Main {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // prints out command line arguments; can remove this
    System.out.println(Arrays.toString(args));
     }
}
