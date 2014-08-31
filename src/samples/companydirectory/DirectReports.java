package samples.companydirectory;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DirectReports extends ListActivity {

	protected Cursor cursor=null;
	protected ListAdapter adapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
        setContentView(R.layout.direct_reports);

        SQLiteDatabase db = (new DatabaseHelper(this)).getWritableDatabase();

        int employeeId = getIntent().getIntExtra("EMPLOYEE_ID", 0);

        Cursor cursor = db.rawQuery("SELECT _id, name, address FROM employee WHERE _id = ?", 
				new String[]{""+employeeId});

        if (cursor.getCount() != 1)
        {
        	return;
        }

        cursor.moveToFirst();

        TextView employeeNameText = (TextView) findViewById(R.id.name);
	    employeeNameText.setText(cursor.getString(cursor.getColumnIndex("name")));

        TextView addressText = (TextView) findViewById(R.id.address);
	    addressText.setText(cursor.getString(cursor.getColumnIndex("address")));
        
        cursor = db.rawQuery("SELECT _id, name, description,  address,  officePhone, cellPhone, email FROM employee WHERE managerId = ?", 
				new String[]{""+employeeId});
		adapter = new SimpleCursorAdapter(
				this, 
				R.layout.company_list_item, 
				cursor, 
				new String[] {"name", "address"}, 
				new int[] {R.id.name, R.id.address}, 0);
		setListAdapter(adapter);
    }
    
    public void onListItemClick(ListView parent, View view, int position, long id) {
    	Intent intent = new Intent(this, CompanyDetails.class);
    	Cursor cursor = (Cursor) adapter.getItem(position);
    	intent.putExtra("EMPLOYEE_ID", cursor.getInt(cursor.getColumnIndex("_id")));
    	startActivity(intent);
    }
    
}