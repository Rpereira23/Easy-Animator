package cs3500.animator.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Basic parser for handling command line arguments for customization of animation.
 */
public class ArgParser {

    /**
     * Represent various traits to be indicated by arguments to main method.
     */
    public enum InputOptions {
        VIEW("-view"),
        OUT("-out"),
        IN("-in"),
        SPEED("-speed");

        private String type;

        InputOptions(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * Represent various versions of view that can be displayed.
     */
    public enum ViewOptions {
        VISUAL, TEXT, SVG, EDIT, PROVIDER;

        /**
         * Return the proper enum based on the given String.  If String is invalid,
         *  return null.
         * @param type the string indicating the type of view and enum required
         * @return the proper enum for the given string
         */
        public static ViewOptions getViewType(String type) {
            switch (type) {
                case "visual":
                    return VISUAL;
                case "text":
                    return TEXT;
                case "svg":
                    return SVG;
                case "edit":
                    return EDIT;
                case "provider":
                    return PROVIDER;
                default:
                    return null;
            }
        }
    }

    private Readable r;
    private Appendable a;
    private ViewOptions viewOptions;
    private int speed;
    private Parser parser;


    /**
     * The parser that holds all information obtained from the parsing.
     * @param r the readable that info will be obtained from
     * @param a the appendable for output
     * @param viewOptions the view type chosen
     * @param speed the speed
     * @param parser the parser this came from
     */
    private ArgParser(Readable r,
                      Appendable a,
                      ViewOptions viewOptions,
                      int speed, Parser parser) {
        this.r = r;
        this.a = a;
        this.viewOptions = viewOptions;
        this.speed = speed;
        this.parser = parser;
    }

    public Readable getIn() {
        return this.r;
    }

    public Appendable getOut() {
        return this.a;
    }

    public ViewOptions getViewType() {
        return this.viewOptions;
    }

    public int getSpeed(){
        return this.speed;
    }

    public Readable getNewIn(){
        this.r = this.parser.resetIn();
        return this.getIn();
    }

    /**
     * Class for parsing command line arguments for the ExCellence Animator.
     */
    public static class Parser {
        private Readable in;
        private Appendable out;
        private ViewOptions viewType;
        private int speed;
        private String filePath;
        //Store functions for process different configuration indicators
        private Map<String, Function<String, Integer>> options;

        public Parser(String[] unparsed) {
            this.speed = 1;
            options = new HashMap<>();
            options.put(InputOptions.IN.getType(), (X) -> {
                return this.processFileOption(X); });
            options.put(InputOptions.VIEW.getType(), (X) -> {
                return this.processView(X); });
            options.put(InputOptions.SPEED.getType(), (X) -> {
                return this.processSpeed(X); });
            options.put(InputOptions.OUT.getType(), (X) -> {
                return this.processOut(X); });
            //Default the output to System.out
            this.out = new PrintStream(System.out);
            this.in = null;

            //Process all arguments
            for (int i = 0; i < unparsed.length; ++i) {
                if (options.containsKey(unparsed[i])) {
                    options.get(unparsed[i]).apply(unparsed[i + 1]);
                }
            }
        }

        public ArgParser parse() {
            return new ArgParser(this.in,
                    this.out, this.viewType, this.speed, this);
        }

        public Readable resetIn(){
            this.processFileOption(this.filePath);
            return this.in;
        }

        /**
         * Attempt to set the input source to the given filepath.
         * @param filePath a string representing the desired file to be read from
         * @return 0 if successful
         */
        private int processFileOption(String filePath) {
            Readable r;
            this.filePath = filePath;
            try {
                r = new FileReader(filePath);
            } catch (IOException e) {
                throw new IllegalArgumentException("invalid file source");
            }
            this.in = r;
            return 0;
        }

        /**
         * Properly set the ViewType based on the given String.
         * @param viewString the desired ViewType
         * @return 0 if successful
         */
        private int processView(String viewString) {
            this.viewType = ViewOptions.getViewType(viewString);
            return 0;
        }

        /**
         * Properly set the output destination based on the given String.
         * @param output a String representing the file name of the desired output location
         * @return 0 if successful
         */
        private int processOut(String output) {
            Appendable a;
            try {
                a = new PrintStream(output);
            } catch (IOException e) {
                throw new IllegalArgumentException("unable to set designated output");
            }
            this.out = a;
            return 0;
        }

        /**
         * Attempt to process the desired speed.
         * @param speedStr a String representing the desired speed to be set
         * @return 0 if successful
         */
        private int processSpeed(String speedStr) {
            try {
                this.speed = Integer.parseInt(speedStr);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid speed provided");
            }
            return 0;
        }

        /**
         * Return the speed found from the parser.
         * @return
         */
        private int getSpeed() {
            return this.speed;
        }

        /**
         * Return the output destination from the parser.
         * @return the input source
         */
        private Appendable getOut() {
            return this.out;
        }

        /**
         * Return the ViewType from the parser.
         * @return the input source
         */
        private ViewOptions getViewType() {
            return this.viewType;
        }

        /**
         * Return the input source from the parser.
         * @return the input source
         */
        private Readable getIn() {
            return this.in;
        }
    }
}
