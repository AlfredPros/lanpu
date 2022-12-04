package umn.ac.id.lanpu.ui.notifications;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;

import umn.ac.id.lanpu.R;
import umn.ac.id.lanpu.ui.dashboard.DashboardViewModel;

public class ChangePassword extends AppCompatActivity {

    TextView accName;
    EditText input;
    EditText inputConf;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_password);

        accName = findViewById(R.id.account_name);
        accName.setText("");
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);;
        // Live Data
        LiveData<DataSnapshot> liveData = dashboardViewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                accName.setText(name);
            }
        });

        input = findViewById(R.id.input_password);
        inputConf = findViewById(R.id.confirm_password);
        submit = findViewById(R.id.change_password_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = input.getText().toString();
                String passConf = inputConf.getText().toString();

                if (pass.equals(passConf) && !pass.equals("") && !passConf.equals("")) {
                    accName.setText("Correct!");
                    Toast.makeText(ChangePassword.this, "Password has been changed.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(ChangePassword.this, "Password is not confirmed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
