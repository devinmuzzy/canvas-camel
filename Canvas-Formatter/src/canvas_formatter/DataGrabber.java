package canvas_formatter;

import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.*;
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

		int json_array_length = json_array.length();

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

		int json_array_length = json_array.length();

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

		int json_array_length = json_array.length();

		courseList = new String[json_array_length];

		for (int i = 0; i < json_array_length; i++) {
			json_object = (JSONObject) json_array.get(i);
			courseList[i] = "" + json_object.get("id") + "";
		}

		return courseList;
	}

	public JSONObject[] getAllCoursesJSON() throws Exception {
		String api_reference = "/api/v1/courses/";
		JSONObject json_object;
		JSONObject[] courseList;

		JSONArray json_array = Accessor.getJSON(api_reference);
		int json_array_length = json_array.length();

		courseList = new JSONObject[json_array_length];

		for (int i = 0; i < json_array_length; i++) {
			json_object = (JSONObject) json_array.get(i);
			courseList[i] = json_object;
		}

		return courseList;
	}

	/**
	 * overloaded for multiple courses
	 * 
	 * @returns Map<String, List<String>> With Name as key and list of classes as
	 *          list
	 * @param
	 */
	@SuppressWarnings("unused")
	public Map<String, List<courseObj>> getActivitiesMap(String[] course_IDs) throws Exception {
		String name = "";
		String last_login = "";
		JSONObject json_object;
		String api_prefix = "/api/v1/courses/";
		String api_suffix = "/enrollments";
		String api_reference, month, day, year, hour, minute, filePathString;
		int json_array_length;

		Map<String, List<courseObj>> userActivityMap;
		userActivityMap = new HashMap<String, List<courseObj>>();

		JSONArray json_array;
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
				JSONTokener json_tokener = new JSONTokener(json_array_string);
				json_array = (JSONArray) json_tokener.nextValue();
			} else {
				System.out.println("No file found");
				String jsonExtraFilename = "getActivitiesMap_num_" + i + ".json";
				json_array = Accessor.getJSON(api_reference);
				File fw = new File(jsonExtraFilename);
				FileWriter wr = new FileWriter(fw);
				wr.write(json_array.toString(1));
				wr.close();
				System.out.println("created: " + jsonExtraFilename);
			}

			json_array_length = json_array.length();
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
						System.out.println("287 - parse failed");
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

	// FIXME ACCESSS DENIED
	public String[] getUsersCourses(int user_id) throws Exception {
//		String name="";
//		String grade = "";
//		JSONObject json_object;	
//		String api_prefix = "/api/v1/users/";
//		String api_suffix = "/courses";
//		String api_reference;
//		int json_array_length;
//		
//		api_reference = api_prefix + user_id  + api_suffix;
//		JSONArray json_array = Accessor.getJSON(api_reference);
//		
		// Systen.out.println(json_array.length());

		return null;
	}

	public Map<String, List<gradeObj>> getGradesMap(String[] course_IDs) throws Exception {
		String name = "";
		String grade = "";
		JSONObject json_object;
		String api_prefix = "/api/v1/courses/";
		String api_suffix = "/enrollments";
		String api_reference, filePathString;
		int json_array_length;

		JSONArray json_array;

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
				JSONTokener json_tokener = new JSONTokener(json_array_string);
				json_array = (JSONArray) json_tokener.nextValue();
			} else {
				String jsonExtraFilename = "getGradesMap_num_" + i + ".json";
				json_array = Accessor.getJSON(api_reference);
				System.out.println("No file found");
				File fw = new File(jsonExtraFilename);
				FileWriter wr = new FileWriter(fw);
				wr.write(json_array.toString(1));
				wr.close();
				System.out.println("file #" + i + " was created");
			}

			json_array_length = json_array.length();
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

	class courseObj {
		public String courseName;
		public String lastLogin;

		courseObj(String courseName, String lastLogin) {
			this.courseName = courseName;
			this.lastLogin = lastLogin;
		}
	}

	class gradeObj {
		public String courseName;
		public String grade;

		gradeObj(String courseName, String grade) {
			this.courseName = courseName;
			this.grade = grade;
		}
	}

	public String[][] getActivities(String course_ID) throws Exception {
		String api_reference = "/api/v1/courses/" + course_ID + "/enrollments";
		String name = "";
		String last_login = "";
		JSONObject json_object;
		String[][] userActivities;

		JSONArray json_array = Accessor.getJSON(api_reference);

		int json_array_length = json_array.length();
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

		String filePathString = "studentList.txt";
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

				Map<String, List<courseObj>> studentMap;
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
