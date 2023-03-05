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
	protected Long oldTime;
	protected boolean start = false;
	protected ArrayList<Long> timeList;
 
	protected abstract void initVariables();//init
	/**
	 * execute job
	 * @param requestCount 
	 * @param warmUpCount 
	 * @param pattern 
	 * @param intensity QPS
	 * @return Request result < request sending time, response time >
	 */
	public abstract void executeJob(int serviceId);

	public abstract void coldStartManage(int serviceId);
	
}
