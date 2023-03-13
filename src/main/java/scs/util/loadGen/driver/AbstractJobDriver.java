package scs.util.loadGen.driver;
  
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.http.impl.client.CloseableHttpClient;  
 

public abstract class AbstractJobDriver {

	protected List<String> queryItemsList=new ArrayList<String>();//Query word list
	protected int queryItemListSize;
	public String queryItemsStr="";//Query link
	protected String jsonParmStr="";
	
	protected Random random=new Random();  
	protected CloseableHttpClient httpClient;
	protected long oldTime;
	protected boolean start = false;
	protected ArrayList<Long> timeList = new ArrayList<>();
	protected Double standard = 0.0;
	protected Double mean = 0.0;
	protected Double cv = 0.0;
 
	protected abstract void initVariables();//init
	/**
	 * execute job
	 * @param requestCount 
	 * @param warmUpCount 
	 * @param pattern 
	 * @param intensity QPS
	 * @return Request result < request sending time, response time >
	 */
	public abstract void executeJob(int serviceId, int type);

	public abstract void coldStartManage(int serviceId);
	
}
