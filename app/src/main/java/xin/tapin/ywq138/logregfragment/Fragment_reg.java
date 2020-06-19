package xin.tapin.ywq138.logregfragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import xin.tapin.ywq138.DB.MySQLite;
import xin.tapin.ywq138.R;

/**
 * 注册Fragment
 */
public class Fragment_reg extends Fragment {
    private final LogRegActivity logRegActivity;
    private MySQLite dbHelper;

    public Fragment_reg(LogRegActivity logRegActivity) {
        this.logRegActivity = logRegActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new MySQLite(getContext(), "userinfo.db", null, 1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View reg = inflater.inflate(R.layout.activity_reg, container, false);
        //注册按钮click
        reg.findViewById(R.id.regButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText editText1 = getView().findViewById(R.id.regName);
                EditText editText2 = getView().findViewById(R.id.regNum);
                EditText editText3 = getView().findViewById(R.id.regPasswd);
                String regname = editText1.getText().toString();
                String regnum = editText2.getText().toString();
                String regpasswd = editText3.getText().toString();
                if (regname.isEmpty()){
                    Toast.makeText(getContext(), "用户名不为空！", Toast.LENGTH_SHORT).show();
                } else if (regnum.length()!=11){
                    Toast.makeText(getContext(), "请输入正确的11位手机号！", Toast.LENGTH_SHORT).show();
                } else if (regpasswd.isEmpty()){
                    Toast.makeText(getContext(), "密码不为空！", Toast.LENGTH_SHORT).show();
                }else if (checkId(regnum)) {
                    Toast.makeText(getContext(), "该手机号码已被注册！", Toast.LENGTH_SHORT).show();
                } else if (checkName(regname)){
                    Toast.makeText(getContext(), "该用户名已被注册！", Toast.LENGTH_SHORT).show();
                } else{
                    if (register(regname, regnum,regpasswd)) {
                        Toast.makeText(getContext(), "注册成功！", Toast.LENGTH_SHORT).show();
                        editText1.setText("");
                        editText2.setText("");
                        editText3.setText("");
                    }
                }
            }
        });
        //重置按钮click
        reg.findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText1 = getView().findViewById(R.id.regName);
                EditText editText2 = getView().findViewById(R.id.regNum);
                EditText editText3 = getView().findViewById(R.id.regPasswd);
                editText1.setText("");
                editText2.setText("");
                editText3.setText("");
            }
        });
        return reg;
    }

    /**
     * 插入用户数据
     * @param name
     * @param id
     * @param passwd
     * @return
     */
    public boolean register(String name, String id, String passwd) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("id", id);
        values.put("passwd", passwd);
        db.insert("userinfo", null, values);
        db.close();
        return true;
    }

    /**
     * 判断用户名是否存在
     * @param name
     * @return
     */
    public boolean checkName(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String Query = "Select * from userinfo where name =?";
        Cursor cursor = db.rawQuery(Query, new String[]{name});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    /**
     * 判断电话号码是否存在
     * @param id
     * @return
     */
    public boolean checkId(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String Query = "Select * from userinfo where id =?";
        Cursor cursor = db.rawQuery(Query, new String[]{id});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
}
