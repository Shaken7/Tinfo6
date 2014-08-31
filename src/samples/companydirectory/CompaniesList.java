package samples.companydirectory;
	
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.ListView;
/*
 * Default activity of the application.
 * main.xml - layout for the default activity of the application.
 */
public class CompaniesList extends ListActivity {

	protected EditText searchText;
	protected SQLiteDatabase db;
	protected Cursor cursor;
	protected ListAdapter adapter;	
	/*protected ExpandableListView expListView;
	protected ExpandableListAdapter listAdapter;
	protected int employeeId;
	List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;*/


	/*
	 * We use the database helper class to open the database.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		searchText = (EditText) findViewById(R.id.searchText);
		
		// get the listView
		// expListView = (ExpandableListView) findViewById(R.id.list);
		
		// open the database
		db = (new DatabaseHelper(this)).getWritableDatabase();
		
		// preparing list data
		// prepareListData();
		
		// listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
		
		// setting list adapter
		// expListView.setAdapter(listAdapter);
	}
		
		
	
		/*expListView.setOnGroupClickListener(new OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                Toast.makeText(getApplicationContext(),
                 "Group Clicked " + listDataHeader.get(groupPosition),
                Toast.LENGTH_SHORT).show();
                return false;
            }
        });
		
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
		            
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        Toast.LENGTH_SHORT).show();
                listDataHeader.get(groupPosition) + " Expanded",
            }
        });
		
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			 
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();
 
            }
        });
		
		expListView.setOnChildClickListener(new OnChildClickListener() {
			 
            
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
	}*/

	/*
	 * We create a new intent for CompanyDetails class, add the employee id to
	 * the intent using intent.putExtra() and start a new activity.
	 */	
	public void onListItemClick(ListView parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, CompanyDetails.class);
		Cursor cursor = (Cursor) adapter.getItem(position);
		intent.putExtra("EMPLOYEE_ID", cursor.getInt(cursor.getColumnIndex("_id")));
		startActivity(intent);
	}

	/*
	 * We use a Cursor to query the database. We then use a SimpleCursorAdapter
	 * to bind the ListView to the Cursor using a custom layout
	 * (company_list_item) to display each item in the list.
	 */
	public void search(View view) {
		// || is the concatenation operation in SQLite
		cursor = db.rawQuery(
						"SELECT _id, name, address FROM employee WHERE name LIKE ?",
						new String[] { "%" + searchText.getText().toString() + "%" });
	 	adapter = new SimpleCursorAdapter(this, R.layout.company_list_item,
				cursor, new String[] { "name", "address" }, new int[] {
						R.id.name, R.id.address }, 0);
		setListAdapter(adapter);
	}
	
	  /*
	   * Preparing the list data
	   */
	 /* private void prepareListData() {
	    listDataHeader = new ArrayList<String>();
	    listDataChild = new HashMap<String, List<String>>();

	    // Adding child data
	    listDataHeader.add("Top 250");
	    listDataHeader.add("Now Showing");
	    listDataHeader.add("Coming Soon..");
	    listDataHeader.add("Category4");
	    listDataHeader.add("Category5");
	    listDataHeader.add("Category6");
	    listDataHeader.add("Category7");
	    listDataHeader.add("Category8");
	    listDataHeader.add("Category9");
	    listDataHeader.add("Category10");
	    listDataHeader.add("Category11");
	    listDataHeader.add("Category12");
	    listDataHeader.add("Category13");
	    listDataHeader.add("Category14");


	    // Adding child data
	    List<String> top250 = new ArrayList<String>();
	    cursor = db.rawQuery(
				"SELECT _id, name, address FROM employee WHERE name LIKE ? AND department = 'top250'",
				new String[] { "" + employeeId });
	    for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
	        // The Cursor is now set to the right position
	        top250.add(cursor.getString(cursor.getColumnIndex("department")));
	    }	   
	    

	    listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
	    
	  }*/

}

	