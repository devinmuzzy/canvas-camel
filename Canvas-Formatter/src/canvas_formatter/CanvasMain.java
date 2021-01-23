package canvas_formatter;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import canvas_formatter.DataGrabber.courseObj;
import canvas_formatter.DataGrabber.gradeObj;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

public class CanvasMain {

	final static String SUFFIX = "?access_token=";
	static DataGrabber response;
	static String token;
	static String institution;

	static void updateAuth() {
		String prefix = "https://" + institution;
		response = new DataGrabber(prefix, SUFFIX, token);
		System.out.println("new prefix = " + prefix);
		System.out.println("new token: " + token);
	}

	/**
	 * 
	 * @param map
	 * @param courses
	 */
	@SuppressWarnings("unused")
	public static Object[][] alphabeticArray(Map<String, List<courseObj>> map, String[] courseNames) {
		Object[] orderedKeys = map.keySet().toArray();
		Object[][] alphabeticArray;
		String lastLogin, y_m_d, h_m_s;
		boolean name_toggle;
		String name;

		Arrays.sort(orderedKeys);

		alphabeticArray = new String[orderedKeys.length][courseNames.length + 1];
		for (int j = 1; j < courseNames.length; j++) {
			alphabeticArray[0][0] = "Names";
			alphabeticArray[0][j] = courseNames[j];
		}

		for (int r = 1; r < orderedKeys.length; r++) {

			alphabeticArray[r][0] = orderedKeys[r];

			for (int i = 0; i < map.get(orderedKeys[r]).size(); i++) {
				if (map.get(orderedKeys[r]).get(i).courseName.equals("null")
						|| map.get(orderedKeys[r]).get(i).lastLogin.equals("null")) {
					continue;
				}

				for (int c = 0; c < courseNames.length; c++) {

					if (map.get(orderedKeys[r]).get(i).courseName.equals(alphabeticArray[0][c + 1])) {
						alphabeticArray[r][c + 1] = map.get(orderedKeys[r]).get(i).lastLogin;
						break;
					} else {
						alphabeticArray[r][c + 1] = "NO DATA";
					}
				}
			}
		}
		return alphabeticArray;
	}

	@SuppressWarnings("unused")
	public static void printAlphabetic(Map<String, List<courseObj>> map) {
		Object[] orderedKeys = map.keySet().toArray();
		String lastLogin, y_m_d, h_m_s;
		boolean name_toggle;
		Arrays.sort(orderedKeys);

		for (Object s : orderedKeys) {
			name_toggle = true;
			for (DataGrabber.courseObj c : map.get(s)) {
				if (c.courseName.equals("null") || c.lastLogin.equals("null")) {
					continue;
				}
				if (name_toggle) {
					lastLogin = c.lastLogin;
					y_m_d = lastLogin.substring(0, lastLogin.lastIndexOf('T'));
					h_m_s = lastLogin.substring(lastLogin.lastIndexOf('T') + 1, lastLogin.lastIndexOf('Z'));
					// system.out.print(" COURSE_ID: " + c.courseName + ": LOGIN AT: " + y_m_d + "
					// at " + h_m_s);
					name_toggle = false;
					continue;
				}
				lastLogin = c.lastLogin;
				y_m_d = lastLogin.substring(0, lastLogin.lastIndexOf('T'));
				h_m_s = lastLogin.substring(lastLogin.lastIndexOf('T') + 1, lastLogin.lastIndexOf('Z'));
				// system.out.print(" COURSE_ID: " + c.courseName + ": LOGIN AT: " + y_m_d + "
				// at " + h_m_s);
			}
			// system.out.println("");

		}
	}

	public static Object[][] getDoubleArray(String tokenString) {
		return null;
	}

	public static Object[] getTableHeaders(String tokenString) {
		return null;
	}

	public static String[] getKeys(String course_ID) {
		// DataGrabber response = new DataGrabber(MOM_PREFIX, SUFFIX, MOM_TOKEN);
		String[] keyArray;

		HashMap<String, String> logins = new HashMap<String, String>();

		Set<String> keySet = logins.keySet();

		keyArray = new String[keySet.size()];

		int i = 0;
		for (String s : keySet) {
			keyArray[i] = s;
			i++;
		}

		return keyArray;
	}

	public static String[][] getValues(String course_ID) {

		// DataGrabber response = new DataGrabber(MOM_PREFIX, SUFFIX, MOM_TOKEN);
		String[][] keyArray;

		HashMap<String, String> logins = new HashMap<String, String>();

		// response.fillLoginMap(logins, course_ID);

		Set<String> keySet = logins.keySet();

		keyArray = new String[keySet.size()][keySet.size()];

		int i = 0;
		for (String s : keySet) {
			keyArray[i][1] = logins.get(s);
			i++;
		}

		int j = 0;
		for (String s : keySet) {
			keyArray[j][0] = s;
			j++;

		}

		return keyArray;
	}

