package canvas_formatter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.util.Scanner;



public class DataGrabber {
	private String prefix;
	private String suffix;
	private String token;
	private WebAccess Accessor;
	final String SUFFIX = "?access_token=";
	ArrayList<String> studentList;

	DataGrabber(String prefix, String suffix, String token) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.token = token;
		Accessor = new WebAccess(this.prefix, this.suffix, this.token);
	}

	public String[] getAllCourses() throws Exception {
		String api_reference = "/api/v1/courses/";
		JSONObject json_object;
		String[] courseList;

		JSONArray json_array = Accessor.getJSON(api_reference);

		int json_array_length = json_array.size();

		courseList = new String[json_array_length];

		for (int i = 0; i < json_array_length; i++) {
			json_object = (JSONObject) json_array.get(i);
			courseList[i] = "" + json_object.get("id") + "";
		}

		return courseList;
	}

	public String[] getCourseNames() throws Exception {
		String api_reference = "/api/v1/courses/";
		JSONObject json_object;
		String[] courseList;

		JSONArray json_array = Accessor.getJSON(api_reference);

		int json_array_length = json_array.size();

		courseList = new String[json_array_length];

		for (int i = 0; i < json_array_length; i++) {
			json_object = (JSONObject) json_array.get(i);
			courseList[i] = "" + json_object.get("name") + "";
		}

		return courseList;
	}

	public String[] getCourseIDs() throws Exception {
		String api_reference = "/api/v1/courses/";
		JSONObject json_object;
		String[] courseList;

		JSONArray json_array = Accessor.getJSON(api_reference);

		int json_array_length = json_array.size();

		courseList = new String[json_array_length];

		for (int i = 0; i < json_array_length; i++) {
			json_object = (JSONObject) json_array.get(i);
			courseList[i] = "" + json_object.get("id") + "";
		}

		return courseList;
	}

	public String toCourseID(String course_name) throws Exception {
		String[][] ref;
		ref = new String[2][];
		ref[0] = getCourseNames();
		ref[1] = getCourseIDs();
		for (int i = 0; i < ref[0].length; i++) {
			System.out.println(course_name + " is not " + ref[0][i]);
			if (course_name.equals(ref[0][i]))
				return ref[1][i];
		}
		return "No ID found.";
	}

	public JSONObject[] getAllCoursesJSON() throws Exception {
		String api_reference = "/api/v1/courses/";
		JSONObject json_object;
		JSONObject[] courseList;

		JSONArray json_array = Accessor.getJSON(api_reference);
		int json_array_length = json_array.size();

		courseList = new JSONObject[json_array_length];

		for (int i = 0; i < json_array_length; i++) {
			json_object = (JSONObject) json_array.get(i);
			courseList[i] = json_object;
		}

		return courseList;
	}

	
	
	
	
	
	public Map<String, List<DataHolder>> getActivitiesMap(String[] course_IDs) throws Exception{
		return getUserMap(course_IDs, new gradeObj("", ""), "last_activity_at");
	}
	
	
	/**
	 * overloaded for multiple courses
	 * 
	 * @returns Map<String, List<String>> With Name as key and list of classes as
	 *          list
	 * @param
	 */
	@Deprecated
	@SuppressWarnings("unused")
	public Map<String, List<courseObj>> getActivitiesMap(String[] course_IDs, boolean useOld) throws Exception {
		String name = "";
		String last_login = "";
		JSONObject json_object;
		String api_prefix = "/api/v1/courses/";
		String api_suffix = "/enrollments";
		String api_reference, month, day, year, hour, minute, filePathString;
		int json_array_length;

		Map<String, List<courseObj>> userActivityMap;
		userActivityMap = new HashMap<String, List<courseObj>>();

		JSONArray json_array = null;
		for (int i = 0; i < course_IDs.length; i++) {
			api_reference = api_prefix + course_IDs[i] + api_suffix;

			filePathString = "getActivitiesMap_num_" + i + ".json";
			File f = new File(filePathString);
			if (f.exists() && !f.isDirectory()) {
				System.out.println("found: " + filePathString);
				Scanner sc = new Scanner(f);
				String json_array_string = "";
				while (sc.hasNext()) {
					json_array_string = json_array_string + sc.nextLine();
				}
				sc.close();
//				JSONTokener json_tokener = new JSONTokener(json_array_string);
//				json_array = (JSONArray) json_tokener.nextValue();
			} else {
				System.out.println("No file found");
				String jsonExtraFilename = "getActivitiesMap_num_" + i + ".json";
				json_array = Accessor.getJSON(api_reference);
				File fw = new File(jsonExtraFilename);
				FileWriter wr = new FileWriter(fw);
				wr.write(json_array.toJSONString());
				wr.close();
				System.out.println("created: " + jsonExtraFilename);
			}

			json_array_length = json_array.size();
			for (int j = 0; j < json_array_length; j++) {
				json_object = (JSONObject) json_array.get(j);
				name = (String) ((JSONObject) json_object.get("user")).get("name");

				System.out.println("line 267 " + name);

				last_login = json_object.get("last_activity_at") + "";
				try {
					last_login = last_login.substring(0, last_login.length() - 1); // remove Z
					SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					SimpleDateFormat output = new SimpleDateFormat("MM/dd : HH:mm");
					Date d = null;
					try {
						d = input.parse(last_login);
					} catch (ParseException e) {
						// e.printStackTrace();
						System.out.println(e.getCause());
						System.out.println("287 - parse failed: " + e.getMessage());
					}
					Calendar cal = Calendar.getInstance();
					cal.setTime(d);
					cal.add(Calendar.HOUR, -8); // set back 4 hours
					String formatted = output.format(cal.getTime());
					last_login = formatted;
				} catch (Exception e) {
					System.out.println(e.getCause());
				} finally {
					if (!userActivityMap.containsKey(name)) {
						userActivityMap.put(name, new ArrayList<courseObj>());
					}
					userActivityMap.get(name).add(new courseObj(course_IDs[i], last_login));
					System.out.println(name + " " + course_IDs[i] + " " + last_login);
				}

			}
		}

		return userActivityMap;
	}
	
	
	
	
	public String formatTime(String time, int offset) throws Exception{
			time = time.substring(0, time.length() - 1); // remove Z
			SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			SimpleDateFormat output = new SimpleDateFormat("MM/dd : HH:mm");
			Date d = null;
			try {
				d = input.parse(time);
			} catch (ParseException e) {

				System.out.println("287 - parse failed");
				return "parse failed";
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.HOUR, offset); 
			String formatted = output.format(cal.getTime());
			time = formatted;
			return time;
	}
	
	/**
	 * Less redundant code
	 * @param courseIDs
	 * @param holder
	 * @param request_id
	 * @return
	 */

	public Map<String, List<DataHolder>> getUserMap(String[] courseIDs, DataHolder holder, String request_type) throws Exception{
		String name = "";
		JSONObject json_object;
		String api_prefix = "/api/v1/courses/";
		String api_suffix = "/enrollments";
		String api_reference, filePathString;
		String data;
		String last_login = "";
		Map<String, List<DataHolder>> userMap;
		JSONArray json_array;		
		userMap = new HashMap<String, List<DataHolder>>();
		String dataDirectory = System.getProperty("user.dir");
		dataDirectory = dataDirectory + File.separator + "pulled_data" + File.separator;
		for (int i = 0; i < courseIDs.length; i++) {
			
			CanvasMain.updateBar(1);
			
			api_reference = api_prefix + courseIDs[i] + api_suffix;
			filePathString = dataDirectory + request_type + courseIDs[i] + ".json";
			json_array = fileToJSONArray(filePathString);
			if (json_array == null) // no file was found
				json_array = makeJSONArray(api_reference, filePathString);
			
			for(Object jsonObj: json_array) {
				
				json_object = (JSONObject)jsonObj;
				name = (String) ((JSONObject) json_object.get("user")).get("name");
				try {
					switch(request_type) {
					case "grades":
						JSONObject jgo = (JSONObject) json_object.get("grades");
						System.out.println(jgo.toJSONString());
						data = (String) "" + jgo.get("current_score");
						break;
					case "last_activity_at":
						last_login = json_object.get("last_activity_at") + "";
						data = formatTime(last_login, -8); // -8 is the offset
						break;
					default:
						data = request_type + " not found";
					}
				}
				catch(Exception e) {
					data = "NO DATA FOUND";
					System.out.println("error at 297");
					//e.printStackTrace();
				}
			
			
			if (!userMap.containsKey(name)) {
				userMap.put(name, new ArrayList<DataHolder>());
			}
			if(request_type.equals("grades")) { // garbage code but too lazy to fix
				userMap.get(name).add(new gradeObj(courseIDs[i], data));
			}
			else if(request_type.equals("last_activity_at")) { // garbage code but too lazy to fix
				userMap.get(name).add(new courseObj(courseIDs[i], data));
			}

			}

		}
		return userMap;
	}
	
	

	/**
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public JSONArray fileToJSONArray(String filename) throws Exception{
		JSONArray json_array;
		File f = new File(filename);
		if (f.exists()) {
			JSONParser p = new JSONParser();
			Object jsonObj = p.parse(new FileReader(f));
				
			if(jsonObj instanceof JSONObject) {
				json_array = new JSONArray();
				json_array.add(jsonObj);
			}
			else {
				json_array = (JSONArray) jsonObj;
			}
			return json_array;
		}
		return null;
	}
	
	public JSONArray makeJSONArray(String api_reference, String filename) throws Exception {	
		JSONArray json_array = Accessor.getJSON(api_reference);
		File fw = new File(filename);
		FileWriter wr = new FileWriter(fw);
		
		wr.write(json_array.toJSONString());
		wr.close();
		
		
		return json_array;
	}
	
	public Map<String, List<DataHolder>> getGradesMap(String[] course_IDs) throws Exception {
		return getUserMap(course_IDs, new gradeObj("",""), "grades");
	}

	
	
	@Deprecated
	public Map<String, List<gradeObj>> getGradesMap(String[] course_IDs, boolean useOld) throws Exception {
		String name = "";
		String grade = "";
		JSONObject json_object;
		String api_prefix = "/api/v1/courses/";
		String api_suffix = "/enrollments";
		String api_reference, filePathString;
		int json_array_length;

		JSONArray json_array = null;

		Map<String, List<gradeObj>> userGradesMap;
		userGradesMap = new HashMap<String, List<gradeObj>>();

		for (int i = 0; i < course_IDs.length; i++) {
			api_reference = api_prefix + course_IDs[i] + api_suffix;

			filePathString = "getGradesMap_num_" + i + ".json";
			File f = new File(filePathString);
			if (f.exists() && !f.isDirectory()) {
				System.out.println("File exists");
				Scanner sc = new Scanner(f);
				String json_array_string = "";
				while (sc.hasNext()) {
					json_array_string = json_array_string + sc.nextLine();
				}

				sc.close();
//				JSONTokener json_tokener = new JSONTokener(json_array_string);
//				json_array = (JSONArray) json_tokener.nextValue();
			} else {
				String jsonExtraFilename = "getGradesMap_num_" + i + ".json";
				json_array = Accessor.getJSON(api_reference);
				System.out.println("No file found");
				File fw = new File(jsonExtraFilename);
				FileWriter wr = new FileWriter(fw);
				wr.write(json_array.toJSONString());
				wr.close();
				System.out.println("file #" + i + " was created");
			}

			json_array_length = json_array.size();
			for (int j = 0; j < json_array_length; j++) {
				json_object = (JSONObject) json_array.get(j);
				name = (String) ((JSONObject) json_object.get("user")).get("name");
				try {
					grade = ((JSONObject) json_object.get("grades")).get("current_score") + "";
					// System.out.println("(at line 302 with j = " + j + " Grade was: " + grade); //
					// TODO

				} catch (Exception e) {
					grade = "Error \"grade\" not found";
				} finally {

					// Systen.out.println("GRADE: " + grade);

					if (!userGradesMap.containsKey(name)) {
						userGradesMap.put(name, new ArrayList<gradeObj>());
					}
					userGradesMap.get(name).add(new gradeObj(course_IDs[i], grade));

//					if(name.equals("Taylor Murray"))
//						System.out.println("Submitted grade for :" + course_IDs[i] + " as " + name);

				}
			}
		}

		return userGradesMap;
	}

	class courseObj implements DataHolder{
		public String courseName;
		public String lastLogin;

		courseObj(String courseName, String lastLogin) {
			this.courseName = courseName;
			this.lastLogin = lastLogin;
		}
		
		public String getCourse() {
			return this.courseName;
		}
		public String getData() {
			return lastLogin;
		}
		public void setData(String data) {
			lastLogin = data;
		}
		public void setCourse(String course) {
			courseName = course;
		}
	}

	class gradeObj implements DataHolder{
		public String courseName;
		public String grade;

		gradeObj(String courseName, String grade) {
			this.courseName = courseName;
			this.grade = grade;
		}
		
		public String getCourse() {
			return this.courseName;
		}
		public String getData() {
			return grade;
		}
		public void setData(String data) {
			grade = data;
		}
		public void setCourse(String course) {
			courseName = course;
		}
	}
	
	
	
	
	
	
	
	class dataHolder{
		public String courseName;
		public String data;
		
		dataHolder(String courseName, String data){
			this.courseName = courseName;
			this.data = data;
		}
	}
	

	public String[][] getActivities(String course_ID) throws Exception {
		String api_reference = "/api/v1/courses/" + course_ID + "/enrollments";
		String name = "";
		String last_login = "";
		JSONObject json_object;
		String[][] userActivities;

		JSONArray json_array = Accessor.getJSON(api_reference);

		int json_array_length = json_array.size();
		userActivities = new String[2][json_array_length];

		for (int i = 0; i < json_array_length; i++) {
			json_object = (JSONObject) json_array.get(i);
			name = (String) ((JSONObject) json_object.get("user")).get("name");
			last_login = json_object.get("last_activity_at") + "";
			userActivities[0][i] = name;
			userActivities[1][i] = last_login;
		}
		return userActivities;
	}

	public String[] getStudentList() {
		if (studentList != null) {
			return studentList.toArray(new String[0]);
		}
		String dataDirectory = System.getProperty("user.dir");
		dataDirectory = dataDirectory + File.separator + "pulled_data" + File.separator;
		String filePathString = "studentList.txt";
		filePathString = dataDirectory + filePathString;
		
		try {
			// load file
			File f = new File(filePathString);
			if (f.exists() && !f.isDirectory()) {
				studentList = new ArrayList<>();
				System.out.println("found: " + filePathString);
				Scanner sc = new Scanner(f);

				while (sc.hasNext()) {
					studentList.add(sc.nextLine());
				}
				sc.close();
				return studentList.toArray(new String[0]);
			}

			else {
				// make new file
				System.out.println(filePathString + " not found");
				File fw = new File(filePathString);
				FileWriter wr = new FileWriter(fw);

				String[] courses = this.getAllCourses();

				Map<String, List<DataHolder>> studentMap;
				studentMap = this.getActivitiesMap(courses);
				System.out.println("Printing names");
				for (String s : studentMap.keySet()) {
					System.out.println(s);
					wr.write((String) s + "\n");
				}

				wr.close();
				System.out.println(filePathString + " was created");
				System.out.println("GOING RECURSIVE");
				return getStudentList();

			}

		} catch (Exception e) {
			CanvasMain.throwErrorMessage(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
