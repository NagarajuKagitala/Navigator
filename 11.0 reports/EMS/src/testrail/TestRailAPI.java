package testrail;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.json.Json;

import java.io.DataOutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TestRailAPI {

	static String api;
	
	public static APIClient client() throws Exception {
		// TODO make this read from settings/config file

		Settings.read();
		api = Settings.getSettingTestrailAPI();
		String username = Settings.getSettingUsername();
		String password = Settings.getSettingPassword();

		APIClient client = new APIClient(api);
		client.setUser(username);
		client.setPassword(password);

		return client;

	}

	public static JSONObject getCase(int p_caseId) throws Exception {
		APIClient client = TestRailAPI.client();
		// Get case by case id
		JSONObject c = (JSONObject) client.sendGet("get_case/" + p_caseId);

		return c;
	}

	public void getRun(int p_caseId, int status_id, String comment, String methodname) throws Exception {
		APIClient client = TestRailAPI.client();

		try {

			// Read run id
			long testrun_id= Long.parseLong(Settings.getTestRunID());
			
						
					
			Integer run_id = (int) testrun_id;
			

			URL url = new URL(api + "index.php?/api/v2/get_results_for_case/" + run_id + "/" + p_caseId);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.addRequestProperty("Content-Type", "application/json");
			String userCredentials = TestRailAPI.client().getUser() + ":" + TestRailAPI.client().getPassword();
			String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
			conn.addRequestProperty("Authorization", basicAuth);

			 //System.out.println("connection status:" + conn.getResponseCode());
			if (conn.getResponseCode() == 200) {

			
					 System.out.println("Run id: " + testrun_id);
					// System.out.println("Suite id: " + suite_id);

					Map<String, Comparable> data = new HashMap();
					data.put("status_id", new Integer(status_id));
					data.put("comment", comment);


						
						JSONObject r = addResult(status_id, comment, run_id, p_caseId, methodname);
						Thread.sleep(8000);
						

						/*
						 * JSONObject r = (JSONObject) TestRailAPI.client()
						 * .sendPost("add_result_for_case/" + run_id + "/" + testCaseID, data);
						 */
					}
				
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public static void updateRun(int p_caseId, int status_id, String comment, String test_name, String methodname)
			throws Exception {
		int run_id = 0;
		try {
			// Initialising Testrail API client
			APIClient client = TestRailAPI.client();

			// Reading test case by caseid
			JSONObject caseObj = getCase(p_caseId);

			// Binding caseids
			Long[] intArray = { (long) p_caseId };

			// System.out.println("array: "+Arrays.asList(intArray)) ;

			// Binding request data
			Map rundata = new HashMap();

			rundata.put("suite_id", (int) (long) caseObj.get("suite_id"));
			rundata.put("name", test_name);
			rundata.put("assignedto_id", 1);
			rundata.put("include_all", false);
			rundata.put("case_ids", Arrays.asList(intArray));

			// System.out.println(Arrays.asList(rundata));

			// Add test run using project id
			JSONObject runobj = (JSONObject) client.sendPost("add_run/2", rundata);

			// System.out.println("Run data: " + runobj);

			// Assigning created test run id
			run_id = (int) (long) runobj.get("id");

			// Check if test run exists by case
			URL url = new URL(api + "index.php?/api/v2/get_results_for_case/" + run_id + "/" + p_caseId);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.addRequestProperty("Content-Type", "application/json");
			String userCredentials = TestRailAPI.client().getUser() + ":" + TestRailAPI.client().getPassword();
			String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
			conn.addRequestProperty("Authorization", basicAuth);

			// System.out.println("connection status:" + conn.getResponseCode());
			// If exists add result to test case
			if (conn.getResponseCode() == 200) {

				JSONObject r = addResult(status_id, comment, run_id, p_caseId, methodname);

			}

			// JSONObject r=addResult(status_id, comment , run_id, p_caseId);
		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
		}
	}

	public static JSONObject addResult(int p_statusId, String p_comment, int p_runId, int p_caseId, String methodname)
			throws Exception {
		// Read setings
		Settings.read();
		String envstr = Settings.getSettingENV();
		// System.out.println("envstr : "+ envstr);
		String commentstr = p_comment;

		if (!envstr.isEmpty()) {
			commentstr = p_comment + " : " + envstr;
		}
		APIClient client = TestRailAPI.client();

		Map data = new HashMap();
		data.put("status_id", new Integer(p_statusId));
		data.put("comment", commentstr);
		// data.put("defects", "19564");
		JSONObject r = (JSONObject) client.sendPost("add_result_for_case/" + p_runId + "/" + p_caseId, data);

		// Add attachment screenshot to results

		return r;
	}

	public static void Getresults(int p_caseId, String methodname) {
		

		try {
			APIClient client = TestRailAPI.client();
			// Read project id
			long project_id = Long.parseLong(Settings.getSettingProjectId());
			long testrun_id= Long.parseLong(Settings.getTestRunID());
			JSONArray testrunsarr = new JSONArray();
			Integer suite_id = 0;
			Integer run_id = (int) testrun_id;
			String defect_id = "";

			URL url = new URL(api + "index.php?/api/v2/get_results_for_case/" + run_id + "/" + p_caseId);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.addRequestProperty("Content-Type", "application/json");
			String userCredentials = TestRailAPI.client().getUser() + ":" + TestRailAPI.client().getPassword();
			String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
			conn.addRequestProperty("Authorization", basicAuth);

			 //System.out.println("connection status:" + conn.getResponseCode());
			if (conn.getResponseCode() == 200) {

				// Get case by case id
				/*
				 * JSONArray c = (JSONArray) client.sendGet("get_results_for_run/" + run_id);
				 * 
				 * JSONObject obj = (JSONObject) c.get(0); defect_id= (String)
				 * obj.get("defects");
				 * 
				 * System.out.println("defect id: " + defect_id);
				 */
					
					//System.out.println("Tested Run id: " + run_id);
					JSONObject r = UpdateResult(run_id, p_caseId, methodname);
				
				

				/*
				 * JSONObject r = (JSONObject) TestRailAPI.client()
				 * .sendPost("add_result_for_case/" + run_id + "/" + testCaseID, data);
				 */
			}
			
		/*	// Get test runs using project id
			testrunsarr = (JSONArray) client.sendGet("get_runs/" + project_id);

			// System.out.println("test run :" + testrunsarr);
			for (int i = 0; i < testrunsarr.size(); i++) {
				JSONObject testrunobj = (JSONObject) testrunsarr.get(i);
				// Reading project details
				if (testrunobj.get("id") != null) {
					run_id = (int) (long) testrunobj.get("id");
					suite_id = (int) (long) testrunobj.get("suite_id");
					// getTests(run_id,client);
					// System.out.println("Run id: " + run_id);
					// System.out.println("Suite id: " + suite_id);

				

					// Check if test run exists by case
					

				}
			}
*/		
			} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	public static JSONObject UpdateResult(int p_runId, int p_caseId,
			String methodname) {
		try {
			APIClient client = TestRailAPI.client();
			JSONArray resiltobj = (JSONArray) client.sendGet("get_results_for_case/" + p_runId + "/" + p_caseId);

			 //System.out.println("Result: "+ (JSONObject) resiltobj.get(0));

			JSONObject obj = (JSONObject) resiltobj.get(0);

			int result_id = (int) (long) obj.get("id");
			
			int p_statusId =(int) (long) obj.get("status_id");
			
			JSONArray arr=(JSONArray) obj.get("attachment_ids");
			
			//System.out.println("Array size: "+ arr.size());

			//System.out.println("Result id: " + result_id);

			if(arr.size()==0)
			{
			if (p_statusId == 5) {
				JSONObject c = (JSONObject) client.sendPost("add_attachment_to_result/" + result_id,
						"Screenshots/ScreenshotsFailure/" + methodname + ".png");
			} else {
				JSONObject c = (JSONObject) client.sendPost("add_attachment_to_result/" + result_id,
						"Screenshots/ScreenshotsSuccess/" + methodname + ".png");
			}
			}
		} catch (Exception e) {

		}
		return null;

	}

}
