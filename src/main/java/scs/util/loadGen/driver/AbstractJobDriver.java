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
	protected double preWarm = 0.0;
	protected double keepAlive = 600000.0;
	protected int coldStartTime = 0;
	protected int invokeTime = 0;

	public static String createCmd[] = new String[]{
			"bash /home/zyy/INFless/developer/servingFunctions/resnet-50-create.sh",
			"bash /home/zyy/INFless/developer/servingFunctions/mobilenet-create.sh",
			"bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/hash-create.sh",
			"bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/Md5-create.sh",
			"bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/hello-create.sh",
			"bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/sort-create.sh",
			"bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/cryptography-create.sh",
	};

	public static String deleteCmd[] = new String[]{
			"bash /home/zyy/INFless/developer/servingFunctions/resnet-50.sh",
			"bash /home/zyy/INFless/developer/servingFunctions/mobilenet.sh",
			"bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/hash.sh",
			"bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/Md5.sh",
			"bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/hello.sh",
			"bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/sort.sh",
			"bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/cryptography.sh",
	};

	public static String FuncName[] = new String[]{
			"resnet-50",
			"mobilenet",
			"hash",
			"Md5",
			"hello",
			"sort",
			"cryptography"
	};
 
	protected abstract void initVariables();//init
	/**
	 * execute job
	 */
	public abstract void executeJob(int serviceId, int type);

	public abstract void coldStartManage(int serviceId);
	
}
