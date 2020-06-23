package xin.tapin.ywq138.logregfragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import xin.tapin.ywq138.db.MySQLite;
import xin.tapin.ywq138.MainActivity;
import xin.tapin.ywq138.R;
import xin.tapin.ywq138.base.MyApplication;
import xin.tapin.ywq138.utils.Email;

/**
 * 登录Fragment
 */
public class Fragment_log extends Fragment {
    private final LogRegActivity logRegActivity;
    private MySQLite dbHelper;
    private EditText id;
    private EditText pwd;
    private Button logButton;
    private Button resetButton;
    private Button emailLogin;
    private TextView loginType;
    private TextView loginTypeCode;
    private View log;
    private boolean email_state;

    public Fragment_log(LogRegActivity logRegActivity) {
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
        log = inflater.inflate(R.layout.activity_log, container, false);

        initView();
        //登录按钮click
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = id.getText().toString();
                String userpasswd = pwd.getText().toString();
                boolean email = MyApplication.isEmail();
                if(email){//判断当前是否是邮箱登录模式
                    String emailCode = MyApplication.getEmailCode();
                    //验证输入的验证码是否正确 不区分大小写
                    if(TextUtils.equals(emailCode,userpasswd.toUpperCase())){
                        MyApplication.setEmailCode(null);
                        MyApplication.setEmail(false);
                        Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getContext(), MainActivity.class);
                        id.setText("");
                        pwd.setText("");
                        startActivity(intent);
                        logRegActivity.finish();
                    }else{
                        Toast.makeText(getContext(), "验证码输入有误", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (userid.isEmpty()){
                        Toast.makeText(getContext(), "手机号不为空！", Toast.LENGTH_SHORT).show();
                    } else if (userid.length()!=11){
                        Toast.makeText(getContext(), "请输入正确的11位手机号！", Toast.LENGTH_SHORT).show();
                    }else if (userpasswd.isEmpty()){
                        Toast.makeText(getContext(), "密码不为空！", Toast.LENGTH_SHORT).show();
                    } else
                    if (login(userid, userpasswd)) {
                        Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getContext(), MainActivity.class);
                        id.setText("");
                        pwd.setText("");
                        startActivity(intent);
                        logRegActivity.finish();
                    } else {
                        Toast.makeText(getContext(), "登录失败，请检查用户名或密码！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        //发送验证码按钮click
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean email = MyApplication.isEmail();//当前现在登录方式
                if(email){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String toEmail = id.getText().toString();
                            String code = Email.randomChar();
                            email_state = Email.sendEmail(toEmail, code);
                            logRegActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(email_state) {
                                        Toast.makeText(logRegActivity, "发送成功", Toast.LENGTH_SHORT).show();
                                        MyApplication.setEmailCode(code);
                                    }else{
                                        Toast.makeText(logRegActivity, "发送失败请检查邮箱是否正确", Toast.LENGTH_SHORT).show();
                                        MyApplication.setEmailCode(null);
                                    }
                                }
                            });
                        }
                    }).start();
                }else{
                    Toast.makeText(logRegActivity, "当前为手机号登录模式", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //点击按钮设置切换登录模式
        emailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailLogin.getText().toString().equals("使用邮箱验证码登录")){
                    loginType.setText("邮箱地址：");
                    loginTypeCode.setText("验证码：");
                    emailLogin.setText("使用注册账号登录");
                    MyApplication.setEmail(true);
                }else{
                    loginType.setText("手机号码：");
                    loginTypeCode.setText("密    码：");
                    emailLogin.setText("使用邮箱验证码登录");
                    MyApplication.setEmail(false);
                }
            }
        });
        return log;
    }

    /**
     * 初始化布局控件
     */
    private void initView() {
        id = log.findViewById(R.id.logNum);
        pwd = log.findViewById(R.id.logPasswd);
        logButton = log.findViewById(R.id.logButton);
        resetButton = log.findViewById(R.id.resetButton);
        emailLogin = log.findViewById(R.id.emailLogin);
        loginType = log.findViewById(R.id.loginType);
        loginTypeCode = log.findViewById(R.id.loginTypeCode);
    }
    /**
     * 验证手机号码和密码
     * @param id
     * @param password
     * @return
     */
    public boolean login(String id, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from userinfo where id=? and passwd=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id, password});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }
}
