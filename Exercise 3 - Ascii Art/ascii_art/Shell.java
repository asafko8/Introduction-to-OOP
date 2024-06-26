package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import exceptions.ExceedingBoundsException;
import exceptions.ExecuteException;
import exceptions.IncorrectFormatException;
import image.Image;
import image.ImageProcessing;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;

/**
 * Responsible for the UI operations.
 *
 * @author Asaf Korman
 */
public class Shell {

    // Default constants
    private static final String DEFAULT_IMAGE_PATH = "cat.jpeg";
    private static final char[] DEFAULT_CHAR_SET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    // Prompts constants
    private static final int DEFAULT_RESOLUTION = 128;
    private static final String INPUT_PROMPT = ">>> ";
    private static final String EXIT_PROMPT = "exit";
    private static final String CHARS_PROMPT = "chars";
    private static final String ADD_PROMPT = "add";
    private static final String REMOVE_PROMPT = "remove";
    private static final String ALL_PROMPT = "all";
    private static final String SPACE_PROMPT = "space";
    // add-removes constants
    private static final char VALID_RANGE_SEPARATOR = '-';
    private static final char SPACE = ' ';
    private static final char TILDE = '~';
    private static final int VALID_RANGE_INPUT = 3;
    // Change resolution constants
    private static final String RES_PROMPT = "res";
    private static final String RES_UP_PROMPT = "up";
    private static final String RES_DOWN_PROMPT = "down";
    private static final int CHANGE_RES_FACTOR = 2;
    private static final String RESOLUTION_EXCEPTION_PROMPT = "change resolution";
    private static final String CHANGE_RES_PROMPT = "Resolution set to ";
    private static final String IMAGE_PROMPT = "image";
    // change output system constants
    private static final String OUT_PROMPT = "output";
    private static final String CONSOLE_PROMPT = "console";
    private static final String HTML_PROMPT = "html";
    private static final String HTML_FONT = "Courier New";
    private static final String HTML_OUTPUT_PATH = "out.html";
    private static final String ASCII_ART_PROMPT = "asciiart";
    // Errors constants
    private static final String RESOLUTION_EXCEED_BOUNDS_EXCEPTION = "Did not change resolution due to " +
            "exceeding boundaries.";
    private static final String INCORRECT_FORMAT_EXCEPTION = "Did not %s due to incorrect format.";
    private static final String EXECUTE_FORMAT_EXCEPTION = "Did not execute due to %s ";
    private static final String IMAGE_PROMPT_EXCEPTION = "problem with image file.";
    private static final String OUTPUT_INCORRECT_FORMAT_EXCEPTION = "change output method";
    private static final String EMPTY_SET_EXCEPTION = "Did not Execute. Charset is empty.";
    private static final String INCORRECT_COMMAND_EXCEPTION = "incorrect command.";


    private ImageProcessing imageToProcess;
    private final SubImgCharMatcher subImgCharMatcher;
    private final ConsoleAsciiOutput consoleAsciiOutput;
    private final HtmlAsciiOutput htmlAsciiOutput;
    private AsciiOutput outputDirectory;
    private int currResolution;


