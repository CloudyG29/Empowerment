package com.nullandvoid.empowerment.ui.dashboard;

import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nullandvoid.empowerment.Menu;
import com.nullandvoid.empowerment.R;
import com.nullandvoid.empowerment.ui.home.RequestUserAdapter;

public class ConfirmEmp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Menu.hideProgressBar();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_emp);
        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(view -> {
            finish(); // Close ConfirmEmp and return to HomeFragment
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            String name=getIntent().getStringExtra("name");
            String surname=getIntent().getStringExtra("surname");
            String quantity=getIntent().getStringExtra("quantity");
            String biography=getIntent().getStringExtra("biography");
            String itemName =getIntent().getStringExtra("selectedItem");

            TextView name_of=findViewById(R.id.name_of);
            name_of.setText(name+" "+surname);
            TextView quantity_of=findViewById(R.id.quantity_of);
            quantity_of.setText(itemName+": "+ quantity);
            TextView bio_of=findViewById(R.id.bio_of);
            bio_of.setText("Biography:\n\n"+ biography);


            return insets;



        });
    }
}