import com.forgescriptcompiler.ScriptBase;

/* To run:
 * Place this in %HOMEPATH%/Scripts on the server side
 * Type /run SampleScript in game */
public class SampleScript extends ScriptBase
{
    public void run()
    {
        setWorldTime(4000);
        
        while(getY() > 12)
        {
            mine('s');
            move('s');

            mine('d');

            mine('e');
            move('e');
            
            mine('d');

            mine('n');
            move('n');
            
            mine('d');

            mine('w');
            move('w');
            
            mine('d');
        }
        
        for (int i = 0; i < 15; i++)
        {
            mine('s');
            move('s');
            mine('e');
            mine('w');
        }
    }
}
