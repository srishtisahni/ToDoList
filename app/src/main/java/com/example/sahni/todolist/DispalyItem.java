package com.example.sahni.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DispalyItem extends AppCompatActivity {
    Bundle bundle;
    int id;
    ListItem item;
    Intent intent;
    ItemOpenHelper openHelper=ItemOpenHelper.getInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispaly_item);
        intent=getIntent();
        bundle=intent.getExtras();
        id=bundle.getInt(Constant.ID_KEY);
        setDataBar();
    }

    private void setDataBar() {
        SQLiteDatabase database=openHelper.getReadableDatabase();
        Cursor cursor=database.query(Contract.ItemList.TABLE_NAME,null,Contract.ItemList.ID+"=?", new String[]{id + ""},null,null,null);
        cursor.moveToNext();
        item=new ListItem(cursor.getString(cursor.getColumnIndex(Contract.ItemList.ITEM)),
                cursor.getInt(cursor.getColumnIndex(Contract.ItemList.DEADLINE)),
                cursor.getInt(cursor.getColumnIndex(Contract.ItemList.ID)));
        LinearLayout dataBar=findViewById(R.id.dataBar);
        TextView ToDo=dataBar.findViewById(R.id.todo);
        TextView Description=dataBar.findViewById(R.id.description);
        TextView Date=dataBar.findViewById(R.id.date);
        ToDo.setText(item.getItemName());
        Description.setText(cursor.getString(cursor.getColumnIndex(Contract.ItemList.DESCRIPTION)));
        Date.setText(item.getDeadLine());
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        menu.add(Menu.NONE,Constant.MenuID.EDIT,Menu.NONE,"EDIT");
        MenuItem edit=menu.findItem(Constant.MenuID.EDIT);
        edit.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==Constant.MenuID.EDIT)
        {
            Intent intent = new Intent(this, AddItem.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, Constant.REQUEST_EDIT);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Constant.RSULT_EDIT)
        {
            intent.putExtras(bundle);
            setResult(Constant.RSULT_EDIT,intent);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        setResult(-1);
        super.onBackPressed();
    }
}