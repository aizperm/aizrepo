import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Test;

public class TestRunner
{
    @Test
    public void test00() throws Exception
    {
        InputOutput inOut = new InputOutput("00");
        System.setIn(new ByteArrayInputStream(inOut.getInput()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Solution.main(new String[0]);

        String exp = inOut.getOutputString().trim().replaceAll("\r\n","\n");
        String act = new String(out.toByteArray()).trim().replaceAll("\r\n","\n");
        Assert.assertEquals(exp, act);
    }
}
