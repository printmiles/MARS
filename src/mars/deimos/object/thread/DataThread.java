package mars.deimos.object.thread;

/**
 * This interface is used by Deimos classes that require some form of concurrency.
 * @author Alex Harris (W4786241)
 */
public interface DataThread extends Runnable
{
  public void start();
  public void stop();
}
