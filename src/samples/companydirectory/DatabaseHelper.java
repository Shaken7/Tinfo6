package samples.companydirectory;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/*
 * We extend SQLiteOpenHelper to manage the access to our database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "employee_directory2";

	protected Context context;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		this.context = context;
	}

	public void onCreate(SQLiteDatabase db) {
		String s;
		try {
			Toast.makeText(context, "1", 2000).show();
			InputStream in = context.getResources().openRawResource(R.raw.sql);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(in, null);
			NodeList statements = doc.getElementsByTagName("statement");
			for (int i=0; i<statements.getLength(); i++) {
				s = statements.item(i).getChildNodes().item(0).getNodeValue();
				db.execSQL(s);
			}
		} catch (Throwable t) {
			Toast.makeText(context, t.toString(), 50000).show();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS employees");
		onCreate(db);
	}
	
	/* Wanted to use it when implementing Expandable ListView. Did not work.*/
  /*
   	public Cursor fetchGroup() {
	    String query = "SELECT DISTINCT id_department FROM employee";
	    return db.rawQuery(query, null);
	}

	public Cursor fetchChildren(String department) {
	    String query = "SELECT * FROM employee WHERE id_department = '" + department + "'";
	    return db.rawQuery(query, null);
	}*/
	
}
	