	public static String[] testGetKeys() {

		// DataGrabber response = new DataGrabber(MOM_PREFIX, SUFFIX, MOM_TOKEN);
		String[] keyArray;

		// String course_ID = "9537";

		HashMap<String, String> logins = new HashMap<String, String>();

		// response.fillLoginMap(logins, course_ID);

		Set<String> keySet = logins.keySet();

		keyArray = new String[keySet.size()];

		int i = 0;
		for (String s : keySet) {
			keyArray[i] = s;
			i++;
		}

		return keyArray;

	}

	public static String[][] testGetValues() {

		// DataGrabber response = new DataGrabber(MOM_PREFIX, SUFFIX, MOM_TOKEN);
		String[][] keyArray;

		// String course_ID = "9537";

		HashMap<String, String> logins = new HashMap<String, String>();

		// response.fillLoginMap(logins, course_ID);

		Set<String> keySet = logins.keySet();

		keyArray = new String[keySet.size()][keySet.size()];

		int i = 0;
		for (String s : keySet) {
			keyArray[i][1] = logins.get(s);
			i++;
		}

		int j = 0;
		for (String s : keySet) {
			keyArray[j][0] = s;
			j++;
		}

		return keyArray;
	}

	public static Object[][] getActivityTableArray(List<String> classList, List<String> students) {

		try {

			// ugh spaghetti code incoming
			String[] classNameList = CanvasMain.getClassList();
			String[] classIDList = CanvasMain.getClassListIDs();
			List<String> chosenIDs = new ArrayList<>();

			for (int i = 0; i < classNameList.length; i++) {
				for (int j = 0; j < classList.size(); j++) {
					if (classNameList[i].equals(classList.get(j))) {
						chosenIDs.add(classIDList[i]);
					}
				}
			}
			// FIXME
			// for(String k: chosenIDs) {
			// system.out.println("ID: " + k + " was chosen");
			// }

			String[] chosenIDsArray = chosenIDs.toArray(new String[0]);

			Map<String, List<courseObj>> activitiesMap = response.getActivitiesMap(chosenIDsArray); // FIXME probably
																									// the issue

			Object[][] table;

			table = new Object[students.size() + 1][classList.size() + 1];

			// set column headers
			table[0][0] = "Students";

			for (int i = 0; i < classList.size(); i++) {
				table[0][i + 1] = classList.get(i);
			}

			// set row headers
			for (int i = 0; i < students.size(); i++) {
				table[i + 1][0] = students.get(i);
			}

			String currentCourse, student;
			List<courseObj> studentCourses;

			for (int i = 1; i < students.size(); i++) {
				student = students.get(i);
				studentCourses = activitiesMap.get(student); // FIXME issue?
				if (studentCourses == null) {
					continue;
				}

				for (int j = 1; j < classList.size(); j++) {
					currentCourse = chosenIDs.get(j - 1);
					for (courseObj c : studentCourses) {
						if (c == null) {
							table[i][j] = "no known grade" + i + j;
							continue;
						}

						// system.out.println("(460) c.courseName:" + c.courseName + " currentCourse:"
						// +currentCourse);

						if (c.lastLogin.contains("null")) {
							table[i][j] = "No Login";
						}

						if (c.courseName.equals(currentCourse)) {
							table[i][j] = c.lastLogin;
							// system.out.println("AYYYYEEEE: " + c.lastLogin);
							break;
						}
					}
				}
			}

			return table;

		} catch (Exception e) {
			throwErrorMessage(e.getMessage());
			// system.out.println("bummer!");
			// system.out.println(e.getLocalizedMessage());

			for (StackTraceElement t : e.getStackTrace()) {
				// system.out.println(t);
				t.getLineNumber();
			}
			return null;
		}

	}

	public static Object[][] getReportTableArray(List<String> classList, List<String> students) {

		Object[][] activity;
		Object[][] grades;
		Object[][] table, tempTable;
		int finalCol;

		grades = getGradesTableArray(classList, students);
		activity = getActivityTableArray(classList, students);

		finalCol = grades[0].length;
		table = new Object[4][finalCol];

		for (int r = 0; r < 2; r++) {
			for (int c = 0, c2 = 0; c < activity[0].length && c2 >= 0; c++, c2++) {

				table[r][c] = activity[r][c2];
				if (table[r][c] == null) {
					table[r][c] = "Redacted";
					finalCol--;
				} else if (table[r][c].equals("No Login")) {
					table[0][c] = "Redacted";
					table[1][c] = "Redacted";
					table[2][c] = "Redacted";
					finalCol--;
				}
			}
		}
		finalCol++;

		table[0][0] = table[1][0];
		table[1][0] = "Last Activity";
		table[2][0] = "Grades";
		for (int c = 1; c < grades[0].length; c++) {
			table[2][c] = grades[1][c];
		}

		tempTable = new Object[4][finalCol];
		int t = 0;
		for (int c = 0; c < grades[0].length; c++) {
			if (!table[1][c].equals("Redacted")) {
				tempTable[0][t] = table[0][c];
				tempTable[1][t] = table[1][c];
				tempTable[2][t] = table[2][c];
				t++;
			}
		}
		return tempTable;
	}

