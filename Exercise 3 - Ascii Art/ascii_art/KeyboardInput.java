package ascii_art;

import java.util.Scanner;

/**
 * Singleton. Responsible for receiving input from the user.
 */
class KeyboardInput
{
    private static KeyboardInput keyboardInputObject = null;
    private Scanner scanner;
    
    private KeyboardInput()
    {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Create an instance of this class.
     *
     * @return The instance.
     */
    public static KeyboardInput getObject()
    {
        if(KeyboardInput.keyboardInputObject == null)
        {
            KeyboardInput.keyboardInputObject = new KeyboardInput();
        }
        return KeyboardInput.keyboardInputObject;
    }

    /**
     * read the input from the console.
     *
     * @return The input as a String.
     */
    public static String readLine()
    {
        return KeyboardInput.getObject().scanner.nextLine().trim();
    }
}