    /**
     * Constructor. initializes the parameters to default values.
     *
     * @throws IOException if the path to the image isn't correct.
     */
    public Shell() throws IOException {
        this.imageToProcess = new ImageProcessing(new Image(DEFAULT_IMAGE_PATH));
        this.subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHAR_SET);
        this.consoleAsciiOutput = new ConsoleAsciiOutput();
        this.htmlAsciiOutput = new HtmlAsciiOutput(HTML_OUTPUT_PATH, HTML_FONT);
        this.outputDirectory = consoleAsciiOutput;
        this.currResolution = DEFAULT_RESOLUTION;

    }

    /**
     * Translates the user input prompts and execute them.
     */
    public void run() {
        System.out.print(INPUT_PROMPT);
        String[] input = KeyboardInput.readLine().split(String.valueOf(SPACE));
        String inputPrompt = input[0].toLowerCase();
        String inputArg = "";
        if (input.length > 1) {
            inputArg = input[1].toLowerCase();
        }
        while (!inputPrompt.equals(EXIT_PROMPT)) {
            promptProcessing(inputPrompt, inputArg);
            System.out.print(INPUT_PROMPT);
            input = KeyboardInput.readLine().split(String.valueOf(SPACE));
            inputPrompt = input[0].toLowerCase();
            inputArg = "";
            if (input.length > 1) {
                inputArg = input[1].toLowerCase();
            }
        }
    }

    /*
     * Takes in user input and execute the given command or handles error.
     */
    private void promptProcessing(String inputPrompt, String inputArg) {
        try {
            switch (inputPrompt) {
            case CHARS_PROMPT:
                for (char c = SPACE; c <= TILDE; c++) {
                    if (subImgCharMatcher.getCharSet().contains(c)) {
                        System.out.print(c + "" + SPACE);
                    }
                }
                System.out.println();
                break;
            case ADD_PROMPT:
                addPrompt(inputArg);
                break;
            case REMOVE_PROMPT:
                removePrompt(inputArg);
                break;
            case RES_PROMPT:
                changeResolutionPrompt(inputArg);
                break;
            case IMAGE_PROMPT:
                imagePrompt(inputArg);
                break;
            case OUT_PROMPT:
                outputPrompt(inputArg);
                break;
            case ASCII_ART_PROMPT:
                asciiArtPrompt();
                break;
            default:
                commandError();
            }
        } catch (ExecuteException e) {
            System.out.printf(EXECUTE_FORMAT_EXCEPTION, e.getType());
            System.out.println();
        } catch (IncorrectFormatException e) {
            System.out.printf(INCORRECT_FORMAT_EXCEPTION, e.getType());
            System.out.println();
        } catch (ExceedingBoundsException e) {
            System.out.println(e.getType());
        } catch (IOException e) {
            System.out.printf(EXECUTE_FORMAT_EXCEPTION, IMAGE_PROMPT_EXCEPTION);
            System.out.println();
        }
    }

    /*
     * Execute the add prompt
     */
    private void addPrompt(String arg) throws IncorrectFormatException {
        if (arg.length() == 1) {
            subImgCharMatcher.addChar(arg.charAt(0));
        } else if (arg.equals(ALL_PROMPT)) {
            for (char c = SPACE; c <= TILDE; c++) {
                subImgCharMatcher.addChar(c);
            }
        } else if (arg.equals(SPACE_PROMPT)) {
            subImgCharMatcher.addChar(SPACE);
        } else if (arg.length() == VALID_RANGE_INPUT && arg.charAt(1) == VALID_RANGE_SEPARATOR) {
            char startRange = (char)Math.min(arg.charAt(0), arg.charAt(2));
            char endRange = (char)Math.max(arg.charAt(0), arg.charAt(2));
            for (char c = startRange; c <= endRange; c++) {
                subImgCharMatcher.addChar(c);
            }
        } else {
            throw new IncorrectFormatException(ADD_PROMPT);
        }
    }

    /*
     * Execute the remove prompt.
     */
    private void removePrompt(String arg) throws IncorrectFormatException {
        if (arg.length() == 1) {
            subImgCharMatcher.removeChar(arg.charAt(0));
        } else if (arg.equals(ALL_PROMPT)) {
            subImgCharMatcher.getCharSet().clear();
        } else if (arg.equals(SPACE_PROMPT)) {
            subImgCharMatcher.removeChar(SPACE);
        } else if (arg.length() == VALID_RANGE_INPUT && arg.charAt(1) == VALID_RANGE_SEPARATOR) {
            char startRange = (char) Math.min(arg.charAt(0), arg.charAt(2));
            char endRange = (char) Math.max(arg.charAt(0), arg.charAt(2));
            for (char c = startRange; c <= endRange; c++) {
                subImgCharMatcher.removeChar(c);
            }
        } else {
            throw new IncorrectFormatException(REMOVE_PROMPT);
        }
    }

    /*
     * Execute the res up/down prompt.
     */
    private void changeResolutionPrompt(String arg) throws ExceedingBoundsException, IncorrectFormatException {
        if (arg.equals(RES_UP_PROMPT)) {
            if (currResolution * CHANGE_RES_FACTOR > imageToProcess.getMaxCharsInRow()) {
                throw new ExceedingBoundsException(RESOLUTION_EXCEED_BOUNDS_EXCEPTION);
            } else {
                currResolution *= CHANGE_RES_FACTOR;
            }
        } else if (arg.equals(RES_DOWN_PROMPT)) {
            if (currResolution / CHANGE_RES_FACTOR < imageToProcess.getMinCharsInRow()) {
                throw new ExceedingBoundsException(RESOLUTION_EXCEED_BOUNDS_EXCEPTION);
            } else {
                currResolution /= CHANGE_RES_FACTOR;
            }
        } else {
            throw new IncorrectFormatException(RESOLUTION_EXCEPTION_PROMPT);
        }
        System.out.println(CHANGE_RES_PROMPT + currResolution);
    }

    /*
     * Execute the image change prompt.
     */
    private void imagePrompt(String arg) throws IOException {
        imageToProcess = new ImageProcessing(new Image(arg));
    }

    /*
     * Execute the output directory change prompt
     */
    private void outputPrompt(String arg) throws IncorrectFormatException {
        if (arg.equals(CONSOLE_PROMPT)) {
            outputDirectory = consoleAsciiOutput;
        } else if (arg.equals(HTML_PROMPT)) {
            outputDirectory = htmlAsciiOutput;
        } else {
            throw new IncorrectFormatException(OUTPUT_INCORRECT_FORMAT_EXCEPTION);
        }
    }

    /*
     * execute the asciiArt prompt
     */
    private void asciiArtPrompt() throws ExceedingBoundsException {
        if (subImgCharMatcher.getCharSet().isEmpty()) {
            throw new ExceedingBoundsException(EMPTY_SET_EXCEPTION);
        } else {
            AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(imageToProcess, currResolution,
                    subImgCharMatcher);
            outputDirectory.out(asciiArtAlgorithm.run());
        }
    }

    /*
     * Throws exception if the command is invalid.
     */
    private void commandError() throws ExecuteException {
        throw new ExecuteException(INCORRECT_COMMAND_EXCEPTION);
    }

    /**
     * The main method that runs the all program.
     *
     * @param args None cmd arguments will bw used in this program.
     */
    public static void main(String[] args) throws IOException {
            new Shell().run();
    }
}