	public static Object[][] getGradesTableArray(List<String> classList, List<String> students) {

		try {

			// ugh spaghetti code incoming
			String[] classNameList = CanvasMain.getClassList();
			String[] classIDList = CanvasMain.getClassListIDs();
			List<String> chosenIDs = new ArrayList<>();

			for (int i = 0; i < classNameList.length; i++) {
				for (int j = 0; j < classList.size(); j++) {
					if (classNameList[i].equals(classList.get(j))) {
						chosenIDs.add(classIDList[i]);
					}
				}
			}
			// FIXME
//		        for(String k: chosenIDs) {
//		        	//system.out.println("ID: " + k + " was chosen");
//		        }

			String[] chosenIDsArray = chosenIDs.toArray(new String[0]);

			Map<String, List<gradeObj>> gradesMap = response.getGradesMap(chosenIDsArray); // FIXME probably the issue

			Object[][] table;

			table = new Object[students.size() + 1][classList.size() + 1];

			// set column headers
			table[0][0] = "Students";

			for (int i = 0; i < classList.size(); i++) {
				table[0][i + 1] = classList.get(i); // FIXME changed from i = 1 to i+1 where needed
			}

			// set row headers
			for (int i = 0; i < students.size(); i++) {
				table[i + 1][0] = students.get(i);
			}

			String currentCourse, student;
			List<gradeObj> studentCourses;

			for (int i = 1; i < students.size(); i++) {
				student = students.get(i);
				studentCourses = gradesMap.get(student); // FIXME issue?
				if (studentCourses == null) {
					continue;
				}
				for (int j = 1; j < classList.size(); j++) {
					currentCourse = chosenIDs.get(j - 1);
					for (gradeObj c : studentCourses) {
						if (c == null) {
							table[i][j] = "no known grade" + i + j;
							continue;
						}

						// system.out.println("(460) c.courseName:" + c.courseName + " currentCourse:"
						// +currentCourse);

						if (c.grade.contains("null")) {
							table[i][j] = "No Grade";
						}

						if (c.courseName.equals(currentCourse)) {
							table[i][j] = c.grade;
							// system.out.println("AYYYYEEEE: " + c.grade);
							break;
						}
					}
				}
			}
			return table;

		} catch (Exception e) {
			throwErrorMessage(e.getMessage());
			// system.out.println("bummer!");
			// system.out.println(e.getLocalizedMessage());

			for (StackTraceElement t : e.getStackTrace()) {
				t.getLineNumber();
				// system.out.println(t);
			}
			return null;
		}

	}

