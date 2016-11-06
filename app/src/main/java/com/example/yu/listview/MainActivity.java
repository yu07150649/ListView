package com.example.yu.listview;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.list_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"ArrayAdpater");
        menu.add(0,2,0,"SimpleCursorAdapter");
        menu.add(0,3,0,"SimpleAdapter");
        menu.add(0,4,0,"BaseAdapter");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case 1:
                arrayAdapter();
                break;
            case 2:
                simpleCursorAdapter();
                break;
            case 3:
                simpleAdapter();
                break;
            case 4:
                baseAdapter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public  void arrayAdapter(){
        final String []weeks = {"Sun","Mon","Tues","Wen","Thus","Fri","Sat"};
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,weeks);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,weeks[position],Toast.LENGTH_LONG).show();
        }
        });
    }
    public void simpleCursorAdapter(){
        //读取联系人
        final Cursor mCursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        startManagingCursor(mCursor);
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_expandable_list_item_1,mCursor,new String[]{ContactsContract.Contacts.DISPLAY_NAME},new int[]{android.R.id.text1});
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),Toast.LENGTH_LONG);
            }
        });
    }
    public void simpleAdapter(){
        final List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("title","G1");
        map.put("info","6661");
        map.put("img",R.drawable.icon1);
        list.add(map);

        map = new HashMap<String,Object>();
        map.put("title","G2");
        map.put("info","6662");
        map.put("img",R.drawable.icon1);
        list.add(map);

        SimpleAdapter sAdapter = new SimpleAdapter(this,list,R.layout.simpleadapter,new String[]{"img","title","info"},new int[]{R.id.imageView,R.id.ltitle,R.id.linfo});
        lv.setAdapter(sAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,list.get(position).get("title").toString(),Toast.LENGTH_LONG).show();
            }
        });

    }
    static class VierHoler{
        public ImageView img;
        public TextView title;
        public TextView info;
        public Button btn;
        public LinearLayout layout;
    }
    public void baseAdapter(){
        class MyBaseAdapter extends BaseAdapter{
            private List<Map<String,Object>> data;
            private Context context;
            private LayoutInflater myLayoutInflater;
            public MyBaseAdapter(Context context,List<Map<String,Object>> data ){
                super();
                this.data=data;
                this.context = context;
                this.myLayoutInflater = LayoutInflater.from(context);
            }
            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int position) {
                return data.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                VierHoler holder = null;
                if(convertView == null){
                    holder = new VierHoler();
                    convertView = myLayoutInflater.inflate(R.layout.li,parent,false);
                    holder.img = (ImageView) convertView.findViewById(R.id.img);
                    holder.title= (TextView) convertView.findViewById(R.id.title);
                    holder.info= (TextView) convertView.findViewById(R.id.info);
                    holder.btn= (Button) convertView.findViewById(R.id.btn);
                    holder.layout = (LinearLayout) convertView.findViewById(R.id.li);
                    convertView.setTag(holder);
                }else{
                    holder = (VierHoler) convertView.getTag();
                }
                holder.img.setImageResource(Integer.parseInt(data.get(position).get("img").toString()));
                holder.title.setText(data.get(position).get("title").toString());
                holder.info.setText(data.get(position).get("info").toString());
                if(position%2==1){
                    holder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
                }else{
                    holder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
                }
                //添加button点击事件
                holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"按钮点击"+position,Toast.LENGTH_SHORT).show();
                    }
                });
                return convertView;
            }
        }
        final List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("title","G1");
        map.put("info","1");
        map.put("img",R.drawable.icon1);
        list.add(map);
        MyBaseAdapter myBaseAdapter =new MyBaseAdapter(this,list);
        lv.setAdapter(myBaseAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,list.get(position).get("title").toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
