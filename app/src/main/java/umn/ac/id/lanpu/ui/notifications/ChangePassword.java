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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import umn.ac.id.lanpu.R;
import umn.ac.id.lanpu.ui.dashboard.DashboardViewModel;

public class ChangePassword extends AppCompatActivity {

    TextView accName;
    EditText input;
    EditText inputConf;
    Button submit;
    String email;

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
                email = dataSnapshot.child("email").getValue(String.class);
            }
        });

        submit = findViewById(R.id.change_password_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();


                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChangePassword.this, "Please Check Your Email", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }

}
