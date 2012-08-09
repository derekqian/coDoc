/**
 * 
 */
package vdt.hdvd.Logging;

/**
 * @author michael
 * Class used as input to Display.getDefault().syncExec
 * Used to hold the log line to write.
 */
public abstract class ProcessLogViewContainer implements Runnable
{

    public ProcessLogViewContainer(String log_entry)
    {
        this._log_entry = log_entry;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public abstract void run();

    protected String _log_entry;

}
