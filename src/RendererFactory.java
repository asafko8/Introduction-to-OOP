/**
 * Responsible for mapping the string from the command line to the appropriate rendering interface.
 */
public class RendererFactory {

    private static final String CONSOLE = "console";
    private static final String NONE = "none";

    /**
     * Constructs a new "RendererFactory".
     */
    public RendererFactory() { }

    /**
     * Build a new renderer according to a string given in the command line.
     * @param type The type of the renderer that will be created.
     * @param size The size of the board that will display.
     * @return An instance of the new renderer or null in case of invalid type.
     */
    public Renderer buildRenderer(String type, int size) {
        type = type.toLowerCase();
        switch (type) {
            case CONSOLE:
                return new ConsoleRenderer(size);
            case NONE:
                return new VoidRenderer();
            default:
                return null;
        }
    }
}
