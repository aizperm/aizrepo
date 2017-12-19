import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class InputOutput
{
    private String number;
    private byte[] input;
    private byte[] output;

    public InputOutput(String number)
    {
        super();
        this.number = number;
        read();
    }

    private void read()
    {
        try
        {
            input = IOUtils.toByteArray(InputOutput.class.getResourceAsStream(String.format("input%s.txt", number)));
            output = IOUtils.toByteArray(InputOutput.class.getResourceAsStream(String.format("output%s.txt", number)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String getInputString()
    {
        return new String(input);
    }

    public String getOutputString()
    {
        return new String(output);
    }

    public byte[] getInput()
    {
        return input;
    }

    public byte[] getOutput()
    {
        return output;
    }

}
