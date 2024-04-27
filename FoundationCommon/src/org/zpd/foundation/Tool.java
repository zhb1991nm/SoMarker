package org.zpd.foundation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Tool {

	private static Tool _tool;

	public static Tool instance() {
		if (_tool == null) {
			_tool = new Tool();
		}
		return _tool;
	}

	public void writeFile(String content, String fileName) {
		try {
			File f = new File(fileName);
			f.createNewFile();
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(content);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public String getRandomNumber(int number) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < number; i++) {
			buffer.append(String.valueOf((int) (Math.random() * 10)));
		}
		return buffer.toString();
	}
	public String getString(Object object) {
		try {
			if (!object.toString().equals("null")) {
				return object.toString();
			}

		} catch (Exception e) {
			return "";
		}
		return "";
	}

	public int getInt(Object value) {
		try {
			return Integer.parseInt(value.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	public Long getLong(Object value) {
		try {
			return Long.parseLong(value.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	public float getFloat(Object value) {
		try {
			return Float.parseFloat(value.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return 0.0f;
		}
	}

	public boolean getBoolean(String value) {
		try {
			return Boolean.valueOf(value);
		} catch (Exception e) {
			return false;
		}
	}
	public String toDate(String dateStr) {
		try {
			SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
			long loc_time = Long.valueOf(dateStr);
			return d.format(new Date(loc_time));
		} catch (Exception e) {
			return "";
		}
	}

	public String AnalystDateSlash(String dateStr) {
		try {
			SimpleDateFormat d = new SimpleDateFormat("yy/MM/dd");
			long loc_time = Long.valueOf(dateStr);
			return d.format(new Date(loc_time));
		} catch (Exception e) {
			return "";
		}
	}

	public String getDatecovertStart(long dateStr) {
		try {
			SimpleDateFormat d = new SimpleDateFormat("yy/MM/dd HH:mm");
			return d.format(new Date(dateStr));
		} catch (Exception e) {
			return "";
		}
	}

	public String getDatecovertNo(long dateStr) {
		try {
			SimpleDateFormat d = new SimpleDateFormat("yy/MM/dd");
			return d.format(new Date(dateStr));
		} catch (Exception e) {
			return "";
		}
	}

	public String getDatecovertEnd(long dateStr) {
		try {
			SimpleDateFormat d = new SimpleDateFormat("yy/MM/dd");
			return d.format(new Date(dateStr));
		} catch (Exception e) {
			return "";
		}
	}

	public String getDateSecond(String dateStr) {
		try {
			SimpleDateFormat d = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
			long loc_time = Long.valueOf(dateStr);
			return d.format(new Date(loc_time));
		} catch (Exception e) {
			return "";
		}
	}

	public String toDateRanking(String dateStr) {
		try {
			SimpleDateFormat d = new SimpleDateFormat("MM/dd");
			long loc_time = Long.valueOf(dateStr);
			return d.format(new Date(loc_time));
		} catch (Exception e) {
			return "";
		}
	}

	public String toDateStr(String dateStr) {
		try {
			SimpleDateFormat d = new SimpleDateFormat("MM/dd HH:mm");
			long loc_time = Long.valueOf(dateStr);
			return d.format(new Date(loc_time));
		} catch (Exception e) {
			return "";
		}
	}

	public String toTimeStr(String dateStr) {
		try {
			SimpleDateFormat d = new SimpleDateFormat("HH:mm");
			long loc_time = Long.valueOf(dateStr);
			return d.format(new Date(loc_time));
		} catch (Exception e) {
			return "";
		}
	}
	public String getTime(String dateStr) {
		if (dateStr != null && dateStr.trim().length() > 0) {
			String re_time = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d;
			try {
				d = sdf.parse(dateStr);
				long l = d.getTime();
				re_time = String.valueOf(l);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return re_time;
		} else {
			return null;
		}
	}
}

