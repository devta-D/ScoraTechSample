package devta.scoratechsample.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moxun.tagcloudlib.view.TagCloudView;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import devta.scoratechsample.Adapter.SkillTagAdapter;
import devta.scoratechsample.Utility.CircleTransform;
import devta.scoratechsample.R;
import gun0912.tedbottompicker.TedBottomPicker;

public class LogIn extends AppCompatActivity
        implements View.OnClickListener, DatePickerDialog.OnDateSetListener, View.OnFocusChangeListener{

    EditText input_fname, input_lname;
    final int PERMISSION_REQUEST_CODE = 101;
    final int REQUEST_PERMISSION_SETTING = 102;
    FlowLayout skillTagLayout;
    TextView dobView;

    String[] skills = new String[]{
        "Android","Accounting","Active Listening", "Administrative","Analytical","Behavioral"
            ,"Business Intelligence","Business","Business Storytelling","Clerical","iOS", "Ruby On Rails",
            "Collaboration","Communication","Computer"," Conceptual","Consulting","Delegation", "Content Strategy",
            "Creative Thinking","Customer Service","Decision Making"," Digital Marketing","Deductive Reasoning",
            "Delegation", "Digital Media"," Editing",};

    List<String> userSkillSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeNoActionBar);
        setContentView(R.layout.activity_login);

        input_fname = findViewById(R.id.input_fname);
        input_lname = findViewById(R.id.input_lname);
        dobView = findViewById(R.id.chooseDOBView);

        input_fname.setOnFocusChangeListener(this);
        input_lname.setOnFocusChangeListener(this);

        skillTagLayout = findViewById(R.id.skillTagLayout);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(view.hasFocus()){
            EditText editText = (EditText)view;
            editText.setError(null);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.userPhotoView:
                pickImage();
                break;

            case R.id.btn_login:
                logInIfValid();
                break;

            case R.id.addSkillView:
                initializeSkillsPicker();
                break;

            case R.id.chooseDOBView:
                initializeDatePicker();
                break;

            case R.id.link_signup:
                Toast.makeText(this,"to sign-up", Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(this, "under_development", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void initializeDatePicker(){

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void initializeSkillsPicker(){

        final Dialog skillPickerDialog = new Dialog(this);
        skillPickerDialog.setContentView(R.layout.dialog_skills);
        skillPickerDialog.setCanceledOnTouchOutside(true);

        if(skillPickerDialog.getWindow()!=null){
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(skillPickerDialog.getWindow().getAttributes());
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            skillPickerDialog.getWindow().setBackgroundDrawable(null);
            skillPickerDialog.getWindow().setAttributes(lp);
        }

        final FlowLayout skillDialogTagLayout = skillPickerDialog.findViewById(R.id.skillDialogTagLayout);

        //This is a third party library used to show skills in a tag cloud.
        TagCloudView skillsCloudView = skillPickerDialog.findViewById(R.id.skillsCloud);
        if(skillsCloudView!=null){
            skillsCloudView.setBackgroundColor(Color.WHITE);
            skillsCloudView.setAutoScrollMode(TagCloudView.MODE_DISABLE);
            skillsCloudView.setAdapter(new SkillTagAdapter(skills));

            skillsCloudView.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
                @Override
                public void onItemClick(ViewGroup parent, View view, final int position) {
                    if(!userSkillSet.contains(skills[position])){
                        final TextView skillTagView = (TextView)LayoutInflater.from(LogIn.this)
                                .inflate(R.layout.view_skill_tag, skillTagLayout, false);
                        skillTagView.setText(skills[position]);
                        skillTagView.setTag(position);
                        skillTagView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                skillDialogTagLayout.removeView(view);
                                userSkillSet.remove(skills[position]);
                            }
                        });
                        skillDialogTagLayout.addView(skillTagView);
                        userSkillSet.add(skills[position]);
                    }else {
                        Toast.makeText(LogIn.this, skills[position]+" already selected", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        TextView doneView = skillPickerDialog.findViewById(R.id.viewDialogDone);
        TextView cancelView = skillPickerDialog.findViewById(R.id.viewDialogCancel);

        doneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skillPickerDialog.cancel();
                skillTagLayout.removeAllViews();
                for(final String skill : userSkillSet){
                    TextView skillTagView = (TextView)LayoutInflater.from(LogIn.this)
                            .inflate(R.layout.view_skill_tag, skillTagLayout, false);

                    skillTagView.setText(skill);
                    skillTagLayout.addView(skillTagView);
                    skillTagView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            userSkillSet.remove(skill);
                            skillTagLayout.removeView(view);
                        }
                    });
                }
            }
        });

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skillPickerDialog.cancel();
            }
        });

        skillPickerDialog.show();

    }

    private void logInIfValid(){

        String inputFname = input_fname.getText().toString().trim();
        String inputLname = input_lname.getText().toString().trim();

        if(inputFname.length()>0){

            if(inputLname.length()>0){

                String loginResponse = "First Name : ".concat(inputFname)
                        .concat("\n").concat("Last Name : ").concat(inputLname);

                Toast.makeText(this, loginResponse, Toast.LENGTH_LONG).show();

            }else {
                input_lname.setError("Enter a valid first name");
            }

        }else {
            input_fname.setError("Enter a valid last name");
        }
    }

    private void pickImage(){

        //>=Marshmallow(23) needs app to request run-time permissions to use some features.
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkPermission()) {

                //permission to use Camera is already granted, now pick image.
                initializePhotoPicker();

            } else {
                //App can still ask user for permission.
                requestPermission();
            }
        }
    }

    private void initializePhotoPicker(){

        //This is a third party library I mostly use for picking/capturing images in application,
        // it makes me to spend less time on boilerplate code and helps me to focus in areas where it is needed.
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(this)
                .showCameraTile(true)
                .showTitle(false)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {

                        int dimenInPx = (int)getResources().getDimension(R.dimen.user_image_size_in_px);

                        Glide.with(LogIn.this)
                                .load(uri)
                                .override(dimenInPx, dimenInPx)
                                .transform(new CircleTransform(LogIn.this))
                                .placeholder(R.drawable.ic_user_default)
                                .into((ImageView)findViewById(R.id.userPhotoView));
                    }
                })
                .create();

        tedBottomPicker.show(getSupportFragmentManager());

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result==PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1]==PackageManager.PERMISSION_GRANTED) {

                    initializePhotoPicker();

                } else if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_DENIED
                        ||grantResults[1]==PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "Permission denied to use Camera", Toast.LENGTH_SHORT).show();

                    if (Build.VERSION.SDK_INT >= 23&&
                            !shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        //user ticked never ask again in previous run, show the last option to use camera.
                        new AlertDialog.Builder(this)
                                .setTitle("Alert")
                                .setMessage("Looks like you have disabled permission for using the camera, " +
                                        "Enable it from settings to use the camera.")
                                .setCancelable(true)
                                .setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQUEST_PERMISSION_SETTING){

            //to re-check whether the user have enabled the permissions or not in Settings.
            pickImage();

        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        dobView.setText(day+"-"+month+"-"+year);
    }
}