	public static String[] getClassList() {
		if (response == null) {
			return new String[0];
		}
		try {
			return response.getCourseNames();

			// return response.getCourseNames();
		} catch (Exception e) {
			throwErrorMessage(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public static String[] getClassListIDs() {
		if (response == null) {
			return new String[0];
		}
		try {
			return response.getCourseIDs();

			// return response.getCourseNames();
		} catch (Exception e) {
			throwErrorMessage(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public static String[] getStudentList() {
		if (response == null) {
			return new String[0];
		}
		String[] sorted = response.getStudentList();
		Arrays.sort(sorted);
		System.out.println("GET STUDENT LIST WAS CALLED IN CANVAS MAIN");
		return sorted;
	}

	static String getToken() {
		if (token == null) {
			return "No token found";
		}
		return token;
	}

	static String getInstitution() {
		if (institution == null) {
			return "No token found";
		}
		return institution;
	}

	static void setToken(String passed_token) {
		token = passed_token;
		System.out.println("passed token: " + passed_token);
		updateAuth();

	}

	static void setInstitution(String passed_institution) {
		institution = passed_institution;
		System.out.println("passed institution: " + passed_institution);

		updateAuth();
	}

	// FIXME add would you like to save preferences frame + underlying JSON
	static void deleteOnExit() {
		System.out.println("CLOSING");
		File f;
		String filePathString;
		int loops;

		try {
			f = new File("studentList.txt");
			if (f.exists())
				f.delete();
			System.out.println("Deleted studentList.txt");
			loops = response.getCourseIDs().length;
			for (int i = 0; i < loops; i++) {
				filePathString = "getActivitiesMap_num_" + i + ".json";
				f = new File(filePathString);
				if (f.exists()) {
					f.delete();
					System.out.println("Deleted " + filePathString);
				}
			}
			for (int i = 0; i < loops; i++) {
				filePathString = "getGradesMap_num_" + i + ".json";
				f = new File(filePathString);
				if (f.exists()) {
					f.delete();
					System.out.println("Deleted " + filePathString);
				}
			}
			f = new File("preferences.json");
			if (f.exists())
				f.delete();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throwErrorMessage(e.getMessage());
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	static Map<String, List<String>[]> loadPreferences() {
		Map<String, List<String>[]> loadedMap;
		loadedMap = new HashMap<String, List<String>[]>();
		org.json.simple.JSONObject groupObj;
		org.json.simple.JSONArray keysArray;
		org.json.simple.JSONArray studentsArray;
		org.json.simple.JSONArray classesArray;
		org.json.simple.JSONArray currentGroup;
		List<String> students;
		List<String> classes;
		List<String>[] groupList;

		String currentKey;

		try {
			File f = new File("preferences.json");
			if (f.exists()) {
				System.out.println("loading preferences");
				JSONParser p = new JSONParser();
				JSONObject jsonObj = (JSONObject) p.parse(new FileReader(f));
				token = (String) jsonObj.get("token");
				institution = (String) jsonObj.get("institution");
				keysArray = (org.json.simple.JSONArray) jsonObj.get("keys array");
				groupObj = (JSONObject) jsonObj.get("groups");

				for (int i = 0; i < keysArray.size(); i++) {
					currentKey = (String) keysArray.get(i);
					currentGroup = (org.json.simple.JSONArray) groupObj.get(currentKey);
					classesArray = (org.json.simple.JSONArray) currentGroup.get(0);
					studentsArray = (org.json.simple.JSONArray) currentGroup.get(1);
					classes = new ArrayList<>();
					students = new ArrayList<>();

					for (int c = 0; c < classesArray.size(); c++) {
						classes.add((String) classesArray.get(c));
					}
					for (int s = 0; s < studentsArray.size(); s++) {
						students.add((String) studentsArray.get(s));
					}

					groupList = new List[2];
					groupList[0] = classes;
					groupList[1] = students;

					loadedMap.put(currentKey, groupList);

				}

				setToken(token);
				setInstitution(institution);

				return loadedMap;
			}

			else {
				WelcomeFrame wf = new WelcomeFrame();
				return loadedMap;
			}
		} catch (Exception e) {
			WelcomeFrame wf = new WelcomeFrame();
			e.printStackTrace();
			throwErrorMessage(e.getMessage());
			return new HashMap<String, List<String>[]>();

		}

	}

	static void clearCache() {
		File f;
		String filePathString;
		int loops;
		try {
			f = new File("preferences.json");
			if (f.exists())
				f.delete();
			f = new File("studentList.txt");
			if (f.exists())
				f.delete();
			System.out.println("Deleted studentList.txt");
			loops = response.getCourseIDs().length;
			for (int i = 0; i < loops; i++) {
				filePathString = "getActivitiesMap_num_" + i + ".json";
				f = new File(filePathString);
				if (f.exists()) {
					f.delete();
					System.out.println("Deleted " + filePathString);
				}
			}
			for (int i = 0; i < loops; i++) {
				filePathString = "getGradesMap_num_" + i + ".json";
				f = new File(filePathString);
				if (f.exists()) {
					f.delete();
					System.out.println("Deleted " + filePathString);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throwErrorMessage(e.getMessage());
			e.printStackTrace();
		}

	}

	// save token, institution, groups
	static void saveOnExit(Map<String, List<String>[]> groups) {

		org.json.JSONObject tempObj;
		try {
			File fw = new File("preferences.json");

			org.json.JSONObject jo = new org.json.JSONObject();
			jo.put("token", token);
			jo.put("institution", institution);
			if (groups == null)
				return;
			jo.put("groups", new JSONObject());
			jo.put("keys array", groups.keySet().toArray(new String[0]));

			tempObj = (org.json.JSONObject) jo.get("groups");
			for (String k : groups.keySet()) {
				tempObj.put(k, groups.get(k));
			}

			FileWriter wr = new FileWriter(fw);
			wr.write(jo.toString());
			wr.close();
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	static void throwErrorMessage(String message, int severity) {
		JOptionPane.showMessageDialog(new JFrame(), "Error: " + message, "Error", severity);
	}

	static void throwErrorMessage(String message) {
		throwErrorMessage(message, 0);
	}

}
