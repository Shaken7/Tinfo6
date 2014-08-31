package samples.companydirectory;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CompanyDetails extends ListActivity {

	protected TextView employeeNameText;
	protected TextView addressText;
	protected List<CompanyAction> actions;
	//protected ImageView pictureView;
	protected CompanyActionAdapter adapter;
	protected int employeeId;
	protected int managerId;

	/*
	 * We build an array of actions (call, email, sms, view manager) available
	 * to user depending on the information available for the displayed company.
	 * For example, we only create a "Call mobile" action if the employee's
	 * mobile phone number is available in the database.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_details);

		employeeId = getIntent().getIntExtra("EMPLOYEE_ID", 0); // retrieve the
																// id of the
																// company.
		SQLiteDatabase db = (new DatabaseHelper(this)).getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT emp._id, emp.name,  emp.description, emp.address, emp.officePhone, emp.cellPhone, emp.email, emp.managerId, mgr.name managerName, mgr.description managerDescription FROM employee emp LEFT OUTER JOIN employee mgr ON emp.managerId = mgr._id WHERE emp._id = ?",
						new String[] { "" + employeeId }); // we use cursor to
															// retrieve company
															// details.

		if (cursor.getCount() == 1) {
			cursor.moveToFirst();

			employeeNameText = (TextView) findViewById(R.id.employeeName);
			employeeNameText.setText(cursor.getString(cursor.getColumnIndex("name")));

			addressText = (TextView) findViewById(R.id.address);
			addressText.setText(cursor.getString(cursor.getColumnIndex("address")));

			actions = new ArrayList<CompanyAction>();

			String officePhone = cursor.getString(cursor.getColumnIndex("officePhone"));
			if (officePhone != null) {
				actions.add(new CompanyAction("Call office", officePhone,
						CompanyAction.ACTION_CALL));
			}

			String cellPhone = cursor.getString(cursor.getColumnIndex("cellPhone"));
			if (cellPhone != null) {
				actions.add(new CompanyAction("Call mobile", cellPhone,
						CompanyAction.ACTION_CALL));
				actions.add(new CompanyAction("SMS", cellPhone,
						CompanyAction.ACTION_SMS));
			}

			String email = cursor.getString(cursor.getColumnIndex("email"));
			if (email != null) {
				actions.add(new CompanyAction("Email", email,
						CompanyAction.ACTION_EMAIL));
			}

			managerId = cursor.getInt(cursor.getColumnIndex("managerId"));
			if (managerId > 0) {
				actions.add(new CompanyAction("View manager", cursor
						.getString(cursor.getColumnIndex("managerName"))
						+ " "
						+ cursor.getString(cursor
								.getColumnIndex("managerDescription")),
						CompanyAction.ACTION_VIEW));
			}

			// this doesn't work (doesn't properly display the image for each company) (1)
			 /*Resources res = getResources();
			 String picture =
			 cursor.getString(cursor.getColumnIndex("picture"));
			 int id =
			 getResources().getIdentifier("samples.companydirectory.drawable/"
			 + picture, null, null);
			 pictureView.setImageDrawable(drawable);
			 Drawable drawable = res.getDrawable(id);*/

			// this doesn't work either (doesn't properly display the image for each company) (2)
			/*pictureView = (ImageView) findViewById(R.id.pictureView);
			String picture = cursor.getString(cursor.getColumnIndex("picture"));
			Context context = this;
			int id = getResources().getIdentifier(picture,
					"drawable", context.getPackageName());
			pictureView.setImageResource(id);*/

			// works but 'howard' is hardcoded. needs to be genereated dynamically. 
			//pictureView = (ImageView) findViewById(R.id.pictureView);
			//pictureView.setImageDrawable(getResources().getDrawable(R.drawable.howard));
			// description = (TextView) findViewById(R.id.description);
			// description.setText(cursor.getString(cursor.getColumnIndex("title")));

			cursor = db.rawQuery(
					"SELECT count(*) FROM employee WHERE managerId = ?",
					new String[] { "" + employeeId });
			cursor.moveToFirst();
			int count = cursor.getInt(0);
			if (count > 0) {
				actions.add(new CompanyAction("View direct reports", "("
						+ count + ")", CompanyAction.ACTION_REPORTS));
			}

			adapter = new CompanyActionAdapter();
			setListAdapter(adapter);
		}

	}

	public void onListItemClick(ListView parent, View view, int position, long id) {

		CompanyAction action = actions.get(position);

		Intent intent;
		switch (action.getType()) {

		case CompanyAction.ACTION_CALL:
			Uri callUri = Uri.parse("tel:" + action.getData());
			intent = new Intent(Intent.ACTION_CALL, callUri);
			startActivity(intent);
			break;

		case CompanyAction.ACTION_EMAIL:
			intent = new Intent(Intent.ACTION_SEND);
			intent.setType("plain/text");
			intent.putExtra(Intent.EXTRA_EMAIL,
					new String[] { action.getData() });
			startActivity(intent);
			break;

		case CompanyAction.ACTION_SMS:
			Uri smsUri = Uri.parse("sms:" + action.getData());
			intent = new Intent(Intent.ACTION_VIEW, smsUri);
			startActivity(intent);
			break;

		case CompanyAction.ACTION_REPORTS:
			intent = new Intent(this, DirectReports.class);
			intent.putExtra("EMPLOYEE_ID", employeeId);
			startActivity(intent);
			break;

		case CompanyAction.ACTION_VIEW:
			intent = new Intent(this, CompanyDetails.class);
			intent.putExtra("EMPLOYEE_ID", managerId);
			startActivity(intent);
			break;
		}
	}

	class CompanyActionAdapter extends ArrayAdapter<CompanyAction> {

		CompanyActionAdapter() {
			super(CompanyDetails.this, R.layout.action_list_item, actions);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CompanyAction action = actions.get(position);
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.action_list_item, parent, false);
			TextView label = (TextView) view.findViewById(R.id.label);
			label.setText(action.getLabel());
			TextView data = (TextView) view.findViewById(R.id.data);
			data.setText(action.getData());
			return view;
		}
	}

}