package com.automic;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.uc4.api.FolderListItem;
import com.uc4.api.Template;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.Job;
import com.uc4.api.objects.UC4Object;
import com.uc4.api.systemoverview.ServerListItem;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.CreateObject;
import com.uc4.communication.requests.CreateSession;
import com.uc4.communication.requests.FolderList;
import com.uc4.communication.requests.FolderTree;
import com.uc4.communication.requests.GetObjectProperties;
import com.uc4.communication.requests.OpenObject;
import com.uc4.communication.requests.SaveObject;
import com.uc4.communication.requests.ServerList;


public class GoAutomic {

	/**   
	 * this class illustrates a simple usage of the Automic Java API
	 */

	// 0 - Change the values below to connect to a specific AE Environment
	
	public static String AEHostnameOrIp = "192.168.11.134"; // Automation Engine IP Adr
	public static int AECPPort = 2217; 						// "Primary" Communication Process port
	public static int AEClientToConnect = 1000;				// AE Client Number (0 - 9999)
	public static String AEDepartment = "UC4"; 				// User Department
	public static String AEUserLogin = "UC4"; 				// AE User Login
	public static String AEUserPassword = "uc4"; 			// AE User Password
	public static char AEMessageLanguage = 'E'; 			// Language: 'E' or 'D', or 'F'
	
	
	
		public static void main(String argv[]) throws IOException {
			
			// 1- First, use the static connection object to initiate the connection (see ConnectionManager class for details)
			
			Connection conn = new ConnectionManager().authenticate(
					AEHostnameOrIp, AECPPort, AEClientToConnect, AEUserLogin, AEDepartment, AEUserPassword, AEMessageLanguage);
			
			// 2- Use the connection object created above to query AE
			// -> the Utils class aims at bringing additional functionalities to the AE API, take a look!
			
			Utils utils = new Utils(conn);
			
			ArrayList<IFolder> mylist = utils.getAllFolders(true);
			
			for(int i=0;i<mylist.size();i++){
				System.out.println("+++ Content of: "+mylist.get(i).getName());
				FolderList itemlist = utils.getFolderContent(mylist.get(i));
				Iterator<FolderListItem> it = itemlist.iterator();
				while(it.hasNext()){
					FolderListItem item = it.next();
					System.out.println("    --> "+item.getName());
				}
			}
			
			// 3 - Do not forget to close your connection when finished
			
			conn.close();


		}
	}